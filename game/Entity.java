package game;

public class Entity {

	// reference to current sell
	protected Cell cell;
	// Constructor
	public Entity(Cell cell) {
		this.cell = cell;

		// x, y are same as that of the cell.
	}
	public Cell getCell() {
		return this.cell;
	}
	public int getRow() {
		return cell.getRow();
	}

	public int getColumn() {
		return cell.getColumn();
	}

	public Cell GetCell() {
		return cell;
	}

	public void ChangeCell(Cell newCell) {
		cell = newCell;
	}
}