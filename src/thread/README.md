# java.util.concurrent 
- 경쟁, 동시성, 멀티 스레드 프로그래밍
- 자바5에서 동시성 라이브러리 도입됨 

## 디자인 콘셉트
- 더그 레아가 java.util.concurrent 를 제작하면서, 중요시한 것
***
01. 안전성
2. 활성성
3. 성능
4. 재사용 가능성 
***

### 스레드의 상태 모델 
```

      [ Block <-> Wating <-> Timed waiting ]
New ->              Runnable                -> Terminated

```


---


---

### 자바 5 이전 (블록 구조 동시성)

자바 5 이전에는 주로 `synchronized` 키워드와 `wait()`, `notify()`, `notifyAll()` 메서드를 사용하여 스레드 간의 동시성을 제어했습니다. 이러한 방식은 **블록 구조(Blocking Concurrency)**라고 불립니다.

* **`synchronized`**: 메서드나 코드 블록에 적용하여 해당 블록에 한 번에 하나의 스레드만 접근할 수 있도록 **잠금(Locking)**을 설정합니다. 이는 공유 자원에 대한 접근을 보호하여 데이터 불일치 문제를 방지합니다.

    ```java
    class Counter {
        private int count = 0;

        // synchronized 키워드를 사용하여 메서드 동기화
        public synchronized void increment() {
            count++;
            System.out.println(Thread.currentThread().getName() + ": " + count);
        }

        public int getCount() {
            return count;
        }
    }

    public class SynchronizedExample {
        public static void main(String[] args) throws InterruptedException {
            Counter counter = new Counter();

            // 여러 스레드가 동시에 increment() 호출
            Thread t1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    counter.increment();
                }
            }, "Thread-1");

            Thread t2 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    counter.increment();
                }
            }, "Thread-2");

            t1.start();
            t2.start();

            t1.join(); // t1이 끝날 때까지 대기
            t2.join(); // t2가 끝날 때까지 대기

            System.out.println("최종 카운트: " + counter.getCount()); // 예상: 2000
        }
    }
    ```

* **`volatile`**: 변수에 `volatile` 키워드를 사용하면, 해당 변수에 대한 모든 읽기/쓰기 작업이 메인 메모리에서 직접 이루어지도록 강제합니다. 이는 캐시 일관성 문제를 해결하여 여러 스레드가 동일한 변수의 최신 값을 볼 수 있도록 보장합니다. `synchronized`와 달리 **잠금 메커니즘을 제공하지 않으므로** 복합적인 작업에는 적합하지 않습니다.

    ```java
    class VolatileExample {
        // volatile 키워드 사용
        private volatile boolean running = true;

        public void stop() {
            running = false;
        }

        public void run() {
            System.out.println("Watcher Thread 시작.");
            while (running) {
                // do some work
            }
            System.out.println("Watcher Thread 종료.");
        }

        public static void main(String[] args) throws InterruptedException {
            VolatileExample example = new VolatileExample();
            Thread watcherThread = new Thread(example::run, "Watcher");
            watcherThread.start();

            Thread.sleep(100); // 잠시 대기하여 watcherThread가 실행되도록 함
            example.stop(); // 다른 스레드에서 running 값을 변경
        }
    }
    ```

* **`wait()`, `notify()`, `notifyAll()`**: 이 메서드들은 `Object` 클래스에 정의되어 있으며, `synchronized` 블록 내에서만 호출될 수 있습니다. 스레드가 특정 조건이 충족될 때까지 기다리거나, 조건을 충족시켰을 때 대기 중인 스레드에게 알리는 데 사용됩니다.

    ```java
    import java.util.LinkedList;
    import java.util.Queue;

    class ProducerConsumer {
        private final Queue<Integer> queue = new LinkedList<>();
        private final int CAPACITY = 5;

        public void produce() throws InterruptedException {
            int value = 0;
            while (true) {
                synchronized (this) {
                    while (queue.size() == CAPACITY) { // 큐가 가득 차면 대기
                        System.out.println("큐 가득 참. 생산자 대기...");
                        wait(); // 생산자 스레드 대기 (락 해제)
                    }
                    System.out.println("생산: " + value);
                    queue.offer(value++);
                    notifyAll(); // 소비자 스레드에게 알림 (락 보유)
                    Thread.sleep(500);
                }
            }
        }

        public void consume() throws InterruptedException {
            while (true) {
                synchronized (this) {
                    while (queue.isEmpty()) { // 큐가 비어있으면 대기
                        System.out.println("큐 비어있음. 소비자 대기...");
                        wait(); // 소비자 스레드 대기 (락 해제)
                    }
                    int val = queue.poll();
                    System.out.println("소비: " + val);
                    notifyAll(); // 생산자 스레드에게 알림 (락 보유)
                    Thread.sleep(500);
                }
            }
        }
    }

    public class ProducerConsumerExample {
        public static void main(String[] args) {
            ProducerConsumer pc = new ProducerConsumer();

            Thread producerThread = new Thread(() -> {
                try {
                    pc.produce();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Producer");

            Thread consumerThread = new Thread(() -> {
                try {
                    pc.consume();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Consumer");

            producerThread.start();
            consumerThread.start();
        }
    }
    ```

