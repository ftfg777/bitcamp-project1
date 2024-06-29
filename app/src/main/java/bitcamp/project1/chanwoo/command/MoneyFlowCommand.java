package bitcamp.project1.chanwoo.command;


import bitcamp.project1.util.Prompt;
import bitcamp.project1.vo.Category.DepositCategory;
import bitcamp.project1.vo.Category.WithdrawCategory;
import bitcamp.project1.vo.MoneyFlow;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class MoneyFlowCommand {

    List<MoneyFlow> moneyFlowList = new ArrayList<>();

    public void deleteMoneyFlow() {
        System.out.println("삭제");
    }

    public void updateMoneyFlow() {
        System.out.println("번호 | 거래유형 | 날짜 | 카테고리 | 금액 | 메모");
        for (Object object : moneyFlowList.toArray()) {
            MoneyFlow moneyFlow = (MoneyFlow) object;
            System.out.printf("%d | %s | %s | %s | %d | %s \n",
                moneyFlow.getNo(),
                moneyFlow.getIncomeOrSpend(),
                moneyFlow.getTransactionDate(),
                moneyFlow.getCategory(),
                moneyFlow.getAmount(),
                moneyFlow.getDescription()
            );
        }
        int no = Prompt.inputInt("수정할 내역 선택:");
        MoneyFlow moneyFlow = moneyFlowList.get(no - 1);
        System.out.printf(moneyFlow.setDescription(Prompt.input("설명:")));
        System.out.printf(moneyFlow.setDescription(Prompt.input("설명:")));
        System.out.printf(moneyFlow.setDescription(Prompt.input("설명:")));
        System.out.printf(moneyFlow.setDescription(Prompt.input("설명:")));
    }


    public void viewMoneyFlow() {
        System.out.println("번호 | 거래유형 | 날짜 | 카테고리 | 금액 | 메모");
        for (Object object : moneyFlowList.toArray()) {
            MoneyFlow moneyFlow = (MoneyFlow) object;
            System.out.printf("%d | %s | %s | %s | %d | %s \n",
                moneyFlow.getNo(),
                moneyFlow.getIncomeOrSpend(),
                moneyFlow.getTransactionDate(),
                moneyFlow.getCategory(),
                moneyFlow.getAmount(),
                moneyFlow.getDescription()
            );
        }
    }


    public void addMoneyFlow() {

        while (true) {
            try {
                MoneyFlow moneyFlow = new MoneyFlow();
                String command;
                int year = 0;
                int month = 0;
                int day = 0;

                command = Prompt.input("년 입력(2000 ~ 2100) 기본값 오늘:");
                if (command.isEmpty()) {
                    year = LocalDate.now().getYear();
                } else {
                    year = Integer.parseInt(command);
                }

                if (!isInRange(year, 2000 - 1, 2100)) {
                    System.out.println("유효한 년도를 입력해 주세요.");
                    continue;
                }

                command = Prompt.input("월 입력(1 ~ 12) 기본값 오늘:");
                if (command.isEmpty()) {
                    month = LocalDate.now().getMonthValue();
                } else {
                    month = Integer.parseInt(command);
                }

                if (!isInRange(month, 0, 12)) {
                    System.out.println("유효한 월을 입력해 주세요.");
                    continue;
                }

                Calendar calendar = printCalendar(year, month - 1);

                while (true) {
                    command = Prompt.input("일 입력(기본값 오늘):");
                    if (command.isEmpty()) {
                        day = LocalDate.now().getDayOfMonth();
                    } else {
                        day = Integer.parseInt(command);
                    }

                    if (!isValidDay(year, month - 1, day)) {
                        System.out.println("유효한 일을 입력해 주세요.");
                    } else {
                        calendar.set(year, month - 1, day);
                        break;
                    }
                }

                moneyFlow.setTransactionDate(calendar);

                int incomeOrSpendNo = Prompt.inputInt("거래 유형(1.입금 2.지출 0.종료):");
                if (incomeOrSpendNo == 0) {
                    System.out.println("입력을 종료합니다.");
                    return;
                }
                if (incomeOrSpendNo < 0 || incomeOrSpendNo >= 3) {
                    System.out.println("유효한 번호를 입력해 주세요.");
                    continue;
                }

                // 입금 로직
                if (incomeOrSpendNo == 1) {
                    moneyFlow.setIncomeOrSpend("입금");

                    int depositAmount = Prompt.inputInt("입금액 입력:");
                    if (depositAmount <= 0) {
                        System.out.println("0보다 큰 금액을 입력해 주세요.");
                        continue;
                    }

                    moneyFlow.setAmount(moneyFlow.getAmount() + depositAmount);

                    int i = 0;
                    for (DepositCategory category : DepositCategory.values()) {
                        System.out.printf("%d. %s\n", i + 1, category.getName());
                        i++;
                    }
                    int categoryNo = Prompt.inputInt("카테고리 선택:");
                    if (!isInRange(categoryNo, 0, DepositCategory.values().length)) {
                        System.out.println("유효한 카테고리 번호를 입력해 주세요.");
                        continue;
                    }
                    moneyFlow.setCategory(DepositCategory.values()[categoryNo - 1].getName());
                }

                // 지출 로직
                if (incomeOrSpendNo == 2) {
                    moneyFlow.setIncomeOrSpend("지출");
                    int amountSpent = Prompt.inputInt("지출액 입력:");
                    if (amountSpent <= 0) {
                        System.out.println("0보다 큰 금액을 입력해 주세요.");
                        continue;
                    }

                    moneyFlow.setAmount(moneyFlow.getAmount() - amountSpent);

                    int i = 0;
                    for (WithdrawCategory category : WithdrawCategory.values()) {
                        System.out.printf("%d. %s\n", i + 1, category.getName());
                        i++;
                    }

                    int categoryNo = Prompt.inputInt("카테고리 선택:");
                    if (!isInRange(categoryNo, 0, WithdrawCategory.values().length)) {
                        System.out.println("유효한 카테고리 번호를 입력해 주세요.");
                        continue;
                    }
                    moneyFlow.setCategory(WithdrawCategory.values()[categoryNo - 1].getName());
                }

                moneyFlow.setDescription(Prompt.input("메모 입력:"));
                moneyFlow.setNo(MoneyFlow.getNextSeqNo());
                moneyFlowList.add(moneyFlow);
                System.out.println("작성 완료");
                return;

            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }
        }
    }


    public Calendar printCalendar(int year, int month) {
        // 현재 연도와 월 가져오기
        String boldAnsi = "\033[1m";
        String redAnsi = "\033[31m";
        String resetAnsi = "\033[0m";

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

        LocalDate today = LocalDate.now();
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









