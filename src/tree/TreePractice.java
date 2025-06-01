package tree;

import java.util.Arrays;

public class TreePractice {
    static int[] nodes = {1,2,3,4,5,6,7};
    // return [전위 순회 값] , [중위 순회 값] , [후위 순회 값]

    static class Solution{

        static String[] solve(int [] nodes) {
            String[] result = new String[3];

            result[0] = preorder(nodes, 0).trim();
            result[1] = inorder(nodes, 0).trim();
            result[2] = postorder(nodes, 0).trim();

            for (String line : result) {
                System.out.print("[");
                System.out.println(line + "]");

            }


            return result;



        }


        static String preorder(int [] nodes, int index) {
            // 인덱스가 범위를 벗어나면 빈 문자열을 반환하고
            if (index >= nodes.length) {
                return "";
            }
            // 루트 노드 -> 왼쪽 서브 트리 -> 오른쪽  서브 트리 순으로 재귀 호출 걸어 결과를 이어 붙임
            return nodes[index] + " "
                    + preorder(nodes, 2 * index + 1)
                    + preorder(nodes, 2 * index + 2);
        }

        static String inorder(int [] nodes, int index) {
            if (index >= nodes.length) { return "";}
            // 왼쪽 서브트리 -> 루트 노드 -> 오른쪽 서브 트리 순으로 재귀 호출하여 결과를 이어 붙임
            return inorder(nodes, 2 * index + 1) + nodes[index] + " " + inorder(nodes, 2 * index + 2);
        }

        static String postorder(int [] nodes, int index) {
            if (index >= nodes.length) { return "";}
            // 왼쪽 서브트리 -> 오른쪽 서브트리 -> 루트 노드 순으로 재귀 호출하여 결과를 이어 붙임
            return postorder(nodes, 2 * index + 1) + postorder(nodes, 2 * index + 2) + nodes[index] + " ";
        }


    }




    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.solve(nodes);
//        System.out.println(solution.solve(nodes));
//        System.out.println(Arrays.toString(solution.solve(nodes)));



    }
}
