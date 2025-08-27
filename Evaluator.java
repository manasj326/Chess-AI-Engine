import java.util.Map;

public final class Evaluator {
    private Evaluator() {}

    public static final Map<String, Integer> PIECE_VALUES = Map.ofEntries(
        Map.entry("P", 100), Map.entry("N", 300), Map.entry("B", 300),
        Map.entry("R", 500), Map.entry("Q", 900), Map.entry("K", 10000),
        Map.entry("p", 100), Map.entry("n", 300), Map.entry("b", 300),
        Map.entry("r", 500), Map.entry("q", 900), Map.entry("k", 10000)
    );

    public static final int[][] PAWN_TABLE = {
        {0,0,0,0,0,0,0,0},{5,5,5,5,5,5,5,5},{1,1,2,3,3,2,1,1},{0,0,0,2,2,0,0,0},
        {0,0,0,-2,-2,0,0,0},{1,-1,-2,0,0,-2,-1,1},{1,2,2,-2,-2,2,2,1},{0,0,0,0,0,0,0,0}
    };
    public static final int[][] KNIGHT_TABLE = {
        {-5,-4,-3,-3,-3,-3,-4,-5},{-4,-2,0,0,0,0,-2,-4},{-3,0,1,1,1,1,0,-3},
        {-3,0,1,2,2,1,0,-3},{-3,0,1,2,2,1,0,-3},{-3,0,1,1,1,1,0,-3},
        {-4,-2,0,0,0,0,-2,-4},{-5,-4,-3,-3,-3,-3,-4,-5}
    };
    public static final int[][] KING_TABLE = {
        {-3,-4,-4,-5,-5,-4,-4,-3},{-3,-4,-4,-5,-5,-4,-4,-3},
        {-3,-4,-4,-5,-5,-4,-4,-3},{-3,-4,-4,-5,-5,-4,-4,-3},
        {-2,-3,-3,-4,-4,-3,-3,-2},{-1,-2,-2,-2,-2,-2,-2,-1},
        {2,2,0,0,0,0,2,2},{2,3,1,0,0,1,3,2}
    };
    public static final int[][] BISHOP_TABLE = {
        {-2,-1,-1,-1,-1,-1,-1,-2},{-1,0,0,0,0,0,0,-1},{-1,0,1,1,1,1,0,-1},
        {-1,1,1,1,1,1,1,-1},{-1,1,1,1,1,1,1,-1},{-1,0,1,1,1,1,0,-1},
        {-1,0,0,0,0,0,0,-1},{-2,-1,-1,-1,-1,-1,-1,-2}
    };
    public static final int[][] ROOK_TABLE = {
        {0,0,0,1,1,0,0,0},{-1,0,0,0,0,0,0,-1},{-1,0,0,0,0,0,0,-1},
        {-1,0,0,0,0,0,0,-1},{-1,0,0,0,0,0,0,-1},{-1,0,0,0,0,0,0,-1},
        {1,1,1,1,1,1,1,1},{0,0,0,0,0,0,0,0}
    };
    public static final int[][] QUEEN_TABLE = {
        {-2,-1,-1,-0,-0,-1,-1,-2},{-1,0,0,0,0,0,0,-1},{-1,0,1,1,1,1,0,-1},
        {0,0,1,1,1,1,0,-0},{0,0,1,1,1,1,0,-0},{-1,1,1,1,1,1,0,-1},
        {-1,0,1,0,0,0,0,-1},{-2,-1,-1,-0,-0,-1,-1,-2}
    };

    private static int pv(String p) {
        switch (p) {
            case "p": return 1;
            case "n": return 3;
            case "b": return 3;
            case "r": return 5;
            case "q": return 9;
            case "k": return 1000;
            default:  return 0;
        }
    }

    public static int evaluateBoard(String[][] board) {
        int score = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String piece = board[row][col];
                if (piece.equals(".")) continue;

                int value = pv(piece.toLowerCase());

                if (piece.equals("P"))      value += PAWN_TABLE[row][col];
                else if (piece.equals("p")) value += PAWN_TABLE[7 - row][col];
                else if (piece.equals("N")) value += KNIGHT_TABLE[row][col];
                else if (piece.equals("n")) value += KNIGHT_TABLE[7 - row][col];
                else if (piece.equals("B")) value += BISHOP_TABLE[row][col];
                else if (piece.equals("b")) value += BISHOP_TABLE[7 - row][col];
                else if (piece.equals("R")) value += ROOK_TABLE[row][col];
                else if (piece.equals("r")) value += ROOK_TABLE[7 - row][col];
                else if (piece.equals("K")) value += KING_TABLE[row][col];
                else if (piece.equals("k")) value += KING_TABLE[7 - row][col];
                else if (piece.equals("Q")) value += QUEEN_TABLE[row][col];
                else if (piece.equals("q")) value += QUEEN_TABLE[7 - row][col];

                if (piece.equals(piece.toUpperCase())) score += value;
                else                                    score -= value;
            }
        }
        return score;
    }

    public static int getPieceValue(String piece) { return pv(piece.toLowerCase()); }
}
