package com.sharevar.appstudio.persitent.logic;

import com.sharevar.appstudio.data.BaseObject;

public class Rule extends BaseObject {
    private Op op;
    private String colum;
    private Object value;

    public static Rule where(){
        Rule rule=new Rule();
        rule.setOp(Op.WHERE);
        return rule;
    }
    public Op getOp() {
        return op;
    }

    public void setOp(Op op) {
        this.op = op;
    }

    public String getColum() {
        return colum;
    }

    public void setColum(String colum) {
        this.colum = colum;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
