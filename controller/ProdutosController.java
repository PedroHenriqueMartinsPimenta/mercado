/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.ProdutoDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import model.Produto;
import model.ProdutosHasFornecedor;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class ProdutosController implements Initializable {
    protected static ProdutosHasFornecedor infos;
    
    @FXML
    private TextField codigo;

    @FXML
    private Button pesquisar;

    @FXML
    private Button editar;
    
    @FXML
    private TableView<ProdutosHasFornecedor> table;
    
    @FXML
    private Button excluir;
    
    private TableColumn<ProdutosHasFornecedor, String> codigoTable = new TableColumn<>("Codigo");
    private TableColumn<ProdutosHasFornecedor, String> descricao = new TableColumn<>("Descrição");
    private TableColumn<ProdutosHasFornecedor, Integer> qtd = new TableColumn<>("Quantidade");
    private TableColumn<ProdutosHasFornecedor, Double> preco = new TableColumn<>("Preço");
    private TableColumn<ProdutosHasFornecedor, String> fornecedorNome = new TableColumn<>("Nome do fornecedor");
    private TableColumn<ProdutosHasFornecedor, Date>validade = new TableColumn<>("Validade");
    private ProdutoDAO dao;
    
    
    @FXML
    private ToggleButton campEditar;


    @FXML
    private ToggleButton campExcluir;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       codigoTable.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCodigo()));
       descricao.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDescricao()));
       qtd.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQuantidade()));
       validade.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getValidade()));
       preco.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPreco()));
       fornecedorNome.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFornecedor()));
       table.getColumns().addAll(codigoTable, descricao, qtd, validade, preco, fornecedorNome);
       dao = new ProdutoDAO();
        ArrayList<ProdutosHasFornecedor> result = dao.getAllProdutosHasFornecedor();
        for(ProdutosHasFornecedor dados : result){
            table.getItems().add(dados);
        }
        
        table.setOnMouseClicked((MouseEvent)->{
            campEditar.setVisible(true);
            campExcluir.setVisible(true);
        });
        
        excluir.setOnAction((ActionEvent)->{
            ProdutosHasFornecedor dados = table.getSelectionModel().getSelectedItem();
            int i = JOptionPane.showConfirmDialog(null, "Realmente deseja deletar o item "+ dados.getDescricao());
            if(i == 0){
                dao = new ProdutoDAO();
                boolean r = dao.deleteProduto(dados.getCodigo());
                if(r){
                    JOptionPane.showMessageDialog(null, "Item deletado");
                    int is = table.getSelectionModel().getSelectedIndex();
                    table.getItems().remove(is);
                }else{
                    JOptionPane.showMessageDialog(null, "Erro ao deletar!");
                }
             }
        });
        
        editar.setOnAction((ActionEvent)->{
            infos = table.getSelectionModel().getSelectedItem();
            try{
                Parent root = FXMLLoader.load(getClass().getResource("/view/UpdateProdutos.fxml"));
                Scene scene = new Scene(root);
                PaginaUserController.stage.setScene(scene);
                PaginaUserController.stage.setTitle("Editar o produto");
                PaginaUserController.stage.show();
                PaginaUserController.stage.centerOnScreen();
            }catch(IOException e){
                
            }
        });
        pesquisar.setOnAction((ActionEvent)->{
            if(pesquisar.getText().equals("Pesquisar")){
            table.getItems().remove(0, table.getItems().size());
            dao = new ProdutoDAO();
            ArrayList<ProdutosHasFornecedor> dados = dao.getProdutosHasFornecedor(codigo.getText());
            for(ProdutosHasFornecedor d : dados){
                table.getItems().add(d);
            }
            pesquisar.setText("X");
            }else if(pesquisar.getText().equals("X")){
                table.getItems().remove(0, table.getItems().size());
                dao = new ProdutoDAO();
                ArrayList<ProdutosHasFornecedor> dados = dao.getAllProdutosHasFornecedor();
                for(ProdutosHasFornecedor d : dados){
                    table.getItems().add(d);
                 }
                pesquisar.setText("Pesquisar");
            }
        });
    }    
    
}
