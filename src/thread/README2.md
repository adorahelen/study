

# 자바 스레드 


---

#### ✅ 1. 동기(Synchronous) vs 비동기(Asynchronous)의 정확한 의미

* **동기(Synchronous):**
    * 작업이 순차적으로 실행됩니다.
    * 하나의 작업이 완료되어야만 다음 작업이 시작될 수 있습니다.
    * 예: 함수 호출 후 결과가 반환될 때까지 대기.
* **비동기(Asynchronous):**
    * 작업이 동시에(혹은 병렬적으로) 실행될 수 있습니다.
    * 현재 작업의 결과를 기다리지 않고 다음 작업을 수행할 수 있습니다.
    * 예: 네트워크 요청을 보내고 응답을 기다리는 동안 다른 작업을 수행.

#### ✅ 2. 멀티스레드 환경의 문제

공유 자원 접근 시 예측 불가능한 문제가 발생할 수 있습니다.

* **Race Condition (경쟁 상태):**
    * 두 개 이상의 스레드가 동시에 공유 변수나 자원에 접근하여 최종 결과가 접근 순서에 따라 달라지는 상황입니다.
    * 결과를 예측하기 어렵게 만듭니다.
* **Deadlock (교착 상태):**
    * 두 스레드(이상)가 서로가 점유하고 있는 자원을 얻기 위해 영원히 대기하는 상태입니다.
    * 시스템이 멈춘 것처럼 보일 수 있습니다.
* **Starvation (기아 상태):**
    * 특정 스레드가 자원을 계속해서 얻지 못하고 작업을 수행하지 못하는 상태입니다.
    * 우선순위가 낮은 스레드가 자주 겪을 수 있습니다.
* **Livelock (활동성 상태):**
    * 데드락과 비슷하게 작업을 진행하지 못하지만, 스레드들이 서로의 상태 변화에 반응하며 계속해서 상태를 바꾸는 상황입니다. (예: 서로 비켜주려다가 계속 같은 자리에서 부딪히는 경우)

#### ✅ 3. 동기화 (Synchronized 키워드)

자바에서 공유 자원 접근을 제어하는 가장 기본적인 방법입니다.

* **Method-level 동기화:**
    * `public synchronized void methodName() { /* ... */ }`
    * 해당 메서드가 속한 객체의 Lock을 획득합니다. (정적 메서드의 경우 클래스 Lock)
    * 한 번에 하나의 스레드만 해당 메서드를 실행할 수 있습니다.
* **Block-level 동기화:**
    * `synchronized(obj) { /* critical section */ }`
    * `obj` 객체의 Lock을 획득하며, 중괄호 `{}` 안의 코드 블록(critical section)만 보호합니다.
    * 메서드 전체보다 더 세밀한 제어가 가능하여 성능상 이점을 얻을 수 있습니다.
* **장점:** 스레드 안전성을 보장합니다.
* **단점:** 과도한 사용은 성능 저하(병목 현상)를 유발할 수 있습니다.

#### ✅ 4. `java.util.concurrent` 패키지 (Java 동시성 API)

실무에서는 `synchronized` 키워드보다 더 유연하고 고성능의 동시성 API 사용을 선호하는 경우가 많습니다.

* **`Lock` 인터페이스 (ReentrantLock, ReadWriteLock 등):**
    * `synchronized` 블록보다 더 유연한 Lock 메커니즘을 제공합니다. (예: Lock 획득 시도, 타임아웃 설정, interruptible Lock)
    * `ReentrantLock`: 재진입 가능한 상호 배제 Lock.
    * `ReadWriteLock`: 읽기 스레드는 여러 개 허용하고 쓰기 스레드는 한 번에 하나만 허용하여 읽기 작업이 많은 경우 성능 향상에 유리합니다.
* **Executor 프레임워크 (ExecutorService, ThreadPoolExecutor):**
    * 스레드 생성 및 관리의 복잡성을 추상화하여 개발자가 비즈니스 로직에 집중할 수 있게 돕습니다.
    * `ThreadPoolExecutor`: 고정된 수의 스레드를 재사용하여 스레드 생성 오버헤드를 줄입니다.
