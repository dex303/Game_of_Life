package life;

import java.util.Random;

class Generation {
    protected static final int GENS = 10;
    protected static Generation[] generations = new Generation[GENS];
    protected static int age;
    private static final Random random = new Random();

    private final boolean[][] state;
    private final boolean[][] visibleState;
    private final int size;
    private final int visibleSize;

    protected Generation(int s) {
        age = 1;
        this.visibleSize = s;
        this.size = this.visibleSize + 2;
        this.state = new boolean[this.size][this.size];
        this.visibleState = new boolean[this.visibleSize][this.visibleSize];

        initVisibleBoard();
        extendVisibleBoard();
    }

    private Generation(Generation prev) {
        age++;
        this.size = prev.size;
        this.visibleSize = prev.visibleSize;
        this.state = new boolean[this.size][this.size];
        this.visibleState = new boolean[this.visibleSize][this.visibleSize];
    }

    private void initVisibleBoard() {
        for (int y = 0; y < visibleSize; y++)
            for (int x = 0; x < visibleSize; x++)
                visibleState[x][y] = random.nextBoolean();
    }

    private void extendVisibleBoard() {
        for (int y = 0; y < visibleSize; y++)
            for (int x = 0; x < visibleSize; x++)
                state[x + 1][y + 1] = visibleState[x][y];

        state[0][0] = visibleState[visibleSize - 1][visibleSize - 1];
        state[0][size - 1] = visibleState[visibleSize - 1][0];
        state[size - 1][0] = visibleState[0][visibleSize - 1];
        state[size - 1][size - 1] = visibleState[0][0];
        for (int i = 1; i <= visibleSize; i++) {
            state[0][i] = visibleState[visibleSize - 1][i - 1];
            state[i][0] = visibleState[i - 1][visibleSize - 1];
            state[size - 1][i] = visibleState[0][i - 1];
            state[i][size - 1] = visibleState[i - 1][0];
        }
    }

    protected Generation advance() {
        Generation nextGen = new Generation(this);
        for (int y = 1; y <= this.visibleSize; y++) {
            for (int x = 1; x <= this.visibleSize; x++) {
                int neighbors = this.state[x][y] ? -1 : 0;
                for (int j = y - 1; j <= y + 1; j++) {
                    for (int i = x - 1; i <= x + 1; i++) {
                        if (this.state[i][j]) {
                            neighbors++;
                        }
                    }
                }

                if(this.state[x][y] && (neighbors < 2 || neighbors > 3)) {
                    nextGen.visibleState[x - 1][y - 1] = false;
                } else if (!this.state[x][y] && neighbors == 3) {
                    nextGen.visibleState[x - 1][y - 1] = true;
                } else {
                    nextGen.visibleState[x - 1][y - 1] = this.state[x][y];
                }
            }
        }
        nextGen.extendVisibleBoard();
        return nextGen;
    }

    protected boolean[][] getVisibleState() {
        return this.visibleState;
    }

    protected int getAliveCount() {
        int count = 0;
        for (int y = 0; y < visibleSize; y++)
            for (int x = 0; x < visibleSize; x++)
                if (visibleState[x][y]) count++;
        return count;
    }

}