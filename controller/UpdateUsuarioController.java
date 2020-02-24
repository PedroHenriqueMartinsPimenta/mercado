/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.UsuarioDAO;
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
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import model.Usuario;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class UpdateUsuarioController implements Initializable {
    @FXML
    private TextField mail;

    @FXML
    private TextField cpf;

    @FXML
    private TextField nome_user;

    @FXML
    private Button atualizar;
    
    @FXML
    private Button voltar;
    
    private Usuario dados;
    private UsuarioDAO dao;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      dados = UsuariosController.usuarioSelected;
      if(dados != null){
        cpf.setText(dados.getCpf());
        mail.setText(dados.getEmail());
        nome_user.setText(dados.getNome_user());
        atualizar.setOnAction((ActionEvent)->{
            updateUser();
        });
      }
      
      voltar.setOnMouseClicked((MouseEvent) -> {
          rediricionar();
      });
    }    

    private void updateUser() {
        dados = new Usuario();
        dados.setCpf(cpf.getText());
        dados.setEmail(mail.getText());
        dados.setNome_user(nome_user.getText());
        dao = new UsuarioDAO();
        boolean result = dao.updateUser(dados);
        if(result){
            JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!");
            rediricionar();
        }
    }
    private void rediricionar(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Usuarios.fxml"));
            Scene scene = new Scene(root);
            PaginaUserController.stage.setScene(scene);
            PaginaUserController.stage.setTitle("Usuarios");
            PaginaUserController.stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UpdateUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
