package com.iisimpler.mysql2entity;

import com.iisimpler.mysql2entity.entity.FieldInfo;
import com.iisimpler.mysql2entity.entity.TableInfo;
import com.iisimpler.mysql2entity.util.DBUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将Mysql数据库转为Java实体类
 * @author iisimpler
 */
public class Mysql2EntityGenerator {

    private static String packageStr;
    private static String outputPath;
    private static String[] tableNameList;
    private static String author;

    private static ResourceBundle rb = ResourceBundle.getBundle("mysql2EntityGenerator");

    static {
        packageStr = rb.getString("packageStr");
        outputPath = rb.getString("outputPath");
        tableNameList = rb.getString("tableNameList").split(",");
        author = rb.getString("author");
    }

    private static Connection connection = DBUtils.getConnection();

    public static void main(String[] args) {
        new Mysql2EntityGenerator().process();
    }

    /**
     * 获取数据库中表的详细信息
     *
     * @return tableInfoList
     */
    private List<TableInfo> getTableInfoList() {
        List<TableInfo> tableInfoList = new ArrayList<>();
        if (tableNameList == null || tableNameList.length == 0) {
            String sql = "show table status";
            tableInfoList = quryTableInfo(sql);
        } else {
            for (String tableName : tableNameList) {
                String sql = String.format("show table status like '%s'", tableName);
                tableInfoList.addAll(quryTableInfo(sql));
            }
        }
        return tableInfoList;
    }

