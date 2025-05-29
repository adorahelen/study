package hashMap;

import java.util.HashMap;

public class MarathonChecker {

    public static String findIncompleteRunner(String[] participant, String[] completion) {
        HashMap<String, Integer> runners = new HashMap<>();

        // 🎯 완주한 사람들을 해시맵에 기록 (이름, 등장 횟수)
        for (String name : completion) {
            runners.put(name, runners.getOrDefault(name, 0) + 1);
        }

        // 🧐 참가자들 중에서 완주하지 못한 사람 찾기
        for (String name : participant) {
            int count = runners.getOrDefault(name, 0);
            if (count == 0) {
                return name; // 👈 이 사람이 완주하지 못한 주인공!
            }
            runners.put(name, count - 1); // 한 명 줄이기
        }

        return null; // 모든 참가자가 완주했으면 null
    }

    // 예시 실행
    public static void main(String[] args) {
        String[] participant = {"leo", "kiki", "eden"};
        String[] completion = {"eden", "kiki"};
        System.out.println("완주하지 못한 선수는: " + findIncompleteRunner(participant, completion));
    }
}