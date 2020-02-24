/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.ClienteDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
public class UpdateClienteController implements Initializable {

    private ClienteDAO dao;
    
    private Cliente dados;

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
    
    @FXML
    private Button cancelar;
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
        dados = ClientesController.cliente;
        cpf.setText(dados.getCpf());
        nome.setText(dados.getNome());
        sobrenome.setText(dados.getSobrenome());
        rua.setText(dados.getRua());
        cidade.setText(dados.getCidade());
        estado.setText(dados.getEstado());
        
        cadastrar.setOnAction((ActionEvent)->{
            dados = new Cliente();
            dados.setCpf(cpf.getText());
            dados.setCidade(cidade.getText());
            dados.setEstado(estado.getText());
            dados.setNome(nome.getText());
            dados.setRua(rua.getText());
            dados.setSobrenome(sobrenome.getText());
            dao = new ClienteDAO();
            boolean result = dao.updateCliente(dados);
            if(result){
                new Thread(){
                    @Override
                    public void run(){
                        JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!");
                    }
                }.start();
                Parent root;
           try {
                root = FXMLLoader.load(getClass().getResource("/view/Clientes.fxml"));
                Scene scene = new Scene(root);
                PaginaUserController.stage.setScene(scene);
                PaginaUserController.stage.show();
                PaginaUserController.stage.centerOnScreen();
           } catch (IOException ex) {
               Logger.getLogger(ClientesController.class.getName()).log(Level.SEVERE, null, ex);
           }
            }else{
                new Thread(){
                    @Override
                    public void run(){
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar dados!");
                    }
                }.start();
            
            }
        });
        cancelar.setOnAction((ActionEvent)->{
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/Clientes.fxml"));
                Scene scene = new Scene(root);
                PaginaUserController.stage.setScene(scene);
                PaginaUserController.stage.show();
                PaginaUserController.stage.centerOnScreen();
           } catch (IOException ex) {
               Logger.getLogger(ClientesController.class.getName()).log(Level.SEVERE, null, ex);
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
}
