package com.liu.controller;

import com.liu.po.JavaWebItem;
import com.liu.service.JavaService;
import com.liu.service.JavawebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */
@Controller
public class IndexController {

    @Autowired
    private JavawebService javawebService;

    @Autowired
    private JavaService javaService;


    @Value("${deploy.type}")
    private String type;

    @RequestMapping("/index")
    public String index(Model model){
        List<String> typeList = Arrays.asList(type.split(","));
        model.addAttribute("typeList", typeList);
        try{
            if(typeList.contains("Java")){
                model.addAttribute("JavaList", javaService.getAll());
            }
            if(typeList.contains("Javaweb")){
                model.addAttribute("JavawebList", javawebService.getAll());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "index";
    }
}
