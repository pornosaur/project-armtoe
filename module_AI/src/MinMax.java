import java.io.IOException;


/**
 * Uses the MiniMax algorithm to play a move in a game of Tic Tac Toe.
 */
class MinMax {
    private static double maxPly;
    public static int[] lastMove = new int[2];
    /**
     * MiniMax cannot be instantiated.
     */
    private MinMax() {}

    /**
     * Execute the algorithm.
     * @param player        the player that the AI will identify as
     * @param board         the Tic Tac Toe board to play on
     * @param maxPly        the maximum depth
     */
    static void run (Board.State player, Board board, double maxPly) {
        if (maxPly < 1) {
            throw new IllegalArgumentException("Maximum depth must be greater than 0.");
        }

        MinMax.maxPly = maxPly;
        miniMax(player, board, 0);
    }

    /**
     * The meat of the algorithm.
     * @param player        the player that the AI will identify as
     * @param board         the Tic Tac Toe board to play on
     * @param currentPly    the current depth
     * @return              the score of the board
     */
    private static int miniMax (Board.State player, Board board, int currentPly) {
    	//System.out.println(board.toString());
        if (currentPly++ == maxPly || board.isGameOver()) {
            return score(player, board);
        }

        if (board.getTurn() == player) {
            return getMax(player, board, currentPly);
        } else {
            return getMin(player, board, currentPly);
        }

    }

    /**
     * Play the move with the highest score.
     * @param player        the player that the AI will identify as
     * @param board         the Tic Tac Toe board to play on
     * @param currentPly    the current depth
     * @return              the score of the board
     */
    private static int getMax (Board.State player, Board board, int currentPly) {
        double bestScore = Double.NEGATIVE_INFINITY;
        int indexOfBestMove = -1;
        for (Integer theMove : board.getAvailableMoves()) {
            Board modifiedBoard = board.getDeepCopy();
            modifiedBoard.move(theMove);

            int score = miniMax(player, modifiedBoard, currentPly);

            if (score >= bestScore) {
                bestScore = score;
                indexOfBestMove = theMove;
            }

        }
        lastMove[0] = indexOfBestMove% board.BOARD_WIDTH;
        lastMove[1] = indexOfBestMove/board.BOARD_HEIGHT;
        board.move(indexOfBestMove);
        return (int)bestScore;
    }

    /**
     * Play the move with the lowest score.
     * @param player        the player that the AI will identify as
     * @param board         the Tic Tac Toe board to play on
     * @param currentPly    the current depth
     * @return              the score of the board
     */
    private static int getMin (Board.State player, Board board, int currentPly) {
        double bestScore = Double.POSITIVE_INFINITY;
        int indexOfBestMove = -1;
        for (Integer theMove : board.getAvailableMoves()) {
            Board modifiedBoard = board.getDeepCopy();
            modifiedBoard.move(theMove);

            int score = miniMax(player, modifiedBoard, currentPly);

            if (score <= bestScore) {
                bestScore = score;
                indexOfBestMove = theMove;
            }

        }
        lastMove[0] = indexOfBestMove% board.BOARD_WIDTH;
        lastMove[1] = indexOfBestMove/board.BOARD_HEIGHT;
        board.move(indexOfBestMove);
        return (int)bestScore;
    }

    /**
     * Get the score of the board.
     * @param player        the play that the AI will identify as
     * @param board         the Tic Tac Toe board to play on
     * @return              the score of the board
     */
    private static int score (Board.State player, Board board) {
        if (player == Board.State.Blank) {
            throw new IllegalArgumentException("Player must be X or O.");
        }

        Board.State opponent = (player == Board.State.X) ? Board.State.O : Board.State.X;

        
        if (board.isGameOver() && board.getWinner() == player) {
            return 10000;
        } else if (board.isGameOver() && board.getWinner() == opponent) {
            return -10000;
        } else {
        	int score = 0;
        	
        	for(int x = 0; x<Board.BOARD_WIDTH;x++) {
        		score += board.checkColumn(x,player)*board.checkColumn(x,player);
        		score -= board.checkColumn(x,opponent)*board.checkColumn(x,opponent);
        	}
        	for(int y = 0; y<Board.BOARD_HEIGHT;y++) {
        		score += board.checkRow(y,player)*board.checkRow(y,player);        
        		score -= board.checkRow(y,opponent)*board.checkRow(y,opponent);  
        	}
        	for(int x = 0; x<Board.BOARD_WIDTH;x++) {
        		score += board.checkDiagonalFromTopLeft(x, Board.BOARD_HEIGHT/2,player)*board.checkDiagonalFromTopLeft(x, Board.BOARD_HEIGHT/2,player);
            	score += board.checkDiagonalFromTopRight(x, Board.BOARD_HEIGHT/2,player)*board.checkDiagonalFromTopRight(x, Board.BOARD_HEIGHT/2,player);
            	score -= board.checkDiagonalFromTopLeft(x, Board.BOARD_HEIGHT/2,opponent)*board.checkDiagonalFromTopLeft(x, Board.BOARD_HEIGHT/2,opponent);
            	score -= board.checkDiagonalFromTopRight(x, Board.BOARD_HEIGHT/2,opponent)*board.checkDiagonalFromTopRight(x, Board.BOARD_HEIGHT/2,opponent);
        	}
        	//System.out.println(score);
        	return score;   			
        }
    }


}