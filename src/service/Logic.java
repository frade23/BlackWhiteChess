package service;

import constant.FileConstant;
import constant.InfoConstant;
import util.FileUtil;
import util.Util;

import java.text.MessageFormat;
import java.util.Scanner;


public class Logic {

    private Player humanPlayer = new Player();
    private Player computerPlayer = new Player();
    private boolean isGaveUp;
    private long gameTime;
    private ChessBoard chessBoard;

    public void run(){
        int capacity = getCapacity();
        chessBoard = new ChessBoard(capacity);
        int color = setColor();
        computerPlayer.init(chessBoard);
        humanPlayer.init(chessBoard);
        computerPlayer.setColor(color);
        humanPlayer.setColor(Util.getAnotherColor(color));
        if (color == InfoConstant.BLACK){
            computerPlayer.autoPut(humanPlayer);
            printComputerPut();
            printChessBoard();
        }else {
            printChessBoard();
        }

    }

    private int getCapacity(){
        Scanner input = new Scanner(System.in);
        String capacity = "3";
        while (!(!"".equals(capacity) &&Integer.parseInt(capacity) >= 4 && Integer.parseInt(capacity) <= 26 && Integer.parseInt(capacity)%2 == 0
        && Util.isNumeric(capacity))){
            System.out.print(InfoConstant.CAPACITY);
            capacity = input.nextLine();
        }
        return Integer.parseInt(capacity);
    }

    //设置玩家棋子颜色
    private int setColor(){
        Scanner scanner = new Scanner(System.in);
        String color = "a";
        while (color.toUpperCase().charAt(0) != 'X' && color.toUpperCase().charAt(0) != 'O'){
            System.out.print(InfoConstant.ENTER_COLOR);
            color = scanner.nextLine();
        }
        return color.toUpperCase().charAt(0) == 'X' ? InfoConstant.BLACK: InfoConstant.WHITE;
    }

    private void printComputerPut(){
        int color = computerPlayer.getColor();
        Chessman chessman = computerPlayer.getLastChessman();
        System.out.println(MessageFormat.format(InfoConstant.COMPUTER_PUT, Util.color2String(color), Util.chessman2String(chessman)));
    }

    private void printChessBoard(){
        String icons = InfoConstant.ICONS;
        Chessman[][] chessmen = chessBoard.getChessmen();
        int capacity = chessmen.length;
//        System.out.print(InfoConstant.STRING_WHITE_BLACK);
        for (int i = 0; i < capacity; i++){
            System.out.print( " " + icons.charAt(i));
        }
        System.out.println();
        for (int i = 0; i < capacity; i++){
            System.out.print(icons.charAt(i));
            for (int j = 0; j < capacity; j++){
                if (chessmen[i][j] == null){
                    System.out.print(InfoConstant.STRING_POINT);
                }else if (chessmen[i][j].getColor() == InfoConstant.BLACK){
                    System.out.print(InfoConstant.STRING_BLACK);
                }else System.out.print(InfoConstant.STRING_WHITE);
            }
            System.out.println();
        }
    }

    public void getOn(){
        gameTime = System.currentTimeMillis();
        while (!isOver()){
            if (!humanPlayerPut()){
                gameTime = System.currentTimeMillis() - gameTime;
                isGaveUp = true;
                return;
            }
            if (isOver())
                break;
            computerPut();
        }
        if((chessBoard.isOver(InfoConstant.BLACK) && chessBoard.isOver(InfoConstant.WHITE)) && !chessBoard.isFull()) {
            System.out.println(InfoConstant.BOTH_INVALID);
        }
        System.out.println(InfoConstant.GG);
        System.out.println("● : ○ = " + getScore());
        System.out.println(gameOver());
        gameTime = System.currentTimeMillis() - gameTime;
    }

    private String gameOver(){
        if(oWin()){
            return InfoConstant.OWIN;
        }else if(isEquals()){
            return InfoConstant.IS_DRAW;
        }else {
            return InfoConstant.XWIN;
        }
    }

    private boolean isEquals() {
        return humanPlayer.getChessmanNumber() == computerPlayer.getChessmanNumber();
    }

    private boolean oWin() {
        return humanPlayer.getChessmanNumber() > computerPlayer.getChessmanNumber() && humanPlayer.getColor() == InfoConstant.WHITE;
    }

    private void computerPut() {
        if (computerPlayer.canPut()){
            computerPlayer.autoPut(humanPlayer);
            printComputerPut();
            printChessBoard();
        }else {
            printInvalid(computerPlayer.getColor());
        }
    }

    private boolean isOver(){//判断游戏是否结束
        return chessBoard.isFull() || (chessBoard.isOver(InfoConstant.WHITE) && chessBoard.isOver(InfoConstant.BLACK));
    }

    private boolean humanPlayerPut(){
        if (humanPlayer.canPut()){
            if (humanPlayer.put(getChessman(), computerPlayer)){
                printChessBoard();
            }else {
                System.out.println(InfoConstant.INVALID_PUT);
                System.out.println(InfoConstant.GG);
                if (humanPlayer.getColor() == InfoConstant.BLACK){
                    System.out.println(InfoConstant.OWIN);
                }else {
                    System.out.println(InfoConstant.XWIN);
                }
                return false;
            }
        }
        else {
            printInvalid(humanPlayer.getColor());
        }
        return true;
    }

    //打印无效下子
    private void printInvalid(int color){
        System.out.print(MessageFormat.format(InfoConstant.HAS_INVALID_PUT, Util.color2String(color)));
    }

    private Chessman getChessman(){
        int color = humanPlayer.getColor();
        int capacity = chessBoard.getCapacity();
        Scanner input = new Scanner(System.in);
        String coordinate = "a1";
        while(!(coordinate.length() >= 2 && coordinate.charAt(0) >= 'a' && coordinate.charAt(0) <= 'a'+capacity-1 && coordinate.charAt(1) >= 'a' && coordinate.charAt(1) <= 'a' + capacity - 1)){
            System.out.print(MessageFormat.format(InfoConstant.PLAYER_ENTER,Util.color2String(color)));
            coordinate = input.nextLine().trim().toLowerCase();
        }
        return new Chessman(coordinate.charAt(0) - 'a', coordinate.charAt(1) - 'a', color);
    }

    public void over(){
        FileUtil.write(getData(), FileConstant.FILE_PATH);
    }

    private String[] getData(){
        String[] data = new String[6];
        data[0] = Util.getTime();
        data[1] = "" + (getGameTime() / 1000);
        data[2] = "" + chessBoard.getCapacity() + "*" + chessBoard.getCapacity();
        data[3] = computerPlayer.getColor() == InfoConstant.WHITE?"Human":"Computer";
        data[4] = computerPlayer.getColor() == InfoConstant.BLACK?"Human":"Computer";
        data[5] = getScore();
        return data;
    }

    private String getScore() {
        String score;
        int computerScore = computerPlayer.getChessmanNumber();
        int humanScore = humanPlayer.getChessmanNumber();
        if (computerPlayer.getColor() == InfoConstant.BLACK){
            score = computerScore + " : " + humanScore;
        }else score = humanScore + " : " + computerScore;
        if (isGaveUp){
            score = InfoConstant.HUMAN_GAVE_UP;
        }
        return score;
    }


    private long getGameTime() {
        return gameTime;
    }

}
