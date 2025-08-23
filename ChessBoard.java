import java.util.List;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



class ChessBoard {
String[][] board;
static int nodesSearched;
Map<String, List<Move>> openingBook;
List<String> moveHistory;


public static final Map<String, Integer> PIECE_VALUES = Map.ofEntries(
    Map.entry("P", 100), Map.entry("N", 300), Map.entry("B", 300),
    Map.entry("R", 500), Map.entry("Q", 900), Map.entry("K", 10000),
    Map.entry("p", 100), Map.entry("n", 300), Map.entry("b", 300),
    Map.entry("r", 500), Map.entry("q", 900), Map.entry("k", 10000)
);


final int[][] PAWN_TABLE = {
    { 0,   0,   0,   0,   0,   0,  0,  0 },
    { 5,   5,   5,   5,   5,   5,  5,  5 },
    { 1,   1,   2,   3,   3,   2,  1,  1 },
    { 0,   0,   0,   2,   2,   0,  0,  0 },
    { 0,   0,   0,  -2,  -2,   0,  0,  0 },
    { 1,  -1,  -2,   0,   0,  -2, -1,  1 },
    { 1,   2,   2,  -2,  -2,   2,  2,  1 },
    { 0,   0,   0,   0,   0,   0,  0,  0 }
};

final int[][] KNIGHT_TABLE = {
    {-5, -4, -3, -3, -3, -3, -4, -5},
    {-4, -2,  0,  0,  0,  0, -2, -4},
    {-3,  0,  1,  1,  1,  1,  0, -3},
    {-3,  0,  1,  2,  2,  1,  0, -3},
    {-3,  0,  1,  2,  2,  1,  0, -3},
    {-3,  0,  1,  1,  1,  1,  0, -3},
    {-4, -2,  0,  0,  0,  0, -2, -4},
    {-5, -4, -3, -3, -3, -3, -4, -5}
};

final int[][] KING_TABLE = {
    {-3, -4, -4, -5, -5, -4, -4, -3},
    {-3, -4, -4, -5, -5, -4, -4, -3},
    {-3, -4, -4, -5, -5, -4, -4, -3},
    {-3, -4, -4, -5, -5, -4, -4, -3},
    {-2, -3, -3, -4, -4, -3, -3, -2},
    {-1, -2, -2, -2, -2, -2, -2, -1},
    { 2,  2,  0,  0,  0,  0,  2,  2},
    { 2,  3,  1,  0,  0,  1,  3,  2}
};

final int[][] BISHOP_TABLE = {
    {-2, -1, -1, -1, -1, -1, -1, -2},
    {-1,  0,  0,  0,  0,  0,  0, -1},
    {-1,  0,  1,  1,  1,  1,  0, -1},
    {-1,  1,  1,  1,  1,  1,  1, -1},
    {-1,  1,  1,  1,  1,  1,  1, -1},
    {-1,  0,  1,  1,  1,  1,  0, -1},
    {-1,  0,  0,  0,  0,  0,  0, -1},
    {-2, -1, -1, -1, -1, -1, -1, -2}
};

final int[][] ROOK_TABLE = {
    { 0,  0,  0,  1,  1,  0,  0,  0},
    {-1,  0,  0,  0,  0,  0,  0, -1},
    {-1,  0,  0,  0,  0,  0,  0, -1},
    {-1,  0,  0,  0,  0,  0,  0, -1},
    {-1,  0,  0,  0,  0,  0,  0, -1},
    {-1,  0,  0,  0,  0,  0,  0, -1},
    { 1,  1,  1,  1,  1,  1,  1,  1},
    { 0,  0,  0,  0,  0,  0,  0,  0}
};

final int[][] QUEEN_TABLE = {
    {-2, -1, -1, -0, -0, -1, -1, -2},
    {-1,  0,  0,  0,  0,  0,  0, -1},
    {-1,  0,  1,  1,  1,  1,  0, -1},
    { 0,  0,  1,  1,  1,  1,  0, -0},
    { 0,  0,  1,  1,  1,  1,  0, -0},
    {-1,  1,  1,  1,  1,  1,  0, -1},
    {-1,  0,  1,  0,  0,  0,  0, -1},
    {-2, -1, -1, -0, -0, -1, -1, -2}
};

public void initializeOpeningBook() {
    openingBook.put("", Arrays.asList(
        new Move("e2", "e4"),
        new Move("d2", "d4"),
        new Move("c2", "c4"),
        new Move("g1", "f3")
    ));

    // e4 openings
    openingBook.put("e2e4", Arrays.asList(
        new Move("e7", "e5"),
        new Move("c7", "c5"), // Sicilian
        new Move("e7", "e6")  // French
    ));

    openingBook.put("e2e4 e7e5", Arrays.asList(
        new Move("g1", "f3"),
        new Move("f1", "c4") // Italian
    ));

    openingBook.put("e2e4 c7c5", Arrays.asList(
        new Move("g1", "f3"),
        new Move("d2", "d4") // Smith-Morra ideas
    ));

    openingBook.put("e2e4 e7e5 g1f3", Arrays.asList(
        new Move("b8", "c6"),
        new Move("d7", "d6") // Philidor
    ));

    openingBook.put("e2e4 e7e5 g1f3 b8c6", Arrays.asList(
        new Move("f1", "c4") // Italian Game
    ));

    openingBook.put("e2e4 e7e5 g1f3 d7d6", Arrays.asList(
        new Move("d2", "d4") // Philidor follow-up
    ));

    // d4 openings
    openingBook.put("d2d4", Arrays.asList(
        new Move("d7", "d5"),
        new Move("g8", "f6")
    ));

    openingBook.put("d2d4 d7d5", Arrays.asList(
        new Move("c2", "c4") // Queen's Gambit
    ));

    openingBook.put("d2d4 g8f6", Arrays.asList(
        new Move("c2", "c4"),
        new Move("g1", "f3")
    ));

    openingBook.put("d2d4 d7d5 c2c4", Arrays.asList(
        new Move("e7", "e6"), // QGD
        new Move("c7", "c6")  // Slav Defense
    ));

    openingBook.put("d2d4 d7d5 c2c4 e7e6", Arrays.asList(
        new Move("g1", "f3"),
        new Move("b1", "c3")
    ));

    openingBook.put("d2d4 d7d5 c2c4 c7c6", Arrays.asList(
        new Move("g1", "f3")
    ));
}


public ChessBoard(){
    board = new String[8][8];
    openingBook = new HashMap<>();
    moveHistory = new ArrayList<>();
    initializeOpeningBook();
    nodesSearched = 0;
    initalizeBoard();
}

public ChessBoard(ChessBoard other){
        board = new String[8][8];
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                board[i][j] = other.board[i][j];
            }
        }
    }

