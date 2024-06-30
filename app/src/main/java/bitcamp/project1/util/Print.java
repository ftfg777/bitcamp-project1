package bitcamp.project1.util;

import bitcamp.project1.vo.Category.DepositCategory;
import bitcamp.project1.vo.Category.WithdrawCategory;
import bitcamp.project1.vo.MoneyFlow;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class Print {

    static String stars = "**************************************";
    static String title = "**  쌈뽕 가계부 : 재테크의 매운맛!  **";

    public static Calendar printCalendar(int year, int month) {
        // 현재 연도와 월 가져오기
        String boldAnsi = "\033[1m";
        String redAnsi = "\033[31m";
        String resetAnsi = "\033[0m";
        LocalDate today = LocalDate.now();

        Calendar calendar = Calendar.getInstance();
        System.out.println("---------------------");

        // 해당 월의 첫 번째 날로 설정
        calendar.set(year, month, 1);

        // 해당 월의 마지막 일 가져오기
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 달력 출력
        System.out.printf("%s%d년 %d월%s \n", boldAnsi, year, (month + 1), resetAnsi);
        System.out.println("일 월 화 수 목 금 토");

        // 해당 월의 첫 번째 날의 요일을 가져오기
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 달력 출력용 변수 설정
        int dayOfWeekCounter = firstDayOfWeek - 1; // Adjust to zero-based index

        // 첫 번째 주 앞쪽 공백 출력
        for (int i = 0; i < dayOfWeekCounter; i++) {
            System.out.print("   ");
        }

        // 달력 일 출력
        for (int day = 1; day <= daysInMonth; day++) {
            dayOfWeekCounter++;
            if (today.getDayOfMonth() == day && today.getMonthValue() == month + 1
                && today.getYear() == year) {
                System.out.printf("%s%2d%s ", (boldAnsi + redAnsi), day, resetAnsi);
            } else {
                System.out.printf("%2d ", day);
            }
            if (dayOfWeekCounter == 7) {
                dayOfWeekCounter = 0;
                System.out.println();
            }
        }
        // 마지막 주 줄바꿈
        if (dayOfWeekCounter != 0) {
            System.out.println();
        }
        System.out.println("---------------------");
        return calendar;
    }

    public static void printAccountBook(List<MoneyFlow> moneyFlowList) {
        printTitleLong();

        System.out.println(
            "No |   날짜   |      수입      |      지출      |      잔액      |   항목   | 결제방식 |       메모");
        System.out.println(
            "------------------------------------------------------------------------------------------------------------------");
        int balance = 0;

        for (int i = 0; i < moneyFlowList.size(); i++) {
            MoneyFlow mf = moneyFlowList.get(i);

            int income = 0;
            int spend = 0;
            if (mf.getIncomeOrSpend().equals("수입")) {
                income = mf.getAmount();
                balance += income;
            } else {
                spend = mf.getAmount();
                balance += spend;
            }

            System.out.printf("%02d |%s| %,14d | %,14d | %,14d | %s | %s | %s\n", mf.getNo(),
                mf.getTransactionDate(), income, spend, balance, mf.getCategory(),
                mf.getPaymentMethod(), mf.getDescription());
        }
        System.out.println("");
    }

    public static void printCategory(Object value) {
        int i = 0;
        System.out.println("------<< 카테고리 [0 : 종료] >>------");

        if (value instanceof DepositCategory) {
            for (DepositCategory category : DepositCategory.values()) {
                System.out.printf("%d. %s\n", i + 1, category.getName());
                i++;
            }
        }
        if (value instanceof WithdrawCategory) {
            for (WithdrawCategory category : WithdrawCategory.values()) {
                System.out.printf("%d. %s\n", i + 1, category.getName());
                i++;
            }
        }
    }

    public static void printMenu(String menuTitle, String[] menus) {
        System.out.println("----------<<  " + menuTitle + "  >>----------");
        for (int i = 0; i < menus.length; i++) {
            String menu = menus[i];
            System.out.printf("%d. %s\n", i + 1, menu);
        }
    }

    public static void printMenuWithExit(String menuTitle, String[] menus) {
        System.out.println("--------<<  " + menuTitle + " [0 = 종료]  >>--------");
        for (int i = 0; i < menus.length; i++) {
            String menu = menus[i];
            System.out.printf("%d. %s\n", i + 1, menu);
        }
    }

    public static void printTitleShort() {
        System.out.println(stars);
        System.out.println(title);
        System.out.println(stars);
    }

    public static void printTitleLong() {
        System.out.println(stars + stars + stars);
        System.out.println(stars + title + stars);
        System.out.println(stars + stars + stars);
    }
}
