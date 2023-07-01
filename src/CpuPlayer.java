package src;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CpuPlayer {

    private Case max = Case.EMPTY;
    private Case PlayerColor = Case.EMPTY;
    private Case EnemyColor = Case.EMPTY;
    private int numExploredNodes = 0;
    private static final long MAX_TIME_MS = 4500;
    private static long startTime = System.nanoTime();

    public int getNumOfExploredNodes() {
        return numExploredNodes;
    }

    public CpuPlayer(Case color) {
        this.PlayerColor = color;
        EnemyColor = color == Case.BLACK ? Case.RED : Case.BLACK;
    }

    public Case GetColor() {
        return this.PlayerColor;
    }

    public Case GetEnemyColor() {
        return this.EnemyColor;
    }

    public ArrayList<Move> getNextMoveAB(Board board) {
        max = PlayerColor;
        numExploredNodes = 0;
        ArrayList<Move> movesEvaluate = new ArrayList<Move>();
        ArrayList<Move> moves = board.getAllMove(PlayerColor);
        int MaxScore = -Integer.MAX_VALUE;
        Case OldCase;
        Position OldPosition;
        CpuPlayer.startTime = System.nanoTime();
        for (Move move : moves) {

            OldPosition = move.getDestination();
            OldCase = board.GetCase(OldPosition.getRow(), OldPosition.getCol());

            board.play(move, max);

            int score = GetAlphaBeta(board, board.reverse(max), -Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
            System.out.println("Score :" + score);
            if (score > MaxScore) {
                MaxScore = score;
                movesEvaluate.clear();
                movesEvaluate.add(move);
            } else if (score == MaxScore) {
                movesEvaluate.add(move);
            }

            board.setOldPosition(move, max, OldCase);
        }

        return movesEvaluate;
    }

    public int GetAlphaBeta(Board board, Case player, int alpha, int beta, int depth) {
        numExploredNodes++;
        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - CpuPlayer.startTime);
        int EvaluationScore = board.evaluate(max, depth);
        ArrayList<Move> moves = board.getAllMove(player);
        // Ajouter evaluation quand la artie est gagner ou perdu
        if (elapsedTime >= MAX_TIME_MS || Math.abs(EvaluationScore) >= 25000) {
            return EvaluationScore = EvaluationScore / (depth + 1);
        }
        Case OldCase;
        Position OldPosition;

        if (player == this.max) {
            int aplhaT = Integer.MIN_VALUE;

            for (int i = 0; i < moves.size(); i++) {
                OldPosition = moves.get(i).getDestination();
                OldCase = board.GetCase(OldPosition.getRow(), OldPosition.getCol());

                board.play(moves.get(i), player);
                int score = GetAlphaBeta(board, board.reverse(player), Math.max(aplhaT, alpha), beta, depth++);
                aplhaT = Math.max(aplhaT, score);
                board.setOldPosition(moves.get(i), player, OldCase);
                if (aplhaT >= beta) {
                    break;
                }
            }
            return aplhaT;
        } else {
            int betaT = Integer.MAX_VALUE;

            for (int i = 0; i < moves.size(); i++) {

                OldPosition = moves.get(i).getDestination();
                OldCase = board.GetCase(OldPosition.getRow(), OldPosition.getCol());

                board.play(moves.get(i), player);

                int score = GetAlphaBeta(board, board.reverse(player), alpha, Math.min(betaT, beta), depth++);

                betaT = Math.min(betaT, score);
                board.setOldPosition(moves.get(i), player, OldCase);
                if (betaT <= alpha) {
                    break;
                }

            }

            return betaT;
        }
    }
}