

# 1. 스레드 개수 

### 1. 고정 스레드 풀 (FixedThreadPool)
* **생성 메서드:** `Executors.newFixedThreadPool(int nThreads)`
* **특징:**
    * `nThreads`로 지정된 **고정된 수의 스레드를 유지**합니다.
    * 새로운 작업이 들어오면 사용 가능한 스레드가 작업을 처리하고, 모든 스레드가 바쁘면 작업은 큐(Queue)에 대기합니다.
    * 스레드 수가 변하지 않으므로 자원 사용량 예측이 용이하고, 서버에 과부하가 걸리는 것을 방지할 수 있습니다.
* **용도:** 동시 작업 수가 제한되어 있거나, 서버의 스레드 자원을 안정적으로 관리해야 할 때 유용합니다. (예: 웹 서버의 요청 처리)

### 2. 캐시된 스레드 풀 (CachedThreadPool)
* **생성 메서드:** `Executors.newCachedThreadPool()`
* **특징:**
    * 필요에 따라 **새로운 스레드를 생성**하거나, 이전에 생성된 스레드를 **재사용**합니다.
    * 스레드 수가 고정되어 있지 않고, 작업 요청에 따라 스레드 수가 동적으로 늘어납니다.
    * 오랫동안 유휴(idle) 상태인 스레드는 자동으로 종료됩니다 (기본 60초).
    * 작업 처리가 빠르지만, 제어되지 않는 작업 부하 시 스레드가 무한정 증가하여 시스템 자원을 고갈시킬 수 있습니다.
* **용도:** 짧은 시간 내에 급증하는 많은 수의 비동기 작업을 처리해야 할 때, 또는 작업의 수와 지속 시간을 예측하기 어려울 때 유용합니다. (예: 간헐적으로 발생하는 많은 작은 작업들)

### 3. 단일 스레드 Executor (SingleThreadExecutor)
* **생성 메서드:** `Executors.newSingleThreadExecutor()`
* **특징:**
    * **단 하나의 스레드만** 사용하여 작업을 순차적으로 실행합니다.
    * 모든 작업이 제출된 순서대로 실행됨을 보장합니다.
    * 스레드 자체에 문제가 생기면 새로운 스레드를 생성하여 대체합니다.
* **용도:** 모든 작업이 순서대로 실행되어야 함을 보장해야 하거나, 특정 순서가 중요한 작업을 처리할 때 사용합니다. (예: 이벤트 로거, 중요한 순차적 데이터 처리)

### 4. 스케줄된 스레드 풀 (ScheduledThreadPool)
* **생성 메서드:** `Executors.newScheduledThreadPool(int corePoolSize)`
* **특징:**
    * 지정된 지연 시간 후에 실행되거나 주기적으로 반복 실행되는 작업을 처리할 수 있습니다.
    * 고정된 코어 스레드 수를 가집니다.
* **용도:** 특정 시간에 예약된 작업 (cron job 유사), 주기적으로 반복되는 작업 (예: 주기적인 데이터 동기화, 알림 발송)을 실행할 때 사용합니다.

### 5. 포크/조인 풀 (ForkJoinPool)
* **생성 메서드:** `new ForkJoinPool()` (`Executors` 팩토리 메서드로는 직접 제공되지 않음)
* **특징:**
    * 분할 정복(Divide and Conquer) 알고리즘을 효율적으로 구현하기 위해 설계된 특수 스레드 풀입니다.
    * 작업을 작은 단위로 분할하여 병렬로 실행하고, 그 결과를 합치는 방식으로 동작합니다.
    * `CompletableFuture`의 기본 스레드 풀로 사용되거나, Java 8의 병렬 스트림(`parallelStream()`)에서 내부적으로 사용됩니다.
    * 작업 훔치기(work-stealing) 알고리즘을 사용하여 스레드 활용률을 최적화합니다.
* **용도:** 대규모 병렬 계산, 재귀적인 알고리즘 등에서 성능 향상을 위해 사용됩니다.

# 02. 스레드 구현 방법 

## `Callable`과 `Runnable`.

### 1. 반환 값 (Return Value)

* **`Runnable`:**
    * 반환 값이 없습니다. `void run()` 메서드를 구현합니다.
    * `Thread` 클래스의 생성자나 `ExecutorService`의 `execute()` 메서드에 전달될 때 사용됩니다.
    * 단순히 어떤 작업을 실행하고 싶을 때 사용합니다.
    * **예시:** 로깅, 파일 쓰기, 계산 결과를 별도로 저장하는 등 작업 후 특정 값을 반환할 필요가 없는 경우.

* **`Callable`:**
    * 값을 반환할 수 있습니다. `V call()` 메서드를 구현하며, `V`는 반환될 값의 타입입니다.
    * `ExecutorService`의 `submit()` 메서드에 전달될 때 사용됩니다.
    * 작업이 완료된 후 결과를 받아 처리해야 할 때 유용합니다.
    * `Future` 객체를 통해 반환 값을 얻을 수 있습니다.
    * **예시:** 데이터베이스 쿼리 실행 후 결과 집합 반환, 복잡한 계산 수행 후 결과 값 반환 등 작업 후 특정 값을 반환해야 하는 경우.

