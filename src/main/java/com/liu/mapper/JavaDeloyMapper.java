package com.liu.mapper;

import com.liu.po.JavaItem;
import com.liu.po.JavaWebItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface JavaDeloyMapper {

    @Insert("insert into java_deploy (uuid,name,url,type,profile,branch) values (#{uuid},#{name},#{url},#{type},#{profile},#{branch})")
    void insert(JavaItem javaItem) throws Exception;

    @Select("select uuid,name,url,type,profile,branch from java_deploy")
    List<JavaItem> findAll() throws Exception;

    @Select("select uuid,name,url,type,profile,branch from java_deploy where uuid = #{uuid}")
    JavaItem findByUUID(String uuid) throws Exception;
}
