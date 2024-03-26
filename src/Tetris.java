import javax.swing.*;

// Tetris 类表示 Tetris 游戏的主窗口
public class Tetris extends JFrame {

    private final GameBoard board; // 游戏板对象

    // Tetris 类的构造函数，用于初始化 Tetris 游戏窗口
    public Tetris() {
        board = new GameBoard(); // 创建游戏板对象
        add(board); // 将游戏板添加到窗口中
        setTitle("俄罗斯方块"); // 设置窗口标题
        setSize(400, 800); // 设置窗口大小
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 设置关闭操作：退出应用程序
        setLocationRelativeTo(null); // 将窗口置于屏幕中央
    }

    // 启动游戏的方法
    public void startGame() {
        board.start(); // 调用游戏板的 start 方法启动游戏
        setVisible(true); // 显示游戏窗口
    }
}