### 2. 예외 처리 (Exception Handling)

* **`Runnable`:**
    * `run()` 메서드는 `throws` 절을 가질 수 없으므로, 체크드 예외(Checked Exception)를 던질 수 없습니다.
    * 체크드 예외가 발생하면 `try-catch` 블록으로 내부에서 처리해야 합니다.
    * 언체크드 예외(Unchecked Exception, `RuntimeException` 등)는 스레드 내에서 발생하며, 기본적으로 해당 스레드를 종료시키지만, `UncaughtExceptionHandler`를 통해 처리할 수 있습니다.

* **`Callable`:**
    * `call()` 메서드는 `throws Exception`을 선언하고 있으므로, 체크드 예외를 던질 수 있습니다.
    * 던져진 예외는 `Future.get()` 메서드를 호출할 때 `ExecutionException`으로 래핑되어 던져집니다. 이를 통해 외부에서 예외를 보다 쉽게 처리할 수 있습니다.

### 3. 인터페이스 시그니처 (Interface Signature)

* **`Runnable`:**
    ```java
    @FunctionalInterface
    public interface Runnable {
        public abstract void run();
    }
    ```

* **`Callable`:**
    ```java
    @FunctionalInterface
    public interface Callable<V> {
        V call() throws Exception;
    }
    ```

### 4. 주로 사용되는 곳

* **`Runnable`:**
    * `Thread` 클래스의 생성자 인자로 직접 스레드를 생성할 때.
    * `ExecutorService.execute(Runnable task)` 메서드 (결과를 기다리지 않고 단순히 작업을 실행할 때).

* **`Callable`:**
    * `ExecutorService.submit(Callable<T> task)` 메서드 (결과를 얻거나 예외를 처리해야 할 때).
    * `Future` 객체와 함께 사용되어 비동기 작업의 결과를 추적하고 가져올 때.

### 요약 비교표

| 특징         | `Runnable`                       | `Callable`                         |
| :----------- | :------------------------------- | :--------------------------------- |
| **반환 값** | 없음 (`void run()`)              | 있음 (`V call()`)                  |
| **예외 처리** | 체크드 예외 던질 수 없음, 내부 처리 | 체크드 예외 던질 수 있음 (`throws Exception`) |
| **메서드** | `run()`                          | `call()`                           |
| **사용처** | `Thread`, `ExecutorService.execute()` | `ExecutorService.submit()`, `Future` |

### 예제 코드

**Runnable 예시:**

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyRunnable implements Runnable {
    private String message;

    public MyRunnable(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": " + message);
        try {
            Thread.sleep(1000); // 1초 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 스레드 인터럽트 상태 복원
            System.err.println(Thread.currentThread().getName() + " was interrupted.");
        }
        System.out.println(Thread.currentThread().getName() + ": " + message + " finished.");
    }
}

public class RunnableExample {
    public static void main(String[] args) {
        // 1. Thread 클래스로 직접 실행
        Thread thread1 = new Thread(new MyRunnable("Hello from Thread 1"));
        thread1.start();

        // 2. ExecutorService를 통해 실행
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(new MyRunnable("Task 1 from Executor"));
        executor.execute(new MyRunnable("Task 2 from Executor"));

        executor.shutdown(); // ExecutorService 종료
    }
}
```

**Callable 예시:**

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

class MyCallable implements Callable<String> {
    private int number;

    public MyCallable(int number) {
        this.number = number;
    }

    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName() + ": Calculating square of " + number);
        Thread.sleep(2000); // 2초 대기
        if (number < 0) {
            throw new IllegalArgumentException("Number must be positive!");
        }
        int result = number * number;
        return "Square of " + number + " is " + result;
    }
}

public class CallableExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Callable 작업 제출
        Future<String> future1 = executor.submit(new MyCallable(5));
        Future<String> future2 = executor.submit(new MyCallable(10));
        Future<String> future3 = executor.submit(new MyCallable(-3)); // 예외 발생 시킬 작업

        try {
            // 작업 결과 가져오기 (작업이 완료될 때까지 블록킹)
            System.out.println(future1.get()); // "Square of 5 is 25"
            System.out.println(future2.get()); // "Square of 10 is 100"
            
            // 예외가 발생하는 경우 처리
            System.out.println(future3.get()); // ExecutionException이 발생
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Task was interrupted: " + e.getMessage());
        } catch (ExecutionException e) {
            // Callable에서 던져진 예외를 여기서 catch
            System.err.println("Task threw an exception: " + e.getCause().getMessage());
        } finally {
            executor.shutdown(); // ExecutorService 종료
        }
    }
}
```

이 두 인터페이스는 자바에서 비동기 작업을 다룰 때 상황과 필요에 따라 적절하게 선택하여 사용됩니다. 결과를 받거나 예외 처리가 중요한 작업에는 `Callable`을, 단순히 작업을 실행하는 경우에는 `Runnable`을 사용하는 것이 일반적입니다.

