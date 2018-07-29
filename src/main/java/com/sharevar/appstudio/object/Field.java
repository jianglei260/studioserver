package com.sharevar.appstudio.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharevar.appstudio.data.BaseObject;

import java.util.Date;

public class Field extends BaseObject {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Type type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object defaultValue;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String objectId="";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Date createdAt=new Date();
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String typeName() {
        return type.getName();
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

    @JsonIgnore
    public boolean isCollection(){
        return type.isCollection();
    }
}
