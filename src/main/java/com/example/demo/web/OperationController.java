package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dao.OperationRepository;

@Controller
@RequestMapping("/operations")
public class OperationController {
    @Autowired
    private OperationRepository operationRepo;

    @GetMapping("")
    public String GetAllOperations(Model model) {
        model.addAttribute("listOperations", this.operationRepo.findAll());
        return "operations";
    }

}