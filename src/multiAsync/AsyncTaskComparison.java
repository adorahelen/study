package multiAsync;

import java.util.concurrent.*; // ExecutorService, Callable, Future, CompletableFuture, Executors

public class AsyncTaskComparison {

    public static void main(String[] args) throws Exception {
        System.out.println("--- Java 비동기 멀티 스레드 프로그래밍 예시 ---");
        System.out.println("현재 스레드: " + Thread.currentThread().getName() + "\n");

        // -----------------------------------------------------------
        System.out.println("🔸 ExecutorService + Callable + Future 방식 시작");
        runExecutorServiceExample();
        System.out.println("🔸 ExecutorService + Callable + Future 방식 종료\n");

        // -----------------------------------------------------------
        System.out.println("🔸 CompletableFuture (Java 8+) 방식 시작");
        runCompletableFutureExample();
        System.out.println("🔸 CompletableFuture (Java 8+) 방식 종료\n");

        System.out.println("--- 모든 비동기 작업 예시 완료 ---");
    }

    /**
     * ExecutorService + Callable + Future 방식을 실행하는 메서드
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static void runExecutorServiceExample() throws InterruptedException, ExecutionException {
        // 3개의 스레드를 가진 스레드 풀 생성
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Callable 작업 정의: 결과를 반환하고 예외를 던질 수 있는 작업
        Callable<String> task1 = () -> {
            System.out.println("  [Executor] 작업 1 시작 - 스레드: " + Thread.currentThread().getName());
            Thread.sleep(1000); // 1초 대기
            System.out.println("  [Executor] 작업 1 종료 - 스레드: " + Thread.currentThread().getName());
            return "✅ 작업 1 완료";
        };

        Callable<String> task2 = () -> {
            System.out.println("  [Executor] 작업 2 시작 - 스레드: " + Thread.currentThread().getName());
            Thread.sleep(2000); // 2초 대기
            System.out.println("  [Executor] 작업 2 종료 - 스레드: " + Thread.currentThread().getName());
            return "✅ 작업 2 완료";
        };

        Callable<String> task3 = () -> {
            System.out.println("  [Executor] 작업 3 시작 - 스레드: " + Thread.currentThread().getName());
            Thread.sleep(1500); // 1.5초 대기
            System.out.println("  [Executor] 작업 3 종료 - 스레드: " + Thread.currentThread().getName());
            return "✅ 작업 3 완료";
        };

        // 작업 제출 및 Future 객체 반환
        System.out.println("  모든 Callable 작업 제출 (비동기 시작)");
        Future<String> future1 = executor.submit(task1);
        Future<String> future2 = executor.submit(task2);
        Future<String> future3 = executor.submit(task3);

        // Future.get()을 통해 각 작업의 결과 대기 및 출력 (블로킹 호출)
        // 작업 완료 순서와 상관없이 get() 호출 순서대로 대기
        System.out.println("  결과를 기다리는 중...");
        System.out.println("  " + future1.get()); // 작업 1이 끝날 때까지 대기
        System.out.println("  " + future2.get()); // 작업 2가 끝날 때까지 대기 (작업 1이 먼저 끝났더라도)
        System.out.println("  " + future3.get()); // 작업 3이 끝날 때까지 대기

        // ExecutorService 종료 (남아있는 작업들을 처리하고 스레드 풀 종료)
        executor.shutdown();
        try {
            // 모든 작업이 완전히 종료될 때까지 최대 5초 대기
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("  스레드 풀이 5초 안에 종료되지 않아 강제 종료 시도...");
                executor.shutdownNow(); // 즉시 종료
            }
        } catch (InterruptedException e) {
            System.err.println("  스레드 풀 종료 대기 중 인터럽트 발생: " + e.getMessage());
            executor.shutdownNow();
        }
    }

    /**
     * CompletableFuture 방식을 실행하는 메서드
     * @throws Exception
     */
    public static void runCompletableFutureExample() throws Exception {
        // CompletableFuture.supplyAsync로 비동기 작업 시작
        // supplyAsync는 내부적으로 ForkJoinPool.commonPool()을 사용하거나
        // Executor를 지정할 수 있습니다.
        System.out.println("  모든 CompletableFuture 작업 제출 (비동기 시작)");
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("  [Completable] Task 1 시작 - 스레드: " + Thread.currentThread().getName());
            sleep(1000);
            System.out.println("  [Completable] Task 1 종료 - 스레드: " + Thread.currentThread().getName());
            return "🔹 Task 1 완료";
        });

        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("  [Completable] Task 2 시작 - 스레드: " + Thread.currentThread().getName());
            sleep(2000);
            System.out.println("  [Completable] Task 2 종료 - 스레드: " + Thread.currentThread().getName());
            return "🔹 Task 2 완료";
        });

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("  [Completable] Task 3 시작 - 스레드: " + Thread.currentThread().getName());
            sleep(1500);
            System.out.println("  [Completable] Task 3 종료 - 스레드: " + Thread.currentThread().getName());
            return "🔹 Task 3 완료";
        });

        // CompletableFuture.allOf: 모든 CompletableFuture들이 완료될 때까지 대기하는 CompletableFuture를 반환
        System.out.println("  모든 CompletableFuture가 완료될 때까지 대기 중...");
        CompletableFuture<Void> allDone = CompletableFuture.allOf(task1, task2, task3);
        allDone.join(); // 모든 작업이 끝날 때까지 대기 (예외 없이, Unchecked)

        // 모든 작업이 완료된 후 각 작업의 결과 출력
        // 이때는 이미 작업이 완료되었으므로 get()은 블로킹하지 않습니다 (거의 즉시 반환).
        System.out.println("  모든 CompletableFuture 작업 완료. 결과 출력:");
        System.out.println("  " + task1.get());
        System.out.println("  " + task2.get());
        System.out.println( "  " + task3.get());
    }

    // 간단한 sleep 유틸리티 메서드
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            // 스레드 인터럽트 발생 시, 현재 스레드의 인터럽트 상태를 다시 설정
            Thread.currentThread().interrupt();
        }
    }
}
