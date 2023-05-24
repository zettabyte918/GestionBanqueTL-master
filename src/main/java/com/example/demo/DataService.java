package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ClientRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataService {
    @Autowired
    private ClientRepository clientrepo;

    public Map<Integer, Double> getLineChartData() {
        Map<Integer, Double> map = new LinkedHashMap<>();
        List<Client> clients = clientrepo.findAll();
        // Iterate over the clients and retrieve data for the chart
        for (Client client : clients) {
            // Access the compte collection for each client
            Collection<Compte> comptes = client.getComptes();

            // Calculate some value based on compte data
            double totalSolde = 0.0;
            for (Compte compte : comptes) {
                totalSolde += compte.getSolde();
            }

            // Add the calculated value to the chart data
            // System.out.println(client.getCode().intValue() + " " + totalSolde);
            map.put(client.getCode().intValue(), totalSolde);
        }

        return map;
    }
}