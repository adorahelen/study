package io.string;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ReadExample {
    public static void main(String[] args) throws IOException {
        Reader reader = new FileReader("./author2.txt");

        char[] buf = new char[5];

        int readCharNum = reader.read(buf, 2, 3);
        if (readCharNum != -1) {// 읽은 문자가 있다면,

            for(int i = 0; i < buf.length; i++) {
                System.out.print(buf[i]);
            }
        }
        reader.close();
    }
}
