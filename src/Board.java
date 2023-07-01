package src;

import java.util.ArrayList;
import java.util.Random;

public class Board {

    private Case[][] board;

    private Random rand = new Random();
    private final int Max_Score = 25000;

    public Board() {
        board = new Case[8][8];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = Case.EMPTY;
            }
        }
    }

    public Board(int[][] defaultBoard) {

        board = new Case[8][8];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = Case.fromInteger(defaultBoard[row][col]);
            }
        }

    }

    public Case[][] Getboard() {
        return this.board;
    }

    public Case GetCase(int row, int col) {
        return board[row][col];
    }

    public void SetCase(int row, int col, Case Case) {
        board[row][col] = Case;
    }

    public void SetCase(String key, Case Case) {
        int[] positions = src.Case.getIndexesByCaseName(key);
        if (key.length() == 2) {
            int col = positions[1];
            int row = positions[0];
            SetCase(row, col, Case);
        }
    }

    public void play(Move m, Case mark) {
        Position origin = m.getOrigin();
        Position destination = m.getDestination();
        SetCase(origin.getRow(), origin.getCol(), Case.EMPTY);
        SetCase(destination.getRow(), destination.getCol(), mark);
    }

    public void setOldPosition(Move m, Case Origin, Case dest) {
        Position origin = m.getOrigin();
        Position destination = m.getDestination();
        board[origin.getRow()][origin.getCol()] = Origin;
        board[destination.getRow()][destination.getCol()] = dest;
    }

    public int evaluate(Case mark, int depth) {
        Case contraire = reverse(mark);
        int points = 0;
        // Evaluer si la partie est gagner ou perdu en premier !
        // [0] == Rouge, [1]==Noir
        boolean[] DidSomeoneWin = HasWin();
        if (mark == Case.RED && DidSomeoneWin[0] || mark == Case.BLACK && DidSomeoneWin[1]) {
            return Max_Score;
        } else if (contraire == Case.RED && DidSomeoneWin[0] || contraire == Case.BLACK && DidSomeoneWin[1]) {
            return -Max_Score;
        }

        // Strategies pour evaluer
        // get(0) == Joueur En Cours, get(1) == Enemy
        ArrayList<ArrayList<Position>> allColorPositions = getAllPositionForColors(mark);
        // Check si le Pion est proteger
        points += BoardStrategy.ScoreForPositionOnBoard(allColorPositions.get(0));
        points += BoardStrategy.evaluateProtection(mark, allColorPositions.get(0), this);
        points += BoardStrategy.evaluateDangerMove(mark, allColorPositions.get(0), this);
        points += BoardStrategy.CheckDefence(allColorPositions.get(0));
        points += BoardStrategy.ScorePerPoundOnBoard(allColorPositions.get(0), allColorPositions.get(1));
        // points += BoardStrategy.DangerZone(mark, this.Getboard());
        return points;
    }

    public boolean[] HasWin() {

        boolean[] GameStatus = { false, false };

        for (int i = 0; i < 8; i++) {
            if (board[0][i] == Case.RED) {
                GameStatus[0] = true;
            }
            if (board[7][i] == Case.BLACK) {
                GameStatus[1] = true;
            }
        }
        return GameStatus;
    }

    public Case reverse(Case mark) {
        if (mark == Case.RED) {
            return Case.BLACK;
        } else {
            return Case.RED;
        }
    }

    public ArrayList<Move> getAllMove(Case color) {
        ArrayList<Move> moves = new ArrayList<Move>();
        if (color == Case.BLACK) {
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    if (board[row][col] == color) {
                        moves.addAll(getAllMoveForCase(row, col));
                    }
                }
            }
        }
        if (color == Case.RED) {
            for (int row = board.length - 1; row >= 0; row--) {
                for (int col = 0; col < board[row].length; col++) {
                    if (board[row][col] == color) {
                        moves.addAll(getAllMoveForCase(row, col));
                    }
                }
            }
        }
        return moves;
    }

    public ArrayList<ArrayList<Position>> getAllPositionForColors(Case myColor) {
        ArrayList<Position> myColorPositions = new ArrayList<Position>();
        ArrayList<Position> enemyColorPositions = new ArrayList<Position>();
        ArrayList<ArrayList<Position>> allColorPositions = new ArrayList<ArrayList<Position>>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == myColor) {
                    myColorPositions.add(new Position(row, col));
                }
                if (board[row][col] == reverse(myColor)) {
                    enemyColorPositions.add(new Position(row, col));
                }
            }
        }
        allColorPositions.add(myColorPositions);
        allColorPositions.add(enemyColorPositions);
        return allColorPositions;
    }

    public ArrayList<Move> getAllMoveForCase(int row, int col) {
        ArrayList<Move> moves = new ArrayList<Move>();
        Position origin = new Position(row, col);
        Case color = board[row][col];
        int orientation = color == Case.BLACK ? 1 : -1;
        // Check move Foward and Left
        Position moveFowardLeft = new Position(row + orientation, col - 1);
        if (validateLeftRight(moveFowardLeft, color)) {
            moves.add(new Move(origin, moveFowardLeft));
        }
        // Check move Foward
        Position moveFoward = new Position(row + orientation, col);
        if (validateFoward(moveFoward)) {
            moves.add(new Move(origin, moveFoward));
        }
        // Check move Foward and Right
        Position moveFowardRight = new Position(row + orientation, col + 1);
        if (validateLeftRight(moveFowardRight, color)) {
            moves.add(new Move(origin, moveFowardRight));
        }

        return moves;
    }

    public boolean validateLeftRight(Position move, Case color) {
        if (validateInsideBoard(move.getRow(), move.getCol())) {
            return board[move.getRow()][move.getCol()] != color;
        }
        return false;
    }

    public boolean validateFoward(Position move) {
        if (validateInsideBoard(move.getRow(), move.getCol())) {
            return board[move.getRow()][move.getCol()] == Case.EMPTY;
        }
        return false;
    }

    public static boolean validateInsideBoard(int row, int col) {
        return row < 8 && col < 8 && row >= 0 && col >= 0;
    }

    @Override
    public String toString() {
        String boardStr = "";
        int x = 0, y = 0;
        for (int i = 0; i < 64; i++) {

            if (board[y][x] != Case.EMPTY) {

                if (board[y][x] == Case.RED) {
                    boardStr += "[ R ]";
                } else {
                    boardStr += "[ B ]";
                }
            } else {
                boardStr += "[   ]";
            }

            x++;
            if (x == 8) {
                boardStr += "\n";
                x = 0;
                y++;
            }
        }
        return boardStr;
    }

}
