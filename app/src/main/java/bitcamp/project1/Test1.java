package bitcamp.project1;

public class Test1 {
    public static class Print {
        public Print() {
            System.out.println("git test임둥둥둥둥둥둥둥");
        }

        static void bug2(){
            System.out.println("dragonfly");
        }

        static void bug3(){
            System.out.println("warm");
        }

        protected void printHello() {
            System.out.println("Hello");
        }
    }

    static void bug1(){
        System.out.println("fly");
    }

    static void m1(){
        System.out.println("m1 메소드");
        Print print = new Print();
        print.printHello();
    }
}
