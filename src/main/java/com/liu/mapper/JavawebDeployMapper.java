package com.liu.mapper;

import com.liu.po.JavaWebItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JavawebDeployMapper {

    @Insert("insert into java_web_deploy (uuid,name,url,context_path,port,type,profile,branch) values (#{uuid},#{name},#{url},#{contextPath},#{port},#{type},#{profile},#{branch})")
    void insert(JavaWebItem javaWebDeployInfo) throws Exception;

    @Select("select uuid,name,url,context_path,port,type,profile,branch from java_web_deploy")
    List<JavaWebItem> findAll() throws Exception;

    @Select("select uuid,name,url,context_path,port,type,profile,branch from java_web_deploy where uuid = #{uuid}")
    JavaWebItem findByUUID(String uuid) throws Exception;
}
