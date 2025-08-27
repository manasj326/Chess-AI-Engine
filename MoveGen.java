import java.util.ArrayList;
import java.util.List;

public final class MoveGen {
    private MoveGen() {}

    public static int[] convert(String pos) {
        int col = pos.charAt(0) - 'a';
        int row = 8 - Character.getNumericValue(pos.charAt(1));
        return new int[] { row, col };
    }

    public static String toPosition(int row, int col) {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }

    public static boolean pawnLegalMove(ChessBoard cb, String fromPos, String toPos) {
        String[][] board = cb.board;
        int[] from = convert(fromPos), to = convert(toPos);
        int fr = from[0], fc = from[1], tr = to[0], tc = to[1];
        String piece = board[fr][fc];

        int direction = piece.equals("P") ? -1 : 1;
        int startRow  = piece.equals("P") ? 6 : 1;
        boolean isWhite = piece.equals("P");

        if (fc == tc && tr == fr + direction && board[tr][tc].equals(".")) return true;

        if (fc == tc && fr == startRow && tr == fr + 2 * direction
                && board[fr + direction][fc].equals(".") && board[tr][tc].equals(".")) return true;

        if ((tc == fc - 1 || tc == fc + 1) && tr == fr + direction) {
            String target = board[tr][tc];
            if (!target.equals(".")) {
                if (isWhite && target.equals(target.toLowerCase())) return true;
                if (!isWhite && target.equals(target.toUpperCase())) return true;
            }
        }
        return false;
    }

    public static boolean rookLegalMove(ChessBoard cb, String fromPos, String toPos) {
        String[][] board = cb.board;
        int[] from = convert(fromPos), to = convert(toPos);
        int fr = from[0], fc = from[1], tr = to[0], tc = to[1];
        String piece = board[fr][fc], target = board[tr][tc];
        boolean isWhite = piece.equals(piece.toUpperCase());

        if (fr != tr && fc != tc) return false;

        if (fr == tr) {
            int step = (tc > fc) ? 1 : -1;
            for (int c = fc + step; c != tc; c += step)
                if (!board[fr][c].equals(".")) return false;
        } else {
            int step = (tr > fr) ? 1 : -1;
            for (int r = fr + step; r != tr; r += step)
                if (!board[r][fc].equals(".")) return false;
        }

        if (target.equals(".")) return true;
        if (isWhite && target.equals(target.toLowerCase())) return true;
        if (!isWhite && target.equals(target.toUpperCase())) return true;
        return false;
    }

    public static boolean knightLegalMove(ChessBoard cb, String fromPos, String toPos) {
        String[][] board = cb.board;
        int[] from = convert(fromPos), to = convert(toPos);
        int fr = from[0], fc = from[1], tr = to[0], tc = to[1];
        String piece = board[fr][fc], target = board[tr][tc];
        boolean isWhite = piece.equals(piece.toUpperCase());

        int rd = Math.abs(tr - fr), cd = Math.abs(tc - fc);
        if (!((rd == 2 && cd == 1) || (rd == 1 && cd == 2))) return false;

        if (target.equals(".")) return true;
        if (isWhite && target.equals(target.toLowerCase())) return true;
        if (!isWhite && target.equals(target.toUpperCase())) return true;
        return false;
    }

    public static boolean bishopLegalMove(ChessBoard cb, String fromPos, String toPos) {
        String[][] board = cb.board;
        int[] from = convert(fromPos), to = convert(toPos);
        int fr = from[0], fc = from[1], tr = to[0], tc = to[1];
        String piece = board[fr][fc], target = board[tr][tc];
        boolean isWhite = piece.equals(piece.toUpperCase());

        int rd = Math.abs(tr - fr), cd = Math.abs(tc - fc);
        if (rd != cd) return false;

        int rs = (tr > fr) ? 1 : -1, cs = (tc > fc) ? 1 : -1;
        for (int r = fr + rs, c = fc + cs; r != tr && c != tc; r += rs, c += cs)
            if (!board[r][c].equals(".")) return false;

        if (target.equals(".")) return true;
        if (isWhite && target.equals(target.toLowerCase())) return true;
        if (!isWhite && target.equals(target.toUpperCase())) return true;
        return false;
    }

    public static boolean queenLegalMove(ChessBoard cb, String fromPos, String toPos) {
        return rookLegalMove(cb, fromPos, toPos) || bishopLegalMove(cb, fromPos, toPos);
    }

    public static boolean kingLegalMove(ChessBoard cb, String fromPos, String toPos) {
        String[][] board = cb.board;
        int[] from = convert(fromPos), to = convert(toPos);
        int fr = from[0], fc = from[1], tr = to[0], tc = to[1];
        String piece = board[fr][fc], target = board[tr][tc];
        boolean isWhite = piece.equals(piece.toUpperCase());

        int rd = Math.abs(tr - fr), cd = Math.abs(tc - fc);
        if (rd <= 1 && cd <= 1) {
            if (target.equals(".")) return true;
            if (isWhite && target.equals(target.toLowerCase())) return true;
            if (!isWhite && target.equals(target.toUpperCase())) return true;
        }
        return false;
    }

    public static boolean isLegalMove(ChessBoard cb, String piece, String fromPos, String toPos) {
        if (piece.equals("P") || piece.equals("p")) return pawnLegalMove(cb, fromPos, toPos);
        if (piece.equals("R") || piece.equals("r")) return rookLegalMove(cb, fromPos, toPos);
        if (piece.equals("N") || piece.equals("n")) return knightLegalMove(cb, fromPos, toPos);
        if (piece.equals("B") || piece.equals("b")) return bishopLegalMove(cb, fromPos, toPos);
        if (piece.equals("Q") || piece.equals("q")) return queenLegalMove(cb, fromPos, toPos);
        if (piece.equals("K") || piece.equals("k")) return kingLegalMove(cb, fromPos, toPos);
        return false;
    }

    public static List<Move> getAllLegalMoves(ChessBoard cb, String playerColor) {
        List<Move> legalMoves = new ArrayList<>();
        boolean isWhite = playerColor.equals("white");

        for (int fr = 0; fr < 8; fr++) {
            for (int fc = 0; fc < 8; fc++) {
                String piece = cb.board[fr][fc];
                if (piece.equals(".")) continue;
                if (isWhite && !piece.equals(piece.toUpperCase())) continue;
                if (!isWhite && !piece.equals(piece.toLowerCase())) continue;

                String fromPos = toPosition(fr, fc);
                for (int tr = 0; tr < 8; tr++) {
                    for (int tc = 0; tc < 8; tc++) {
                        if (fr == tr && fc == tc) continue;
                        String toPos = toPosition(tr, tc);
                        if (isLegalMove(cb, piece, fromPos, toPos)) {
                            legalMoves.add(new Move(fromPos, toPos));
                        }
                    }
                }
            }
        }
        return legalMoves;
    }
}
