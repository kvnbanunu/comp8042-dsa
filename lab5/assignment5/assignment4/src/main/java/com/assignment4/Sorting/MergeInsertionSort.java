package com.assignment4.Sorting;

import java.util.*;

public class MergeInsertionSort<T extends Comparable<T>> {
    static int insertCount = 0;
    static int mergeCount = 0;
    InsertionSort<T> insertionSorter = new InsertionSort<>();

    public void insertMergeSort(T[] arr){
        InsertionSort.insertCount = 0;
        mergeCount = 0;

        insertMergeSort(arr, 0, arr.length);

        // update this classes insertCount when the array has been sorted
        insertCount = InsertionSort.insertCount;
    }

    // Using this to call recursively makes things easier
    private void insertMergeSort(T[] arr, int left, int right) {
        if (right - left == 1) {
            return;
        }

        if (right - left <= 10) {
            insertionSorter.insertionSort(arr, left, right - 1);
        } else {
            int mid = left + (right - left) / 2;
            insertMergeSort(arr, left, mid);
            insertMergeSort(arr, mid, right);
            merge(arr, left, mid, right);
        }
    }

    //merge two sub-arrays which are beside each other
    private void merge(T[] arr, int leftStart, int center, int rightEnd) {
        mergeCount++;
        T[] temp = Arrays.copyOfRange(arr, leftStart, center);
        int curr = leftStart;
        int l = 0;
        int r = center;

        while (l < temp.length && r < rightEnd ) {
            if (temp[l].compareTo(arr[r]) <= 0) {
                arr[curr++] = temp[l++];
            } else {
                arr[curr++] = arr[r++];
            }
        }

        // swap leftovers
        while (l < temp.length) {
            arr[curr++] = temp[l++];
        }
        while (r < rightEnd) {
            arr[curr++] = arr[r++];
        }
    }
}
