package capstone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class newFrame extends JPanel implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener, ActionListener {

    // java class inits
    JFrame frame;
    newAstar astar;
    Node startNode, endNode;
    Node[][] gridNodes;
    Dimension screen;
    // Dimension used to handle frame sizing without needing final values (used for scalability)
    Dimension frameSize;

    // control inits
    char currentKey = ' ';
    int gridSize = 38;
    Timer visualise = new Timer(0, this);
    // A* inits+
    private ArrayList<Node> openNodes, closeNodes, path;

    public newFrame() {
        init();
    }

    // frame inits
    public void init() {
        // Set up A*
        astar = new newAstar(this, gridSize);
        // sets specified heuristic / search function (for future)
//        astar.setManhattanDist(true);
        astar.setDiagonalDist(true);
//        astar.setEuclideanDist(true);

        addMouseListener(this);
        addKeyListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        // Set up frame
        frame = new JFrame();
        frame.setContentPane(this);
        frame.setTitle("Pathfinding Visualiser");
        frame.getContentPane().setPreferredSize(new Dimension(800, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // update and validate frame
        this.revalidate();
        this.repaint();
    }

    void startSearch() {
        if (startNode != null && endNode != null) {
            astar.initNodes(startNode, endNode);
        } else {
            System.out.println("Please select a start and an end node.");
        }
    }

    public void paint(Graphics g) {
        // https://www.colorspire.com/rgb-color-wheel/
        Color pathColour = new Color(57, 212, 250);
        Color openColour = new Color(123, 249, 140);
        Color closedColour = new Color(255, 102, 102);


        if (astar.noPath() || astar.isFound()) {
            visualise.start();
        }
        super.paintComponent(g);


        // testing auto generate default grid...
        if (gridNodes == null) {
            gridNodes = new Node[gridSize][gridSize];
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    gridNodes[i][j] = new Node(i, j);
                }
            }
        }
        // Draws grid
        g.setColor(Color.black);
        for (int j = 0; j < this.getHeight(); j += gridSize) {
            for (int i = 0; i < this.getWidth(); i += gridSize) {
                g.drawRect(i, j, gridSize, gridSize);
            }
        }

        // Draws all walls
        g.setColor(Color.black);
        for (int i = 0; i < astar.getWallList().size(); i++) {
            g.fillRect(astar.getWallList().get(i).getX() + 1, astar.getWallList().get(i).getY(),
                    gridSize - 1, gridSize - 1);
        }

        // Draws open nodes
        for (int i = 0; i < astar.getOpenList().size(); i++) {
            Node curr = astar.getOpenList().get(i);

            g.setColor(openColour);
            g.fillRect(curr.getX(), curr.getY(), gridSize - 1, gridSize - 1);
        }

        // Draws closed nodes
        for (int i = 0; i < astar.getClosedList().size(); i++) {
            Node curr = astar.getClosedList().get(i);

            g.setColor(closedColour);
            g.fillRect(curr.getX(), curr.getY(), gridSize - 1, gridSize - 1);
        }

        // Draws path
        for (int i = 0; i < astar.getPathList().size(); i++) {
            Node curr = astar.getPathList().get(i);

            g.setColor(pathColour);
            g.fillRect(curr.getX(), curr.getY(), gridSize - 1, gridSize - 1);
        }

        // Draws start node
        if (startNode != null) {
            g.setColor(Color.blue);
            g.fillRect(startNode.getX(), startNode.getY(), gridSize - 1, gridSize - 1);
        }
        // Draws end node
        if (endNode != null) {
            g.setColor(Color.red);
            g.fillRect(endNode.getX(), endNode.getY(), gridSize - 1, gridSize - 1);
        }
    }

    // key & mouse events
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        currentKey = e.getKeyChar();
        // Start if space is pressed
        if (currentKey == KeyEvent.VK_SPACE) {
            startSearch();
        }

        if (currentKey == 'c') {
            // clears the board (temp measure, want to add it as a button)
            astar.restart();
        }

        if (currentKey == 'g') {

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentKey = ' ';
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println("clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int xVal = e.getX() % gridSize;
        int yVal = e.getY() % gridSize;


        if (SwingUtilities.isLeftMouseButton(e)) {
            // create start node
            if (startNode == null) {
                // - the vals will give exact position
                startNode = new Node(e.getX() - xVal, e.getY() - yVal);
            } else {
                startNode.setXYValues(e.getX() - xVal, e.getY() - yVal);
            }
            repaint();

            // create end node

            } else if (SwingUtilities.isRightMouseButton(e)) {
            if (endNode == null) {
                // - the vals will give exact position
                endNode = new Node(e.getX() - xVal, e.getY() - yVal);
            } else {
                endNode.setXYValues(e.getX() - xVal, e.getY() - yVal);
            }
            repaint();
            // remove start node
            if (startNode != null && xVal == startNode.getX() && startNode.getY() == yVal) {
                startNode = null;
                repaint();
            }
            // remove end node
            if (endNode != null && xVal == endNode.getX() && endNode.getY() == yVal) {
                endNode = null;
                repaint();
            }
        }
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
//        System.out.println("dragged");
        int xVal = e.getX() % gridSize;
        int yVal = e.getY() % gridSize;

        if (SwingUtilities.isLeftMouseButton(e)) {
            // - the vals will give exact position
            int xWall = e.getX() - (e.getX() % gridSize);
            int yWall = e.getY() - (e.getY() % gridSize);

            Node newWall = new Node(xWall, yWall);
            astar.addWall(newWall);

            repaint();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            int xClicked = e.getX() - (e.getX() % gridSize);
            int yClicked = e.getY() - (e.getY() % gridSize);

            int val = astar.searchWall(xClicked, yClicked);
            if (val != 0) {
                astar.removeWall(val);
            }
            repaint();
        }


    }

    @Override
    public void mouseMoved(MouseEvent e) {
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (astar.isFound()) {
            astar.startPathFinding(astar.getParent());
        }
        repaint();
    }
}

