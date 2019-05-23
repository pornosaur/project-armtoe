import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;



public class Game {

	private static final String IP = "192.168.0.11";
	private static final int PORT = 59002;

	private static final int MAX_PLY = 4;
	private static final int GAME_STATE_ROUNDS = 5;

	private SocketManager socketManager;
	private Scanner scanner;



	public static void main(String[] args) throws IOException, InterruptedException {
		new Game();
	}

	public Game() throws InterruptedException, IOException {

		/*Board b = new Board();
		while(!b.isGameOver()) {
			b.move(Integer.parseInt(scanner.nextLine()), Integer.parseInt(scanner.nextLine()));
			System.out.println(b.toString());
			System.out.println();
			MinMax.run(b.getTurn(), b, MAX_PLY);
			System.out.println(b.toString());
			System.out.println();
		}
		*/
		
		socketManager = new SocketManager(IP, PORT);
		if(!socketManager.isOpen()){
			System.out.println("Connection was not established!");
			System.exit(1);
		}

		scanner = new Scanner(System.in);


		try {
			while (true) {
				int[][] gameState = getGameStateToPlay();
				Board board = new Board(gameState);

				MinMax.run(Board.State.O, board, MAX_PLY);
				int[] lastMove = MinMax.lastMove;
				socketManager.sendCoordinates(lastMove[0], lastMove[1]);
				if(board.isGameOver()) {
					break;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			socketManager.stopConnection();
		}

		scanner.close();
	}

	private int[][] getGameStateToPlay(){

		int sameRounds = 1;

		int [][] gameState = readInputData();

		while (GAME_STATE_ROUNDS > sameRounds) {
			int [][] tmpGameState = readInputData();

			if (isEmpty(tmpGameState)) continue; // First player must be human
			if (!isOnMove(tmpGameState)) continue;

			if (areEquals(tmpGameState, gameState)) {
				sameRounds++;
				System.out.println("Games are equals");
			} else {
				sameRounds = 1;
				gameState = tmpGameState;
				System.out.println("Games are not equals. Reset counter.");
			}
		}
		return gameState;
	}

	private int[][] readInputData(){
		String[] input = scanner.next().split(",");

		int[][] gameState = new int[9][7];
		for(int i = 0; i<input.length;i++) {
			gameState[i/7][i%7] = Integer.parseInt(input[i]);
		}

		return gameState;
	}

	private boolean isOnMove(int [][] gameState){
		int player1 = 0;
		int player2 = 0;
		for (int[] ints : gameState) {
			for (int anInt : ints) {
				if (anInt == 1) player1++;
				else if (anInt == 2) player2++;
			}
		}
		boolean result = player1 > player2;

		if(result) System.out.println("I am on move");
		else System.out.println("I am not on move");

		return result;
	}

	private boolean isEmpty(int[][] gameState){
		for (int[] ints : gameState) {
			for (int anInt : ints) {
				if (anInt != 0) {
					System.out.println("Input array is empty");
					return false;
				}
			}
		}
		return true;
	}

	private boolean areEquals(int[][] first, int[][] second){
		if (first.length != second.length) return false;

		for (int i = 0; i < first.length; i++) {
			if(!Arrays.equals(first[i], second[i])) return false;
		}

		return true;
	}
}
