# 문법

### 1. for (char c : a) — 향상된 for(for-each) 문
```
char[] a = {'A', 'B', 'C'};

for (char c : a) {
    // c는 순서대로 'A', 'B', 'C'가 된다
    System.out.println(c);
}
```
- c는 차례 차례 a 가 된다.

### 2. 2. A : for ( ... ) { … } — 라벨을 붙인 for 문
```
A: for (int i = 0; i < n; i++) {
    // (1) 이 for문에 ‘A’라는 이름(label)’을 붙였다.
    for (int j = 0; j < m; j++) {
        if (어떤_조건) {
            break A;   // 바깥쪽 for문(A)을 즉시 종료
            // 또는 continue A; // A를 다시 반복 -> i가 증가하며 다음 반복으로
        }
    }
}
```
- 맨 앞에 A:처럼 “라벨(label)”을 붙여두면, 
    * -> 내부 어디서건 break A; 또는 continue A; 같은 식으로 바깥쪽 반복문을 직접 제어