package SL275.M9.Ex3;

public class OverdraftException extends Exception{
    private double deficit;

    public double getDeficit() {
        return deficit;
    }

    public OverdraftException(String message, double deficit){
        super(message);
        this.deficit=deficit;
    }
}
