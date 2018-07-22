package com.sharevar.appstudio.repository;

import com.sharevar.appstudio.common.ds.CollectionOP;
import com.sharevar.appstudio.data.Entity;
import com.sharevar.appstudio.object.DynamicObject;
import com.sharevar.appstudio.object.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRepository {
    private static EntityRepository instance;
    private static List<Entity> entities = new ArrayList<>();
    private static Map<String, Class> builtinType = new HashMap<>();

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
        /*************************project builtin type***********************/
    }

    public static EntityRepository getInstance() {
        if (instance == null)
            instance = new EntityRepository();
        return instance;
    }

    public Entity find(String name) {
        return CollectionOP.findByAttr(entities, "name", name);
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

    public boolean isJavaBuiltinType(Type type){
       return builtinType.containsKey(type.getName());
    }
    public boolean isDynamicType(Object o) {
        return o instanceof DynamicObject;
    }

    public void add(List<Entity> entities) {
        this.entities.addAll(entities);
    }
}
