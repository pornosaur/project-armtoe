import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents the Tic Tac Toe board.
 */
public class Board {

    static final int BOARD_WIDTH = 7;

    static final int BOARD_HEIGHT = 9;
    
    static final int MAX_LINE = 5;

    public static enum State {Blank, X, O}
    private State[][] board;
    private State playersTurn;
    private State winner;
    private ArrayList<Integer> movesAvailable;

    private int moveCount;
    private boolean gameOver;


    Board(int[][] gameState){
        this();

        if(gameState.length != BOARD_HEIGHT || gameState[0].length != BOARD_WIDTH) {
            System.out.println("ERROR: Can not initialize Board. Incoming board size is invalid.");
            return;
        }

        for (int i = 0; i < gameState.length; i++) {
            for (int j = 0; j < gameState[i].length; j++) {
                if(gameState[i][j] == 1)
                    board[i][j] = State.X;

                else if (gameState[i][j] == 2)
                    board[i][j] = State.O;
            }
        }
    }

    /**
     * Construct the Tic Tac Toe board.
     */
    Board() {
        board = new State[BOARD_HEIGHT][BOARD_WIDTH];
        movesAvailable = new ArrayList<Integer>();
        reset();
    }

    /**
     * Set the cells to be blank and load the available moves (all the moves are
     * available at the start of the game).
     */
    private void initialize () {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                board[row][col] = State.Blank;
            }
        }

        movesAvailable.clear();

        for (int i = 0; i < BOARD_HEIGHT*BOARD_WIDTH; i++) {
            movesAvailable.add(i);
        }
    }

    /**
     * Restart the game with a new blank board.
     */
    void reset () {
        moveCount = 0;
        gameOver = false;
        playersTurn = State.X;
        winner = State.Blank;
        initialize();
    }

    /**
     * Places an X or an O on the specified index depending on whose turn it is.
     * @param index     the position on the board (example: index 4 is location (0, 1))
     * @return          true if the move has not already been played
     */
    public boolean move (int index) {
        return move(index% BOARD_WIDTH, index/BOARD_HEIGHT );
    }
    
    public State getState(int index) {
    	return board[index/BOARD_HEIGHT ][index% BOARD_WIDTH];
    }

    /**
     * Places an X or an O on the specified location depending on who turn it is.
     * @param x         the x coordinate of the location
     * @param y         the y coordinate of the location
     * @return          true if the move has not already been played
     */
    public boolean move (int x, int y) {
        if (gameOver) {
            throw new IllegalStateException("TicTacToe is over. No moves can be played.");
        }
        if (board[y][x] == State.Blank) {
            board[y][x] = playersTurn;
        } else {
            return false;
        }

        moveCount++;
        movesAvailable.remove((Integer)(y * BOARD_HEIGHT + x));

        // The game is a draw.
        if (moveCount == BOARD_HEIGHT * BOARD_WIDTH) {
            winner = State.Blank;
            gameOver = true;
        }

        // Check for a winner.
        checkRow(y,playersTurn);
        checkColumn(x,playersTurn);
        checkDiagonalFromTopLeft(x, y,playersTurn);
        checkDiagonalFromTopRight(x, y,playersTurn);

        playersTurn = (playersTurn == State.X) ? State.O : State.X;
        
        return true;
    }

    /**
     * Check to see if the game is over (if there is a winner or a draw).
     * @return          true if the game is over
     */
    public boolean isGameOver () {
        return gameOver;
    }

    /**
     * Get a copy of the array that represents the board.
     * @return          the board array
     */
    State[][] toArray () {
        return board.clone();
    }

    /**
     * Check to see who's turn it is.
     * @return          the player who's turn it is
     */
    public State getTurn () {
        return playersTurn;
    }

    /**
     * Check to see who won.
     * @return          the player who won (or Blank if the game is a draw)
     */
    public State getWinner () {
        if (!gameOver) {
            throw new IllegalStateException("TicTacToe is not over yet.");
        }
        return winner;
    }

    /**
     * Get the indexes of all the positions on the board that are empty.
     * @return          the empty cells
     */
    public ArrayList<Integer> getAvailableMoves () {
    	//return movesAvailable;
    	ArrayList<Integer> actualMoves = new ArrayList<Integer>(movesAvailable);
    	for(int i = 0; i<movesAvailable.size();i++) {
    		int index = movesAvailable.get(i);
    		if(!isViable(index) || getState(index)!=State.Blank) {
    			actualMoves.remove((Integer)index);
    		}
    	}
        return actualMoves;
    }

    public boolean isViable(int index) {
    	int x = index % BOARD_WIDTH;
    	int y = index / BOARD_HEIGHT;
    	int startPosX = (x - 1 < 0) ? x : x-1;
    	int startPosY = (y - 1 < 0) ? y : y-1;
    	int endPosX =   (x + 1 >= BOARD_WIDTH) ? x : x+1;
    	int endPosY =   (y + 1 >= BOARD_HEIGHT) ? y : y+1;
    	for (int rowNum=startPosX; rowNum<=endPosX; rowNum++) {
    	    for (int colNum=startPosY; colNum<=endPosY; colNum++) {
    	    	
    	    	if(board[colNum][rowNum]!=State.Blank) {
    	    		//System.out.println("yes "+colNum+" "+rowNum);
    	    		return true;
    	    	}
    	    }
    	}
    	return false;
    }
    /**
     * Checks the specified row to see if there is a winner.
     * @param row       the row to check
     * @param playersTurn 
     */
    public int checkRow (int row, State playersTurn) {
    	int inARow = 0;
    	boolean startOver = false;
        for (int i = 0; i < BOARD_WIDTH; i++) {
        	if(startOver == true) {
        		inARow = 0;
        		startOver = false;
        	}
            if (board[row][i] == playersTurn) {
            	inARow++;
            } else {
            	startOver = true;
            }
            if (inARow >= MAX_LINE) {
                winner = playersTurn;
                gameOver = true;
            }
        }
        

    	return inARow;
    }

    /**
     * Checks the specified column to see if there is a winner.
     * @param column    the column to check
     */
    public int checkColumn (int column, State playersTurn) {
    	int inARow = 0;
    	boolean startOver = false;
        for (int i = 0; i < BOARD_HEIGHT; i++) {
        	if(startOver == true) {
        		inARow = 0;
        		startOver = false;
        	}
            if (board[i][column] == playersTurn) {
            	inARow++;
            } else {
            	startOver = true;
            }
            if (inARow >= MAX_LINE) {
                winner = playersTurn;
                gameOver = true;
            }
        }
        

    	return inARow;
    }

    /**
     * Check the left diagonal to see if there is a winner.
     * @param x         the x coordinate of the most recently played move
     * @param y         the y coordinate of the most recently played move
     */
    public int checkDiagonalFromTopLeft (int x, int y, State playersTurn) {
    	int min = Math.min(x, y);
    	int diagonalX = x-min;
    	int diagonalY = y-min;
    	int inARow = 0;
    	boolean startOver = false;
    	while(diagonalX < BOARD_WIDTH && diagonalY < BOARD_HEIGHT) {
    		if(startOver == true) {
        		inARow = 0;
        		startOver = false;
        	}
            if (board[diagonalY][diagonalX] == playersTurn) {
            	inARow++;
            } else {
            	startOver = true;
            }
            if (inARow >= MAX_LINE) {
                winner = playersTurn;
                gameOver = true;
            }
            diagonalX++;
            diagonalY++;
    	}

    	return inARow;
    }

    /**
     * Check the right diagonal to see if there is a winner.
     * @param x     the x coordinate of the most recently played move
     * @param y     the y coordinate of the most recently played move
     */
    public int checkDiagonalFromTopRight (int x, int y, State playersTurn) {
    	int diagonalX = x+y;
    	int diagonalY = 0;
    	int inARow = 0;
    	boolean startOver = false;
    	
    	while(diagonalX < BOARD_WIDTH && diagonalY < BOARD_HEIGHT && 0 <= diagonalX && 0 <= diagonalY) {
    		if(startOver == true) {
        		inARow = 0;
        		startOver = false;
        	}
            if (board[diagonalY][diagonalX] == playersTurn) {
            	inARow++;
            } else {
            	startOver = true;
            }
            if (inARow >= MAX_LINE) {
                winner = playersTurn;
                gameOver = true;
            }
            diagonalX--;
            diagonalY++;
            
            
    	}
    	
    	return inARow;
    }

    /**
     * Get a deep copy of the Tic Tac Toe board.
     * @return      an identical copy of the board
     */
    public Board getDeepCopy () {
        Board board             = new Board();

        for (int i = 0; i < board.board.length; i++) {
            board.board[i] = this.board[i].clone();
        }

        board.playersTurn       = this.playersTurn;
        board.winner            = this.winner;
        board.movesAvailable    = new ArrayList<>();
        board.movesAvailable.addAll(this.movesAvailable);
        board.moveCount         = this.moveCount;
        board.gameOver          = this.gameOver;
        return board;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {

                if (board[y][x] == State.Blank) {
                    sb.append("-");
                } else {
                    sb.append(board[y][x].name());
                }
                sb.append(" ");

            }
            if (y != BOARD_HEIGHT -1) {
                sb.append("\n");
            }
        }

        return new String(sb);
    }

}