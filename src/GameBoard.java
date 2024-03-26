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
    private int squareWidth;
    private int squareHeight;

    public GameBoard() {
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
        board[(y * BoardWidth) + x] = shape;
    }

    // 方块下落一格的操作
    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    // 方块落地后的处理
    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            placeInBoard(x, y, curPiece.getShape());
        }

        removeFullLines();

        if (!isFallingFinished) {
            newPiece();
        }
    }

    // 从游戏板中移除满行
    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = BoardHeight - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < BoardWidth; j++) {
                if (shapeAt(j, i) == Shape.Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                numFullLines++;
                // 移除满行，并将上方行下移
                for (int k = i; k < BoardHeight - 1; k++) {
                    for (int j = 0; j < BoardWidth; j++) {
                        board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                    }
                }
                // 顶部添加新的空行
                for (int j = 0; j < BoardWidth; j++) {
                    board[(BoardHeight - 1) * BoardWidth + j] = Shape.Tetrominoes.NoShape;
                }
            }
        }
        // 更新移除的行数
        numLinesRemoved += numFullLines;
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
        // 游戏是否暂停
        if (isPaused) return;

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();

        timer.start();
        newPiece();
        initBoard(); // 在开始游戏时初始化游戏板
    }

    // 暂停游戏
    private void pause() {
        if (isStarted) {
            isPaused = !isPaused;
            if (isPaused) {
                timer.stop(); // 暂停计时器
                // 可以添加其他暂停游戏时需要执行的逻辑
            } else {
                timer.start(); // 恢复计时器
                // 可以添加其他恢复游戏时需要执行的逻辑
            }
            repaint(); // 重新绘制游戏界面
        }
    }

    // 继续游戏的方法
    private void resume() {
        if (isPaused) {
            isPaused = false; // 设置游戏为非暂停状态
            timer.start(); // 启动计时器，继续游戏
            // 其他恢复游戏的逻辑...
        }
    }

    // 键盘事件处理
    class TAdapter extends KeyAdapter {
        Shape shape = new Shape();

        @Override
        public void keyPressed(KeyEvent e) {
            if (!isStarted || curPiece.getShape() == Shape.Tetrominoes.NoShape) {
                return;
            }

            int keycode = e.getKeyCode();
            if (keycode == KeyEvent.VK_P) { // 如果按下的是P键
                pause(); // 调用暂停游戏的方法
            }else if (keycode == KeyEvent.VK_R) { // 继续游戏按键
                resume(); // 调用继续游戏的方法
            }else if (keycode == KeyEvent.VK_UP) { // 如果按下的是上箭头键
                shape.rotateRight(); // 调用旋转方块的方法
            }else if (keycode == KeyEvent.VK_DOWN){// 如果按下的是下箭头键
                shape.rotateLeft();
            }
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
        // 从可用的方块形状中选择一个作为下一个要出现的方块
        // 这里可以根据游戏规则或算法来选择方块形状
        Shape.Tetrominoes nextShape = Shape.getRandomShape().getShape();

        // 设置当前方块形状为选择的方块形状
        curPiece.setShape(nextShape);

        // 设置方块的初始位置
        // 这里可以根据游戏设计将方块放置在游戏板的合适位置
        curX = BoardWidth / 2; // 设置为游戏板的中间位置
        curY = BoardHeight - 1; // 设置为游戏板的底部位置

        // 检查游戏结束条件
        // 在生成新方块之后，通常需要检查是否有方块堆积到顶部，导致游戏结束
        if (isCollision(curPiece, curX, curY)) {
            gameOver();
        }
    }

    // 检查当前方块是否与游戏板中的其他方块发生碰撞
    private boolean isCollision(Shape shape, int x, int y) {
        for (int i = 0; i < 4; i++) {
            int row = y + shape.y(i);
            int col = x + shape.x(i);

            // 检查是否超出游戏板边界
            if (row < 0 || row >= BoardHeight || col < 0 || col >= BoardWidth) {
                return true; // 发生碰撞
            }

            // 检查当前位置是否已经有方块
            if (board[row * BoardWidth + col] != Shape.Tetrominoes.NoShape) {
                return true; // 发生碰撞
            }
        }
        return false; // 未发生碰撞
    }

    // 检查游戏是否结束
    private void gameOver() {
        // 如果新生成的方块无法移动到游戏板的顶部，则游戏结束
        // 如果新生成的方块无法移动到游戏板的顶部，则游戏结束
        if (isCollision(curPiece, curX, curY)) {
            // 游戏结束时的处理逻辑，例如显示游戏结束界面、停止计时器等
            // 这里只是示例，具体逻辑可以根据需要进行修改
            JOptionPane.showMessageDialog(this,
                    "游戏结束！", "消息",
                    JOptionPane.INFORMATION_MESSAGE);
            timer.stop(); // 停止计时器
        }
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
        Color[] colors = { new Color(0, 0, 0), new Color(204, 102, 102),
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
    private Shape.Tetrominoes shapeAt(int x, int y) {
        return board[(y * BoardWidth) + x];
    }
}
