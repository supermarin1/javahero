package javahero;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    public static List<File> splitForFiles(File file, int lines) {
        List<File> files = new ArrayList<>();
        List<Integer> returnedList = new ArrayList<>();
        int j = 1;
        try (Scanner sc = new Scanner(file);) {
            while (sc.hasNext()) {
                int i = 0;
                while (sc.hasNext() && i < lines) {
                    returnedList.add(Integer.valueOf(sc.nextLine()));

                    if (i == lines - 1 || !sc.hasNext()) {
                        Collections.sort(returnedList);
                        File newFile = writeToTempFile(returnedList, j);
                        files.add(newFile);
                        returnedList = new ArrayList<>();
                        j++;
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    public static File getFile(String name) {
        String homeDir = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");
        String absoluteFilePath = homeDir + fileSeparator + name;
        return new File(absoluteFilePath);
    }

    public static File getTempFile(int i) {
        String homeDir = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");
        String absoluteFilePath = homeDir + fileSeparator + "temporary" + i + ".txt";
        File file = new File(absoluteFilePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public static File newSortedFile(int k) {
        String homeDir = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");
        String absoluteFilePath = homeDir + fileSeparator + "sortedFile" + k + ".txt";
        File file = new File(absoluteFilePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public static File writeToTempFile(List<Integer> intValuesFromOriginFile, int i) throws IOException {
        File file = getTempFile(i);
        StringBuilder str = new StringBuilder();
        for (Integer integer : intValuesFromOriginFile) {
            str.append(integer).append("\n");
        }

        try (FileWriter fw = new FileWriter(file.getAbsolutePath(), true)) {
            fw.write(str.toString());
        }

        return file;
    }

    public static File mergeFiles(File left, File right, int num) throws IOException {
        File out = FileManager.newSortedFile(num);

        try (PrintWriter pw = new PrintWriter(out);
             BufferedReader br1 = new BufferedReader(new FileReader(left));
             BufferedReader br2 = new BufferedReader(new FileReader(right))) {

            Integer line1 = Integer.valueOf(br1.readLine());
            Integer line2 = Integer.valueOf(br2.readLine());

            while (line1 != null || line2 != null) {
                if (line1 != null && line2 != null) {
                    if (line1 < line2) {
                        pw.println(line1);
                        String linestr = br1.readLine();
                        line1 = linestr != null ? Integer.valueOf(linestr) : null;
                    } else {
                        pw.println(line2);
                        String linestr = br2.readLine();
                        line2 = linestr != null ? Integer.valueOf(linestr) : null;
                    }
                }

                if (line1 == null && line2 != null) {
                    pw.println(line2);
                    String linestr = br2.readLine();
                    line2 = linestr != null ? Integer.valueOf(linestr) : null;
                }

                if (line1 != null && line2 == null) {
                    pw.println(line1);
                    String linestr = br1.readLine();
                    line1 = linestr != null ? Integer.valueOf(linestr) : null;
                }
            }

            pw.flush();
        }

        left.delete();
        right.delete();

        return out;
    }
}
