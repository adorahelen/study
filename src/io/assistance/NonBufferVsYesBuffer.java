package io.assistance;

import java.io.*;

// 성능 향상 보조 스트림
// Buffered + InputStream & OutputStream & Reader & Writer
public class NonBufferVsYesBuffer {
    // 프로그램에서 전송한 데이터를 내부 버퍼에 쌓아 두었다가,
    //  => 버파가 꽉차면, 버퍼의 모든 데이터를 한꺼번에 내보냄

    public static void main(String[] args) throws IOException {

        // section A
        String originalFilePath1 = "./temp/test1.pdf";

//        String originalFilePath1 =
//                NonBufferVsYesBuffer.class.getResource("test1.pdf").getPath();
        String targetFilePath1 = "/tmp/test1.pdf";
        FileInputStream fis = new FileInputStream(originalFilePath1);
        FileOutputStream fos = new FileOutputStream(targetFilePath1);

        // section B
        String originalFilePath2 = "./temp/test2.pdf";

//        String originalFilePath2 =
//                NonBufferVsYesBuffer.class.getResource("test2.pdf").getPath();
        String targetFilePath2 = "/tmp/test2.pdf";
        FileInputStream fis2 = new FileInputStream(originalFilePath2);
        FileOutputStream fos2 = new FileOutputStream(targetFilePath2);
        // + 버퍼 스트림 추가
        BufferedInputStream bis = new BufferedInputStream(fis2);
        BufferedOutputStream bos = new BufferedOutputStream(fos2);

        // main section C : 시간 측정
        long nonBufferTime = copy(fis, fos);
        System.out.println("버퍼를 사용하지 않았을때 : " + nonBufferTime + "ns");

        long yesBufferTime = copy(bis,bos);
        System.out.println("버퍼를 사용하고 측정하면 : " + yesBufferTime + "ns");

        fis.close();
        bos.close();
        bis.close();
        fos.close();

    }

    static int data = -1;
    public static long copy(InputStream in, OutputStream out) throws IOException {
        long start = System.nanoTime();
        while (true) {
            data = in.read();
            if (data == -1) {break;}
            out.write(data);
        }
        out.flush();
        long end = System.nanoTime();
        return (end-start);
    }

}
