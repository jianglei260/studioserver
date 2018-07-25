package com.sharevar.appstudio.repository;

import com.sharevar.appstudio.common.ds.CollectionOP;
import com.sharevar.appstudio.data.*;
import com.sharevar.appstudio.object.DynamicObject;
import com.sharevar.appstudio.object.Statement;
import com.sharevar.appstudio.object.Type;
import com.sharevar.appstudio.object.Variable;
import com.sharevar.appstudio.object.function.Function;
import com.sharevar.appstudio.object.function.Parameter;
import com.sharevar.appstudio.object.function.ReturnType;
import com.sharevar.appstudio.object.function.builtin.*;
import com.sharevar.appstudio.persitent.logic.Rule;

import java.lang.Void;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class EntityRepository {
    private static EntityRepository instance;
    private static Map<String, Entity> entities = new HashMap<>();
    private static Map<String, Class> builtinType = new HashMap<>();
    public static Map<String, String> dbTypeMap = new HashMap<>();
    public static final String VARCHAR="VARCHAR(256)";

    static {
        /**********************java builtin type*************************/
        builtinType.put(String.class.getName(), String.class);
        builtinType.put(String.class.getSimpleName(), String.class);
        builtinType.put("int", int.class);
        builtinType.put("Integer", Integer.class);
        builtinType.put(Integer.class.getName(), Integer.class);
        builtinType.put(long.class.getName(), long.class);
        builtinType.put(Long.class.getName(), Long.class);
        builtinType.put(double.class.getName(), Double.class);
        builtinType.put(Double.class.getName(), Double.class);
        builtinType.put(double.class.getSimpleName(), Double.class);
        builtinType.put(boolean.class.getSimpleName(), Boolean.class);
        builtinType.put(Boolean.class.getSimpleName(), Boolean.class);
        builtinType.put(Boolean.class.getName(), Boolean.class);
        builtinType.put(Date.class.getName(), Date.class);

        /*************************project builtin entity***********************/
        fromClass(Action.class);
        fromClass(API.class);
        fromClass(Attr.class);
        fromClass(BaseObject.class);
        fromClass(Entity.class);
        fromClass(Icon.class);
        fromClass(Layout.class);
        fromClass(Project.class);
        fromClass(Response.class);
        fromClass(Widget.class);
        fromClass(CodeBlock.class);
        fromClass(Else.class);
        fromClass(If.class);
        fromClass(Loop.class);
        fromClass(RemoteAPI.class);
        fromClass(Root.class);
        fromClass(Void.class);
        fromClass(Function.class);
        fromClass(Parameter.class);
        fromClass(ReturnType.class);
        fromClass(DynamicObject.class);
        fromClass(com.sharevar.appstudio.object.Field.class);
        fromClass(Statement.class);
        fromClass(Type.class);
        fromClass(Variable.class);
        fromClass(Rule.class);
        /*************************project db type map***********************/
        dbTypeMap.put(String.class.getName(),VARCHAR);
        dbTypeMap.put(Integer.class.getName(),"INTEGER");
        dbTypeMap.put(int.class.getName(),"INTEGER");
        dbTypeMap.put(long.class.getName(),"BIGINT");
        dbTypeMap.put(Long.class.getName(),"BIGINT");
        dbTypeMap.put(float.class.getName(),"FLOAT");
        dbTypeMap.put(Float.class.getName(),"FLOAT");
        dbTypeMap.put(double.class.getName(),"DOUBLE");
        dbTypeMap.put(Double.class.getName(),"DOUBLE");
        dbTypeMap.put(double.class.getName(),"DOUBLE");
        dbTypeMap.put(Date.class.getName(),"DATETIME");
        dbTypeMap.put(java.sql.Date.class.getName(),"DATETIME");
        dbTypeMap.put(List.class.getName(),"TEXT");
    }

    public static String getDBType(String type){
        String dbType=dbTypeMap.get(type);
        if (dbType==null){
            dbType=VARCHAR;
        }
        return dbType;
    }

    public static List<Entity> getEntities() {
        return new ArrayList<>(entities.values());
    }

    public void syncEntity(Entity entity){
        DBEngine.getInstance().createTableIfNotExist(entity);
    }

    public void syncEntities(){
        for (Entity entity : entities.values()) {
            syncEntity(entity);
        }
    }

    public Entity from(Class clazz){
        return entities.get(clazz.getName());
    }
    public static Entity fromClass(Class clazz) {
        Entity entity = entities.get(clazz.getName());
        if (entity != null)
            return entity;
        entity = new Entity();
        entity.setName(clazz.getName());
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            com.sharevar.appstudio.object.Field entityField = new com.sharevar.appstudio.object.Field();
            Type type = Type.of(field.getType());
            java.lang.reflect.Type genericType=field.getGenericType();
            if (genericType !=null&&genericType instanceof ParameterizedType) {
                String parameterizeTypeName = ((ParameterizedType) genericType).getActualTypeArguments()[0].getTypeName();
                type.setParameterizedType(Type.forName(parameterizeTypeName));
            }
            entityField.setType(type);
            entityField.setName(field.getName());
            entity.getFields().add(entityField);
        }
        entities.put(entity.getName(), entity);
        return entity;
    }

    public static EntityRepository getInstance() {
        if (instance == null)
            instance = new EntityRepository();
        return instance;
    }

    public Entity find(String name) {
        return entities.get(name);
    }

    public Class findClassForEntity(Entity entity) {
        Class clazz = builtinType.get(entity.getName());
        if (clazz == null) {
            try {
                clazz = Class.forName(entity.getName());
            } catch (Exception e) {
            }
            if (clazz == null) {
                return DynamicObject.class;
            }
        }
        return clazz;
    }

    public boolean isJavaBuiltinType(Type type) {
        return builtinType.containsKey(type.getName());
    }

    public boolean isDynamicType(Object o) {
        return o instanceof DynamicObject;
    }

    public void add(List<Entity> entities) {
        for (Entity entity : entities) {
            EntityRepository.entities.put(entity.getName(), entity);
        }
    }
}
