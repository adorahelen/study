package test;


    class Person {
        private static String name; // 필드 (인스턴스 || 스태틱)
        public Person(String val) { // 생성자
            name = val;
        }
        public String get() { // 메소드
            return name; // 메소드가 스태틱인데, 필드가 인스턴스이면 컴파일 에러
            // 둘다 스태틱이거나, 둘다 인스턴스이거나, 필드만 스태틱이고 메소드는 인스턴스
        }
        public void print() { // 메소드
            System.out.println(name);
        }
    }
    public class demo {
        public static void main(String[] args) {
            Person obj = new Person("Kim");
            obj.print();
        }
    }


