package me.akaany.po_markdown;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;

import java.util.Properties;

public class DialectUtils {
    public static Dialect getDialect(String dialectName){
        Properties properties=new Properties();
        properties.setProperty(Environment.DIALECT, dialectName);

        return Dialect.getDialect(properties);
    }
}
