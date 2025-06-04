package io.string;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriteExample {
    public static void main(String[] args) throws IOException {
//        Writer author = new FileWriter("author.txt");
//
//        char a = 'A';
//        char b = 'B';
//        char c = 'C';
//
//        author.write(a);
//        author.write(b);
//        author.write(c);
//
//        author.flush();
//        author.close();
        new arrayW();
    }

    static class arrayW {
        Writer author2 = new FileWriter("author2.txt");

        arrayW() throws IOException {
            char[] array = {'a', 'b', 'c'};
            author2.write(array);
            author2.flush();
            author2.close();
        }
    }
}