public void initalizeBoard(){

    // black peices
    board[0] = new String[] {"r", "n", "b", "q", "k", "b", "n", "r"};
    board[1] = new String[] {"p", "p", "p", "p", "p", "p", "p", "p"};

    // empty peices
    for (int i = 2; i <= 5; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ".";
            }
        }

    // white peices
    board[6] = new String[] {"P", "P", "P", "P", "P", "P", "P", "P"};
    board[7] = new String[] {"R", "N", "B", "Q", "K", "B", "N", "R"};

}

public String[][] getBoard() {
    return board;
}

public String getSymbol(String piece) {
final String WHITE = "\u001B[37m";
final String BLACK = "\u001B[31m";
final String RESET = "\u001B[0m";

if (piece.equals(".")) return ".";

String symbol = switch (piece) {
    case "P" -> "♙"; case "p" -> "♟";
    case "R" -> "♖"; case "r" -> "♜";
    case "N" -> "♘"; case "n" -> "♞";
    case "B" -> "♗"; case "b" -> "♝";
    case "Q" -> "♕"; case "q" -> "♛";
    case "K" -> "♔"; case "k" -> "♚";
    default -> piece;
};

return piece.equals(piece.toUpperCase()) ? WHITE + symbol + RESET : BLACK + symbol + RESET;
}


public void printBoard() {
    System.out.println("    a   b   c   d   e   f   g   h");
    System.out.println("  +---+---+---+---+---+---+---+---+");

    for (int i = 0; i < 8; i++) {
        System.out.print((8 - i) + " |");
        for (int j = 0; j < 8; j++) {
            System.out.print(" " + getSymbol(board[i][j]) + " |");
        }
        System.out.println(" " + (8 - i));
        System.out.println("  +---+---+---+---+---+---+---+---+");
    }

    System.out.println("    a   b   c   d   e   f   g   h");
}



