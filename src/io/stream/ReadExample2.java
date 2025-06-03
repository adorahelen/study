package io.stream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadExample2 {
    public static void main(String[] args) throws IOException {
        InputStream is = new FileInputStream("/Users/vianney/study/testIO2.db\n");

        byte[] buffer = new byte[100]; // 길이 100인 배열 생성

        while(true) {
            int read = is.read(buffer); // 생성한 바이트 배열 길이만큼 읽기
            if(read == -1) break;
            for (int i = 0; i < read; i++) {
                System.out.println(buffer[i]); // 읽은 바이트 수 만큼 반복하면서 배열에 저장된 바이트를 출력
            }
        }
        is.close(); // 입력 스트림을 닫는다.
    }
}
