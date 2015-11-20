package it.playfellas.superapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by affo on 06/08/15.
 */
public class RandomUtils {

    public static <T> List<T> choice(T[] array, int n) {
        int size = n > array.length ? array.length : n;

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            indexes.add(i);
        }

        Collections.shuffle(indexes);
        List<Integer> rndIndexes = indexes.subList(0, size);

        List<T> ret = new ArrayList<>();
        for (Integer i : rndIndexes) {
            ret.add(array[i]);
        }

        return ret;
    }

    public static <T> List<T> choice(Collection<T> coll, int n) {
        return (List<T>) choice(coll.toArray(), n);
    }

    public static <T> T choice(Collection<T> coll) {
        return (T) choice(coll.toArray());
    }

    public static <T> T choice(T[] a) {
        return a[(new Random()).nextInt(a.length)];
    }
}
