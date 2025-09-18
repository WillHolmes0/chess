package chess.ChessMoveCalculator;

public class PawnMoveCalculator {

    private int x;
    private int y;

    public PawnMoveCalculator (int x, int y) {
       this.x = x;
       this.y = y;
    }
    public boolean isInBounds(int x, int y) {
        return (x > 8 || y > 8) ? false : true;
    }


}
