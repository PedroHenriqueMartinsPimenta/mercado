/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.LoginDAO;
import DAO.UsuarioDAO;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import model.LoginModel;
import model.Usuario;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class AdicionarUsuariosController implements Initializable {
    
    @FXML
    private PasswordField senha;

    @FXML
    private Label legenda;

    @FXML
    private TextField mail;

    @FXML
    private TextField cpf;

    @FXML
    private TextField nome_user;

    @FXML
    private Button cadastrar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cpf.setOnKeyPressed((KeyEvent e)->{
            cpf.setText(mascara(cpf.getText(), e));
            cpf.positionCaret(cpf.getLength());
        });
        
        cpf.setOnKeyReleased((KeyEvent)->{
            LoginDAO log = new LoginDAO();
            List<LoginModel> result = log.getDados(cpf.getText());
            if(result.size()>0){
                legenda.setVisible(true);
            }else{
                legenda.setVisible(false);
            }
        });
        
        cadastrar.setOnAction((ActionEvent)->{
            cadastrarUser();
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
    
    public void cadastrarUser(){
        if(!legenda.isVisible()){
            Usuario user = new Usuario();
            user.setCpf(cpf.getText());
            user.setEmail(mail.getText());
            user.setNome_user(nome_user.getText());
            user.setSenha(senha.getText());
            UsuarioDAO dao = new UsuarioDAO();
            boolean result = dao.cadastrarUser(user);
            if(result){
            new Thread(){
                @Override
                public void run(){
                    JOptionPane.showMessageDialog(null, "Usuario cadastrado com sucesso!");
                }
            }.start();
             cpf.setText("");
             mail.setText("");
             nome_user.setText("");
             senha.setText("");
            }
        }else{
            new Thread(){
                @Override
                public void run(){
                    JOptionPane.showMessageDialog(null, "Usuario ja cadastrado");
                }
            }.start();
        }
    }
}
