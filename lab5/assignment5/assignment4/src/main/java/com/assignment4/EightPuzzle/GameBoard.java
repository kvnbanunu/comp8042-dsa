package com.assignment4.EightPuzzle;

import java.util.*;

// No reason to implement comparable so I removed it
public class GameBoard implements Comparable<GameBoard> {
    private static class Coordinate{
        private final int row;
        private final int col;

        public Coordinate(int row, int col){
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    }

    // for quick comparisons against the goal state
    private static final int[][] GOAL = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 0},
    };
    // Stores the coordinates of each tile in the goal state
    private static final Coordinate[] GOAL_COORDINATES = {
        new Coordinate(2, 2),
        new Coordinate(0, 0),
        new Coordinate(0, 1),
        new Coordinate(0, 2),
        new Coordinate(1, 0),
        new Coordinate(1, 1),
        new Coordinate(1, 2),
        new Coordinate(2, 0),
        new Coordinate(2, 1),
    };

    // The hashCode of the GOAL state
    private static final int GOAL_HASH = 123456780;

    private final int hashCode;

    private final int[][] tiles;
    private final int dimension;
    //Useful to access the empty square in constant time
    private Coordinate emptySquare;

    public GameBoard(int[][] tiles){
        //Assume 0 denotes the empty square
        this.tiles = tiles;
        this.dimension = tiles.length;

        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                if(tiles[i][j] == 0){
                    emptySquare = new Coordinate(i, j);
                }
            }
        }

        this.hashCode = hashCode();

        if (!isValid()){
            tiles = null;
            throw new IllegalArgumentException("Invalid board! It must include consecutive numbers and be square");
        }
    }

    public boolean isValid(){
        if (tiles == null) return false;
        if (tiles.length != tiles[0].length) return false;

        Set<Integer> unseen = new HashSet<>();
        for(int counter = 0; counter < dimension * dimension; counter++){
            unseen.add(counter);
        }

        for (int[] row : tiles) {
            for (int tile : row) {
                if (tile < 0){
                    return false;
                }
                unseen.remove(tile);
            }
        }
        return unseen.isEmpty();
    }

    public int[][] getTiles(){
        return tiles;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                sb.append(tiles[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int dimension(){
        return dimension;
    }

    public int getHashCode() { return hashCode; }

    /*
     * The Hamming distance between a board and the goal board is the number of tiles in the wrong position.
     * returns the number of tiles out of place relative to goal
     */
    public int hamming(){
        int count = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] != GOAL[i][j] && tiles[i][j] != 0) count++;
            }
        }
        return count;
    }

    /*
     * The Manhattan distance between a board and the goal board is the sum of the Manhattan distances (sum of the vertical and horizontal distance) from the tiles to their goal positions.
     * returns the sum of Manhattan distances between tiles and goal
     */
    public int manhattan(){
        int total = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int val = tiles[i][j];
                if (val != 0 && val != GOAL[i][j]) {
                    Coordinate goal = GOAL_COORDINATES[val];
                    total += Math.abs(goal.row - i) + Math.abs(goal.col - j);
                }
            }
        }

        return total;
    }

    public int manhattanPlusHamming(){
        int total = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int val = tiles[i][j];
                if (val != 0 && val != GOAL[i][j]) {
                    total++;
                    Coordinate goal = GOAL_COORDINATES[val];
                    total += Math.abs(goal.row - i) + Math.abs(goal.col - j);
                }
            }
        }
        return total;
    }

    public boolean isGoal() { return hashCode == GOAL_HASH; }

    @Override
    public int hashCode(){
        return hashCode(tiles);
    }

    // Simply stores the state as the tiles in order. Only works for 3x3
    private int hashCode(int[][] state) {
        int res = 0;
        int digit = 100000000;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                res += digit * state[i][j];
                digit /= 10;
            }
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        GameBoard other = (GameBoard) obj;
        if (dimension != other.dimension()) return false;

        return hashCode == other.getHashCode();
    }

    @Override
    public int compareTo(GameBoard other) {
        return Integer.compare(hashCode, other.getHashCode());
    }

    // all neighboring board states
    public Iterable<GameBoard> neighbors(){
        ArrayList<GameBoard> neighbors = new ArrayList<>();

        //Swap the empty square with all of its neighbors
        if(emptySquare.row > 0){
            neighbors.add(swap(tiles[emptySquare.row - 1][emptySquare.col], 0));
        }
        if(emptySquare.row < dimension - 1){
            neighbors.add(swap(tiles[emptySquare.row + 1][emptySquare.col], 0));
        }
        if(emptySquare.col > 0){
            neighbors.add(swap(tiles[emptySquare.row][emptySquare.col - 1], 0));
        }
        if(emptySquare.col < dimension - 1){
            neighbors.add(swap(tiles[emptySquare.row][emptySquare.col + 1], 0));
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public GameBoard swap(int tile1Value, int tile2Value){
        //Swap the tiles at the given indices
        if(tile1Value != 0 & tile2Value != 0){
            throw new IllegalArgumentException("One of the tiles must be the empty square!");
        }
        else{
            int[][] newTiles = new int[dimension][dimension];

            for(int i = 0; i < dimension; i++){
                for(int j = 0; j < dimension; j++){
                    if(tiles[i][j] == tile1Value){
                        newTiles[i][j] = tile2Value;
                    }
                    else if(tiles[i][j] == tile2Value){
                        newTiles[i][j] = tile1Value;
                    }
                    else{
                        newTiles[i][j] = tiles[i][j];
                    }
                }
            }
            return new GameBoard(newTiles);
        }
    }
}
