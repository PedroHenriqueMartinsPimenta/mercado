/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.ProdutoDAO;
import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import model.Produto;

/**
 * FXML Controller class
 *  
 * @author Pedro Henrique
 */
public class PaginaUserController implements Initializable {
    private TableColumn<Produto,String> descricao = new TableColumn<>("Descricao");
    private TableColumn<Produto,Integer> quantidade = new TableColumn<>("Quantidade");
    private TableColumn<Produto,Double> preco = new TableColumn<>("Preço total");
    private TableColumn<Produto,Double> precoUnico = new TableColumn<>("Preço unitario");
    protected static Stage stage;
    private Scene scene;
    protected static ObservableList<Produto> produtos;
    protected static PaginaUserController controller;
  
    @FXML
    private  Button iniciar;
    
    @FXML
    private Label esc;
    
    @FXML
    private Label del;
    
    @FXML
    private Label F2;

    @FXML
    private Label enter;

    @FXML
    private TableView<Produto> table;
    
    @FXML
    private TextField codigo;
    
    @FXML
    private Label valor;
    
    @FXML
    private TextField qtd;
    
    @FXML
    private TextArea descricaoProduto;
    
    @FXML
    private DatePicker validade; 
    
    @FXML
    private TextField precoUnitario;
    
     @FXML
    private AnchorPane corpo;
       @FXML
    private Label labelCodigo;

    @FXML
    private Label labelDesc;

    @FXML
    private Label labelQtd;
    
    @FXML
    private Label labelValorUnitario;
    
