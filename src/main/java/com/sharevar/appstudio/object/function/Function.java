package com.sharevar.appstudio.object.function;

import com.sharevar.appstudio.data.BaseObject;

import java.util.Date;
import java.util.List;

public abstract class Function extends BaseObject {
    protected List<Parameter> parameters;
    protected ReturnType returnType;
    protected String name;

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

    public abstract Object invoke();

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
