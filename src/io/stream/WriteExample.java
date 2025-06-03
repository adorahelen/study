package io.stream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WriteExample {
    public static void main(String[] args) throws IOException {
        OutputStream os = new FileOutputStream("/Users/vianney/study/testIO.db\n"); // 01. 출력 스트림 생성
        // File 경로 추가시 throws FileNotFoundException

        byte a = 10;
        byte b = 20;
        byte c = 30;

        // File Write [ FileNotFoundException ] -> throws IOException
        os.write(a);
        os.write(b);
        os.write(c);

        os.flush(); // 출력 버퍼에 잔류하는 모든 바이트를 출력 [위에서 바로 들어간게 아니라, 버퍼에 담겨 있음]
        os.close(); // 02. 출력 스트림을 닫음
    }
}
