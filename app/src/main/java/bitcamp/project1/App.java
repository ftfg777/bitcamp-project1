package bitcamp.project1;

import bitcamp.project1.command.MoneyFlowCommand;
import bitcamp.project1.util.Prompt;

public class App {

    static String[] menus = new String[]{"작성", "조회", "수정", "삭제", "종료"};

    MoneyFlowCommand moneyFlowCommand = new MoneyFlowCommand();

    public static void main(String[] args) {
        new App().execute();
    }


    void execute() {
        printMenu();

        String command;
        while (true) {
            try {
                System.out.println();
                command = Prompt.input("메인 >>");
                if (command.equals("menu")) {
                    printMenu();
                } else {
                    int menuNo = Integer.parseInt(command);
                    String menuTitle = getMenuTitle(menuNo, menus); // 설명하는 변수
                    if (menuTitle == null) {
                        System.out.println("유효한 메뉴 번호가 아닙니다.");
                    } else if (menuTitle.equals("종료")) {
                        break;
                    } else {
                        processMenu(menuTitle);
                    }
                }
            } catch (NumberFormatException ex) {
                System.out.println("숫자로 메뉴 번호를 입력하세요.");
            }
        }

        System.out.println("종료합니다.");
        Prompt.close();

    }


    private void printMenu() {
        for (int i = 0; i < menus.length; i++) {
            String menu = menus[i];
            System.out.printf("%d. %s\n", i + 1, menu);
        }
    }

    boolean isValidateMenu(int menuNo, String[] menus) {
        return menuNo >= 1 && menuNo <= menus.length;
    }

    String getMenuTitle(int menuNo, String[] menus) {
        return isValidateMenu(menuNo, menus) ? menus[menuNo - 1] : null;
    }


    void processMenu(String menuTitle) {
        try {
            switch (menuTitle) {
                case "작성":
                    moneyFlowCommand.executeCreate();
                    break;
                case "조회":
                    moneyFlowCommand.executeRead();
                    break;
                case "수정":
                    moneyFlowCommand.executeUpdate();
                    break;
                case "삭제":
                    moneyFlowCommand.executeDelete();
                    break;
                default:
                    System.out.printf("%s 메뉴의 명령을 처리할 수 없습니다.\n", menuTitle);
            }
        } catch (NumberFormatException ex) {
            System.out.println("숫자로 메뉴 번호를 입력하세요.");
        }

    }


}
