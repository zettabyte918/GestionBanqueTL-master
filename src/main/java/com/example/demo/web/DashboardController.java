package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import groovyjarjarpicocli.CommandLine.Model;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/page1")
    public String getPage1(Model model) {
        // Add any necessary data to the model
        return "dashboard/page1.html";
    }

    @GetMapping("/page2")
    public String getPage2(Model model) {
        // Add any necessary data to the model
        return "dashboard/page2.html";
    }
}
