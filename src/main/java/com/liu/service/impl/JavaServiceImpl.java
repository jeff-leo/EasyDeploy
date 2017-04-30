package com.liu.service.impl;

import com.liu.mapper.JavaDeloyMapper;
import com.liu.po.JavaItem;
import com.liu.po.JavaWebItem;
import com.liu.service.JavaService;
import com.liu.util.ShellUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/4/29.
 */
@Service
public class JavaServiceImpl implements JavaService{

    @Autowired
    private JavaDeloyMapper javaDeloyMapper;

    @Value("${shell.javadeploy}")
    private String shellPath;

    @Value("${java.deploypath}")
    private String deploypath;

    @Override
    public void insert(JavaItem item) throws Exception {
        javaDeloyMapper.insert(item);
    }

    @Override
    public List<JavaItem> getAll() throws Exception {
        return javaDeloyMapper.findAll();
    }

    @Override
    public JavaItem getByUUID(String uuid) throws Exception {
        return javaDeloyMapper.findByUUID(uuid);
    }

    /**
     * 获取项目的运行状态
     * @param uuid
     * @return
     * @throws Exception
     */
    @Override
    public String getStatus(String uuid) throws Exception{
        JavaItem item = javaDeloyMapper.findByUUID(uuid);
        if(item != null){
            //查看当前uuid的端口是否运行
            String output = ShellUtil.exec("sh " + shellPath + "/isrunning.sh", uuid);
            //如果Jar项目已经启动，命令一定会有java -jar
            System.out.println("Status : " +  String.valueOf(StringUtils.hasText(output) && output.contains("java -jar")));
            return String.valueOf(StringUtils.hasText(output) && output.contains("java -jar"));
        }
        return "false";
    }

    /**
     * 部署项目
     * @param uuid
     * @return
     * @throws Exception
     */
    @Override
    public String deploy(String uuid) throws Exception{
        JavaItem item = javaDeloyMapper.findByUUID(uuid);
        if(item != null){
            //用于保存日志消息,显示到界面
            StringBuilder sb = new StringBuilder();
            //把当前uuid的进程杀死
            String killResult = ShellUtil.exec("sh " + shellPath + "/kill.sh", uuid);
            sb.append(killResult);

            //调用package命令,下载项目，用maven打包成jar
            String[] packageArgs = {item.getUuid(), item.getUrl(), deploypath, item.getType()+"",
                    item.getProfile(), item.getBranch()};
            String packageResult = ShellUtil.exec("sh " + shellPath + "/package.sh", packageArgs);
            sb.append(packageResult);

            //获取jar包
            String filename = getJarName(uuid);

            //启动jar
            if(filename != null){
                String[] startArgs = {uuid, filename, deploypath};
                sb.append(ShellUtil.exec("sh " + shellPath + "/start.sh", startArgs));
            }else{
                System.out.println("打包失败");
            }
            return sb.toString();
        }else{
            return uuid + "项目不存在";
        }
    }

    @Override
    public String restart(String uuid) throws Exception {
        JavaItem item = javaDeloyMapper.findByUUID(uuid);
        if(item != null){
            StringBuilder sb = new StringBuilder();
            sb.append(ShellUtil.exec("sh " + shellPath + "/kill.sh", uuid));
            String jarName = getJarName(uuid);
            sb.append(ShellUtil.exec("sh " +shellPath + "/start.sh", new String[]{uuid,
                    jarName , deploypath}));
            return sb.toString();
        }else{
            return uuid + " 项目不存在";
        }
    }

    @Override
    public String stop(String uuid) throws Exception {
        JavaItem item = javaDeloyMapper.findByUUID(uuid);
        if(item != null){
            return ShellUtil.exec("sh " + shellPath + "/kill.sh", uuid);
        }else{
            return uuid + " 项目不存在";
        }
    }

    public String getJarName(String uuid){
        String filename = null;
        File dir = new File(deploypath + "/" + uuid + "/target");//获取maven打包下的target目录
        File[] files = dir.listFiles();
        for (File file: files) {
            String name = file.getName();
            if(file.isFile() && name.endsWith(".jar")){
                filename = name;
                break;
            }
        }
        return filename;
    }
}
