package me.akaany.po_markdown;

import lombok.SneakyThrows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class PackageUtils {
    public static Stream<Class<?>> listClass(ClassLoader cl,String packageName) {
        String filePath =cl.getResource("").getPath() + packageName.replace(".", "\\");
        List<String> fileNames = getClassNameList(filePath, null);
        return fileNames.stream().map(new Function<String, Class<?>>() {
            @SneakyThrows(ClassNotFoundException.class)
            @Override
            public Class<?> apply(String className) {
                if(!className.startsWith(packageName)){ //java.main.[pkg name].[class simple name]
                    className= className.substring(className.indexOf(packageName));
                }
                return cl.loadClass(className);
            }
        });
    }

    private static List<String> getClassNameList(String filePath, List<String> className) {
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                myClassName.addAll(getClassNameList(childFile.getPath(), myClassName));
            } else {
                String childFilePath = childFile.getPath();
                childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                childFilePath = childFilePath.replace("\\", ".");
                myClassName.add(childFilePath);
            }
        }

        return myClassName;
    }
}
