import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MonicaTrink2 implements SudokuAgent {


    private String name;
    private int height;
    private int width;

    ArrayList<SudokuSquare> positions;
    ArrayList<Domain> csp;
    ArrayList<Integer> initialDomain;
    int[][] workingPuzzle;
    int[][] solution;
    //Unit map
    Map<Integer, ArrayList<SudokuSquare> > map;

    MonicaTrink2() {
        name = "Agent Monica";
    }

    @Override
    public int[][] solve(int dimension, int[][] puzzle) {
        width = dimension * dimension;
        height = dimension * dimension;
        map = setupUnits(dimension);

        workingPuzzle = puzzle;


        if(backtrackingSearchSimple(new SudokuSquare(0,0,0))) {
            return workingPuzzle;
        }

        return puzzle;
    }

    private boolean backtrackingSearchSimple(SudokuSquare s) {
        if(s == null) {
            return true;
        }

        if(workingPuzzle[s.getRow()][s.getCol()] != 0){
            return backtrackingSearchSimple(nextSquare(s));
        }

        //start at one
        for(int i=1;i<=width;i++) {
            boolean isconsistent = isConsistent2(i, s.getRow(), s.getCol());

            if (isconsistent) {
                workingPuzzle[s.getRow()][s.getCol()] = i;
                boolean solved = backtrackingSearchSimple(nextSquare(s));
                // if solved, return, else try other values
                if (solved)
                    return true;
                else
                    workingPuzzle[s.getRow()][s.getCol()] = 0; // reset
                // continue with other possible values

            } else {
                continue;
            }
        }

        return false;
    }

    private SudokuSquare nextSquare(SudokuSquare s) {
        int row = s.getRow();
        int col = s.getCol();

        col++;

        if (col > width-1) {
            col = 0;
            row++;
        }

        // reached end of matrix, return null
        if (row > width-1) {
            return null; // reached end
        }

        SudokuSquare next = new SudokuSquare(row, col,0);
        return next;
    }













    private void initDomain(ArrayList<Integer> initialDomain) {
        initialDomain.add(1);
        initialDomain.add(2);
        initialDomain.add(3);
        initialDomain.add(4);
        initialDomain.add(5);
        initialDomain.add(6);
        initialDomain.add(7);
        initialDomain.add(8);
        initialDomain.add(9);
    }

    private void setConstraints(int[][] puzzle, ArrayList<Integer> initialDomain, ArrayList<Domain> csp) {
        csp.clear();
        //init domains
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (puzzle[i][j] == 0) {
                    csp.add(new Domain(i, j, new ArrayList<Integer>(initialDomain)));
                }
            }
        }
    }

    private void initPositions(int[][] puzzle) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                    positions.add(new SudokuSquare(i,j,puzzle[i][j]));
                }
            }
        }
    private Map<Integer, ArrayList<SudokuSquare>> setupUnits(int dimension) {
        Map<Integer, ArrayList<SudokuSquare>> map = new HashMap<Integer, ArrayList<SudokuSquare>>();

        int mapCounter = 1;

        int startRow = 0;
        int startCol = 0;
        ArrayList<SudokuSquare> posi = new ArrayList<SudokuSquare>();

        int endPos = dimension * dimension - 1;

        int counter = 0;
        while (startCol <= (endPos) && startRow <= (endPos)) {


            for (int i = startRow; i < startRow + dimension; i++) {
                for (int j = startCol; j < startCol + dimension; j++) {
                    posi.add(new SudokuSquare(i, j, 0));
                }
            }

            counter++;

            map.put(mapCounter, new ArrayList<SudokuSquare>(posi));
            mapCounter++;
            posi.clear();
            if(counter < dimension) {
                startCol += dimension;
            }else{
                counter=0;
                startRow += dimension;
                startCol = 0;
            }
        }

        return map;
    }
    private int[][] backtrackingSearch(int[][] assigned, ArrayList<Domain> csp) {
        return backtrack(assigned, csp);
    }

    private int[][] backtrack(int[][] assigned, ArrayList<Domain> constraints) {
        if(isComplete(assigned)) {
            return assigned;
        }

        SudokuSquare s = selectUnassignedVariable();

        for(int v : getDomainValues(s)) {
            if(isConsistent(assigned, v, s.getRow(), s.getCol())) {
                assigned[s.getRow()][s.getCol()] = v;
//                populatePosition(s, v);
                int[][] result = backtrack(assigned.clone(), constraints); //populateCsp(s, v, constraints));
                if(isComplete(result)) {
                    return result;
                }
            }
        }

        return assigned;
    }

    private void populatePosition(SudokuSquare s, int v) {

        for(SudokuSquare ss : positions) {
            if(ss.equals(s)) {
                ss.setValue(v);
            }
        }
    }
    private ArrayList<Domain> populateCsp(SudokuSquare s, int value, ArrayList<Domain> csp) {
        ArrayList<Domain> newOne = new ArrayList<Domain>(csp);
        populateAllRowNeighbours(value, s.getRow(), newOne);
        populateAllColNeighbours(value, s.getCol(), newOne);
        populateAllUnitNeighbours(value, s.getRow(), s.getCol(), map, newOne);
        return newOne;
    }

    private void populateAllUnitNeighbours(int value, int row, int col, Map<Integer, ArrayList<SudokuSquare>> map,
                                           ArrayList<Domain> domains) {

        int searchKey = 0;

        for (Integer key : map.keySet()) {
            ArrayList<SudokuSquare> posi = map.get(key);

            for (SudokuSquare p : posi) {
                if (p.getRow() == row && p.getCol() == col) {
                    searchKey = key;
                }
            }
        }

        if (searchKey != 0) {
            ArrayList<SudokuSquare> posi = map.get(searchKey);
            for (SudokuSquare p : posi) {
                for (Domain d : domains) {
                    if (d.getRow() == p.getRow() && d.getCol() == p.getCol()) {
                        if (d.getPossibleValues().contains(value)) {
                            int i = d.getPossibleValues().indexOf(value);
                            ArrayList<Integer> oldList = d.getPossibleValues();
                            oldList.remove(i);
                            d.setPossibleValues(oldList);
//                            d.getPossibleValues().remove(i);
                        }
                    }
                }
            }
        }
    }

    private void populateAllRowNeighbours(int value, int row, ArrayList<Domain> domains) {

        for (Domain d : domains) {
            if (d.getRow() == row && d.getPossibleValues().size() > 0) {
                if (d.getPossibleValues().contains(value)) {
                    int i = d.getPossibleValues().indexOf(value);
                    ArrayList<Integer> oldList = d.getPossibleValues();
                    oldList.remove(i);
                    d.setPossibleValues(oldList);
//                    d.getPossibleValues().remove(i);
                }
            }
        }


    }

    private void populateAllColNeighbours(int value, int col, ArrayList<Domain> domains) {
        for (Domain d : domains) {
            if (d.getCol() == col && d.getPossibleValues().size() > 0) {
                if (d.getPossibleValues().contains(value)) {
                    int i = d.getPossibleValues().indexOf(value);
                    ArrayList<Integer> oldList = d.getPossibleValues();
                    oldList.remove(i);
                    d.setPossibleValues(oldList);
//                    d.getPossibleValues().remove(i);
                }
            }
        }
    }

    private boolean isConsistent2(int value, int row, int col) {
        return noConstraintsOnRelatedRows2(value, row, col) &&
                noConstraintsOnRelatedCols2(value, row, col) &&
                noConstraintsOnRelatedUnit2(value, row, col);
    }


    private boolean isConsistent(int[][] puzzle, int value, int row, int col) {
        return noConstraintsOnRelatedRows(value, row, col, csp) &&
                noConstraintsOnRelatedCols(value, row, col, csp) &&
                noConstraintsOnRelatedUnit(puzzle, value, row, col, map, csp);
    }

    private boolean noConstraintsOnRelatedUnit(int[][] puzzle, int value, int row, int col, Map<Integer, ArrayList<SudokuSquare>> map, ArrayList<Domain> domains) {
        int searchKey = 0;

        for (Integer key : map.keySet()) {
            ArrayList<SudokuSquare> posi = map.get(key);

            for (SudokuSquare p : posi) {
                if (p.getRow() == row && p.getCol() == col) {
                    searchKey = key;
                }
            }
        }

        if (searchKey != 0) {
            ArrayList<SudokuSquare> posi = map.get(searchKey);
            for (SudokuSquare p : posi) {
                if (p.getRow() != row && p.getCol() != col) {
                    if(puzzle[p.getRow()][p.getCol()] == value){
                        return false;
                    }
                    for (Domain d : domains) {
                        if (d.getRow() == p.getRow() && d.getCol() == p.getCol() && hasValue(d.getPossibleValues(), value)) {
                            return d.getPossibleValues().size() - 1 > 0 ? true : false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean noConstraintsOnRelatedUnit2(int value, int row, int col) {
        int searchKey = 0;

        for (Integer key : map.keySet()) {
            ArrayList<SudokuSquare> posi = map.get(key);

            for (SudokuSquare p : posi) {
                if (p.getRow() == row && p.getCol() == col) {
                    searchKey = key;
                }
            }
        }

        if (searchKey != 0) {
            ArrayList<SudokuSquare> posi = map.get(searchKey);
            for (SudokuSquare p : posi) {
                if (p.getRow() != row && p.getCol() != col) {
                    if(workingPuzzle[p.getRow()][p.getCol()] == value){
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private boolean noConstraintsOnRelatedRows2(int value, int row, int col) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i==row && j!=col) {
                    if(workingPuzzle[i][j] == value) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private boolean noConstraintsOnRelatedCols2(int value, int row, int col) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j==col && i!=row) {
                    if(workingPuzzle[i][j] == value) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean noConstraintsOnRelatedCols(int value, int row, int col, ArrayList<Domain> domains) {
        //check existing values
        for(SudokuSquare pos : positions) {
            if(pos.getRow() != row && pos.getCol() == col && pos.getValue() == value){
                return false;
            }
        }

        for (Domain d : domains) {
            if (d.getCol() == col && d.getRow() != row && hasValue(d.getPossibleValues(), value)) {
                return d.getPossibleValues().size() - 1 > 0 ? true : false;
            }
        }
        return true;
    }

    private boolean noConstraintsOnRelatedRows(int value, int row, int col, ArrayList<Domain> domains) {
        //check existing values
        for(SudokuSquare pos : positions) {
            if(pos.getRow() == row && pos.getCol() != col && pos.getValue() == value){
                return false;
            }
        }

        //possible values in domain
        for (Domain d : domains) {
            if (d.getRow() == row && d.getCol() != col && hasValue(d.getPossibleValues(), value)) {
                return d.getPossibleValues().size() - 1 > 0 ? true : false;
            }
        }
        return true;
    }

    private boolean hasValue(ArrayList<Integer> possibleValues, int value) {
        for (int i : possibleValues) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }


    private ArrayList<Integer> getDomainValues(SudokuSquare s) {
        for(Domain d : csp) {
            if(d.row == s.getRow() && d.col == s.getCol()){
                return d.getPossibleValues();
            }
        }
        return new ArrayList<Integer>();
    }
    private SudokuSquare selectUnassignedVariable() {
        for(SudokuSquare s : positions) {
            if(s.getValue() == 0) {
                return s;
            }
        }
        return null;
    }

    private boolean isComplete(int[][] puzzle) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (puzzle[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String s) {

    }


}
