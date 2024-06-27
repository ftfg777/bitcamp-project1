package bitcamp.project1.vo;

import java.util.Date;
import java.util.Objects;

public class MoneyFlow {
    private int index;
    private Date transactionDate;
    private int amount;
    private String incomeOrSpend;
    private String category;
    private String note;
    private String paymentMethod; // 임시 제목, 수정 필요

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoneyFlow moneyFlow = (MoneyFlow) o;
        return index == moneyFlow.index;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(index);
    }

    // empty MoneyFLow 생성자
    public MoneyFlow(){
    }

    // index 값만 가진 MoneyFlow 생성자
    public MoneyFlow(int index){
        this.index = index;
    }

    // 전체 값 갖는 MoneyFLow 생성자
    public MoneyFlow(int index, Date transactionDate, int amount, String incomeOrSpend,
        String category, String note, String paymentMethod) {
        this.index = index;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.incomeOrSpend = incomeOrSpend;
        this.category = category;
        this.note = note;
        this.paymentMethod = paymentMethod;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
