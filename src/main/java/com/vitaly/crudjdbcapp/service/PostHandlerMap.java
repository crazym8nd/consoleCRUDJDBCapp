package com.vitaly.crudjdbcapp.service;

import com.vitaly.crudjdbcapp.model.Post;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.HashMap;
import java.util.Map;

/*
23-Dec-23
gh /crazym8nd
*/
public class PostHandlerMap extends BeanListHandler<Post> {
    public PostHandlerMap() {
        super(Post.class);
        new BasicRowProcessor(new BeanProcessor(getColumsToFieldsMap()));
    }
    public static Map<String,String> getColumsToFieldsMap(){
        Map<String,String> columnsToFields = new HashMap<>();
        columnsToFields.put("p.id","id");
        columnsToFields.put("content","content");
        columnsToFields.put("created","created");
        columnsToFields.put("updated","updated");
        columnsToFields.put("post_status","postStatus");
        columnsToFields.put("label_id","labelId");
        columnsToFields.put("name","labelName");
        columnsToFields.put("status","status");
        return columnsToFields;
    }
}
