package com.sharevar.appstudio.repository;

import com.sharevar.appstudio.common.ds.CollectionOP;
import com.sharevar.appstudio.data.BaseObject;
import com.sharevar.appstudio.data.Entity;
import com.sharevar.appstudio.object.DynamicObject;
import com.sharevar.appstudio.object.Field;
import com.sharevar.appstudio.object.Type;
import com.sharevar.appstudio.persitent.logic.Op;
import com.sharevar.appstudio.persitent.logic.Rule;
import com.sharevar.appstudio.stand.type.TypeInference;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBEngine {

    private static DBEngine instance;
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/sqltestdb";
    private String user = "root";
    private String password = "root";
    private Connection con;

    public static DBEngine getInstance() {
        if (instance == null)
            instance = new DBEngine();
        return instance;
    }

    public DBEngine() {

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed()) {
                System.out.println("Succeeded connecting to the Database!");
            }
        } catch (Exception e) {
        }
    }


    public void buildSQL(Entity entity, List<Rule> rules, boolean fetch) {
        StringBuilder builder = new StringBuilder();
        String sql = null;
        switch (rules.get(0).getOp()) {
            case QUERY:
                sql = buildQuerySQL(entity, rules.subList(1, rules.size()), builder, fetch);
                break;

        }
        executeQuery(entity, sql, fetch);
    }

    public String buildQuerySQL(Entity entity, List<Rule> rules, StringBuilder builder, boolean fetch) {
        builder.append("select * from");
        builder.append(entity.getName());
        for (Rule rule : rules) {
            switch (rule.getOp()) {
                case WHERE:
                    builder.append(" where ");
                    break;
                case EQUALTO:
                case GREATER:
                case NOT_EQUALTO:
                case LESS:
                case GREATER_EQUAL:
                case LESS_EQUAL:
                case IN:
                    builder.append(rule.getColum());
                    builder.append(rule.getOp().value);
                    builder.append(rule.getValue());
                    builder.append(" ");
                    break;
                case AND:
                case OR:
                    builder.append(rule.getOp().value);
                    builder.append(" ");
                case SKIP:
                case LIMIT:
                    builder.append(rule.getOp().value);
                    builder.append(" ");
                    builder.append(rule.getValue());
            }
        }
        String sql = builder.toString();
        return sql;
    }

    private List executeQuery(Entity entity, String sql, boolean fetch) {
        System.out.println(sql);
        List<BaseObject> results = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                BaseObject object = createObject(entity, resultSet, fetch);
                results.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private BaseObject createObject(Entity entity, ResultSet resultSet, boolean fetch) {
        Class clazz = EntityRepository.getInstance().findClassForEntity(entity);
        try {
            BaseObject object = (BaseObject) clazz.newInstance();
            for (Field field : entity.getFields()) {
                if (field.getName().equals("objectId")) {
                    object.setObjectId(resultSet.getString("objectId"));
                } else if (field.getName().equals("createdAt")) {
                    object.setCreatedAt(resultSet.getDate("createdAt"));
                } else if (field.getName().equals("createdAt")) {
                    object.setCreatedAt(resultSet.getDate("createdAt"));
                } else {
                    if (field.isCollection()) {
                        Type parameterizedType = field.getType().getParameterizedType();
                        String ids = resultSet.getString(field.getName());
                        if (EntityRepository.getInstance().isJavaBuiltinType(parameterizedType)) {
                            String values[] = ids.split(",");
                            List list = new ArrayList();
                            for (String value : values) {
                                list.add(TypeInference.get().convertTo(value, parameterizedType));
                            }
                            inflateField(object, field.getName(), list);
                        } else {
                            if (fetch) {
                                StringBuilder builder = new StringBuilder();
                                Entity fieldEntity = EntityRepository.getInstance().find(parameterizedType.getName());
                                Rule inRule = new Rule();
                                inRule.setOp(Op.IN);
                                inRule.setValue("(" + ids + ")");
                                inRule.setColum("objectId");
                                String sql = buildQuerySQL(fieldEntity, Arrays.asList(Rule.where(), inRule), builder, true);
                                List result = executeQuery(fieldEntity, sql, true);
                                inflateField(object, field.getName(), result);
                            } else {
                                String objectIds[] = ids.split(",");
                                List list = new ArrayList();
                                for (String objectId : objectIds) {
                                    BaseObject baseObject = new BaseObject();
                                    baseObject.setObjectId(objectId);
                                    list.add(baseObject);
                                }
                                inflateField(object, field.getName(), list);
                            }
                        }
                    } else if (!EntityRepository.getInstance().isJavaBuiltinType(field.getType())) {
                        StringBuilder builder = new StringBuilder();
                        Entity fieldEntity = EntityRepository.getInstance().find(field.getType().getName());
                        Rule inRule = new Rule();
                        inRule.setOp(Op.EQUALTO);
                        inRule.setValue(resultSet.getString(field.getName()));
                        inRule.setColum("objectId");
                        String sql = buildQuerySQL(fieldEntity, Arrays.asList(Rule.where(), inRule), builder, fetch);
                        List result = executeQuery(fieldEntity, sql, true);
                        inflateField(object, field.getName(), result);
                    } else {
                        inflateField(object, field.getName(), resultSet.getObject(field.getName()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void inflateField(BaseObject baseObject, String filedName, Object value) {
        if (baseObject instanceof DynamicObject) {
            ((DynamicObject) baseObject).getAttrs().put(filedName, value);
        } else {
            setField(baseObject, filedName, value);
        }
    }

    public void setField(Object o, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = o.getClass().getField(fieldName);
            field.setAccessible(true);
            field.set(o, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
