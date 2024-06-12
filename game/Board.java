package game;

import java.util.ArrayList; //Board Class is made of a 2d arraylist that stores all the cells,
                            // the number of rows and columns is stored

public class Board {
    private final int ROWS;
    private final int COLUMNS;
    private final ArrayList<ArrayList<Cell>> Cells = new ArrayList<>();

    Board(int rows, int columns) { // Constructor creates a Rows x Columns Array and stores it as Cells
        this.ROWS = rows;
        this.COLUMNS = columns;
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> NewColumn = new ArrayList<>();
            for (int j = 0; j < columns; j++) {
                Cell s = new Cell(i, j, this);
                NewColumn.add(s);
            }
            Cells.add(NewColumn);
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.getCell(i, j).createNeighborhood();
            }
        }
    }

    public void AddHuman(Human human, Cell cell) {
        Cells.get(cell.getRow()).get(cell.getColumn()).AddHuman(human);
    }

    public void RemoveHuman(Human human, int row, int column) {
        Cells.get(row).get(column).RemoveHuman(human);
    }

    public void MoveElement(Human human, Cell currentCell, Cell destinationCell) {
        // get coordinates of first cell
        Cells.get(currentCell.getRow()).get(currentCell.getColumn()).RemoveHuman(human);
        Cells.get(destinationCell.getRow()).get(destinationCell.getColumn()).AddHuman(human);

    }

    public boolean cellInRange(int row, int column) {
        return ((-1 < row) && (row < ROWS) && (-1 < column) && (column < COLUMNS));
    }

    public ArrayList<Human> GetHumans(Cell cell) {
        return Cells.get(cell.getRow()).get(cell.getColumn()).getHumans();
    }

    public ArrayList<Human> GetHumans(int row, int column) {
        return Cells.get(row).get(column).getHumans();
    }

    public int getRows() {
        return ROWS;
    }

    public int getColumns() {
        return COLUMNS;
    }

    public Cell getCell(int row, int column) {
        return Cells.get(row).get(column);
    }

    public ArrayList<Cell> getFoodVision(Human human, int vision) {
        int row = human.getColumn();
        int column = human.getRow();
        // int vision = human.getVision();
        ArrayList<Cell> food = new ArrayList<>();

        for (int i = 0; i < vision + 1; i++) {
            if (cellInRange(row + vision - i, column)) {
                if (Cells.get(row + vision - i).get(column).hasFood()) {
                    food.add(Cells.get(row + vision - i).get(column));
                }
            }
            if ((cellInRange(row - vision + i, column)) && (i != vision)) {
                if (Cells.get(row - vision + i).get(column).hasFood()) {
                    food.add(Cells.get(row - vision + i).get(column));
                }
            }
            for (int j = 1; j < i + 1; j++) {
                if (cellInRange(row + vision - i, column + j)) {
                    if (Cells.get(row + vision - i).get(column + j).hasFood()) {
                        food.add(Cells.get(row + vision - i).get(column + j));
                    }
                }
                if ((cellInRange(row + vision - i, column - j)) && (i != vision)) {
                    if (Cells.get(row + vision - i).get(column - j).hasFood()) {
                        food.add(Cells.get(row + vision - i).get(column - j));
                    }
                }
                if ((cellInRange(row - vision + i, column + j)) && (i != vision)) {
                    if (Cells.get(row - vision + i).get(column + j).hasFood()) {
                        food.add(Cells.get(row - vision + i).get(column + j));
                    }
                }
                if (cellInRange(row - vision + i, column - j)) {
                    if (Cells.get(row - vision + i).get(column - j).hasFood()) {
                        food.add(Cells.get(row - vision + i).get(column - j));
                    }
                }
            }
        }
        return food;
    }
}