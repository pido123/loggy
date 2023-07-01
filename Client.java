import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

import src.Board;
import src.Case;
import src.CpuPlayer;
import src.Move;

class Client {
	public static void main(String[] args) throws InterruptedException {

		Socket MyClient;
		BufferedInputStream input;
		BufferedOutputStream output;
		// int[][] board = new int[8][8];
		Board board = new Board();
		CpuPlayer cpuPlayer = new CpuPlayer(Case.RED);
		try {
			Case.InitCaseNameToArray();

			MyClient = new Socket("localhost", 8888);

			input = new BufferedInputStream(MyClient.getInputStream());
			output = new BufferedOutputStream(MyClient.getOutputStream());
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			while (1 == 1) {
				char cmd = 0;

				cmd = (char) input.read();
				System.out.println(cmd);
				// Debut de la partie en joueur RED
				if (cmd == '1') {
					byte[] aBuffer = new byte[1024];
					cpuPlayer = new CpuPlayer(Case.RED);
					int size = input.available();
					// System.out.println("size " + size);
					input.read(aBuffer, 0, size);
					String s = new String(aBuffer).trim();
					System.out.println(s);
					String[] boardValues;
					boardValues = s.split(" ");
					int x = 0, y = 0;
					for (int i = 0; i < boardValues.length; i++) {
						board.SetCase(y, x, Case.fromInteger(Integer.parseInt(boardValues[i])));
						x++;
						if (x == 8) {
							x = 0;
							y++;
						}
					}
					System.out.println(board.toString());

					ArrayList<Move> moves = cpuPlayer.getNextMoveAB(board);

					Random r = new Random();
					int result = r.nextInt(moves.size());
					;
					board.SetCase(moves.get(result).getOrigin().toString(), Case.EMPTY);
					board.SetCase(moves.get(result).getDestination().toString(), cpuPlayer.GetColor());
					output.write(moves.get(result).toString().getBytes(), 0, moves.get(result).toString().length());
					output.flush();
				}
				// Debut de la partie en joueur Noir
				if (cmd == '2') {
					System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
					byte[] aBuffer = new byte[1024];
					cpuPlayer = new CpuPlayer(Case.BLACK);
					int size = input.available();
					// System.out.println("size " + size);
					input.read(aBuffer, 0, size);
					String s = new String(aBuffer).trim();
					System.out.println(s);
					String[] boardValues;
					boardValues = s.split(" ");
					int x = 0, y = 0;
					for (int i = 0; i < boardValues.length; i++) {
						board.SetCase(y, x, Case.fromInteger(Integer.parseInt(boardValues[i])));
						x++;
						if (x == 8) {
							x = 0;
							y++;
						}
					}
					System.out.println("\n\r" + board.toString());
				}

				// Le serveur demande le prochain coup
				// Le message contient aussi le dernier coup joue.
				if (cmd == '3') {
					byte[] aBuffer = new byte[16];

					int size = input.available();
					System.out.println("size :" + size);
					input.read(aBuffer, 0, size);
					String s = new String(aBuffer);
					System.out.println("Dernier coup :" + s);
					String[] lastMove = s.split("-");
					board.SetCase(lastMove[0].trim(), Case.EMPTY);
					board.SetCase(lastMove[1].trim(), cpuPlayer.GetEnemyColor());
					// System.out.println(board.toString());
					ArrayList<Move> moves = cpuPlayer.getNextMoveAB(board);
					System.out.println("Explored Nodes MinMax:" + cpuPlayer.getNumOfExploredNodes());
					Random r = new Random();
					int result = r.nextInt(moves.size());
					board.SetCase(moves.get(result).getOrigin().toString(), Case.EMPTY);
					board.SetCase(moves.get(result).getDestination().toString(), cpuPlayer.GetColor());
					output.write(moves.get(result).toString().getBytes(), 0, moves.get(result).toString().length());
					output.flush();

				}
				// Le dernier coup est invalide
				if (cmd == '4') {
					System.out.println("Coup invalide, entrez un nouveau coup : ");
					String move = null;
					move = console.readLine();
					output.write(move.getBytes(), 0, move.length());
					output.flush();

				}
				// La partie est terminée
				if (cmd == '5') {
					byte[] aBuffer = new byte[16];
					int size = input.available();
					input.read(aBuffer, 0, size);
					String s = new String(aBuffer);
					String[] lastMove = s.split("-");

					System.out.println("Partie Terminé. Le dernier coup joué est: " + s);
					String move = null;
					move = console.readLine();
					output.write(move.getBytes(), 0, move.length());
					output.flush();

				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}

	}
}
