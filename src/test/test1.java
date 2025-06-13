package test;

public class test1 {

        public static void main(String[] args) {

            int a=5,b=0;

            try{
                System.out.print(a/b);
            }catch(ArithmeticException e){
                System.out.print("출력1");
            }catch(ArrayIndexOutOfBoundsException e) {
                System.out.print("출력2");
            }catch(NumberFormatException e) {
                System.out.print("출력3");
            }catch(Exception e){
                System.out.print("출력4");
            }finally{
                System.out.print("출력5");
            }
        }
    }


    // ArithmeticException의 주요 원인과 예시:
//나눗셈:
//정수를 0으로 나누는 경우.
//형변환:
//문자열을 숫자로 변환할 때 숫자가 아닌 문자열을 입력한 경우.
//연산 결과:
//산술 연산 결과가 대상 데이터 유형의 범위를 벗어나는 경우.
//부동 소수점 연산:
//부동 소수점 연산 결과가 NaN, Infinity와 같은 값을 반환할 때.
