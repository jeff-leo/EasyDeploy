package com.liu.service;

import com.liu.po.JavaWebItem;

import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */
public interface JavawebService {

    void insert(JavaWebItem info) throws Exception;

    List<JavaWebItem> getAll() throws Exception;

    JavaWebItem getByUUID(String uuid) throws Exception;

    String getStatus(String uuid) throws Exception;

    String deploy(String uuid) throws Exception;

    String restart(String uuid) throws Exception;

    String stop(String uuid) throws Exception;
}
