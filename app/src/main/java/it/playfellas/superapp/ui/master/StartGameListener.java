package it.playfellas.superapp.ui.master;

/**
 * Created by Stefano Cappa on 29/07/15.
 */

import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.Config3;

/**
 * Callback interface for {@link it.playfellas.superapp.ui.master.GameActivity}
 */
public interface StartGameListener {
    void startGame1(Config1 config);

    void startGame2(Config2 config);

    void startGame3(Config3 config);
}
