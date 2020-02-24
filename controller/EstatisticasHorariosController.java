/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.EstatisticasDAO;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import model.EstatisticaHorario;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class EstatisticasHorariosController implements Initializable {
    
    @FXML
    private Label legenda;

    @FXML
    private PieChart pierChart;
    private EstatisticasDAO dao;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       int ano = new Date().getYear()+1900;
       legenda.setText("Níveis de venda por horarios em "+ano+":");
       dao = new EstatisticasDAO();
        ArrayList<EstatisticaHorario> result = dao.getEstatiticaHorario();
        for(EstatisticaHorario dados : result){
            pierChart.getData().add(new PieChart.Data(String.valueOf(dados.getHora()+" - R$"+dados.getValor()), dados.getValor()));
        }
        pierChart.setLegendSide(Side.LEFT);
    }    
    
}
