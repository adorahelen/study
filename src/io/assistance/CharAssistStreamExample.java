package io.assistance;

import java.io.*;

// 바이트 기반 입력 스트림 -> [보조 스트림 : 문자 입력 스트림으로 변환] -> 문자 입력 스트립
// FileInputStream -> [InputStreamReader : byte 2 char] ->  Reader

public class CharAssistStreamExample {
    public static void main(String[] args) throws Exception {
        write("문자 변환 스트림을 사용합니다.");
        String data = read();
        System.out.println(data);
    }

    private static void write(String str) throws Exception {
        FileOutputStream fos = new FileOutputStream("./temp/file.txt");
        Writer writer = new OutputStreamWriter(fos);// 보조 스트림을 연결 FileOutputStream<-Assist
        writer.write(str);
        writer.flush(); // 보조 스트림을 이용해서 문자 출력
        writer.close();
    }

    private static String read() throws Exception {
        FileInputStream fis = new FileInputStream("./temp/file.txt");
        Reader reader = new InputStreamReader(fis);
        char[] buffer = new char[100];
        int read = reader.read(buffer); // 보조 스트림을 통해 문자 입력
        reader.close();

        String data = new String(buffer, 0, read);
        return data;
    }
}
