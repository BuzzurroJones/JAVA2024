package game;

import java.util.ArrayList;
import java.util.Random;

public class Human extends Entity {
    private final int vision;
    private final int speed;
    private boolean alive = true;
    private Cell nextCell = cell;
    private int currentSpeed;
    private final boolean social;
    private static final Random rand = new Random();

    public Human(Cell cell, int vision, int speed, boolean social) {
        super(cell);
        this.vision = vision;
        this.speed = speed;
        this.currentSpeed = speed;
        this.social = social;
    }

    public void death() {
        this.Remove();
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void Remove() {
        cell.RemoveHuman(this);
    }

    public void moveCell(Cell newCell) {
        cell.getBoard().MoveElement(this, cell, newCell);
        this.ChangeCell(newCell);
    }

    private int getDistance(Cell cell) {
        int x = Math.abs(cell.getRow() - this.getRow());
        int y = Math.abs(cell.getColumn() - this.getColumn());
        return x + y;
    }

    public void pickMove() {
        // updates next_cell
        if ((currentSpeed <= 0) || (cell.hasFood())) {
            return;
        }
        // checks for food in the vision radius
        ArrayList<Cell> viewFood = cell.getBoard().getFoodVision(this, vision);
        Cell pickedFood = cell;
        ArrayList<Cell> foodList = new ArrayList<>();
        for (int i = 0; i < viewFood.size(); i++) {
            Cell currentFood = viewFood.get(i);

            if (getDistance(currentFood) < currentSpeed) {
                foodList.add(currentFood);
            }
        }
        if (!foodList.isEmpty()) {
            pickedFood = randomCell(foodList);
        } else {
            pickedFood = randomCell(cell.getNeighborhood());
        }

        // picks exact move
        int row = pickedFood.getRow();
        int column = pickedFood.getColumn();
        int row_moves = row - this.getRow();
        int column_moves = column - this.getColumn();
        currentSpeed = currentSpeed - 1;

        if ((row_moves == 0) && (column_moves == 0)) { 
            return;
        }
        boolean direction = randomizeMovement(row_moves, column_moves);
        if (direction) {
            if (column_moves < 0) {
                nextCell = cell.getBoard().getCell(this.getRow(), this.getColumn() - 1);
            } else {
                nextCell = cell.getBoard().getCell(this.getRow(), this.getColumn() + 1);
            }
        } else {
            if (row_moves < 0) {
                nextCell = cell.getBoard().getCell(this.getRow() - 1, this.getColumn());
            } else {
                nextCell = cell.getBoard().getCell(this.getRow() + 1, this.getColumn());
            }
        }    
    }
    

    public void makeMove() {
        moveCell(nextCell);
    }

    private Cell randomCell(ArrayList<Cell> cellList) {
        return cellList.get(rand.nextInt(cellList.size()));
    }

    public void resetSpeed() {
        currentSpeed = speed;
    }

    public Human reproduce() {
        Human son = new Human(randomCell(cell.getNeighborhood()), newGenVision(), newGenSpeed(), social);
        cell.getBoard().getCell(son.getRow(), son.getColumn()).AddHuman(son);
        return son;
    }

    public boolean isSocial() {
        return social;
    }

    private int newGenSpeed() {
        return Math.max(Math.min(speed-1+rand.nextInt(3), Constants.MAXSPEED),1);
    }

    private int newGenVision() {
        return Math.max(Math.min(vision-1+rand.nextInt(3), Constants.MAXVISION),1);
    }

    public int getSpeed() {
        return speed;
    }

    public int getVision() {
        return vision;
    }

    private boolean randomizeMovement(int distanceRows, int distanceColumns) {
        if (distanceRows == 0) {
            return true;
        } else if (distanceColumns == 0) {
            return false;
        }
        return rand.nextBoolean();
    }
}