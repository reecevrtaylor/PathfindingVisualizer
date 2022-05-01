package capstone;

import java.util.ArrayList;

public class newAstar {

    private final int gridSize;
    private final newFrame frame;
    private final ArrayList<Node> walls;
    private final ArrayList<Node> open;
    private final ArrayList<Node> closed;
    private final ArrayList<Node> path;
    private int heuristicValue;
    private Node startNode, endNode, parentNode;
    private boolean searching;
    private boolean finish, noPath;

    public newAstar(newFrame frame, int gridSize) {
        this.frame = frame;
        this.gridSize = gridSize;

        // method to calc euclid distance between two nodes


//        heuristicValue = 0;
        searching = true;
        finish = false;

        walls = new ArrayList<>();
        open = new ArrayList<>();
        closed = new ArrayList<>();
        path = new ArrayList<>();
    }

    public void initNodes(Node start, Node end) {

        startNode = start;
        startNode.setGCost(0);

        endNode = end;

        // http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#breaking-ties
        // Manhattan distance heuristic
        int manhattanDist = Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());

        // diagonal distance
        int diagonalDist = (int) (Math.sqrt(2 * (Math.pow(gridSize, 2))));

        // similar to Euclidean distance heuristic
        int euclideanDist = (start.getX() - end.getX()) * (start.getX() - end.getX()) + (start.getY() - end.getY()) * (start.getY() - end.getY());

        heuristicValue = diagonalDist;

