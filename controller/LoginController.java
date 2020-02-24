/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.LoginDAO;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import model.LoginModel;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cpf.setOnKeyPressed((KeyEvent event)->{
           cpf.setText(mascara(cpf.getText(), event));
           cpf.positionCaret(cpf.getLength());
        });
        
    } 
    
    @FXML
    private PasswordField senha;

    @FXML
    private TextField cpf;
    
    @FXML
    void entrar(ActionEvent event) {
           LoginDAO loginDAO = new LoginDAO();
           List<LoginModel> result = loginDAO.getDados(cpf.getText());
           if(result.size() > 0){
           for(LoginModel dados : result){
               if(dados.getCpf().equals(cpf.getText()) && dados.getSenha().equals(senha.getText())){
                   try {
                       Parent root = FXMLLoader.load(getClass().getResource("/view/paginaUser.fxml"));
                       Scene scene = new Scene(root);
                       main.Main.stage.setScene(scene);
                       main.Main.stage.setTitle("Pagina de usuario");
                       main.Main.stage.setResizable(true);
                       main.Main.stage.centerOnScreen();
                       main.Main.stage.setMaximized(true);
                   } catch (IOException ex) {
                       Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }else{
                   incompatibilidade();
               }
           }
         }else{
               incompatibilidade();
           }
    }
    
    public void incompatibilidade(){
        new Thread(){
             @Override
              public void run(){
                    JOptionPane.showMessageDialog(null, "CPF ou senha incorretos!");
             }
         }.start();
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
    
}
