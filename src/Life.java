import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Life implements MouseListener, ActionListener, Runnable {
    static final int rows = 50;
    static final int cols = 50;
    boolean[][] cells = new boolean[rows][cols];

    JFrame frame = new JFrame("Life");
    LifePanel panel = new LifePanel(cells);
    JButton start = new JButton("Start");
    JButton pause = new JButton("Pause");
    JButton step = new JButton("Step");
    JButton reset = new JButton("Reset");
    Container south = new Container();
    boolean running = false;

    public Life() {
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        panel.addMouseListener(this);

        //South Container
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

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    static int[][] make2dgrid(int rows, int cols) {
        int[][] arr = new int[rows][cols];
        return arr;
    }

    static void fillGrid(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = ThreadLocalRandom.current().nextInt(0, 2);
            }
        }

    }

    static int countNeighbors(boolean[][] grid, int row, int col) {
        int total = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            if (i >= 0 && i < grid.length)
                for (int j = col - 1; j <= col + 1; j++)
                    if (j >= 0 && j < grid[i].length)
                        total += (grid[i][j] && !(i == row && j == col) ? 1 : 0);
        }
        return total;
    }

    static boolean isAlive(boolean[][] grid, int row, int col) {
        return grid[row][col];
    }


    public static void main(String[] args) throws InterruptedException {

        new Life();

        final int[][] grid = make2dgrid(rows, cols);

        fillGrid(grid);

        int count = 0;

        /*do {

            System.out.println(Arrays.deepToString(grid).replace("], ", "]\n"));

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (isAlive(grid, i, j) && countNeighbors(grid, i, j) < 2) {
                        grid[i][j] = 0;
                    } else if (isAlive(grid, i, j) && countNeighbors(grid, i, j) > 3) {
                        grid[i][j] = 0;
                    } else if (!isAlive(grid, i, j) && countNeighbors(grid, i, j) == 3) {
                        grid[i][j] = 1;
                    }
                }
            }

            Thread.sleep(1000);

            count++;

        } while (count < 10);*/

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
        } else if (event.getSource().equals(reset)) {
            running = false;
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[0].length; j++) {
                    cells[i][j] = false;
                }
            }
            frame.repaint();
        }
    }

    public void step() {
        boolean nextCells[][] = new boolean[cells.length][cells[0].length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (isAlive(cells, i, j) && countNeighbors(cells, i, j) < 2) {
                    nextCells[i][j] = false;
                    /*continue*/;
                }

                if (isAlive(cells, i, j) && (countNeighbors(cells, i, j) == 2 || countNeighbors(cells, i, j) == 3)) {
                    nextCells[i][j] = true;
                    /*continue;*/
                }

                if (isAlive(cells, i, j) && countNeighbors(cells, i, j) > 3) {
                    nextCells[i][j] = false;
                    /*continue;*/
                }

                if (!isAlive(cells, i, j) && countNeighbors(cells, i, j) == 3) {
                    nextCells[i][j] = true;
                    /*continue;*/
                }
                /*cells[i][j] = nextCells[i][j];
                frame.repaint();*/
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
                Thread.sleep(500);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
