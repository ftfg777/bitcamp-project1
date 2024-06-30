package bitcamp.project1.command;


import bitcamp.project1.util.MoneyFlowInterface;
import bitcamp.project1.util.Print;
import bitcamp.project1.util.Prompt;
import bitcamp.project1.util.PromptMoneyFlow;
import bitcamp.project1.vo.Category.DepositCategory;
import bitcamp.project1.vo.Category.WithdrawCategory;
import bitcamp.project1.vo.MoneyFlow;
import java.util.Comparator;
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

                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // 읽은 후 삭제 혹은 답변 부탁드릴게요!
                // 변경된 로직 (240629 by 동인) 컨펌 후 주석 제거
                // 여기부터 Calendar inputCalendar() 로 바꿔도 될 듯 해서 메서드화 했습니다!
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                Calendar calendar = PromptMoneyFlow.inputCalendar(Calendar.getInstance());
                if (calendar == null) break;
                else moneyFlow.setTransactionDate(calendar);


                int result = processTransactionType(moneyFlow); // 수입 | 지출 선택
                if (result == 0) break;
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // 읽은 후 삭제 혹은 답변 부탁드릴게요!
                // 각 입력들마다 종료를 위한 반환값을 주거나, 내부에서 탈출할 수 있게
                // 모든 입력 탈출부분을 통일하고싶어요! 이건 같이 생각해봐요 ㅎㅎ
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                // 메모 입력 및 세팅
                String description = PromptMoneyFlow.inputTransactionDescription("메모 입력 >>", moneyFlow); // 메모 입력
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

        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // 읽은 후 삭제 혹은 답변 부탁드릴게요!
        // (0) 예외 처리 필요할 거 같습니다!
        //
        // -> 혹시 이건 throw 되는 error에 대한 대응 말씀하시는 걸까요?
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        while (true) {
            int no = Prompt.inputInt("수정 할 기록의 No [0 = 종료] >>");
            if (no == 0) break;

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

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // 읽은 후 삭제 혹은 답변 부탁드릴게요!
            // (1) 직접적인 날짜 수정은 막고 수정을 하고 싶으면 삭제하거나 새로 만드는 걸로 하는 게
            // 어떨까 싶습니다!
            //
            // -> 3번 주석 답변처럼, 생성자를 사용해 아예 새로운 객체를 생성하는 방식을 사용할
            // 예정이라 삭제 및 새로 만들기 개념이 될 것 같습니다!
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Calendar calendar = PromptMoneyFlow.inputCalendar(updateMF.getCalendar());
            if (calendar == null) break;


            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // 읽은 후 삭제 혹은 답변 부탁드릴게요!
            // (2) 이 부분 가독성이
            //
            //  -> 깰꼼하게 바꿔놓을게요! ㅋㅋㅋㅋ
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            String inputIncomeOrSpendMessage = "수입 or 지출 변경 (" + updateMF.getIncomeOrSpend() + ") >>";
            String incomeOrSpend = PromptMoneyFlow.inputIncomeOrSpend(inputIncomeOrSpendMessage);

            String inputAmountMessage = "금액 변경 (" + updateMF.getAmount() + ") >>";
            int amount = PromptMoneyFlow.inputAmount(inputAmountMessage);

            String inputPaymentMethodMessage = "결제 수단 변경 (" + updateMF.getPaymentMethod().toString() + ") >>";
            String paymentMethod = PromptMoneyFlow.inputPaymentMethod(inputPaymentMethodMessage);

            String inputCategoryMessage = "항목 변경 (" + updateMF.getCategory() + ") >>";
            String category = PromptMoneyFlow.inputCategory(updateMF.getIncomeOrSpend(), inputCategoryMessage);

            String inputDescriptionMessage = "설명 변경 (" + updateMF.getDescription() + ") >>";
            String description = PromptMoneyFlow.inputDescription(inputDescriptionMessage);

            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // 읽은 후 삭제 혹은 답변 부탁드릴게요!
            // (3) 생성자로 수정을 하면 기존 객체가 아닌 힙영역에 새롭게 생성되는 객체로 하시려는
            // 의도가 맞는지 궁금합니다~!
            //
            //  -> 넵 맞습니다. 그런 방식을 채택하고 있긴 했는데, 혹시나 setter로 수정하는 방식을
            //  원하시면 바꿀 순 있습니다. 다만, 다른 입력은 setter로 변경하고
            //  날짜 입력은 삭제 및 새로 만드는 방식을 채택하게 된다면 날짜 변경 시에는
            //  1. 유저가 직접 삭제 후 새로 만들기 (no가 바뀌는 문제가 있음)
            //  2. 생성자를 사용해 삭제와 새로 만들기를 코드 내에서 진행 (기존 방식과 크게 달라질 점이 없음)
            //  이런 문제가 예상되어서 그런 문제가 상관 없다면 바꿀 수 있습니다.
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            MoneyFlow updatedMoneyFlow = new MoneyFlow(calendar, amount,
                incomeOrSpend, category, description, paymentMethod);

            updatedMoneyFlow.setNo(((MoneyFlow)moneyFlowList.get(updateIndex)).getNo());

            moneyFlowList.set(updateIndex, updatedMoneyFlow);

            moneyFlowList = sortNoByDate((ArrayList) moneyFlowList);
            return;
        }
        System.out.println("수정이 취소되었습니다.");
    }

    private int ofIndex(ArrayList arrayList, MoneyFlow moneyFlow) {
        for (int i = 0; i < arrayList.size(); i++) {
            if(moneyFlow.equals(arrayList.get(i))){
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

    public MoneyFlow getByNo(int no){
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
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // 읽은 후 삭제 혹은 답변 부탁드릴게요!
                // incomeOrSpend를 String 반환형의 inputIncomeOrSpend로 변경하였고
                // 값을 받은 뒤에 set 했습니다.
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                String incomeOrSpend = PromptMoneyFlow.inputIncomeOrSpend("거래 유형 선택 >>");
                if (incomeOrSpend.equals("종료")) return 0;
                else moneyFlow.setIncomeOrSpend(incomeOrSpend);


                // 수입 로직
                if (moneyFlow.getIncomeOrSpend().equals("수입")) {

                    // ************************
                    // start of setting amount
                    int depositAmount = Prompt.inputInt("수입액 입력 >>");
                    if (depositAmount <= 0) {
                        System.out.println("0보다 큰 금액을 입력해 주세요.");
                        continue;
                    }

                    moneyFlow.setAmount(moneyFlow.getAmount() + depositAmount);
                    // end of setting amount
                    // ************************

                    // ************************
                    // start of setting category
                    Print.printCategory(DepositCategory.values()[0]);
                    int categoryNo = 0;
                    while (true) {
                        categoryNo = Prompt.inputInt("카테고리 선택 >>");
                        if (!PromptMoneyFlow.isInRange(categoryNo, 0, DepositCategory.values().length)) {
                            System.out.println("유효한 카테고리 번호를 입력해 주세요.");
                            continue;
                        }
                        break;
                    }

                    moneyFlow.setCategory(DepositCategory.values()[categoryNo - 1].getName());
                    // end of setting category
                    // ************************

                    // set paymentMethod to "          " because its income
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
                    // end of setting amount
                    // ************************

                    // ************************
                    // start of setting category
                    Print.printCategory(WithdrawCategory.values()[0]);
                    int categoryNo = 0;
                    while (true) {
                        categoryNo = Prompt.inputInt("카테고리 선택:");
                        if (!PromptMoneyFlow.isInRange(categoryNo, 0, WithdrawCategory.values().length)) {
                            System.out.println("유효한 카테고리 번호를 입력해 주세요.");
                            continue;
                        }
                        break;
                    }
                    moneyFlow.setCategory(WithdrawCategory.values()[categoryNo - 1].getName());
                    // end of setting category
                    // ************************

                    // set paymentMethod by user's select because its spend
                    moneyFlow.setPaymentMethod(PromptMoneyFlow.inputPaymentMethod("결제 수단 >>"));
                }
                return 1;
            } catch (NumberFormatException e) {
                System.out.println("유효한 값이 아닙니다.");
            }
        }

    }













}









