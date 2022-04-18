package me.akaany.po_markdown;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Main {

    public static void main(String[] args) throws IOException {
        ClassLoader cl= Thread.currentThread().getContextClassLoader();
        String poMarkdown=PODocParser.parse(cl,"me.akaany.po_markdown.example");
        System.out.println(poMarkdown);
    }

}
