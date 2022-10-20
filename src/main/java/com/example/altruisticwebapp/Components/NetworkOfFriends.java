package com.example.altruisticwebapp.Components;

import java.util.HashMap;
public class NetworkOfFriends{
    private int [][] matrix;
    private int size;

    public int getSize(){
        return size;
    }

    public NetworkOfFriends(int size){
        this.size = size;
        init();
    }
    public NetworkOfFriends(PlayerSet players){
        this.size = players.size();
        init();
    }

    public void init(){
        //Initializes all matrix values (Integer) to 0; Uses indices as player positions
        matrix = new int [this.size][this.size];
        for (int i = 0; i < this.size; i++){
            for (int j = 0; j < this.size; j++){
                matrix[i][j] = 0;
            }
            matrix[i][i] = -1;
        }
    }

    public int numberOfPlayers(){
        return matrix.length;
    }

    public int [][] getMatrix(){
        return matrix;
    }

    public void addFriendship(int p1, int p2){
        if (p1 == p2) return;
        matrix[p1][p2] = 1;
        matrix[p2][p1] = 1;
    }

    public void removeFriendship(int p1, int p2){
        if (p1 == p2) return;
        matrix[p1][p2] = 0;
        matrix[p2][p1] = 0;
    }

    public boolean areFriends (int p1, int p2){
        return matrix[p1][p2] == 1;
    }

    public void printMatrix(){
        for (int i = 0; i < matrix.length; i++){
            System.out.print("*" + i + "* ");
        }
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("*" + i + "* ");
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int getVal(int pos1, int pos2){
        return matrix[pos1][pos2];
    }

    public void addPlayer(){
        int[][] mat = new int [matrix.length+1][matrix.length+1];
        for (int i = 0; i < matrix.length; i++){
            System.arraycopy(matrix[i], 0, mat[i], 0, matrix.length);
        }
        for (int i = 0; i < mat.length; i++){
            mat[mat.length-1][i] = 0;
            mat[i][mat.length-1] = 0;
        }
        this.matrix = mat;
        this.size = mat.length;
    }

    public void removePlayer(int key){
        int [][] newMat = new int[matrix.length-1][matrix.length-1];

        for (int i = 0; i < key; i++){
            for (int j = 0; j < key; j++){
                newMat[i][j] = matrix[i][j];
            }
        }
        for (int i = key+1; i < matrix.length; i++){
            for (int j = key+1; j < matrix.length; j++){
                newMat[i-1][j-1] = matrix[i][j];
            }
        }
        matrix = newMat;

    }
}
