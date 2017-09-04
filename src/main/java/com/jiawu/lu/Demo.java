package com.jiawu.lu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {

    public static void main(String[] args) throws Exception {

        //System.out.println(YouDaoApi.translate("你好world"));

        File fileDir = new File(".");

        Files.walk(Paths.get(fileDir.getAbsolutePath()))
            .forEach(path -> {
                try {
                    readPath(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

    }



    public static void readPath(Path path) throws IOException {
        File file = path.toFile();
        if (!file.getName().endsWith(".java")){
            return;
        }
        //System.out.println(file.getAbsolutePath());

        String content = new String(Files.readAllBytes(path), "UTF-8");

        String regex = "[^\\x00-\\xff]+[^\"|\\n|\\s]*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        StringBuffer sb = new StringBuffer();
        boolean found = false;
        while (matcher.find()) {
            found = true;
            String zh = matcher.group(0);
            //String en = "my test";
            String en = YouDaoApi.translate(zh);
            System.out.println(zh   + "->" + en);
            matcher.appendReplacement(sb, en);
        }
        matcher.appendTail(sb);
        if (found) {
            System.out.println("replace en " + file.getAbsolutePath());
            Files.write(path, sb.toString().getBytes("UTF-8"));
        }

    }

}