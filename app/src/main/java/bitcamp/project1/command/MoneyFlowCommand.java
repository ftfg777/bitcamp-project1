package bitcamp.project1.command;


import bitcamp.project1.util.MoneyFlowInterface;
import bitcamp.project1.util.Print;
import bitcamp.project1.util.Prompt;
import bitcamp.project1.util.PromptMoneyFlow;
import bitcamp.project1.vo.Category.DepositCategory;
import bitcamp.project1.vo.Category.WithdrawCategory;
import bitcamp.project1.vo.MoneyFlow;
import java.time.LocalDate;
import java.util.Collections;
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

                Calendar calendar = PromptMoneyFlow.inputCalendar(Calendar.getInstance());
                if (calendar == null) {
                    break;
                } else {
                    moneyFlow.setTransactionDate(calendar);
                }

                int result = processTransactionType(moneyFlow); // 수입 | 지출 선택
                if (result == 0) {
                    break;
                }

                // 메모 입력 및 세팅
                String description = PromptMoneyFlow.inputTransactionDescription("메모 입력 >>",
                    moneyFlow); // 메모 입력
                moneyFlow.setDescription(description);

                // static seqNo 증가 후 리스트 추가
                moneyFlowList.add(addMoneyFlowWithId(moneyFlow)); // 시퀀스 증가 및 리스트에 추가

                moneyFlowList = sortNoByDate((ArrayList) moneyFlowList);
                System.out.println("작성 완료!");

                return;
            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }
        }
        System.out.println("생성이 취소되었습니다.");

    }

    @Override
    public void executeUpdate() {
        Print.printAccountBook(moneyFlowList);

        while (true) {
            int no = Prompt.inputInt("수정 할 기록의 No [0 = 종료] >>");
            if (no == 0) {
                break;
            }

            // 선택한 No의 MoneyFlow 선택
            MoneyFlow updateMF = getByNo(no);
            int updateIndex = ofIndex((ArrayList) moneyFlowList, updateMF);
            if (updateIndex == 1) {
                System.out.println("error accured...");
            }

            if (updateMF == null) {
                System.out.println("해당 번호의 거래 내역이 없습니다.");
                continue;
            }

            Calendar calendar = PromptMoneyFlow.inputCalendar(updateMF.getCalendar());
            if (calendar == null) {
                break;
            }

            String inputIncomeOrSpendMessage =
                "수입 or 지출 변경 (" + updateMF.getIncomeOrSpend() + ") >>";
            String incomeOrSpend = PromptMoneyFlow.inputIncomeOrSpend(inputIncomeOrSpendMessage);

            String inputAmountMessage = "금액 변경 (" + updateMF.getAmount() + ") >>";
            int amount = PromptMoneyFlow.inputAmount(inputAmountMessage);

            String inputPaymentMethodMessage =
                "결제 수단 변경 (" + updateMF.getPaymentMethod().toString() + ") >>";
            String paymentMethod = PromptMoneyFlow.inputPaymentMethod(inputPaymentMethodMessage);

            String inputCategoryMessage = "항목 변경 (" + updateMF.getCategory() + ") >>";
            String category = PromptMoneyFlow.inputCategory(updateMF.getIncomeOrSpend(),
                inputCategoryMessage);

            String inputDescriptionMessage = "설명 변경 (" + updateMF.getDescription() + ") >>";
            String description = PromptMoneyFlow.inputDescription(inputDescriptionMessage);

            MoneyFlow updatedMoneyFlow = new MoneyFlow(calendar, amount,
                incomeOrSpend, category, description, paymentMethod);

            updatedMoneyFlow.setNo(moneyFlowList.get(updateIndex).getNo());

            moneyFlowList.set(updateIndex, updatedMoneyFlow);

            moneyFlowList = sortNoByDate((ArrayList) moneyFlowList);
            return;
        }
        System.out.println("수정이 취소되었습니다.");
    }

    private int ofIndex(ArrayList arrayList, MoneyFlow moneyFlow) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (moneyFlow.equals(arrayList.get(i))) {
                return i;
            }
        }
        return -1;
    }


    public ArrayList sortNoByDate(ArrayList moneyFlowList) {
        for (int i = 0; i < moneyFlowList.size(); i++) {
            for (int j = i + 1; j < moneyFlowList.size(); j++) {
                MoneyFlow moneyFlowA = (MoneyFlow) moneyFlowList.get(i);
                MoneyFlow moneyFlowB = (MoneyFlow) moneyFlowList.get(j);
                if (moneyFlowA.getCalendar().after(moneyFlowB.getCalendar())) {
                    System.out.println("디버깅용");
                    int tempNo = moneyFlowA.getNo();
                    moneyFlowA.setNo(moneyFlowB.getNo());
                    moneyFlowB.setNo(tempNo);
                    System.out.println(moneyFlowA);
                    System.out.println(moneyFlowB);
                    Collections.swap(moneyFlowList, i, j);
                    System.out.println(moneyFlowA);
                    System.out.println(moneyFlowB);
                }
            }
        }
        return moneyFlowList;
    }


    @Override
    public void executeRead() {
        Print.printAccountBook(moneyFlowList);
    }

    @Override
    public void executeDelete() {
        Print.printAccountBook(moneyFlowList);

        int index = Prompt.inputInt("삭제 할 기록의 index : ");

        moneyFlowList.remove(index - 1);
    }

    private MoneyFlow addMoneyFlowWithId(MoneyFlow moneyFlow) {
        moneyFlow.setNo(MoneyFlow.getNextSeqNo());
        return moneyFlow;
    }

    public MoneyFlow getByNo(int no) {
        for (int i = 0; i < moneyFlowList.size(); i++) {
            if (moneyFlowList.get(i).getNo() == no) {
                return moneyFlowList.get(i);
            }
        }
        return null;
    }

    public int processTransactionType(MoneyFlow moneyFlow) {
        while (true) {
            try {
                String incomeOrSpend = PromptMoneyFlow.inputIncomeOrSpend("거래 유형 선택 >>");
                if (incomeOrSpend.equals("종료")) {
                    return 0;
                } else {
                    moneyFlow.setIncomeOrSpend(incomeOrSpend);
                }

                // 수입 로직
                if (moneyFlow.getIncomeOrSpend().equals("수입")) {

                    int depositAmount = Prompt.inputInt("수입액 입력 >>");
                    if (depositAmount <= 0) {
                        System.out.println("0보다 큰 금액을 입력해 주세요.");
                        continue;
                    }

                    moneyFlow.setAmount(moneyFlow.getAmount() + depositAmount);

                    Print.printCategory(DepositCategory.values()[0]);
                    int categoryNo = 0;
                    while (true) {
                        categoryNo = Prompt.inputInt("카테고리 선택 >>");
                        if (!PromptMoneyFlow.isInRange(categoryNo, 0,
                            DepositCategory.values().length)) {
                            System.out.println("유효한 카테고리 번호를 입력해 주세요.");
                            continue;
                        }
                        break;
                    }

                    moneyFlow.setCategory(DepositCategory.values()[categoryNo - 1].getName());

                    moneyFlow.setPaymentMethod("        ");
                }

                // 지출 로직
                else if (moneyFlow.getIncomeOrSpend().equals("지출")) {

                    // ************************
                    // start of setting amount
                    int amountSpent = Prompt.inputInt("지출액 입력:");
                    if (amountSpent <= 0) {
                        System.out.println("0보다 큰 금액을 입력해 주세요.");
                    }

                    moneyFlow.setAmount(moneyFlow.getAmount() - amountSpent);

                    Print.printCategory(WithdrawCategory.values()[0]);
                    int categoryNo = 0;
                    while (true) {
                        categoryNo = Prompt.inputInt("카테고리 선택:");
                        if (!PromptMoneyFlow.isInRange(categoryNo, 0,
                            WithdrawCategory.values().length)) {
                            System.out.println("유효한 카테고리 번호를 입력해 주세요.");
                            continue;
                        }
                        break;
                    }
                    moneyFlow.setCategory(WithdrawCategory.values()[categoryNo - 1].getName());

                    moneyFlow.setPaymentMethod(PromptMoneyFlow.inputPaymentMethod("결제 수단 >>"));
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


            } catch (Exception e) {

            }
        }
    }
}









