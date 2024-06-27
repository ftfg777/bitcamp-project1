package bitcamp.project1.vo;

import java.util.Date;
import bitcamp.project1.vo.Category;
import bitcamp.project1.vo.PaymentMethod;   // 임시 제목, 수정 요
import java.util.Objects;

public class MoneyFlow {
    private int index;
    private Date transactionDate;
    private int amount;
    private String incomeOrSpend;
    private Category category;
    private String note;
    private PaymentMethod paymentMethod; // 임시 제목, 수정 요

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

    public MoneyFlow(){
    }

    public MoneyFlow(int index){
        this.index = index;
    }

    public MoneyFlow(int index, Date transactionDate, int amount, String incomeOrSpend,
        Category category, String note, PaymentMethod paymentMethod) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
