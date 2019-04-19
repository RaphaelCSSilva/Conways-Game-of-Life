import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ThreadLocalRandom;

public class Life implements MouseListener, ActionListener, Runnable {
    private static final int rows = 100;
    private static final int cols = 100;
    private static int downtime = 500;
    private boolean[][] cells = new boolean[rows][cols];
    private static int birthCount;
    private static int deathCount;

    private JFrame frame = new JFrame("Life");
    private LifePanel panel = new LifePanel(cells);
    private JButton start = new JButton("Start");
    private JButton pause = new JButton("Pause");
    private JButton step = new JButton("Step");
    private JButton reset = new JButton("Reset");
    private boolean running = false;
    private JButton speed = new JButton("Speed");

    public Life() {
        //Setting up the frame
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        panel.addMouseListener(this);

        //South Container
        Container south = new Container();
        south.setLayout(new GridLayout(1, 3));
        south.add(start);
        start.addActionListener(this);
        south.add(pause);
        pause.addActionListener(this);
        south.add(step);
        step.addActionListener(this);
        south.add(reset);
        reset.addActionListener(this);
        frame.add(south, BorderLayout.SOUTH);

        //Speed button
        speed.addActionListener(this);

        //Toolbar
        JToolBar toolBar = new JToolBar();
        frame.add(toolBar, BorderLayout.NORTH);
        toolBar.add(speed);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    private static void fillGrid(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = ThreadLocalRandom.current().nextInt(0, 2);
            }
        }

    }

    private static int countNeighbors(boolean[][] grid, int row, int col) {
        int total = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            if (i >= 0 && i < grid.length)
                for (int j = col - 1; j <= col + 1; j++)
                    if (j >= 0 && j < grid[i].length)
                        total += (grid[i][j] && !(i == row && j == col) ? 1 : 0);
        }
        return total;
    }

    private static boolean isAlive(boolean[][] grid, int row, int col) {
        return grid[row][col];
    }


    public static void main(String[] args) {
        new Life();
    }


    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        System.out.println(event.getX() + "," + event.getY());
        double width = (double)panel.getWidth() / cells[0].length;
        double height = (double)panel.getHeight() / cells.length;

        int column = Math.min(cells[0].length - 1, (int) (event.getX() / width));
        int row = Math.min(cells.length - 1, (int) (event.getY() / height));
        System.out.println(column + "," + row);

        cells[row][column] = !cells[row][column];
        frame.repaint();

    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(step)) {
            step();
        } else if (event.getSource().equals(start)) {
            System.out.println("Start");
            if (!running) {
                running = true;
                Thread t = new Thread(this);
                t.start();
            }
        } else if (event.getSource().equals(pause)) {
            System.out.println("Pause");
            running = false;
            String message = "There have been " + deathCount + " deaths and " + birthCount + " births so far";
            JOptionPane.showMessageDialog(null, message,  "Deaths and births information", JOptionPane.INFORMATION_MESSAGE);

        } else if (event.getSource().equals(reset)) {
            running = false;
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[0].length; j++) {
                    cells[i][j] = false;
                }
            }
            String message = "There have been " + deathCount + " deaths and " + birthCount + " births so far";
            JOptionPane.showMessageDialog(null, message,  "Deaths and births information", JOptionPane.INFORMATION_MESSAGE);
            deathCount = 0;
            birthCount = 0;
            frame.repaint();
        } else if (event.getSource().equals(speed)) {
            downtime = Integer.parseInt(JOptionPane.showInputDialog(frame, "Insert the new desired downtime between generations.  (milliseconds)"));

        }
    }

    private void step() {
        boolean[][] nextCells = new boolean[cells.length][cells[0].length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (isAlive(cells, i, j) && countNeighbors(cells, i, j) < 2) {
                    nextCells[i][j] = false;
                    deathCount++;
                }

                if (isAlive(cells, i, j) && (countNeighbors(cells, i, j) == 2 || countNeighbors(cells, i, j) == 3)) {
                    nextCells[i][j] = true;
                }

                if (isAlive(cells, i, j) && countNeighbors(cells, i, j) > 3) {
                    nextCells[i][j] = false;
                    deathCount++;
                }

                if (!isAlive(cells, i, j) && countNeighbors(cells, i, j) == 3) {
                    nextCells[i][j] = true;
                    birthCount++;
                }
            }
        }

        for (int row = 0; row < nextCells.length; row++) {
            System.arraycopy(nextCells[row], 0, cells[row], 0, nextCells[0].length);
        }
        frame.repaint();
    }

    @Override
    public void run() {
        while (running) {
            step();
            try {
                Thread.sleep(downtime);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
