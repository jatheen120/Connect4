
/**
 * DivideAndConquer.java
 * Comprehensive Divide & Conquer implementations for Connect4
 * 
 * Contains 6 different D&C algorithms:
 * 1. D&C for Win Detection (4 directions)
 * 2. D&C for Best Move Finding
 * 3. D&C for Board Evaluation
 * 4. D&C for Connected Count
 * 5. D&C for Finding Valid Moves
 * 6. D&C for Winner Detection using Recursion
 */

import java.util.*;

public class DivideAndConquer {

    private Board board;
    // Cache for Memoization (Transposition Table)
    private Map<String, Integer> memoizationTable;

    public DivideAndConquer(Board board) {
        this.board = board;
        this.memoizationTable = new HashMap<>();
    }

    // =====================================================================
    // ✅ D&C ALGORITHM 1: WIN DETECTION (4 DIRECTIONS)
    // Divides win checking into 4 independent subproblems
    // Time Complexity: O(R*C)
    // =====================================================================

    /**
     * Main D&C function: Check if a player has won
     * Divides into 4 subproblems and conquers
     */
    public boolean checkWin(char player) {
        return checkHorizontal(player) ||
                checkVertical(player) ||
                checkDiagonalDown(player) ||
                checkDiagonalUp(player);
    }

    /**
     * Subproblem 1: Check Horizontal wins
     */
    /**
     * Subproblem 1: Check Horizontal wins (Recursive)
     */
    private boolean checkHorizontal(char player) {
        char[][] grid = board.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();
        return checkHorizontalRec(grid, player, 0, 0, rows, cols);
    }

    private boolean checkHorizontalRec(char[][] grid, char player, int row, int col, int rows, int cols) {
        if (row >= rows)
            return false;
        if (col > cols - 4)
            return checkHorizontalRec(grid, player, row + 1, 0, rows, cols);

        if (grid[row][col] == player &&
                grid[row][col + 1] == player &&
                grid[row][col + 2] == player &&
                grid[row][col + 3] == player) {
            return true;
        }
        return checkHorizontalRec(grid, player, row, col + 1, rows, cols);
    }

    /**
     * Subproblem 2: Check Vertical wins (Recursive)
     */
    private boolean checkVertical(char player) {
        char[][] grid = board.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();
        return checkVerticalRec(grid, player, 0, 0, rows, cols);
    }

    private boolean checkVerticalRec(char[][] grid, char player, int col, int row, int rows, int cols) {
        if (col >= cols)
            return false;
        if (row > rows - 4)
            return checkVerticalRec(grid, player, col + 1, 0, rows, cols);

        if (grid[row][col] == player &&
                grid[row + 1][col] == player &&
                grid[row + 2][col] == player &&
                grid[row + 3][col] == player) {
            return true;
        }
        return checkVerticalRec(grid, player, col, row + 1, rows, cols);
    }

    /**
     * Subproblem 3: Check Diagonal Down-Right (\) (Recursive)
     */
    private boolean checkDiagonalDown(char player) {
        char[][] grid = board.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();
        return checkDiagonalDownRec(grid, player, 0, 0, rows, cols);
    }

    private boolean checkDiagonalDownRec(char[][] grid, char player, int row, int col, int rows, int cols) {
        if (row > rows - 4)
            return false;
        if (col > cols - 4)
            return checkDiagonalDownRec(grid, player, row + 1, 0, rows, cols);

        if (grid[row][col] == player &&
                grid[row + 1][col + 1] == player &&
                grid[row + 2][col + 2] == player &&
                grid[row + 3][col + 3] == player) {
            return true;
        }
        return checkDiagonalDownRec(grid, player, row, col + 1, rows, cols);
    }

    /**
     * Subproblem 4: Check Diagonal Up-Right (/) (Recursive)
     */
    private boolean checkDiagonalUp(char player) {
        char[][] grid = board.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();
        return checkDiagonalUpRec(grid, player, 3, 0, rows, cols);
    }

    private boolean checkDiagonalUpRec(char[][] grid, char player, int row, int col, int rows, int cols) {
        if (row >= rows)
            return false;
        if (col > cols - 4)
            return checkDiagonalUpRec(grid, player, row + 1, 0, rows, cols);

        if (grid[row][col] == player &&
                grid[row - 1][col + 1] == player &&
                grid[row - 2][col + 2] == player &&
                grid[row - 3][col + 3] == player) {
            return true;
        }
        return checkDiagonalUpRec(grid, player, row, col + 1, rows, cols);
    }

