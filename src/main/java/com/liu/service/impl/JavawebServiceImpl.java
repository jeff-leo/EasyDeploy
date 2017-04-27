package com.liu.service.impl;

import com.liu.mapper.JavawebDeployMapper;
import com.liu.po.JavaWebItem;
import com.liu.service.JavawebService;
import com.liu.util.ShellUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */
@Service
public class JavawebServiceImpl implements JavawebService{

    @Autowired
    private JavawebDeployMapper javawebDeployMapper;

    @Value("${shell.webdeploy}")
    private String shellPath;

    @Value("${jetty.basepath}")
    private String jettyPath;

    @Value("${web.deploypath}")
    private String deploypath;

    /**
     * 新增项目
     * @param info
     */
    @Override
    public void insert(JavaWebItem info) throws Exception{
        javawebDeployMapper.insert(info);
    }

    /**
     * 获取所有项目
     * @return
     */
    @Override
    public List<JavaWebItem> getAll() throws Exception{
        return javawebDeployMapper.findAll();
    }

    /**
     * 根据uuid获取项目
     * @param uuid
     * @return
     */
    @Override
    public JavaWebItem getByUUID(String uuid) throws Exception{
        return javawebDeployMapper.findByUUID(uuid);
    }

    /**
     * 获取项目的运行状态
     * @param uuid
     * @return
     * @throws Exception
     */
    @Override
    public String getStatus(String uuid) throws Exception{
        JavaWebItem item = javawebDeployMapper.findByUUID(uuid);
        if(item != null){
            //查看当前uuid的端口是否运行
            String output =ShellUtil.exec("sh " + shellPath + "/isrunning.sh", uuid);
            //如果项目已经在jetty中启动，命令一定会有java -jar
            System.out.println("状态 : " + output);
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
        JavaWebItem item = javawebDeployMapper.findByUUID(uuid);
        if(item != null){
            //用于保存日志消息,显示到界面
            StringBuilder sb = new StringBuilder();
            //把当前uuid的进程杀死
            String killResult = ShellUtil.exec("sh " + shellPath + "/kill.sh", uuid);
            sb.append(killResult);

            //处理ContextPath，如果用户路径有/，删除掉,如果没有填写路径，默认为deploy
            String contextPath = item.getContextPath();
            contextPath = contextPath.replace("/", "");
            if(contextPath.length() == 0) {
                contextPath = "deploy";
            }

            //调用package命令,下载项目，用maven打包成war
            String[] packageArgs = {item.getUuid(), item.getUrl(), jettyPath, deploypath, item.getType()+"",
                    item.getProfile(), item.getBranch()};
            String packageResult = ShellUtil.exec("sh " + shellPath + "/package.sh", packageArgs);
            sb.append(packageResult);

            //获取war包
            String filename = null;
            File dir = new File(deploypath + "/" + uuid + "/target");//获取maven打包下的target目录
            File[] files = dir.listFiles();
            for (File file: files) {
                String name = file.getName();
                if(file.isFile() && name.endsWith(".war")){
                    filename = name;
                    break;
                }
            }

            //部署到jetty中
            if(filename != null){
                FileUtils.copyFile(new File(deploypath + "/" + uuid + "/target/" + filename),
                        new File(deploypath + "/" + uuid + "/webapps/" + contextPath + ".war"));
                String[] startArgs = {uuid, item.getPort()+"", jettyPath, deploypath};
                sb.append(ShellUtil.exec("sh " + shellPath + "/start.sh", startArgs));
            }else{
                System.out.println("打包失败");
            }
            return sb.toString();
        }else{
            return uuid + "项目不存在";
        }
    }

    /**
     * 重启项目
     * @param uuid
     * @return
     * @throws IOException
     */
    @Override
    public String restart(String uuid) throws Exception {
        JavaWebItem item = javawebDeployMapper.findByUUID(uuid);
        if(item != null){
            StringBuilder sb = new StringBuilder();
            sb.append(ShellUtil.exec("sh " + shellPath + "/kill.sh", uuid));
            sb.append(ShellUtil.exec("sh " +shellPath + "/start.sh", new String[]{uuid,
                    item.getPort()+"", jettyPath, deploypath}));
            return sb.toString();
        }else{
            return uuid + " 项目不存在";
        }
    }

    /**
     * 停止项目
     * @param uuid
     * @return
     * @throws Exception
     */
    @Override
    public String stop(String uuid) throws Exception {
        JavaWebItem item = javawebDeployMapper.findByUUID(uuid);
        if(item != null){
            return ShellUtil.exec("sh " + shellPath + "/kill.sh", uuid);
        }else{
            return uuid + " 项目不存在";
        }
    }


}
