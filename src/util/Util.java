package util;

import constant.InfoConstant;
import service.Chessman;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Util {
    public static int getAnotherColor(int color) {
        return color == InfoConstant.BLACK? InfoConstant.WHITE : InfoConstant.BLACK;
    }

    public static boolean isNumeric(String string){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    public static String getTime(){
        long current = System.currentTimeMillis();
        Date date = new Date(current);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return dateFormat.format(date);
    }
    public static String color2String(int color){
        return color == InfoConstant.BLACK?InfoConstant.STRING_BLACK : InfoConstant.STRING_WHITE;
    }

    public static String chessman2String(Chessman chessman){
        return "" + (char) (chessman.getRow() + 'a') + (char)(chessman.getColumn() + 'a');
    }
}
