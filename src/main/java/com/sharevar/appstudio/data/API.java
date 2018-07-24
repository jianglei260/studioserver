package com.sharevar.appstudio.data;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class API extends BaseObject {
    private int type;//server or local
    private String name;
    private String path;
    private List<Attr> params=new ArrayList<>();
    private String method;
    private String responseType;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Attr> getParams() {
        return params;
    }

    public void setParams(List<Attr> params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
}
