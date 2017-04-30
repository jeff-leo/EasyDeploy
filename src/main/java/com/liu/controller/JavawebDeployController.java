package com.liu.controller;

import com.liu.po.JavaWebItem;
import com.liu.service.JavawebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.UUID;


/**
 * 异常要记得处理
 */
@Controller
@RequestMapping("webdeploy")
public class JavawebDeployController {

    @Autowired
    private JavawebService javawebService;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String index(){
        return "web/new";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String addNewItem(JavaWebItem item){
        String uuid = UUID.randomUUID().toString();
        item.setUuid(uuid);
        try {
            javawebService.insert(item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/webdeploy/detail/" + uuid;
    }

    @RequestMapping(value = "/detail/{uuid}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String uuid){
        JavaWebItem item = null;
        try{
            item = javawebService.getByUUID(uuid);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("项目路径" + item.getContextPath());
        ModelAndView model = new ModelAndView("web/detail");
        model.addObject("item", item);
        return model;
    }

    /**
     * 进入detail界面，实时刷新运行状态
     * @param uuid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "status", method = RequestMethod.GET)
    public String getStatus(String uuid){
        try {
            return javawebService.getStatus(uuid);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "deploy", produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
    public String deploy(String uuid){
        try {
            return HtmlUtils.htmlEscape(javawebService.deploy(uuid));
        } catch (Exception e){
            e.printStackTrace();
        }
        return "部署失败";
    }


    @ResponseBody
    @RequestMapping(value = "restart", produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
    public String restart(String uuid) throws IOException {
        try {
            return HtmlUtils.htmlEscape(javawebService.restart(uuid));
        }catch (Exception e){
            return "重启失败";
        }
    }

    /**
     * ajax停止
     */
    @ResponseBody
    @RequestMapping(value = "stop", produces = "text/plain;charset=UTF-8", method = RequestMethod.POST)
    public String stop(String uuid) throws IOException {
        try {
            return HtmlUtils.htmlEscape(javawebService.stop(uuid));
        }catch (Exception e){
            return "停止出错";
        }
    }
}
