package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Integer> linkedList = new LinkedList<>();
        List<Integer> arrayList = new ArrayList<>();

        /*Block 1: Insert at last in LinkedList*/
        long t1 = System.currentTimeMillis();
//        System.out.println("t1 = "+t1);
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(2);
        arrayList.add(2);
        arrayList.add(2);
        arrayList.add(2);
        long t2 = System.currentTimeMillis() - t1;
        System.out.println("t2 = "+t2);
        linkedList.add(111);
        System.out.println(linkedList); /* Output: [1, 111]*/

        /*Block 2: Insert at last in Arraylist*/
        arrayList.add(1);
        arrayList.add(111);
        System.out.println(arrayList); /* Output: [1, 111]*/
    }
}
