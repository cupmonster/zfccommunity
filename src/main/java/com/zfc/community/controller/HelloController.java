package com.zfc.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
    @GetMapping(path="/index")
    public String hello(@RequestParam(name = "name") String name, Model model) {
        model.addAttribute("name",name);
//        System.out.println(name);
        return "index";
    }
}
