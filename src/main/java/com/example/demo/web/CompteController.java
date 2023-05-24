package com.example.demo.web;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.CompteRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;
import com.example.demo.entities.CompteCourant;
import com.example.demo.entities.CompteEpargne;
import com.example.demo.metier.BanqueMetierImpl;

@Controller
@RequestMapping("/comptes")
public class CompteController {

  @Autowired
  private CompteRepository compteRepo;
  @Autowired
  private ClientRepository clientRepo;
  @Autowired
  private BanqueMetierImpl banqueMetier;

  @GetMapping("")
  public String getAllComptes(Model model) {
    List<Compte> comptes = this.compteRepo.findAll();
    double sum = 0;
    for (Compte compte : comptes) {
      sum += compte.getSolde();
    }
    model.addAttribute("comptes", comptes);
    model.addAttribute("totale", sum);
    return "comptes/list_comptes";
  }

  /*
   * @GetMapping("/comptedetails/{id}")
   * public String getComptess(@PathVariable Long id, Model model){
   * model.addAttribute("operations",banqueMetier.consulterCompte(id));
   * System.out.println(( banqueMetier.consulterCompte(id)));
   * return "comptes/compte_details" ;
   * }
   */

  @GetMapping("/addCourant")
  public String addCorantAccount(Model model) {
    model.addAttribute("compte", new CompteCourant());
    model.addAttribute("clients", clientRepo.findAll());
    return "comptes/add_courant";
  }

  @GetMapping("/addEpargne")
  public String addEpargneAccount(Model model) {
    model.addAttribute("compte", new CompteEpargne());
    model.addAttribute("clients", clientRepo.findAll());
    return "comptes/add_epargne";
  }

  @PostMapping("/savecompteepargne")
  public String saveCompteEpargne(@ModelAttribute("compte") CompteEpargne compte) {
    compteRepo.save(compte);
    return "redirect:/comptes/";
  }

  @PostMapping("/savecomptecourant")
  public String saveCompteCourant(@ModelAttribute("compte") CompteCourant compte) {
    compteRepo.save(compte);
    return "redirect:/comptes/";
  }

  @GetMapping("/delete/{id}")
  public String deleteCompte(@PathVariable("id") Long id) {
    compteRepo.deleteById(id);
    return "redirect:/comptes/";
  }

}
