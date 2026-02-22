public class Greedy2 {
    
    public int findPlayerThreat(GameBoard board) { // O(rows*cols) - scan each board cell
        
        int rows = board.getRows();
        int cols = board.getCols();
        int[][] gameBoard = board.getBoard();

        for (int row = 0; row < rows; row++) { 
            for (int col = 0; col < cols; col++) { 
                if (gameBoard[row][col] == 1) {
                    
                    int blockCol = checkHorizontalThreat(board, row, col);
                    if (blockCol != -1) {
                        return blockCol;
                    }
                    
                    blockCol = checkVerticalThreat(board, row, col);
                    if (blockCol != -1) {
                        return blockCol;
                    }
                    
                    blockCol = checkDiagonalThreat(board, row, col);
                    if (blockCol != -1) {
                        return blockCol;
                    }
                }
            }
        }

        return -1;
    }
    
    public int checkHorizontalThreat(GameBoard board, int row, int col) { // O(1) - bounded loops (max 4 iterations)
        int[][] gameBoard = board.getBoard();
        int cols = board.getCols();
        int playerCount = 1;


        for (int c = col + 1; c < cols && c < col + 4 && gameBoard[row][c] == 1; c++) {
            playerCount++;
        }

        
        for (int c = col - 1; c >= 0 && c > col - 4 && gameBoard[row][c] == 1; c--) {
            playerCount++;
        }
        
        
        if (playerCount >= 3) {
            
            if (col + 3 < cols && gameBoard[row][col + 1] == 1 && gameBoard[row][col + 2] == 1) {
                int blockCol = col + 3;
                if (board.isValidMove(blockCol) && gameBoard[row][blockCol] == 0) {
                    return blockCol;
                }
            }
            
            if (col - 3 >= 0 && gameBoard[row][col - 1] == 1 && gameBoard[row][col - 2] == 1) {
                int blockCol = col - 3;
                if (board.isValidMove(blockCol) && gameBoard[row][blockCol] == 0) {
                    return blockCol;
                }
            }
            
            if (col + 2 < cols && col - 1 >= 0 && 
                gameBoard[row][col + 1] == 1 && gameBoard[row][col - 1] == 1) {
                
                if (col + 2 < cols && gameBoard[row][col + 2] == 0 && board.isValidMove(col + 2)) {
                    return col + 2;
                }
                
                if (col - 2 >= 0 && gameBoard[row][col - 2] == 0 && board.isValidMove(col - 2)) {
                    return col - 2;
                }
            }
            
            
        }   
        return -1;
    }
    
    public int checkVerticalThreat(GameBoard board, int row, int col) { // O(1) - bounded loops (max 4 iterations)
        int[][] gameBoard = board.getBoard();
        int rows = board.getRows();
        int playerCount = 1;

        
        for (int r = row + 1; r < rows && r < row + 4 && gameBoard[r][col] == 1; r++) {
            playerCount++;
        }

        
        for (int r = row - 1; r >= 0 && r > row - 4 && gameBoard[r][col] == 1; r--) {
            playerCount++;
        }
        
        
        if (playerCount >= 3) {
            if (board.isValidMove(col)) {
                return col;
            }
        }
        
        return -1;
    }
    
    public int checkDiagonalThreat(GameBoard board, int row, int col) { // O(1) - bounded loops (max 4 iterations)
        int[][] gameBoard = board.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();
        
        
        int count1 = 1;
        for (int i = 1; i < 4; i++) {
            int r = row + i;
            int c = col + i;
            if (r < rows && c < cols && gameBoard[r][c] == 1) {
                count1++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 4; i++) {
            int r = row - i;
            int c = col - i;
            if (r >= 0 && c >= 0 && gameBoard[r][c] == 1) {
                count1++;
            } else {
                break;
            }
        }
        
        if (count1 >= 3) {
            
            for (int i = 1; i <= 3; i++) {
                int r = row + i;
                int c = col + i;
                if (r >= 0 && r < rows && c >= 0 && c < cols && gameBoard[r][c] == 0) {
                    if (board.isValidMove(c)) {
                        return c;
                    }
                }
            }
            for (int i = 1; i <= 3; i++) {
                int r = row - i;
                int c = col - i;
                if (r >= 0 && r < rows && c >= 0 && c < cols && gameBoard[r][c] == 0) {
                    if (board.isValidMove(c)) {
                        return c;
                    }
                }
            }
        }
        
        
        int count2 = 1;
        for (int i = 1; i < 4; i++) {
            int r = row + i;
            int c = col - i;
            if (r < rows && c >= 0 && gameBoard[r][c] == 1) {
                count2++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 4; i++) {
            int r = row - i;
            int c = col + i;
            if (r >= 0 && c < cols && gameBoard[r][c] == 1) {
                count2++;
            } else {
                break;
            }
        }
        
        if (count2 >= 3) {
            
            for (int i = 1; i <= 3; i++) {
                int r = row + i;
                int c = col - i;
                if (r >= 0 && r < rows && c >= 0 && c < cols && gameBoard[r][c] == 0) {
                    if (board.isValidMove(c)) {
                        return c;
                    }
                }
            }
            for (int i = 1; i <= 3; i++) {
                int r = row - i;
                int c = col + i;
                if (r >= 0 && r < rows && c >= 0 && c < cols && gameBoard[r][c] == 0) {
                    if (board.isValidMove(c)) {
                        return c;
                    }
                }
            }
        }
        
        return -1;
    }
    
    public boolean hasThreeInRow(GameBoard board, int row, int col, int player) { // O(1) - 4 calls to countConsecutive, each O(1)
        int[][] gameBoard = board.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();

        // Check all directions - O(1)
        if (countConsecutive(gameBoard, row, col, 0, 1, player, rows, cols) >= 3 ||
            countConsecutive(gameBoard, row, col, 1, 0, player, rows, cols) >= 3 ||
            countConsecutive(gameBoard, row, col, 1, 1, player, rows, cols) >= 3 ||
            countConsecutive(gameBoard, row, col, 1, -1, player, rows, cols) >= 3) {
            return true;
        }

        return false;
    }
    
    public int countConsecutive(int[][] board, int row, int col, int deltaRow, int deltaCol, int player, int rows, int cols) { // O(1) - bounded by board dimensions
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
    
}
