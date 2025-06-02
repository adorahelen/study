package thread;

        // 자바의 모든 어플리케이션은 메인 스레드가 main() 메소드를 실행하면서 시작된다.
// 01. 싱글 스레드 애플리케이션에서는 메인 스레드가 종료하면 프로세스도 종료되지만
        // => 멀티 스레드 애플리케이션에서는 실행 중인 스레드가 하나라도 있다면, 프로스세는 종료 되지 않음
// 02. 아무튼 멀티 스레드 사용할꺼면, 몇 개의 작업을 병렬로 실행하지 결정하고 각 작업별로 스레드 생성해야함


// 1. 단일 스레드(Single Thread)
// 개념: 프로그램이 하나의 작업만 순차적으로 처리. main() 스레드 하나만 사용.
// 🔹 출력은 순서대로 실행됨, 병렬 처리가 없음.
    public class SingleThread {
        public static void main(String[] args) {
            System.out.println("작업 1 시작");
            doTask();
            System.out.println("작업 2 시작");
            doTask();
        } // 작업 1 시작 -> 0,1,2,3,4 -> 2시작 1->2,3,4

        public static void doTask() {
            for (int i = 0; i < 5; i++) {
                System.out.println("작업 중... " + i);
            }
        }

}


