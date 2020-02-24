/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.ClienteDAO;
import DAO.vendaDAO;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import model.Cliente;
import model.Itens;
import model.Venda;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 */
public class UpdateVendaController implements Initializable {
    @FXML
    private TableColumn<Itens, Integer> qtd;

    @FXML
    private TextField localizacao;

    @FXML
    private TableColumn<Itens, Double> precoUnico;
    
    @FXML
    private Label valor;
    
    @FXML
    private Label valorPG;
    
    @FXML
    private Label valorRes;

    @FXML
    private TextField nome;

    @FXML
    private TableColumn<Itens, String> cnpj;

    @FXML
    private TableColumn<Itens, Double> precoTotal;

    @FXML
    private ToolBar content;

    @FXML
    private TableColumn<Itens, String> descricao;

    @FXML
    private TextField codigoVenda;

    @FXML
    private Button atualizar;

    @FXML
    private TextField cpf;

    @FXML
    private TableColumn<Itens, String> codigoProduto;

    @FXML
    private TextField sobrenome; 

    @FXML
    private TableView<Itens> table;
    
    @FXML
    private TextField data;
    
    @FXML
    private TextField pago;
    
    protected static Itens item;
   
    private Venda venda;
    private vendaDAO dao;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         cpf.setOnKeyPressed((KeyEvent event)->{
           cpf.setText(mascara(cpf.getText(), event));
           cpf.positionCaret(cpf.getLength());
          });
         cpf.setOnKeyReleased((KeyEvent event)->{
          if(event.getCode() != KeyCode.BACK_SPACE && event.getCode() != KeyCode.DELETE){
                ArrayList<Cliente> result2 = buscarCliente(cpf.getText());
                if(result2.size() > 0){
                    nome.setText(result2.get(0).getNome());
                    sobrenome.setText(result2.get(0).getSobrenome());
                    localizacao.setText(result2.get(0).getRua()+", "+result2.get(0).getCidade()+"-"+result2.get(0).getEstado());
                }
            }
         });
         
        venda = VendasController.venda;
        if(venda != null){
            codigoVenda.setText(String.valueOf(venda.getCodigo()));
            cpf.setText(venda.getCliente().getCpf());
            nome.setText(venda.getCliente().getNome());
            sobrenome.setText(venda.getCliente().getSobrenome());
            localizacao.setText(venda.getCliente().getRua()+", "+ venda.getCliente().getCidade()+"-"+venda.getCliente().getEstado());
            data.setText(String.valueOf(venda.getData()));
        }
      codigoProduto.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduto().getCodigo()));
      descricao.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduto().getDescricao()));
      qtd.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQuantidade()));
      cnpj.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduto().getFornecedor()));
      precoUnico.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduto().getPreco()));
      precoTotal.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProduto().getPreco() * data.getValue().getQuantidade()));
      table.setOnMouseClicked((MouseEvent)->{
           content.setVisible(true);
       });
      dao = new vendaDAO();
        ArrayList<Itens> result = dao.getItensHasProdutos(venda.getCodigo());
        ObservableList list = FXCollections.observableArrayList(result);
        table.setItems(list);
        double preco  = 0;
        for(Itens item : result){
            preco += item.getProduto().getPreco() * item.getQuantidade();
        }
        pago.setText(String.valueOf(venda.getPago()));
        valor.setText("Valor total: R$ "+preco);
        valorPG.setText("Valor pago: R$ "+ venda.getPago());
        valorRes.setText("Valor restante: R$ "+Math.round(preco - venda.getPago()));
        atualizar.setOnAction((ActionEvent)->{
                dao = new vendaDAO();
                boolean r = dao.updateVenda(cpf.getText(),Double.parseDouble(pago.getText()), Integer.parseInt(codigoVenda.getText()));
                if(r){
                    JOptionPane.showMessageDialog(null, "Dados atualizados!");
                    redicioanar();
                }
        });
         this.pago.setOnKeyReleased((KeyEvent e)->{
             if(pago.getLength() - 1 >= 0){
                String number = this.pago.getText().substring(this.pago.getLength() -1, this.pago.getLength());
                if(!(number.equals("1") || number.equals("2") || number.equals("3") || number.equals("4") || number.equals("5") || number.equals("6") || number.equals("7") || number.equals("8") || number.equals("9") || number.equals("0") || number.equals("."))){
                    this.pago.setText(this.pago.getText().substring(0, this.pago.getLength() -1));
                    this.pago.positionCaret(this.pago.getLength());
                }
             }
         });
    }   
     

    @FXML
    void editarProduto(ActionEvent event) {
        item = table.getSelectionModel().getSelectedItem();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/UpdateItem.fxml"));
            Scene scene = new Scene(root);
            PaginaUserController.stage.setScene(scene);
            PaginaUserController.stage.setTitle("Atualizar item");
            PaginaUserController.stage.centerOnScreen();
            PaginaUserController.stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UpdateVendaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @FXML
    void removerProduto(ActionEvent event) {
        item = table.getSelectionModel().getSelectedItem();
        
        int i = JOptionPane.showConfirmDialog(null, "Realmente deseja deletar o item: "+ item.getProduto().getDescricao());
        if(i == 0){
            dao = new vendaDAO();
            boolean result = dao.deleteItens(item.getCodigoVenda(), item.getProduto().getCodigo(), item.getQuantidade());
            if(result){
                JOptionPane.showMessageDialog(null, "Item removido!");
                dao = new vendaDAO();
                System.out.println(item.getCodigoVenda()+" : "+ item.getProduto().getCodigo());
                  ArrayList<Itens> r = dao.getItensHasProdutos(venda.getCodigo());
                  ObservableList list = FXCollections.observableArrayList(r);
                  table.getItems().remove(0, table.getItems().size());
                  table.setItems(list);
                  double preco = 0;
                  for(Itens item : r){
                      preco += item.getProduto().getPreco() * item.getQuantidade();
                  }
                  valor.setText("R$ "+ preco);
                  valorPG.setText("Valor pago: R$ "+ venda.getPago());
                  valorRes.setText("Valor restante: R$ "+(preco - venda.getPago()));
            }
        }
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
     @FXML
     public void cancelar(){
         redicioanar();
     }
     
     private ArrayList<Cliente> buscarCliente(String cpf) {
       ClienteDAO clienteDAO = new ClienteDAO();
        ArrayList<Cliente> result = clienteDAO.getClient(cpf);
        return result;
    }
     
     private void redicioanar(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Vendas.fxml"));
            Scene scene = new Scene(root);
            PaginaUserController.stage.setScene(scene);
            PaginaUserController.stage.setMaximized(true);
            PaginaUserController.stage.centerOnScreen();
            PaginaUserController.stage.setTitle("Vendas");
            PaginaUserController.stage.show();            
            
        } catch (IOException ex) {
            Logger.getLogger(UpdateVendaController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
}
