import java.util.ArrayList;

public class GameBoard {
    private int rows;
    private int cols;
    private int[][] board;
    private ArrayList<GameState> stateGraph;  
    
    public GameBoard() {
        this.rows = 6;
        this.cols = 7;
        board = new int[rows][cols];
        stateGraph = new ArrayList<GameState>();
        
        int[][] startBoard = copyBoard();
        GameState startState = new GameState(startBoard, 0, -1);
        stateGraph.add(startState);
    }
    
    public GameBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new int[rows][cols];
        stateGraph = new ArrayList<GameState>();
        
        int[][] startBoard = copyBoard();
        GameState startState = new GameState(startBoard, 0, -1);
        stateGraph.add(startState);
    }
    
    
    public GameBoard(GameBoard other) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.board = copyBoard(other.board);
        this.stateGraph = new ArrayList<GameState>();
       
        for (int i = 0; i < other.stateGraph.size(); i++) {
            stateGraph.add(other.stateGraph.get(i));
        }
    }
    
    public int makeMove(int col, int player) { // O(rows + rows*cols) - find empty row O(rows) + copy board O(rows*cols)
        if (!isValidMove(col)) {
            return -1;
        }

        
        int row = -1;
        for (int r = rows - 1; r >= 0; r--) {
            if (board[r][col] == 0) {
                row = r;
                break;
            }
        }
        
        if (row == -1) {
            return -1;
        }
        
        board[row][col] = player;
        
       
        int[][] newBoard = copyBoard();
        GameState newState = new GameState(newBoard, player, col);
        stateGraph.add(newState);
        
  
        if (stateGraph.size() > 1) {
            int prevIndex = stateGraph.size() - 2;
            GameState prevState = stateGraph.get(prevIndex);
            prevState.addNextState(newState);
        }
        
        return row;
    }
    
    public boolean isValidMove(int col) { // O(1) - constant time boundary and array access
        if (col < 0 || col >= cols) {
            return false;
        }
        return board[0][col] == 0;
    }
    
    public boolean checkWin(int row, int col, int player) { // O(1) - 4 calls to checkDirection, each O(1)
       
        if (checkDirection(row, col, 0, 1, player) >= 4) {
            return true;
        }
     
        if (checkDirection(row, col, 1, 0, player) >= 4) {
            return true;
        }
      
        if (checkDirection(row, col, 1, 1, player) >= 4) {
            return true;
        }
      
        if (checkDirection(row, col, 1, -1, player) >= 4) {
            return true;
        }

        return false;
    }
    
    private int checkDirection(int row, int col, int deltaRow, int deltaCol, int player) { // O(1) - bounded by max board dimension (7)
        int count = 1;

   
        int r = row + deltaRow;
        int c = col + deltaCol;
        while (r >= 0 && r < rows && c >= 0 && c < cols && board[r][c] == player) {
            count++;
            r = r + deltaRow;
            c = c + deltaCol;
        }

    
        r = row - deltaRow;
        c = col - deltaCol;
        while (r >= 0 && r < rows && c >= 0 && c < cols && board[r][c] == player) {
            count++;
            r = r - deltaRow;
            c = c - deltaCol;
        }

        return count;
    }
    
    public boolean isBoardFull() { // O(cols) - check each column top row
        for (int col = 0; col < cols; col++) {
            if (board[0][col] == 0) {
                return false;
            }
        }
        return true;
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    public int[][] copyBoard() {
        return copyBoard(this.board);
    }
    
    private int[][] copyBoard(int[][] source) { // O(rows * cols) - copy each board cell
        int[][] copy = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = source[i][j];
            }
        }
        return copy;
    }
    
    public ArrayList<GameState> getStateGraph() {
        return stateGraph;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public int evaluateMove(GameBoard board, int row, int col) { // O(1) - 4 calls to evaluateDirection, each O(1)
        int score = 0;
        int[][] gameBoard = board.getBoard();

       
        score = score + evaluateDirection(gameBoard, row, col, 0, 1, 2, board.getRows(), board.getCols());
        score = score + evaluateDirection(gameBoard, row, col, 1, 0, 2, board.getRows(), board.getCols());
        score = score + evaluateDirection(gameBoard, row, col, 1, 1, 2, board.getRows(), board.getCols());
        score = score + evaluateDirection(gameBoard, row, col, 1, -1, 2, board.getRows(), board.getCols());

        return score;
    }
    
    public int evaluateDirection(int[][] board, int row, int col, int deltaRow, int deltaCol, int player, int rows, int cols) { // O(1) - bounded by max board dimension
        int score = 0;
        int count = 1;

      
        int r = row + deltaRow;
        int c = col + deltaCol;
        while (r >= 0 && r < rows && c >= 0 && c < cols && board[r][c] == player) {
            count++;
            r = r + deltaRow;
            c = c + deltaCol;
        }

        r = row - deltaRow;
        c = col - deltaCol;
        while (r >= 0 && r < rows && c >= 0 && c < cols && board[r][c] == player) {
            count++;
            r = r - deltaRow;
            c = c - deltaCol;
        }

    
        if (count >= 4) {
            score = score + 1000;
        } else if (count == 3) {
            score = score + 100;
        } else if (count == 2) {
            score = score + 20;
        }

        return score;
    }



}
