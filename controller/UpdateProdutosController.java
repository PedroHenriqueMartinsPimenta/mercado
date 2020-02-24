/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.ProdutoDAO;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import model.ProdutosHasFornecedor;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class UpdateProdutosController implements Initializable {
    @FXML
    private TextField qtd;

    @FXML
    private TextField preco;

    @FXML
    private TextField cnpj;

    @FXML
    private TextField codigo;

    @FXML
    private Button atualizar;

    @FXML
    private DatePicker validade;
    

    @FXML
    private TextArea descricao;
    
   @FXML
   private Button cancelar;
   
   private ProdutoDAO dao;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         ProdutosHasFornecedor dados = ProdutosController.infos;
        qtd.setOnKeyReleased((KeyEvent tecla)->{
            if(tecla.getCode() != KeyCode.BACK_SPACE && tecla.getCode() != KeyCode.DELETE && qtd.getText() != ""){
            String key = qtd.getText().substring(qtd.getLength()-1);
            try{
                int test = Integer.parseInt(key);
                
            }catch(Exception e){
                String qtd = this.qtd.getText().substring(0, this.qtd.getText().length()-1);
                this.qtd.setText(qtd);
                this.qtd.positionCaret(this.qtd.getLength());
            }
            }
        });
        preco.setOnKeyReleased((KeyEvent tecla)->{
            if(tecla.getCode() != KeyCode.BACK_SPACE && tecla.getCode() != KeyCode.DELETE && preco.getText() != " "){
            String key = preco.getText().substring(0, preco.getLength());
            try{
                double test = Double.parseDouble(key);
                
            }catch(Exception e){
                String preco = "";
                if(this.preco.getText().length()-1 > 0){
                   preco = this.preco.getText().substring(0, this.preco.getText().length()-1);
                }else{
                   preco = this.preco.getText().substring(0, this.preco.getText().length());
                }
                this.preco.setText(preco);
                this.preco.positionCaret(this.preco.getLength());
            }
            }
        });
        cnpj.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode() != KeyCode.BACK_SPACE && e.getCode() != KeyCode.DELETE){
            cnpj.setText(mascaraCNPJ(cnpj.getText()));
            cnpj.positionCaret(cnpj.getLength());
            }
        });
        codigo.setText(String.valueOf(dados.getCodigo()));
        descricao.setText(dados.getDescricao());
        qtd.setText(String.valueOf(dados.getQuantidade()));
        validade.setValue(LocalDate.parse(dados.getValidade().toString()));
        preco.setText(String.valueOf(dados.getPreco()));
        cnpj.setText(dados.getCnpj());
        atualizar.setOnAction((ActionEvent)->{
            dao = new ProdutoDAO();
            ProdutosHasFornecedor newDados = new ProdutosHasFornecedor();
            newDados.setCodigo(codigo.getText());
            newDados.setCnpj(cnpj.getText());
            newDados.setDescricao(descricao.getText());
            newDados.setPreco(Double.parseDouble(preco.getText()));
            newDados.setQuantidade(Integer.parseInt(qtd.getText()));
            System.out.println(validade.getValue().getYear()+"-"+validade.getValue().getMonthValue()+"-"+ validade.getValue().getDayOfMonth());
            newDados.setValidade(new Date(validade.getValue().getYear(), validade.getValue().getMonthValue(),  validade.getValue().getDayOfMonth()));
            boolean result = dao.updateProduto(newDados);
            if(result){
                JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!");
                voltar();
            }else{
                JOptionPane.showMessageDialog(null, "Erro ao atualizar \n verefique se não estar sendo ultizado em uma venda \n ou o fornecedor esar cadastrado");
            }
        });
        cancelar.setOnAction((ActionEvent)->{
            voltar();
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
    public void voltar(){
         try {
                 Parent root = FXMLLoader.load(getClass().getResource("/view/Produtos.fxml"));
                 Scene scene = new Scene(root);
                 PaginaUserController.stage.setScene(scene);
                 PaginaUserController.stage.setTitle("Produtos");
                 PaginaUserController.stage.show();
                 PaginaUserController.stage.centerOnScreen();
             } catch (IOException ex) {
                 Logger.getLogger(UpdateProdutosController.class.getName()).log(Level.SEVERE, null, ex);
             }
    }
}
