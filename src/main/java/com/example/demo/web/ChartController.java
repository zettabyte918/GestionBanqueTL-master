package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import groovyjarjarpicocli.CommandLine.Model;

@Controller
@RequestMapping("/chart")
public class ChartController {

    @GetMapping({ "/stats", "/" })
    public String showClient(Model model) {
        return "chart/chart";
    }
}
