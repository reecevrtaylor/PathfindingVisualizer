package capstone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class frame extends JFrame {

    final int WIDTH = 30;
    final int LENGTH = 20;
    final int wWIDTH = 1375;
    final int wLENGTH = 950;
    // INITs
    int space = 1;
    int mouseX = 0;
    int mouseY = 0;
    int[][] grid = new int[WIDTH][LENGTH];


    public frame() {
        this.setTitle("Grid");
        this.setSize(wWIDTH, wLENGTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(true);

        Board board = new Board();
        this.setContentPane(board);

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);

        Mode mode = new Mode();
        this.addKeyListener(mode);
    }

    public static void main(String[] args) {
        frame frame = new frame();
    }

    public int xValue() {
        int i;
        for (i = 0; i < 35; i++) {
            for (int j = 0; j < 25; j++) {
                if (mouseX >= space + i * 45 && mouseX < i * 45 + 45 - space && mouseY >= space + j * 45 + 45 && mouseY < j * 45 + 80 - space) {
                    return i;
                }
            }

        }
        return i;

    }

    public int yValue() {
        int i;
        for (i = 0; i < 35; i++) {
            for (int j = 0; j < 25; j++) {
                if (mouseX >= space + i * 45 && mouseX < i * 45 + 45 - space && mouseY >= space + j * 45 + 45 && mouseY < j * 45 + 80 - space) {
                    return j;


                }
            }

        }
        return i;

    }

    public class Board extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.black);
            g.fillRect(0, 0, wWIDTH, wLENGTH);

            // how many squares
            for (int i = 0; i < frame.this.WIDTH; i++) {
                for (int j = 0; j < frame.this.LENGTH; j++) {
                    g.setColor(Color.white);
                    // test to see how to change square based on click position
                    if (mouseX >= space + i * 45 && mouseX < i * 45 + 45 - space && mouseY >= space + j * 45 + 45 && mouseY < j * 45 + 80 - space) {
                        g.setColor(Color.red);
                        // testing values debugging
                        if (xValue() == 0 && yValue() == 0) {
                            g.setColor(Color.black);
                        }
                    }
                    // size of each square
                    g.fillRect(space + i * 45, space + j * 45, 45 - 2 * space, 45 - 2 * space);

                }

            }

        }


    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
//            for hover over square you want to select
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    public class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            // testing how ML looks for x and y values
            mouseX = e.getX();
            mouseY = e.getY();
            if (xValue() != -1 && yValue() != -1)
                System.out.println("Box located at [" + xValue() + ", " + yValue() + "]");

            // DEBUG
            if (xValue() == 1 && yValue() == 1) {
                System.out.println("test");
            }


        }

        @Override
        public void mousePressed(MouseEvent e) {

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
    }

    // key listener to eventually change 'mode' (press 's' to put down start node, 'e' for end node etc etc.)
    public class Mode implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {


        }

        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_S) {
                System.out.println("Please place start node");
            } else if (e.getKeyCode() == KeyEvent.VK_E) {
                System.out.println("Please place end node");

            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

}
