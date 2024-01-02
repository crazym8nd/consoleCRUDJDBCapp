package com.vitaly.crudjdbcapp.controller;

import com.vitaly.crudjdbcapp.model.Writer;
import com.vitaly.crudjdbcapp.service.WriterService;

import java.util.List;

public class WriterController {
    private final WriterService writerService = new WriterService();
    public Writer createWriter(String name){
        Writer createdWriter = new Writer();
        return writerService.save(createdWriter);
    }
    public List<Writer> getAll(){
        return writerService.getAll();
    }
    public Writer getById(Integer id){
        return writerService.getById(id);
    }
    public void update(Writer writer){
        writerService.update(writer);
    }
    public void deleteById(Integer id){
        writerService.deleteById(id);
    }
}