public boolean movePiece(String fromPos, String toPos){

    int[] from = convert(fromPos);
    int[] to = convert(toPos);

    int fromRow = from[0];
    int fromCol = from[1];

    int toRow = to[0];
    int toCol = to[1];


    // is peice even on board
    if (board[fromRow][fromCol].equals(".")){
        return false;
    }

    String piece = board[fromRow][fromCol];


    if (piece.equals("P") || piece.equals("p")) {
    if (!pawnLegalMove(fromPos, toPos)) {
        System.out.println("Illegal pawn move.");
        return false;
    }
} else if (piece.equals("R") || piece.equals("r")) {
    if (!rookLegalMove(fromPos, toPos)) {
        System.out.println("Illegal rook move.");
        return false;
    }}
    else if (piece.equals("N") || piece.equals("n")) {
    if (!knightLegalMove(fromPos, toPos)) {
        System.out.println("Illegal knight move.");
        return false;
    }
}
else if (piece.equals("B") || piece.equals("b")) {
    if (!bishopLegalMove(fromPos, toPos)) {
        System.out.println("Illegal bishop move.");
        return false;
    }
}
else if (piece.equals("Q") || piece.equals("q")) {
    if (!queenLegalMove(fromPos, toPos)) {
        System.out.println("Illegal queen move.");
        return false;
    }
}
else if (piece.equals("K") || piece.equals("k")) {
    if (!kingLegalMove(fromPos, toPos)) {
        System.out.println("Illegal king move.");
        return false;
    }
}
else{
    System.out.println("No peice");
}

    // peice moves to new position, original position becomes empty
    board[toRow][toCol] = board[fromRow][fromCol];
    board[fromRow][fromCol] = ".";
    return true;
}


public int[] convert(String pos){
    int col = pos.charAt(0) - 'a';
    int row = 8 - Character.getNumericValue(pos.charAt(1));
    return new int[]{row, col};

}

public boolean pawnLegalMove(String fromPos, String toPos){

    int[] from = convert(fromPos);
    int[] to = convert(toPos);

    int fromRow = from[0];
    int fromCol = from[1];

    int toRow = to[0];
    int toCol = to[1];

    int direction;
    boolean isWhite;
    int startRow;

    String piece = board[fromRow][fromCol];

    // white
    if (piece.equals("P")){
        direction = -1;
        startRow = 6;
        isWhite = true;
    }
    else{
        direction = 1;
        startRow = 1;
        isWhite = false;
    }


    if (fromCol == toCol &&
        toRow == fromRow + direction &&
        board[toRow][toCol].equals(".")) {
        return true;
    }

    if (fromCol == toCol &&
        fromRow == startRow &&
        toRow == fromRow + 2 * direction &&
        board[fromRow + direction][fromCol].equals(".") &&
        board[toRow][toCol].equals(".")) {
        return true;
    }

    if ((toCol == fromCol - 1 || toCol == fromCol + 1) &&
        toRow == fromRow + direction) {
        
        String target = board[toRow][toCol];
        
        if (!target.equals(".")) {
            if (isWhite && target.equals(target.toLowerCase())) {
                return true;
            }
            if (!isWhite && target.equals(target.toUpperCase())) {
                return true;
            }
        }
    }

    return false;
   
}


public boolean rookLegalMove(String fromPos, String toPos){

    /**
     * rook can move horizontal vertical, never diagonal
     * fromRow, toRow should be same or fromCol toCol should be the same
     * nothing in between
     */

    int[] from = convert(fromPos);
    int[] to = convert(toPos);

    int fromRow = from[0];
    int fromCol = from[1];

    int toRow = to[0];
    int toCol = to[1];

    String piece = board[fromRow][fromCol];

    boolean isWhite = piece.equals(piece.toUpperCase());

    String target = board[toRow][toCol];

    if (fromRow != toRow && fromCol != toCol) {
        return false;
    }


   if (fromRow == toRow) {
        int step = (toCol > fromCol) ? 1 : -1;
        for (int c = fromCol + step; c != toCol; c += step) {
            if (!board[fromRow][c].equals(".")) {
                return false;
            }
        }
    } else {
        int step = (toRow > fromRow) ? 1 : -1;
        for (int r = fromRow + step; r != toRow; r += step) {
            if (!board[r][fromCol].equals(".")) {
                return false;
            }
        }
    }

    if (target.equals(".")) {
        return true; 
    } else if (isWhite && target.equals(target.toLowerCase())) {
        return true; 
    } else if (!isWhite && target.equals(target.toUpperCase())) {
        return true; 
    }

    return false; 
}

