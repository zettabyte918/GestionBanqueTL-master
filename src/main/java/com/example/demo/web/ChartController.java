package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.CompteRepository;
import com.example.demo.dao.OperationRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;
import com.example.demo.entities.Operation;

@Controller
@RequestMapping("/chart")
public class ChartController {

    @Autowired
    private ClientRepository clientrepo;
    @Autowired
    private CompteRepository compterepo;

    @Autowired
    private OperationRepository operationrepo;

    @GetMapping({ "/stats", "/" })
    public String showClient(Model model) {
        List<Client> clients = clientrepo.findAll();
        List<Compte> comptes = this.compterepo.findAll();
        List<Operation> operations = this.operationrepo.findAll();

        model.addAttribute("clients", clients.size());
        model.addAttribute("comptes", comptes.size());
        model.addAttribute("operations", operations.size());

        return "chart/chart";
    }
}
