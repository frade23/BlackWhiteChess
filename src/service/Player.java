package service;

import java.util.ArrayList;

class Player {

    private ChessBoard chessBoard;
    private Chessman lastChessman;
    private int chessmanNumber;//分数
    private int color;

    Player(){
        this.chessmanNumber = 2;
    }

    void init(ChessBoard chessBoard){
        this.chessBoard = chessBoard;
    }

    private boolean put(int row, int col,Player player){
        int playerScore = chessBoard.put(row,col,color);
        this.chessmanNumber += playerScore + (playerScore > 0? 1 : 0);
        player.decreaseScore(playerScore);
        return playerScore > 0;
    }

    boolean put(Chessman chessman, Player player){
        return put(chessman.getRow(), chessman.getColumn(), player);
    }

    //电脑下子
    void autoPut(Player player){
        ArrayList<Chessman> optionChessmen = chessBoard.getOptions(color);
        if (optionChessmen.size() == 0)
            return;
        int index = 0;
        int chessmanNumber = 0;
        int playerNumber;
        for(int i = 0; i < optionChessmen.size(); i++){
            if((playerNumber = chessBoard.checkWeight(optionChessmen.get(i),color)) > chessmanNumber){
                index = i;
                chessmanNumber = playerNumber;
            }
        }
        player.decreaseScore(chessmanNumber);
        this.chessmanNumber +=chessmanNumber + 1;
        lastChessman = optionChessmen.get(index);
        chessBoard.put(lastChessman, color);
    }

    boolean canPut(){
        return !chessBoard.isOver(color);
    }

    private void decreaseScore(int count) {
        chessmanNumber -= count;
    }

    Chessman getLastChessman() {
        return lastChessman;
    }

    int getChessmanNumber() {
        return chessmanNumber;
    }

    int getColor() {
        return color;
    }

    void setColor(int color) {
        this.color = color;
    }
}
