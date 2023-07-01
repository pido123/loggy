package src;

public class Move {
    private Position origin;
    private Position destination;

    public Move(Position origin, Position destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public String toString() {
        return origin.toString() + "-" + destination.toString();
    }

    public Position getOrigin() {
        return origin;
    }

    public Position getDestination() {
        return destination;
    }
}
