package bitcamp.project1.vo;

public class PaymentMethod {

    public enum PayMethod {
        CARD("  카드  "),
        CASH("  현금  "),
        TRANSFER("계좌이체"),
        OTHERS("  기타  ");

        private final String name;

        PayMethod(String name) {
            this.name = name;
        }

        // 문자열 값을 반환하는 메서드
        public String getName() {
            return name;
        }

        public static String[] toArray() {
            String[] str = new String[PayMethod.values().length];
            int i = 0;
            for (PayMethod payMethod : PayMethod.values()) {
                str[i] = payMethod.getName();
                i++;
            }
            return str;
        }

    }


}
