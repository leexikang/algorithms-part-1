import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = copyTiles(tiles);
    }
    

    // string representation of this board
    public String toString() {
        int n = dimension();
        StringBuilder s = new StringBuilder();
            s.append(n + "\n");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    s.append(String.format("%2d ", tiles[i][j]));
                }
                s.append("\n");
            }
            return s.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] != (i * tiles[i].length) + j + 1 && tiles[i][j] != 0) {
                    hamming++;
                }
            }
        }

        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }

                int currnetIndex = (i * tiles[i].length) + j + 1;
                if (tiles[i][j] != currnetIndex) {
                    int y = tiles[i][j] / tiles.length;
                    int x = tiles[i][j] % tiles.length;
                    if (x == 0) {
                        x = tiles.length;
                        y = y - 1;
                    }
                    manhattan += Math.abs(i - y) + Math.abs(j+1 - x);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {

                if (i == tiles.length - 1 && j == tiles.length - 1) {
                    continue;
                }

                int currnetIndex = (i * tiles[i].length) + j + 1;
                if (tiles[i][j] != currnetIndex) {
                    return false;
                }
            }
        }

        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }

        if (y.getClass() == this.getClass()) {
            Board that = (Board) y;
            return Arrays.deepEquals(this.tiles, that.tiles);
        }

        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int x = 0;
        int y = 0;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == 0) {
                    y = i;
                    x = j;
                }
            }
        }

        ArrayList<Board> neighbors = new ArrayList<Board>();

        if (x + 1 < tiles.length) {
            neighbors.add(neighborBoard(x, y, x + 1, y));
        }

        if (x - 1 >= 0) {
            neighbors.add(neighborBoard(x, y, x - 1, y));
        }

        if (y + 1 < tiles.length) {
            neighbors.add(neighborBoard(x, y, x, y + 1));
        }

        if (y - 1 >= 0) {
            neighbors.add(neighborBoard(x, y, x, y - 1));
        }

        return neighbors;
    }

    private Board neighborBoard(int blankX, int blankY, int x, int y) {
        int[][] neighborsTiles = copyTiles(this.tiles);
        neighborsTiles[blankY][blankX] = tiles[y][x];
        neighborsTiles[y][x] = this.tiles[blankY][blankX];
        return new Board(neighborsTiles);
    }

    private int[][] copyTiles(int[][] from) {
        int[][] copyTiles = new int[from.length][from.length];
        for (int i = 0; i < from.length; i++) {
            for (int j = 0; j < from[i].length; j++) {
                copyTiles[i][j] = from[i][j];
            }
        }

        return copyTiles;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (this.tiles[i][j] != 0) {
                    if (j + 1 < tiles.length) {
                        if (tiles[i][j + 1] != 0) {
                            return neighborBoard(j, i, j + 1, i);
                        }
                    } else {
                        if (tiles[i + 1][0] != 0) {
                            return neighborBoard(j, i, 0, i + 1);
                        }
                    }
                }
            }
        }
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
                { 1, 0},
                { 2, 3},
        };

        // int[][] tiles2 = {
        // { 1, 2, 3, 4 },
        // { 5, 6, 7, 8 },
        // { 9, 10, 11, 12 },
        // { 13, 14, 15, 0 },
        // };

        Board board = new Board(tiles);
        System.out.println(board.manhattan());
        // for (Board bo : board.neighbors()) {
        // System.out.println(bo.toString());
        // }
    }
}