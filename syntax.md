### 01. Type (프리미티브 / 레퍼런스)

#### int vs Integer 차이
- int는 더 빠르고 가볍지만 null을 저장할 수 없고, 컬렉션(List 등)에 직접 못 들어감. 
- Integer는 객체이므로 null을 저장할 수 있고, 컬렉션에 넣을 수 있으며 메서드도 사용 가능함.
- 오토박싱/언박싱은 편리하지만 무심코 쓰면 성능 저하나 예외 발생 가능함.

```
(1)
List<Integer> list = new ArrayList<>();
list.add(10); // int → Integer (오토박싱)
int num = list.get(0); // Integer → int (언박싱)

(2)
Integer는 다양한 정적(static) 및 인스턴스 메서드를 제공합니다.
- parseInt(String s) : 문자열을 int로 변환
- toString(int i) : 정수를 문자열로 변환
- compare(int x, int y) : 두 정수를 비교 (x < y → 음수, 같으면 0, 크면 양수)
- hashCode(int value), decode(String nm) 등 다양한 함수 제공 
```
### 02. ArrayList<> vs Array[]
- Array 는 크기가 고정, 데이터의 삽입 삭제 X
- ArrayList 는 가변 크기라 가능, 하지만 끝이 아닌 중간의 경우 시간 복잡도 급격히 증가

### 03. HashMap 
- Key 와 Value 쌍을 저장하는 해시 테이블로 구현 
```
HashMap<String, Integer> map = new HasgMap<>();
// 키는 문자열을 값은 32비트 정수형을 저장하는 해시맵을 하나 선언한 모습
map.put("a", 1); // 삽입
map.contailnsKey() // 보유하고 있는지 확인
map.get() // 조회 
```

### 04. String(문자열)

- 수정방법 : replace() 메서드 
```
String A = "Hello";
A =  A.replace("l", ""); // 알파벳 l 을 모두 삭제
// A 안에 담긴 값은 "Heo"
```

