package thread;

// ✅ 2. 멀티 스레드(Multi Thread)
public class MultiThread {
// 개념: 여러 스레드가 동시에 작업을 처리함 (병렬 or 동시성).
// 🔹 두 작업이 동시에 출력되며 순서는 실행 환경에 따라 달라짐

    public static void main(String[] args) {
        Runnable task1 = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("▶ Task 1: " + i);
            }
        };

        Runnable task2 = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("★ Task 2: " + i);
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();
    }
}
