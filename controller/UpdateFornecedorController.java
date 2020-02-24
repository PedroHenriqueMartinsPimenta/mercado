/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.FornecedoresDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import model.Fornecedor;

/**
 * FXML Controller class
 *
 * @author Pedro Henrique
 * 
 */
public class UpdateFornecedorController implements Initializable {
private Fornecedor fornecedor;
    @FXML
    private TextField nome;

    @FXML
    private TextField cnpj;

    @FXML
    private Button cadastrar;
    
    @FXML
    private Button voltar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     fornecedor = FornecedoresController.dados;
     if(fornecedor != null){
        cnpj.setText(fornecedor.getCnpj());
        nome.setText(fornecedor.getNome());
        cadastrar.setOnAction((ActionEvent)->{
            updateFornecedor();
        });
     }else{
         rediricionar();
     }
     voltar.setOnMouseClicked((MouseEvent) ->{
         rediricionar();
     });
    }
    
    private void updateFornecedor(){
        FornecedoresDAO dao = new FornecedoresDAO();
        Fornecedor dados = new Fornecedor();
        dados.setCnpj(cnpj.getText());
        dados.setNome(nome.getText());
        boolean result = dao.updateFornecedor(dados);
        if(result){
            new Thread(){
                @Override
                public void run(){
                    JOptionPane.showMessageDialog(null,"Dados atualizados com sucesso!");
                }
            }.start();
            rediricionar();
        }
    }
    private void rediricionar(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/view/Fornecedores.fxml"));
            Scene scene = new Scene(root);
            PaginaUserController.stage.setScene(scene);
            PaginaUserController.stage.setTitle("Fornecedores");
            PaginaUserController.stage.show();
        }catch(Exception e){
        
            
        }
    }
    
}
