package org.lazarev.list;





import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbstractLists {
 private   List<String> list = new ArrayList<>();
    ByteArrayOutputStream buffer;
    public List addList(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();
            list = new ArrayList<>(Arrays.asList(line.split(",")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    }


