/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.FornecedoresDAO;
import DAO.ProdutoDAO;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import model.Produto;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class AdicionarProdutosController implements Initializable {

    
    @FXML
    private TextField qtd;

    @FXML
    private TextField preco;

    @FXML
    private TextField cnpj;

    @FXML
    private TextField codigo;

    @FXML
    private Button cadastrar;

    @FXML
    private DatePicker validade;
    
    @FXML
    private Label cadastrado;

    @FXML
    private TextArea descricao;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       codigo.setOnKeyReleased((KeyEvent tecla)->{
           if(tecla.getCode() != KeyCode.BACK_SPACE && tecla.getCode() != KeyCode.DELETE && codigo.getText() != ""){
            String key = codigo.getText().substring(codigo.getLength()-1);
            try{
                int test = Integer.parseInt(key);
                
            }catch(Exception e){
                String codigo = this.codigo.getText().substring(0, this.codigo.getText().length()-1);
                this.codigo.setText(codigo);
                this.codigo.positionCaret(this.codigo.getLength());
            }
            ProdutoDAO dao = new ProdutoDAO();
            ArrayList<Produto> count = dao.getProduto(codigo.getText());
            if(count.size() > 0){
                cadastrado.setVisible(true);
                descricao.setText(count.get(0).getDescricao());
                preco.setText(String.valueOf(count.get(0).getPreco()));
                validade.setValue(LocalDate.parse(count.get(0).getValidade()));
                qtd.setText(String.valueOf(count.get(0).getQuantidade()));
            }else{
                cadastrado.setVisible(false);
                descricao.setText("");
                preco.setText("");
                validade.setValue(null);
                qtd.setText("");
            }
            }
       });
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
        
        cadastrar.setOnAction((ActionEvent)->{
        cadastrar();
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
    private void cadastrar(){
        FornecedoresDAO fonecedorDAO = new FornecedoresDAO();
        ArrayList fornecedor = fonecedorDAO.getFornecedor(cnpj.getText());
        if (fornecedor.size() > 0) {
            Produto dados = new Produto();
            dados.setCodigo(codigo.getText());
            dados.setDescricao(descricao.getText());
            dados.setFornecedor(cnpj.getText());
            dados.setPreco(Double.parseDouble(preco.getText()));
            dados.setQuantidade(Integer.parseInt(qtd.getText()));
            dados.setValidade(validade.getValue().format(DateTimeFormatter.ISO_DATE));
            ProdutoDAO dao = new ProdutoDAO();
            boolean result = dao.cadastrarProduto(dados);
            if (result) {
                new Thread(){
                    @Override
                    public void run(){
                        JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
                        
                    }
                }.start();
                codigo.setText("");
                descricao.setText("");
                cnpj.setText("");
                preco.setText("");
                validade.setValue(null);
                qtd.setText("");
            }
        }else{
            new Thread(){

                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null, "Fornecedor ainda não cadastrado no sistema\n Por favor abra uma nova "
                            + "janela e cadastre o novo fornecedor");
                }
                
            }.start();
        }
    }
    
}