public boolean knightLegalMove(String fromPos, String toPos) {
    int[] from = convert(fromPos);
    int[] to = convert(toPos);

    int fromRow = from[0];
    int fromCol = from[1];
    int toRow = to[0];
    int toCol = to[1];

    String piece = board[fromRow][fromCol];
    String target = board[toRow][toCol];

    boolean isWhite = piece.equals(piece.toUpperCase());

    int rowDiff = Math.abs(toRow - fromRow);
    int colDiff = Math.abs(toCol - fromCol);

    // Must be an L-shape move: 2 by 1 or 1 by 2
    boolean isLShape = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);

    if (!isLShape) {
        return false;
    }

    // Empty square or capture opponent
    if (target.equals(".")) {
        return true;
    } else if (isWhite && target.equals(target.toLowerCase())) {
        return true;
    } else if (!isWhite && target.equals(target.toUpperCase())) {
        return true;
    }

    return false; // Trying to capture own piece
}

public boolean bishopLegalMove(String fromPos, String toPos) {
    int[] from = convert(fromPos);
    int[] to = convert(toPos);

    int fromRow = from[0];
    int fromCol = from[1];
    int toRow = to[0];
    int toCol = to[1];

    String piece = board[fromRow][fromCol];
    String target = board[toRow][toCol];

    boolean isWhite = piece.equals(piece.toUpperCase());

    int rowDiff = Math.abs(toRow - fromRow);
    int colDiff = Math.abs(toCol - fromCol);

    // Check it's a diagonal move
    if (rowDiff != colDiff) {
        return false;
    }

    int rowStep = (toRow > fromRow) ? 1 : -1;
    int colStep = (toCol > fromCol) ? 1 : -1;

    int r = fromRow + rowStep;
    int c = fromCol + colStep;

    while (r != toRow && c != toCol) {
        if (!board[r][c].equals(".")) {
            return false; // blocked
        }
        r += rowStep;
        c += colStep;
    }

    // Check target square
    if (target.equals(".")) {
        return true;
    } else if (isWhite && target.equals(target.toLowerCase())) {
        return true; // capturing black
    } else if (!isWhite && target.equals(target.toUpperCase())) {
        return true; // capturing white
    }

    return false; // can't capture own piece
}

public boolean queenLegalMove(String fromPos, String toPos) {
    // A queen move is legal if it's a valid rook move or bishop move
    return rookLegalMove(fromPos, toPos) || bishopLegalMove(fromPos, toPos);
}


