package capstone;

import javax.swing.*;
import java.awt.*;

public class Grid {
    private final int rows;
    private final int cols;


    private final Node[][] grid;

    private Node start;
    private Node end;


    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        grid = new Node[rows][cols];

        initGrid();
    }

    public void initGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Node(i, j);
            }
        }
        System.out.println("Rows: " + getRows());
        System.out.println("Col: " + getColumns());
        // sets position of start node to x 0, y 0
        start = grid[0][0];
        start.setStart(true);

        // set end node next to start, user can drag them where they want (makes it more user-friendly)
        end = grid[1][0];
        end.setEnd(true);
        // end node wasn't appearing on grid
//        System.out.println(end.getX());
//        System.out.println(end.getY());
    }

    public void clearWalls() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].addWall(false);
            }
        }
    }

    public void clearNodes() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].setStart(false);
                grid[i][j].setEnd(false);
            }
        }
    }

    public void resetPath() {
        restart();
    }

    public void restart() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                Node node = grid[i][j];
                node.setParent(null);
                // so when the grid rests, it removes any visited nodes and won't get stuck
                node.hasVisited(false);
                node.setState(NodeState.IDLE);
                node.setGCost(0);
                node.setHCost(0);
            }
        }
    }

    public void drawGrid(Graphics2D g, JPanel panel) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].draw(g, panel);
                g.setColor(Color.gray);
                g.drawRect(i * Node.getSize(), j * Node.getSize(), Node.getSize(), Node.getSize());
            }
        }
        start.draw(g, panel);
        end.draw(g, panel);
        panel.revalidate();
        panel.repaint();
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return cols;
    }

    public Node getNode(int x, int y) {
        return grid[x][y];
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        if (start.equals(end)) {
            return;
        }
        this.start.setStart(false);
        this.start = start;
        this.start.setStart(true);
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        if (start.equals(end)) {
            return;
        }
        this.end.setEnd(false);
        this.end = end;
        this.end.setEnd(true);
    }


}
