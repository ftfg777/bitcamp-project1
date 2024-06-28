package bitcamp.project1.DongIn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Prompt {

  static Scanner keyboardScanner = new Scanner(System.in);

  public static String input(String format, Object... args) {
    System.out.printf(format + " ", args);
    return keyboardScanner.nextLine();
  }

  public static int inputInt(String format, Object... args) {
    return Integer.parseInt(input(format, args));
  }

  public static Date inputDate(String format, Object... args){
    while (true) {
      try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = input(format, args);

        if (dateString.isEmpty()){
          DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate now = LocalDate.now();

          String formatedNow = now.format(dateTimeFormatter);
          return formatter.parse(formatedNow);
        } else {
          return formatter.parse(dateString);
        }
      } catch (ParseException e) {
        System.out.println("올바른 형식의 날짜를 입력해주세요.");
        System.out.println("예시 : 2024-06-03");
      }
    }
  }

  public static void close() {
    keyboardScanner.close();
  }
}
