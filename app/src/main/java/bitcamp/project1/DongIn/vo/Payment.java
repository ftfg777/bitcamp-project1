package bitcamp.project1.DongIn.vo;

public class Payment {

    // 결제 수단
    public enum PaymentMethod {
        CARD("  카드  "),
        CASH("  현금  "),
        OTHERS("  기타  ");

        private final String name;

        // 생성자
        PaymentMethod(String name) {
            this.name = name;
        }

        // 문자열 값을 반환하는 메서드
        public String getName() {
            return name;
        }
    }
}
