import java.util.*;

public final class OpeningBook {
    private OpeningBook() {}

    public static Map<String, List<Move>> create() {
        Map<String, List<Move>> openingBook = new HashMap<>();

        openingBook.put("", Arrays.asList(
                new Move("e2","e4"), new Move("d2","d4"),
                new Move("c2","c4"), new Move("g1","f3")));

        openingBook.put("e2e4", Arrays.asList(
                new Move("e7","e5"), new Move("c7","c5"), new Move("e7","e6")));

        openingBook.put("e2e4 e7e5", Arrays.asList(
                new Move("g1","f3"), new Move("f1","c4")));

        openingBook.put("e2e4 c7c5", Arrays.asList(
                new Move("g1","f3"), new Move("d2","d4")));

        openingBook.put("e2e4 e7e5 g1f3", Arrays.asList(
                new Move("b8","c6"), new Move("d7","d6")));

        openingBook.put("e2e4 e7e5 g1f3 b8c6", Arrays.asList(
                new Move("f1","c4")));

        openingBook.put("e2e4 e7e5 g1f3 d7d6", Arrays.asList(
                new Move("d2","d4")));

        openingBook.put("d2d4", Arrays.asList(
                new Move("d7","d5"), new Move("g8","f6")));

        openingBook.put("d2d4 d7d5", Arrays.asList(new Move("c2","c4")));

        openingBook.put("d2d4 g8f6", Arrays.asList(
                new Move("c2","c4"), new Move("g1","f3")));

        openingBook.put("d2d4 d7d5 c2c4", Arrays.asList(
                new Move("e7","e6"), new Move("c7","c6")));

        openingBook.put("d2d4 d7d5 c2c4 e7e6", Arrays.asList(
                new Move("g1","f3"), new Move("b1","c3")));

        openingBook.put("d2d4 d7d5 c2c4 c7c6", Arrays.asList(
                new Move("g1","f3")));

        return openingBook;
    }
}
