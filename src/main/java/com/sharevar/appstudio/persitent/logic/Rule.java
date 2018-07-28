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
    public static Rule query(){
        Rule rule=new Rule();
        rule.setOp(Op.QUERY);
        return rule;
    }
    public static Rule id(String id){
        Rule rule=new Rule();
        rule.setOp(Op.EQUALTO);
        rule.setColum("objectId");
        rule.setValue(id);
        return rule;
    }
    public static Rule delete(){
        Rule rule=new Rule();
        rule.setOp(Op.DELETE);
        return rule;
    }
    public static Rule count(){
        Rule rule=new Rule();
        rule.setOp(Op.COUNT);
        return rule;
    }
    public static Rule equal(){
        Rule rule=new Rule();
        rule.setOp(Op.EQUALTO);
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