    @FXML
    private Label labelValidade;
     @FXML
     private Label menssagem;
    @FXML
    void iniciarVenda(ActionEvent event) {
        aparecerItens();
        codigo.requestFocus();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controller = this;
        descricao.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getDescricao()));
        quantidade.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getQuantidade()));
        precoUnico.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPreco()));
        preco.setCellValueFactory(data-> new SimpleObjectProperty<>(data.getValue().getPreco() * data.getValue().getQuantidade()));
        table.getColumns().addAll(descricao, quantidade, precoUnico, preco);
        desaparecerItens();
        corpo.setOnKeyReleased((KeyEvent e)->{
             if(e.getCode() == KeyCode.F2){
                produtos = table.getItems();
              try {
                    finalizarCompra();
                } catch (IOException ex) {
                    Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }else if(e.getCode() == KeyCode.SPACE  && !codigo.getText().equals("")){
                addItem();
                codigo.setText(codigo.getText().replace(" ", ""));
            }else if(e.getCode() == KeyCode.ESCAPE){
                int i = JOptionPane.showConfirmDialog(null, "Realmente deseja cancelar venda?");
                if(i == 0){
                    cancelarCompra();
                }
            }
        });
        
        codigo.setOnKeyReleased((KeyEvent e)->{
            if(table.isVisible() && e.getCode() != KeyCode.DELETE && e.getCode() != KeyCode.BACK_SPACE && !codigo.getText().equals("")){
                Produto produto = getProduto(codigo.getText());
                if(produto != null){
                    descricaoProduto.setText(produto.getDescricao());
                    precoUnitario.setText(String.valueOf(produto.getPreco()));
                    validade.setValue(LocalDate.parse(produto.getValidade()));
                }
            }else{
                descricaoProduto.setText("");
                qtd.setText("1");
                validade.setValue(null);
                precoUnitario.setText("0.00");
            }
        });
        
        codigo.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode() == KeyCode.F2){
                produtos = table.getItems();
              try {
                    finalizarCompra();
                } catch (IOException ex) {
                    Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }else if(e.getCode() == KeyCode.SPACE  && !codigo.getText().equals("")){
                addItem();
                codigo.setText(codigo.getText().replace(" ", ""));
            }
        });
        
        
        qtd.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode() == KeyCode.F2){
                produtos = table.getItems();
              try {
                    finalizarCompra();
                } catch (IOException ex) {
                    Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }else if(e.getCode() == KeyCode.SPACE && !codigo.getText().equals("")){
                addItem();
                codigo.setText(codigo.getText().replace(" ", ""));
            }else if(e.getCode() == KeyCode.ESCAPE){
                int i = JOptionPane.showConfirmDialog(null, "Realmente deseja cancelar venda?");
                if(i == 0){
                    cancelarCompra();
                
                }
            }
        });
        table.setOnMouseClicked((MouseEvent event)->{
            if(event.getClickCount() >= 2 && event.getButton() == MouseButton.SECONDARY){
                int del_confirm = JOptionPane.showConfirmDialog(null, "Realmente deseja remover esté item da venda?");
                if(del_confirm == 0){
                    table.getItems().remove(table.getSelectionModel().getSelectedIndex());
                    updateValor();
                }
            }
        });
        
        table.setOnKeyReleased((KeyEvent e) -> {
            if(e.getCode() == KeyCode.DELETE){
                int del_confirm = JOptionPane.showConfirmDialog(null, "Realmente deseja remover esté item da venda?");
                if(del_confirm == 0){
                    table.getItems().remove(table.getSelectionModel().getSelectedIndex());
                    updateValor();
                }
            }
        });
    }    
    
       @FXML
    void fechar(ActionEvent event) {
        int i =  JOptionPane.showConfirmDialog(null, "Realmente deseja sair?");
        if(i == 0){
            Parent parent;
            try {
                parent = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
                scene = new Scene(parent);
                main.Main.stage.setScene(scene);
                main.Main.stage.setTitle("Login");
                main.Main.stage.setWidth(400);
                main.Main.stage.setHeight(442);
                main.Main.stage.setMaximized(false);
                main.Main.stage.setResizable(false);
                main.Main.stage.centerOnScreen();
            } catch (IOException ex) {
                Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML
    public void addProdutos(){
        Parent  root;
        stage = new Stage();
        try {
            root = FXMLLoader.load(getClass().getResource("/view/AdicionarProdutos.fxml"));
            scene = new Scene(root);
            stage.setTitle("Adicionar produtos");
            stage.setScene(scene);
            stage.getIcons().add(main.Main.stage.getIcons().get(0));
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }

  public void aparecerItens(){
      table.setVisible(true);
      esc.setVisible(true);
      del.setVisible(true);
      enter.setVisible(true);
      F2.setVisible(true);
      codigo.setVisible(true);
      valor.setVisible(true);
      qtd.setVisible(true);
      labelCodigo.setVisible(true);
      labelDesc.setVisible(true);
      labelQtd.setVisible(true);
      labelValidade.setVisible(true);
      labelValorUnitario.setVisible(true);
      validade.setVisible(true);
      precoUnitario.setVisible(true);
      descricaoProduto.setVisible(true);
      iniciar.setVisible(false);
      
      
  }
  
  public void desaparecerItens(){
      table.setVisible(false);
      esc.setVisible(false);
      del.setVisible(false);
      enter.setVisible(false);
      F2.setVisible(false);
      codigo.setVisible(false);
      valor.setVisible(false);
      qtd.setVisible(false);
      validade.setVisible(false);
      precoUnitario.setVisible(false);
      descricaoProduto.setVisible(false);
      menssagem.setVisible(false);
      labelCodigo.setVisible(false);
      labelDesc.setVisible(false);
      labelQtd.setVisible(false);
      labelValidade.setVisible(false);
      labelValorUnitario.setVisible(false);
      iniciar.setVisible(true);
      
  }
  
  public  void finalizarCompra() throws IOException{
      stage = new Stage();
      Parent root = FXMLLoader.load(getClass().getResource("/view/Finalizar_venda.fxml"));
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.setTitle("Informe dados do cliente");
      stage.setResizable(false);
      stage.getIcons().add(main.Main.stage.getIcons().get(0));      
      stage.show();
  }
  public void addItem(){
      if(table.isVisible()){
          addProduto();
          codigo.requestFocus();
      }
  }
  public void cancelarCompra(){
      if(table.isVisible()){
        desaparecerItens();
        table.getItems().remove(0, table.getItems().size()); 
        codigo.setText("");
        descricaoProduto.setText("");
        validade.setValue(null);
        this.qtd.setText("1");
        precoUnitario.setText("0.0");
        valor.setText("Valor: R$0.00");
      }
  }
  
  @FXML
  public void addFornecedor(){
      stage = new Stage();
      try{
          Parent root = FXMLLoader.load(getClass().getResource("/view/AdicionarFornecedor.fxml"));
          scene = new Scene(root);
          stage.setScene(scene);
          stage.setTitle("Adicionar fornecedor");
          stage.getIcons().add(main.Main.stage.getIcons().get(0));
          stage.show();
      }catch(Exception e){
      
      }
  }
  
  @FXML
  public void addUsers(){
      stage = new Stage();
      try{
          Parent root = FXMLLoader.load(getClass().getResource("/view/AdicionarUsuarios.fxml"));
          scene = new Scene(root);
          stage.setScene(scene);
          stage.setTitle("Adicionar Ususarios");
          stage.getIcons().add(main.Main.stage.getIcons().get(0));
          stage.show();
      }catch(Exception e){
      
      }
  }
  
  @FXML
  public void addCliente(){
      
      stage = new Stage();
      try{
          Parent root = FXMLLoader.load(getClass().getResource("/view/AdicionarCliente.fxml"));
          scene = new Scene(root);
          stage.setScene(scene);
          stage.setTitle("Cadastrar clientes");
          stage.getIcons().add(main.Main.stage.getIcons().get(0));
          stage.show();
      }catch(Exception e){
      
      }
  }
  
  public void codigoFocus(){
      if(codigo.isVisible()){ 
          codigo.requestFocus();
      }
  }
  public void addProduto(){
      Produto produto = getProduto(codigo.getText());
      int qtd = Integer.parseInt(this.qtd.getText());
      if(produto != null){
       for(Produto p : table.getItems()){
          if(p.getCodigo().equals(produto.getCodigo())){
              qtd+= p.getQuantidade();
          }
          
      }
      if(produto.getQuantidade() - qtd >= 0){
          produto.setQuantidade(Integer.parseInt(this.qtd.getText()));
          table.getItems().add(produto);
          codigo.setText("");
          this.qtd.setText("1");
          descricaoProduto.setText("");
          validade.setValue(null);
          codigo.setText("");
          precoUnitario.setText("0.0");
          updateValor();
      }else{
          menssagem.setVisible(true);
          menssagem.setText("Não há estoque suficiente a esse produto!");
          codigo.requestFocus(); 
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
  
  public void updateValor(){
      double valor = 0;
      for(Produto obj : table.getItems()){
          valor += obj.getPreco() * obj.getQuantidade();
      }
      this.valor.setText("Valor: R$"+valor);
  }
  
  @FXML
  public void editClientes()throws IOException{
      stage = new Stage();
      Parent root = FXMLLoader.load(getClass().getResource("/view/Clientes.fxml"));
      scene = new Scene(root);
      stage.setScene(scene);
      stage.setTitle("Clientes");
      stage.getIcons().add(main.Main.stage.getIcons().get(0));
      stage.setResizable(true);
      stage.show();
  }
  
  @FXML
  public void updateProdutos(){
      stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Produtos.fxml"));
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("Produtos");
            stage.getIcons().add(main.Main.stage.getIcons().get(0));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
  
  @FXML
  public void updateFornecedores(){
      stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Fornecedores.fxml"));
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("Fornecedores");
            stage.getIcons().add(main.Main.stage.getIcons().get(0));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
  
  }
  
  @FXML
  public void updateUsuarios(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Usuarios.fxml"));
            scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Usuarios");
            stage.getIcons().add(main.Main.stage.getIcons().get(0));
            stage.setResizable(true);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
  
  @FXML
  public void updateVenda(){        
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Vendas.fxml"));
            scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Vendas");
            stage.getIcons().add(main.Main.stage.getIcons().get(0));
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
  
  @FXML
  public void getMes(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Estatisticas.fxml"));
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Estatisticas");
            stage.getIcons().add(main.Main.stage.getIcons().get(0));
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
  
  @FXML
  public void getAnos(){
      try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/EstatisticasAnos.fxml"));
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Estatisticas");
            stage.getIcons().add(main.Main.stage.getIcons().get(0));
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
  
  @FXML
  public void getHorarios(){
      try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/EstatisticasHorarios.fxml"));
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Estatisticas");
            stage.getIcons().add(main.Main.stage.getIcons().get(0));
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
  
  }
  
  @FXML
  public void manualUsuario(){
      Desktop desktop = Desktop.getDesktop();
        try {
            String diretorio = new File(".").getCanonicalPath()+"\\src\\manual\\manual.pdf"; 
            desktop.open(new File(diretorio));
        } catch (IOException ex) {
            Logger.getLogger(PaginaUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
}
