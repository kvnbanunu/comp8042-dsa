package com.assignment4.Sorting;
import java.util.Arrays;
import java.util.Random;

public class QuickInsertionSort<T extends Comparable<T>> {
    static int insertCount = 0;
    static int pivotCount = 0;
    // If you want to randomly choose pivots, you can use this (not necessary)
    static Random random = new Random();
    InsertionSort<T> sorter = new InsertionSort<>();


    public void quickInsertionSort(T[] arr){
        InsertionSort.insertCount = 0;
        pivotCount = 0;
        quickInsertionSort(arr, 0, arr.length);
        insertCount = InsertionSort.insertCount;
    }

    //Using this to call recursively makes things easier
    public void quickInsertionSort(T[] arr, int low, int high){
        if (high - low == 1) {
            return;
        }

        if (high - low <= 20) {
            sorter.insertionSort(arr, low, high - 1);
        } else {
            int mid = partition(arr, low, high);
            quickInsertionSort(arr, low, mid);
            quickInsertionSort(arr, mid + 1, high);
        }
    }

    //returns the index of the pivot element
    //everything to the left of the pivot is less than the pivot and everything to the right is greater than the pivot
    private int partition(T[] arr, int left, int right){
        pivotCount++;
        //choose the pivot element deterministically
        T pivot = arr[right - 1]; // last element

        int curr = left;

        for (int i = left; i < right - 1; i++) {
            if (arr[i].compareTo(pivot) <= 0) {
                swap(arr, curr++, i);
            }
        }

        // insert pivot
        swap(arr, curr, right - 1);

        return curr;

    }

    //helper method
    private void swap(T[] arr, int i, int j){
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
