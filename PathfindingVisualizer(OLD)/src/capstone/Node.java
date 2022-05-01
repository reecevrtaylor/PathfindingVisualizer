package capstone;

public class Node implements Comparable<Node> {
    private int x;
    private int y;
    private int g;
    private int h;
    private int f;
    private Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static boolean isEqual(Node start, Node end) {
        return start.getX() == end.getX() && start.getY() == end.getY();
    }

    public int getX() {
        return x+1;
    }

    public int getY() {
        return y+1;
    }

    public void setXYValues(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getGCost() {
        return g;
    }

    public void setGCost(int g) {
        this.g = g;
    }

    public int getHCost() {
        return h;
    }

    public void setHCost(int h) {
        this.h = h;
    }

    public int getFCost() {
        return f;
    }

    public void setFCost(int f) {
        this.f = f;
        // return g cost + h cost
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
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
    public int compareTo(Node comparison) {

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
}