    private List<TableInfo> quryTableInfo(String sql) {
        ArrayList<TableInfo> tableInfoList = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setComment(rs.getString("Comment"));
                if ("VIEW".equals(tableInfo.getComment())) {
                    continue;
                }
                tableInfo.setName(rs.getString("Name"));
                tableInfo.setRows(rs.getString("Rows"));
                tableInfo.setDataLength(rs.getString("Data_length"));
                tableInfo.setCreateTime(rs.getDate("Create_time"));
                tableInfo.setUpdateTime(rs.getDate("Update_time"));
                tableInfoList.add(tableInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tableInfoList;
    }

    /**
     * 根据表名获取其中的字段详细信息
     *
     * @param tableName 表名
     * @return fieldInfoList
     */
    private List<FieldInfo> getFieldInfoList(String tableName) {
        ArrayList<FieldInfo> fieldInfoList = new ArrayList<>();

        String sql = "show full fields from " + tableName;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            //ps.setString(1,tableName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setField(rs.getString("Field"));
                fieldInfo.setType(rs.getString("Type"));
                fieldInfo.setNull(rs.getBoolean("Null"));
                fieldInfo.setPrimaryKey("PRI".equals(rs.getString("Key")));
                fieldInfo.setComment(rs.getString("Comment"));
                fieldInfoList.add(fieldInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fieldInfoList;
    }

    /**
     * 转换字符串为驼峰式
     *
     * @param str 字符串
     * @param isFirstLetterUpper 首字母是否大写
     * @return parseStr
     */
    private String parseStr(String str, boolean isFirstLetterUpper) {
        String parseStr = str;

        String pattern = "_.";
        Matcher m = Pattern.compile(pattern).matcher(parseStr);
        while (m.find()) {
            String reStr = m.group().toUpperCase().replace("_", "");
            parseStr = parseStr.replaceFirst(pattern, reStr);
        }
        if (isFirstLetterUpper) {
            parseStr = parseStr.replaceFirst(parseStr.substring(0, 1), parseStr.substring(0, 1).toUpperCase());
        }

        return parseStr;
    }

    /**
     * 将数据库数据类型转为Java类型
     *
     * @param sqlType 数据库类型
     * @return Java类型
     */
    private String parseType(String sqlType) {
        if (sqlType.contains("bigint")) {
            return "Long";
        }
        if (sqlType.contains("int")) {
            return "Integer";
        }
        if (sqlType.contains("char") || sqlType.contains("enum")) {
            return "String";
        }
        if (sqlType.contains("date")||sqlType.contains("time")) {
            return "Date";
        }
        if (sqlType.contains("decimal")||sqlType.contains("numeric")) {
            return "BigDecimal";
        }
        if (sqlType.contains("float")) {
            return "Float";
        }
        if (sqlType.contains("double")) {
            return "Double";
        }
        if (sqlType.contains("bit")) {
            return "Boolean";
        }
        if (sqlType.contains("image")) {
            return "Blob";
        }
        if (sqlType.contains("text")) {
            return "Clob";
        }

        return null;
    }

    /**
     * 根据表信息生成实体字符串
     *
     * @param tableInfo 表对象
     * @return 实体字符串
     */
    private String processDomainStr(TableInfo tableInfo) {

        StringBuilder classStr = new StringBuilder();
        classStr.append(String.format("/**\n * %s %s\n * @author %s  %s\n*/\n", tableInfo.getName(), tableInfo.getComment(),author,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        classStr.append(String.format("@Entity\n@Table(name = \"%s\")\n", tableInfo.getName()));
        String className = parseStr(tableInfo.getName(), true);
        classStr.append("public class ").append(className).append(" implements Serializable {\n\t");

        StringBuilder importStr = new StringBuilder();
        importStr.append("import java.io.Serializable;\n");
        importStr.append("import javax.persistence.Entity;\n");
        importStr.append("import javax.persistence.Table;\n");
        importStr.append("import javax.persistence.Id;\n");
        importStr.append("import javax.persistence.Column;\n");
        importStr.append("import javax.persistence.GeneratedValue;\n");
        importStr.append("import javax.persistence.GenerationType;\n");

        StringBuilder methodStr = new StringBuilder();

        StringBuilder toStringStr = new StringBuilder();
        StringBuilder toStringStrTemp = new StringBuilder();// deptNo='"+deptNo+"',


        StringBuilder attrStr = new StringBuilder();
        attrStr.append("\tprivate static final long serialVersionUID = 1L;\n\n");

        List<FieldInfo> fieldInfoList = tableInfo.getFieldInfoList();
        boolean isFirst= true;
        for (FieldInfo fieldInfo : fieldInfoList) {
            if (fieldInfo.getType().contains("date")||fieldInfo.getType().contains("time") && !importStr.toString().contains("java.util.Date") && !importStr.toString().contains("Temporal")) {
                importStr.append("import java.util.Date;\n");
                importStr.append("import javax.persistence.Temporal;\n");
                importStr.append("import javax.persistence.TemporalType;\n");
                importStr.append("import org.springframework.format.annotation.DateTimeFormat;\n");
            }
            if (fieldInfo.getType().contains("text")) {
                importStr.append("import java.sql.Clob;\n");
            }
            if (fieldInfo.getType().contains("image")) {
                importStr.append("import java.sql.Blob;\n");
            }

            if (!fieldInfo.getComment().isEmpty()) {
                attrStr.append(String.format("\t/** %s */\n", fieldInfo.getComment()));
            }
            if (fieldInfo.getPrimaryKey()&&isFirst) {
                attrStr.append("\t@Id\n\t@GeneratedValue(strategy = GenerationType.AUTO)\n");
                isFirst = false;
            }
            if (fieldInfo.getType().contains("date")||fieldInfo.getType().contains("time")) {
                attrStr.append("\t@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n");
                attrStr.append("\t@Temporal(value = TemporalType.TIMESTAMP)\n");
            }
            attrStr.append(String.format("\t@Column(name = \"%s\")\n",fieldInfo.getField()));
            String fieldName = parseStr(fieldInfo.getField(), false);
            attrStr.append(String.format("\tprivate %s %s;\n\n", parseType(fieldInfo.getType()), fieldName));

            String getter = String.format("\tpublic %s get%s() {\n\t\treturn %s;\n\t}\n\n ", parseType(fieldInfo.getType()), parseStr(fieldInfo.getField(), true), fieldName);
            String setter = String.format("\tpublic void set%s(%s %s) {\n\t\tthis.%s = %s;\n\t}\n\n", parseStr(fieldInfo.getField(), true), parseType(fieldInfo.getType()), fieldName, fieldName, fieldName);
            methodStr.append(getter).append(setter);

            toStringStrTemp.append(","+ fieldName +"='+\" + "+ fieldName +" + \"'");
        }
        toStringStr.append("\t@Override\n\tpublic String toString() {\n\t\treturn \""+className+"{"+toStringStrTemp.substring(1)+"}\";\n\t}\n");
        return new StringBuilder("package ").append(packageStr).append(";\n\n").append(importStr).append("\n").append(classStr).append("\n").append(attrStr).append(methodStr).append(toStringStr).append("\n}").toString();
    }


    /**
     * 创建实体类文件
     * @param fileName 文件名
     * @param fileContent 文件内容
     * @return 是否成功创建
     */
    private boolean createEntityFile(String fileName, String fileContent) {
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            String outputPathTemp = outputPath + "/" + fileName + ".java";
            fw = new FileWriter(outputPathTemp);
            pw = new PrintWriter(fw);
            pw.println(fileContent);
            pw.flush();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw!=null)
                pw.close();
            if (fw!=null)
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return false;
    }

    private void process() {
        List<TableInfo> tableInfoList = getTableInfoList();
        int numSuccess = 0;
        int numFail= 0;
        for (TableInfo tableInfo : tableInfoList) {
            List<FieldInfo> fieldInfoList = getFieldInfoList(tableInfo.getName());
            tableInfo.setFieldInfoList(fieldInfoList);
            String domainStr = processDomainStr(tableInfo);
            String fileName = parseStr(tableInfo.getName(), true);
            boolean isSuccess = createEntityFile(fileName, domainStr);
            if (isSuccess) {
                System.out.println("成功创建实体类 " + fileName + ".java");
                numSuccess++;
            } else {
                System.out.printf("未能成功创建实体类 "+ fileName + ".java");
                numFail++;
            }
        }
        System.out.println("success："+numSuccess+"  fail："+numFail);
    }


    @Deprecated
    public List getTableNames() {
        List<String> tableNames = new ArrayList<>();
        try {
            DatabaseMetaData dmd = connection.getMetaData();
            ResultSet rs = dmd.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tableNames;
    }


}
