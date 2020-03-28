package hr.kunc.sudoku.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JPanel implements KeyListener, MouseListener {
    /**
     * Created by kunc on Mar, 2020
     */
    JFrame window;
    int sizeW, sizeH;
    Sudoku sudoku;
    int[][] grid = {
            {0, 7, 0, 0, 4, 9, 0, 0, 0},
            {0, 0, 0, 0, 3, 7, 0, 0, 8},
            {0, 0, 9, 0, 0, 0, 0, 7, 0},
            {0, 0, 0, 0, 0, 0, 0, 5, 0},
            {0, 0, 0, 6, 9, 1, 0, 0, 0},
            {0, 0, 6, 0, 0, 0, 1, 8, 0},
            {5, 0, 4, 0, 0, 0, 9, 0, 0},
            {1, 0, 0, 0, 5, 0, 0, 0, 0},
            {6, 0, 0, 2, 7, 0, 0, 0, 3}
    };
    int[][] gridCurrent;
    int[][] gridSolved;
    int mouseBoxLX, mouseBoxLY;
    int boxLX, boxLY, iX, jY, x, y;
    boolean isSelected, Running, Solved;
    char key;


    Font numbers;
    String stringNumber;

    public GUI() {

        addMouseListener(this);
        addKeyListener(this);
        setLayout(null);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        gridSolved = new int[9][9];
        gridCurrent = new int[9][9];

        this.sudoku = new Sudoku(this, grid);
        sudoku.addSolved();
        copyGrid(grid, gridCurrent);
        numbers = new Font("arial", Font.BOLD, 40);

        window = new JFrame();
        window.setContentPane(this);
        window.setTitle("Sudoku Solver");
        window.getContentPane().setPreferredSize(new Dimension(900, 900));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        this.sizeW = window.getContentPane().getWidth() / 9;
        this.sizeH = window.getContentPane().getHeight() / 9;
        this.boxLX = 0;
        this.boxLY = 0;
        this.isSelected = false;
        this.Running = false;
        this.Solved = false;
        this.iX = 0;
        this.jY = 0;
        this.key = '0';
        this.x = 0;
        this.y = 0;

    }

    public static void main(String[] args) {
        new GUI();
    }

    public void paintComponent(Graphics g) {
        sizeW = window.getContentPane().getWidth() / 9;
        sizeH = window.getContentPane().getHeight() / 9;
        FontMetrics metrics = g.getFontMetrics(numbers);

        super.paintComponent(g);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int currentX = i * sizeW;
                int currentY = j * sizeH;
                stringNumber = Integer.toString(gridCurrent[j][i]);
                g.setColor(Color.lightGray);
                g.fillRect(currentX, currentY, sizeW, sizeH);
                g.setColor(Color.BLACK);
                g.drawRect(currentX, currentY, sizeW, sizeH);
                if (gridCurrent[j][i] != 0) {
                    g.setFont(numbers);
                    g.drawString(stringNumber, currentX + (sizeW - metrics.stringWidth(stringNumber)) / 2,
                            currentY + (sizeH - metrics.getHeight()) / 2 + metrics.getAscent());
                }
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        for (int i = 0; i <= 9; i += 3) {
            g2.drawLine(i * sizeW, 0, i * sizeW, window.getContentPane().getHeight());
            g2.drawLine(0, i * sizeH, window.getContentPane().getWidth(), i * sizeH);
        }
        if (isSelected) {
            g2.setColor(Color.BLUE);
            g2.drawRect(boxLX + 2, boxLY + 2, sizeW - 4, sizeH - 4);

        }
        if (Running) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (gridCurrent[i][j] != grid[i][j]) {
                        g2.setColor(Color.GREEN);
                        g2.drawRect(i * sizeW + 2, j * sizeH + 2, sizeW - 4, sizeH - 4);
                    } else {
                        g2.setColor(Color.RED);
                        g2.drawRect(i * sizeW + 2, j * sizeH + 2, sizeW - 4, sizeH - 4);
                    }
                }
            }
        }
    }


    public void copyGrid(int[][] source, int[][] dest) {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(source[i], 0, dest[i], 0, 9);
        }
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        key = keyEvent.getKeyChar();
        if (Character.isDigit(key) && key != '0' && !Running) {
            int keyNumber = Character.getNumericValue(key);
            if (keyNumber == gridSolved[jY][iX] && isSelected) {
                gridCurrent[jY][iX] = keyNumber;
                repaint();
            } else {
                System.out.println("Wrong,try again");
            }
        } else if (key == keyEvent.VK_SPACE) {
            System.out.println("Start");
            startSolving();
        }else if (key=='r'){
            reset();
        }
    }

    public void startSolving() {
        copyGrid(grid, gridCurrent);
        sudoku.resetBoard();
        Running = true;
        solve();
        repaint();


    }
    public void reset(){
        copyGrid(grid, gridCurrent);
        repaint();
    }


    public boolean solve() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (gridCurrent[x][y] == 0) {                         //finding an empty cell
                    for (int number = 1; number < 10; number++) { //Try all numbers 1-9
                        if (sudoku.isPossible(x, y, number, gridCurrent)) {           //Number follows the rules
                            gridCurrent[x][y] = number;
                            if (solve()) {
                                return true;
                            } else {                             //if not a solution, empty the cell and continue
                                gridCurrent[x][y] = 0;
                            }
                        }
                    }
                    Running = false;
                    return false;
                }
            }
        }
        Running = false;
        return true;

    }



    @Override
    public void keyReleased(KeyEvent keyEvent) {
        key = '0';
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            mouseBoxLX = e.getX() - (e.getX() % sizeW);
            mouseBoxLY = e.getY() - (e.getY() % sizeH);
            iX = mouseBoxLX / sizeW;
            jY = mouseBoxLY / sizeH;
            System.out.println(mouseBoxLX + ", " + mouseBoxLY);
            if (gridCurrent[jY][iX] == 0) {
                boxLX = mouseBoxLX;
                boxLY = mouseBoxLY;
                isSelected = true;
                repaint();
            }
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


