package array;

import java.util.HashMap;
import java.util.HashSet;

public class test2 {

    // 01. 좌표평면을 벗어나는지 체크하는 메서드 (0~10 범위 내에 있어야 함)
    static boolean isValidMove(int nx, int ny){
        return 0 <= nx && nx < 11 && 0 <= ny && ny < 11;
    }

    // 02. 이동 방향을 위한 오프셋 정보 저장 ('U', 'D', 'L', 'R' 명령에 해당)
    static final HashMap<Character, int[]> location = new HashMap<>();

    static void initLocation() {
        location.put('U', new int[]{0, 1});   // 위로 이동
        location.put('D', new int[]{0, -1});  // 아래로 이동
        location.put('L', new int[]{-1, 0});  // 왼쪽으로 이동
        location.put('R', new int[]{1, 0});   // 오른쪽으로 이동
    }

    // 03. 주어진 명령 문자열을 바탕으로 고유 경로 개수 계산
    static int solve(String dirs){
        initLocation(); // 방향 정보 초기화

        int x = 5, y = 5; // 시작 위치 (중앙)
        HashSet<String> visited = new HashSet<>(); // 방문한 경로 저장용

        for (int i = 0; i < dirs.length(); i++) {
            char command = dirs.charAt(i);
            int[] offset = location.get(command);

            int nx = x + offset[0];
            int ny = y + offset[1];

            // 이동할 좌표가 유효하지 않으면 무시
            if (!isValidMove(nx, ny)) continue;

            // 양방향 경로 모두 저장 → "중복 경로 제거" 목적
            String path1 = x + "" + y + "" + nx + "" + ny;
            String path2 = nx + "" + ny + "" + x + "" + y;

            visited.add(path1);
            visited.add(path2);

            // 현재 위치 갱신
            x = nx;
            y = ny;
        }

        // 각 경로는 양방향으로 저장되므로 2로 나눠서 반환
        return visited.size() / 2;
    }

    public static void main(String[] args) {
        // 예시 실행: "ULURRDLLU"
        System.out.println(solve("ULURRDLLU"));
    }
}