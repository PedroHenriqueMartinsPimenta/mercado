/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.FornecedoresDAO;
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
import javax.swing.JOptionPane;
import model.Fornecedor;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class FornecedoresController implements Initializable {
@FXML
    private Button pesquisar;

    @FXML
    private TableColumn<Fornecedor, String> tbCnpj;

    @FXML
    private TableColumn<Fornecedor, String> nome;

    @FXML
    private TextField psCnpj;

    @FXML
    private TableView<Fornecedor> table;
    
    @FXML
    private ToggleButton contDelete;
    
    @FXML
    private ToggleButton contentEdit;
    
    private FornecedoresDAO dao;
    
    protected static Fornecedor dados;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       tbCnpj.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCnpj()));
       nome.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNome()));
       dao = new FornecedoresDAO();
       ArrayList<Fornecedor> result = dao.allFornecedores();
        ObservableList observableArray = FXCollections.observableArrayList(result); 
        table.setItems(observableArray);
        psCnpj.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode() != KeyCode.BACK_SPACE && e.getCode() != KeyCode.DELETE){
                psCnpj.setText(mascaraCNPJ(psCnpj.getText()));
                psCnpj.positionCaret(psCnpj.getLength());
            }
        });
        table.setOnMouseClicked((MouseEvent)->{
            showContent();
        });
        
        pesquisar.setOnAction((ActionEvent)->{
            dao = new FornecedoresDAO();
            if(pesquisar.getText().equals("Pesquisar")){
                ArrayList<Fornecedor> psResult = dao.getFornecedor(psCnpj.getText());
                ObservableList dados = FXCollections.observableArrayList(psResult);
                table.setItems(dados);
                pesquisar.setText("X");
            }else if(pesquisar.getText().equals("X")){
                ArrayList<Fornecedor> psResult = dao.allFornecedores();
                 ObservableList dados = FXCollections.observableArrayList(result); 
                 table.setItems(dados);
                pesquisar.setText("Pesquisar");
            }
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
    
    
    @FXML
    void deletar(ActionEvent event) {
        if(table.getSelectionModel().getSelectedItem() != null){
            int confirm =  JOptionPane.showConfirmDialog(null, "Realmente deseja deletar este fornecedor?");
            if(confirm == 0){
                dao = new FornecedoresDAO();
                boolean result = dao.deleteFornecedor(table.getSelectionModel().getSelectedItem().getCnpj());
                if(result){
                    new Thread(){
                        @Override
                        public void run(){
                            JOptionPane.showMessageDialog(null, "Fornecedor apagado permanetemente!");
                        }
                    }.start();
                    table.getItems().remove(0, table.getItems().size());
                    ArrayList<Fornecedor> psResult = dao.allFornecedores();
                     ObservableList observableArray = FXCollections.observableArrayList(psResult); 
                     table.setItems(observableArray);

                }else{
                    new Thread(){
                        @Override
                        public void run(){
                            JOptionPane.showMessageDialog(null, "Ocorreu uma erro, utilize o codigo informado \n anteriomente");
                        }
                    }.start();
                }
            }
        }
    }


    @FXML
    void editar(ActionEvent event) {
                dados = table.getSelectionModel().getSelectedItem();
        if(dados != null){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/UpdateFornecedor.fxml"));
                Scene scene = new Scene(root);
                PaginaUserController.stage.setScene(scene);
                PaginaUserController.stage.setTitle("Atualizar fornecedor");
                PaginaUserController.stage.show();
                
            } catch (IOException ex) {
                Logger.getLogger(FornecedoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void showContent(){
        contentEdit.setVisible(true);
        contDelete.setVisible(true);
    }
    
}
