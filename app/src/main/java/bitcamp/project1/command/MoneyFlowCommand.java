package bitcamp.project1.command;


import bitcamp.project1.util.MoneyFlowInterface;
import bitcamp.project1.util.Prompt;
import bitcamp.project1.vo.Category.DepositCategory;
import bitcamp.project1.vo.Category.WithdrawCategory;
import bitcamp.project1.vo.MoneyFlow;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MoneyFlowCommand implements MoneyFlowInterface {

    List<MoneyFlow> moneyFlowList = new ArrayList<>();


    @Override
    public void executeCreate() {
        while (true) {
            try {
                MoneyFlow moneyFlow = new MoneyFlow();
                int year = 0;
                int month = 0;

                year = inputYearWithDefault("연도 입력(2000~2100) Default now :");

                month = inputMonthWithDefault("월 입력(1~12) Default now :");

                Calendar calendar = printCalendar(year, month - 1);

                inputDayWithDefault("일 입력 Default now :", calendar);

                moneyFlow.setTransactionDate(calendar);

                int result = processTransactionType(moneyFlow); // 수입 | 지출 선택
                if (result == 0) {
                    return;
                }

                inputTransactionDescription("메모 입력:", moneyFlow); // 메모 입력

                moneyFlowList.add(addMoneyFlowWithId(moneyFlow)); // 시퀀스 증가 및 리스트에 추가

                System.out.println("작성 완료!");
                return;
            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }
        }

    }

    private void inputDayWithDefault(String message, Calendar calendar) {
        while (true) {
            try {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day;
                String command = Prompt.input(message);
                if (command.isEmpty()) {
                    day = LocalDate.now().getDayOfMonth();
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


    private int inputMonthWithDefault(String message) {
        int month;
        while (true) {
            try {
                String command = Prompt.input(message);
                if (command.isEmpty()) {
                    month = LocalDate.now().getMonthValue();
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

    private int inputYearWithDefault(String message) {
        int year;
        while (true) {
            String command = Prompt.input(message);
            if (command.isEmpty()) {
                year = LocalDate.now().getYear();
            } else {
                year = Integer.parseInt(command);
            }

            if (!isInRange(year, 2000 - 1, 2100)) {
                System.out.println("유효한 년도를 입력해 주세요.");
                continue;
            }
            return year;
        }

    }

    private MoneyFlow addMoneyFlowWithId(MoneyFlow moneyFlow) {
        moneyFlow.setNo(MoneyFlow.getNextSeqNo());
        return moneyFlow;
    }


    private void inputTransactionDescription(String message, MoneyFlow moneyFlow) {
        moneyFlow.setDescription(Prompt.input(message));
    }

    @Override
    public void executeUpdate() {
        executeRead();

        // 예외 처리 필요할 거 같습니다!
        while (true) {
            int index = Prompt.inputInt("수정 할 기록의 INDEX : ");
            if (index == 0) {
                System.out.println("수정이 취소되었습니다.");
                return;
            }
            MoneyFlow updateMF = moneyFlowList.get(index);
            if (updateMF == null) {
                System.out.println("해당 번호의 거래 내역이 없습니다.");
                continue;
            }

            // 직접적인 날짜 수정은 막고 수정을 하고 싶으면 삭제하거나 새로 만드는 걸로 하는 게 어떨까 싶습니다!
            int year = inputYearWithDefault("넣어주세용! 연도 :");
            int month = inputMonthWithDefault("넣어주세용! 월 :");
            Calendar calendar = printCalendar(year, month - 1);
            inputDayWithDefault("넣어주세용! 일 :", calendar);

            // 이 부분 가독성이
            String incomeOrSpend =
                inputIncomeOrSpend("수입 or 지출 변경 (" + updateMF.getIncomeOrSpend() + ") : ");
            int amount =
                inputAmount("금액 변경 (" + updateMF.getAmount() + ") : ");
            String paymentMethod =
                inputPaymentMethod("결제 수단 변경 (" + updateMF.getPaymentMethod().toString() + ") :");
            String category =
                inputCategory(updateMF.getIncomeOrSpend(),
                    "항목 변경 (" + updateMF.getCategory() + ") :");
            String note =
                inputDescription("설명 변경 (" + updateMF.getDescription() + ") :");

            // 생성자로 수정을 하면 기존 객체가 아닌 힙영역에 새롭게 생성되는 객체로 하시려는
            // 의도가 맞는지 궁금합니다~!
            MoneyFlow updatedMoneyFlow = new MoneyFlow(calendar, amount,
                incomeOrSpend,
                category,
                note, paymentMethod);

            int addIndex = 0;
            for (addIndex = 0; addIndex < moneyFlowList.size(); addIndex++) {
                MoneyFlow oldMoneyFlow = moneyFlowList.get(addIndex);

                // 캘린더로 받으면서 .after를 사용할 수 없게 됨.
                // 캘린더 -> Date 형변환 후 .after 메소드 사용
                if (oldMoneyFlow.getCalendar().getTime()
                    .after(updatedMoneyFlow.getCalendar().getTime())) {
                    break;
                }
            }
            moneyFlowList.set(addIndex, updatedMoneyFlow);
        }

    }

    @Override
    public void executeRead() {
        printAccountBook(moneyFlowList);
    }

    @Override
    public void executeDelete() {
        printAccountBook(moneyFlowList);

        int index = Prompt.inputInt("삭제 할 기록의 index : ");

        moneyFlowList.remove(index - 1);

    }

    public int processTransactionType(MoneyFlow moneyFlow) {
        while (true) {
            try {
                int incomeOrSpendNo = Prompt.inputInt("거래 유형 [1.수입 2.지출 0.종료]: ");
                if (incomeOrSpendNo == 0) {
                    System.out.println("입력을 종료합니다.");
                    return 0;
                }
                if (!isInRange(incomeOrSpendNo, 0, 3)) {
                    System.out.println("유효한 번호를 입력해 주세요.");
                    continue;
                }
                // 입금 로직
                if (incomeOrSpendNo == 1 || incomeOrSpendNo == 2) {
                    if (incomeOrSpendNo == 1) {
                        moneyFlow.setIncomeOrSpend("수입");

                        int depositAmount = Prompt.inputInt("수입액 입력:");
                        if (depositAmount <= 0) {
                            System.out.println("0보다 큰 금액을 입력해 주세요.");
                            continue;
                        }

                        moneyFlow.setAmount(moneyFlow.getAmount() + depositAmount);

                        printCategory(DepositCategory.values()[0]);
                        int categoryNo = 0;
                        while (true) {
                            categoryNo = Prompt.inputInt("카테고리 선택:");
                            if (!isInRange(categoryNo, 0, DepositCategory.values().length)) {
                                System.out.println("유효한 카테고리 번호를 입력해 주세요.");
                                continue;
                            }
                            break;
                        }
                        moneyFlow.setCategory(DepositCategory.values()[categoryNo - 1].getName());
                    }

                    // 지출 로직
                    if (incomeOrSpendNo == 2) {
                        moneyFlow.setIncomeOrSpend("지출");
                        int amountSpent = Prompt.inputInt("지출액 입력:");
                        if (amountSpent <= 0) {
                            System.out.println("0보다 큰 금액을 입력해 주세요.");
                        }

                        moneyFlow.setAmount(moneyFlow.getAmount() - amountSpent);

                        printCategory(WithdrawCategory.values()[0]);
                        int categoryNo = 0;
                        while (true) {
                            categoryNo = Prompt.inputInt("카테고리 선택:");
                            if (!isInRange(categoryNo, 0, WithdrawCategory.values().length)) {
                                System.out.println("유효한 카테고리 번호를 입력해 주세요.");
                                continue;
                            }
                            break;
                        }
                        moneyFlow.setCategory(WithdrawCategory.values()[categoryNo - 1].getName());
                    }
                } else {
                    System.out.println("유효한 값이 아닙니다.");
                    continue;
                }

                return 1;
            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }
        }

    }


    public void printCategory(Object value) {
        int i = 0;

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

    public void printAccountBook(List<MoneyFlow> moneyFlowList) {
        String title = "-----reach to rich-----";

        System.out.println(title);
        System.out.println(
            "No |   날짜   |     수입     |     지출     |     잔액     |   항목   | 결제방식 |     메모");
        System.out.println(
            "-------------------------------------------------------------------------------------------------------------");
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
                balance -= spend;
            }

            System.out.printf("%02d |%s| %,12d | %,12d | %,12d | %s | %s | %s\n", mf.getNo(),
                mf.getTransactionDate(), income, spend, balance, mf.getCategory(),
                "신용카드", mf.getDescription());
        }
    }

    public Calendar printCalendar(int year, int month) {
        // 현재 연도와 월 가져오기
        String boldAnsi = "\033[1m";
        String redAnsi = "\033[31m";
        String resetAnsi = "\033[0m";
        LocalDate today = LocalDate.now();

        Calendar calendar = Calendar.getInstance();
        System.out.println("--------------------------------------");

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
        System.out.println("--------------------------------------");
        return calendar;
    }

    public static boolean isInRange(int value, int min, int max) {
        return value > min && value <= max; // 월은 0부터 11까지 유효 (0: 1월, 11: 12월)
    }

    public static boolean isValidDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day >= 1 && day <= maxDay;
    }

    public static int inputAmount(String message) {
        while (true) {
            try {
                int amount = Prompt.inputInt(message);
                return amount;
            } catch (NumberFormatException e) {
                System.out.println("올바른 금액을 입력하세요.");
            }
        }
    }

    public static String inputIncomeOrSpend(String message) {
        while (true) {
            try {
                System.out.println("1.   수입");
                System.out.println("2.   지출");

                int incomeOrSpend = Prompt.inputInt(message);

                switch (incomeOrSpend) {
                    case 1:
                        return "수입";
                    case 2:
                        return "지출";
                    default:
                        System.out.println("올바른 항목을 선택해주세요.");
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

                    int inputCategory = Prompt.inputInt(message);

                    if (0 < inputCategory && inputCategory < DepositCategory.values().length + 1) {
                        return DepositCategory.values()[inputCategory - 1].getName();
                    } else {
                        System.out.println("올바른 항목을 선택하세요. ");
                    }
                } else if (incomeOrSpend.equals("지출")) {
                    for (int i = 0; i < WithdrawCategory.values().length; i++) {
                        System.out.println(i + 1 + ". " + WithdrawCategory.values()[i].getName());
                    }

                    int inputCategory = Prompt.inputInt(message);

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
        return Prompt.input(message);
    }


    public static String inputPaymentMethod(String message) {
        while (true) {
            try {
                for (int i = 0; i < PaymentMethod.values().length; i++) {
                    System.out.println(i + 1 + ". " + PaymentMethod.values()[i].getName());
                }
                int inputPaymentMethod = Prompt.inputInt(message);

                if (0 < inputPaymentMethod
                    && inputPaymentMethod < PaymentMethod.values().length + 1) {
                    return PaymentMethod.values()[inputPaymentMethod - 1].getName();
                } else {
                    System.out.println("올바른 항목을 선택하세요. ");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력해주세요.");
            }
        }
    }

}