public boolean kingLegalMove(String fromPos, String toPos){
    int[] from = convert(fromPos);
    int[] to = convert(toPos);

    int fromRow = from[0];
    int fromCol = from[1];

    int toRow = to[0];
    int toCol = to[1];

    String piece = board[fromRow][fromCol];
    String target = board[toRow][toCol];

    boolean isWhite = piece.equals(piece.toUpperCase());

    int rowDiff = Math.abs(toRow - fromRow);
    int colDiff = Math.abs(toCol - fromCol);

    if (rowDiff <= 1 && colDiff <= 1){
        if (target.equals(".")){
            return true;
        } else if (isWhite && target.equals(target.toLowerCase())) {
            return true;
        } else if (!isWhite && target.equals(target.toUpperCase())) {
            return true;
        }
    }

    return false;
    }


    public List<Move> getAllLegalMoves(String playerColor){

        List<Move> legalMoves = new ArrayList<>();


        boolean isWhite = playerColor.equals("white");

        for (int fromRow = 0; fromRow < 8; fromRow++){
            for (int fromCol = 0; fromCol < 8; fromCol++){
                String piece = board[fromRow][fromCol];

                if (piece.equals(".")){
                    continue;
                }
                if (isWhite && !piece.equals(piece.toUpperCase())){
                    continue;
                }
                if (!isWhite && !piece.equals(piece.toLowerCase())){
                    continue;
                }

                String fromPos = toPosition(fromRow, fromCol);

                for (int toRow = 0; toRow < 8; toRow++){
                    for (int toCol = 0; toCol < 8; toCol++){
                        if (fromRow == toRow && fromCol == toCol){
                            continue;
                        }

                        String toPos = toPosition(toRow, toCol);

                        if (isLegalMove(piece, fromPos, toPos)){
                            legalMoves.add(new Move(fromPos, toPos));
                    }
                }
                }
            } 
        }

        return legalMoves;

    }

    public String toPosition(int row, int col){
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }

    public boolean isLegalMove(String piece, String fromPos, String toPos){
        if (piece.equals("P") || piece.equals("p")) return pawnLegalMove(fromPos, toPos);
        if (piece.equals("R") || piece.equals("r")) return rookLegalMove(fromPos, toPos);
        if (piece.equals("N") || piece.equals("n")) return knightLegalMove(fromPos, toPos);
        if (piece.equals("B") || piece.equals("b")) return bishopLegalMove(fromPos, toPos);
        if (piece.equals("Q") || piece.equals("q")) return queenLegalMove(fromPos, toPos);
        if (piece.equals("K") || piece.equals("k")) return kingLegalMove(fromPos, toPos);
        return false;
    }


    public ChessBoard simulateMove (Move move){
        ChessBoard copy = new ChessBoard(this);
        copy.movePiece(move.from, move.to);
        return copy;
    }


    public int evaluateBoard(){
        int score = 0;


        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                String piece = board[row][col];

                if (piece.equals(".")){
                    continue;
                }

                int value = getPieceValue(piece.toLowerCase());

               if (piece.equals("P")) {
    value += PAWN_TABLE[row][col];
} else if (piece.equals("p")) {
    value += PAWN_TABLE[7 - row][col];
} else if (piece.equals("N")) {
    value += KNIGHT_TABLE[row][col];
} else if (piece.equals("n")) {
    value += KNIGHT_TABLE[7 - row][col];
} else if (piece.equals("B")) {
    value += BISHOP_TABLE[row][col];
} else if (piece.equals("b")) {
    value += BISHOP_TABLE[7 - row][col];
} else if (piece.equals("R")) {
    value += ROOK_TABLE[row][col];
} else if (piece.equals("r")) {
    value += ROOK_TABLE[7 - row][col];
} else if (piece.equals("K")) {
    value += KING_TABLE[row][col];
} else if (piece.equals("k")) {
    value += KING_TABLE[7 - row][col];
}
else if (piece.equals("Q")) {
    value += QUEEN_TABLE[row][col];
} else if (piece.equals("q")) {
    value += QUEEN_TABLE[7 - row][col];
}


                if (piece.equals(piece.toUpperCase())){
                    score += value;
                }
                else{
                    score -= value;
                }
            }
        }

        return score;
    }


    public int getPieceValue(String piece){
        switch (piece){
            case "p": return 1;
            case "n": return 3;
            case "b": return 3;
            case "r": return 5;
            case "q": return 9;
            case "k": return 1000;
            default: return 0;

        }
    }

    public int minimax(int depth, int alpha, int beta, boolean isMaximizingPlayer) {
    if (depth == 0) {
        ChessBoard.nodesSearched++;
        return quiescence(alpha, beta, isMaximizingPlayer);
    }

    String player = isMaximizingPlayer ? "white" : "black";
    List<Move> moves = getAllLegalMoves(player);

    if (moves.isEmpty()) {
        return isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }


    moves.sort((a, b) -> {
    int capA = getMVVLVAValue(a);
    int capB = getMVVLVAValue(b);

    if (capA == capB) {
        int evalA = simulateMove(a).evaluateBoard();
        int evalB = simulateMove(b).evaluateBoard();
        return isMaximizingPlayer ? evalB - evalA : evalA - evalB;
    }
    return capB - capA;
});



    if (isMaximizingPlayer) {
        int maxEval = Integer.MIN_VALUE;
        for (Move move : moves) {
            ChessBoard simulated = simulateMove(move);
            int eval = simulated.minimax(depth - 1, alpha, beta, false);
            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);
            if (beta <= alpha) break; // beta cutoff
        }
        return maxEval;
    } else {
        int minEval = Integer.MAX_VALUE;
        for (Move move : moves) {
            ChessBoard simulated = simulateMove(move);
            int eval = simulated.minimax(depth - 1, alpha, beta, true);
            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);
            if (beta <= alpha) break; // alpha cutoff
        }
        return minEval;
    }
}



   public Move findBestMove(int maxDepth, boolean isWhite) {

    String movesCombination = String.join(" ", moveHistory);
    if (openingBook.containsKey(movesCombination)){
        List<Move> bestMoves = openingBook.get(movesCombination);
        return bestMoves.get(new Random().nextInt(bestMoves.size()));
    }


    long startTime = System.currentTimeMillis();
    long timeLimit = 5000; // 5 seconds
    Move bestMove = null;

    for (int d = 1; d <= maxDepth; d++) {
        final int depth = d;
        nodesSearched = 0;
        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime > timeLimit) break;

        List<Move> moves = getAllLegalMoves(isWhite ? "white" : "black");

        // Use parallel stream to evaluate top-level moves concurrently
        bestMove = moves.parallelStream()
            .map(move -> {
                // Time check for each task
                if (System.currentTimeMillis() - startTime > timeLimit) {
                    return null;
                }
                ChessBoard simulated = simulateMove(move);
                int score = simulated.minimax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, !isWhite);
                return new AbstractMap.SimpleEntry<>(move, score);
            })
            .filter(entry -> entry != null) // filter out nulls (timed-out tasks)
            .reduce((a, b) -> {
                if (isWhite) return a.getValue() > b.getValue() ? a : b;
                else return a.getValue() < b.getValue() ? a : b;
            })
            .map(Map.Entry::getKey)
            .orElse(bestMove); // fallback to previous best

        if (bestMove != null) {
            System.out.println("Depth " + depth + " best move: " + bestMove.from + " -> " + bestMove.to);
            System.out.println("Nodes searched at depth " + depth + ": " + nodesSearched);
        }
    }

    long endTime = System.currentTimeMillis();
    System.out.println("AI chose move in " + (endTime - startTime) + " ms");

    return bestMove;
}



