package com.sharevar.appstudio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sharevar.appstudio.data.Entity;
import com.sharevar.appstudio.data.Icon;
import com.sharevar.appstudio.persitent.logic.Rule;
import com.sharevar.appstudio.repository.DBEngine;
import com.sharevar.appstudio.repository.EntityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


public class StudioserverApplicationTests {

    @Test
    public void contextLoads() {
        Entity entity=EntityRepository.fromClass(Entity.class);
//        Icon icon=new Icon();
//        icon.setName("image");
////        icon.setObjectId("3b40a4052cd046d8acda29ba7e96342d");
//        icon.setPath("https://gss1.bdstatic.com/-vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike150%2C5%2C5%2C150%2C50/sign=fd7aab5b0b3b5bb5aada28ac57babe5c/7dd98d1001e93901f47127ce73ec54e737d196cb.jpg");
        JsonElement jsonObject= (JsonElement) new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJsonTree(EntityRepository.getEntities());
        EntityRepository.getInstance().syncEntities();
        DBEngine.getInstance().insertOrUpdate(entity,jsonObject,true);
//        Rule queryRule=Rule.delete();
//        Rule where=Rule.where();
//        Rule equals=Rule.equal();
//        equals.setColum("objectId");
//        equals.setValue("d69d71cb417044dc85b28dae69a1fe4e");
//        Icon result= (Icon) DBEngine.getInstance().query(entity,Arrays.asList(queryRule,where,equals),true).get(0);
//        boolean result= DBEngine.getInstance().delete(entity,Arrays.asList(queryRule,where,equals),false);
//        System.out.println(result);
    }

}
