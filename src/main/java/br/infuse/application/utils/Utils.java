package br.infuse.application.utils;

import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Utils {

    public static LocalDate stringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return LocalDate.parse(date, formatter);
    }

    public static String pegarDataAtual(){
        LocalDate now = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }

    public static String dateToString(LocalDate now){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(formatter);
    }

    public static boolean verificarVazioOuNulo(Object val){
        if(Objects.isNull(val) || Strings.isEmpty(val.toString())){
            return false;
        }else{
            return true;
        }
    }
}
