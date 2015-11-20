package it.playfellas.superapp.logic.master;

import android.util.Log;

import com.firebase.client.Firebase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by affo on 29/07/15.
 */
public class GameHistory {
    private static final String TAG = GameHistory.class.getSimpleName();
    private final String STAGE_PREFIX = "stage_";
    private final String PLAYER_PREFIX = "player_";
    private final String FRACTION_FMT = "%d-%d"; // NB: Keys must not contain '/', '.', '#', '$', '[', or ']'
    private DateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS", Locale.ITALY);

    @Getter
    private String gameID;
    private static final int NO_STAGE_FRACTIONS = 3;
    private List<Integer> stagePtrs;
    private List<Record> history;
    private Set<String> players;
    private Date startDate;
    private Date lastAnswerDate;

    private Firebase fbRef;

    public GameHistory(Firebase fbRef) {
        this.gameID = UUID.randomUUID().toString().substring(0, 8);
        this.fbRef = fbRef.child(gameID);

        this.history = new ArrayList<>();
        this.stagePtrs = new ArrayList<>();
        this.players = new HashSet<>();
        this.startDate = new Date();
        this.lastAnswerDate = this.startDate;
    }

    private Record newRecord(String player, boolean rw) {
        Record r = new Record(player, rw);
        try {
            this.lastAnswerDate = dateFmt.parse(r.getTs());
        } catch (ParseException e) {
            Log.d(TAG, "Error in parsing Date", e);
        }
        return r;
    }

    public void right(String k) {
        this.players.add(k);
        history.add(newRecord(k, true));
    }

    public void wrong(String k) {
        this.players.add(k);
        history.add(newRecord(k, false));
    }

    public void endStage() {
        this.stagePtrs.add(history.size());
    }

    public void save() {
        Data data = new Data();

        data.setIndex1_elapsedTime(elapsedTime());
        data.setIndex4_noRightPerStage(noRWPerStage(true));
        data.setIndex2_noWrongPerStage(noRWPerStage(false));
        data.setIndex5_noRight(noRW(true));
        data.setIndex3_noWrong(noRW(false));
        data.setIndex9_noRightPerPlayerPerStage(noRWPerPlayerPerStage(true));
        data.setIndex11_noWrongPerPlayerPerStage(noRWPerPlayerPerStage(false));
        data.setIndex10_noRightPerPlayer(noRWPerPlayer(true));
        data.setIndex12_noWrongPerPlayer(noRWPerPlayer(false));
        data.setIndex6_playerContributionPerStage(playerContributionPerStage());
        data.setIndex7_balancePerStage(balancePerStage());
        data.setIndex8_playerContributionStabilityPerStage(playerContributionStabilityPerStage());
        data.setHistory(history);
        data.setRatios();

        fbRef.setValue(data);
    }

    private int noStages() {
        return stagePtrs.size();
    }

    private List<Record> getStage(int stageNumber) {
        int start = 0;
        if (stageNumber > 0) {
            start = stagePtrs.get(stageNumber - 1);
        }
        int end = stagePtrs.get(stageNumber);

        return this.history.subList(start, end);
    }

