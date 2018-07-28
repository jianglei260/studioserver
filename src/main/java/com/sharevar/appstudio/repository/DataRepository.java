package com.sharevar.appstudio.repository;

import com.google.gson.JsonObject;
import com.sharevar.appstudio.common.convert.ProjectTypeConverter;
import com.sharevar.appstudio.data.Entity;
import com.sharevar.appstudio.data.Response;
import com.sharevar.appstudio.object.function.Function;
import com.sharevar.appstudio.persitent.logic.Rule;

import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataRepository<T> {
    private DBEngine dbEngine;
    private EntityRepository entityRepository;
    private String type;
    private Entity entity;

    public DataRepository() {
        this.dbEngine = DBEngine.getInstance();
        entityRepository = EntityRepository.getInstance();
    }

    public Object exexut(Function function) {
        Object vaule = ProjectTypeConverter.invokeClassFunction(this, function);
        type = function.getPath();
        entity = entityRepository.find(type);
        return vaule;
    }

    String save(JsonObject jsonObject, boolean inflate) {
        return dbEngine.insertOrUpdate(entity, jsonObject, inflate);
    }

    List<String> saveAll(JsonObject jsonObject, boolean inflate) {
        return Arrays.asList(dbEngine.insertOrUpdate(entity, jsonObject, inflate).split("[,]"));
    }

    T findById(String objectId, boolean fetch) {
        List<T> result = dbEngine.query(entity, Arrays.asList(Rule.query(), Rule.id(objectId)), fetch);
        return result.size() > 0 ? result.get(0) : null;
    }

    List<T> findAll(boolean fetch) {
        return dbEngine.query(entity, Arrays.asList(Rule.query()), fetch);
    }

    List<T> findAllById(List<String> ids, boolean fetch) {
        List<T> results = new ArrayList<>();
        for (String id : ids) {
            results.add(findById(id, fetch));
        }
        return results;
    }

    List<T> findByRules(List<Rule> rules,boolean fetch){
        List<T> result = dbEngine.query(entity, rules, fetch);
        return result;
    }
    Long count() {
        return dbEngine.count(entity, Arrays.asList(Rule.query(), Rule.count()));
    }

    boolean deleteById(String objectId,boolean fetch){
       return dbEngine.delete(entity, Arrays.asList(Rule.delete(), Rule.id(objectId)),fetch);
    }

    Boolean deleteByRules(List<Rule> rules, boolean fetch){
        Boolean result = dbEngine.delete(entity, rules, fetch);
        return result;
    }
    List<Boolean> deleteAll(List<String> ids,boolean fetch){
        List<Boolean> results = new ArrayList<>();
        for (String id : ids) {
            results.add(deleteById(id, fetch));
        }
        return results;
    }

    Boolean deleteAll(boolean fetch){
       return dbEngine.delete(entity, Arrays.asList(Rule.delete()),fetch);
    }
}
