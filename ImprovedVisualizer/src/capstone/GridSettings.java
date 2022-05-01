package capstone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static main.java.capstone.GlobalSettings.gridSize;


public class GridSettings extends JPanel implements MouseListener, KeyListener, MouseMotionListener {

    private final Grid grid;

    char currentKey = ' ';

    Node currentNode = null;

    public GridSettings(int height, int width, Grid grid) {
        this.grid = grid;

        this.setSize(width, height);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }


    public void clearWalls() {
        grid.clearWalls();
        this.revalidate();
        this.repaint();
    }

    public void clearNodes() {
        grid.clearNodes();
        this.revalidate();
        this.repaint();
    }

    public Grid getGrid() {
        return grid;
    }

//    public void handler(MouseEvent e) {
//    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        super.paintComponent(g);
        g.setColor(Color.black);
        grid.drawGrid(g, this);
    }

    public void reset() {
        grid.resetPath();
        this.revalidate();
        this.repaint();
    }

    public void displayCosts(Node current, Graphics g) {
        Font costs = new Font("arial", Font.BOLD, 9);
        g.setFont(costs);
        g.setColor(Color.black);

        // get the cost + sort the positioning of the text
        g.drawString(Integer.toString((int) current.getFCost()), current.getX() + 3, current.getY() + 14);
        g.drawString(Integer.toString((int) current.getGCost()), current.getX() + 3, current.getY() + gridSize - 6);
        g.drawString(Integer.toString((int) current.getHCost()), current.getX() + 18, current.getY() + gridSize - 6);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX() / gridSize;
        int y = e.getY() / gridSize;

        if (x < 0 || y >= grid.getColumns() || y < 0 || x >= grid.getRows()) {
            return;
        }
        // click to place wall
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (grid.getNode(x, y) != null && !grid.getNode(x, y).isEndNode() && !grid.getNode(x, y).isStartNode()) {
                grid.getNode(x, y).addWall(true);
            }
        }
        // click to remove wall
        if (SwingUtilities.isRightMouseButton(e)) {
            if (grid.getNode(x, y) != null && !grid.getNode(x, y).isEndNode() && !grid.getNode(x, y).isStartNode()) {
                grid.getNode(x, y).addWall(false);
            }
        }
        currentNode = grid.getNode(x, y);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / gridSize;
        int y = e.getY() / gridSize;

        if (x < 0 || y >= grid.getColumns() || y < 0 || x >= grid.getRows()) {
            return;
        }

        // drags around start node
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentNode != null && currentNode.isStartNode()) {
                currentNode = grid.getNode(x, y);
                currentNode.addWall(false);
                grid.setStart(currentNode);
            }
        }
        // drags around end node
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentNode != null && currentNode.isEndNode()) {
                currentNode = grid.getNode(x, y);
                currentNode.addWall(false);
                grid.setEnd(currentNode);
            }
        }
        // draws walls
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentNode != null && !currentNode.isEndNode() && !currentNode.isStartNode()) {
                currentNode = grid.getNode(x, y);
                currentNode.addWall(true);
            }
        }
        // remove walls
        if (SwingUtilities.isRightMouseButton(e)) {
            if (currentNode != null && !currentNode.isEndNode() && !currentNode.isStartNode()) {
                currentNode = grid.getNode(x, y);
                currentNode.addWall(false);
            }
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX() / gridSize;
        int y = e.getY() / gridSize;

        if (x < 0 || y >= grid.getColumns() || y < 0 || x >= grid.getRows()) {
            return;
        }
        grid.getNode(x, y);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentKey = e.getKeyChar();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentKey = ' ';
    }

}