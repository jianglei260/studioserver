package com.sharevar.appstudio.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Layout extends BaseObject {
    private Widget widget;
    private List<Attr> layoutAttrs = new ArrayList<>();
    private List<Action> bindActions = new ArrayList<>();
    private List<Layout> children = new ArrayList<>();
    private List<Attr> bindAttrs = new ArrayList<>();

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


    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public List<Attr> getLayoutAttrs() {
        return layoutAttrs;
    }

    public void setLayoutAttrs(List<Attr> layoutAttrs) {
        this.layoutAttrs = layoutAttrs;
    }

    public List<Action> getBindActions() {
        return bindActions;
    }

    public void setBindActions(List<Action> bindActions) {
        this.bindActions = bindActions;
    }

    public List<Layout> getChildren() {
        return children;
    }

    public void setChildren(List<Layout> children) {
        this.children = children;
    }

    public List<Attr> getBindAttrs() {
        return bindAttrs;
    }

    public void setBindAttrs(List<Attr> bindAttrs) {
        this.bindAttrs = bindAttrs;
    }
}