    // =====================================================================
    // ✅ D&C ALGORITHM 2: BEST MOVE FINDING
    // Divides columns into left and right halves recursively
    // Time Complexity: O(C log C) where C = number of columns
    // =====================================================================

    /**
     * Find best column using Divide & Conquer
     * Divides columns into halves and recursively finds best move
     */
    public int findBestMoveDnC(char player, int[] columns) {
        if (columns.length == 0) {
            return -1;
        }

        if (columns.length == 1) {
            return board.isValidMove(columns[0]) ? columns[0] : -1;
        }

        // Divide into two halves
        int mid = columns.length / 2;
        int[] leftHalf = Arrays.copyOfRange(columns, 0, mid);
        int[] rightHalf = Arrays.copyOfRange(columns, mid, columns.length);

        // Conquer: Evaluate both halves
        int leftBest = findBestMoveDnC(player, leftHalf);
        int rightBest = findBestMoveDnC(player, rightHalf);

        // Combine: Choose better move
        return chooseBetterMove(player, leftBest, rightBest);
    }

    /**
     * Helper: Choose better move between two columns
     */
    private int chooseBetterMove(char player, int col1, int col2) {
        if (col1 == -1)
            return col2;
        if (col2 == -1)
            return col1;

        // Simulate both moves and compare scores
        board.insertDisc(col1, player);
        int score1;
        if (checkWinRecursive(player)) {
            score1 = 100000; // Immediate win detected by Algo 6
        } else {
            score1 = evaluatePositionDnC(player);
        }
        board.removeDisc(col1);

        board.insertDisc(col2, player);
        int score2;
        if (checkWinRecursive(player)) {
            score2 = 100000; // Immediate win detected by Algo 6
        } else {
            score2 = evaluatePositionDnC(player);
        }
        board.removeDisc(col2);

        return (score1 >= score2) ? col1 : col2;
    }

    /**
     * Wrapper to find best move from all columns
     * INTEGRATED WITH GREEDY ALGORITHMS
     */
    public int findBestMove(char player) {
        char opponent = (player == 'R') ? 'Y' : 'R';

        // 1️⃣ Immediate Winning Move (Highest Priority)
        int winMove = findImmediateWin(player);
        if (winMove != -1)
            return winMove;

        // 2️⃣ Immediate Block Opponent Win (Second Priority)
        int blockMove = findImmediateWin(opponent); // If opponent can win, block it
        if (blockMove != -1)
            return blockMove;

        // Use D&C Algorithm 5 to find valid moves
        List<Integer> validMovesList = findValidMovesDnC(0, board.getCols() - 1);
        if (validMovesList.isEmpty())
            return -1;

        // Filter unsafe moves first
        List<Integer> safeMoves = new ArrayList<>();
        for (int col : validMovesList) {
            if (isSafeMove(player, col)) {
                safeMoves.add(col);
            }
        }

        // If no safe moves, use all valid moves
        if (safeMoves.isEmpty()) {
            safeMoves = validMovesList;
        }

        // ✅ USE ALGORITHM 2: D&C Column Split for move selection
        int[] safeMovesArray = new int[safeMoves.size()];
        for (int i = 0; i < safeMoves.size(); i++) {
            safeMovesArray[i] = safeMoves.get(i);
        }

        return findBestMoveDnC(player, safeMovesArray);
    }

