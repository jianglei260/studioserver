package com.sharevar.appstudio.data;

import com.sharevar.appstudio.object.Field;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private String name;
    private List<Field> fields=new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimpleName(){
       String[] names=name.split("[.]");
        return names[names.length-1];
    }
    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj)
            return true;
        if (!(obj instanceof Entity))
            return false;
        if (this.name.equals(((Entity)obj).name))
            return true;
        return false;
    }
}
