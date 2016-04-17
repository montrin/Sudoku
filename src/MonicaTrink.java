//import java.util.*;
//
///**
// * Created by monicatrink on 08/04/16.
// */
//public class MonicaTrink implements SudokuAgent {
//
//
//    private String name;
//
//    MonicaTrink() {
//        name = "Agent Monica";
//    }
//
//    private ArrayList<UnitDomain> unitDomains;
//    Map<Integer, ArrayList<Position>> map;
//
//    private int height;
//    private int width;
//    private ArrayList<Integer> initialDomain;
//    private class Position {
//        public int row = 0;
//        public int col = 0;
//
//        public Position(int row, int col) {
//            this.row = row;
//            this.col = col;
//        }
//    }
//
//    private class UnitDomain {
//        int unitId;
//        ArrayList<Integer> possibleValues;
//
//        public UnitDomain(int id, ArrayList<Integer> possibleValues) {
//            this.unitId = id;
//            this.possibleValues = possibleValues;
//        }
//
//        public ArrayList<Integer> getPossibleValues() {
//            return possibleValues;
//        }
//    }
//
//    private class Domain implements Comparable<Domain> {
//        int row;
//        int col;
//        ArrayList<Integer> possibleValues;
//
//        public Domain(int row, int col, ArrayList<Integer> possibleValues) {
//            this.row = row;
//            this.col = col;
//            this.possibleValues = possibleValues;
//        }
//
//        public Domain() {
//            this.row = 0;
//            this.col = 0;
//            this.possibleValues = new ArrayList<Integer>();
//        }
//
//        public int getRow() {
//            return row;
//        }
//
//        public int getCol() {
//            return col;
//        }
//
//        public void setPossibleValues(ArrayList<Integer> possibleValues) {
//            this.possibleValues = possibleValues;
//        }
//
//        public ArrayList<Integer> getPossibleValues() {
//            return possibleValues;
//        }
//
//        @Override
//        public int compareTo(Domain o) {
//            return this.getPossibleValues().size() - o.getPossibleValues().size();
//        }
//    }
//
//    private Map<Integer, ArrayList<Position>> setupUnits(int dimension) {
//        int width = dimension * dimension;
//        int height = dimension * dimension;
//        int dimRow = dimension;
//        int dimCol = dimension;
//
//        Map<Integer, ArrayList<Position>> map = new HashMap<Integer, ArrayList<Position>>();
//
//        int mapCounter = 1;
//
//        int startRow = 0;
//        int startCol = 0;
//        ArrayList<Position> posi = new ArrayList<Position>();
//
//        int endPos = dimension * dimension - 1;
//
//        int counter = 0;
//        while (startCol <= (endPos) && startRow <= (endPos)) {
//
//
//            for (int i = startRow; i < startRow + dimension; i++) {
//                for (int j = startCol; j < startCol + dimension; j++) {
//                    posi.add(new Position(i, j));
//                }
//            }
//
//            counter++;
//
//            map.put(mapCounter, new ArrayList<Position>(posi));
//            mapCounter++;
//            posi.clear();
//            if(counter < dimension) {
////                startRow += 0;
//                startCol += dimension;
//            }else{
//                counter=0;
//                startRow += dimension;
//                startCol = 0;
//            }
//        }
//
//        return map;
//    }
//
//    public class MyComparator implements Comparator<Domain> {
//
//        @Override
//        public int compare(Domain d1, Domain d2) {
//            return (d1.getPossibleValues().size()) - (d2.getPossibleValues().size());
//        }
//    }
//
//    public int[][] solve(int dimension, int[][] puzzle) {
//
//
//        width = dimension * dimension;
//        height = dimension * dimension;
//        int[][] solved = new int[width][height];
//        initialDomain = new ArrayList<Integer>();
//        map = setupUnits(dimension);
//        initDomain(initialDomain);
//
//
//        ArrayList<Domain> domains = new ArrayList<Domain>();
//        setDomains(puzzle, initialDomain, domains);
//        ArrayList<Domain> workingOndomains = new ArrayList<Domain>(domains);
//
//        for (Domain d : workingOndomains) {
//            //remove existing values of row in domain
//            int currRow = d.getRow();
//            int currCol = d.getCol();
//
//            ArrayList<Integer> valuesToRemoveFromDomain = new ArrayList<Integer>();
//
//            //add existing values to remove from current col
//            ArrayList<Integer> populatedValues = removeAlreadyAssigned(puzzle, d, currRow, currCol, valuesToRemoveFromDomain);
//
//            d.setPossibleValues(populatedValues);
//        }
//
//        domains = workingOndomains;
//        Collections.sort(domains, new MyComparator());
//        int[][] startPuzzle = puzzle;
//        for(int val : domains.get(0).getPossibleValues()) {
//            int row = domains.get(0).row;
//            int col = domains.get(0).col;
//            puzzle=startPuzzle;
//            puzzle[row][col] = val;
//            solved = backtrackingSearch(puzzle, val, row, col, domains);
//            if(isComplete(solved)) {
//                break;
//            }
//        }
//
////        if(!isComplete(solved)) {
////
////            solved = backtrackingSearch2(startPuzzle, workingOndomains);
////        }
//
//        return solved;
//    }
//
//    private ArrayList<Integer> removeAlreadyAssigned(int[][] puzzle, Domain d, int currRow, int currCol, ArrayList<Integer> valuesToRemoveFromDomain) {
//        for (int j = 0; j < height; j++) {
//            if (puzzle[currRow][j] != 0) {
//                valuesToRemoveFromDomain.add(puzzle[currRow][j]);
//            }
//        }
//
//        //add existing values to remove from current row
//        for (int i = 0; i < width; i++) {
//            if (puzzle[i][currCol] != 0) {
//                valuesToRemoveFromDomain.add(puzzle[i][currCol]);
//            }
//        }
//
//        //remove existing values of unit in domain
//        valuesToRemoveFromDomain.addAll(collectDomainsInUnit(map, currRow, currCol, puzzle));
//        ArrayList<Integer> populatedValues = d.getPossibleValues();
//
//        for (Integer rm : valuesToRemoveFromDomain) {
//            if (populatedValues.contains(rm)) {
//                populatedValues.remove(rm);
//            }
//        }
//        return populatedValues;
//    }
//
//    private void setDomains(int[][] puzzle, ArrayList<Integer> initialDomain, ArrayList<Domain> domains) {
//        domains.clear();
//        //init domains
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                if (puzzle[i][j] == 0) {
//                    domains.add(new Domain(i, j, new ArrayList<Integer>(initialDomain)));
//                }
//            }
//        }
//    }
//
//    private int[][] backtrackingSearch(int[][] puzzle, int value, int row, int col, ArrayList<Domain> domains) {
//        return backtrack(puzzle, domains, row, col, value);
//    }
//
//
////    private int[][] backtrackingSearch2(int[][] puzzle, ArrayList<Domain> domains) {
////        return backtrack2(puzzle, domains);
////    }
//
//    private boolean isComplete(int[][] puzzle) {
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                if (puzzle[i][j] == 0) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    private ArrayList<Domain> selectNextVariable(int ii, ArrayList<Domain> domains, int row, int col) {
//        for (Domain dd : domains) {
//            if (dd.getPossibleValues().size() > 0 &&
//                    dd.getRow() == row
//                    && dd.getCol() == col) {
//
//                dd.setPossibleValues(new ArrayList<Integer>());
//            }
//        }
//        //populate variables
//        populateAllRowNeighbours(ii, row, domains);
//        populateAllColNeighbours(ii, col, domains);
//        populateAllUnitNeighbours(ii, row, col, map, domains);
//        ArrayList<Domain> temporaryWorking = new ArrayList<Domain>(domains);
//        for(Domain d : temporaryWorking) {
//            if(d.getPossibleValues().size() == 0) {
//                domains.remove(d);
//            }
//        }
//
//
//
//        Collections.sort(domains, new MyComparator());
//        return domains;
//    }
//
//
////    private int[][] backtrack2(int[][] puzzle, ArrayList<Domain> domains, int row, int col, int value) {
////
////    }
//
//    private int[][] backtrack(int[][] puzzle, ArrayList<Domain> domains, int row, int col, int value) {
//        //finally done
//        if(isComplete(puzzle)) {
//            return puzzle;
//        }
//
//
//
//        //or search further
//        ArrayList<Domain> var = selectNextVariable(value, domains, row, col);
//
//            puzzle[row][col] = value;
//
//            for (Integer val : var.getPossibleValues()) {
//                if (isConsistent(val, var.row, var.col, domains)) {
//                    int[][] result;
//                    int rrow = var.row;
//                    int rcol = var.col;
//                    System.out.println("Try ");
//                    consistentAndOpen(puzzle, rrow, rcol, val, domains);
//                    result = backtrack(puzzle.clone(), new ArrayList<Domain>(domains), rrow, rcol, val);
//                    if(isComplete(result)) {
//                        return result;
//                    }
//                }
//            }
//
//
//        System.out.println("backtrack");
////        consistentAndOpen(puzzle, row, col, value, domains);
//        return puzzle;
//    }
//
//
//    private void consistentAndOpen(int[][] puzzle, int row, int col, int value, ArrayList<Domain> domains) {
//        int nropen = 0;
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                if (puzzle[i][j] == 0) {
//                    nropen++;
//                }
//            }
//        }
//        System.out.println("nropen: " + nropen + " ("+row+","+col+")="+value+" Possible Domains: "+domains.size());
//    }
//
//    private boolean isConsistent(int value, int row, int col, ArrayList<Domain> domains) {
//        return noConstraints(value, row, col, map, domains);
//    }
//
//    private void process(int[][] puzzle, Map<Integer, ArrayList<Position>> map, ArrayList<Domain> workingOndomains, ArrayList<Domain> domains) {
//        for(Domain cd : workingOndomains) {
//            ArrayList<Integer> i = cd.getPossibleValues();
//            if (!i.isEmpty()) {
//                //check all possible values of d
//                for (Integer ii : i) {
////                    if(cd.getCol() == 0 && cd.getRow() == 2) {
////                        System.out.println("adf");
////                    }
//                    if (noConstraints(ii, cd.getRow(), cd.getCol(), map, domains)) {
//                        //no constraints so set field and populate neighbours
//                        //if cd.getpossiblevalues contains more than one, then all other values which were not token should be
//                        //removed.
//                        puzzle[cd.getRow()][cd.getCol()] = ii;
//
//                        //remove selected value from possible values
//                        for (Domain dd : domains) {
//                            if (dd.getPossibleValues().size() > 0 &&
//                                    dd.getRow() == cd.getRow()
//                                    && dd.getCol() == cd.getCol()) {
//
//                                dd.setPossibleValues(new ArrayList<Integer>());
////                                if (dd.getPossibleValues().contains(ii)) {
////                                    dd.getPossibleValues().remove(i.indexOf(ii));
////                                }
//                            }
//                        }
//                        //remove from domain
//
//                        //populate variables
//                        populateAllRowNeighbours(ii, cd.getRow(), domains);
//                        populateAllColNeighbours(ii, cd.getCol(), domains);
//                        populateAllUnitNeighbours(ii, cd.getRow(), cd.getCol(), map, domains);
//
//                        //TODO: do not loop in this, instead populate value and its neighbours and then do
//                        //re-sort and process again
//                        return;
//                    }
//                }
//            }
////                    Collections.sort(workingOndomains, new MyComparator());
//        }
//    }
//
//    private boolean noConstraints(int value, int row, int col, Map<Integer, ArrayList<Position>> map, ArrayList<Domain> domains) {
//
//        return noConstraintsOnRelatedRows(value, row, col, domains) &&
//                noConstraintsOnRelatedCols(value, row, col, domains) &&
//                noConstraintsOnRelatedUnit(value, row, col, map, domains);
//
////        if(noConstraintsOnRelatedRows(value, row, col, domains) &&
////                noConstraintsOnRelatedCols(value, row, col, domains) &&
////                noConstraintsOnRelatedUnit(value, row, col, map, domains)) {
////
////            Integer unitId = getUnitId(row, col);
////            ArrayList<Position> otherPos = getOtherPosPerUnit(row, col, unitId);
////
////            ArrayList<Integer> possibleValuesInUnit = getPossibleValuesPerUnit(unitId);
////            for(Position p : otherPos) {
////
////            }
////
////            return true;
////        }
////
////
////        return false;
//
//    }
//
//    private ArrayList<Integer> getPossibleValuesPerUnit(int unitId) {
//        for(UnitDomain d : unitDomains) {
//            if(d.unitId == unitId) {
//                return d.getPossibleValues();
//            }
//        }
//
//        return null;
//    }
//    private Integer getUnitId(int row, int col) {
//        int searchKey = 0;
//        //search for key
//        for (Integer key : map.keySet()) {
//            ArrayList<Position> posi = map.get(key);
//
//            for (Position p : posi) {
//                if (p.row == row && p.col == col) {
//                    searchKey = key;
//                }
//            }
//        }
//        return searchKey;
//    }
//    private ArrayList<Position> getOtherPosPerUnit(int row, int col, int searchKey) {
//
//        ArrayList<Position> otherpos = new ArrayList<Position>();
//
//        //get other positions
//        if (searchKey != 0) {
//            ArrayList<Position> posi = map.get(searchKey);
//            for (Position p : posi) {
//                if (p.row != row && p.col != col) {
//                    otherpos.add(p);
//                }
//            }
//        }
//        return otherpos;
//    }
//
//    private boolean noConstraintsInUnit(int  value, int unitId, ArrayList<Position> otherPosInUnit) {
//        ArrayList<Integer> unitPossibleVal = new ArrayList<Integer>();
//
//        for(UnitDomain unit : unitDomains) {
//            if(unit.unitId == unitId) {
//                unitPossibleVal = unit.possibleValues;
//            }
//        }
//
//        for(Position pos : otherPosInUnit){
//
//
//        }
//
//        for (Integer val: unitPossibleVal) {
//            if(val != value) {
//
//            }
//        }
//
//
//        return true;
//    }
//
//    private boolean noConstraintsOnRelatedUnit(int value, int row, int col, Map<Integer, ArrayList<Position>> map, ArrayList<Domain> domains) {
//        int searchKey = 0;
//
//        for (Integer key : map.keySet()) {
//            ArrayList<Position> posi = map.get(key);
//
//            for (Position p : posi) {
//                if (p.row == row && p.col == col) {
//                    searchKey = key;
//                }
//            }
//        }
//
//        if (searchKey != 0) {
//            ArrayList<Position> posi = map.get(searchKey);
//            for (Position p : posi) {
//                if (p.row != row && p.col != col) {
//                    for (Domain d : domains) {
//                        if (d.getRow() == p.row && d.getCol() == p.col && hasValue(d.getPossibleValues(), value)) {
//                            return d.getPossibleValues().size() - 1 > 0 ? true : false;
//                        }
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    private boolean noConstraintsOnRelatedCols(int value, int row, int col, ArrayList<Domain> domains) {
//        for (Domain d : domains) {
//            if (d.getCol() == col && d.getRow() != row && hasValue(d.getPossibleValues(), value)) {
//                return d.getPossibleValues().size() - 1 > 0 ? true : false;
//            }
//        }
//        return true;
//    }
//
//    private boolean noConstraintsOnRelatedRows(int value, int row, int col, ArrayList<Domain> domains) {
//        for (Domain d : domains) {
//            if (d.getRow() == row && d.getCol() != col && hasValue(d.getPossibleValues(), value)) {
//                return d.getPossibleValues().size() - 1 > 0 ? true : false;
//            }
//        }
//        return true;
//    }
//
//    private boolean hasValue(ArrayList<Integer> possibleValues, int value) {
//        for (int i : possibleValues) {
//            if (i == value) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    private boolean hasUnassigned(int[][] puzzle, int width, int height) {
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                if (puzzle[i][j] == 0) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private void initDomain(ArrayList<Integer> initialDomain) {
//        initialDomain.add(1);
//        initialDomain.add(2);
//        initialDomain.add(3);
//        initialDomain.add(4);
//        initialDomain.add(5);
//        initialDomain.add(6);
//        initialDomain.add(7);
//        initialDomain.add(8);
//        initialDomain.add(9);
//    }
//
//    private boolean domainsExists(ArrayList<Domain> domains) {
//
//        for (Domain d : domains) {
//
//            if (d.getPossibleValues().size() > 0) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private void populateAllUnitNeighbours(int value, int row, int col, Map<Integer, ArrayList<Position>> map,
//                                           ArrayList<Domain> domains) {
//
//        int searchKey = 0;
//
//        for (Integer key : map.keySet()) {
//            ArrayList<Position> posi = map.get(key);
//
//            for (Position p : posi) {
//                if (p.row == row && p.col == col) {
//                    searchKey = key;
//                }
//            }
//        }
//
//        if (searchKey != 0) {
//            ArrayList<Position> posi = map.get(searchKey);
//            for (Position p : posi) {
//                for (Domain d : domains) {
//                    if (d.getRow() == p.row && d.getCol() == p.col) {
//                        if (d.getPossibleValues().contains(value)) {
//                            int i = d.getPossibleValues().indexOf(value);
//                            d.getPossibleValues().remove(i);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void populateAllRowNeighbours(int value, int row, ArrayList<Domain> domains) {
//        for (Domain d : domains) {
//            if (d.getRow() == row && d.getPossibleValues().size() > 0) {
//                if (d.getPossibleValues().contains(value)) {
//                    int i = d.getPossibleValues().indexOf(value);
//                    d.getPossibleValues().remove(i);
//                }
//            }
//        }
//    }
//
//    private void populateAllColNeighbours(int value, int col, ArrayList<Domain> domains) {
//        for (Domain d : domains) {
//            if (d.getCol() == col && d.getPossibleValues().size() > 0) {
//                if (d.getPossibleValues().contains(value)) {
//                    int i = d.getPossibleValues().indexOf(value);
//                    d.getPossibleValues().remove(i);
//                }
//            }
//        }
//    }
//
//    public ArrayList<Integer> collectDomainsInUnit(Map<Integer, ArrayList<Position>> map, int row, int col, int[][] puzzle) {
//
//        ArrayList<Integer> toRemove = new ArrayList<Integer>();
//        int searchKey = 0;
//
//        for (Integer key : map.keySet()) {
//            ArrayList<Position> posi = map.get(key);
//
//            for (Position p : posi) {
//                if (p.row == row && p.col == col) {
//                    searchKey = key;
//                }
//            }
//        }
//
//        if (searchKey != 0) {
//            ArrayList<Position> posi = map.get(searchKey);
//            for (Position p : posi) {
//                toRemove.add(puzzle[p.row][p.col]);
//            }
//        }
//        return toRemove;
//    }
//
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//}
