/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.FornecedoresDAO;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import model.Fornecedor;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class AdicionarFornecedorController implements Initializable {
    @FXML
    private TextField nome;

    @FXML
    private TextField cnpj;

    @FXML
    private Button cadastrar;
    
    @FXML
    private Label legenda;
    
    private  FornecedoresDAO dao;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cnpj.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode() != KeyCode.BACK_SPACE && e.getCode() != KeyCode.DELETE){
                cnpj.setText(mascaraCNPJ(cnpj.getText()));
                cnpj.positionCaret(cnpj.getLength());
            }
        });
        cnpj.setOnKeyReleased((KeyEvent)->{
            dao = new FornecedoresDAO();
            ArrayList<Fornecedor> result = dao.getFornecedor(cnpj.getText());
            if(result.size() > 0){
                legenda.setVisible(true);
                nome.setText(result.get(0).getNome());
            }else{
              legenda.setVisible(false);
                nome.setText(""); 
            }
        });
        cadastrar.setOnAction((ActionEvent)->{
            cadastrarFornecedor();
        });
    }    
    
private String mascaraCNPJ(String numero){
        String cnpj = numero;
        if(numero.length() == 2){
            cnpj = numero + ".";
        }
        if(numero.length() == 6){
            cnpj = numero + ".";
        }
        if(numero.length() == 10){
            cnpj = numero + "/";
        }
        if(numero.length() == 15){
            cnpj = numero + "-";
        }
        if(numero.length() > 17){
            cnpj = numero.substring(0, 17);
        }
    return cnpj;
    }
    private void cadastrarFornecedor(){
        if(!legenda.isVisible()){
        if(!cnpj.getText().equals("") && !nome.getText().equals("")){
            Fornecedor dados = new Fornecedor();
            dados.setCnpj(cnpj.getText());
            dados.setNome(nome.getText());
            dao = new  FornecedoresDAO();
           boolean result = dao.cadastrarFornecedor(dados);
            if (result) {
                new Thread(){
                    @Override
                    public void run(){
                        JOptionPane.showMessageDialog(null, "Cadastrado com sucesso");
                    }
                }.start();
                cnpj.setText("");
                nome.setText("");
            }
        }else {
           
        new Thread(){
            @Override
            public void run(){
                JOptionPane.showMessageDialog(null,"Um dos campos em branco");
            }
        }.start();
        if(cnpj.getText().equals("")){
            cnpj.requestFocus();
        }else if(nome.getText().equals("")){
            nome.requestFocus();
        }
        }
        }else{
        new Thread(){
            @Override 
            public void run(){
            JOptionPane.showMessageDialog(null, "Fornecedor ja cadastrado");
            }
        }.start();
        }
    }
}
