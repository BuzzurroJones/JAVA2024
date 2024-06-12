package game;

import java.util.ArrayList; // Class used to define the object the board is made up of, it stores his own position and

// any element that "walks" on it whether that is a resource or a player

public class Cell {
    private final Board BOARD;
    private final int ROW;
    private final int COLUMN;
    private ArrayList<Human> humans = new ArrayList<>();
    private boolean food = false;
    private ArrayList<Cell> neighbourCells = new ArrayList<>(); // neighborhood is only initialized, a method to create
                                                                // it should be implemented

    Cell(int row, int column, Board board) { // Constructor that takes the ROW, column coordinates of the Cell and
                                             // stores
        // them
        this.ROW = row; // Currently the values are not used but could become useful
        this.COLUMN = column;
        this.BOARD = board;
    }
    public void createNeighborhood() {
        if (ROW == 0) {
            neighbourCells.add(BOARD.getCell(ROW + 1, COLUMN));
        } else if (ROW == BOARD.getRows() - 1) {
            neighbourCells.add(BOARD.getCell(ROW - 1, COLUMN));
        } else {
            neighbourCells.add(BOARD.getCell(ROW + 1, COLUMN));
            neighbourCells.add(BOARD.getCell(ROW - 1, COLUMN));
        }
        if (COLUMN == 0) {
            neighbourCells.add(BOARD.getCell(ROW, COLUMN + 1));
        } else if (COLUMN == BOARD.getColumns() - 1) {
            neighbourCells.add(BOARD.getCell(ROW, COLUMN - 1));
        } else {
            neighbourCells.add(BOARD.getCell(ROW, COLUMN + 1));
            neighbourCells.add(BOARD.getCell(ROW, COLUMN - 1));
        }
    }

    public ArrayList<Cell> getNeighborhood() {
        return neighbourCells;
    }

    public boolean isEmpty() {
        return this.humans.isEmpty();
    }

    public void AddHuman(Human human) {
        humans.add(human);
    }

    public ArrayList<Human> getHumans() { // Returns the array containing all the elements in the cell
        return humans;
    }

    public void RemoveHuman(Human entity) {
        humans.remove(entity);
    }

    public int getRow() {
        return ROW;
    }

    public int getColumn() {
        return COLUMN;
    }

    public Board getBoard() {
        return BOARD;
    }

    public boolean hasFood() {
        return food;
    }

    public void foodAdded() {
        food = true;
    }

    public void foodRemoved() {
        food = false;
    }

    // temporary 
    public int getState() {
        if (isEmpty()) {return Constants.EMPTY;}
        int s = 0;
        int gr = 0;
        for (int i = 0; i < humans.size(); i++) {
            if(humans.get(i).isSocial()) {
                s++;
            } else { gr++;}
        }
        if (s>gr){return Constants.BLUE;}
        else {return Constants.RED;}
    }

}
