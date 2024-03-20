public class Shape {
    // 定义形状的枚举，包括无形状和七种标准形状
    enum Tetrominoes { NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape }

    private Tetrominoes pieceShape;
    private int[][] coords; // 存储当前形状的坐标

    public Shape() {
        coords = new int[4][2]; // 每个形状由4个方块组成，每个方块有x和y坐标
        setShape(Tetrominoes.NoShape);
    }

    // 设置形状
    public void setShape(Tetrominoes shape) {
        // 每种形状方块的相对坐标
        int[][][] coordsTable = new int[][][] {
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}}
        };

        for (int i = 0; i < 4 ; i++) {
            System.arraycopy(coordsTable[shape.ordinal()], 0, coords, 0, 4);
        }
        pieceShape = shape;
    }

    // 旋转形状的方法
    public Shape rotateLeft() {
        if (pieceShape == Tetrominoes.SquareShape)
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, getY(i));
            result.setY(i, -getX(i));
        }
        return result;
    }

    // 旋转右边的方法
    public Shape rotateRight() {
        if (pieceShape == Tetrominoes.SquareShape)
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, -getY(i));
            result.setY(i, getX(i));
        }
        return result;
    }

    // 设置x坐标
    private void setX(int index, int x) { coords[index][0] = x; }
    // 设置y坐标
    private void setY(int index, int y) { coords[index][1] = y; }
    // 获取x坐标
    public int getX(int index) { return coords[index][0]; }
    // 获取y坐标
    public int getY(int index) { return coords[index][1]; }
    // 获取形状
    public Tetrominoes getShape() { return pieceShape; }

    // 省略了形状随机生成等方法...
}