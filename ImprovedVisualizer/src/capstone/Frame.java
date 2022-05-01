package capstone;

import javax.swing.*;
import java.awt.*;

import static main.java.capstone.GlobalSettings.gridSize;

public class Frame extends JFrame {

    public static final int HEIGHT = 700;
    public static final int row = HEIGHT / gridSize;
    public static final int column = HEIGHT / gridSize;
    public final int WIDTH = 875;

    public Frame() {

        Grid grid = new Grid(row, column);
        JPanel panel = new JPanel();
        GridSettings gridSettings = new GridSettings(HEIGHT, WIDTH, grid);
        // add on the control panel
        ControlSettings controlSettings = new ControlSettings(175, HEIGHT, gridSettings);

        panel.setLayout(new BorderLayout());
        panel.add(BorderLayout.WEST, controlSettings);
        panel.add(BorderLayout.CENTER, gridSettings);

        this.setTitle("Pathfinding Visualization");
        this.setContentPane(panel);
        this.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.pack();
        this.setVisible(true);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}