public boolean hasNoLegalMoves(boolean isWhite){
    String player = isWhite? "white" : "black";
    List<Move> moves = getAllLegalMoves(player);
    return moves.isEmpty();
}


public int getMVVLVAValue(Move move) {
    int[] from = convert(move.from);
    int[] to = convert(move.to);

    String attacker = board[from[0]][from[1]];
    String victim = board[to[0]][to[1]];

    if (victim.equals(".")) return 0; // not a capture

    int attackerValue = PIECE_VALUES.getOrDefault(attacker, 0);
    int victimValue = PIECE_VALUES.getOrDefault(victim, 0);

    // Return a high score for good captures: victim - attacker
    return victimValue * 10 - attackerValue;
}


public List<Move> getAllCaptures(String player){
    List<Move> allMoves = getAllLegalMoves(player);
    List<Move> allCaptureMoves = new ArrayList<>();

    for (Move move: allMoves){
        int[] to = convert(move.to);
        String target = board[to[0]][to[1]];
        if (!target.equals(".")){
            allCaptureMoves.add(move);
        }
    }

    return allCaptureMoves;
}

public int quiescence(int alpha, int beta, boolean isMaximizingPlayer) {
    ChessBoard.nodesSearched++;
    int standPat = evaluateBoard(); // Static evaluation (no more depth)

    // Stand pat cutoff
    if (isMaximizingPlayer) {
        if (standPat >= beta) return beta;     // Fail-high beta cutoff
        if (standPat > alpha) alpha = standPat;
    } else {
        if (standPat <= alpha) return alpha;   // Fail-low alpha cutoff
        if (standPat < beta) beta = standPat;
    }

    // Generate all capture moves
    List<Move> captures = getAllCaptures(isMaximizingPlayer ? "white" : "black");

    for (Move move : captures) {
        ChessBoard simulated = simulateMove(move);
        int score = simulated.quiescence(alpha, beta, !isMaximizingPlayer);

        if (isMaximizingPlayer) {
            if (score > alpha) alpha = score;
            if (alpha >= beta) break; // Beta cutoff
        } else {
            if (score < beta) beta = score;
            if (beta <= alpha) break; // Alpha cutoff
        }
    }

    return isMaximizingPlayer ? alpha : beta;
}

}








    
