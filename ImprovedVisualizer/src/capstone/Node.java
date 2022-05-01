package capstone;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

import static capstone.GlobalSettings.gridSize;

public class Node implements Comparable<Node> {

    double heuristicValue;
    private int x;
    private int y;
    private int g;
    private double h;
    private double f;
    //    // best to set an idle state the program can return to when parts have completed
    private NodeState nodeState = NodeState.IDLE;

    private boolean isWall = false;
    private boolean isStartNode = false;
    private boolean isEndNode = false;

    private boolean visited;

    private Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static int getSize() {
        return gridSize;
    }

    public void draw(Graphics2D g, JPanel panel) {

        g.setColor(Color.black);
        if (isWall) {
            g.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
        }

        // https://www.colorspire.com/rgb-color-wheel/
        Color pathColour = new Color(57, 212, 250);
        Color openColour = new Color(123, 249, 140);
        Color closedColour = new Color(255, 102, 102);
        Color currentColour = new Color(205, 89, 240);

        switch (nodeState) {
            case START:
                g.setColor(Color.blue);
                break;
            case END:
                g.setColor(Color.red);
                break;
            case OPEN:
                g.setColor(openColour);
                break;
            case CLOSED:
                g.setColor(closedColour);
                break;
            case CURRENT:
                g.setColor(currentColour);
                break;
            case PATH:
                g.setColor(pathColour);
                break;

            default:
                return;
        }
        g.setStroke(new BasicStroke(1.5f));
        g.fillRect(x * gridSize, y * gridSize, gridSize, gridSize);
        panel.revalidate();
        panel.repaint();
    }

    // created a heuristic method
    public double setHeuristic(Grid grid) {
        // http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#breaking-ties

        //x diff y diff variables, stops repeating
        int xDiff = Math.abs(this.x - grid.getEnd().getX());
        int yDiff = Math.abs(this.y - grid.getEnd().getY());

        // Manhattan distance heuristic
        // "this" is startNode in these instances.
        double manhattanDist = Math.abs(xDiff) + Math.abs(yDiff);

        // similar to Euclidean distance heuristic
        double euclideanDist = (xDiff) * (xDiff) + (yDiff) * (yDiff);

        // diagonal distance
        double diagonalDist = xDiff + yDiff - (Math.sqrt(2) - 1) * Math.min(xDiff, yDiff);
        // x diff + y diff - ( sqrt(2)-1 ) * math.min( x y diff)

        double octileDist = Math.sqrt(2) * Math.min(xDiff, yDiff) + (1 - Math.sqrt(2)) * Math.max(xDiff, yDiff);

        // dijkstra is essentially a* without a heuristic (f(n) = g(n))
        int dijkstra = 0;

        double randomWalkHeuristic = Math.random() * gridSize;

        double chebyshevDist = Math.max(xDiff, yDiff);

        // ONLY ALLOWS 4 DIRECTIONS ATM, NO DIAGONALS

        if (GlobalSettings.running) {
            switch (GlobalSettings.heuristic) {
                case 0 -> heuristicValue = manhattanDist;
                case 1 -> heuristicValue = euclideanDist;
                case 2 -> heuristicValue = diagonalDist;
                case 3 -> heuristicValue = octileDist;
                case 4 -> heuristicValue = dijkstra;
                case 5 -> heuristicValue = chebyshevDist;
                case 6 -> heuristicValue = randomWalkHeuristic;
            }
        }
        return heuristicValue;
    }

    public double getHeuristicValue() {
        return heuristicValue;
    }

    public void neighbour(Node node, Grid grid) {
        node.setParent(this);
        node.setGCost(this.g + 1);
        node.setHCost(node.setHeuristic(grid));
        node.setFCost();
    }

    public LinkedList<Node> getNeighbour(Grid grid) {
        LinkedList<Node> neighbours = new LinkedList<>();
        if (this.x + 1 < grid.getRows() && x >= 0) {
            Node node = grid.getNode(this.x + 1, this.y);
            if (!node.visited && !node.isWall) {
                neighbour(node, grid);
                neighbours.add(node);
            }
        }

        if (this.y + 1 < grid.getColumns() && this.y >= 0) {
            Node node = grid.getNode(this.x, this.y + 1);
            if (!node.visited && !node.isWall) {
                neighbour(node, grid);
                neighbours.add(node);
            }
        }

        if (this.y - 1 >= 0 && this.y < grid.getColumns()) {
            Node node = grid.getNode(this.x, this.y - 1);
            if (!node.visited && !node.isWall) {
                neighbour(node, grid);
                neighbours.add(node);
            }
        }

        if (this.x - 1 >= 0 && this.x < grid.getRows()) {
            Node node = grid.getNode(this.x - 1, this.y);
            if (!node.visited && !node.isWall) {
                neighbour(node, grid);
                neighbours.add(node);
            }
        }
        return neighbours;
    }

    public void hasVisited(boolean visited) {
        this.visited = visited;
    }

    public void addWall(boolean isWall) {
        if (isWall) {
            this.setState(NodeState.WALL);
        } else {
            this.setState(NodeState.IDLE);
        }
        this.isWall = isWall;
    }

    @Override
    // generic equals method
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Node compare = (Node) obj;
        return x == compare.x && y == compare.y;
    }

    @Override
    // from implementing comparable to node, we can now use compareTo override method to compare two nodes
    public int compareTo(Node comparison) {
        // 0 = A*
        if (GlobalSettings.algorithm == 0) {
            if (this.equals(comparison)) {
                return 0;
            }
            if (this.getFCost() > comparison.getFCost()) {
                return 1;
            } else if (this.getFCost() < comparison.getFCost()) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getHCost() {
        return h;
    }

    public void setHCost(double h) {
        this.h = h;
    }

    public double getFCost() {
        return f;
    }

    public double getGCost() {
        return g;
    }

    public void setGCost(int g) {
        this.g = g;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public NodeState getState() {
        return nodeState;
    }

    public void setState(NodeState nodeState) {
        if (isStartNode || isEndNode) {
            return;
        }
        this.nodeState = nodeState;
    }

    public void setFCost() {
        this.f = this.g + this.h;
    }

    public void setStart(boolean isStartNode) {
        // SET THE STATES, otherwise when you drag node, will create a wall of that node (like when you drag to create walls)
        if (isStartNode) {
            this.nodeState = NodeState.START;
        }
        this.isStartNode = isStartNode;
    }

    public void setEnd(boolean isEndNode) {
        // SET THE STATES, otherwise when you drag node, will create a wall of that node (like when you drag to create walls)
        if (isEndNode) {
            this.nodeState = NodeState.END;
        } else {
            this.nodeState = NodeState.IDLE;
        }
        this.isEndNode = isEndNode;
    }

    public boolean isStartNode() {
        return isStartNode;
    }

    public boolean isEndNode() {
        return isEndNode;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setXYValues(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void getXYValues(int x, int y) {
        this.x = x;
        this.y = y;
    }

}

