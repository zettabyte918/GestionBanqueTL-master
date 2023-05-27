package com.example.demo;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BubbleChartModel;
import org.primefaces.model.chart.BubbleChartSeries;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dao.ClientRepository;
import com.example.demo.dao.OperationRepository;
import com.example.demo.entities.Client;
import com.example.demo.entities.Compte;
import com.example.demo.entities.Operation;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@Component
@ManagedBean
@ViewScoped
public class LineChartBean {
    @Autowired
    private ClientRepository clientrepo;

    private int year;
    private int month;
    private Date inputDate;

    public Date getinputDate() {
        return inputDate;
    }

    public void setinputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public int getInputYear() {
        if (inputDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inputDate);
            return calendar.get(Calendar.YEAR);
        } else {
            return 0; // Return a default value or handle the null case appropriately
        }
    }

    public int getInputMonth() {
        if (inputDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inputDate);
            return calendar.get(Calendar.MONTH) + 1; // Months in Calendar class are zero-based
        } else {
            return 0; // Return a default value or handle the null case appropriately
        }
    }

    @Autowired
    private OperationRepository operationrepo;

    @Autowired
    private DataService dataService;
    private LineChartModel lineModel;
    private BubbleChartModel bubbleModel2;
    private BarChartModel barModel;

    @PostConstruct
    public void init() {
        createBubbleModels();
        createBarModel();
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

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void createBarModel() {
        barModel = initBarModel();

        barModel.setTitle("Bar Chart");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Days");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Total credits");

    }

    private BarChartModel initBarModel() {
        int year = getInputYear();
        int month = getInputMonth();

        System.out.println("the input date is: " + year + ":" + month);

        BarChartModel model = new BarChartModel();
        Map<Integer, Double> dailyCreditMap = new TreeMap<>();
        List<Operation> operationsbydate = operationrepo.listOperationbydate(year, month);

        // Calculate the total credit for each day
        for (Operation operation : operationsbydate) {
            Date date = operation.getDateOperation();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            double credit = operation.getMontant();
            if (dailyCreditMap.containsKey(day)) {
                double totalCredit = dailyCreditMap.get(day) + credit;
                dailyCreditMap.put(day, totalCredit);
            } else {
                dailyCreditMap.put(day, credit);
            }

        }

        ChartSeries creditSeries = new ChartSeries();
        creditSeries.setLabel("Total Credit");

        // Add the data points to the chart series
        for (Map.Entry<Integer, Double> entry : dailyCreditMap.entrySet()) {
            int day = entry.getKey();
            double totalCredit = entry.getValue();
            System.out.println("day: " + day + " " + totalCredit);
            creditSeries.set(String.valueOf(day), totalCredit);
        }

        model.addSeries(creditSeries);

        return model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
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
