package src;

import java.util.ArrayList;

public class BoardStrategy {

    private static int SCORE_PER_POUND = 200;

    private static final int[][] POSITION_VALUES = {
            { 100, 100, 100, 100, 100, 100, 100, 100 },
            { 90, 60, 80, 50, 50, 80, 60, 90 },
            { 80, 60, 40, 30, 30, 40, 60, 80 },
            { 70, 50, 30, 20, 20, 30, 50, 70 },
            { 70, 50, 30, 20, 20, 30, 50, 70 },
            { 80, 60, 40, 30, 30, 40, 60, 80 },
            { 90, 60, 80, 50, 50, 80, 60, 90 },
            { 100, 100, 100, 100, 100, 100, 100, 100 }
    };

    public static int evaluateProtection(Case mark, ArrayList<Position> allMyColors, Board boardObj) {
        int points = 0;
        int orientationBackWard = mark == Case.BLACK ? -1 : 1;
        Case[][] board = boardObj.Getboard();
        for (Position position : allMyColors) {
            int rowBack = position.getRow() + orientationBackWard;
            int colLeft = position.getRow() - 1;
            int colRight = position.getRow() - 1;

            if (boardObj.validateInsideBoard(rowBack, colLeft)) {
                if (board[rowBack][colLeft] == mark) {
                    points += 150;
                } else if (board[rowBack][colLeft] == Case.EMPTY) {
                    points -= 150;
                }
            }
            if (boardObj.validateInsideBoard(rowBack, colRight)) {
                if (board[rowBack][colRight] == mark) {
                    points += 150;
                } else if (board[rowBack][colRight] == Case.EMPTY) {
                    points -= 150;
                }
            }

        }
        return points;
    }

    public static int ScoreForPositionOnBoard(ArrayList<Position> allMyColors) {
        int score = 0;
        for (Position pos : allMyColors) {
            score += POSITION_VALUES[pos.getRow()][pos.getCol()];
        }
        return score;
    }

    public static int evaluateDangerMove(Case mark, ArrayList<Position> allMyColors, Board boardObj) {
        int points = 0;
        int orientationFoward = mark == Case.BLACK ? 1 : -1;
        Case[][] board = boardObj.Getboard();
        for (Position position : allMyColors) {
            int rowFoward = position.getRow() + orientationFoward;
            int colLeft = position.getRow() - 1;
            int colRight = position.getRow() - 1;

            if (boardObj.validateInsideBoard(rowFoward, colLeft)) {
                if (board[rowFoward][colLeft] == boardObj.reverse(mark)) {
                    points -= 150;
                }
            }
            if (boardObj.validateInsideBoard(rowFoward, colRight)) {
                if (board[rowFoward][colRight] == boardObj.reverse(mark)) {
                    points -= 150;
                }
            }
        }
        return points;
    }

    public static int CheckDefence(ArrayList<Position> allMyColors) {
        int points = 0;
        for (Position position : allMyColors) {
            if (position.getRow() == 7 || position.getRow() == 0) {
                points += 400;
            }
        }
        return points;
    }

    public static int DangerZone(Case Mark, Case[][] board) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            if (Mark == Case.BLACK && board[1][i] == Case.RED) {
                score = score - 5000;
            }
            if (Mark == Case.BLACK && board[1][i] == Case.BLACK) {
                score += 100;
            }
            if (Mark == Case.RED && board[6][i] == Case.BLACK) {
                score = score - 5000;
            }
        }
        return score;
    }

    public static int ScorePerPoundOnBoard(ArrayList<Position> Player, ArrayList<Position> Ennemy) {

        int nb_player = Player.size();
        int nb_ennemy = Ennemy.size();
        int Score = 0;

        Score = ((nb_player * SCORE_PER_POUND) - (nb_ennemy * SCORE_PER_POUND));

        return Score;
    }

}