---

### 자바 5 이후 (`java.util.concurrent` 패키지)

자바 5에서 Doug Lea가 설계한 `java.util.concurrent` 패키지가 도입되면서, 더 복잡하고 유연하며 고성능의 동시성 프로그래밍이 가능해졌습니다. 이 패키지는 **비블록 구조(Non-Blocking Concurrency)** 또는 **락 프리(Lock-Free)** 알고리즘을 지향하며, 추상화된 동시성 유틸리티를 제공합니다.

#### 주요 구성 요소

---

### 1. Executor Framework (실행자 프레임워크)

스레드 생성 및 관리 로직을 애플리케이션 로직과 분리하여, 스레드 풀을 통해 작업을 효율적으로 실행할 수 있도록 돕습니다.

* **`Executor`**: 작업을 실행하는 가장 기본적인 인터페이스입니다.
* **`ExecutorService`**: `Executor`를 확장한 인터페이스로, 스레드 풀의 생명 주기 관리(`shutdown()`, `awaitTermination()`), 그리고 작업 제출(`submit()`, `invokeAll()`) 등 더 풍부한 기능을 제공합니다.
* **`Executors`**: `ExecutorService` 인스턴스를 쉽게 생성할 수 있는 팩토리 클래스입니다 (예: `newFixedThreadPool`, `newCachedThreadPool`, `newSingleThreadExecutor`).

**예제: `ExecutorService`를 이용한 `Runnable` 작업 제출**

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BasicExecutorServiceExample {
    public static void main(String[] args) throws InterruptedException {
        // 3개의 스레드를 가진 고정 스레드 풀 생성
        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("Runnable 작업 제출:");
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            // Runnable 작업은 결과를 반환하지 않습니다.
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + ": Task " + taskId + " 실행 중...");
                try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                System.out.println(Thread.currentThread().getName() + ": Task " + taskId + " 완료.");
            });
        }

        // 스레드 풀 종료
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("BasicExecutorServiceExample 종료.");
    }
}
```

---

### 1-1. Callable`<V>`

* **역할**: `Runnable`과 유사하게 별도의 스레드에서 실행될 작업을 정의하는 인터페이스입니다. `Runnable`과의 가장 큰 차이점은 작업을 완료한 후 **결과 값(`V`)을 반환**할 수 있고, `Exception`을 던질 수 있다는 점입니다. 이는 비동기 작업의 결과를 받아야 할 때 매우 유용합니다.
* **메서드**: `V call()`: 이 메서드가 작업을 수행하고 결과를 반환합니다. `Exception`을 선언할 수 있습니다.

---

### 1-2. Future`<V>`

* **역할**: `Callable` 작업의 **비동기적인 결과**를 나타내는 객체입니다. 작업이 완료되었는지 확인하거나, 작업의 완료를 기다리거나, 작업의 결과를 검색하는 데 사용됩니다. 작업이 아직 완료되지 않았다면, `get()` 메서드는 작업이 완료될 때까지 호출 스레드를 블록(대기)시킵니다.
* **주요 메서드**:
    * `V get()`: 작업이 완료될 때까지 현재 스레드를 블록하고, 완료되면 작업의 결과를 반환합니다. 작업 중 예외가 발생하면 `ExecutionException`이 발생합니다.
    * `boolean isDone()`: 작업이 완료되었으면 `true`를 반환합니다.
    * `boolean cancel(boolean mayInterruptIfRunning)`: 작업을 취소하려고 시도합니다.

**예제: `Callable`과 `Future` 함께 사용**

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// Callable을 구현하여 결과를 반환하는 작업 정의
class SummingTask implements Callable<Integer> {
    private final int number;

    public SummingTask(int number) {
        this.number = number;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + ": " + number + "까지 합계 계산 시작...");
        int sum = 0;
        for (int i = 1; i <= number; i++) {
            sum += i;
            Thread.sleep(10); // 작업 시뮬레이션
        }
        System.out.println(Thread.currentThread().getName() + ": " + number + "까지 합계 계산 완료.");
        return sum; // 결과 반환
    }
}

public class CallableFutureExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<Integer>> futures = new ArrayList<>();

        System.out.println("Callable 작업 제출 및 Future 객체 획득:");
        for (int i = 1; i <= 5; i++) {
            // Callable 작업 제출. ExecutorService는 Future 객체를 반환합니다.
            Future<Integer> future = executor.submit(new SummingTask(i * 10));
            futures.add(future);
        }

        System.out.println("\n모든 작업이 제출되었습니다. Future를 통해 결과 대기 중...");

        // Future 객체를 통해 각 작업의 결과 얻기
        for (Future<Integer> future : futures) {
            try {
                // get() 메서드는 작업이 완료될 때까지 블록됩니다.
                Integer result = future.get();
                System.out.println("결과: " + result);
            } catch (ExecutionException e) {
                System.err.println("작업 실행 중 예외 발생: " + e.getCause().getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("작업 대기 중 인터럽트 발생.");
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("CallableFutureExample 종료.");
    }
}
```

