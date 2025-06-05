package io.file;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileExample {
    public static void main(String[] args) throws IOException {
        File dir = new File("./temp");
        File file1 =new File(dir, "file1.txt");
        File file2 =new File(dir, "file2.txt");
        File file3 =new File(dir, "file3.txt");

        if (dir.exists() == false) {dir.mkdir();}
        if (file1.exists() == false) {file1.createNewFile();}
        if (file2.exists() == false) {file2.createNewFile();}
        if (file3.exists() == false) {file3.createNewFile();}

        File temp = new File("./temp");
        File[] contents = temp.listFiles();

        System.out.println("time\t\t\t\tformat\t\tsize\t\tname");
        System.out.println("-------------------------------------------------------");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (File file : contents) {

            // 파일 정보 출력
            System.out.print(sdf.format(new Date(file.lastModified())));
            if (file.isDirectory()){
                System.out.print("\t<DIR>\t\t\t" + file.getName());
            } else {
                System.out.print("\t\t\t\t\t" + file.length() + "\t" + file.getName());
            }
            System.out.println();
        }
    }
}
