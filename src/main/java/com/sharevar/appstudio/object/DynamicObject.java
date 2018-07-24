package com.sharevar.appstudio.object;

import com.sharevar.appstudio.data.BaseObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DynamicObject extends BaseObject {
    private Type type;
    private Map<String, Object> attrs=new HashMap<>();
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
        return type.getName();
    }

    public DynamicObject(Type type) {
        this.type = type;
        attrs = new HashMap<>();
    }

    public Type getType() {
        return type;
    }

    public void set(String name, Object value) {
        attrs.put(name, value);
    }

    public Object get(String name) {
        return attrs.get(name);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }
}
