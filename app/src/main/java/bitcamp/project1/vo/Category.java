package bitcamp.project1.vo;

public class Category {

    // 입금 카테고리
    public enum DepositCategory {
        SALARY("월급"),
        POCKETMONEY("용돈"),
        OTHERS("기타");

        private final String name;

        // 생성자
        DepositCategory(String koreanName) {
            this.name = koreanName;
        }
        // 문자열 값을 반환하는 메서드
        public String getName() {
            return name;
        }
    }

    // 출금 카테고리
    public enum WithdrawCategory {
        SHOPPING("쇼핑"),
        LIVING_SUPPLIES("생활용품"),
        FOOD("음식"),
        ENTERTAINMENT("유흥"),
        TRANSPORT("교통"),
        HOBBY("취미"),
        UTILITIES("공과금"),
        BEAUTY("미용"),
        OTHERS("기타");

        private final String name;

        // 생성자
        WithdrawCategory(String name) {
            this.name = name;
        }
        // 문자열 값을 반환하는 메서드
        public String getName() {
            return name;
        }
    }


}
