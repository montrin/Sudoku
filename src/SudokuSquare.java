/**
 * Created by monicatrink on 16/04/16.
 */
public class SudokuSquare {

    private int row;
    private int col;
    private int value;

    public SudokuSquare(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setValue(int v) {
        this.value =v;
    }
    public int getValue() {
        return value;
    }
}
