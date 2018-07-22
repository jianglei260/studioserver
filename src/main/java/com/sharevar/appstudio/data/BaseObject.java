package com.sharevar.appstudio.data;

import com.sharevar.appstudio.object.Type;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseObject {
    private String objectId;
    private Date createdAt;
    private Date updateAt;

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

    public Map<String, Object> attrs() {
        Field[] fields = getClass().getFields();
        HashMap<String, Object> attrs = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                attrs.put(field.getName(), field.get(this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return attrs;
    }
}
