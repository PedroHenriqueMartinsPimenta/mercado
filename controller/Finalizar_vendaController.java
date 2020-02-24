/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.ClienteDAO;
import DAO.vendaDAO;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Cliente;
import model.Produto;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class Finalizar_vendaController implements Initializable {
    private ArrayList<Cliente> result;

    @FXML
    private TextField cpf;
    
    @FXML
    private TextField nome;

    @FXML
    private TextField sobrenome;

    @FXML
    private TextField local;
    
    @FXML
    private TextField valor;
    
    @FXML
    private Label troco;
    
    @FXML
    private Label qtd;
    
    private ObservableList<Produto> produtos;
    
    protected static Stage stage;
    
    private double valorCompra = 0;

    
    @FXML
    void finalizarCompra(ActionEvent event) {
        vender();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stage = PaginaUserController.stage;
        produtos = PaginaUserController.produtos;
        double valor = 0;
        for(Produto obj : produtos){
            valor += obj.getPreco() * obj.getQuantidade();
        }
        this.valorCompra = valor;
        this.troco.setText("Receber: R$ " + String.valueOf(this.valorCompra));
        
        qtd.setText(produtos.size() + " produtos no valor total de: \n R$" + valor);
          cpf.setOnKeyPressed((KeyEvent event)->{
           cpf.setText(mascara(cpf.getText(), event));
           cpf.positionCaret(cpf.getLength());
        });
         cpf.setOnKeyReleased((KeyEvent)->{
             pesquisarCliente(cpf.getText());
         });
         
         this.valor.setOnKeyReleased((KeyEvent e)->{
             if(this.valor.getLength() - 1 >= 0){
             String number = this.valor.getText().substring(this.valor.getLength() -1, this.valor.getLength());
             if(!(number.equals("1") || number.equals("2") || number.equals("3") || number.equals("4") || number.equals("5") || number.equals("6") || number.equals("7") || number.equals("8") || number.equals("9") || number.equals("0") || number.equals("."))){
                 this.valor.setText(this.valor.getText().substring(0, this.valor.getLength() -1));
                 this.valor.positionCaret(this.valor.getLength());
             }else{
                 double troco = arredondar(this.valorCompra - Double.parseDouble(this.valor.getText()), 2, 0);
                 if(troco >= 0){
                     this.troco.setText("Receder: R$ "+String.valueOf(troco));
                 }else{
                    this.troco.setText("Troco: R$ "+String.valueOf(troco * -1));
                 }
             }
             }
         });
         
    }    
    
    public double arredondar(double valor, int casas, int ceilOrFloor) {
        double arredondado = valor;
        arredondado *= (Math.pow(10, casas));
        if (ceilOrFloor == 0) {
            arredondado = Math.ceil(arredondado);           
        } else {
            arredondado = Math.floor(arredondado);
        }
        arredondado /= (Math.pow(10, casas));
        return arredondado;
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
    
    public void pesquisarCliente(String cpf){
        ClienteDAO dao = new ClienteDAO();
        result = dao.getClient(cpf);
        if(result.size() > 0){
            for(Cliente cliente : result){
                nome.setText(cliente.getNome());
                sobrenome.setText(cliente.getSobrenome());
                local.setText(cliente.getRua()+" - " + cliente.getCidade()+", "+ cliente.getEstado());
                
            }
        }else{
            nome.setText("");
            sobrenome.setText("");
            local.setText("");
        }
    }
    
    private void vender(){
        vendaDAO vendaDAO = new vendaDAO();
        result.get(0).setPago(Double.parseDouble(valor.getText()));
        ArrayList<Produto> produtos = new ArrayList<>();
        for(int i = 0; i < this.produtos.size();i++){
            produtos.add(this.produtos.get(i));
        }
        boolean r = vendaDAO.vender(produtos, result.get(0));
        if(r){
            new Thread(){
                @Override
                public void run(){
                    JOptionPane.showMessageDialog(null, "Venda concluida!");
                    String cupom_cabecario = "";
                }
            }.start();
            finsh();
           
        }else{
            new Thread(){
                @Override
                public void run(){
                    JOptionPane.showMessageDialog(null, "Erro na venda!");
                }
                
            }.start();
        }
    }
    public void finsh(){
        PaginaUserController.controller.cancelarCompra();
       stage.close();
    }
}