---

### 2. Locks (락)

* `synchronized` 키워드보다 더 유연하고 정교한 락 메커니즘을 제공합니다.
* **`Lock` 인터페이스**: `ReentrantLock` 구현체를 통해 사용됩니다. `synchronized`와 달리 락을 명시적으로 획득하고(`lock()`) 해제해야 합니다(`unlock()`). 조건부 대기(`Condition`)를 함께 사용할 수 있습니다.
* **`ReentrantLock`**: 동일 스레드가 이미 획득한 락을 다시 획득할 수 있는 재진입(reentrant) 가능한 락입니다.

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SafeCounter {
    private int count = 0;
    private final Lock lock = new ReentrantLock(); // ReentrantLock 인스턴스

    public void increment() {
        lock.lock(); // 락 획득
        try {
            count++;
            System.out.println(Thread.currentThread().getName() + ": " + count);
        } finally {
            lock.unlock(); // 락 해제 (finally 블록에서 항상 해제해야 함)
        }
    }

    public int getCount() {
        return count;
    }
}

public class ReentrantLockExample {
    public static void main(String[] args) throws InterruptedException {
        SafeCounter counter = new SafeCounter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        }, "Lock-Thread-1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        }, "Lock-Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("최종 카운트: " + counter.getCount());
    }
}
```

---

### 3. Atomic Variables (원자 변수)

* `java.util.concurrent.atomic` 패키지에 포함됩니다.
* 락 없이(Lock-Free) 원자적인 연산(단일 단계로 완료되어 중간에 중단되지 않는 연산)을 제공하는 클래스들입니다. CPU의 CAS(Compare-And-Swap) 명령어를 활용하여 동기화 오버헤드를 줄입니다.
* 주요 클래스: `AtomicInteger`, `AtomicLong`, `AtomicBoolean`, `AtomicReference` 등.

```java
import java.util.concurrent.atomic.AtomicInteger;

class AtomicCounter {
    private AtomicInteger count = new AtomicInteger(0); // AtomicInteger 사용

    public void increment() {
        count.incrementAndGet(); // 원자적으로 값을 1 증가시킴
        System.out.println(Thread.currentThread().getName() + ": " + count.get());
    }

    public int getCount() {
        return count.get();
    }
}

public class AtomicExample {
    public static void main(String[] args) throws InterruptedException {
        AtomicCounter counter = new AtomicCounter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        }, "Atomic-Thread-1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        }, "Atomic-Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("최종 카운트: " + counter.getCount()); // 예상: 2000
    }
}
```

---

### 4. Concurrent Collections (동시성 컬렉션)

* 멀티스레드 환경에서 안전하게 사용할 수 있도록 설계된 컬렉션 클래스들입니다. 기존 `Collections.synchronizedList()`와 같은 래퍼 방식보다 훨씬 높은 성능을 제공합니다.
* 주요 클래스: `ConcurrentHashMap`, `CopyOnWriteArrayList`, `ConcurrentLinkedQueue`, `BlockingQueue` 인터페이스의 구현체들 (`ArrayBlockingQueue`, `LinkedBlockingQueue` 등).

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ConcurrentHashMapExample {
    public static void main(String[] args) throws InterruptedException {
        // ConcurrentHashMap: 멀티스레드 환경에서 안전하고 효율적인 HashMap
        Map<String, Integer> map = new ConcurrentHashMap<>();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                map.compute("key", (k, v) -> (v == null) ? 1 : v + 1); // 원자적인 연산
            }
        };

        Thread t1 = new Thread(task, "Map-Thread-1");
        Thread t2 = new Thread(task, "Map-Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("최종 'key' 값: " + map.get("key")); // 예상: 2000
    }
}
```

---

### 5. Synchronizers (동기화 도구)

* 특정 조건을 만족할 때까지 스레드를 대기시키거나, 스레드 간의 상호작용을 조정하는 데 사용됩니다.
* **`CountDownLatch`**: 하나 이상의 스레드가 다른 스레드에서 수행되는 일련의 작업을 완료할 때까지 대기하도록 하는 데 사용됩니다.
* **`CyclicBarrier`**: 여러 스레드가 모두 특정 지점에 도달할 때까지 서로를 기다리게 한 다음, 동시에 작업을 다시 시작하도록 합니다.
* **`Phaser`**: `CyclicBarrier`보다 더 유연한 장벽으로, 동적으로 참여자 수를 조절할 수 있습니다.
* **`Semaphore`**: 특정 자원에 대한 동시 접근 수를 제한합니다 (이전 답변에서 상세 예제 제공).

---

`java.util.concurrent` 패키지는 현대 자바 애플리케이션에서 복잡한 동시성 문제를 안전하고 효율적으로 해결하기 위한 필수적인 도구들을 제공합니다. 이전 방식의 단점을 보완하고, 더 높은 수준의 추상화와 성능을 제공하여 개발자가 동시성 버그를 줄이고 애플리케이션의 활성성과 확장성을 높일 수 있도록 돕습니다.

