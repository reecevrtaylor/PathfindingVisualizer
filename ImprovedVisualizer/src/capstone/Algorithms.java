package capstone;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeSet;

public class Algorithms extends Thread {

    private final Grid nodes;
    private final JPanel grid;
    private boolean foundEnd = false;

    private long runTime;
    private long startTime;
    private long endTime;

    private String strHeuristic;

    public Algorithms(Grid nodes, JPanel grid) {
        this.nodes = nodes;
        this.grid = grid;
    }

    // run thread without any access restrictions
    @Override
    public void run() {
        if (GlobalSettings.running) {
            foundEnd = false;
            switch (GlobalSettings.algorithm) {
                case 0 -> AStar(nodes.getStart());
                case 1 -> BFS(nodes.getStart());
            }
        }
    }

    private void AStar(Node startNode) {
        TreeSet<Node> list = new TreeSet<>();
        // add the start node to new tree set
        list.add(startNode);
        // set start node to already have been visited
        startNode.hasVisited(true);

        // while it's running, the list isn't empty and there isn't an end
        while (GlobalSettings.running && !list.isEmpty() && !foundEnd) {
            // record start time
            startTime = System.currentTimeMillis();
            // remove first element
            Node currentNode = list.pollFirst();
            assert currentNode != null;
            // new current node is now the next first element after poll
            currentNode.setState(NodeState.CURRENT);
            setAnimationSpeed();

            // if current node equals the end node
            if (currentNode.equals(nodes.getEnd())) {
                // receive path from current node
                path(currentNode);
                GlobalSettings.running = false;
                foundEnd = true;
                // record end time
                endTime = System.currentTimeMillis();
                // calculate runtime
                runTime = endTime - startTime;
//                System.out.println(currentNode.getHeuristicValue());

                if (GlobalSettings.heuristic == 0) {
                    strHeuristic = "Manhattan";
                } else if (GlobalSettings.heuristic == 1) {
                    strHeuristic = "Euclidean";
                } else if (GlobalSettings.heuristic == 2) {
                    strHeuristic = "Diagonal";
                } else if (GlobalSettings.heuristic == 3) {
                    strHeuristic = "Octile";
                } else if (GlobalSettings.heuristic == 4) {
                    strHeuristic = "Dijkstra (no h(n))";
                } else if (GlobalSettings.heuristic == 5) {
                    strHeuristic = "Random";
                }

                System.out.println("\n***** A* Search Complete ***** " + "\nHeuristic: " + strHeuristic +
                        "\nTime: " + runTime + "ms" + "\nFinished with remaining open: " + list.size());
                if (runTime == 0) {
                    System.out.println("!!! USING NO ANIMATION INHIBITS TIME CALCULATION !!!");
                }
                return;
            } else {
                currentNode.setState(NodeState.CLOSED);
                for (Node child : currentNode.getNeighbour(nodes)) {
                    list.add(child);
                    child.hasVisited(true);
                    child.setState(NodeState.OPEN);
                }
            }
        }
        // if program is still in running state & openQueue queue is empty with no path to the end
        if (GlobalSettings.running && list.isEmpty() && !foundEnd) {
            // no solution
            System.out.println("No solution found");
        }
    }


    private void BFS(Node startNode) {
        // init queue & linked list
        Queue<Node> openQueue = new LinkedList<>();
        Node currentNode;
        openQueue.add(startNode);

        // while it's in its solving state, and it hasn't finished
        while (GlobalSettings.running && !openQueue.isEmpty() && !foundEnd) {
            // record start time
            startTime = System.currentTimeMillis();
            // gets and removes head of queue
            currentNode = openQueue.poll();
            assert currentNode != null;
            currentNode.setState(NodeState.CURRENT);
            setAnimationSpeed();

            // if the current node equals to the end node
            if (currentNode.equals(nodes.getEnd())) {
                // get the path from current node
                path(currentNode);
                // set running state to false
                GlobalSettings.running = false;
                // found the end bool
                foundEnd = true;
                // record end time.
                endTime = System.currentTimeMillis();
                // calculate runtime
                runTime = endTime - startTime;
                System.out.println("\n***** BFS Search Complete ***** " + "\nTime: " + runTime + "ms" +
                        "\nFinished with remaining open: " + openQueue.size());

                if (runTime == 0) {
                    System.out.println("!!! USING NO ANIMATION INHIBITS TIME CALCULATION !!!");
                }
            } else {
                // if the current node is not the end node,
                // set current node to closed
                currentNode.setState(NodeState.CLOSED);
                // add neighbours to open node
                for (Node neighbour : currentNode.getNeighbour(nodes)) {
                    openQueue.add(neighbour);
                    neighbour.setState(NodeState.OPEN);
                    neighbour.hasVisited(true);
                }
            }
        }
        // if program is still in running state & openQueue queue is empty with no path to the end
        if (GlobalSettings.running && openQueue.isEmpty() && !foundEnd) {
            // no solution
            System.out.println("No solution found");
        }
    }

    // Test DFS, not working yet
    private void DepthFirstSearch(Node startNode) {
        Stack<Node> stack = new Stack<>();
        stack.push(startNode);
        while (!stack.isEmpty()) {
            Node current = stack.pop();
            if (current.equals(nodes.getEnd())) {
                path(current);
                GlobalSettings.running = false;
                foundEnd = true;
                endTime = System.currentTimeMillis();
                runTime = endTime - startTime;
                System.out.println("\n***** DFS Search Complete ***** " + "\nTime: " + runTime + "ms" +
                        "\nFinished with remaining open: " + stack.size());
            }
            for (Node neighbor : current.getNeighbour(nodes)) {
                if (neighbor.getState() == NodeState.IDLE) {
                    neighbor.setState(NodeState.CLOSED);
                    stack.push(neighbor);
                }
            }
        }
    }


    public void path(Node node) {
        Node parent = node.getParent();

        while (!nodes.getStart().equals(parent)) {
            parent.setState(NodeState.PATH);
            grid.revalidate();
            grid.repaint();
            setAnimationSpeed();
            parent = parent.getParent();
        }
        grid.revalidate();
        grid.repaint();
    }

    public void setAnimationSpeed() {
        try {
            Thread.sleep(GlobalSettings.animationSpeed);
            grid.repaint();
        } catch (InterruptedException ignored) {
        }
    }


}