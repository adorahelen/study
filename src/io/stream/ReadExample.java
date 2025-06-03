package io.stream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadExample {
    public static void main(String[] args) throws IOException {
        InputStream is = new FileInputStream("/Users/vianney/study/testIO.db\n");

        while (true){
            int data = is.read(); // 1byte 씩 읽기
            if (data == -1) break; // 파일 끝에 도달했다면 탈출
            System.out.println(data);
        }
        is.close();

    }
}