    private List<List<Record>> getStageFractions(List<Record> stage, int length) {
        int fractionSize = stage.size() / length;
        List<List<Record>> fractions = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int start = fractionSize * i;
            int end = fractionSize * (i + 1);
            if ((stage.size() - end) < fractionSize) {
                end = stage.size(); // include last clicks in last fraction
            }
            fractions.add(stage.subList(start, end));
        }
        return fractions;
    }

    private String getStageKey(int noStage) {
        return STAGE_PREFIX + (noStage + 1);
    }

    private String getPlayerKey(String player) {
        return PLAYER_PREFIX + player;
    }

    private String getFractionKey(int fraction, int noFractions) {
        return String.format(FRACTION_FMT, fraction + 1, noFractions);
    }

    private <E> HashMap<String, E> toFractionMap(List<E> l) {
        HashMap<String, E> res = new HashMap<>();
        int noFractions = l.size();
        for (int i = 0; i < noFractions; i++) {
            res.put(getFractionKey(i, noFractions), l.get(i));
        }
        return res;
    }

    // 1- Tempo complessivo di gioco
    private double elapsedTime() {
        Date d = new Date();
        return ((double) (d.getTime() - startDate.getTime())) / 1000 / 60; // from ms to minutes
    }

    // 2- Numero errori complessivo per ogni manche
    // 4- Numero risposte esatte complessive per ogni manche
    private HashMap<String, Integer> noRWPerStage(boolean rw) {
        HashMap<String, Integer> res = new HashMap<>();

        for (int i = 0; i < noStages(); i++) {
            List<Record> stage = getStage(i);
            int count = 0;
            for (Record r : stage) {
                if (r.isRw() == rw) {
                    count++;
                }
            }
            res.put(getStageKey(i), count);
        }

        return res;
    }

    // 3- Numero errori complessivo per tutta la partita
    // 5- Numero risposte esatte complessive per tutta la partita
    private int noRW(boolean rw) {
        int count = 0;
        for (Record r : history) {
            if (r.isRw() == rw) {
                count++;
            }
        }
        return count;
    }

    /*
    6- Tracciare per ogni manche l’ordine di partecipazione dei 4 giocatori espresso come sequenza di interventi
    (es. a-b-a-a-c-d-c-a…)
    e dividere la sequenza in 3 parti uguali;
    per ogni parte (cioè per ogni terzo), calcolare il rapporto di ogni intervento (es. a)
    rispetto al totale degli interventi (es. 2 interventi di a su un totale di 5 interventi in quel terzo);
    */
    private HashMap<String, HashMap<String, HashMap<String, Double>>> playerContributionPerStage() {
        HashMap<String, HashMap<String, HashMap<String, Double>>> res = new HashMap<>();

        for (String p : players) {
            HashMap<String, HashMap<String, Double>> perPlayerMap = new HashMap<>();

            for (int i = 0; i < noStages(); i++) {
                List<Record> stage = getStage(i);
                List<List<Record>> fractions = getStageFractions(stage, NO_STAGE_FRACTIONS);
                List<Double> ratios = new ArrayList<>();

                for (int j = 0; j < fractions.size(); j++) {
                    List<Record> fraction = fractions.get(j);
                    int count = 0;
                    for (Record r : fraction) {
                        if (r.getPlayer().equals(p)) {
                            count++;
                        }
                    }
                    ratios.add(((double) count) / fraction.size());
                }

                perPlayerMap.put(getStageKey(i), toFractionMap(ratios));
            }

            res.put(getPlayerKey(p), perPlayerMap);
        }

        return res;
    }

    /*
    7- Calcolare per ogni terzo la differenza tra ogni coppia di giocatori (a-b; a-c; a-d; b-c; b-d ecc.)
    in valore assoluto e calcolare  la media di tutte le differenze
    (è un indicatore del bilanciamento degli interventi tra giocatori: quanto più bilanciato, tanto più vicina a zero)
    */
    private HashMap<String, HashMap<String, Double>> balancePerStage() {
        HashMap<String, HashMap<String, Double>> res = new HashMap<>();

        for (int i = 0; i < noStages(); i++) {
            List<Record> stage = getStage(i);
            List<List<Record>> fractions = getStageFractions(stage, NO_STAGE_FRACTIONS);
            List<Double> avgs = new ArrayList<>();

            for (int j = 0; j < fractions.size(); j++) {
                List<Record> fraction = fractions.get(j);
                List<Integer> perPlayer = new ArrayList<>();

                for (String p : players) {
                    int count = 0;
                    for (Record r : fraction) {
                        if (r.getPlayer().equals(p)) {
                            count++;
                        }
                    }
                    perPlayer.add(count);
                }

                double avg = 0;
                for (int k = 0; k < perPlayer.size(); k++) {
                    for (int t = i + 1; t < perPlayer.size(); t++) {
                        avg += Math.abs(perPlayer.get(k) - perPlayer.get(t));
                    }
                }

                avg /= perPlayer.size();
                avgs.add(avg);
            }

            res.put(getStageKey(i), toFractionMap(avgs));
        }

        return res;
    }

    /*
    8- calcolare per ogni giocatore la differenza fra i tre terzi
    (primo meno secondo, secondo meno terzo, primo meno terzo)
    in valore assoluto e calcolare la media delle tre differenze
    (è un indicatore della stabilità di partecipazione di ogni giocatore: quanto più stabile, tanto più vicino a zero)
    */
    private HashMap<String, HashMap<String, Double>> playerContributionStabilityPerStage() {
        HashMap<String, HashMap<String, Double>> res = new HashMap<>();

        for (String p : players) {
            HashMap<String, Double> perPlayerMap = new HashMap<>();

            for (int i = 0; i < noStages(); i++) {
                List<Record> stage = getStage(i);
                List<List<Record>> fractions = getStageFractions(stage, NO_STAGE_FRACTIONS);
                List<Integer> perFraction = new ArrayList<>();

                for (int j = 0; j < fractions.size(); j++) {
                    List<Record> fraction = fractions.get(j);
                    int count = 0;
                    for (Record r : fraction) {
                        if (r.getPlayer().equals(p)) {
                            count++;
                        }
                    }
                    perFraction.add(count);
                }

                double avg = 0;
                for (int k = 0; k < perFraction.size(); k++) {
                    for (int t = i + 1; t < perFraction.size(); t++) {
                        avg += Math.abs(perFraction.get(k) - perFraction.get(t));
                    }
                }

                avg /= perFraction.size();
                perPlayerMap.put(getStageKey(i), avg);
            }

            res.put(getPlayerKey(p), perPlayerMap);
        }

        return res;
    }

    // 9- Numero clic esatti in ogni manche
    // 11- Numero clic errati in ogni manche
    private HashMap<String, HashMap<String, Integer>> noRWPerPlayerPerStage(boolean rw) {
        HashMap<String, HashMap<String, Integer>> res = new HashMap<>();

        for (String p : players) {
            HashMap<String, Integer> perPlayerMap = new HashMap<>();

            for (int i = 0; i < noStages(); i++) {
                List<Record> stage = getStage(i);
                int count = 0;
                for (Record r : stage) {
                    if (r.getPlayer().equals(p) && r.isRw() == rw) {
                        count++;
                    }
                }
                perPlayerMap.put(getStageKey(i), count);
            }

            res.put(getPlayerKey(p), perPlayerMap);
        }

        return res;
    }

    // 10- Numero di clic esatti totali per tutte le manche giocate
    // 12- Numero di clic errati totali per tutte le manche giocate
    private HashMap<String, Integer> noRWPerPlayer(boolean rw) {
        HashMap<String, Integer> res = new HashMap<>();
        for (String p : players) {
            int count = 0;
            for (Record r : history) {
                if (r.getPlayer().equals(p) && r.isRw() == rw) {
                    count++;
                }
            }
            res.put(getPlayerKey(p), count);
        }
        return res;
    }

    private class Record {
        @Getter
        private String player;
        @Getter
        private boolean rw;
        @Getter
        private String ts;
        @Getter
        private double deltaT;

        public Record() {
            this.player = PLAYER_PREFIX + "UNKNOWN";
            this.rw = false;
            initDate();
        }

        private Record(String player, boolean rw) {
            this.player = player;
            this.rw = rw;
            initDate();
        }

        private void initDate() {
            Date d = new Date();
            this.ts = dateFmt.format(d);
            this.deltaT = ((double) (d.getTime() - lastAnswerDate.getTime())) / 1000; // ms -> s
        }
    }

    // the data that will be saved to fb
    private class Data {
        // the complete history
        @Getter
        @Setter
        private List<Record> history;
        // 1- Tempo complessivo di gioco
        @Getter
        @Setter
        private double index1_elapsedTime; // in minutes
        // 2- Numero errori complessivo per ogni manche
        @Getter
        @Setter
        private HashMap<String, Integer> index2_noWrongPerStage;
        // 3- Numero errori complessivo per tutta la partita
        @Getter
        @Setter
        private int index3_noWrong;
        // 4- Numero risposte esatte complessive per ogni manche
        @Getter
        @Setter
        private HashMap<String, Integer> index4_noRightPerStage;
        // 5- Numero risposte esatte complessive per tutta la partita
        @Getter
        @Setter
        private int index5_noRight;

        /*
        6- Tracciare per ogni manche l’ordine di partecipazione dei 4 giocatori espresso come sequenza di interventi
        (es. a-b-a-a-c-d-c-a…)
        e dividere la sequenza in 3 parti uguali;
        per ogni parte (cioè per ogni terzo), calcolare il rapporto di ogni intervento (es. a)
        rispetto al totale degli interventi (es. 2 interventi di a su un totale di 5 interventi in quel terzo);
        */
        @Getter
        @Setter
        private HashMap<String, HashMap<String, HashMap<String, Double>>> index6_playerContributionPerStage;
        /*
        7- Calcolare per ogni terzo la differenza tra ogni coppia di giocatori (a-b; a-c; a-d; b-c; b-d ecc.)
        in valore assoluto e calcolare  la media di tutte le differenze
        (è un indicatore del bilanciamento degli interventi tra giocatori: quanto più bilanciato, tanto più vicina a zero)
        */
        @Getter
        @Setter
        private HashMap<String, HashMap<String, Double>> index7_balancePerStage;
        /*
        8- calcolare per ogni giocatore la differenza fra i tre terzi
        (primo meno secondo, secondo meno terzo, primo meno terzo)
        in valore assoluto e calcolare la media delle tre differenze
        (è un indicatore della stabilità di partecipazione di ogni giocatore: quanto più stabile, tanto più vicino a zero)
        */
        @Getter
        @Setter
        private HashMap<String, HashMap<String, Double>> index8_playerContributionStabilityPerStage;

        // 9- Numero clic esatti in ogni manche
        @Getter
        @Setter
        private HashMap<String, HashMap<String, Integer>> index9_noRightPerPlayerPerStage;
        // 10- Numero di clic esatti totali per tutte le manche giocate
        @Getter
        @Setter
        private HashMap<String, Integer> index10_noRightPerPlayer;
        // 11- Numero clic errati in ogni manche
        @Getter
        @Setter
        private HashMap<String, HashMap<String, Integer>> index11_noWrongPerPlayerPerStage;
        // 12- Numero di clic errati totali per tutte le manche giocate
        @Getter
        @Setter
        private HashMap<String, Integer> index12_noWrongPerPlayer;
        // 13- Rapporto tra 9 e 11
        @Getter
        private HashMap<String, HashMap<String, Double>> index13_ratio9_11;
        // 14- Rapporto tra 10 e 12
        @Getter
        private HashMap<String, Double> index14_ratio10_12;
        // 15- Rapporto tra (10+12)  e (2+3)
        // I think they mean (10+12) and (3+5)
        @Getter
        private HashMap<String, Double> index15_clicksRatio;

        // to be called after setting all other values
        public void setRatios() {
            if (index9_noRightPerPlayerPerStage == null
                    || index11_noWrongPerPlayerPerStage == null
                    || index10_noRightPerPlayer == null
                    || index12_noWrongPerPlayer == null) {
                return;
            }

            HashMap<String, HashMap<String, Double>> r1 = new HashMap<>();

            for (String p : players) {
                String playerKey = getPlayerKey(p);
                HashMap<String, Double> perStageMap = new HashMap<>();

                for (int i = 0; i < noStages(); i++) {
                    String stageKey = getStageKey(i);
                    int right = index9_noRightPerPlayerPerStage.get(playerKey).get(stageKey);
                    int wrong = index11_noWrongPerPlayerPerStage.get(playerKey).get(stageKey);
                    perStageMap.put(stageKey, ((double) right) / wrong);
                }

                r1.put(playerKey, perStageMap);
            }

            this.index13_ratio9_11 = r1;


            HashMap<String, Double> r2 = new HashMap<>();
            HashMap<String, Double> r3 = new HashMap<>();
            int threePlusFive = index3_noWrong + index5_noRight;

            for (String p : players) {
                String playerKey = getPlayerKey(p);
                int right = index10_noRightPerPlayer.get(playerKey);
                int wrong = index12_noWrongPerPlayer.get(playerKey);
                r2.put(playerKey, ((double) right) / wrong);
                r3.put(playerKey, ((double) (right + wrong)) / threePlusFive);
            }

            this.index14_ratio10_12 = r2;
            this.index15_clicksRatio = r3;
        }
    }
}