* **Future, Callable, CompletableFuture:**
    * 비동기 작업의 결과를 나타내거나, 비동기 작업을 조합하고 결과를 처리하는 데 사용됩니다.
    * `Callable`: `Runnable`과 유사하나 결과를 반환하고 예외를 던질 수 있습니다.
    * `Future`: `Callable`의 실행 결과를 얻어오거나 작업의 완료 여부를 확인합니다.
    * `CompletableFuture`: Java 8에서 도입된 비동기 프로그래밍의 강력한 도구로, 여러 비동기 작업을 쉽게 조합하고 변환하며 에러를 처리할 수 있게 합니다.
* **Atomic 변수 (AtomicInteger, AtomicLong, AtomicBoolean 등):**
    * CAS(Compare-And-Swap) 연산을 사용하여 Lock 없이 스레드 안전한 원자적(atomic) 연산을 제공합니다.
    * `synchronized`보다 성능상 이점이 있습니다.
* **스레드 안전 컬렉션 (ConcurrentHashMap, CopyOnWriteArrayList 등):**
    * 멀티스레드 환경에서 안전하게 사용할 수 있도록 설계된 컬렉션입니다.
    * `ConcurrentHashMap`: 일반 `HashMap`보다 동시성 처리 성능이 우수합니다.

#### ✅ 5. 비동기 처리 방식의 구현

* **전통적 방법:**
    * `Runnable` 인터페이스 구현
    * `Thread` 클래스 직접 사용 (일반적으로 `Executor` 프레임워크 사용을 권장)
    * `Executor` 프레임워크를 이용한 스레드 풀 관리
* **현대적 방법:**
    * **`CompletableFuture`:**
        * 비동기 연산을 조합하고 파이프라인을 구축하는 데 매우 효과적입니다.
        * `thenApply()`, `thenAccept()`, `thenCompose()`, ``allOf()`, `anyOf()` 등의 메서드를 사용하여 다양한 비동기 흐름 제어가 가능합니다.
    * **Reactive Programming (리액티브 프로그래밍):**
        * Project Reactor (Spring WebFlux 포함), RxJava 등의 프레임워크를 사용합니다.
        * 데이터 스트림과 비동기 이벤트를 다루는 데 특화되어 있으며, 논블로킹(Non-blocking) 및 이벤트 기반 애플리케이션 개발에 적합합니다.

#### ✅ 6. 실전 적용 시 주의할 점

* **스레드 수 관리 (Thread pool 사용):**
    * 무분별한 스레드 생성은 시스템 자원 고갈과 성능 저하를 유발합니다.
    * `ThreadPoolExecutor` 등을 사용하여 적절한 스레드 풀 크기를 유지해야 합니다.
* **자원 관리 (동기화 또는 락):**
    * 공유 자원에 대한 접근은 반드시 동기화 메커니즘을 통해 보호해야 합니다.
    * `synchronized`, `Lock`, `Atomic` 변수, `Concurrent` 컬렉션 등 상황에 맞는 최적의 도구를 선택해야 합니다.
* **타임아웃 및 예외 처리:**
    * 비동기 작업이나 Lock 획득 시 무한 대기를 방지하기 위해 타임아웃을 설정해야 합니다.
    * 각 스레드에서 발생하는 예외를 적절히 처리하고 전파해야 합니다.
* **비동기 결과의 처리 순서 보장 여부:**
    * 비동기 작업의 완료 순서는 보장되지 않을 수 있습니다. 순서가 중요한 경우 `CompletableFuture.thenCompose()`나 다른 동기화 메커니즘을 통해 순서를 제어해야 합니다.

---

### 💡 추가적으로 더 알아야 할 내용

위 내용 외에도 자바 동시성 프로그래밍을 더 깊이 이해하고 실전에 적용하는 데 도움이 되는 몇 가지 개념이 있습니다.

* **`volatile` 키워드:**
    * 변수의 가시성(Visibility)을 보장합니다. 즉, 한 스레드에서 `volatile` 변수의 변경이 다른 스레드에게 즉시 보장되도록 합니다.
    * `synchronized`나 `Lock`과는 달리 원자성(Atomicity)은 보장하지 않습니다. 단순한 플래그 변수나 상태 변경에 유용합니다.
* **`ThreadLocal` 클래스:**
    * 스레드 로컬 변수를 생성하여 각 스레드가 자신만의 독립적인 변수 복사본을 가질 수 있도록 합니다.
    * 공유 자원을 사용하지 않고도 스레드 간의 데이터 격리를 통해 Race Condition을 피할 수 있습니다. (예: 사용자 세션 정보, 트랜잭션 ID 등)
* **동시성 설계 패턴 및 원칙:**
    * Immutable Objects (불변 객체): 객체 생성 후 상태를 변경할 수 없게 하여 스레드 안전성을 높이는 방법.
    * Guarded Suspension (보호된 일시 중단): 특정 조건이 충족될 때까지 스레드를 일시 중단시키는 패턴.
    * Producer-Consumer Pattern (생산자-소비자 패턴): 작업을 생성하는 스레드와 소비하는 스레드를 분리하여 효율적으로 처리하는 패턴.
* **동시성 코드 테스트:**
    * 멀티스레드 환경의 코드는 테스트하기 매우 어렵습니다.
    * 예측 불가능한 Race Condition 등을 잡아내기 위한 특별한 테스트 전략과 도구(예: JCStress)가 필요합니다.
* **JDK 21+ Structured Concurrency (구조적 동시성):** (미래 지향적)
    * 복잡한 동시성 작업을 구조화된 방식으로 작성하고 관리하는 새로운 접근 방식입니다.
    * 동시성 작업의 라이프사이클을 더 쉽게 관리하고 에러 처리를 간소화합니다. (현 시점에서는 선택적 학습)

---



---

## Concurrent 라이브러리 내부 인터페이스 차이 

 `Executor`와 `ExecutorService`는 모두 스레드에서 작업을 실행하는 데 사용되는 핵심 인터페이스입니다. 이 둘은 계층 관계에 있으며, `ExecutorService`가 `Executor`를 확장하여 더 많은 기능을 제공합니다.

### 1. `Executor` 인터페이스

`Executor`는 스레드 실행을 위한 가장 기본적인 인터페이스입니다.
* **단일 메서드:** `void execute(Runnable command)`만을 가집니다.
* **역할:** 작업 제출(task submission)과 작업 실행(task execution)의 메커니즘을 분리하는 역할을 합니다. 즉, `execute()` 메서드를 호출하는 쪽에서는 실제 작업이 어떤 스레드에서, 언제, 어떻게 실행될지에 대해 전혀 알 필요가 없습니다. 구현체에 따라 새로운 스레드를 만들 수도, 기존 스레드 풀의 스레드를 사용할 수도 있습니다.
* **반환 값/예외:** `Runnable`만을 받기 때문에 작업을 실행한 결과를 반환받을 수 없고, 체크드 예외도 외부로 던질 수 없습니다.
* **생명주기 관리 없음:** `Executor` 인터페이스 자체는 스레드 풀의 시작, 종료, 현재 상태 확인 등과 같은 생명주기 관리 기능을 제공하지 않습니다.

**간단한 사용 예시:**
```java
import java.util.concurrent.Executor;

