package capstone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ControlSettings extends JPanel implements ActionListener {
    private final JComboBox<String> algorithms;
    private final JComboBox<String> heuristics;

    private final JCheckBox disableAnimation;

    private final GridSettings gridSettings;

    Algorithms a = null;

    public ControlSettings(int width, int height, GridSettings gridSettings) {
        this.gridSettings = gridSettings;

        this.setPreferredSize(new Dimension(width, height));
        this.setFocusable(true);
        this.setLayout(null);


        // *** HEURISTIC SELECTION ***
        JLabel heuristicLabel = new JLabel("Select a heuristic (A*)");
        heuristicLabel.setBounds(27, 5, 125, 20);

        String[] heuristicSelection = {"Manhattan", "Euclidean", "Diagonal", "Octile", "Dijkstra (no h(n))", "Chebyshev", "Random"};
        heuristics = new JComboBox<>(heuristicSelection);
        heuristics.setBounds(15, 30, 150, 30);
        heuristics.addActionListener(e -> {
            if (GlobalSettings.running) {
                heuristics.setSelectedIndex(GlobalSettings.heuristic);
                return;
            }
            GlobalSettings.heuristic = heuristics.getSelectedIndex();
        });

        // *** ALGORITHM SELECTION ***
        JLabel algorithmLabel = new JLabel("Select an algorithm");
        algorithmLabel.setBounds(27, 65, 125, 20);

        String[] selection = {"A*", "BFS", "DFS"};
        algorithms = new JComboBox<>(selection);
        algorithms.setBounds(15, 90, 150, 30);
        algorithms.addActionListener(e -> {
            if (GlobalSettings.running) {
                algorithms.setSelectedIndex(GlobalSettings.algorithm);
                return;
            }
            GlobalSettings.algorithm = algorithms.getSelectedIndex();
        });

        // *** GENERATION LABELS ***
        JLabel generationLabel = new JLabel("Generation Buttons:");
        generationLabel.setBounds(35, 155, 150, 20);

        // *** MAZE GENERATION ***
        JButton mazeGen = new JButton("Generate Maze");
        mazeGen.setBounds(15, 180, 150, 30);

//        mazeGen.setBounds(350, 650, 150, 30);
        mazeGen.addActionListener(e -> {
            if (GlobalSettings.running) {
                return;
            }

            gridSettings.clearWalls();

            Grid grid = gridSettings.getGrid();
            Random r = new Random();

            for (int i = 0; i < grid.getRows(); i++) {
                for (int j = 0; j < grid.getColumns(); j++) {
                    Node node = grid.getNode(i, j);
//                    System.out.println(node);
                    // the float number here allows the control how much of the grid we want to be a wall, 0.1 = almost every square is a wall, 0.9 = almost no square is a wall.
                    if (!node.isStartNode() && !node.isEndNode() && r.nextFloat() > 0.65) {
                        node.addWall(true);
                    }
                }
            }

        });

        // *** RANDOMIZE NODE GENERATION ***
        JButton nodeGen = new JButton("Randomize Nodes");
        nodeGen.setBounds(15, 220, 150, 30);

        nodeGen.addActionListener(e -> {
            if (GlobalSettings.running) {
                return;
            }
            gridSettings.clearNodes();

            // random number generation based on rows (doesn't matter as both will be the same value as it's a square grid)
            Grid grid = gridSettings.getGrid();
            Random r = new Random();

            int max = grid.getRows();

            int sX = r.nextInt(max);
            int sY = r.nextInt(max);

            int eX = r.nextInt(max);
            int eY = r.nextInt(max);

            Node sNode = grid.getNode(sX, sY);
            Node eNode = grid.getNode(eX, eY);
//            System.out.println(sNode.isStartNode());

            for (int i = 0; i < grid.getRows(); i++) {
                for (int j = 0; j < grid.getColumns(); j++) {
                    Node node = grid.getNode(i, j);
                    // if no walls are placed
                    if (!node.isWall()) {
                        grid.setStart(sNode);
                        grid.setEnd(eNode);
                    }
//                    System.out.println(node);
                    // if the start or end node is placed into a wall, it will remove the wall so search will not be impacted
                    if (node.isStartNode() == node.isWall() || node.isEndNode() == node.isWall()) {
                        node.addWall(false);
                        node.addWall(false);
                    } else {
                        grid.setStart(sNode);
                        grid.setEnd(eNode);

                    }
                }
            }

        });

        // *** CLEAR SETTINGS ***
        JLabel clearLabel = new JLabel("Clear Buttons:");
        clearLabel.setBounds(50, 275, 150, 20);

        // *** CLEAR GRID ***
        JButton gridClear = new JButton("Clear Grid");
        gridClear.setBounds(15, 300, 150, 30);

        gridClear.addActionListener(e -> {
            if (GlobalSettings.running) {
                return;
            }
            gridSettings.clearWalls();
        });

        // ** STOP PROGRAM AND OR REMOVE PATH **
        JButton stopSearch = new JButton("Stop / Remove Path");
        stopSearch.setBounds(15, 340, 150, 30);

        stopSearch.addActionListener(e -> {
            GlobalSettings.running = false;
            gridSettings.reset();
            gridSettings.repaint();
        });

        // *** USER HELP BUTTON ***
        JLabel helpLabel = new JLabel("Read Instructions:");
        helpLabel.setBounds(39, 405, 150, 20);

        JButton help = new JButton("Instructions");
        help.setBounds(15, 430, 150, 30);

        String gridKey = """
                <html>
                *** GRID KEY ***
                <ul>
                <li><font color=blue>Start Node</font>
                <li><font color=red>End Node</font>
                <li><font color=black>Wall</font>
                <li><font color=black>Wall</font>
                <li><font color=black>Wall</font>
                <li><font color=cyan>Path</font>
                <li><font size=-2>small</font>
                <li><font size=+2>large</font>
                <li><i>italic</i>
                <li><b>bold</b>
                </ul>
                """;

        help.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, """
                    *** USER INSTRUCTIONS ***
                    Select the algorithm you would like to use using the drop down menu.
                    For A*, you can select the heuristic you would like to use.
                    Click and drag to place the start and end nodes.
                    Click and drag on the grid to place walls.
                    Holding right click will remove walls.
                    Click the randomize nodes button to generate random positions for the start and end node.
                    Click the clear grid button to clear the grid.
                    Click the stop / remove path button to stop the search while it's running, or to remove the path if it's already been found.
                    Check the Disable Animation checkbox to disable animation.
                    Click the visualize button to start the search.

                    *** GRID KEY ***
                    Start Node = Blue
                    End Node = Red
                    Wall = Black
                    Open Nodes = Green
                    Closed Nodes = Light Red
                    Path = Cyan

                     *** PLEASE IGNORE THE BLUE START NODE WALL IM FIXING IT ***""", "Instructions", JOptionPane.INFORMATION_MESSAGE);
        });


        // *** INSTANT PATH ***
        disableAnimation = new JCheckBox("Disable Animation");
        disableAnimation.setBounds(25, 625, 150, 30);
        disableAnimation.setFocusable(false);

        disableAnimation.addChangeListener(e -> {
            if (GlobalSettings.disableAnimation) {
                // 0 = instant / no animation
                GlobalSettings.animationSpeed = 0;
            } else {
                GlobalSettings.animationSpeed = 5;
            }

            if (GlobalSettings.running) {
                if (GlobalSettings.disableAnimation) {
                    disableAnimation.setSelected(true);
                    GlobalSettings.disableAnimation = true;
                } else {
                    disableAnimation.setSelected(false);
                    GlobalSettings.disableAnimation = false;
                }
                return;
            }
            GlobalSettings.disableAnimation = disableAnimation.isSelected();
        });

        // *** VISUALIZE SEARCH ***
        JButton search = new JButton("Visualize");
        search.setBounds(15, 660, 150, 30);
        search.addActionListener(this);

        // add to control panel
        // TOP OF CONTROL
        this.add(mazeGen);
        this.add(nodeGen);
        this.add(heuristicLabel);
        this.add(heuristics);
        this.add(algorithmLabel);
        this.add(algorithms);
        this.add(gridClear);
        this.add(stopSearch);

        // MIDDLE OF CONTROL (JLABLES)
        this.add(generationLabel);
        this.add(clearLabel);
        this.add(helpLabel);
        this.add(help);

        // BOTTOM OF CONTROL
        this.add(disableAnimation);
        this.add(search);
    }

    // testing method, doesn't work atm
    public void noInterrupt() {
        if (GlobalSettings.running) {
            return;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gridSettings.reset();
        GlobalSettings.running = true;
        a = new Algorithms(gridSettings.getGrid(), gridSettings);
        // calls run thread
        a.start();
    }
}

