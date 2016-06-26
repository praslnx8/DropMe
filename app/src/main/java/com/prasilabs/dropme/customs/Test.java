package com.prasilabs.dropme.customs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by prasi on 25/6/16.
 */
public class Test {
    private static final String TAG = Test.class.getSimpleName();
    List<List<Integer>> perfectList = new ArrayList<>();

    public static void main(String[] args) {
        int[] intArray = new int[]{2, 3, 5, 6, 8, 10};

        int tot = 10;

        List<List<Integer>> integerArrayList = new ArrayList<>();

        for (int i = 0; i < intArray.length; i++) {
            List<Integer> integers = new ArrayList<>();
            integers.add(intArray[i]);

            integerArrayList.add(integers);
        }

        new Test().addHelper(integerArrayList, intArray, tot, 0);
    }

    private static List<List<Integer>> removeSameList(List<List<Integer>> listList) {
        List<List<Integer>> integetList = new ArrayList<>();

        for (int i = 0; i < listList.size(); i++) {

            List<Integer> aList = listList.get(i);

            boolean isSameList = false;
            for (int j = i + 1; j < listList.size(); j++) {
                List<Integer> bList = listList.get(j);

                if (checkSameList(aList, bList)) {
                    isSameList = true;
                    break;
                }
            }

            if (!isSameList) {
                integetList.add(aList);
            }
        }

        return integetList;
    }

    private static boolean checkSameList(List<Integer> integers1, List<Integer> integers2) {
        boolean same = true;
        if (integers1.size() == integers2.size()) {
            for (int i = 0; i < integers1.size(); i++) {
                int val = integers1.get(i);

                if (!integers2.contains(val)) {
                    same = false;
                    break;
                }
            }
        } else {
            same = false;
            Logger.getLogger(TAG).log(Level.INFO, integers2 + " size differs " + integers1);
        }

        return same;
    }

    private void addHelper(List<List<Integer>> integerList, int[] destInit, int tot, int i) {
        List<List<Integer>> integerArrayList = new ArrayList<>();

        System.out.println(integerList);
        if (i < destInit.length) {
            Iterator<List<Integer>> integerListIterator = integerList.listIterator();

            while (integerListIterator.hasNext()) {
                List<Integer> integers = integerListIterator.next();

                int sum = 0;
                for (int j = 0; j < integers.size(); j++) {
                    sum = sum + integers.get(j);
                }

                if (sum < tot) {
                    for (int j = i; j < destInit.length; j++) {
                        int element = destInit[j];
                        if (!integers.contains(element)) {
                            sum = sum + element;

                            if (sum <= tot) {
                                integers.add(element);
                            }
                        }
                    }
                }

                integerArrayList.add(integers);
            }

            System.out.println(integerArrayList);
            i++;
            addHelper(integerArrayList, destInit, tot, i);
        } else {
            Iterator<List<Integer>> listIterator = integerList.listIterator();

            while (listIterator.hasNext()) {
                List<Integer> perfectList = listIterator.next();

                int total = 0;
                for (Integer integer : perfectList) {
                    total = total + integer;
                }

                if (perfectList.size() < 2) {
                    listIterator.remove();
                } else if (total != tot) {
                    listIterator.remove();
                }
            }

            integerList = removeSameList(integerList);

            System.out.println("We got " + integerList.size() + " perfect counts");
            if (integerList.size() > 0) {
                System.out.println("The perfect counts are");
                for (List<Integer> integers : integerList) {
                    System.out.print(integers);
                }
            } else {
                System.out.print(-1);
            }
            System.out.println();
        }
    }
}