class MyTask implements Runnable {
    private String name;
    public MyTask(String name) { this.name = name; }
    @Override
    public void run() {
        System.out.println("Executing " + name + " by " + Thread.currentThread().getName());
    }
}

public class ExecutorExample {
    public static void main(String[] args) {
        // Executor 구현체 (람다 표현식으로 간단하게)
        Executor executor = command -> new Thread(command).start(); // 새 스레드를 만들어서 실행

        executor.execute(new MyTask("Task 1"));
        executor.execute(new MyTask("Task 2"));
    }
}
```

### 2. `ExecutorService` 인터페이스

`ExecutorService`는 `Executor` 인터페이스를 확장한(extends) 인터페이스로, `Executor`의 기능에 더해 스레드 풀의 **생명주기 관리** 및 **비동기 작업의 결과 및 상태 관리** 기능을 제공합니다.

* **확장된 기능:**
  * **작업 제출 및 결과 반환:**
    * `Future<?> submit(Runnable task)`: `Runnable` 작업을 제출하고 `Future` 객체를 반환합니다. `Future`는 작업의 완료 여부를 확인하거나 취소할 수 있습니다.
    * `<T> Future<T> submit(Callable<T> task)`: `Callable` 작업을 제출하고 `Future` 객체를 반환합니다. `Callable`은 값을 반환할 수 있으므로, `Future`를 통해 그 값을 얻을 수 있습니다.
  * **스레드 풀 생명주기 관리:**
    * `void shutdown()`: 현재 실행 중인 모든 작업이 완료될 때까지 기다린 후 스레드 풀을 우아하게 종료합니다. 새로운 작업을 더 이상 받지 않습니다.
    * `List<Runnable> shutdownNow()`: 즉시 스레드 풀을 종료하려고 시도합니다. 실행 중인 작업을 인터럽트하고, 대기 중인 작업을 반환합니다.
    * `boolean isShutdown()`: 스레드 풀이 종료되었는지 여부를 확인합니다.
    * `boolean isTerminated()`: `shutdown()` 또는 `shutdownNow()` 호출 후 모든 작업이 완료되어 스레드 풀이 완전히 종료되었는지 여부를 확인합니다.
    * `boolean awaitTermination(long timeout, TimeUnit unit)`: 스레드 풀이 지정된 시간 내에 종료될 때까지 현재 스레드를 블록합니다.
  * **다중 작업 제출:**
    * `<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)`: 여러 `Callable` 작업을 한 번에 제출하고, 모든 작업이 완료될 때까지 기다린 후 각 작업의 `Future` 목록을 반환합니다.
    * `<T> T invokeAny(Collection<? extends Callable<T>> tasks)`: 여러 `Callable` 작업을 제출하고, 그 중 하나라도 완료되면 해당 작업의 결과를 반환하고 나머지 작업을 취소합니다.

**일반적인 사용 예시:**
```java
import java.util.concurrent.*;

