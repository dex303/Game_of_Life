package life;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameOfLife extends JFrame {
    private static boolean[][] currentState;
    private static final int FRAME_WIDTH = 415;
    private static final int FRAME_HEIGHT = 467;
    private Display display;
    private Banner banner;
    private JLabel genLabel;
    private JLabel aliveLabel;


    public GameOfLife() throws InterruptedException {
        super("Game of Life");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        this.setResizable(false);
        this.setVisible(true);

        JToggleButton pause = new JToggleButton("Pause/resume");
        pause.setBounds(160,2,100,25);
        pause.setName("PlayToggleButton");
        add(pause);
        JButton restart = new JButton("Restart");
        restart.setBounds(280,2,100,25);
        restart.setName("ResetButton");
        add(restart);

        this.initComponents();
        this.game();
    }

    private void initComponents() {
        this.banner = new Banner();
        this.genLabel = new JLabel();
        this.aliveLabel = new JLabel();
        this.display = new Display(null);

        this.genLabel.setName("GenerationLabel");
        this.aliveLabel.setName("AliveLabel");
        this.genLabel.setBounds(5, 2, 100, 10);
        this.aliveLabel.setBounds(5, 17, 100, 10);

        this.banner.add(this.genLabel);
        this.banner.add(this.aliveLabel);
        this.add(this.banner);
        this.add(this.display);
    }

    private void game() throws InterruptedException {
        Generation.generations[0] = new Generation(20);
        Generation currentGen = Generation.generations[0];

        for (int i = 0; i < Generation.GENS; i++) {
            if (i != 0) {
                Generation.generations[i] = currentGen.advance();
                currentGen = Generation.generations[i];
            }
            currentState = currentGen.getVisibleState();
            this.genLabel.setText("Generations #" + i);
            this.aliveLabel.setText("Alive: " + currentGen.getAliveCount());
            this.display.update(currentState);
            this.banner.update();
            this.repaint();
            Thread.sleep(1000L);
        }
    }

    private class Banner extends JPanel {
        Banner() {
            this.setLayout(null);
            this.setSize(400, 30);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.setBackground(Color.GRAY);
        }

        void update() {
            this.repaint();
        }
    }

    private class Display extends JPanel {
        boolean[][] universe;

        Display(boolean[][] universe) {
            this.universe = universe;
            this.setLayout(null);
            this.setSize(400, 400);
            this.setBorder(new EmptyBorder(0, 0, 0, 0));
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            g.setColor(Color.BLACK);


            for (int i = 20; i <= 400; i += 20) {
                g.drawLine(i, 30, i, 430);
            }

            for (int i = 30; i <= 430; i += 20) {
                g.drawLine(0, i, 400, i);
            }

            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 20; j++) {
                    if (currentState[i][j]) {
                        g.fillRect(i * 20, j * 20 + 30, 20, 20);
                    }
                }
            }
        }

        void update(boolean[][] universe) {
            this.universe = universe;
            repaint();
        }
    }
}