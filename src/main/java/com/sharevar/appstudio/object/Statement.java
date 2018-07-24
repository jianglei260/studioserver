package com.sharevar.appstudio.object;

import com.sharevar.appstudio.object.function.Function;

import java.io.Serializable;
import java.util.Date;

public class Statement implements Serializable {
    private Variable retVaule;
    private Function function;
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

    public Variable getRetVaule() {
        return retVaule;
    }

    public void setRetVaule(Variable retVaule) {
        this.retVaule = retVaule;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }
}
