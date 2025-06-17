package test;

public class test2 {

        public static void main(String[] args) {
            new Child();
            // 1. 자식 생성자 호출
            System.out.println(Parent.total);
        }
    }


    // 3. 부모 생성자가 먼저 호출 되지만, 메소드는 오버라이딩 되었기에 패스함
    class Parent {
        static int total = 0;
        int v = 1;

        public Parent() {
            total += (++v); //4.  total 은 2
            show();
        }

        public void show() {
            total += total;
        }
    }


    // 2. 자식 생성자가 상속을 받았다면, 부모 생성자가 먼저 호출됨
    class Child extends Parent {
        int v = 10;

        public Child() {
            v += 2; // 5. v는 12
            total += v++;// 6. 토탈은 14, v는 13이 된다.
            show();
        }

        @Override
        public void show() {
            total += total * 2; // 7. 토탈은 28
        }
    }


