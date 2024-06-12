package game;

import java.awt.GridLayout;
import javax.swing.JPanel;

public class UIBoard extends JPanel {
    private final int rows;
    private final int cols;
    private UIBoardCell[][] cells;

    public UIBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new UIBoardCell[rows][cols];

        setLayout(new GridLayout(rows, cols));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new UIBoardCell(Constants.EMPTY, false);
                add(cells[row][col]);
            }
        }
    }

    public void setCellState(int row, int col, int state) {
        if (isValidCell(row, col)) {
            cells[row][col].setState(state);
        }
    }

    public void setCellHasFood(int row, int col, boolean hasFood) {
        if (isValidCell(row, col)) {
            cells[row][col].setHasFood(hasFood);
        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}
