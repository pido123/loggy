package src;

public class Position {
    private int row;
    private int col;

    public Position() {
        row = -1;
        col = -1;
    }

    public Position(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int r) {
        row = r;
    }

    public void setCol(int c) {
        col = c;
    }

    public String toString(){       
        return Case.getCaseNameByIndexes(row,col);
    }

}