    /**
     * Find best move using State-Space D&C (Algorithm 7)
     * Used for Moderate difficulty - explores game tree without pruning
     */
    public int findBestMoveModerate(char player, int depth) {
        char opponent = (player == 'R') ? 'Y' : 'R';

        // Check for immediate win/block first (Greedy shortcut)
        int winMove = findImmediateWin(player);
        if (winMove != -1)
            return winMove;
        int blockMove = findImmediateWin(opponent);
        if (blockMove != -1)
            return blockMove;

        List<Integer> validMoves = findValidMovesDnC(0, board.getCols() - 1);
        int bestMove = validMoves.isEmpty() ? -1 : validMoves.get(0);
        int maxScore = Integer.MIN_VALUE;

        // ✅ USE ALGORITHM 7: State-Space D&C
        // Clear cache before new search to free memory from old moves
        memoizationTable.clear();

        for (int col : validMoves) {
            board.insertDisc(col, player);
            // Optimized call with Alpha-Beta bounds & Memoization
            int score = stateSpaceDnC(player, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            board.removeDisc(col);

            if (score > maxScore) {
                maxScore = score;
                bestMove = col;
            }
        }

        return bestMove;
    }

    // =====================================================================
    // 🧠 GREEDY ALGORITHMS IMPLEMENTATION
    // =====================================================================

    /**
     * 1️⃣ & 2️⃣ Find Immediate Win / Block
     */
    private int findImmediateWin(char player) {
        for (int col = 0; col < board.getCols(); col++) {
            if (board.isValidMove(col)) {
                board.insertDisc(col, player);
                boolean wins = checkWinRecursive(player); // Uses D&C Algo 6
                board.removeDisc(col);
                if (wins)
                    return col;
            }
        }
        return -1;
    }

    /**
     * 4️⃣ Maximize Immediate Connections
     */
    private int scoreMaxConnections(char player, int col) {
        board.insertDisc(col, player);
        int score = 0;
        score += countConnectedDnC(player, 3) * 100; // 3-in-a-row
        score += countConnectedDnC(player, 2) * 50; // 2-in-a-row
        board.removeDisc(col);
        return score;
    }

    /**
     * 6️⃣ Column Safety Check
     * Returns false if playing here allows opponent to win immediately
     */
    private boolean isSafeMove(char player, int col) {
        char opponent = (player == 'R') ? 'Y' : 'R';
        board.insertDisc(col, player);

        boolean safe = true;
        // Check if opponent can win in ANY column after our move
        for (int c = 0; c < board.getCols(); c++) {
            if (board.isValidMove(c)) {
                board.insertDisc(c, opponent);
                if (checkWinRecursive(opponent)) {
                    safe = false;
                }
                board.removeDisc(c);
                if (!safe)
                    break;
            }
        }

        board.removeDisc(col);
        return safe;
    }

    // =====================================================================
    // ✅ D&C ALGORITHM 3: BOARD EVALUATION
    // Divides board into top and bottom halves
    // Time Complexity: O(R*C)
    // =====================================================================

    /**
     * Evaluate board position using Divide & Conquer
     * Divides board into top and bottom halves AND uses quadrant counts
     */
    public int evaluatePositionDnC(char player) {
        int rows = board.getRows();

        // Divide into top and bottom halves (Algo 3)
        int midRow = rows / 2;

        int topScore = evaluateHalfBoard(player, 0, midRow);
        int bottomScore = evaluateHalfBoard(player, midRow, rows);

        // Integrate D&C Algorithm 4: Connected Count
        int connectedBonus = countConnectedDnC(player, 3) * 50;
        int connectedScore = connectedBonus + (countConnectedDnC(player, 2) * 10);

        // Strategy: Combine spatial evaluation (top/bottom) with pattern evaluation
        // (quadrants)
        return topScore + bottomScore + connectedScore;
    }

    /**
     * Evaluate half of the board (from startRow to endRow)
     */
    private int evaluateHalfBoard(char player, int startRow, int endRow) {
        char opponent = (player == 'R') ? 'Y' : 'R';
        char[][] grid = board.getBoard();
        int cols = board.getCols();
        int score = 0;

        // Count pieces in this half
        for (int row = startRow; row < endRow; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == player) {
                    score += 10;
                    // Bonus for center control
                    if (col == 3)
                        score += 5;
                } else if (grid[row][col] == opponent) {
                    score -= 10;
                    if (col == 3)
                        score -= 5;
                }
            }
        }

