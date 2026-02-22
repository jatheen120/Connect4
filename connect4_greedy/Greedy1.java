public class Greedy1 {
    
    private Greedy2 greedy2;
    private int lastMoveScore = 0;
    
    public Greedy1() {
        greedy2 = new Greedy2();
    }
    
    public int getLastMoveScore() {
        return lastMoveScore;
    }
    
    public int getBestMove(GameBoard board) { // O(cols * (rows*cols + 1)) worst case - priority 5 dominates
        int cols = board.getCols();
        int bestCol = -1;
        int bestScore = -999999;

        // Priority 1: Check if CPU can win immediately - O(cols * (rows*cols + 1))
        for (int col = 0; col < cols; col++) {
            if (!board.isValidMove(col)) {
                continue;
            }
            GameBoard testBoard = new GameBoard(board); // O(rows*cols)
            int row = testBoard.makeMove(col, 2); // O(rows + rows*cols)
            if (row != -1 && testBoard.checkWin(row, col, 2)) { // O(1)
                lastMoveScore = 10000;
                return col;
            }
        }
        
        
        // Priority 2: Block player threats (3 in a row that could become 4) - O(rows*cols)
        int threatCol = greedy2.findPlayerThreat(board); // O(rows*cols)
        if (threatCol != -1) {
            lastMoveScore = 2500;
            return threatCol;
        }

        // Priority 3: Block player 2-in-a-row threats - O(rows*cols)
        int twoInRowCol = findTwoInRowThreat(board); // O(rows*cols)
        if (twoInRowCol != -1) {
            lastMoveScore = 500;
            return twoInRowCol;
        }
        
        // Priority 4: Evaluate offensive moves - O(cols * (rows*cols + 1))
        for (int col = 0; col < cols; col++) {
            if (!board.isValidMove(col)) {
                continue;
            }

            GameBoard testBoard = new GameBoard(board); // O(rows*cols)
            int row = testBoard.makeMove(col, 2); // O(rows + rows*cols)

            if (row == -1) {
                continue;
            }

            int score = board.evaluateMove(testBoard, row, col); 

            if (greedy2.hasThreeInRow(testBoard, row, col, 2)) {
                score = score + 150;
            }

            if (score > bestScore) {
                bestScore = score;
                bestCol = col;
            }
        }
        
        lastMoveScore = bestScore;
        
        
        if (bestCol == -1) {
            for (int col = 0; col < cols; col++) {
                if (board.isValidMove(col)) {
                    lastMoveScore = 0;
                    return col;
                }
            }
        }
        
        return bestCol;
    }
    
    private int findTwoInRowThreat(GameBoard board) { // O(rows*cols) - scan each board cell
       
        int rows = board.getRows();
        int cols = board.getCols();
        int[][] gameBoard = board.getBoard();

        for (int row = 0; row < rows; row++) { // O(rows)
            for (int col = 0; col < cols; col++) { // O(cols)
                if (gameBoard[row][col] == 1) {
                    
                    int blockCol = checkHorizontalTwoThreat(board, row, col);
                    if (blockCol != -1) {
                        return blockCol;
                    }
                    
                    blockCol = checkVerticalTwoThreat(board, row, col);
                    if (blockCol != -1) {
                        return blockCol;
                    }
                    
                    blockCol = checkDiagonalTwoThreat(board, row, col);
                    if (blockCol != -1) {
                        return blockCol;
                    }
                }
            }
        }

        return -1;
    }
    
    private int checkHorizontalTwoThreat(GameBoard board, int row, int col) { // O(1) - constant array accesses
        int[][] gameBoard = board.getBoard();
        int cols = board.getCols();
        
        
        if (col + 1 < cols && gameBoard[row][col] == 1 && gameBoard[row][col + 1] == 1) {
            
            if (col + 2 < cols && gameBoard[row][col + 2] == 0 && board.isValidMove(col + 2)) {
                return col + 2;
            }
            
            if (col - 1 >= 0 && gameBoard[row][col - 1] == 0 && board.isValidMove(col - 1)) {
                return col - 1;
            }
        }
        
        
        if (col + 2 < cols && gameBoard[row][col] == 1 && gameBoard[row][col + 2] == 1 && 
            gameBoard[row][col + 1] == 0 && board.isValidMove(col + 1)) {
            return col + 1;
        }
        
        return -1;
    }
    
    private int checkVerticalTwoThreat(GameBoard board, int row, int col) { // O(1) - constant array accesses
        int[][] gameBoard = board.getBoard();
        int rows = board.getRows();
        
        
        if (row + 1 < rows && gameBoard[row][col] == 1 && gameBoard[row + 1][col] == 1) {
            
            if (board.isValidMove(col)) {
                return col;
            }
        }
        
        return -1;
    }
    
    private int checkDiagonalTwoThreat(GameBoard board, int row, int col) { // O(1) - constant array accesses
        int[][] gameBoard = board.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();
        
        
        if (row + 1 < rows && col + 1 < cols && 
            gameBoard[row][col] == 1 && gameBoard[row + 1][col + 1] == 1) {
            
            if (row + 2 < rows && col + 2 < cols && gameBoard[row + 2][col + 2] == 0 && 
                board.isValidMove(col + 2)) {
                return col + 2;
            }
            if (row - 1 >= 0 && col - 1 >= 0 && gameBoard[row - 1][col - 1] == 0 && 
                board.isValidMove(col - 1)) {
                return col - 1;
            }
        }
        
        
        if (row + 1 < rows && col - 1 >= 0 && 
            gameBoard[row][col] == 1 && gameBoard[row + 1][col - 1] == 1) {
            
            if (row + 2 < rows && col - 2 >= 0 && gameBoard[row + 2][col - 2] == 0 && 
                board.isValidMove(col - 2)) {
                return col - 2;
            }
            if (row - 1 >= 0 && col + 1 < cols && gameBoard[row - 1][col + 1] == 0 && 
                board.isValidMove(col + 1)) {
                return col + 1;
            }
        }
        
        return -1;
    }
    
    private boolean checkBlockPlayer(GameBoard board, int col) { // O(rows*cols + 1) - copy board + check win
        
        GameBoard testBoard = new GameBoard(board); // O(rows*cols)
        int row = testBoard.makeMove(col, 1); // O(rows + rows*cols)

        if (row != -1 && testBoard.checkWin(row, col, 1)) { // O(1)
            return true;
        }

        return false;
    }
}
