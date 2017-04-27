package com.liu.controller;

import com.liu.po.JavaWebItem;
import com.liu.service.JavawebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */
@Controller
public class IndexController {

    @Autowired
    private JavawebService javawebService;

    @RequestMapping("/index")
    public String index(Model model){
        try{
            List<JavaWebItem> items = javawebService.getAll();
            model.addAttribute("items", items);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "index";
    }
}
