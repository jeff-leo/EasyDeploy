package com.liu.service;

import com.liu.po.JavaItem;
import com.liu.po.JavaWebItem;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */
public interface JavaService {

    void insert(JavaItem info) throws Exception;

    List<JavaItem> getAll() throws Exception;

    JavaItem getByUUID(String uuid) throws Exception;

    String getStatus(String uuid) throws Exception;

    String deploy(String uuid) throws Exception;

    String restart(String uuid) throws Exception;

    String stop(String uuid) throws Exception;
}
