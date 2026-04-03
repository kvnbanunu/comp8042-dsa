package com.assignment4.EightPuzzle;

public class EightPuzzleRunner {
    public static void main(String[] args) {

        // GameBoard initialState = new GameBoard(new int[][] {
        //     {8, 4, 5},
        //     {2, 0, 3},
        //     {7, 1, 6}
        // });

//        GameBoard initialState = new GameBoard(new int[][] {
//            {2, 3, 0},
//            {1, 4, 6},
//            {7, 5, 8}
//        });

        // Figure 2 example
        GameBoard initialState = new GameBoard(new int[][] {
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5}
        });
        System.out.println(initialState.hashCode());

        System.out.println(initialState.hamming());
        System.out.println(initialState.manhattan());
        System.out.println(initialState.manhattanPlusHamming());
        System.out.println(initialState.toString());

        GameBoard goalState = new GameBoard(new int[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        });

        System.out.println(goalState.hamming());
        System.out.println(goalState.manhattan());
        System.out.println(goalState.manhattanPlusHamming());
        System.out.println(goalState.toString());

//        AStar8PuzzleSolver solver = new AStar8PuzzleSolver(initialState, goalState);
//        solver.solve();
//
//        if(solver.status() == AStar8PuzzleSolver.solvedStatus.NOT_POSSIBLE) {
//            System.out.println("No solution found.");
//        }
//        else {
//            System.out.println("Solution found:");
//            //This should print out the set of board states that lead to the solution
//            solver.printSolution();
//        }
    }
}
