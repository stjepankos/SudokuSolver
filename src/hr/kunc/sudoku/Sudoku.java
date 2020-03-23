package hr.kunc.sudoku;


public class Sudoku {

    public static int[][] GRID={
            {0,7,0,0,4,9,0,0,0},
            {0,0,0,0,3,7,0,0,8},
            {0,0,9,0,0,0,0,7,0},
            {0,0,0,0,0,0,0,5,0},
            {0,0,0,6,9,1,0,0,0},
            {0,0,6,0,0,0,1,8,0},
            {5,0,4,0,0,0,9,0,0},
            {1,0,0,0,5,0,0,0,0},
            {6,0,0,2,7,0,0,0,3}

    };
    private int[][] board;
    public static final int EMPTY = 0; // Empty cell
    public static final int SIZE =9; // X and Y dimension

    public Sudoku(int[][] board){
        this.board=new int[SIZE][SIZE];
        for (int i=0; i<SIZE;i++){
            for (int j=0;j<SIZE;j++){            //Arraycopy could be used:
                this.board[i][j]=board[i][j];    //System.arraycopy(board[i], 0, this.board[i], 0, SIZE);
            }
        }
    }

    private boolean isInRow(int x,int number){  //Checks if number is already in the row
        for (int i=0;i<SIZE;i++){
            if (board[x][i]==number){
                return true;
            }
        }
        return false;
    }
    private boolean isInCol(int y, int number){  //Checks if number is already in the column
        for (int j=0;j<SIZE;j++){
            if (board[j][y]==number){
                return true;
            }
        }
        return false;
    }
    private boolean isInSquare(int x, int y, int number){ //Checks if number is already in 3x3 box
        int r= x-x%3;
        int c= y-y%3;
        for (int i=r;i<r+3;i++){
            for (int j=c;j<c+3;j++){
                if (board[i][j]==number){
                    return true;
                }
            }
        }
        return false;
    }

    // Checks if everything returns true
    private boolean isPossible(int x, int y, int number){
        return !isInRow(x,number) && !isInCol(y,number) && !isInSquare(x,y,number);
    }

    //Solving method using Backtracking algorithm
    public boolean solve(){
        for (int x=0;x<SIZE;x++){
            for (int y=0;y<SIZE;y++){
                if (board[x][y]==EMPTY) { //finding an empty cell
                    for (int number = 1; number < 10; number++) { //Try all numbers 1-9
                        if (isPossible(x, y, number)) { //Number follows the rules
                            board[x][y] = number;
                            if (solve()) {    //The core of the algorithm, we start backtracking recursively
                                return true;
                            } else {                  //if not a solution, empty the cell and continue
                                board[x][y] = EMPTY;
                            }
                        }
                    }
                    return false;
                }
            }
        }

        return true; //Sudoku is solved
    }
    public void print(){
        for (int i=0;i<SIZE;i++){
            for (int j =0;j<SIZE;j++){
                if (j==2 || j==5){
                    System.out.print(" "+board[i][j]+" |");
                }else {
                    System.out.print(" " + board[i][j]);
                }
            }
            System.out.println();
            if (i==2 || i==5){
                System.out.println("----------------------");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku(GRID);
        System.out.println("Sudoku Grid to solve:"); //Prints original
        sudoku.print();
        if (sudoku.solve()){
            System.out.println("Sudoku is solved:");
            sudoku.print();
        }else{
            System.out.println("The grid is unsolvable");
        }
    }
}
