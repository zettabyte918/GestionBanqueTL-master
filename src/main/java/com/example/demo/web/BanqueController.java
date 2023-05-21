package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.CompteRepository;
import com.example.demo.dao.OperationRepository;
import com.example.demo.entities.Compte;
import com.example.demo.entities.Operation;
import com.example.demo.entities.Versement;
import com.example.demo.metier.IBanqueMetier;

import aj.org.objectweb.asm.Type;

@Controller
public class BanqueController {
	@Autowired
	private IBanqueMetier banqueMetier;
	@Autowired
	private CompteRepository compteRepo;
	@Autowired 
	private ClientRepository clientrepo;
    @Autowired
	private OperationRepository operationrepo;
	
	@RequestMapping(value="/operations")	

	public String index(Model model) {
        model.addAttribute("listOperations", operationrepo.findAll());
		
	    return "operations";
	}

	
	@RequestMapping(value="comptes/comptedetails/{id}",method = RequestMethod.GET)
	public String consulter(Model model, @PathVariable("id") Long codeCompte, 
			@RequestParam(name = "page",defaultValue = "0") int page ,
            @RequestParam(name = "size",defaultValue = "4") int size){
		try{
			model.addAttribute("codeCompte",codeCompte);
			model.addAttribute("compte",( compteRepo.findById(codeCompte)) );
			System.out.println(codeCompte);
			Compte cp = banqueMetier.consulterCompte(codeCompte).get();
			
            Page<Operation> pageOperations = banqueMetier.listOperation(codeCompte,page,size);
            model.addAttribute("listOperations",pageOperations.getContent());
			System.out.println((pageOperations.getContent()));
            int[] pages = new int[pageOperations.getTotalPages()];//nombre de pages
            model.addAttribute("pages",pages);
            model.addAttribute("compte",cp);
			System.out.println(cp.getSolde());
			System.out.println(cp.getDateCreation());
		}catch (Exception e){
			model.addAttribute("exception","Compte introuvable");
		}
		return "comptes/compte_details";//meme vue comptes
	}
	@RequestMapping(value="/saveOperation" ,method = RequestMethod.POST )
	    public String saveOperation(Model model ,  String typeOperation , Long codeCompte , double montant , Long codeCompte2){
	      try{
	          if(typeOperation.equals("VERS")){
	        	  banqueMetier.verser(codeCompte,montant);
	          }else if(typeOperation.equals("RETR")){
	        	  banqueMetier.retirer(codeCompte,montant);
	          }else if(typeOperation.equals("VIR")){
	        	  banqueMetier.virement(codeCompte,codeCompte2,montant);
	          }
	      }catch (Exception e){
	          model.addAttribute("error",e);
	          return "redirect:/consulterCompte?codeCompte="+codeCompte+"&error="+e.getMessage();
	      }

	        return "redirect:/comptes/comptedetails/"+codeCompte;
	    }
}