        // Adding the starting node to the closed list
        addClosed(startNode);
        startPathFinding(startNode);
        finish = true;
        System.out.println("Visualising...");
    }

    public void startPathFinding(Node parent) {

        if (searching) {
            // Detects and adds one step of nodes to open list
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i == 1 && j == 1) {
                        continue;
                    }
                    int potXValue = (parent.getX() - gridSize) + (gridSize * i);
                    int potYValue = (parent.getY() - gridSize) + (gridSize * j);

                    int x = parent.getX() + (potXValue - parent.getX());
                    int y = parent.getY() + (potYValue - parent.getY());

                    // Doesn't allow the algo to go through searching gaps
                    if (searchWall(x, parent.getY()) != -1 | searchWall(parent.getX(), y) != -1 && ((j == 0 | j == 2) && i != 1)) {
                        continue;
                    }

                    calcCost(potXValue, potYValue, parent);
                }
            }
        }
        // Set parent node
        parent = lowestFCost();

        // if the parent and end nodes are the same, the path is finish, connect the paths and set bools
        if (Node.isEqual(parent, endNode)) {
            endNode.setParent(parent.getParent());

            Path();
            finish = true;
            frame.repaint();

            System.out.println("\n***** A* Search Complete ***** " + "\nFinished with remaining open: " + open.size() +
                    "\nFinished with remaining closed: " + closed.size() + "\n");
            return;
        }

        // remove parent from open list
        removeOpen(parent);
        // add parent to closed list
        addClosed(parent);

        if (searching) {
            // the way the program searches
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
//                    System.out.println(i);
//                    System.out.println(j);

                    int potXValue = (parent.getX() - gridSize) + (gridSize * i);
                    int potYValue = (parent.getY() - gridSize) + (gridSize * j);
                    // loop from -1, add on grid size, have a look into changing
                    Node check = getOpen(potXValue, potYValue);

                    // If the checked node is open (null = no value = open)
                    if (check != null) {
                        int xDist = parent.getX() - check.getX();
                        int yDist = parent.getY() - check.getY();
                        int updateG = parent.getGCost();

                        if (xDist != 0 && yDist != 0) {
                            updateG += heuristicValue;
                        } else {
                            updateG += gridSize;
                        }
                        // g cost. parents g cost + euclid from par to cur
                        // update... something that figures out h cost of current node


                        if (updateG < check.getGCost()) {
                            int search = searchOpen(potXValue, potYValue);
                            int fCost = open.get(search).getGCost() + open.get(search).getHCost();
                            // update

                            open.get(search).setParent(parent);
                            open.get(search).setGCost(updateG);
                            open.get(search).setFCost(fCost);
                        }
                    }
                }
            }
        }
        parentNode = parent;
    }

    public void calcCost(int potXValue, int potYValue, Node parent) {

        // so nodes don't go through walls
        if (searchWall(potXValue, potYValue) != -1 | searchClosed(potXValue, potYValue) != -1 | searchOpen(potXValue, potYValue) != -1) {
            return;
        }

        // gets the height and width of border, so the search can't go out of bounds
        if (potXValue < 0 | potYValue < 0 | potXValue >= frame.getWidth() | potYValue >= frame.getHeight()) {
            return;
        }

        // init open nodes
        Node openNode = new Node(potXValue, potYValue);

        // set parent
        openNode.setParent(parent);

        // calculate g cost
        int xCost = openNode.getX() - parent.getX();
        int yCost = openNode.getY() - parent.getY();
        int gCost = parent.getGCost();

        if (xCost != 0 && yCost != 0) {
            gCost += heuristicValue;
        } else {
            gCost += gridSize;
        }

        openNode.setGCost(gCost);

        // calculate h Cost
        int xH = endNode.getX() - openNode.getX();
        int yH = endNode.getY() - openNode.getY();
        int hCost = xH + yH;
        openNode.setHCost(hCost);

        // calculate f cost
        int fCost = gCost + hCost;
        openNode.setFCost(fCost);

        addOpen(openNode);
    }

    public void Path() {
        if (getPathList().size() == 0) {
            Node parentNode = endNode.getParent();

            while (!Node.isEqual(parentNode, startNode)) {
                addPath(parentNode);

                for (int i = 0; i < getClosedList().size(); i++) {
                    Node currNode = getClosedList().get(i);

                    if (Node.isEqual(currNode, parentNode)) {
                        parentNode = currNode.getParent();
                        break;
                    }
                }
            }
            sort(getPathList());
        }
    }

    public void path() {

    }

    public void sort(ArrayList list) {
        int j = list.size() - 1;

        for (int i = 0; i < j; i++) {
            Object tempList = list.get(i);
            list.remove(i);
            list.add(i, list.get(j - 1));
            list.remove(j);
            list.add(j, tempList);
            j--;
        }
    }

    public void restart() {
        // while loop used to go through the whole arrayList and remove each node
        while (open.size() > 0) {
            open.remove(0);
        }
        while (closed.size() > 0) {
            closed.remove(0);
        }
        while (path.size() > 0) {
            path.remove(0);
        }

        // set these back to false, so you can redo
        noPath = false;
        finish = false;
    }

    public void addWall(Node node) {
        if (walls.size() == 0) {
            walls.add(node);
        } else if (!sameWall(node)) {
            walls.add(node);
        }
    }

    public void removeWall(int val) {
        walls.remove(val);
    }

    public boolean sameWall(Node node) {
        for (Node wall : walls) {
            if (node.getX() == wall.getX() && node.getY() == wall.getY()) {
                return true;
            }
        }
        return false;
    }

    public void addOpen(Node node) {
        if (open.size() == 0) {
            open.add(node);
        } else if (!sameOpen(node)) {
            open.add(node);
        }
    }

    public boolean sameOpen(Node node) {
        for (Node value : open) {
            if (node.getX() == value.getX() && node.getY() == value.getY()) {
                return true;
            }
        }
        return false;
    }

    public void removeOpen(Node node) {
        for (int i = 0; i < open.size(); i++) {
            if (node.getX() == open.get(i).getX() && node.getY() == open.get(i).getY()) {
                open.remove(i);
            }
        }
    }

    public Node getOpen(int x, int y) {
        for (Node node : open) {
            if (node.getX() == x && node.getY() == y) {
                return node;
            }
        }
        return null;
    }

    public void addClosed(Node node) {
        if (closed.size() == 0) {
            closed.add(node);
        } else if (!sameClosed(node)) {
            closed.add(node);
        }
    }

    public boolean sameClosed(Node node) {
        for (Node value : closed) {
            if (node.getX() == value.getX() && node.getY() == value.getY()) {
                return true;
            }
        }
        return false;
    }

    public void addPath(Node node) {
        path.add(node);
    }

    public Node lowestFCost() {
        if (open.size() > 0) {
            return open.get(0);
        }
        return null;
    }

    public int searchWall(int x, int y) {
        int val = 0;

        for (int i = 0; i < walls.size(); i++) {
            if (walls.get(i).getX() == x && walls.get(i).getY() == y) {
                val = i;
                break;
            }
        }
        return val;
    }

    public int searchClosed(int x, int y) {
        int val = 0;

        for (int i = 0; i < closed.size(); i++) {
            if (closed.get(i).getX() == x && closed.get(i).getY() == y) {
                val = i;
                break;
            }
        }
        return val;
    }

    public int searchOpen(int x, int y) {
        int val = 0;

        for (int i = 0; i < open.size(); i++) {
            if (open.get(i).getX() == x && open.get(i).getY() == y) {
                val = i;
                break;
            }
        }
        return val;
    }

    public ArrayList<Node> getWallList() {
        return walls;
    }

    public ArrayList<Node> getOpenList() {
        return open;
    }

    public ArrayList<Node> getClosedList() {
        return closed;
    }

    public ArrayList<Node> getPathList() {
        return path;
    }

    public boolean isFound() {
        return finish;
    }

    public Node getParent() {
        return parentNode;
    }

    public boolean noPath() {
        return noPath;
    }

    public void setManhattanDist(boolean manhattanDist) {
        searching = manhattanDist;
    }

    public void setDiagonalDist(boolean diagonalDist) {
        searching = diagonalDist;
    }

    public void setEuclideanDist(boolean euclideanDist) {
        searching = euclideanDist;
    }


}
