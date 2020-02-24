/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.ProdutoDAO;
import DAO.vendaDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import model.Itens;
import model.Produto;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class UpdateItemController implements Initializable {
    @FXML
    private TextField qtd;
    @FXML
    private Label menssagem;
    @FXML
    private TextField preco;

    @FXML
    private TextField codigo;

    @FXML
    private TextField precoTotal;

    @FXML
    private TextField descricao;
    private vendaDAO dao;

    @FXML
    void atualizar(ActionEvent event) {
        if(!menssagem.isVisible()){
            dao = new vendaDAO();
            Itens newDados = new Itens();
            newDados.setCodigoProduto(codigo.getText());
            newDados.setCodigoVenda(item.getCodigoVenda());
            newDados.setQuantidade(Integer.parseInt(qtd.getText()));
            boolean result = dao.updateItem(item, newDados);
            if(result){
                JOptionPane.showMessageDialog(null, "Dados atualizados");
                rediricioanr();
            }
        }
    }
    private Itens item;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      item = UpdateVendaController.item;
      if(item != null){
          codigo.setText(item.getProduto().getCodigo());
          qtd.setText(String.valueOf(item.getQuantidade()));
          preco.setText(String.valueOf(item.getProduto().getPreco()));
          descricao.setText(item.getProduto().getDescricao());
          precoTotal.setText(String.valueOf(item.getProduto().getPreco() * item.getQuantidade()));
      }
      codigo.setOnKeyReleased((KeyEvent)->{
          addProduto();
          codigo.positionCaret(codigo.getLength());
      });
      qtd.setOnKeyReleased((KeyEvent)->{
          addProduto();
      });
    }    
    public void addProduto(){
      Produto produto = getProduto(codigo.getText());
      int qtd = Integer.parseInt(this.qtd.getText());
      if(produto != null){
       if((produto.getQuantidade() + item.getQuantidade()) - qtd >= 0){
          codigo.setText(produto.getCodigo());
          preco.setText(String.valueOf(produto.getPreco()));
          descricao.setText(produto.getDescricao());
          precoTotal.setText(String.valueOf(produto.getPreco() * Integer.parseInt(this.qtd.getText())));
           menssagem.setVisible(false);
       }else{
          menssagem.setVisible(true);
          menssagem.setText("Não há estoque suficiente");
       
       }
      }
    }
     public Produto getProduto(String codigo){
      ProdutoDAO dao = new ProdutoDAO();
      ArrayList<Produto> result = dao.getProduto(codigo);
      if(result.size() > 0){
          menssagem.setVisible(false);
      return result.get(0);
      }else{
          menssagem.setVisible(true);
          menssagem.setText("Produto não cadastrado!");
          return null;
      }
  }
     
     private void rediricioanr(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/UpdateVenda.fxml"));
            Scene scene = new Scene(root);
            PaginaUserController.stage.setScene(scene);
            PaginaUserController.stage.setTitle("Atualizar venda");
            PaginaUserController.stage.centerOnScreen();
            PaginaUserController.stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UpdateItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
}
