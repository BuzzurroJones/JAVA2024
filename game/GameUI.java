package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GameUI extends JFrame {
    private final JLabel goodPlayersLabel;
    private final JLabel evilPlayersLabel;
    private final JLabel averageSpeedLabel;
    private final JLabel averageVisionLabel;
    private final List<Integer> goodPlayersHistory;
    private final List<Integer> evilPlayersHistory;
    private final List<Double> averageSpeedHistory;
    private final List<Double> averageVisionHistory;
    private boolean isUpdating;
    private final Timer timer;
    private static final int MAX_HISTORY_SIZE = 200;

    private final UIBoard UIboard;
    private final Board board;

    public GameUI(int boardDimension, int playersNumber, int foodAmount) {
        this(boardDimension, playersNumber, foodAmount, 0.1);
    }

    public GameUI(int boardDimension, int playersNumber, int foodAmount, double volatility) {
        setTitle("Good vs Evil Players");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        goodPlayersLabel = new JLabel("Good Players: 0");
        evilPlayersLabel = new JLabel("Evil Players: 0");
        averageSpeedLabel = new JLabel("Average Speed: 0");
        averageVisionLabel = new JLabel("Average Vision: 0");

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(1, 4));
        labelPanel.add(goodPlayersLabel);
        labelPanel.add(evilPlayersLabel);
        labelPanel.add(averageSpeedLabel);
        labelPanel.add(averageVisionLabel);

        goodPlayersHistory = new ArrayList<>();
        evilPlayersHistory = new ArrayList<>();
        averageSpeedHistory = new ArrayList<>();
        averageVisionHistory = new ArrayList<>();

        GraphPanel graphPanel = new GraphPanel();
        UIboard = new UIBoard(boardDimension, boardDimension);
        UIboard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isUpdating = !isUpdating;
            }
        });

        // equal sizes for graphPanel and UIboard
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(graphPanel);
        mainPanel.add(UIboard);

        add(labelPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        isUpdating = true;

        // Initialize the board
        this.board = new Board(boardDimension, boardDimension);
        GameLogic gl = new GameLogic(board, playersNumber, foodAmount, volatility);

        timer = new Timer(50, e -> {
            if (isUpdating) {
                int goodPlayers = gl.getSocial();
                int evilPlayers = gl.getGreedy();
                double averageSpeed = gl.getAverageSpeed();
                double averageVision = gl.getAverageVision();
                gl.nextTurn();
                updateCounts(goodPlayers, evilPlayers, averageSpeed, averageVision);
                updateBoard();
            }
        });

        timer.start();
    }

    public void updateCounts(int goodPlayers, int evilPlayers, double averageSpeed, double averageVision) {
        goodPlayersLabel.setText("Good Players: " + goodPlayers);
        evilPlayersLabel.setText("Evil Players: " + evilPlayers);
        averageSpeedLabel.setText(Constants.LABELSPEED + String.format("%.2f", averageSpeed));
        averageVisionLabel.setText(Constants.LABELVISION + String.format("%.2f", averageVision));

        addValueToHistory(goodPlayersHistory, goodPlayers);
        addValueToHistory(evilPlayersHistory, evilPlayers);
        addValueToHistory(averageSpeedHistory, averageSpeed);
        addValueToHistory(averageVisionHistory, averageVision);

        repaint();
    }

    private <T> void addValueToHistory(List<T> history, T value) {
        if (history.size() >= MAX_HISTORY_SIZE) {
            history.remove(0);
        }
        history.add(value);
    }

    private void updateBoard() {
        // updates board
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                int state = board.getCell(row, col).getState();
                boolean hasFood = board.getCell(row, col).hasFood();
                UIboard.setCellState(row, col, state);
                UIboard.setCellHasFood(row, col, hasFood);
            }
        }
        UIboard.repaint();
    }

    class GraphPanel extends JPanel {
        public GraphPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    isUpdating = !isUpdating;
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            int padding = 50;

            // Draw axes
            g2d.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
            g2d.drawLine(padding, padding, padding, height - padding); // Y-axis

            if (goodPlayersHistory.isEmpty())
                return;

            int maxPoints = goodPlayersHistory.size();
            int maxPlayers = Math.max(goodPlayersHistory.stream().max(Integer::compareTo).orElse(0),
                    evilPlayersHistory.stream().max(Integer::compareTo).orElse(0));

            int[] xPoints = new int[maxPoints + 2];
            int[] yPointsGood = new int[maxPoints + 2];
            int[] yPointsEvil = new int[maxPoints + 2];

            xPoints[0] = padding;
            yPointsGood[0] = height - padding;
            yPointsEvil[0] = height - padding;

            for (int i = 0; i < maxPoints; i++) {
                xPoints[i + 1] = padding + i * (width - 2 * padding) / maxPoints;
                yPointsGood[i + 1] = height - padding - goodPlayersHistory.get(i) * (height - 2 * padding) / maxPlayers;
                yPointsEvil[i + 1] = height - padding - evilPlayersHistory.get(i) * (height - 2 * padding) / maxPlayers;
            }

            xPoints[maxPoints + 1] = padding + (maxPoints - 1) * (width - 2 * padding) / maxPoints;
            yPointsGood[maxPoints + 1] = height - padding;
            yPointsEvil[maxPoints + 1] = height - padding;

            // Draw the filled areas
            g2d.setColor(new Color(173, 216, 230, 150)); // Light blue with some transparency
            g2d.fillPolygon(xPoints, yPointsGood, maxPoints + 2);

            g2d.setColor(new Color(255, 182, 193, 150)); // Light red with some transparency
            g2d.fillPolygon(xPoints, yPointsEvil, maxPoints + 2);

            // Draw the lines
            g2d.setColor(Color.BLUE);
            for (int i = 1; i < maxPoints; i++) {
                int x1 = padding + (i - 1) * (width - 2 * padding) / maxPoints;
                int y1Good = height - padding - goodPlayersHistory.get(i - 1) * (height - 2 * padding) / maxPlayers;
                int x2 = padding + i * (width - 2 * padding) / maxPoints;
                int y2Good = height - padding - goodPlayersHistory.get(i) * (height - 2 * padding) / maxPlayers;
                g2d.drawLine(x1, y1Good, x2, y2Good);
            }

            g2d.setColor(Color.RED);
            for (int i = 1; i < maxPoints; i++) {
                int x1 = padding + (i - 1) * (width - 2 * padding) / maxPoints;
                int y1Evil = height - padding - evilPlayersHistory.get(i - 1) * (height - 2 * padding) / maxPlayers;
                int x2 = padding + i * (width - 2 * padding) / maxPoints;
                int y2Evil = height - padding - evilPlayersHistory.get(i) * (height - 2 * padding) / maxPlayers;
                g2d.drawLine(x1, y1Evil, x2, y2Evil);
            }

            // Calculate the maximum speed and vision for scaling purposes
            double maxSpeed = averageSpeedHistory.stream().max(Double::compareTo).orElse(0.0);
            double maxVision = averageVisionHistory.stream().max(Double::compareTo).orElse(0.0);
            double maxMetric = Math.max(maxSpeed, maxVision);

            int maxPlayersHeight = height - 2 * padding;
            int maxMetricHeight = (int) (0.4 * maxPlayersHeight); // 0.4 times the height of the good/evil players graph

            int[] yPointsSpeed = new int[maxPoints];
            int[] yPointsVision = new int[maxPoints];

            for (int i = 0; i < maxPoints; i++) {
                yPointsSpeed[i] = height - padding - (int) (averageSpeedHistory.get(i) * maxMetricHeight / maxMetric);
                yPointsVision[i] = height - padding - (int) (averageVisionHistory.get(i) * maxMetricHeight / maxMetric);
            }

            // Draw the average speed and vision lines
            g2d.setColor(new Color(34, 139, 34)); // Green for average speed
            for (int i = 1; i < maxPoints; i++) {
                int x1 = padding + (i - 1) * (width - 2 * padding) / maxPoints;
                int y1Speed = yPointsSpeed[i - 1];
                int x2 = padding + i * (width - 2 * padding) / maxPoints;
                int y2Speed = yPointsSpeed[i];
                g2d.drawLine(x1, y1Speed, x2, y2Speed);
            }

            g2d.setColor(new Color(255, 140, 0)); // Orange for average vision
            for (int i = 1; i < maxPoints; i++) {
                int x1 = padding + (i - 1) * (width - 2 * padding) / maxPoints;
                int y1Vision = yPointsVision[i - 1];
                int x2 = padding + i * (width - 2 * padding) / maxPoints;
                int y2Vision = yPointsVision[i];
                g2d.drawLine(x1, y1Vision, x2, y2Vision);
            }
        }
    }
}