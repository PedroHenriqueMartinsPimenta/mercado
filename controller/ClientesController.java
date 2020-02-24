/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.ClienteDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Cliente;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class ClientesController implements Initializable {
    protected static Cliente cliente;
    private Stage stage;
    
    @FXML
    private Button pesquisar;
    
    @FXML
    private Button editar;
    
    @FXML
    private ToolBar opc;

    @FXML
    private TextField cpf;

    @FXML
    private TableView<Cliente> table;
    
    private TableColumn<Cliente, String>  cpf_column = new TableColumn<>("CPF"),
            nome = new TableColumn<>("Nome"),
            sobrenome = new TableColumn<>("Sobrenome"),
            rua = new TableColumn<>("Rua"),
            cidade = new TableColumn<>("Cidade"),
            estado = new TableColumn<>("Estado");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       cpf_column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCpf()));
       nome.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNome()));
       sobrenome.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSobrenome()));
       rua.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRua()));
       cidade.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCidade()));
       estado.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEstado()));
       
       table.getColumns().addAll(cpf_column, nome, sobrenome, rua, cidade, estado);
       
        ClienteDAO dao = new ClienteDAO();
        ArrayList<Cliente> result = dao.allClientes();
        for(Cliente cliente : result){
            addItemTable(cliente);
        }
    cpf.setOnKeyPressed((KeyEvent event)->{
           cpf.setText(mascara(cpf.getText(), event));
           cpf.positionCaret(cpf.getLength());
        });
    
    pesquisar.setOnMouseClicked((MouseEvent event)->{
        if(event.getButton() == MouseButton.PRIMARY){
            if(pesquisar.getText().equals("X")){
                remover();
            }else{
                 pesquisar();
            }
        }
    });
    
    table.setOnMouseClicked((MouseEvent event)->{
        if(event.getButton() == MouseButton.PRIMARY){
            opc.setVisible(true);
            
        }
    });
    
    editar.setOnAction((ActionEvent)->{
        cliente = table.getSelectionModel().getSelectedItem();
        Parent root;
           try {
                root = FXMLLoader.load(getClass().getResource("/view/UpdateCliente.fxml"));
                Scene scene = new Scene(root);
                PaginaUserController.stage.setScene(scene);
                PaginaUserController.stage.show();
                PaginaUserController.stage.centerOnScreen();
           } catch (IOException ex) {
               Logger.getLogger(ClientesController.class.getName()).log(Level.SEVERE, null, ex);
           }
    });
    }    
    private void addItemTable(Cliente dados){
        table.getItems().add(dados);
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
    
    private void pesquisar(){
        ClienteDAO cliente = new ClienteDAO();
        ArrayList<Cliente> client = cliente.getClient(cpf.getText());
        table.getItems().remove(0, table.getItems().size());        
        for(Cliente c : client){
           table.getItems().add(c);
        }
        pesquisar.setText("X");
    }
    private void remover(){
        ClienteDAO cliente = new ClienteDAO();
        ArrayList<Cliente> client = cliente.allClientes();
        table.getItems().remove(0, table.getItems().size());
        for(Cliente c : client){
           table.getItems().add(c);
        }
        pesquisar.setText("Pesquisar");
    }
    
    
}
