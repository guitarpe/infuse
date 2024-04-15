package br.infuse.application.utils;

import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Utils {

    public static LocalDateTime convertDtString(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.parse(date, formatter);
    }

    public static String pegarDataAtual(){
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public static boolean verifyEmptyOrNull(Object val){
        if(Objects.isNull(val) || Strings.isEmpty(val.toString())){
            return false;
        }else{
            return true;
        }
    }
}
