package com.sharevar.appstudio.object;

import com.sharevar.appstudio.data.BaseObject;

public class Field extends BaseObject {
    private Type type;
    private String name;
    private Object defaultValue;
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

    public boolean isCollection(){
        return type.isCollection();
    }
}