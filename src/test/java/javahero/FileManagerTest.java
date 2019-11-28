package javahero;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class FileManagerTest {

    private String fileName = "filetest.txt";


    @Before
    public void setUp() {

        File file = FileManager.getFile(fileName);
        try {
            if (file.createNewFile()) {
                System.out.println(file.getAbsolutePath() + " File Created");
            } else {
                System.out.println("File " + file.getAbsolutePath() + " already exists");
            }
        } catch (IOException e) {
            System.out.println("Smth went wrong: " + e.getMessage());
        }

        List<Integer> inputValues = new ArrayList<>();
        fillInputValues(inputValues);

        for (Integer value : inputValues) {
            try (FileWriter fw = new FileWriter(file.getAbsolutePath(), true)) {
                fw.write(value + "\n");
            } catch (IOException e) {
                System.out.println("Smth went wrong: " + e.getMessage());
            }
        }
    }

    private void fillInputValues(List<Integer> inputValues) {
        inputValues.add(-10);
        inputValues.add(2);
        inputValues.add(4);
        inputValues.add(-999);
        inputValues.add(500);
        inputValues.add(13);
        inputValues.add(-4);
        inputValues.add(33);
    }

    @After
    public void tearDown() {
        File file = FileManager.getFile(fileName);
        file.delete();
    }

    @Test
    public void splitForFiles() {
        assertThat(FileManager.splitForFiles(FileManager.getFile(fileName), 4).size()).isEqualTo(2);
    }

    @Test
    public void getFile() {
        assertThat(FileManager.getFile(fileName)).exists();
    }

    @Test
    public void writeToTempFile() {
    }

    @Test
    public void mergeFiles() throws IOException {
        List<File> files = FileManager.splitForFiles(FileManager.getFile(fileName), 1_000_000);

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

        assertThat(files.size()).isEqualTo(1);

        List<Integer> values = new ArrayList<>();
        Scanner sc = new Scanner(files.get(0));
        while (sc.hasNext()) {
            values.add(Integer.valueOf(sc.next()));
        }

        List<Integer> inputValues = new ArrayList<>();
        fillInputValues(inputValues);
        Collections.sort(inputValues);
        assertThat(inputValues).isEqualTo(values);
    }
}