package test;


public class test5 {

    public static class Collection<T>{
        T value;

        public Collection(T t){
            value = t;
        }

        public void print(){
            new Printer().print(value);
        }

        class Printer{
            void print(Integer a){
                System.out.print("A" + a);
            }
            void print(Object a){
                System.out.print("B" + a);
            }
            void print(Number a){
                System.out.print("C" + a);
            }
        }
    }

    public static void main(String[] args) {
        new Collection<>(0).print();
    }
    /*
    *
            설명 : 내부 클래스 많음
            =>  (Printer는 Collection 내부 클래스)
            오버로딩 선택 : T는 Integer지만, 컴파일 타임엔 Object로 처리됨

            *
            * */

}
