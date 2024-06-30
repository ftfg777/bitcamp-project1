package bitcamp.project1.command;


import bitcamp.project1.util.MoneyFlowInterface;
import bitcamp.project1.util.Print;
import bitcamp.project1.util.Prompt;
import bitcamp.project1.util.PromptMoneyFlow;
import bitcamp.project1.vo.Category.DepositCategory;
import bitcamp.project1.vo.Category.WithdrawCategory;
import bitcamp.project1.vo.MoneyFlow;
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

                moneyFlowList = sortNoByDate(moneyFlowList);
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
            MoneyFlow newMoneyFlow = new MoneyFlow();

            int no = Prompt.inputInt("수정 할 기록의 No [0 = 종료] >>");
            if (no == 0) {
                break;
            }

            // 선택한 No의 MoneyFlow 선택
            MoneyFlow updateMoneyFlow = getByNo(no);

            // 선택한 No를 가진 MoneyFLow의 index
            int updateIndex = ofIndex(moneyFlowList, updateMoneyFlow);

            if (updateIndex == -1) {
                System.out.println("error accured...");
            }
            if (updateMoneyFlow == null) {
                System.out.println("해당 번호의 거래 내역이 없습니다.");
                continue;
            }

            Calendar calendar = PromptMoneyFlow.inputCalendar(updateMoneyFlow.getCalendar());
            if (calendar == null) {
                break;
            } else {
                newMoneyFlow.setTransactionDate(calendar);
            }

            String inputIncomeOrSpendMessage =
                "수입 or 지출 변경 (" + updateMoneyFlow.getIncomeOrSpend() + ") >>";
            String incomeOrSpend = PromptMoneyFlow.inputIncomeOrSpend(inputIncomeOrSpendMessage);
            newMoneyFlow.setIncomeOrSpend(incomeOrSpend);

            String inputAmountMessage = "금액 변경 (" + updateMoneyFlow.getAmount() + ") >>";
            int amount = PromptMoneyFlow.inputAmount(inputAmountMessage,
                newMoneyFlow.getIncomeOrSpend());
            newMoneyFlow.setAmount(amount);

            if (newMoneyFlow.getIncomeOrSpend().equals("지출")) {
                String inputPaymentMethodMessage =
                    "결제 수단 변경 (" + updateMoneyFlow.getPaymentMethod().toString() + ") >>";
                String paymentMethod = PromptMoneyFlow.inputPaymentMethod(
                    inputPaymentMethodMessage);
                newMoneyFlow.setPaymentMethod(paymentMethod);
            } else if (newMoneyFlow.getIncomeOrSpend().equals("수입")) {
                newMoneyFlow.setPaymentMethod("        ");
            }

            String inputCategoryMessage = "항목 변경 (" + updateMoneyFlow.getCategory() + ") >>";
            String category = PromptMoneyFlow.inputCategory(newMoneyFlow.getIncomeOrSpend(),
                inputCategoryMessage);
            newMoneyFlow.setCategory(category);

            String inputDescriptionMessage = "설명 변경 (" + updateMoneyFlow.getDescription() + ") >>";
            String description = PromptMoneyFlow.inputDescription(inputDescriptionMessage);
            newMoneyFlow.setDescription(description);

            newMoneyFlow.setNo(moneyFlowList.get(updateIndex).getNo());

            moneyFlowList.set(updateIndex, newMoneyFlow);

            moneyFlowList = sortNoByDate(moneyFlowList);
            return;
        }
        System.out.println("수정이 취소되었습니다.");
    }

    private int ofIndex(List<MoneyFlow> arrayList, MoneyFlow moneyFlow) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (moneyFlow.equals(arrayList.get(i))) {
                return i;
            }
        }
        return -1;
    }


    public List<MoneyFlow> sortNoByDate(List<MoneyFlow> moneyFlowList) {
        for (int i = 0; i < moneyFlowList.size(); i++) {
            for (int j = i + 1; j < moneyFlowList.size(); j++) {
                MoneyFlow moneyFlowA = moneyFlowList.get(i);
                MoneyFlow moneyFlowB = moneyFlowList.get(j);
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


}









