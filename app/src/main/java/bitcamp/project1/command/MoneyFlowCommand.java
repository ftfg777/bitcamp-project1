package bitcamp.project1.command;


import bitcamp.project1.util.MoneyFlowInterface;
import bitcamp.project1.util.Print;
import bitcamp.project1.util.Prompt;
import bitcamp.project1.util.PromptMoneyFlow;
import bitcamp.project1.vo.Category.DepositCategory;
import bitcamp.project1.vo.Category.WithdrawCategory;
import bitcamp.project1.vo.MoneyFlow;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class MoneyFlowCommand implements MoneyFlowInterface {

    static List<MoneyFlow> moneyFlowList = new ArrayList<>();

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

                System.out.println("");
                System.out.println("[System] : 작성 완료!");
                System.out.println("");

                return;
            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }
        }
        System.out.println("생성이 취소되었습니다.");

    }

    @Override
    public void executeRead() {
        String[] readMenus = {"전체", "입금", "출금", "기간"};
        String command;

        Print.printMenuWithExit("조회", readMenus);

        while (true) {
            try {
                command = Prompt.input("메인/조회 >>");
                System.out.println("");
                if (command.equals("menu")) {
                    Print.printMenuWithExit("조회", readMenus);
                } else {
                    int menuNo = Integer.parseInt(command);
                    if (menuNo == 0) {
                        break;
                    } else {
                        String menuTitle = getMenuTitle(menuNo, readMenus); // 설명하는 변수
                        if (menuTitle == null) {
                            System.out.println("유효한 메뉴 번호가 아닙니다.");
                        } else {
                            ReadCommand.processReadMenu(menuTitle);
                            return;
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                System.out.println("숫자로 메뉴 번호를 입력하세요.");
            }
        }
        System.out.println("조회가 종료되었습니다.");
    }


    boolean isValidateMenu(int menuNo, String[] menus) {
        return menuNo >= 1 && menuNo <= menus.length;
    }

    String getMenuTitle(int menuNo, String[] menus) {
        return isValidateMenu(menuNo, menus) ? menus[menuNo - 1] : null;
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
            System.out.println("");

            // 선택한 No의 MoneyFlow 선택
            MoneyFlow updateMoneyFlow = getByNo(no);

            // 선택한 No를 가진 MoneyFLow의 index
            int updateIndex = ofIndex(updateMoneyFlow);

            if (updateMoneyFlow == null) {
                System.out.println("해당 번호의 거래 내역이 없습니다.");
                continue;
            }

            // Calendar 변경
            Calendar calendar = PromptMoneyFlow.inputCalendar(updateMoneyFlow.getCalendar());
            if (calendar == null) {
                break;
            } else {
                newMoneyFlow.setTransactionDate(calendar);
            }

            // incomeOrSpend 변경
            String inputIncomeOrSpendMessage =
                "거래 유형 변경 (" + updateMoneyFlow.getIncomeOrSpend() + ") >>";
            String incomeOrSpend = PromptMoneyFlow.inputIncomeOrSpend(inputIncomeOrSpendMessage);
            if (inputIncomeOrSpendMessage.equals("종료")) {
                break;
            } else {
                newMoneyFlow.setIncomeOrSpend(incomeOrSpend);
            }

            // amount 변경
            String inputAmountMessage = "금액 변경 (" + updateMoneyFlow.getAmount() + ") >>";
            int amount = PromptMoneyFlow.inputAmount(inputAmountMessage,
                newMoneyFlow.getIncomeOrSpend());
            if (amount == 0) {
                break;
            } else {
                if (newMoneyFlow.getIncomeOrSpend().equals("수입")) {
                    newMoneyFlow.setAmount(amount);
                } else {
                    newMoneyFlow.setAmount(-amount);
                }
            }

            // paymentMethod 변경
            if (newMoneyFlow.getIncomeOrSpend().equals("지출")) {
                String inputPaymentMethodMessage =
                    "결제 수단 변경 (" + updateMoneyFlow.getPaymentMethod().toString() + ") >>";
                String paymentMethod = PromptMoneyFlow.inputPaymentMethod(
                    inputPaymentMethodMessage);
                if (paymentMethod.equals("종료")) {
                    break;
                } else {
                    newMoneyFlow.setPaymentMethod(paymentMethod);
                }
            } else if (newMoneyFlow.getIncomeOrSpend().equals("수입")) {
                newMoneyFlow.setPaymentMethod("        ");
            }

            // category 변경
            String inputCategoryMessage = "카테고리 변경 (" + updateMoneyFlow.getCategory() + ") >>";
            String category = PromptMoneyFlow.inputCategory(newMoneyFlow.getIncomeOrSpend(),
                inputCategoryMessage);
            if (category.equals("종료")) {
                break;
            } else {
                newMoneyFlow.setCategory(category);
            }

            // decription 변경
            String inputDescriptionMessage = "설명 변경 (" + updateMoneyFlow.getDescription() + ") >>";
            String description = PromptMoneyFlow.inputDescription(inputDescriptionMessage);
            newMoneyFlow.setDescription(description);

            // no 변경
            newMoneyFlow.setNo(moneyFlowList.get(updateIndex).getNo());

            // 기존 MoneyFlow 위치에 변경된 MoneyFlow 삽입
            moneyFlowList.set(updateIndex, newMoneyFlow);

            // 변경 후 재정렬
            moneyFlowList = sortNoByDate(moneyFlowList);

            System.out.println("");
            System.out.println("[System] : 수정 완료!");
            System.out.println("");
            return;
        }
        System.out.println("수정이 취소되었습니다.");
    }

    @Override
    public void executeDelete() {
        Print.printAccountBook(moneyFlowList);

        while (true) {
            try {
                int deleteNo = Prompt.inputInt("삭제 할 기록의 No [0 = 종료] >>");
                if (deleteNo == 0) {
                    break;
                } else if (0 < deleteNo && deleteNo < moneyFlowList.size() + 1) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(2101, 12, 31);

                    getByNo(deleteNo).setTransactionDate(calendar);
                    sortNoByDate(moneyFlowList);

                    moneyFlowList.remove(getByNo(moneyFlowList.size()));
                    MoneyFlow.decreaseSeqNo();

                    if (!moneyFlowList.isEmpty()) {
                        sortNoByDate(moneyFlowList);
                    }
                    if (moneyFlowList.size() == 1) {
                        moneyFlowList.getFirst().setNo(1);
                    }

                    return;
                } else {
                    System.out.println("올바르지 않은 No 입니다.");
                }

            } catch (NumberFormatException e) {
                System.out.println("숫자로 입력해주세요.");
            }
        }
        System.out.println("삭제가 종료 되었습니다.");
    }

    public static MoneyFlow addMoneyFlowWithId(MoneyFlow moneyFlow) {
        moneyFlow.setNo(MoneyFlow.getNextSeqNo());
        return moneyFlow;
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
                    System.out.println("");
                    moneyFlow.setAmount(
                        moneyFlow.getAmount() + PromptMoneyFlow.inputAmount("수입액 입력 >>"));

                    DepositCategory category = DepositCategory.values()[0];
                    Print.printCategory(category);

                    int categoryNo = PromptMoneyFlow.inputCategory(category, "카테고리 선택 >>");

                    System.out.println("");
                    if (categoryNo == 0) {
                        return 0;
                    }
                    moneyFlow.setCategory(DepositCategory.values()[categoryNo - 1].getName());

                }

                // 지출 로직
                else if (moneyFlow.getIncomeOrSpend().equals("지출")) {
                    System.out.println("");
                    moneyFlow.setAmount(
                        moneyFlow.getAmount() - PromptMoneyFlow.inputAmount("지출액 입력 >>"));

                    WithdrawCategory category = WithdrawCategory.values()[0];
                    Print.printCategory(category);
                    int categoryNo = PromptMoneyFlow.inputCategory(category, "카테고리 선택 >>");

                    System.out.println("");
                    if (categoryNo == 0) {
                        return 0;
                    }
                    moneyFlow.setCategory(WithdrawCategory.values()[categoryNo - 1].getName());

                }
                String paymentMethod = PromptMoneyFlow.inputPaymentMethod("결제 수단 선택 >>");

                if (paymentMethod.equals("종료")) {
                    return 0;
                }
                moneyFlow.setPaymentMethod(paymentMethod);

                return 1;
            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }
        }
    }

    public static List<MoneyFlow> sortNoByDate(List<MoneyFlow> moneyFlowList) {
        for (int i = 0; i < moneyFlowList.size(); i++) {
            for (int j = i + 1; j < moneyFlowList.size(); j++) {
                MoneyFlow moneyFlowA = moneyFlowList.get(i);
                MoneyFlow moneyFlowB = moneyFlowList.get(j);
                if (moneyFlowA.getCalendar().after(moneyFlowB.getCalendar())) {
                    int tempNo = moneyFlowA.getNo();
                    moneyFlowA.setNo(moneyFlowB.getNo());
                    moneyFlowB.setNo(tempNo);
                    Collections.swap(moneyFlowList, i, j);
                }
            }
        }
        return moneyFlowList;
    }

    public MoneyFlow getByNo(int no) {
        for (int i = 0; i < moneyFlowList.size(); i++) {
            if (moneyFlowList.get(i).getNo() == no) {
                return moneyFlowList.get(i);
            }
        }
        return null;
    }

    private int ofIndex(MoneyFlow moneyFlow) {
        for (int i = 0; i < moneyFlowList.size(); i++) {
            if (moneyFlow.equals(moneyFlowList.get(i))) {
                return i;
            }
        }
        return -1;
    }
}









