package com.liu.util;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;

/**
 * Java 操作 Shell脚本工具类
 */
public class ShellUtil {

    public static String exec(String cmd, String ... args) throws Exception{
        System.out.print("shell执行参数 : ");
        //解析命令
        CommandLine command = CommandLine.parse(cmd);
        //添加参数
        for (String arg: args) {
            System.out.print(arg + " ");
            //如果不为null
            if(StringUtils.hasText(arg)){
                command.addArgument(arg, false);
            }else{
                command.addArgument("null", false);
            }
        }

        DefaultExecutor executor = new DefaultExecutor();
        //防止出现异常
        executor.setExitValues(null);

        //设置watchdog，限制命令执行时间,限制200s
        ExecuteWatchdog watchdog = new ExecuteWatchdog(200000);
        executor.setWatchdog(watchdog);

        //CommandLine的执行放到一个缓冲区中
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(streamHandler);

        //执行命令
        executor.execute(command);

        return outputStream.toString();
    }
}
