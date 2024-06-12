package game;

import java.util.ArrayList;
import java.util.Random;

public class GameLogic {
    private ArrayList<Human> playersArrayList = new ArrayList<>();
    private ArrayList<Human> deadPlayerList = new ArrayList<>();
    private ArrayList<Human> reproducePlayerList = new ArrayList<>();

    private final int FOODCOUNT;
    private final int LOWERBOUND;
    private final int UPPERBOUND;
    private int currentFood;
    private final Board board;
    private static final Random rand = new Random();
    private int greedyCounter = 0;
    private final int NCELLS;
    private int socialCounter = 0;
    private double avgSpeed;
    private double avgVision;
    private int generation = 0;

    // making volatility optional
    public GameLogic(Board board, int initialPlayers, int foodAmount) {
        this(board, initialPlayers, foodAmount, 0.1);
    }

    GameLogic(Board board, int initialPlayers, int foodAmount, double volatility) {
        this.board = board;
        this.FOODCOUNT = foodAmount;
        this.NCELLS = board.getColumns() * board.getRows();
        if (volatility < 0) {
            throw new IllegalArgumentException("Volatility must be non-negative.");
        }
        LOWERBOUND = (int) Math.round(FOODCOUNT * (1 - volatility));
        UPPERBOUND = (int) Math.round(FOODCOUNT * (1 + volatility));
        populateBoard(initialPlayers);
        // No need to store food if we want to keep it at a fixed amount, if x food is
        // eaten at the end of a turn x food will be added back
        currentFood = 0;
        spawnFood();
    }

    // game init, creating players, determining types.
    private void populateBoard(int initialPlayers) {
        for (int i = 0; i < initialPlayers; i++) {
            Human player;
            Cell cell = board.getCell(randomPosition(), randomPosition());
            boolean social = randomTrueFalse();
            updateCounter(social);
            player = new Human(cell, randomSpeed(), randomSpeed(), social);
            playersArrayList.add(player);
            board.AddHuman(player, cell);
        }
    }

    public void nextTurn() { // key turn logic
        // resets speed for all alive players
        generation++;
        reset();
        updateAverages();
        spawnFood();
        // System.out.println("b" + currentFood);
        for (int i = 0; i < Constants.MAXSPEED; i++) {
            nextSubTurn();
        }
        deathFromHunger();

        // simulating eating food.
        for (int i = 0; i < playersArrayList.size(); i++) {

            Cell currCell = playersArrayList.get(i).GetCell();
            if (currCell.hasFood()) {

                ArrayList<Human> players = currCell.getHumans();
                foodConflict(0, 1, players);
                playersArrayList.get(i).getCell().foodRemoved();
                currentFood--;
            }
        }
        finalizeDeath();
        reproduceAll();
        // System.out.println("e" + currentFood);
    }

    private void nextSubTurn() {
        for (int i = 0; i < playersArrayList.size(); i++) {
            playersArrayList.get(i).pickMove();
        }
        for (int i = 0; i < playersArrayList.size(); i++) {
            playersArrayList.get(i).makeMove();
        }
    }

    private void foodConflict(int indx1, int indx2, ArrayList<Human> conflictList) {

        if (indx2 >= conflictList.size()) {
            // kill all except indx1, indx2 (kills all if indx1, indx2 are out of range)
            deathInConflict(indx1, indx2, conflictList);
            return;
        }
        boolean player1 = conflictList.get(indx1).isSocial();
        boolean player2 = conflictList.get(indx2).isSocial();

        if (player1) {
            if (player2) {
                // kill all except indx1 & indx2
                deathInConflict(indx1, indx2, conflictList);
                return;
            } else {
                // indx 1 dies
                indx1 = indx2;
                indx2++;
            }
        } else {
            if (player2) {
                // second dies
                indx2++;
            } else {
                // both die
                indx1 = indx2 + 1;
                indx2 = indx1 + 1;
            }
        }
        foodConflict(indx1, indx2, conflictList);
    }

