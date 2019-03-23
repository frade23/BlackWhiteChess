package service;

import constant.InfoConstant;
import util.Util;

import java.util.ArrayList;

class ChessBoard {

    private int capacity;
    private Chessman[][] chessmen;

    ChessBoard(int size){
        this.capacity = size;
        chessmen = new Chessman[size][size];
        addChessman(new Chessman(size/2, size/2, InfoConstant.WHITE));
        addChessman(new Chessman(size/2 - 1, size/2 - 1, InfoConstant.WHITE));
        addChessman(new Chessman(size/2 - 1, size/2, InfoConstant.BLACK));
        addChessman(new Chessman(size/2, size/2 - 1, InfoConstant.BLACK));
    }

    private void addChessman(Chessman chessman){
        int row = chessman.getRow();
        int col = chessman.getColumn();
        chessmen[row][col] = chessman;
    }

    //当前颜色棋子的可选位置
    //
    ArrayList<Chessman> getOptions(int color){
        ArrayList<Chessman> optionChessmen = new ArrayList<>();
        for (int i = 0; i < capacity; i++){
            for (int j = 0; j < capacity; j++){
                if (checkWeight(i, j, color, false) > 0){
                    optionChessmen.add(new Chessman(i, j, color));
                }
            }
        }
        return optionChessmen;
    }

    //检查某个位置的权值
    private int checkWeight(int row, int col, int color, boolean isPut){
        if (chessmen[row][col] != null){
            return 0;
        }
        int weight = 0;
        int[][] pointer = {{0,1},{0,-1},{1,0},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
        int index;
        for (int i = 0; i < 8; i++){
            index = 1;
            while (row + (index + 1) * pointer[i][0] >= 0 && row + (index + 1) * pointer[i][0] < capacity
                    && col + (index + 1) * pointer[i][1] >= 0 && col + (index + 1) * pointer[i][1] < capacity
                    && chessmen[row + index * pointer[i][0]][col + index * pointer[i][1]] != null
                    && chessmen[row + (index + 1) * pointer[i][0]][col + (index + 1) * pointer[i][1]] != null
                    && chessmen[row + index * pointer[i][0]][col + index * pointer[i][1]].getColor() == Util.getAnotherColor(color)){
                if (chessmen[row + (1+index) * pointer[i][0]][col + (index+1) * pointer[i][1]].getColor() == color && !isPut){
                    weight += index;
                    break;
                }else if (chessmen[row + (index+1) * pointer[i][0]][col + (index+1) * pointer[i][1]].getColor() == color){
                    weight += index;
                    for (int j = 1; j <= index; j++){
                        chessmen[row + j * pointer[i][0]][col + j * pointer[i][1]].changeColor();
                    }
                    break;
                }
                index++;
            }
        }
        return weight;
    }

    int checkWeight(Chessman chessman, int color){
        return checkWeight(chessman.getRow(), chessman.getColumn(), color, false);
    }

    //判断棋盘是否下满
    boolean isFull(){
        for (int i = 0; i < capacity; i++){
            for (int j = 0; j < capacity; j++){
                if (chessmen[i][j] == null)
                    return false;
            }
        }
        return true;
    }

     int put(int row, int col, int color){
        int number = checkWeight(row, col, color, true);
        addChessman(new Chessman(row, col, color));
        return number;
    }

    void put(Chessman chessman, int color){
        put(chessman.getRow(), chessman.getColumn(), color);
    }

    //判断是否有空位下子
    boolean isOver(int color){
        return getOptions(color).size() == 0;
    }

    Chessman[][] getChessmen() {
        return chessmen;
    }

    int getCapacity() {
        return capacity;
    }

}
