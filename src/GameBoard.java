import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameBoard extends JPanel implements ActionListener {
    // 定义游戏板的宽度和高度
    private final int BoardWidth = 10;
    private final int BoardHeight = 20;
    private Timer timer;
    private boolean isFallingFinished = false; // 当前方块是否落地
    private boolean isStarted = false; // 游戏是否开始
    private boolean isPaused = false; // 游戏是否暂停
    private int numLinesRemoved = 0; // 移除的行数
    private int curX = 0; // 当前方块的X位置
    private int curY = 0; // 当前方块的Y位置
    private Shape curPiece; // 当前的方块形状
    private Shape.Tetrominoes[] board; // 游戏板的格子，存储每个格子的方块形状

    public GameBoard(Tetris parent) {
        setFocusable(true);
        curPiece = new Shape();
        timer = new Timer(400, this);
        timer.start();

        board = new Shape.Tetrominoes[BoardWidth * BoardHeight];
        clearBoard();
        addKeyListener(new TAdapter());
    }

    private void initBoard() {
        setFocusable(true); // 设置为焦点，以便可以接收键盘事件
        curPiece = new Shape();
        timer = new Timer(400, this); // 设置定时器，每400ms触发一次动作事件
        board = new Shape.Tetrominoes[BoardWidth * BoardHeight]; // 初始化游戏板数组
        clearBoard(); // 清空游戏板
        addKeyListener(new TAdapter()); // 添加键盘监听器
    }

    // 清空游戏板
    private void clearBoard() {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i) {
            board[i] = Shape.Tetrominoes.NoShape;
        }
    }

    // 在指定位置放置方块形状
    private void placeInBoard(int x, int y, Shape.Tetrominoes shape) {
        // 此处省略具体实现...
    }

    // 方块下落一格的操作
    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    // 方块落地后的处理
    private void pieceDropped() {
        // 此处省略具体实现...
    }

    // 尝试移动当前方块到新位置，如果不能移动则返回false
    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) {
                return false;
            }
            if (shapeAt(x, y) != Shape.Tetrominoes.NoShape) {
                return false;
            }
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    // 开始游戏
    public void start() {
        if (isPaused) return;

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();

        newPiece();
        timer.start();
    }

    // 暂停游戏
    private void pause() {
        // 此处省略具体实现...
    }

    // 键盘事件处理
    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!isStarted || curPiece.getShape() == Shape.Tetrominoes.NoShape) {
                return;
            }

            int keycode = e.getKeyCode();

            // 此处省略按键处理逻辑...
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    // 创建新方块
    private void newPiece() {
        // 此处省略具体实现...
    }

    // 绘制游戏界面
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
    }

    // 绘制游戏板
    private void drawBoard(Graphics g) {
        int squareWidth = getSize().width / BoardWidth;
        int squareHeight = getSize().height / BoardHeight;

        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                Shape.Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
                if (shape != Shape.Tetrominoes.NoShape) {
                    drawSquare(g, j * squareWidth, i * squareHeight, shape);
                }
            }
        }

        if (curPiece.getShape() != Shape.Tetrominoes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, x * squareWidth, (BoardHeight - y - 1) * squareHeight, curPiece.getShape());
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y, Shape.Tetrominoes shape) {
        Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102),
                new Color(102, 204, 102), new Color(102, 102, 204),
                new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0) };
        var color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth - 2, squareHeight - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight - 1, x, y);
        g.drawLine(x, y, x + squareWidth - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight - 1, x + squareWidth - 1, y + squareHeight - 1);
        g.drawLine(x + squareWidth - 1, y + squareHeight - 1, x + squareWidth - 1, y + 1);
    }

    // 其他方法，如绘图、行移除、新形状的创建等
}
