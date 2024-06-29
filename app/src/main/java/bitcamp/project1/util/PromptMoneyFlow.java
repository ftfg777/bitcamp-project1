package bitcamp.project1.util;

import bitcamp.project1.vo.Category.DepositCategory;
import bitcamp.project1.vo.Category.WithdrawCategory;
import bitcamp.project1.vo.MoneyFlow;
import bitcamp.project1.util.Print;
import java.time.LocalDate;
import java.util.Calendar;

public class PromptMoneyFlow extends Prompt{

    // 추가된 메서드 (240629 by 동인) 컨펌 후 주석 제거
    public static Calendar inputCalendar(Calendar defaultCalendar) {
        int year = 0;
        int month = 0;

        int defaultYear = defaultCalendar.get(Calendar.YEAR);
        int defaultMonth = defaultCalendar.get(Calendar.MONTH) + 1;
        int defaultDay = defaultCalendar.get(Calendar.DATE);

        String defaultDate = defaultYear + "-" + defaultMonth + "-" + defaultDay;

        while(true) {
            System.out.println("날짜 입력 [0 = 종료]");
            System.out.println("1. 자동 입력 (" + defaultDate + ")");
            System.out.println("2. 수동 입력");
            int selectInputMethod = inputInt("날짜 입력 방식 선택 >>");

            switch (selectInputMethod) {
                case 0:
                    return null;
                case 1:
                    return defaultCalendar;
                case 2:
                    String inputYearMessage = "연도 입력(2000 ~ 2100, default = " + defaultYear + ")";
                    year = inputYearWithDefault(inputYearMessage, defaultYear);

                    if (year == defaultYear){
                        String inputMonthMessage = "월 입력(1 ~ 12, default =" + defaultMonth + ")";
                        month = inputMonthWithDefault(inputMonthMessage, defaultMonth - 1);

                        if (month == defaultMonth) {
                            Calendar calendar = Print.printCalendar(year, month - 1);

                            String inputDayMessage = "일 입력 (1 ~ " + getMaxDay(calendar) + ", default =" + defaultDay + ")";
                            inputDayWithDefault(inputDayMessage, defaultDay, calendar);

                            return calendar;
                        } else {
                            Calendar calendar = Print.printCalendar(year, month - 1);

                            String inputDayMessage = "일 입력 (1 ~ " + getMaxDay(calendar) + ")";
                            inputDayWithoutDefault(inputDayMessage, calendar);
                            return calendar;
                        }
                    } else {
                        String inputMonthMessage =  "월 입력(1 ~ 12)";
                        month = inputMonthWithoutDefault(inputMonthMessage);
                        Calendar calendar = Print.printCalendar(year, month - 1);

                        String inputDayMessage = "일 입력( ~ " + getMaxDay(calendar) + ")";
                        inputDayWithoutDefault(inputDayMessage, calendar);
                        return calendar;
                    }
                default:
                    System.out.println("올바른 값을 입력하세요.");
            }
        }
    }

    private static int inputMonthWithoutDefault(String message) {
        int month;
        while (true) {
            try {
                String command = input(message);
                if (command.isEmpty()) {
                    System.out.println("값을 입력해 주세요.");
                    continue;
                } else {
                    month = Integer.parseInt(command);
                }

                if (!isInRange(month, 0, 12)) {
                    System.out.println("유효한 월을 입력해 주세요.");
                    continue;
                }
                return month;
            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }

        }
    }

