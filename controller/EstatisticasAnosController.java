/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.EstatisticasDAO;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import model.EstastiticaAnual;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class EstatisticasAnosController implements Initializable {
    @FXML
    private StackedBarChart<String, Double> grafico;

    @FXML
    private CategoryAxis anoX;
    private EstatisticasDAO dao;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dao = new EstatisticasDAO();
        XYChart.Series series = new XYChart.Series();
        series.setName("Lucro anual");
        ArrayList<EstastiticaAnual> result = dao.getEstatiticaAnual();
        for(EstastiticaAnual dados : result){
            series.getData().add( new XYChart.Data<>(String.valueOf(dados.getAno()), dados.getValor()));
        }
        grafico.getData().add(series);
    }    
    
}
