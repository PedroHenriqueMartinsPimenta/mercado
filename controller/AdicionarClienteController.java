/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.ClienteDAO;
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
import model.Cliente;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class AdicionarClienteController implements Initializable {
    private ClienteDAO dao;
    
    @FXML
    private Label legenda;

    @FXML
    private TextField cidade;

    @FXML
    private TextField estado;

    @FXML
    private TextField cpf;

    @FXML
    private TextField nome;

    @FXML
    private TextField sobrenome;

    @FXML
    private Button cadastrar;

    @FXML
    private TextField rua;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cpf.setOnKeyPressed((KeyEvent e)->{
            cpf.setText(mascara(cpf.getText(), e));
            cpf.positionCaret(cpf.getLength());
        });
        estado.setOnKeyReleased((KeyEvent e)->{
            if(e.getCode() != KeyCode.BACK_SPACE && e.getCode() != KeyCode.DELETE){
               estado.setText(mascaraEstado(estado.getText()));
               estado.positionCaret(estado.getLength());
            }
        });
        cpf.setOnKeyReleased((KeyEvent e)->{
            if(e.getCode() != KeyCode.BACK_SPACE && e.getCode() != KeyCode.DELETE){
                boolean result = buscarCliente(cpf.getText());
                
                legenda.setVisible(result);
            }
        });
        
        cadastrar.setOnAction((ActionEvent)->{
            if(!legenda.isVisible()){
                boolean result = cadastrar();
                if (result) {
                    new Thread(){
                        @Override
                        public void run(){
                            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
                        }
                    }.start();
                    cpf.setText("");
                    nome.setText("");
                    sobrenome.setText("");
                    rua.setText("");
                    cidade.setText("");
                    estado.setText("");
                }else{
                    
                    new Thread(){
                        @Override
                        public void run(){
                            JOptionPane.showMessageDialog(null, "Erro ao cadastrar cliente");
                        }
                    }.start();
                }
            }else{
                    new Thread(){
                        @Override
                        public void run(){
                            JOptionPane.showMessageDialog(null, "Cliente ja cadastrado");
                        }
                    }.start();
            
            }
        });
        
    } 
    
    public String mascara(String cpf,KeyEvent event){
        String newCpf = cpf;
        if(event.getCode() != KeyCode.BACK_SPACE && event.getCode() != KeyCode.TAB){          
            if(cpf.length() == 3){
                newCpf+=".";
            }
            if(cpf.length() == 7){
                newCpf+=".";
            }
            if(cpf.length() == 11){
                newCpf+="-";
            }
            if(cpf.length() >= 14){
                newCpf = cpf.substring(0, 13);
            }
        }
        return newCpf;
    }
    
    public String mascaraEstado(String estado){
        if(estado.length() > 2){
            estado = estado.substring(0, 2);
        }
        return estado;
    }

    private boolean buscarCliente(String cpf) {
       dao = new ClienteDAO();
        ArrayList<Cliente> result = dao.getClient(cpf);
        if(result.size() > 0){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean cadastrar(){
        Cliente cliente = new Cliente();
        cliente.setCidade(cidade.getText());
        cliente.setCpf(cpf.getText());
        cliente.setEstado(estado.getText());
        cliente.setNome(nome.getText());
        cliente.setRua(rua.getText());
        cliente.setSobrenome(sobrenome.getText());
        dao = new ClienteDAO();
        boolean result= dao.cadastrar(cliente);
    return result;
    }
}