    private static void inputDayWithoutDefault(String message, Calendar calendar) {
        while (true) {
            try {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day;
                String command = input(message);
                if (command.isEmpty()) {
                    System.out.println("값을 입력해 주세요.");
                    continue;
                } else {
                    day = Integer.parseInt(command);
                }

                if (!isValidDay(year, month, day)) {
                    System.out.println("유효한 날짜를 입력해 주세요.");
                } else {
                    calendar.set(year, month, day);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }

        }
    }

    private static int inputYearWithDefault(String message, int defaultYear) {
        int year;
        while (true) {
            try {
                String command = input(message);
                if (command.isEmpty()) {
                    year = defaultYear;
                } else {
                    year = Integer.parseInt(command);
                }

                if (!isInRange(year, 2000 - 1, 2100)) {
                    System.out.println("유효한 년도를 입력해 주세요.");
                    continue;
                }
                return year;
            } catch (NumberFormatException e){
                System.out.println("숫자로 입력해 주세요.");
            }
        }

    }

    private static int inputMonthWithDefault(String message, int defaultMonth) {
        int month;
        while (true) {
            try {
                String command = input(message);
                if (command.isEmpty()) {
                    month = defaultMonth;
                } else {
                    month = Integer.parseInt(command);
                }

                if (!isInRange(month, 0, 12)) {
                    System.out.println("유효한 월을 입력해 주세요.");
                    continue;
                }
                return month;
            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }

        }
    }

    private static void inputDayWithDefault(String message, int defaultDay, Calendar calendar) {
        while (true) {
            try {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day;
                String command = input(message);
                if (command.isEmpty()) {
                    day = defaultDay;
                } else {
                    day = Integer.parseInt(command);
                }

                if (!isValidDay(year, month, day)) {
                    System.out.println("유효한 날짜를 입력해 주세요.");
                    continue;
                } else {
                    calendar.set(year, month, day);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }

        }
    }

    public static String inputTransactionDescription(String message, MoneyFlow moneyFlow) {
        return input(message);
    }

    public static int inputAmount(String message) {
        while (true) {
            try {
                return inputInt(message);
            } catch (NumberFormatException e) {
                System.out.println("올바른 금액을 입력하세요.");
            }
        }
    }

    public static String inputIncomeOrSpend(String message) {
        while (true) {
            try {
                System.out.println("거래 유형 [0 : 종료]");
                System.out.println("1. 수입");
                System.out.println("2. 지출");
                int incomeOrSpendNo = inputInt(message);

                if (incomeOrSpendNo == 0) {
                    System.out.println("입력을 종료합니다.");
                    return null;
                }
                if (!PromptMoneyFlow.isInRange(incomeOrSpendNo, 0, 3)) {
                    System.out.println("유효한 번호를 입력해 주세요.");
                    continue;
                }

                switch (incomeOrSpendNo) {
                    case 1:
                        return "수입";
                    case 2:
                        return "지출";
                    default:
                        return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력해주세요.");
            }
        }
    }

    public static String inputCategory(String incomeOrSpend, String message) {
        while (true) {
            try {
                if (incomeOrSpend.equals("수입")) {
                    for (int i = 0; i < DepositCategory.values().length; i++) {
                        System.out.println(i + 1 + ". " + DepositCategory.values()[i].getName());
                    }

                    int inputCategory = inputInt(message);

                    if (0 < inputCategory && inputCategory < DepositCategory.values().length + 1) {
                        return DepositCategory.values()[inputCategory - 1].getName();
                    } else {
                        System.out.println("올바른 항목을 선택하세요. ");
                    }
                } else if (incomeOrSpend.equals("지출")) {
                    for (int i = 0; i < WithdrawCategory.values().length; i++) {
                        System.out.println(i + 1 + ". " + WithdrawCategory.values()[i].getName());
                    }

                    int inputCategory = inputInt(message);

                    if (0 < inputCategory && inputCategory < WithdrawCategory.values().length + 1) {
                        return WithdrawCategory.values()[inputCategory - 1].getName();
                    } else {
                        System.out.println("올바른 항목을 선택하세요. ");
                    }
                } else {
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력해주세요.");
            }
        }
    }

    public static String inputDescription(String message) {
        return input(message);
    }


    public static String inputPaymentMethod(String message) {
        String[] paymentMethods = {"카드", "현금"};

        while (true) {
            try {
                for (int i = 0; i < paymentMethods.length; i++) {
                    System.out.println(i + 1 + ". " + paymentMethods[i]);
                }
                int inputPaymentMethod = inputInt(message);

                if (0 < inputPaymentMethod && inputPaymentMethod < paymentMethods.length + 1) {
                    return paymentMethods[inputPaymentMethod-1];
                } else {
                    System.out.println("올바른 항목을 선택하세요. ");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력해주세요.");
            }
        }
    }

    public static boolean isInRange(int value, int min, int max) {
        return value > min && value <= max; // 월은 0부터 11까지 유효 (0: 1월, 11: 12월)
    }

    public static boolean isValidDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int maxDay = getMaxDay(calendar);
        return day >= 1 && day <= maxDay;
    }

    public static int getMaxDay(Calendar calendar){
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
