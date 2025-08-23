public class Move{

    public String from, to;

    public Move(String from, String to){
        this.from = from;
        this.to = to;
    }

    public String toString() {
        return from + " → " + to;
    }

}