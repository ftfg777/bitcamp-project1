package bitcamp.project1.DongIn.vo;

import java.util.Date;
import java.util.Objects;

public class MoneyFlow {

    private int index;                      // 인덱스
    private Date transactionDate;           // 날짜
    private int amount;                     // 금액
    private String incomeOrSpend;           // 수입 지출 구분
    private String category;                // 카테고리
    private String note;                    // 설명
    private String paymentMethod;           // 결제수단


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

    public MoneyFlow() {
    }

    public MoneyFlow(int index) {
        this.index = index;
    }

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
        return this.transactionDate;
    }

    public String getTransactionDateByString() {
        return String.format("%4d-%02d-%02d", this.transactionDate.getYear() + 1900,
            this.transactionDate.getMonth() + 1, this.transactionDate.getDate());
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

    public Object getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
