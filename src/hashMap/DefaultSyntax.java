package hashMap;

import java.util.HashMap;

public class DefaultSyntax {

//    클래스 블록 안에서 실행 문장(hashMap.put(...))을 직접 쓸 수는 없음.
//    실행 문장은 반드시 메서드, 생성자, 또는 초기화 블록 안에 들어가야 함.

// “나는 지금 무엇을 정확히 모르고 있는거지?”
//→ 클래스 블록 안에서 무조건 쓸 수 있는 문장과, 반드시 메서드 등에서만 쓸 수 있는 문장의 차이를 정확히 구분하지 못하고 있는 겁니다.

    // 선언 (Declaration) : HashMap<String, Integer> hashMap = new HashMap<>();
        // -> 변수 또는 필드를 선언하는 것. 클래스 블록 안에 쓸 수 있음.
    // 실행 문장 (Executable Statement) : hashMap.put("apple", 3);
        // -> 무언가를 실행하는 명령문. 반드시 메서드나 생성자, 초기화 블록 안에 있어야 함.

//    static HashMap<String, Integer> hashMap1 = new HashMap<>();
    HashMap<String, Integer> hashMap1 = new HashMap<>();


    public static void main(String[] args) {
        DefaultSyntax test = new DefaultSyntax();

        HashMap<String, Integer> hashMap2 = new HashMap<>();
        hashMap2.put("a", 1);
        hashMap2.put("b", 2);
        hashMap2.put("c", 3);
        hashMap2.put("d", 4);
        hashMap2.put("e", 5);
        hashMap2.put("f", 6);
        hashMap2.put("g", 7);
        hashMap2.put("h", 8);
        hashMap2.put("i", 9);
        hashMap2.put("j", 10);

        test.hashMap1.put("o", 15);

        System.out.println(hashMap2.isEmpty()); // maybe false
        System.out.println(hashMap2.get("a")); // maybe 1
        System.out.println(hashMap2.containsKey("b")); // maybe true

        System.out.println(hashMap2.size()); // 10
        hashMap2.remove("a");
        System.out.println(hashMap2.size()); // 9

        hashMap2.clear();
        System.out.println(hashMap2.size()); // 0
        System.out.println(hashMap2.isEmpty()); // tue

    }

}
