package com.sharevar.appstudio.object.function;

import com.sharevar.appstudio.data.BaseObject;
import com.sharevar.appstudio.object.Type;

import java.util.Date;

public class Parameter extends BaseObject {
    private Type type;
    private String name;
    private Object defaultValue;
    private Object value;
    public String objectId="";
    public Date createdAt=new Date();
    public Date updateAt=new Date();

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getTypeName() {
        return getClass().getSimpleName();
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
