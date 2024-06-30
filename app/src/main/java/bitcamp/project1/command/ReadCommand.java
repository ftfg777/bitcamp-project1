package bitcamp.project1.command;

import static bitcamp.project1.command.MoneyFlowCommand.moneyFlowList;

import bitcamp.project1.util.Print;
import bitcamp.project1.util.Prompt;
import bitcamp.project1.util.PromptMoneyFlow;
import bitcamp.project1.vo.MoneyFlow;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReadCommand {

    static void processReadMenu(String menuTitle) {
        try {
            switch (menuTitle) {
                case "전체":
                    readAll();
                    break;
                case "입금":
                    readIncome();
                    break;
                case "출금":
                    readSpend();
                    break;
                case "기간":
                    readPeriod();
                    break;
                default:
                    System.out.printf("%s 메뉴의 명령을 처리할 수 없습니다.\n", menuTitle);
            }
        } catch (NumberFormatException ex) {
            System.out.println("숫자로 메뉴 번호를 입력하세요.");
        }

    }

    private static void readAll() {
        Print.printAccountBook(moneyFlowList);
    }

    private static void readIncome() {
        List<MoneyFlow> incomeMoneyFlowList = new ArrayList<>();

        for (int i = 0; i < moneyFlowList.size(); i++) {
            String incomeOrSpend = moneyFlowList.get(i).getIncomeOrSpend();
            if (incomeOrSpend.equals("수입")) {
                incomeMoneyFlowList.add(moneyFlowList.get(i));
            }

        }
        Print.printAccountBook(incomeMoneyFlowList);
    }

    private static void readSpend() {
        List<MoneyFlow> incomeMoneyFlowList = new ArrayList<>();

        for (int i = 0; i < moneyFlowList.size(); i++) {
            String incomeOrSpend = moneyFlowList.get(i).getIncomeOrSpend();
            if (incomeOrSpend.equals("지출")) {
                incomeMoneyFlowList.add(moneyFlowList.get(i));
            }

        }
        Print.printAccountBook(incomeMoneyFlowList);
    }

    private static void readPeriod() {
        while (true) {
            try {
                String[] readPeriodMenus = {"연", "월"};
                Print.printMenuWithExit("기간", readPeriodMenus);

                int menuNo = Prompt.inputInt("기간 선택 >>");

                if (menuNo == 0) {
                    break;
                } else {
                    switch (menuNo) {
                        case 1:
                            readYear();
                            return;
                        case 2:
                            readMonth();
                            return;
                        default:
                            System.out.println("유효한 번호를 입력해주세요.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력해 주세요.");
            }
        }
        System.out.println("조회가 종료되었습니다.");
    }

    private static void readYear() {
        List<MoneyFlow> yearMoneyFlowList = new ArrayList<>();

        System.out.println("------<< 연도 입력 [0 : 종료] >>------");

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        int defaultYear = startCalendar.get(Calendar.YEAR);

        String inputYearMessage =
            "연도 입력(2000 ~ 2100, default = " + defaultYear + ") >>";

        int year = PromptMoneyFlow.inputYearWithDefault(inputYearMessage, defaultYear);

        startCalendar.set(year - 1, 12, 31);
        endCalendar.set(year + 1, 1, 1);

        for (int i = 0; i < moneyFlowList.size(); i++) {
            Calendar compareCalendar = moneyFlowList.get(i).getCalendar();
            if (compareCalendar.after(startCalendar) && compareCalendar.before(endCalendar)) {
                yearMoneyFlowList.add(moneyFlowList.get(i));
            }

        }
        Print.printAccountBook(yearMoneyFlowList);

    }


    private static void readMonth() {
        List<MoneyFlow> monthMoneyFlowList = new ArrayList<>();

        System.out.println("-------<< 연월 입력 [0 : 종료] >>-------");

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        int defaultYear = startCalendar.get(Calendar.YEAR);
        int defaultMonth = startCalendar.get(Calendar.MONTH);

        String inputYearMessage =
            "연도 입력(2000 ~ 2100, default = " + defaultYear + ") >>";
        String inputMonthMessage =
            "월 입력(1 ~ 12, default = " + (defaultMonth + 1) + ") >>";

        int year = PromptMoneyFlow.inputYearWithDefault(inputYearMessage, defaultYear);
        int month = PromptMoneyFlow.inputMonthWithDefault(inputMonthMessage, defaultMonth);

        if (month == 1) {
            startCalendar.set(year - 1, 12, 31);
            endCalendar.set(year, month + 1, 1);
        } else if (month == 12) {
            startCalendar.set(year, 11, 30);
            endCalendar.set(year + 1, 1, 1);
        } else {
            Calendar lastMonthCalendar = Calendar.getInstance();
            lastMonthCalendar.set(year, month - 1, 1);
            int max_day = PromptMoneyFlow.getMaxDay(lastMonthCalendar);

            startCalendar.set(year, month - 1, max_day);
            endCalendar.set(year, month + 1, 1);
        }

        for (int i = 0; i < moneyFlowList.size(); i++) {
            Calendar compareCalendar = moneyFlowList.get(i).getCalendar();
            if (compareCalendar.after(startCalendar) && compareCalendar.before(endCalendar)) {
                monthMoneyFlowList.add(moneyFlowList.get(i));
            }

        }
        Print.printAccountBook(monthMoneyFlowList);

    }
}
