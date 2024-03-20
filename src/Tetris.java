import javax.swing.*;

public class Tetris extends JFrame {

    private final GameBoard board;

    public Tetris() {
        board = new GameBoard();
        add(board);
        setTitle("Tetris Game");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void startGame() {
        board.start();
        setVisible(true);
    }
}