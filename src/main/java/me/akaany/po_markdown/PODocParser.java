package me.akaany.po_markdown;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class PODocParser {

    public static String parse(ClassLoader cl,String basePackageName){
        StringBuilder builder=new StringBuilder();
        builder.append("# 存储结构定义").append("\n");
        PackageUtils.listClass(cl,basePackageName).filter(new Predicate<Class<?>>() {
            @Override
            public boolean test(Class<?> aClass) {
                if(aClass==null){
                    return false;
                }
                return aClass.isAnnotationPresent(Entity.class) && aClass.isAnnotationPresent(Table.class);
            }
        }).map(new Function<Class<?>, EntityClassDTO>() {
            @Override
            public EntityClassDTO apply(Class<?> poClass) {
                //System.out.println("po class:"+poClass);
                EntityClassDTO entityClassDTO=new EntityClassDTO(poClass);
                return entityClassDTO;
            }
        }).forEachOrdered(new Consumer<EntityClassDTO>() {
            @Override
            public void accept(EntityClassDTO entityClassDTO) {
                builder.append(entityClassDTO.toMarkdown()).append("\n");
            }
        });
        return builder.toString();
    }
}
