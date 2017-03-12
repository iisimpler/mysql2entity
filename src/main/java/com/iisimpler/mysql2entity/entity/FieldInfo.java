package com.iisimpler.mysql2entity.entity;

/**
 * 字段的详细信息
 */
public class FieldInfo {
    private String field;
    private String type;
    private Boolean isNull;
    private Boolean primaryKey;
    private String comment;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getNull() {
        return isNull;
    }

    public void setNull(Boolean aNull) {
        isNull = aNull;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "field='" + field + '\'' +
                ", type='" + type + '\'' +
                ", isNull=" + isNull +
                ", primaryKey=" + primaryKey +
                ", comment='" + comment + '\'' +
                '}';
    }
}
