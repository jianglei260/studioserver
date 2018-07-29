package com.sharevar.appstudio.repository;

import com.google.gson.JsonObject;
import com.sharevar.appstudio.common.convert.ProjectTypeConverter;
import com.sharevar.appstudio.data.Entity;
import com.sharevar.appstudio.data.Response;
import com.sharevar.appstudio.object.function.Function;
import com.sharevar.appstudio.persitent.logic.Rule;
import org.springframework.stereotype.Repository;

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

    String save(JsonObject jsonObject, Boolean inflate) {
        return dbEngine.insertOrUpdate(entity, jsonObject, inflate);
    }

    List<String> saveAll(JsonObject jsonObject, Boolean inflate) {
        return Arrays.asList(dbEngine.insertOrUpdate(entity, jsonObject, inflate).split("[,]"));
    }

    T findById(String objectId, Boolean fetch) {
        List<T> result = dbEngine.query(entity, Arrays.asList(Rule.query(), Rule.id(objectId)), fetch);
        return result.size() > 0 ? result.get(0) : null;
    }

    List<T> findAll(Boolean fetch) {
        return dbEngine.query(entity, Arrays.asList(Rule.query()), fetch);
    }

    List<T> findAllById(List<String> ids, Boolean fetch) {
        List<T> results = new ArrayList<>();
        for (String id : ids) {
            results.add(findById(id, fetch));
        }
        return results;
    }

    List<T> findByRules(List<Rule> rules,Boolean fetch){
        List<T> result = dbEngine.query(entity, rules, fetch);
        return result;
    }
    Long count() {
        return dbEngine.count(entity, Arrays.asList(Rule.query(), Rule.count()));
    }

    boolean deleteById(String objectId,Boolean fetch){
       return dbEngine.delete(entity, Arrays.asList(Rule.delete(), Rule.id(objectId)),fetch);
    }

    Boolean deleteByRules(List<Rule> rules, Boolean fetch){
        Boolean result = dbEngine.delete(entity, rules, fetch);
        return result;
    }
    List<Boolean> deleteAll(List<String> ids,Boolean fetch){
        List<Boolean> results = new ArrayList<>();
        for (String id : ids) {
            results.add(deleteById(id, fetch));
        }
        return results;
    }

    Boolean deleteAll(Boolean fetch){
       return dbEngine.delete(entity, Arrays.asList(Rule.delete()),fetch);
    }
}