public class ExecutorServiceExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // FixedThreadPool 생성 (ExecutorService의 한 종류)
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // 1. Runnable 작업 제출 (Future 반환)
        Future<?> future1 = executorService.submit(new MyTask("Runnable Task"));
        System.out.println("Runnable Task submitted. Is Done? " + future1.isDone()); // false or true

        // 2. Callable 작업 제출 (Future 반환 및 결과 획득)
        Callable<String> myCallable = () -> {
            System.out.println("Executing Callable Task by " + Thread.currentThread().getName());
            Thread.sleep(1500);
            return "Callable Task Result!";
        };
        Future<String> future2 = executorService.submit(myCallable);

        try {
            // Future.get()은 작업이 완료될 때까지 블록킹합니다.
            System.out.println("Waiting for Callable result...");
            String result = future2.get(); // 결과 가져오기
            System.out.println("Callable Task finished with result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 스레드 풀 종료 (새로운 작업은 받지 않지만, 이미 제출된 작업은 완료)
        executorService.shutdown();
        System.out.println("ExecutorService shutdown initiated.");

        // 모든 작업이 종료될 때까지 대기
        if (executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("ExecutorService terminated.");
        } else {
            System.out.println("ExecutorService did not terminate in time, forcing shutdown.");
            executorService.shutdownNow();
        }
    }
}
```

### 요약 비교표

| 특징         | `Executor`                                    | `ExecutorService`                                         |
| :----------- | :-------------------------------------------- | :-------------------------------------------------------- |
| **상속 관계** | 기본 인터페이스                               | `Executor` 인터페이스를 확장                                  |
| **메서드** | `void execute(Runnable command)`              | `execute()` 외에 `submit()`, `shutdown()`, `awaitTermination()`, `invokeAll()`, `invokeAny()` 등 추가 |
| **작업 제출** | `Runnable`만 가능                             | `Runnable`과 `Callable` 모두 가능                          |
| **결과 반환** | 없음                                          | `submit()` 메서드를 통해 `Future` 객체 반환, 작업 결과 획득 가능 |
| **예외 처리** | 내부 처리 필요 (던질 수 없음)                 | `Future.get()`을 통해 `ExecutionException`으로 예외를 전달받아 처리 가능 |
| **생명주기** | 관리 기능 없음                                | `shutdown()`, `shutdownNow()`, `isTerminated()` 등을 통해 스레드 풀의 시작/종료/상태 관리 가능 |
| **주요 용도** | 간단한 작업 실행 분리 (추상화)                | 복잡한 비동기 작업 관리, 스레드 풀의 생명주기 제어, 결과/예외 처리 필요 시 |

결론적으로, `Executor`는 작업을 실행하는 가장 기본적인 계약(contract)을 정의하고, `ExecutorService`는 그 계약을 확장하여 스레드 풀의 생명주기 관리 및 비동기 작업의 결과 관리에 필요한 강력한 기능들을 추가 제공합니다. 실제 대부분의 동시성 프로그래밍에서는 `Executor`의 구현체이자 더 많은 기능을 제공하는 `ExecutorService`를 주로 사용합니다.