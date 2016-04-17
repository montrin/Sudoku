import java.util.ArrayList;

/**
 * Created by monicatrink on 16/04/16.
 */
public class Domain implements Comparable<Domain> {
    int row;
    int col;
    ArrayList<Integer> possibleValues;

    public Domain(int row, int col, ArrayList<Integer> possibleValues) {
        this.row = row;
        this.col = col;
        this.possibleValues = possibleValues;
    }

    public Domain() {
        this.row = 0;
        this.col = 0;
        this.possibleValues = new ArrayList<Integer>();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPossibleValues(ArrayList<Integer> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public ArrayList<Integer> getPossibleValues() {
        return possibleValues;
    }

    @Override
    public int compareTo(Domain o) {
        return this.getPossibleValues().size() - o.getPossibleValues().size();
    }
}
