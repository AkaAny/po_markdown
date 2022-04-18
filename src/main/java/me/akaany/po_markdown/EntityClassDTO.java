package me.akaany.po_markdown;

import org.hibernate.cfg.JPAIndexHolder;
import org.springframework.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntityClassDTO {
    public String entityName;
    public String visibleName;
    public String description;

    public String tableName;

    public transient Class<?> reflectClass;

    public List<FieldDTO> fieldList;

    public EntityClassDTO(Class<?> poClass){
        Entity entityAttr= poClass.getAnnotation(Entity.class);
        String entityName=entityAttr.name();
        if(ObjectUtils.isEmpty(entityName)){
            entityName=poClass.getSimpleName();
        }
        TableDescription tableDescriptionAttr=poClass.getAnnotation(TableDescription.class);
        if(tableDescriptionAttr!=null){
            this.visibleName=tableDescriptionAttr.visibleName();
            this.description=tableDescriptionAttr.description();
        }
        Table tableAttr=poClass.getAnnotation(Table.class);
        Index[] indices= tableAttr.indexes();
        List<String> indexDBNameList=new ArrayList<>();
        Arrays.stream(indices).forEach(new Consumer<Index>() {
            @Override
            public void accept(Index indexAttr) {
                JPAIndexHolder indexHolder=new JPAIndexHolder(indexAttr);
                indexDBNameList.addAll(Arrays.asList(indexHolder.getColumns()));
            }
        });
        List<FieldDTO> fieldInfoList= Arrays.stream(poClass.getFields()).filter(new Predicate<Field>() {
            @Override
            public boolean test(Field field) {
                return field.isAnnotationPresent(Column.class);
            }
        }).map(new Function<Field, FieldDTO>() {
            @Override
            public FieldDTO apply(Field field) {
                FieldDTO fieldDTO=new FieldDTO(field,indexDBNameList);
                return fieldDTO;
            }
        }).collect(Collectors.toList());
        this.entityName=entityName;
        this.tableName=tableAttr.name();
        this.fieldList =fieldInfoList;
        this.reflectClass =poClass;
    }

    public String toMarkdown(){
        StringBuilder builder=new StringBuilder();
        builder.append("## ").append(visibleName).append("\n");
        builder.append("### 类型名").append("\n");
        builder.append(entityName).append("\n");
        builder.append("### 表名").append("\n");
        builder.append(tableName).append("\n");
        builder.append("### 描述").append("\n");
        if(!ObjectUtils.isEmpty(description)){
            builder.append(description);
        }else{
            builder.append(visibleName);
        }
        builder.append("\n");
        builder.append("### 字段").append("\n");
        builder.append(FieldDTO.getMarkdownTableHeader()).append("\n");
        fieldList.stream().forEachOrdered(new Consumer<FieldDTO>() {
            @Override
            public void accept(FieldDTO fieldDTO) {
                builder.append(fieldDTO.toMarkdownTableLine()).append("\n");
            }
        });
        return builder.toString();
    }
}
