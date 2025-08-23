import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        Scanner scanner = new Scanner(System.in);
        

        boolean isWhiteTurn = true;
        System.out.print("Choose AI difficulty (search depth): ");
        int depth = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        List<Move> allMoves = new ArrayList<>();
        List<String> pieces = new ArrayList<>();
        while (true) {
            board.printBoard();

            if (board.hasNoLegalMoves(isWhiteTurn)) {
                System.out.println((isWhiteTurn ? "White" : "Black") + " has no legal moves.");
                break;
            }

            if (isWhiteTurn) {
                Move bestMove = board.findBestMove(depth, true);
                int[] pos = board.convert(bestMove.from);
                int r = pos[0];
                int c = pos[1];
                String piece = board.getBoard()[r][c];
                System.out.println("You could play " + board.getSymbol(piece) + " " + bestMove.from + " -> " + bestMove.to);
                System.out.print("Enter your move (e.g., e2 e4), or 'exit' to quit: ");
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase("exit")) break;

                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.out.println("Invalid input. Try again.");
                    continue;
                }

                String from = parts[0];
                String to = parts[1];

                board.moveHistory.add(from + to);

                int[] posMove = board.convert(from);
                int rMove = posMove[0];
                int cMove = posMove[1];
                String pieceMove = board.getBoard()[rMove][cMove];
                pieces.add(pieceMove);



                if (!board.movePiece(from, to)) {
                    System.out.println("Illegal move. Try again.");
                    continue;
                }
                else{
                    Move move = new Move(from , to);
                    allMoves.add(move);
                    System.out.println("You moved " + board.getSymbol(pieceMove) + " from " + from + " -> " + to);
                }
            } else {
                System.out.println("AI is thinking...");
                Move bestMove = board.findBestMove(depth, false);
                
                if (bestMove == null) {
                    System.out.println("AI has no legal moves.");
                    break;
                }
                board.moveHistory.add(bestMove.from + bestMove.to);
                allMoves.add(bestMove);

                int[] pos = board.convert(bestMove.from);
                int r = pos[0];
                int c = pos[1];
                String piece = board.getBoard()[r][c];
                pieces.add(piece);


                int[] posCap = board.convert(bestMove.to);
                int rCap = posCap[0];
                int cCap = posCap[1];
                String pieceCap = board.getBoard()[rCap][cCap];
                String statement;
                if (!board.getBoard()[rCap][cCap].equals(".")){
                    statement = "AI plays: " + board.getSymbol(piece) + " moves " + bestMove.from + " -> " + bestMove.to + " and captures " + board.getSymbol(pieceCap);
                }
                else{
                    statement = "AI plays: " + board.getSymbol(piece) + " moves " + bestMove.from + " -> " + bestMove.to;
                }

                board.movePiece(bestMove.from, bestMove.to);
                

                System.out.println(statement);
            }

            isWhiteTurn = !isWhiteTurn;
        }

        System.out.println("Move history:");
for (int i = 0; i < allMoves.size(); i++) {
    String pieceSymbol = board.getSymbol(pieces.get(i));
    Move move = allMoves.get(i);
    String label = (i % 2 == 0 ? ((i / 2) + 1) + ". " : "");
    System.out.println(label + pieceSymbol + move + " ");
    System.out.println();
}
        System.out.println("Game over.");
        scanner.close();
    }
}
