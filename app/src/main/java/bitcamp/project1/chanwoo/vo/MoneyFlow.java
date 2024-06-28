package bitcamp.project1.chanwoo.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MoneyFlow {

    private static int seqNo;

    private int no;                              // 인덱스
    private Calendar transactionDate;           // 날짜
    private int amount;                       // 금액
    private String incomeOrSpend;                // 수입 지출 구분
    private String category;                     // 카테고리
    private String description;                  // 설명
    private Object paymentMethod;                // 결제수단


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoneyFlow moneyFlow = (MoneyFlow) o;
        return no == moneyFlow.no;
    }

    @Override
    public String toString() {
        return "MoneyFlow{" +
            "no=" + no +
            ", transactionDate=" + getTransactionDate() +
            ", amount=" + amount +
            ", incomeOrSpend='" + incomeOrSpend + '\'' +
            ", category='" + category + '\'' +
            ", description='" + description + '\'' +
            ", paymentMethod=" + paymentMethod +
            '}';
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(no);
    }

    public MoneyFlow() {
    }

    public MoneyFlow(int no) {
        this.no = no;
    }

    public MoneyFlow(Calendar transactionDate, int amount, String incomeOrSpend,
        String category, String description, Object paymentMethod) {
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.incomeOrSpend = incomeOrSpend;
        this.category = category;
        this.description = description;
        this.paymentMethod = paymentMethod;
    }

    public static int getNextSeqNo() {
        return ++seqNo;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTransactionDate() {
        Date date = transactionDate.getTime();

        // 원하는 형식으로 포맷팅
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 결과 출력
        return dateFormat.format(date);
    }

    public void setTransactionDate(Calendar transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getIncomeOrSpend() {
        return incomeOrSpend;
    }

    public void setIncomeOrSpend(String incomeOrSpend) {
        this.incomeOrSpend = incomeOrSpend;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public String setDescription(String description) {
        this.description = description;
        return description;
    }

    public Object getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Object paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


}
