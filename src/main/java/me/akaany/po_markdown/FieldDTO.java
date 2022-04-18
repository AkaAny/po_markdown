package me.akaany.po_markdown;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.descriptor.sql.JdbcTypeJavaClassMappings;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.List;

public class FieldDTO {
    public String fieldName;
    public String description;
    public String dbName;

    public String dbType;

    public boolean isPrimaryKey;
    public boolean index;
    public boolean autoIncrement;

    public transient Field reflectField;

    public static final Dialect DIALECT= DialectUtils.getDialect("org.hibernate.dialect.MySQL8Dialect");

    public FieldDTO(Field field, List<String> indexDBNameList){
        Class<?> fieldClass=field.getType();
        int jdbcTypeCode= JdbcTypeJavaClassMappings.INSTANCE.determineJdbcTypeCodeForJavaClass(fieldClass);
        String dbType=null;
        if(jdbcTypeCode==fieldClass.hashCode()){ //no default jdbc mapping, it is a special code
            dbType="json";
        }else {
            dbType = DIALECT.getTypeName(jdbcTypeCode);
        }
        //System.out.println("db type:"+dbType);

        //column def
        this.fieldName=field.getName();
        Column columnAttr=field.getAnnotation(Column.class);
        String dbName=columnAttr.name();
        if(fieldClass==String.class && dbType.equals("longtext")){ //handle string case
            dbType="varchar("+columnAttr.length()+")";
        }
        ColumnDescription columnDescriptionAttr=field.getAnnotation(ColumnDescription.class);
        if(columnDescriptionAttr!=null){
            this.description=columnDescriptionAttr.value();
        }
        this.dbName=dbName;
        this.dbType=dbType;
        //column attr
        this.isPrimaryKey=field.isAnnotationPresent(Id.class);
        if(indexDBNameList.contains(dbName)){
            this.index=true;
        }
        this.autoIncrement=field.isAnnotationPresent(GeneratedValue.class);
        this.reflectField=field;
    }

    public static String getMarkdownTableHeader(){
        StringBuilder builder=new StringBuilder();
        builder.append(COLUMN_SEPARATOR).append("字段名")
                .append(COLUMN_SEPARATOR).append("描述")
                .append(COLUMN_SEPARATOR).append("数据库字段名")
                .append(COLUMN_SEPARATOR).append("字段类型")
                .append(COLUMN_SEPARATOR).append("主键")
                .append(COLUMN_SEPARATOR).append("索引")
                        .append(COLUMN_SEPARATOR).append("自增");
        builder.append(COLUMN_SEPARATOR);
        builder.append("\n");
        //column def data sep line
        builder.append(COLUMN_SEPARATOR);
        for (int i = 0; i < 7; i++) {
            builder.append("----").append(COLUMN_SEPARATOR);
        }
        return builder.toString();
    }

    private static final String COLUMN_SEPARATOR ="|";

    public String toMarkdownTableLine(){
        StringBuilder builder=new StringBuilder();
        builder.append(COLUMN_SEPARATOR).append(fieldName)
                .append(COLUMN_SEPARATOR).append(description)
                .append(COLUMN_SEPARATOR).append(dbName)
                .append(COLUMN_SEPARATOR).append(dbType)
                .append(COLUMN_SEPARATOR).append(trueTickOrEmpty(isPrimaryKey))
                .append(COLUMN_SEPARATOR).append(trueTickOrEmpty(index))
                .append(COLUMN_SEPARATOR).append(trueTickOrEmpty(autoIncrement));
        builder.append(COLUMN_SEPARATOR);
        return builder.toString();
    }

    private static String trueTickOrEmpty(boolean b){
        return b?"√":"";
    }
}
