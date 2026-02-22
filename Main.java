import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
public class Main extends JPanel implements ActionListener, KeyListener {
    private final int TILE_SIZE = 25;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private ArrayList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = true;
    private Timer timer;
    private Random random;
    public Main() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        random = new Random();
        initGame();
        timer = new Timer(120, this);
        timer.start();
    }
    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));
        direction = 'R';
        running = true;
        spawnFood();
    }
    private void spawnFood() {
        int x = random.nextInt(WIDTH / TILE_SIZE);
        int y = random.nextInt(HEIGHT / TILE_SIZE);
        food = new Point(x, y);
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < WIDTH / TILE_SIZE; i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, HEIGHT);
            g.drawLine(0, i * TILE_SIZE, WIDTH, i * TILE_SIZE);
        }
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + (snake.size() - 3), 10, 25);
    }
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            repaint();
        }
    }
    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);
        switch (direction) {
            case 'U': newHead.y--; break;
            case 'D': newHead.y++; break;
            case 'L': newHead.x--; break;
            case 'R': newHead.x++; break;
        }
        snake.add(0, newHead);
        if (newHead.equals(food)) {
            spawnFood();
            // Increase speed
            int delay = Math.max(50, timer.getDelay() - 5);
            timer.setDelay(delay);
        } else {
            snake.remove(snake.size() - 1);
        }
    }
    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.y < 0 ||
                head.x >= WIDTH / TILE_SIZE ||
                head.y >= HEIGHT / TILE_SIZE) {
            gameOver();
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
            }
        }
    }
    private void gameOver() {
        running = false;
        timer.stop();
        int option = JOptionPane.showConfirmDialog(
                this,
                "Game Over!\nScore: " + (snake.size() - 3) + "\nPlay Again?",
                "Snake Game",
                JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            initGame();
            timer.setDelay(120);
            timer.start();
            requestFocusInWindow();
        } else {
            System.exit(0);
        }
    }
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
        }
    }
     public void keyReleased(KeyEvent e) {}
     public void keyTyped(KeyEvent e) {}
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        Main game = new Main();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.requestFocusInWindow();
    }
}