    private void deathInConflict(int indx1, int indx2, ArrayList<Human> conflictList) {

        if (indx1 < conflictList.size() && indx2 >= conflictList.size()) {
            reproducePlayerList.add((conflictList.get(indx1)));
        }
        for (int i = 0; i < conflictList.size(); i++) {
            if (i == indx1 || i == indx2) {
                continue;
            }
            deadPlayerList.add(conflictList.get(i));
        }
    }

    private void deathFromHunger() {
        // gets all dead players
        for (int i = 0; i < playersArrayList.size(); i++) {
            if (!playersArrayList.get(i).getCell().hasFood()) {
                deadPlayerList.add(playersArrayList.get(i));
            }
        }
    }

    private void finalizeDeath() { // removes dead players from players list and cells' elements list
        for (int i = 0; i < deadPlayerList.size(); i++) {
            updateCounter(deadPlayerList.get(i).isSocial(), -1);
            deadPlayerList.get(i).death();
            playersArrayList.remove(deadPlayerList.get(i));
        }
        System.gc();
        deadPlayerList.clear();
    }

    private void reset() { // resets speed
        for (int i = 0; i < playersArrayList.size(); i++) {
            playersArrayList.get(i).resetSpeed();
        }
    }

    private void spawnFood() { // spawns food
        int cap = Math.min(NCELLS, rand.nextInt((UPPERBOUND - LOWERBOUND) + 1) + LOWERBOUND);
        while (currentFood < cap) {
            Cell cell = board.getCell(randomPosition(), randomPosition());
            if (!(cell.hasFood())) {
                cell.foodAdded();
                currentFood++;
            }
        }
    }

    private void reproduceAll() {
        // System.out.println("init len" + playersArrayList.size());
        // System.out.println("init rep" + reproducePlayerList.size());
        for (int i = 0; i < reproducePlayerList.size(); i++) {
            reproduce(reproducePlayerList.get(i));
            updateCounter(reproducePlayerList.get(i).isSocial());
        }

        reproducePlayerList.clear();
        // System.out.println("final len" + playersArrayList.size());
    }

    private int randomPosition() {
        return rand.nextInt(board.getRows()); // AAnother function will be needed if the n rows != n columns
    }

    private boolean randomTrueFalse() {
        return rand.nextBoolean();
    }

    public ArrayList<Human> getPlayers() {
        return playersArrayList;
    }

    public void reproduce(Human human) {
        // System.out.println("reproduced");
        Human son = human.reproduce();
        playersArrayList.add(son);
    }

    private void updateCounter(boolean isSocial) {
        if (isSocial) {
            socialCounter++;
        } else {
            greedyCounter++;
        }
    }

    private void updateCounter(boolean isSocial, int inc) {
        if (isSocial) {
            socialCounter += inc;
        } else {
            greedyCounter += inc;
        }
    }

    public ArrayList<Integer> getCounters() {
        ArrayList<Integer> counters = new ArrayList<>();
        counters.add(socialCounter);
        counters.add(greedyCounter);
        return counters;
    }

    public int getSocial() {
        return socialCounter;
    }

    public int getGreedy() {
        return greedyCounter;
    }

    public int getGeneration() {
        return generation;
    }

    private void updateAverages() {
        avgSpeed = 0;
        avgVision = 0;
        for (int i = 0; i < playersArrayList.size(); i++) {
            avgSpeed += playersArrayList.get(i).getSpeed();
            avgVision += playersArrayList.get(i).getVision();

        }
        avgSpeed = avgSpeed / playersArrayList.size();
        avgVision = avgVision / playersArrayList.size();
    }

    public double getAverageSpeed() {
        return avgSpeed;
    }

    public double getAverageVision() {
        return avgVision;
    }

    private int randomSpeed() {
        Random random = new Random();
        int max = 7;
        int min = 2;
        int randomInt = random.nextInt((max - min) + 1) + min;
        return randomInt;
    }

}