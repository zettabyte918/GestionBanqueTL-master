package com.example.demo;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BubbleChartModel;
import org.primefaces.model.chart.BubbleChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dao.ClientRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@Component
@ManagedBean
@ViewScoped
public class LineChartBean {
    @Autowired
    private ClientRepository clientrepo;

    @Autowired
    private DataService dataService;
    private LineChartModel lineModel;
    private BubbleChartModel bubbleModel2;

    @PostConstruct
    public void init() {
        createBubbleModels();
        lineModel = new LineChartModel();
        LineChartSeries s = new LineChartSeries();
        s.setLabel("Accounts credit");

        dataService.getLineChartData().forEach(s::set);

        lineModel.addSeries(s);
        lineModel.setLegendPosition("e");
        Axis y = lineModel.getAxis(AxisType.Y);

        y.setLabel("Account id");

        Axis x = lineModel.getAxis(AxisType.X);

        x.setLabel("Total Balance in TND");

    }

    private void createBubbleModels() {

        bubbleModel2 = initBubbleModel();
        bubbleModel2.setTitle("Client chart balance");
        bubbleModel2.setShadow(false);
        bubbleModel2.setBubbleGradients(true);
        bubbleModel2.setBubbleAlpha(0.8);
        Axis y = bubbleModel2.getAxis(AxisType.Y);

        y.setLabel("Number of account per client");

        Axis x = bubbleModel2.getAxis(AxisType.X);

        x.setLabel("Total Balance in TND");

    }

    private BubbleChartModel initBubbleModel() {
        BubbleChartModel model = new BubbleChartModel();
        List<Client> clients = clientrepo.findAll();
        for (Client client : clients) {
            // index
            int index = clients.indexOf(client) + 1;
            // Access the compte collection for each client
            Collection<Compte> comptes = client.getComptes();

            // Calculate some value based on compte data
            double totalSolde = 0.0;
            for (Compte compte : comptes) {
                totalSolde += compte.getSolde();
            }

            // Add the calculated value to the chart data
            System.out.println(
                    client.getNom() + " " + index + " " + client.getComptes().size() + " " + scaleBalance(totalSolde));

            model.add(new BubbleChartSeries(client.getNom() + " " + totalSolde + " TND",
                    totalSolde, index,
                    scaleBalance(totalSolde)));
        }

        return model;
    }

    public double scaleBalance(double balance) {
        double minBalance = 0.0; // Minimum balance in the original range
        double maxBalance = 1005.0; // Maximum balance in the original range
        double minScaled = 0.0; // Minimum value in the scaled range (0 to 100)
        double maxScaled = 100.0; // Maximum value in the scaled range (0 to 100)

        // Calculate the scaled value
        double scaledValue = ((balance - minBalance) / (maxBalance - minBalance)) * (maxScaled - minScaled) + minScaled;

        return scaledValue;
    }

    public BubbleChartModel getBubbleModel2() {
        return bubbleModel2;
    }

    public LineChartModel getLineModel() {
        return lineModel;
    }
}
