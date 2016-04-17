import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MonicaTrink3 implements SudokuAgent {


    private String name;
    private int height;
    private int width;

    ArrayList<SudokuSquare> positions;
    ArrayList<Domain> csp;
    ArrayList<Integer> initialDomain;
    int[][] workingPuzzle;
    int[][] solution;
    //Unit map which contains squares per unit e.g. for a 3x3 unit, unit 1 contains the first 9 squares with indices
    //(0,0)(0,1)(0,2)
    //(1,0)(1,1)(1,2)
    //(2,0)(2,1)(2,2)

    Map<Integer, ArrayList<SudokuSquare> > map;

    MonicaTrink3() {
        name = "Agent Monica";
    }

    @Override
    public int[][] solve(int dimension, int[][] puzzle) {
        width = dimension * dimension;
        height = dimension * dimension;
        map = setupUnits(dimension);
//        initialDomain = new ArrayList<Integer>();
//        initDomain(initialDomain);

        workingPuzzle = puzzle;

        //simple backtracking through all squares from left to right
        //start backtrackingSearch at (0,0)
        if(backtrackingSearchSimple(new SudokuSquare(0,0,0))) {
            return workingPuzzle;
        }

        //strategy with considering valid values by forward looking
//        ArrayList<Domain> domains = populateConstraints(puzzle);
//
//        if(backtrackingSearchSimple(new SudokuSquare(domains.get(0).getRow(), domains.get(0).getCol(),0))) {
//            return workingPuzzle;
//        }

        //no solution in that case return, return initial puzzle
        return puzzle;
    }

    private ArrayList<Domain> populateConstraints(int[][] puzzle) {
        ArrayList<Domain> domains = new ArrayList<Domain>();
        setDomains(puzzle, initialDomain, domains);
        ArrayList<Domain> workingOndomains = new ArrayList<Domain>(domains);

        for (Domain d : workingOndomains) {
            //remove existing values of row in domain
            int currRow = d.getRow();
            int currCol = d.getCol();

            ArrayList<Integer> valuesToRemoveFromDomain = new ArrayList<Integer>();

            //add existing values to remove from current col
            ArrayList<Integer> populatedValues = removeAlreadyAssigned(puzzle, d, currRow, currCol, valuesToRemoveFromDomain);

            d.setPossibleValues(populatedValues);
        }

        domains = workingOndomains;
        Collections.sort(domains, new DomainComparator());
        return domains;
    }

    private ArrayList<Integer> removeAlreadyAssigned(int[][] puzzle, Domain d, int currRow, int currCol, ArrayList<Integer> valuesToRemoveFromDomain) {
        for (int j = 0; j < height; j++) {
            if (puzzle[currRow][j] != 0) {
                valuesToRemoveFromDomain.add(puzzle[currRow][j]);
            }
        }

        //add existing values to remove from current row
        for (int i = 0; i < width; i++) {
            if (puzzle[i][currCol] != 0) {
                valuesToRemoveFromDomain.add(puzzle[i][currCol]);
            }
        }

        //remove existing values of unit in domain
        valuesToRemoveFromDomain.addAll(collectDomainsInUnit(map, currRow, currCol, puzzle));
        ArrayList<Integer> populatedValues = d.getPossibleValues();

        for (Integer rm : valuesToRemoveFromDomain) {
            if (populatedValues.contains(rm)) {
                populatedValues.remove(rm);
            }
        }
        return populatedValues;
    }

        public ArrayList<Integer> collectDomainsInUnit(Map<Integer, ArrayList<SudokuSquare>> map, int row, int col, int[][] puzzle) {

        ArrayList<Integer> toRemove = new ArrayList<Integer>();
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
                toRemove.add(puzzle[p.getRow()][p.getCol()]);
            }
        }
        return toRemove;
    }

    private boolean backtrackingSearchSimple(SudokuSquare s) {
        if(s == null) {
            return true;
        }

        if(workingPuzzle[s.getRow()][s.getCol()] != 0){
            return backtrackingSearchSimple(nextSquare(s));
        }

        //start at one because valid values are {1,2,3...}
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

    //just next square
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


    private SudokuSquare nextSquareAccordingCsp(SudokuSquare s) {
        ArrayList<Domain> domains = populateConstraints(workingPuzzle);
        if(domains.size() > 0){
            return new SudokuSquare(domains.get(0).getRow(), domains.get(0).getCol(),0);
        }

        return nextSquare(s);
    }

    private void setDomains(int[][] puzzle, ArrayList<Integer> initialDomain, ArrayList<Domain> domains) {
        domains.clear();
        //init domains
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (puzzle[i][j] == 0) {
                    domains.add(new Domain(i, j, new ArrayList<Integer>(initialDomain)));
                }
            }
        }
    }

    private void initDomain(ArrayList<Integer> initialDomain) {
        for(int i=1;i<=width;i++){
            initialDomain.add(i);
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




    private boolean isConsistent2(int value, int row, int col) {
        return noConstraintsOnRelatedRows2(value, row, col) &&
                noConstraintsOnRelatedCols2(value, row, col) &&
                noConstraintsOnRelatedUnit2(value, row, col);
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


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String s) {

    }
}
