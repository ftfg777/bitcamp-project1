package bitcamp.project1.command;

import bitcamp.project1.vo.Category.DepositCategory;
import bitcamp.project1.vo.Category.WithdrawCategory;
import bitcamp.project1.vo.MoneyFlow;
import java.util.Calendar;

public class TestCommand {

    static Calendar[] calendars = {Calendar.getInstance(), Calendar.getInstance(),
        Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
        Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
        Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
        Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
        Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(),
        Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance()};

    static int[] amounts = {1000000, 2000000, 3000000, 550000, 450000, 220000,
        600000, 580000, 54000, 44000, 22000, 120000, 520000,
        500000, 240000, 220000, 5522000, 2204022, 5500220, 500000};

    static String[] incomeOrSpends = {"수입", "수입", "수입", "수입", "수입", "수입",
        "지출", "지출", "지출", "지출", "지출", "지출",
        "수입", "수입", "수입", "지출", "지출", "지출", "수입", "지출"};

    static String[] descriptionForIncome = {"내 소중한 돈", "굴러 들어온 돈", "없음"};
    static String[] descriptionForSpend = {"충동", "언제 썼지", "없음"};


    static String[] category = new String[20];
    static String[] description = new String[20];
    static Object[] paymentMethod = new Object[20];

    public static void addListForTest() {
        System.out.println("Test Command");

        calendars[0].set(2023, 6, 2);
        calendars[1].set(2023, 6, 18);
        calendars[2].set(2023, 8, 20);
        calendars[3].set(2023, 9, 1);
        calendars[4].set(2023, 10, 2);
        calendars[5].set(2023, 10, 8);
        calendars[6].set(2023, 11, 2);
        calendars[7].set(2023, 11, 10);
        calendars[8].set(2024, 0, 1);
        calendars[9].set(2024, 1, 16);
        calendars[10].set(2024, 2, 20);
        calendars[11].set(2024, 2, 22);
        calendars[12].set(2024, 3, 10);
        calendars[13].set(2024, 5, 12);
        calendars[14].set(2024, 5, 14);
        calendars[15].set(2024, 5, 18);
        calendars[16].set(2024, 5, 20);
        calendars[17].set(2024, 5, 22);
        calendars[18].set(2024, 5, 23);
        calendars[19].set(2024, 5, 24);

        for (int i = 0; i < 20; i++) {
            if (incomeOrSpends[i] == "수입") {
                category[i] = DepositCategory.values()[getRandomNumber(0, 2)].getName();
                description[i] = descriptionForIncome[getRandomNumber(0, 2)];
                paymentMethod[i] = i % 3 == 1 ? "  현금  " : i % 3 == 2 ? "계좌이체" : "  기타  ";
            } else {
                amounts[i] = -amounts[i];
                category[i] = WithdrawCategory.values()[getRandomNumber(0, 8)].getName();
                description[i] = descriptionForSpend[getRandomNumber(0, 2)];
                paymentMethod[i] = i % 2 == 1 ? "  카드  " : "  현금  ";
            }
            MoneyFlow newMoneyFlow = new MoneyFlow(calendars[i], amounts[i], incomeOrSpends[i],
                category[i], description[i], paymentMethod[i]);
            MoneyFlowCommand.addMoneyFlowWithId(newMoneyFlow);
            MoneyFlowCommand.moneyFlowList.add(newMoneyFlow);
        }

        MoneyFlowCommand.sortNoByDate(MoneyFlowCommand.moneyFlowList);

        System.out.println("Complete!!!");
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
