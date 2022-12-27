package com.mehdilagdimi.myrh.util;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class UIDGenerator {
    private static Set<Long> UIDSet;
    private static Iterator<Long> iterator;
    static {
        UIDSet = new Random().longs(1, (int)Math.pow(10,100))
                .distinct()
                .limit(5)
                .boxed()
                .collect(Collectors.toSet());
        iterator = UIDSet.iterator();
    }

    public static Long getUID(){
        Long UID = iterator.next();
        iterator.remove();
        return iterator.next();
    }
}