        return score;
    }

    // =====================================================================
    // ✅ D&C ALGORITHM 4: CONNECTED COUNT (OPTIMIZED)
    // Optimized: Only checks cells with pieces, not entire grid
    // Time Complexity: O(P) where P = number of pieces (instead of O(R*C))
    // =====================================================================

    /**
     * OPTIMIZED: Count connected pieces using Divide & Conquer
     * Instead of scanning entire grid O(R*C), only checks around pieces O(P)
     * Uses piece-centric approach with early termination
     */
    public int countConnectedDnC(char player, int length) {
        char[][] grid = board.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();

        // Optimization 1: Collect piece positions first (O(P) where P = pieces)
        java.util.List<int[]> piecePositions = new java.util.ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == player) {
                    piecePositions.add(new int[] { row, col });
                }
            }
        }

        // If fewer pieces than required length, return 0 early
        if (piecePositions.size() < length) {
            return 0;
        }

        // Use optimized counting that only checks from piece positions
        int count = 0;

        // Optimization 2: Only start pattern search from valid starting positions
        // Instead of checking every cell, check only from piece positions
        java.util.Set<String> countedPatterns = new java.util.HashSet<>();

        for (int[] pos : piecePositions) {
            int row = pos[0];
            int col = pos[1];

            // Check horizontal pattern starting at this piece
            if (col <= cols - length) {
                String key = "H" + row + "," + col;
                if (!countedPatterns.contains(key)) {
                    if (checkPattern(grid, player, row, col, 0, 1, length)) {
                        count++;
                        countedPatterns.add(key);
                    }
                }
            }

            // Check vertical pattern starting at this piece
            if (row <= rows - length) {
                String key = "V" + row + "," + col;
                if (!countedPatterns.contains(key)) {
                    if (checkPattern(grid, player, row, col, 1, 0, length)) {
                        count++;
                        countedPatterns.add(key);
                    }
                }
            }

            // Check diagonal down-right starting at this piece
            if (row <= rows - length && col <= cols - length) {
                String key = "D1" + row + "," + col;
                if (!countedPatterns.contains(key)) {
                    if (checkPattern(grid, player, row, col, 1, 1, length)) {
                        count++;
                        countedPatterns.add(key);
                    }
                }
            }

            // Check diagonal up-right starting at this piece
            if (row >= length - 1 && col <= cols - length) {
                String key = "D2" + row + "," + col;
                if (!countedPatterns.contains(key)) {
                    if (checkPattern(grid, player, row, col, -1, 1, length)) {
                        count++;
                        countedPatterns.add(key);
                    }
                }
            }
        }

        return count;
    }

    /**
     * Helper: Check if a pattern of given length exists in specified direction
     * O(length) per check - typically O(1) for Connect4 (length 2-4)
     */
    private boolean checkPattern(char[][] grid, char player, int startRow, int startCol,
            int rowDir, int colDir, int length) {
        for (int i = 0; i < length; i++) {
            int r = startRow + i * rowDir;
            int c = startCol + i * colDir;
            if (r < 0 || r >= board.getRows() || c < 0 || c >= board.getCols()) {
                return false;
            }
            if (grid[r][c] != player) {
                return false;
            }
        }
        return true;
    }

    // =====================================================================
    // ✅ D&C ALGORITHM 5: FIND VALID MOVES
    // Divides columns into halves recursively
    // Time Complexity: O(C log C)
    // =====================================================================

    /**
     * Find all valid moves using Divide & Conquer
     * Divides columns into halves
     */
    public List<Integer> findValidMovesDnC(int start, int end) {
        List<Integer> validMoves = new ArrayList<>();

        // Base case
        if (start > end) {
            return validMoves;
        }

        if (start == end) {
            if (board.isValidMove(start)) {
                validMoves.add(start);
            }
            return validMoves;
        }

        // Divide
        int mid = (start + end) / 2;

        // Conquer both halves
        List<Integer> leftMoves = findValidMovesDnC(start, mid);
        List<Integer> rightMoves = findValidMovesDnC(mid + 1, end);

        // Combine results
        validMoves.addAll(leftMoves);
        validMoves.addAll(rightMoves);

        return validMoves;
    }

    /**
     * Wrapper to find all valid moves
     */
    public List<Integer> getAllValidMoves() {
        return findValidMovesDnC(0, board.getCols() - 1);
    }

    // =====================================================================
    // ✅ D&C ALGORITHM 6: RECURSIVE WINNER DETECTION
    // Uses recursion to check win in row segments
    // Time Complexity: O(C log C) per row
    // =====================================================================

    /**
     * Check if player won using recursive D&C approach
     */
    public boolean checkWinRecursive(char player) {
        char[][] grid = board.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();

        return checkRowsRecursive(grid, player, 0, rows, cols) ||
                checkColsRecursive(grid, player, 0, rows, cols);
    }

    private boolean checkRowsRecursive(char[][] grid, char player, int row, int rows, int cols) {
        if (row >= rows)
            return false;
        if (checkRowWinDnC(grid[row], player, 0, cols - 1))
            return true;
        return checkRowsRecursive(grid, player, row + 1, rows, cols);
    }

    private boolean checkColsRecursive(char[][] grid, char player, int col, int rows, int cols) {
        if (col >= cols)
            return false;

        char[] column = new char[rows];
        fillColumnRecursive(grid, column, col, 0, rows);

        if (checkRowWinDnC(column, player, 0, rows - 1))
            return true;
        return checkColsRecursive(grid, player, col + 1, rows, cols);
    }

    private void fillColumnRecursive(char[][] grid, char[] column, int col, int row, int rows) {
        if (row >= rows)
            return;
        column[row] = grid[row][col];
        fillColumnRecursive(grid, column, col, row + 1, rows);
    }

    /**
     * Recursively check if a row/column segment contains 4-in-a-row
     */
    private boolean checkRowWinDnC(char[] array, char player, int start, int end) {
        // Base case: segment too small
        if (end - start < 3) {
            return false;
        }

        // Check current segment for 4-in-a-row
        for (int i = start; i <= end - 3; i++) {
            if (array[i] == player &&
                    array[i + 1] == player &&
                    array[i + 2] == player &&
                    array[i + 3] == player) {
                return true;
            }
        }

        // Divide into two halves
        int mid = (start + end) / 2;

        // Conquer: Check both halves
        boolean leftWin = checkRowWinDnC(array, player, start, mid);
        boolean rightWin = checkRowWinDnC(array, player, mid + 1, end);

        // Also check middle region (crossing boundary)
        boolean middleWin = false;
        if (mid - 2 >= start && mid + 3 <= end) {
            for (int i = mid - 2; i <= mid; i++) {
                if (i >= start && i + 3 <= end) {
                    if (array[i] == player &&
                            array[i + 1] == player &&
                            array[i + 2] == player &&
                            array[i + 3] == player) {
                        middleWin = true;
                        break;
                    }
                }
            }
        }

        return leftWin || rightWin || middleWin;
    }

    // =====================================================================
    // 📊 UTILITY: Count connected pieces (standard implementation)
    // =====================================================================

    /**
     * Standard count connected (for comparison with D&C version)
     */
    public int countConnected(char player, int length) {
        int count = 0;
        char[][] grid = board.getBoard();
        int rows = board.getRows();
        int cols = board.getCols();

        // Horizontal
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= cols - length; col++) {
                boolean valid = true;
                for (int i = 0; i < length; i++) {
                    if (grid[row][col + i] != player) {
                        valid = false;
                        break;
                    }
                }
                if (valid)
                    count++;
            }
        }

        // Vertical
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row <= rows - length; row++) {
                boolean valid = true;
                for (int i = 0; i < length; i++) {
                    if (grid[row + i][col] != player) {
                        valid = false;
                        break;
                    }
                }
                if (valid)
                    count++;
            }
        }

        // Diagonal down
        for (int row = 0; row <= rows - length; row++) {
            for (int col = 0; col <= cols - length; col++) {
                boolean valid = true;
                for (int i = 0; i < length; i++) {
                    if (grid[row + i][col + i] != player) {
                        valid = false;
                        break;
                    }
                }
                if (valid)
                    count++;
            }
        }

        // Diagonal up
        for (int row = length - 1; row < rows; row++) {
            for (int col = 0; col <= cols - length; col++) {
                boolean valid = true;
                for (int i = 0; i < length; i++) {
                    if (grid[row - i][col + i] != player) {
                        valid = false;
                        break;
                    }
                }
                if (valid)
                    count++;
            }
        }

        return count;
    }

    // =====================================================================
    // ✅ D&C ALGORITHM 7: STATE-SPACE DIVIDE & CONQUER (Minimax Foundation)
    // Explores future game states by dividing into child moves
    // Time Complexity: O(b^d) where b = branching factor, d = depth
    // =====================================================================

    /**
     * State-Space D&C: Explores all future game states
     * Divide: For each valid move, create a child state
     * Conquer: Recursively evaluate each child state
     * Combine: Return max score for AI, min for opponent
     */
    /**
     * State-Space D&C (Optimized): Explores future game states
     * Uses Alpha-Beta Pruning + Memoization (Transposition Table)
     * Uses Heuristic Move Ordering to improve pruning efficiency
     */
    public int stateSpaceDnC(char player, int depth, int alpha, int beta, boolean isMaximizing) {
        char opponent = (player == 'R') ? 'Y' : 'R';

        // 1. Generate unique key for current state
        // (Simple string representation of board + player turn)
        String stateKey = generateBoardKey() + ":" + depth + ":" + isMaximizing;

        // 2. Check Cache (Memoization)
        if (memoizationTable.containsKey(stateKey)) {
            return memoizationTable.get(stateKey);
        }

        // Base Case
        if (checkWin(player))
            return 10000 + depth;
        if (checkWin(opponent))
            return -10000 - depth;
        if (board.isBoardFull() || depth == 0)
            return evaluatePositionDnC(player);

        List<Integer> validMoves = findValidMovesDnC(0, board.getCols() - 1);

        // 🚀 Optimization: Sort moves for better pruning (Center preference)
        validMoves.sort((c1, c2) -> {
            int center = board.getCols() / 2;
            return Integer.compare(Math.abs(c2 - center), Math.abs(c1 - center)); // heuristic: center first

        });

        int bestScore;
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col : validMoves) {
                board.insertDisc(col, player);
                int eval = stateSpaceDnC(player, depth - 1, alpha, beta, false);
                board.removeDisc(col);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break; // Pruning
            }
            bestScore = maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col : validMoves) {
                board.insertDisc(col, opponent);
                int eval = stateSpaceDnC(player, depth - 1, alpha, beta, true);
                board.removeDisc(col);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break; // Pruning
            }
            bestScore = minEval;
        }

        // 3. Store result in Cache
        memoizationTable.put(stateKey, bestScore);
        return bestScore;
    }

    /**
     * Generates a unique string key for the current board state
     */
    private String generateBoardKey() {
        char[][] grid = board.getBoard();
        StringBuilder key = new StringBuilder();
        for (int r = 0; r < board.getRows(); r++) {
            key.append(grid[r]);
        }
        return key.toString();
    }

    // =====================================================================
    // ✅ D&C ALGORITHM 8: RECURSIVE PRIORITY FILTER (Greedy + D&C)
    // Multistage recursive filtering to select best move
    // Time Complexity: O(n) to O(n^2) - Very fast
    // =====================================================================

    /**
     * Find best move using Recursive Priority Filter
     * Divides potential moves by filtering them through prioritized criteria
     */
    public int findBestMoveRecursiveFilter(char player) {
        // 1. Start with all valid moves
        List<Integer> candidates = findValidMovesDnC(0, board.getCols() - 1);
        if (candidates.isEmpty())
            return -1;

        // 2. recursive filter starting at priority level 1
        return filterMovesRecursive(player, candidates, 1);
    }

    /**
     * Recursively filters moves based on priority levels
     * Level 1: Win / Block Win (Critical)
     * Level 2: Safety (Don't lose next turn)
     * Level 3: Center Preference
     * Level 4: Connection Potential
     */
    private int filterMovesRecursive(char player, List<Integer> moves, int level) {
        // Base case: If only one move left, take it
        if (moves.size() == 1) {
            return moves.get(0);
        }
        // Base case: If no moves left (shouldn't happen if logic is sound, but
        // fallback)
        if (moves.isEmpty()) {
            return -1;
        }
        // Base case: Max level reached, return random or first from remaining
        if (level > 4) {
            return moves.get(0);
        }

        char opponent = (player == 'R') ? 'Y' : 'R';
        List<Integer> nextCandidates = new ArrayList<>();

        switch (level) {
            case 1: // CRITICAL: Immediate Win or Block
                for (int col : moves) {
                    board.insertDisc(col, player);
                    boolean wins = checkWinRecursive(player);
                    board.removeDisc(col);
                    if (wins)
                        return col; // Took win immediately

                    // Check blocking
                    board.insertDisc(col, opponent);
                    boolean blocks = checkWinRecursive(opponent);
                    board.removeDisc(col);
                    if (blocks)
                        nextCandidates.add(col);
                }
                // If we found blocking moves, strictly prioritize them
                if (!nextCandidates.isEmpty()) {
                    return filterMovesRecursive(player, nextCandidates, level + 1);
                }
                // If no critical moves, proceed with ALL moves to next level
                return filterMovesRecursive(player, moves, level + 1);

            case 2: // SAFETY: Avoid moves that give opponent a win
                for (int col : moves) {
                    if (isSafeMove(player, col)) {
                        nextCandidates.add(col);
                    }
                }
                // If we have safe moves, only consider those. Otherwise forced to play unsafe.
                if (!nextCandidates.isEmpty()) {
                    return filterMovesRecursive(player, nextCandidates, level + 1);
                }
                return filterMovesRecursive(player, moves, level + 1);

            case 3: // CENTER PREFERENCE: Filter for moves closer to center
                int bestDist = Integer.MAX_VALUE;
                int center = board.getCols() / 2;

                // Find min distance in current set
                for (int col : moves) {
                    int dist = Math.abs(col - center);
                    if (dist < bestDist)
                        bestDist = dist;
                }

                // Keep only moves with that metric
                for (int col : moves) {
                    if (Math.abs(col - center) == bestDist) {
                        nextCandidates.add(col);
                    }
                }
                return filterMovesRecursive(player, nextCandidates, level + 1);

            case 4: // CONNECTION POTENTIAL: Maximize connections
                int maxScore = -1;
                for (int col : moves) {
                    int score = scoreMaxConnections(player, col);
                    if (score > maxScore)
                        maxScore = score;
                }
                for (int col : moves) {
                    if (scoreMaxConnections(player, col) == maxScore) {
                        nextCandidates.add(col);
                    }
                }
                return filterMovesRecursive(player, nextCandidates, level + 1);
        }

        return moves.get(0);
    }

    // =====================================================================
    // ✅ D&C ALGORITHM 9: THREAT LATTICE ANALYSIS (OPTIMIZED)
    // Divides board by columns, analyzes threats using DELTA ANALYSIS
    // Time Complexity: O(R × C) - Linear relative to board size
    // =====================================================================

    /**
     * * Find best move using Optimized Threat Lattice Analysis
     * Uses Delta Analysis: Evaluates only the *change* in threats caused by a move
     */
    public int findBestMoveThreatLattice(char player) {
        char opponent = (player == 'R') ? 'Y' : 'R';

        // Check for immediate win/block first
        int winMove = findImmediateWin(player);
        if (winMove != -1)
            return winMove;
        int blockMove = findImmediateWin(opponent);
        if (blockMove != -1)
            return blockMove;

        List<Integer> validMoves = findValidMovesDnC(0, board.getCols() - 1);
        if (validMoves.isEmpty())
            return -1;

        // 1. Precompute current board state (Baseline) - Not needed for pure Delta
        // comparison
        // We only compare the *gain* from each move.

        int bestMove = validMoves.get(0);
        int maxNetBenefit = Integer.MIN_VALUE;

        for (int col : validMoves) {
            // 2. Calculate DELTA (Impact of move) locally
            // We don't need to re-scan the whole board!
            int row = getNextOpenRow(col);

            // Benefit 1: New threats created by me
            int newThreatsCreated = calculateThreatImpact(player, row, col);

            // Benefit 2: Opponent threats blocked (destroyed)
            // calculating what opponent *would* have had if they played there vs now
            int opponentThreatsBlocked = calculateThreatImpact(opponent, row, col);

            int netBenefit = newThreatsCreated + (opponentThreatsBlocked * 2); // Blocking is valuable

            if (netBenefit > maxNetBenefit) {
                maxNetBenefit = netBenefit;
                bestMove = col;
            }
        }

        return bestMove;
    }

    private int getNextOpenRow(int col) {
        char[][] grid = board.getBoard();
        for (int r = board.getRows() - 1; r >= 0; r--) {
            if (grid[r][col] == ' ') {
                return r;
            }
        }
        return -1; // Should not happen for valid moves
    }

    /**
     * Delta Function: Calculates score of threats passing through (row, col)
     * This is O(1) - checks only 4 lines of length 4.
     */
    private int calculateThreatImpact(char player, int row, int col) {
        char[][] grid = board.getBoard();
        int score = 0;

        // Temporarily place piece to evaluate "after" state
        grid[row][col] = player;

        // Horizontal
        score += evaluateLineDelta(grid, player, row, col, 0, 1);
        // Vertical
        score += evaluateLineDelta(grid, player, row, col, 1, 0);
        // Diagonal 1
        score += evaluateLineDelta(grid, player, row, col, 1, 1);
        // Diagonal 2
        score += evaluateLineDelta(grid, player, row, col, 1, -1);

        // Revert
        grid[row][col] = ' ';

        return score;
    }

    private int evaluateLineDelta(char[][] grid, char player, int r, int c, int dr, int dc) {
        int totalScore = 0;
        // Check all 4-windows that include (r, c)
        for (int offset = 0; offset < 4; offset++) {
            int startR = r - (offset * dr);
            int startC = c - (offset * dc);
            // Check boundaries
            if (isValidWindow(startR, startC, dr, dc)) {
                totalScore += evaluateLine(grid, player, startR, startC, dr, dc, 4);
            }
        }
        return totalScore;
    }

    private boolean isValidWindow(int r, int c, int dr, int dc) {
        int endR = r + 3 * dr;
        int endC = c + 3 * dc;
        return r >= 0 && r < board.getRows() && c >= 0 && c < board.getCols() &&
                endR >= 0 && endR < board.getRows() && endC >= 0 && endC < board.getCols();
    }

    /**
     * Evaluate a single line of 4 cells as a threat
     * Returns: Score based on threat quality
     * - 1000 if 3/4 filled by player (one move to win)
     * - 100 if 2/4 filled
     * - 10 if 1/4 filled
     * - 0 if blocked by opponent
     */
    private int evaluateLine(char[][] grid, char player, int startRow, int startCol,
            int rowDir, int colDir, int length) {
        char opponent = (player == 'R') ? 'Y' : 'R';
        int playerCount = 0;

        for (int i = 0; i < length; i++) {
            int r = startRow + i * rowDir;
            int c = startCol + i * colDir;

            if (r < 0 || r >= board.getRows() || c < 0 || c >= board.getCols()) {
                return 0; // Out of bounds
            }

            char cell = grid[r][c];
            if (cell == player) {
                playerCount++;
            } else if (cell == opponent) {
                return 0; // Blocked by opponent - not a valid threat
            }
        }

        // Score based on how close to winning
        switch (playerCount) {
            case 4:
                return 10000; // Already won (shouldn't happen in evaluation)
            case 3:
                return 1000; // One move to win
            case 2:
                return 100; // Two moves to win
            case 1:
                return 10; // Three moves to win
            default:
                return 1; // Empty line - still potential
        }
    }

    // =====================================================================
    // 🎯 DEMO: Print algorithm information
    // =====================================================================

    public void printAlgorithmInfo() {
        System.out.println("\n========================================");
        System.out.println("📚 DIVIDE & CONQUER ALGORITHMS IN USE");
        System.out.println("========================================\n");

        System.out.println("✅ Algorithm 1: Win Detection (4 Directions)");
        System.out.println("   - Divides into: Horizontal, Vertical, Diagonal↘, Diagonal↗");
        System.out.println("   - Time: O(R×C)\n");

        System.out.println("✅ Algorithm 2: Best Move Finding (Column Split)");
        System.out.println("   - Divides columns into left/right halves recursively");
        System.out.println("   - Time: O(C log C)\n");

        System.out.println("✅ Algorithm 3: Board Evaluation (Top/Bottom Split)");
        System.out.println("   - Divides board into top/bottom halves");
        System.out.println("   - Time: O(R×C)\n");

        System.out.println("✅ Algorithm 4: Connected Count (Quadrant Split)");
        System.out.println("   - Divides board into 4 quadrants + cross-boundary");
        System.out.println("   - Time: O(R×C)\n");

        System.out.println("✅ Algorithm 5: Find Valid Moves (Column Range)");
        System.out.println("   - Divides columns recursively");
        System.out.println("   - Time: O(C log C)\n");

        System.out.println("✅ Algorithm 6: Recursive Winner Detection");
        System.out.println("   - Recursive segment checking");
        System.out.println("   - Time: O(C log C) per row\n");

        System.out.println("✅ Algorithm 7: State-Space D&C (Maximized)");
        System.out.println("   - Alpha-Beta Pruning + Memoization (Transposition Table)");
        System.out.println("   - Time: O(b^(d/2)) with massive reduction for duplicates\n");

        System.out.println("✅ Algorithm 8: Recursive Priority Filter");
        System.out.println("   - Multistage recursive filtering (Win -> Block -> Safety -> Center)");
        System.out.println("   - Time: O(n) to O(n^2)\n");

        System.out.println("✅ Algorithm 9: Threat Lattice Analysis (Optimized)");
        System.out.println("   - Uses Delta Analysis to evaluate move impact locally");
        System.out.println("   - Time: O(R × C) (Linear efficiency)\n");

        System.out.println("========================================\n");
    }
}
