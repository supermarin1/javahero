package javahero;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaHero {
//    private static final int MAX_COUNT = 100 * 1024 * 1024 / 12;

    public static void main(String[] args) throws IOException {
        List<File> files = FileManager.splitForFiles(FileManager.getFile("file1.txt"), 100);

        int k = 0;
        while (files.size() != 1) {
            List<File> newFiles = new ArrayList<>();
            for (int i = 0; i < files.size(); i = i + 2) {
                if (i + 1 == files.size()) {
                    newFiles.add(files.get(i));
                    break;
                }
                File newFile = FileManager.mergeFiles(files.get(i), files.get(i + 1), k);
                newFiles.add(newFile);
                k++;
            }
            files = newFiles;
        }
        System.out.println("Finished.. Please, check the file " + files.get(0).getAbsolutePath());
    }
}
