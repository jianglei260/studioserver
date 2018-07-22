package com.sharevar.appstudio.persitent.logic;

import com.sharevar.appstudio.data.Attr;

public enum Op {
    QUERY("QUERY"), INSERT("INSERT"), UPDATE("UPDATE"), DELETE("DELETE"),
    WHERE("WHERE"), AND("AND"), OR("OR"),
    EQUALTO("="), NOT_EQUALTO("><"), GREATER(">"), LESS("<"), GREATER_EQUAL(">="), LESS_EQUAL("<"),IN("IN"),
    LIMIT("LIMIT"), SKIP("OFFSET"),
    ORDER_BY("ORDER BY"),
    FETCH_LIST("FETCH_LIST");


    Op(String op) {
        this.value = op;
    }

    public String value;
}
