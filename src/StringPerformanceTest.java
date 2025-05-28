public class StringPerformanceTest {
    // 성능 테스트 예제에서 측정된 시간은 문자열에 a라는 문자를 반복적으로 추가(연결)하는 데 걸린 총 소요 시간

    private static final int ITERATIONS = 100_000; // 반복 횟수

    public static void main(String[] args) {
        // 1. String 클래스 성능 테스트
        testStringConcatenation();

        System.out.println("-------------------------");

        // 2. StringBuilder 클래스 성능 테스트
        testStringBuilderConcatenation();

        System.out.println("-------------------------");

        // 3. StringBuffer 클래스 성능 테스트
        testStringBufferConcatenation();
    }

    // String 클래스 성능 테스트 메서드
    public static void testStringConcatenation() {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록
        String result = ""; // 빈 문자열로 초기화

        for (int i = 0; i < ITERATIONS; i++) {
            result += "a"; // 문자열 연결 (새로운 String 객체 생성)
        }

        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        System.out.println("String 클래스 성능 테스트:");
        System.out.println("총 소요 시간: " + (endTime - startTime) + "ms");
        // System.out.println("최종 문자열 길이: " + result.length()); // 결과 길이 확인 (선택 사항)
    }

    // StringBuilder 클래스 성능 테스트 메서드
    public static void testStringBuilderConcatenation() {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록
        StringBuilder sb = new StringBuilder(); // StringBuilder 객체 생성

        for (int i = 0; i < ITERATIONS; i++) {
            sb.append("a"); // 문자열 추가 (기존 객체 내부 수정)
        }

        String result = sb.toString(); // 최종 문자열로 변환 (필요 시)
        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        System.out.println("StringBuilder 클래스 성능 테스트:");
        System.out.println("총 소요 시간: " + (endTime - startTime) + "ms");
        // System.out.println("최종 문자열 길이: " + result.length()); // 결과 길이 확인 (선택 사항)
    }

    // StringBuffer 클래스 성능 테스트 메서드
    public static void testStringBufferConcatenation() {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록
        StringBuffer sbf = new StringBuffer(); // StringBuffer 객체 생성

        for (int i = 0; i < ITERATIONS; i++) {
            sbf.append("a"); // 문자열 추가 (기존 객체 내부 수정, 동기화)
        }

        String result = sbf.toString(); // 최종 문자열로 변환 (필요 시)
        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        System.out.println("StringBuffer 클래스 성능 테스트:");
        System.out.println("총 소요 시간: " + (endTime - startTime) + "ms");
        // System.gSystem.out.println("최종 문자열 길이: " + result.length()); // 결과 길이 확인 (선택 사항)
    }
}