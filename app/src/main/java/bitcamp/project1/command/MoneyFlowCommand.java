package bitcamp.project1.command;

import java.util.LinkedList;

public class MoneyFlowCommand {

    LinkedList projectList = new LinkedList();
    LinkedList userList;

    public MoneyFlowCommand(LinkedList userList) {
        this.userList = userList;
    }

    private void addMembers() {

    }

    private void deleteMembers() {

    }

    public void executeProjectCommand(String command) {
        System.out.printf("[%s]\n", command);
        switch (command) {
            case "등록":
                this.addProject();
                break;
            case "조회":
                this.viewProject();
                break;
            case "목록":
                this.listProject();
                break;
            case "변경":
                this.updateProject();
                break;
            case "삭제":
                this.deleteProject();
                break;
        }
    }

    private void addProject() {
        System.out.println("생성");
    }

    private void listProject() {
        System.out.println("번호 프로젝트 기간");
    }

    private void viewProject() {
        System.out.println("조회");
    }



    private void updateProject() {
        System.out.println("수정");
    }

    private void deleteProject() {
        System.out.println("삭제");
    }

}
