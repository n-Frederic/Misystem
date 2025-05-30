package SL275.M7.Ex1.banking.domain;

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
