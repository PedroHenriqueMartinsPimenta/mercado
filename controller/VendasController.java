/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.vendaDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import model.Itens;
import model.Produto;
import model.Venda;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class VendasController implements Initializable {

    @FXML
    private TableColumn<Venda, Integer> codigo;

    @FXML
    private TableColumn<Venda, String> localizacao;

    @FXML
    private ToggleButton contentEdit;

    @FXML
    private TableColumn<Venda, Date> data;
    
    @FXML
    private ToggleButton contentDel;
    
    @FXML
    private TableColumn<Venda, String> nome;


    @FXML
    private Button pesquisar;


    @FXML
    private TableColumn<Venda, String> tbCpf;

    @FXML
    private TextField psCpf;
    


    @FXML
    private TableView<Venda> table;
    protected static Venda venda;
    @FXML
    public void deletarVenda(){
        Venda venda = new Venda();
         venda = table.getSelectionModel().getSelectedItem();
        int i = JOptionPane.showConfirmDialog(null, "Realmente deseja deletar a venda de codigo "+venda.getCodigo()+"?");
        if(i == 0){
            dao = new vendaDAO();
            ArrayList<String> dados = new ArrayList<>();
            ArrayList<Itens> itens = dao.getItensHasProdutos(venda.getCodigo());
            for(Itens item : itens){
                dados.add(item.getProduto().getCodigo());
            }
            boolean result = dao.deleteVenda(venda.getCodigo(), dados, venda.getProduto().getQuantidade());
            if(result){
                JOptionPane.showMessageDialog(null, "Venda deletada com sucesso!");
                reiniciarStage();
            }
        }
    }

private void reiniciarStage(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Vendas.fxml"));
            Scene scene = new Scene(root);
            PaginaUserController.stage.setScene(scene);
            PaginaUserController.stage.setMaximized(true);
            PaginaUserController.stage.centerOnScreen();
            PaginaUserController.stage.setTitle("Vendas");
            PaginaUserController.stage.setMaximized(true);
            PaginaUserController.stage.show();            
            
        } catch (IOException ex) {
            Logger.getLogger(UpdateVendaController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    @FXML
    void editar(ActionEvent event) {
         Venda venda = new Venda();
         venda = table.getSelectionModel().getSelectedItem();
         this.venda = venda;
       try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/UpdateVenda.fxml"));
            Scene scene = new Scene(root);
            PaginaUserController.stage.setScene(scene);
            PaginaUserController.stage.setTitle("Atualizar venda");
            PaginaUserController.stage.setMaximized(true);
            PaginaUserController.stage.centerOnScreen();
            PaginaUserController.stage.show();
        } catch (IOException ex) {
            Logger.getLogger(VendasController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private vendaDAO dao;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     codigo.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCodigo()));
     tbCpf.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCliente().getCpf()));
     nome.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCliente().getNome()+" "+ data.getValue().getCliente().getSobrenome()));
     localizacao.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCliente().getRua()+", "+data.getValue().getCliente().getCidade()+" - "+data.getValue().getCliente().getEstado()));
     data.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getData()));
        dao = new vendaDAO();
        ArrayList<Venda> result = dao.allVendas();
        ObservableList dados = FXCollections.observableArrayList(result);
        table.setItems(dados);
        
        table.setOnMouseClicked((MouseEvent)->{
            showContent();
        });
        
        psCpf.setOnKeyPressed((KeyEvent event)->{
           psCpf.setText(mascara(psCpf.getText(), event));
           psCpf.positionCaret(psCpf.getLength());
        });
        
        pesquisar.setOnAction((ActionEvent)->{
            if(pesquisar.getText().equals("Pesquisar")){
                dao = new vendaDAO();
                ArrayList<Venda> r = dao.getVendas(psCpf.getText());
                ObservableList d = FXCollections.observableArrayList(r);
                table.getItems().remove(0, table.getItems().size());
                table.setItems(d);
                   pesquisar.setText("X");
            }else if(pesquisar.getText().equals("X")){
                dao = new vendaDAO();
                ArrayList<Venda> r = dao.allVendas();
                ObservableList d = FXCollections.observableArrayList(r);
                table.getItems().remove(0, table.getItems().size());
                table.setItems(d);
                pesquisar.setText("Pesquisar");
            }
        });
        
    }    
    private void showContent(){
        contentEdit.setVisible(true);
        contentDel.setVisible(true);
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
