/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.UsuarioDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import model.Mail;
import model.Usuario;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class UsuariosController implements Initializable {
    @FXML
    private ToggleButton contentRecuperacao;

    @FXML
    private ToggleButton contentEdit;

    @FXML
    private Button pesquisar;

    @FXML
    private TableColumn<Usuario, String> cpf;

    @FXML
    private TextField psCpf;

    @FXML
    private TableColumn<Usuario, String> usuario;

    @FXML
    private TableView<Usuario> table;

    @FXML
    private TableColumn<Usuario, String> tbMail;

    @FXML
    private ToggleButton contDelete;
    protected static Usuario usuarioSelected;
    private UsuarioDAO dao;
    @FXML
    void deletar(ActionEvent event) {
        int i = JOptionPane.showConfirmDialog(null, "Realmente deseja apagar permanetemente esté usuario?");
        if(i == 0){
            dao = new UsuarioDAO();
            Usuario dados = table.getSelectionModel().getSelectedItem();
            boolean result = dao.deleteUser(dados.getCpf());
            if(result){
               table.getItems().remove(0, table.getItems().size());
               dao = new UsuarioDAO();
               ArrayList<Usuario> result2 = dao.allUsers();
               ObservableList dados2 = FXCollections.observableArrayList(result2);
               table.setItems(dados2);
               
            }
        }
    }

    @FXML
    public void recuperar(){
        Mail mail = new Mail();
        Usuario usuario = table.getSelectionModel().getSelectedItem();
        dao = new UsuarioDAO();
        ArrayList<Usuario> result = dao.getUser(usuario.getCpf());
        boolean r = mail.sendEmail(result.get(0).getEmail(), "Recuperação de senha", "Caro usuario, sua senha é: "+result.get(0).getSenha());
        if(r){
            JOptionPane.showMessageDialog(null, "E-mail enviado com sucesso!");
        }else{
            JOptionPane.showMessageDialog(null, "Erro ao enviar E-mail!");
        }
    }

    @FXML
    void editar(ActionEvent event) {
           usuarioSelected = table.getSelectionModel().getSelectedItem();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/UpdateUsuario.fxml"));
            Scene scene = new Scene(root);
            PaginaUserController.stage.setScene(scene);
            PaginaUserController.stage.setTitle("Atualizar usuario");
            PaginaUserController.stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(UsuariosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cpf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCpf()));
        usuario.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNome_user()));
        tbMail.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEmail()));
        psCpf.setOnKeyPressed((KeyEvent event)->{
               psCpf.setText(mascara(psCpf.getText(), event));
               psCpf.positionCaret(psCpf.getLength());
            });
        dao = new UsuarioDAO();
        ArrayList<Usuario> result = dao.allUsers();
        ObservableList dados = FXCollections.observableArrayList(result);
        table.setItems(dados);
        table.setOnMouseClicked((MouseEvent)->{
            showContents();
        });
        
        pesquisar.setOnAction((ActionEvent)->{
            pesquisa();
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
    
    private void showContents(){
        contDelete.setVisible(true);
        contentEdit.setVisible(true);
        contentRecuperacao.setVisible(true);
    }
    
    private void pesquisa(){
        if(pesquisar.getText().equals("Pesquisar")){
            dao = new UsuarioDAO();
            ArrayList<Usuario> result = dao.getUser(psCpf.getText());
            table.getItems().remove(0, table.getItems().size());
            ObservableList dados = FXCollections.observableArrayList(result);
            table.setItems(dados);
            pesquisar.setText("X");
        }else if(pesquisar.getText().equals("X")){
            dao = new UsuarioDAO();
            ArrayList<Usuario> result = dao.allUsers();
            table.getItems().remove(0, table.getItems().size());
            ObservableList dados = FXCollections.observableArrayList(result);
            table.setItems(dados);
            pesquisar.setText("Pesquisar");
        
        }
    }
}
