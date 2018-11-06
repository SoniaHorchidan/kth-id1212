package hangman.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class WordPicker {

    private static final String filePath = "words.txt";
    private RandomAccessFile file;

    public WordPicker() {
        try {
            file = new RandomAccessFile(filePath, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String next() {
        Random random = new Random();
        try {
            int position = random.nextInt((int) file.length());
            file.seek(position);
            String word = file.readLine();
            System.out.println(word);
            return word;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
