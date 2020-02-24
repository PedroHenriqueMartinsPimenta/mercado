/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.EstatisticasDAO;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class EstatisticasController implements Initializable {
       
    @FXML
    private PieChart pierChart; 
    private EstatisticasDAO dao;
    @FXML
    private Label legenda;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        legenda.setText("Níveis de vendas por meses de "+(new Date().getYear()+1900)+":");
        dao = new EstatisticasDAO();
        String meses[] = DateFormatSymbols.getInstance(Locale.getDefault()).getMonths();
        double result[] = dao.getMesMelhorVenda();
        for(int i = 0; i < result.length;i++){
               pierChart.getData().addAll(new PieChart.Data(meses[i]+" - R$"+result[i], result[i]));
        }
        
        pierChart.setLegendSide(Side.LEFT);  
        pierChart.setClockwise(false);
        pierChart.startAngleProperty();
    }    
    
}
