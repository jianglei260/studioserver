package com.sharevar.appstudio.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sharevar.appstudio.common.ds.CollectionOP;
import com.sharevar.appstudio.data.BaseObject;
import com.sharevar.appstudio.data.Entity;
import com.sharevar.appstudio.object.DynamicObject;
import com.sharevar.appstudio.object.Field;
import com.sharevar.appstudio.object.Type;
import com.sharevar.appstudio.persitent.logic.Op;
import com.sharevar.appstudio.persitent.logic.Rule;
import com.sharevar.appstudio.stand.type.TypeInference;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DBEngine {
    private static DBEngine instance;
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/studio?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&useSSL=false";
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
            e.printStackTrace();
        }
    }


    public boolean delete(Entity entity, List<Rule> rules, boolean fetch) {
        String sql = buildQuerySQL(entity, rules, fetch);
        return executeDelete(entity, sql, fetch);
    }

    public <T> List<T> query(Entity entity, List<Rule> rules, boolean fetch) {
        String sql = buildQuerySQL(entity, rules, fetch);
        return executeQuery(entity, sql, fetch);
    }

    public String insertOrUpdate(Entity entity, JsonElement jsonObject, boolean inflate) {
        StringBuilder ids = new StringBuilder();
        if (jsonObject.isJsonArray()) {
            for (JsonElement jsonElement : jsonObject.getAsJsonArray()) {
                ids.append(executeInsertOrUpdate(entity, (JsonObject) jsonElement, inflate));
                ids.append(",");
            }
        } else {
            ids.append(executeInsertOrUpdate(entity, jsonObject, inflate));
        }
        return ids.toString();
    }

    public boolean createTableIfNotExist(Entity entity) {
        //todo
        List<Field> insertFields = new ArrayList<>(entity.getFields());
        List<Field> deleteFields = new ArrayList<>();
        List<Field> modifyFields = new ArrayList<>();
        String sql = "SELECT column_name,column_type FROM information_schema.columns WHERE table_schema = 'studio'  AND table_name='" + entity.getSimpleName() + "';";
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                do {
                    String columnName = resultSet.getString("column_name");
                    String columnType = resultSet.getString("column_type");
                    Field field = CollectionOP.findByAttr(entity.getFields(), "name", columnName);
                    if (field != null) {
                        if (!columnType.equalsIgnoreCase(EntityRepository.getDBType(field.typeName()))) {
                            modifyFields.add(field);
                        }
                        insertFields.remove(field);
                    } else {
                        deleteFields.add(field);
                    }
                } while (resultSet.next());
                StringBuilder allBuilder = new StringBuilder();
                if (modifyFields.size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (Field modifyField : modifyFields) {
                        builder.append("ALTER TABLE " + entity.getSimpleName() + " CHANGE " + modifyField.getName() + " " + modifyField.getName() + " " + EntityRepository.getDBType(modifyField.typeName()) + ";");
                    }
                    allBuilder.append(builder);
                }
                if (deleteFields.size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (Field deleteField : deleteFields) {
                        builder.append("ALTER TABLE " + entity.getSimpleName() + " DROP " + deleteField.getName() + ";");
                    }
                    allBuilder.append(builder);
                }
                if (insertFields.size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (Field insertField : insertFields) {
                        builder.append("ALTER TABLE " + entity.getSimpleName() + " ADD " + insertField.getName() + " " + EntityRepository.getDBType(insertField.typeName()) + ";");
                    }
                    allBuilder.append(builder);
                }
                if (allBuilder.length() > 0) {
                    String alterSql = allBuilder.toString();
                    Statement alterStatement = con.createStatement();
                    String[] sqls = alterSql.split(";");
                    for (String s : sqls) {
                        alterStatement.addBatch(s);
                    }
                    System.out.println(alterSql);
                    alterStatement.executeBatch();
                    return true;
                } else {
                    return false;
                }
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("CREATE TABLE IF NOT EXISTS " + "`" + entity.getSimpleName() + "`(");
                for (Field insertField : insertFields) {
                    builder.append("`" + insertField.getName() + "` " + EntityRepository.getDBType(insertField.typeName()) + ",");
                }
                builder.append("PRIMARY KEY ( `objectId` ))ENGINE=InnoDB DEFAULT CHARSET=utf8;");
                String createSql = builder.toString();
                System.out.println(createSql);
                con.createStatement().execute(createSql);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String buildQuerySQL(Entity entity, List<Rule> rules, boolean fetch) {
        StringBuilder builder = new StringBuilder();
        for (Rule rule : rules) {
            switch (rule.getOp()) {
                case QUERY:
                    if (builder.length() > 0) {
                        builder.append(";");
                    }
                    builder.append("select * from ");
                    builder.append(entity.getSimpleName());
                    break;
                case DELETE:
                    if (builder.length() > 0) {
                        builder.append(";");
                    }
                    builder.append("delete from ");
                    builder.append(entity.getSimpleName());
                    break;
                case WHERE:
                    builder.append(" where ");
                    break;
                case COUNT:
                    int index=builder.indexOf("*");
                    builder.replace(index,index+1,"count(objectID) ");
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
                    if (rule.getValue() instanceof String) {
                        builder.append("'");
                    }
                    builder.append(rule.getValue());
                    if (rule.getValue() instanceof String) {
                        builder.append("'");
                    }
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

    //todo 删除相关数据
    private boolean executeDelete(Entity entity, String sql, boolean fetch) {
        try {
            Statement statement = con.createStatement();
            statement.execute(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Long count(Entity entity,List<Rule> rules){
        String sql=buildQuerySQL(entity,rules,false);
        Statement statement = null;
        try {
            statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.getLong(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    private List executeQuery(Entity entity, String sql, boolean fetch) {
        System.out.println(sql);
        List<Object> results = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Object object = createObject(entity, resultSet, fetch);
                results.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    //todo 更新逻辑，按其他条件更新，不仅仅是是ID
    private String executeInsertOrUpdate(Entity entity, JsonElement jsonElement, boolean inflate) {
        StringBuilder builder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();
        JsonObject jsonObject = (JsonObject) jsonElement;
        boolean exist = false;
        String objectId = "";

        objectId = jsonObject.get("objectId").getAsString();
        if (objectId != null && objectId.length() > 0) {
            exist = true;
            builder.append("update ");
            builder.append(entity.getSimpleName());
            builder.append(" set ");
        } else {
            exist = false;
            objectId = UUID.randomUUID().toString().replaceAll("-", "");
            builder.append("insert into ");
            builder.append(entity.getSimpleName());
            builder.append(" (objectId");
            valuesBuilder.append("values(");
            valuesBuilder.append("'");
            valuesBuilder.append(objectId);
            valuesBuilder.append("'");
        }
        for (Field field : entity.getFields()) {
            String fieldName = field.getName();
            Object value = null;
            if (field.getName().equals("objectId")) {
                continue;
            }
            if (!jsonObject.has(fieldName))
                continue;
            if (EntityRepository.getInstance().isJavaBuiltinType(field.getType())) {
                value = getJsonPrimitiveAttr(field.getName(), jsonObject);
            } else if (field.isCollection()) {
                JsonArray array = jsonObject.getAsJsonArray(field.getName());
                Type parameterizedType = field.getType().getParameterizedType();
                StringBuilder values = new StringBuilder();
                if (EntityRepository.getInstance().isJavaBuiltinType(parameterizedType)) {
                    for (JsonElement element : array) {
                        values.append(element.getAsString());
                        values.append(",");
                    }
                } else {
                    if (inflate) {
                        for (JsonElement element : array) {
                            Entity fieldEntity = EntityRepository.getInstance().find(parameterizedType.getName());
                            String inflateObjectId = executeInsertOrUpdate(fieldEntity, element.getAsJsonObject(), true);
                            values.append(inflateObjectId);
                            values.append(",");
                        }
                    } else {
                        for (JsonElement element : array) {
                            String id = element.getAsJsonObject().get("objectId").getAsString();
                            values.append(id);
                            values.append(",");
                        }
                    }
                }
                value = values.toString();
            } else {
                if (jsonObject.has(fieldName)){
                    JsonObject inflateJsonObject = jsonObject.get(fieldName).getAsJsonObject();
                    value = executeInsertOrUpdate(EntityRepository.getInstance().find(field.typeName()), inflateJsonObject, inflate);
                }
            }
            boolean isString = value instanceof String;
            if (exist) {
                builder.append(fieldName);
                builder.append("=");
                if (isString) {
                    builder.append("'");
                }
                builder.append(value);
                if (isString) {
                    builder.append("'");
                }
                builder.append(",");
            } else {
                builder.append(",");
                builder.append(fieldName);
                valuesBuilder.append(",");
                if (isString) {
                    valuesBuilder.append("'");
                }
                valuesBuilder.append(value);
                if (isString) {
                    valuesBuilder.append("'");
                }

            }
        }
        if (exist) {
            builder.deleteCharAt(builder.length() - 1);
            builder.append(" ");
            builder.append("where objectId = '" + objectId + "'");
        } else {
            valuesBuilder.append(")");
            builder.append(")");
        }

        builder.append(valuesBuilder);
        String sql = builder.toString();
        System.out.println(sql);
        try {
            Statement statement = con.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectId;
    }

    private Object getJsonPrimitiveAttr(String name, JsonObject jsonObject) {
        JsonElement element = jsonObject.get(name);
        try {
            java.lang.reflect.Field field = element.getClass().getDeclaredField("value");
            field.setAccessible(true);
            return field.get(element);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setObjectId(Object object, String id) {
        try {
            Method method = object.getClass().getMethod("setObjectId", String.class);
            if (method != null) {
                method.invoke(object, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCreatedAt(Object object, Date createAt) {
        try {
            Method method = object.getClass().getMethod("setCreatedAt", Date.class);
            if (method != null) {
                method.invoke(object, createAt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpdatedAt(Object object, Date date) {
        try {
            Method method = object.getClass().getMethod("setUpdateAt", Date.class);
            if (method != null) {
                method.invoke(object, date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object createObject(Entity entity, ResultSet resultSet, boolean fetch) {
        Class clazz = EntityRepository.getInstance().findClassForEntity(entity);
        try {
            Object object =  clazz.newInstance();
            for (Field field : entity.getFields()) {
                if (field.getName().equals("objectId")) {
                    setObjectId(object, resultSet.getString("objectId"));
                } else if (field.getName().equals("createdAt")) {
                    setCreatedAt(object, resultSet.getDate("createdAt"));
                } else if (field.getName().equals("updateAt")) {
                    setUpdatedAt(object, resultSet.getDate("updateAt"));
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
                            Entity fieldEntity = EntityRepository.getInstance().find(parameterizedType.getName());
                            if (fetch) {
                                Rule inRule = new Rule();
                                inRule.setOp(Op.IN);
                                inRule.setValue("(" + ids + ")");
                                inRule.setColum("objectId");
                                String sql = buildQuerySQL(fieldEntity, Arrays.asList(Rule.where(), inRule), true);
                                List result = executeQuery(fieldEntity, sql, true);
                                inflateField(object, field.getName(), result);
                            } else {
                                String objectIds[] = ids.split(",");
                                List list = new ArrayList();
                                for (String objectId : objectIds) {
                                    Object baseObject = EntityRepository.getInstance().findClassForEntity(fieldEntity).newInstance();
                                    setObjectId(baseObject, objectId);
                                    list.add(baseObject);
                                }
                                inflateField(object, field.getName(), list);
                            }
                        }
                    } else if (!EntityRepository.getInstance().isJavaBuiltinType(field.getType())) {
                        Entity fieldEntity = EntityRepository.getInstance().find(field.getType().getName());
                        Rule inRule = new Rule();
                        inRule.setOp(Op.EQUALTO);
                        inRule.setValue(resultSet.getString(field.getName()));
                        inRule.setColum("objectId");
                        String sql = buildQuerySQL(fieldEntity, Arrays.asList(Rule.query(), Rule.where(), inRule), fetch);
                        List result = executeQuery(fieldEntity, sql, true);
                        inflateField(object, field.getName(), result);
                    } else {
                        inflateField(object, field.getName(), resultSet.getObject(field.getName()));
                    }
                }
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void inflateField(Object baseObject, String filedName, Object value) {
        if (baseObject instanceof DynamicObject) {
            ((DynamicObject) baseObject).getAttrs().put(filedName, value);
        } else {
            setField(baseObject, filedName, value);
        }
    }

    private void setField(Object o, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(o, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
