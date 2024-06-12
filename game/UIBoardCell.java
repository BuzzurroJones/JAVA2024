package game;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class UIBoardCell extends JPanel {
    final  static Color BLUE = new Color(0, 128, 255, 200);
    final  static Color RED = new Color(255, 51, 51, 200);

    private int state;
    private boolean hasFood;

    public UIBoardCell(int state, boolean hasFood) {
        this.state = state;
        this.hasFood = hasFood;
        setPreferredSize(new java.awt.Dimension(50, 50));
    }

    public void setState(int state) {
        this.state = state;
        repaint();
    }

    public void setHasFood(boolean hasFood) {
        this.hasFood = hasFood;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        switch (state) {
            case Constants.RED:
                g.setColor(RED);
                break;
            case Constants.BLUE:
                g.setColor(BLUE);
                break;
            case Constants.EMPTY:
            default:
                g.setColor(Color.WHITE);
                break;
        }
        
        g.fillRect(0, 0, getWidth(), getHeight());
        
        if (hasFood) {
            g.setColor(Color.GREEN);
            g.drawRect(0, 0, getWidth()-1, getHeight()-1);
        }
    }
}
