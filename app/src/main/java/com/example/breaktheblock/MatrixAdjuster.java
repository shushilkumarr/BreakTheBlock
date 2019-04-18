package com.example.breaktheblock;
/*
 * This class handles manipulation of the matrix of the board
 *
 * The analyse method is used to scan the matrix and store the blocks of each number
 *
 * the remove method removes a given number from the matrix by replacing it with 0
 *
 * the adjustable method checks whether the give mstrix has blocks that could be adjusted/moved
 *
 * the adjust method moves the block with number passed as parameter
 *
 *
 * */

import java.util.ArrayList;

public class MatrixAdjuster {
    ArrayList<ArrayList<int[]>> indices; //[number][point][i][j]

    public MatrixAdjuster() {
        indices = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            indices.add(new ArrayList<int[]>());
    }

    public void analyse(int[][] matrix) {
        indices.removeAll(indices);
        for (int i = 1; i <= 20; i++)
            indices.add(new ArrayList<int[]>());

        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 6; j++) {
                try {

                    indices.get(matrix[i][j] - 1).add(new int[]{i, j});
                } catch (IndexOutOfBoundsException e) {
                }
            }

    }

    public int[][] remove(int[][] mat, int n) {
        for (int i = 0; i < indices.get(n - 1).size(); i++)
            mat[indices.get(n - 1).get(i)[0]][indices.get(n - 1).get(i)[1]] = 0;
        while (adjustable(mat)) {
        }
        analyse(mat);
        return mat;

    }

    private boolean adjustable(int[][] mat) {
        analyse(mat);
        boolean adjustable;
        for (int n = 1; n <= 20; n++) {
            adjustable = true;
            for (int i = indices.get(n - 1).size() - 1; i >= 0; i--) {
                try {
                    if (indices.get(n - 1).size() > 0 && !(mat[indices.get(n - 1).get(i)[0] + 1][indices.get(n - 1).get(i)[1]] == 0 || mat[indices.get(n - 1).get(i)[0] + 1][indices.get(n - 1).get(i)[1]] == n)) {
                        adjustable = false;
                        continue;
                    }
                } catch (Exception e) {
                    adjustable = false;
                }

                if (adjustable) {
                    adjust(mat, n);
                    return true;
                }
            }
        }


        return false;
    }

    private int[][] adjust(int[][] mat, int num) {
        ArrayList<int[]> newPos = new ArrayList<>();
        for (int i = indices.get(num - 1).size() - 1; i >= 0; i--) {
            try {
                mat[indices.get(num - 1).get(i)[0] + 1][indices.get(num - 1).get(i)[1]] = mat[indices.get(num - 1).get(i)[0]][indices.get(num - 1).get(i)[1]];
                mat[indices.get(num - 1).get(i)[0]][indices.get(num - 1).get(i)[1]] = 0;
                newPos.add(new int[]{indices.get(num - 1).get(i)[0] + 1, indices.get(num - 1).get(i)[1]});
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        for (int i = 0; i < newPos.size(); i++) {
            mat[newPos.get(i)[0]][newPos.get(i)[1]] = num;
        }
        return mat;


    }
}
