package service;

import constant.InfoConstant;

public class Chessman {

    private int color;
    private int row;//行
    private int column;//列

    Chessman(int row, int column, int color){
        this.row = row;
        this.column = column;
        this.color = color;
    }

    void changeColor(){
        if (this.color == InfoConstant.BLACK){
            this.color = InfoConstant.WHITE;
        }
        else this.color = InfoConstant.BLACK;
    }

    int getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}
