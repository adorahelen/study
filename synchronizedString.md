

자바에서 문자열을 다룰 때 사용하는 세 가지 주요 클래스인
**String**, 
**StringBuilder**, 
그리고 **StringBuffer**의 차이점.

---

## String, StringBuilder, StringBuffer 비교

| 항목             | String           | StringBuilder      | StringBuffer       |
| :--------------- | :--------------- | :----------------- | :----------------- |
| **불변성** | ✅ 불변(Immutable) | ❌ 가변(Mutable)   | ❌ 가변(Mutable)   |
| **스레드 안전성** | ❌ 아님          | ❌ 아님            | ✅ 스레드 안전함   |
| **성능** | 느림 (새 객체 생성) | 빠름               | StringBuilder보다 느림 |
| **멀티스레드 환경** | 비추천           | 비추천             | 추천               |
| **단일스레드 환경** | 사용 가능        | **추천!** | 사용 가능하나 느림   |
| **동기화(synchronized)** | 없음             | 없음               | 있음               |

### 1. String 클래스

* **불변 객체**: 문자열을 수정하면 항상 새로운 `String` 객체가 생성됩니다.
* **비용이 큰 편**: 문자열 연결 시 매번 새로운 메모리를 할당하므로, 잦은 문자열 변경에는 비효율적입니다.
    ```java
    String s = "hello";
    s += " world"; // 새 String 객체가 생성됨
    ```

### 2. StringBuilder 클래스

* **가변 객체**: 내부 문자열을 직접 수정합니다.
* **빠름**: 동기화 처리가 없으므로 성능이 뛰어납니다.
* **단일 스레드 환경**에서 문자열을 자주 변경할 때 **추천**합니다.
    ```java
    StringBuilder sb = new StringBuilder("hello");
    sb.append(" world"); // 기존 객체 내부 수정
    System.out.println(sb.toString()); // 출력: hello world
    ```

### 3. StringBuffer 클래스

* **StringBuilder와 유사**: 내부 문자열을 직접 수정하는 가변 객체입니다.
* **스레드 안전**: 메서드들이 `synchronized` 처리되어 있어 **멀티스레드 환경**에서 안전하게 사용할 수 있습니다.
* **약간 느림**: 동기화 처리 때문에 `StringBuilder`보다 성능이 다소 낮습니다.
    ```java
    StringBuffer sbf = new StringBuffer("hello");
    sbf.append(" world");
    System.out.println(sbf.toString()); // 출력: hello world
    ```

---

### ✅ 언제 어떤 걸 써야 할까?

* **문자열 변경이 거의 없다면**: **String**
* **단일 스레드 환경에서 문자열을 자주 변경한다면**: **StringBuilder**
* **멀티 스레드 환경에서 문자열을 자주 변경한다면**: **StringBuffer**

