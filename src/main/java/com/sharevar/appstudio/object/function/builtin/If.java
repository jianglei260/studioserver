package com.sharevar.appstudio.object.function.builtin;

import com.sharevar.appstudio.object.function.Function;
import com.sharevar.appstudio.object.function.ReturnType;

import java.util.Date;

public class If extends Function {
    CodeBlock codeBlock;
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

    @Override
    public ReturnType invoke() {
        if (codeBlock != null) {
            codeBlock.invoke();
        }
        return new Void();
    }
}
