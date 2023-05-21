package com.example.demo.web;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.CompteRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;

@Controller
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private ClientRepository clientrepo;
	@Autowired
	private CompteRepository compterepo;

	@GetMapping("/showdetails/{code}")
	public String showClientdetails(@PathVariable("code") Long code, Model model) {
		List<Compte> listOfComptes = compterepo.findByClientCode(code);
		Client client = clientrepo.findByCode(code);
		model.addAttribute("client", client);
		model.addAttribute("comptes", listOfComptes);
		return "client/client_details";
	}

	@GetMapping({ "/showClient", "/" })
	public String showClient(Model model) {
		List<Client> clients = clientrepo.findAll();
		model.addAttribute("clients", clients);
		return "client/list_clients";
	}

	@GetMapping("/edit/{id}")
	public String editClient(@PathVariable("id") Long id, Model model) {
		Client client = clientrepo.findById(id).orElse(null);
		model.addAttribute("client", client);
		return "client/edit_client";
	}

	@GetMapping("/addClient")
	public String newClient(Model model) {
		Client client = new Client();
		model.addAttribute("client", client);
		return "client/addclient";
	}

	@PostMapping("/saveClient")
	public String saveClient(@ModelAttribute("client") Client client, RedirectAttributes redirectAttributes) {
		clientrepo.save(client);
		redirectAttributes.addFlashAttribute("success", true);
		redirectAttributes.addFlashAttribute("name", client.getNom());
		return "redirect:/client/showClient";
	}

	@GetMapping("/delete/{code}")
	@Transactional
	public String deleteClient(@PathVariable("code") Long code) {
		clientrepo.deleteByCode(code);
		return "redirect:/client/showClient";
	}

}
