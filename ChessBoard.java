import java.util.List;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

class ChessBoard {
    String[][] board;
    static int nodesSearched;
    Map<String, List<Move>> openingBook;
    List<String> moveHistory;

    public ChessBoard() {
        board = new String[8][8];
        openingBook = OpeningBook.create();
        moveHistory = new ArrayList<>();
        nodesSearched = 0;
        initalizeBoard();
    }

    public ChessBoard(ChessBoard other) {
        board = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = other.board[i][j];
            }
        }
        // shallow copy
        this.openingBook = other.openingBook;
        this.moveHistory = new ArrayList<>(other.moveHistory);
    }

    public void initalizeBoard() {
        // black pieces
        board[0] = new String[] { "r", "n", "b", "q", "k", "b", "n", "r" };
        board[1] = new String[] { "p", "p", "p", "p", "p", "p", "p", "p" };

        // empties
        for (int i = 2; i <= 5; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ".";
            }
        }

        // white pieces
        board[6] = new String[] { "P", "P", "P", "P", "P", "P", "P", "P" };
        board[7] = new String[] { "R", "N", "B", "Q", "K", "B", "N", "R" };
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
            case "P" -> "♙";
            case "p" -> "♟";
            case "R" -> "♖";
            case "r" -> "♜";
            case "N" -> "♘";
            case "n" -> "♞";
            case "B" -> "♗";
            case "b" -> "♝";
            case "Q" -> "♕";
            case "q" -> "♛";
            case "K" -> "♔";
            case "k" -> "♚";
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

    public boolean movePiece(String fromPos, String toPos) {
        int[] from = convert(fromPos);
        int[] to = convert(toPos);

        int fromRow = from[0];
        int fromCol = from[1];

        int toRow = to[0];
        int toCol = to[1];

        // check piece exists
        if (board[fromRow][fromCol].equals(".")) {
            return false;
        }

        String piece = board[fromRow][fromCol];

        // delegate legality to MoveGen
        if (piece.equals("P") || piece.equals("p")) {
            if (!pawnLegalMove(fromPos, toPos)) {
                System.out.println("Illegal pawn move.");
                return false;
            }
        } else if (piece.equals("R") || piece.equals("r")) {
            if (!rookLegalMove(fromPos, toPos)) {
                System.out.println("Illegal rook move.");
                return false;
            }
        } else if (piece.equals("N") || piece.equals("n")) {
            if (!knightLegalMove(fromPos, toPos)) {
                System.out.println("Illegal knight move.");
                return false;
            }
        } else if (piece.equals("B") || piece.equals("b")) {
            if (!bishopLegalMove(fromPos, toPos)) {
                System.out.println("Illegal bishop move.");
                return false;
            }
        } else if (piece.equals("Q") || piece.equals("q")) {
            if (!queenLegalMove(fromPos, toPos)) {
                System.out.println("Illegal queen move.");
                return false;
            }
        } else if (piece.equals("K") || piece.equals("k")) {
            if (!kingLegalMove(fromPos, toPos)) {
                System.out.println("Illegal king move.");
                return false;
            }
        } else {
            System.out.println("No piece");
        }

        // make move
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = ".";
        return true;
    }

    public int[] convert(String pos) { return MoveGen.convert(pos); }

    public boolean pawnLegalMove(String fromPos, String toPos) {
        return MoveGen.pawnLegalMove(this, fromPos, toPos);
    }

    public boolean rookLegalMove(String fromPos, String toPos) {
        return MoveGen.rookLegalMove(this, fromPos, toPos);
    }

    public boolean knightLegalMove(String fromPos, String toPos) {
        return MoveGen.knightLegalMove(this, fromPos, toPos);
    }

    public boolean bishopLegalMove(String fromPos, String toPos) {
        return MoveGen.bishopLegalMove(this, fromPos, toPos);
    }

    public boolean queenLegalMove(String fromPos, String toPos) {
        return MoveGen.queenLegalMove(this, fromPos, toPos);
    }

    public boolean kingLegalMove(String fromPos, String toPos) {
        return MoveGen.kingLegalMove(this, fromPos, toPos);
    }

    public String toPosition(int row, int col) {
        return MoveGen.toPosition(row, col);
    }

    public boolean isLegalMove(String piece, String fromPos, String toPos) {
        return MoveGen.isLegalMove(this, piece, fromPos, toPos);
    }

    public List<Move> getAllLegalMoves(String playerColor) {
        return MoveGen.getAllLegalMoves(this, playerColor);
    }

    public ChessBoard simulateMove(Move move) {
        ChessBoard copy = new ChessBoard(this);
        copy.movePiece(move.from, move.to);
        return copy;
    }

    public int evaluateBoard() {
        return Evaluator.evaluateBoard(board);
    }

    public int getPieceValue(String piece) {
        return Evaluator.getPieceValue(piece);
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
        if (openingBook.containsKey(movesCombination)) {
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

            bestMove = moves.parallelStream()
                    .map(move -> {
                        if (System.currentTimeMillis() - startTime > timeLimit) {
                            return null;
                        }
                        ChessBoard simulated = simulateMove(move);
                        int score = simulated.minimax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, !isWhite);
                        return new AbstractMap.SimpleEntry<>(move, score);
                    })
                    .filter(entry -> entry != null)
                    .reduce((a, b) -> {
                        if (isWhite)
                            return a.getValue() > b.getValue() ? a : b;
                        else
                            return a.getValue() < b.getValue() ? a : b;
                    })
                    .map(Map.Entry::getKey)
                    .orElse(bestMove);

            if (bestMove != null) {
                System.out.println("Depth " + depth + " best move: " + bestMove.from + " -> " + bestMove.to);
                System.out.println("Nodes searched at depth " + depth + ": " + nodesSearched);
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("AI chose move in " + (endTime - startTime) + " ms");

        return bestMove;
    }

    public boolean hasNoLegalMoves(boolean isWhite) {
        String player = isWhite ? "white" : "black";
        List<Move> moves = getAllLegalMoves(player);
        return moves.isEmpty();
    }

    public int getMVVLVAValue(Move move) {
        int[] from = convert(move.from);
        int[] to = convert(move.to);

        String attacker = board[from[0]][from[1]];
        String victim = board[to[0]][to[1]];

        if (victim.equals(".")) return 0; // not a capture

        int attackerValue = Evaluator.PIECE_VALUES.getOrDefault(attacker, 0);
        int victimValue = Evaluator.PIECE_VALUES.getOrDefault(victim, 0);

        // Return a high score for good captures: victim - attacker
        return victimValue * 10 - attackerValue;
    }

    public List<Move> getAllCaptures(String player) {
        List<Move> allMoves = getAllLegalMoves(player);
        List<Move> allCaptureMoves = new ArrayList<>();

        for (Move move : allMoves) {
            int[] to = convert(move.to);
            String target = board[to[0]][to[1]];
            if (!target.equals(".")) {
                allCaptureMoves.add(move);
            }
        }
        return allCaptureMoves;
    }

    public int quiescence(int alpha, int beta, boolean isMaximizingPlayer) {
        ChessBoard.nodesSearched++;
        int standPat = evaluateBoard(); // static eval

        if (isMaximizingPlayer) {
            if (standPat >= beta) return beta;
            if (standPat > alpha) alpha = standPat;
        } else {
            if (standPat <= alpha) return alpha;
            if (standPat < beta) beta = standPat;
        }

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
