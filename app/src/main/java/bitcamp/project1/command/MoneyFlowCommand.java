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
//        executeRead(accountBook);
//
//        int index = Prompt.inputInt("수정 할 기록의 index : ");
//
//        MoneyFlow updateMF = (MoneyFlow) accountBook.get(index);
//
////        Date date =
////            Prompt.inputDate("date 변경 (" + updateMF.getTransactionDate() + ") :");
//        String incomeOrSpend =
//            inputIncomeOrSpend("수입 or 지출 변경 (" + updateMF.getIncomeOrSpend() + ") : ");
//        int amount =
//            inputAmount("금액 변경 (" + updateMF.getAmount() + ") : ");
//        String paymentMethod =
//            inputPaymentMethod("결제 수단 변경 (" + updateMF.getPaymentMethod().toString() + ") :");
//        String category =
//            inputCategory(updateMF.getIncomeOrSpend(), "항목 변경 (" + updateMF.getCategory() + ") :");
//        String note =
//            inputNote("설명 변경 (" + updateMF.getDescription() + ") :");
//
////        MoneyFlow updatedMoneyFlow = new MoneyFlow(accountBook.size(), date, amount, incomeOrSpend,
////            category,
////            note, paymentMethod);
////
////        int addIndex = 0;
////        for (addIndex = 0; addIndex < accountBook.size(); addIndex++) {
////            MoneyFlow oldMoneyFlow = (MoneyFlow) accountBook.get(addIndex);
////
////            if (oldMoneyFlow.getTransactionDate().after(updatedMoneyFlow.getTransactionDate())) {
////                break;
////            }
////        }
////        accountBook.set(addIndex, updatedMoneyFlow);
    }

    @Override
    public void executeRead() {
        printAccountBook(moneyFlowList);
    }

    @Override
    public void executeDelete() {
//        printAccountBook(accountBook);
//
//        int index = Prompt.inputInt("삭제 할 기록의 index : ");
//
//        accountBook.remove(index - 1);
//
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


}









