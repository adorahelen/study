package queue;

import java.util.ArrayDeque;
import java.util.Queue;

public class queuePractice {
    // Enqueue IS add
    // Dequeue IS Poll

    static class ArrayQueue {
        Queue<Integer> queue = new ArrayDeque<>();

        void GeneralQueue() {
            ArrayQueue q = new ArrayQueue();
            q.queue.add(1);
            q.queue.add(2);
            q.queue.add(3);
            q.queue.add(4);

            Integer result = q.queue.poll();
            Integer result1 = q.queue.poll();
            Integer result2 = q.queue.poll();
            Integer result3 = q.queue.poll();
            Integer result4 = q.queue.poll();
            Integer result5 = q.queue.poll();

            System.out.println(result5);
        }


    }

    static class DeQueue {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        void GeneralQueue() {
            deque.addLast(1);
            deque.addLast(2);
            deque.addLast(3);
            deque.addLast(4);

            Integer result = deque.pollFirst();
            System.out.println(result); // 아마 출력은 1

            deque.addLast(5);
            deque.addLast(6);

            result = deque.pollFirst();
            System.out.println(result); // 2
        }
    }

    public static void main(String[] args) {

        ArrayQueue q = new ArrayQueue();
        q.GeneralQueue();

        DeQueue dq = new DeQueue();
        dq.GeneralQueue();

    }

}
