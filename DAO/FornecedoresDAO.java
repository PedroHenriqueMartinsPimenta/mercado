/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Fornecedor;

/**
 *
 * @author Pedro Henrique
 */
public class FornecedoresDAO {
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;
    
    public ArrayList getFornecedor(String cnpj){
        ArrayList<Fornecedor> result = new ArrayList<>();
        con = Conexao.getConnection();
        String sql = "SELECT * FROM FORNECEDORES WHERE CNPJ = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cnpj);
            rs = stmt.executeQuery();
            while(rs.next()){
                Fornecedor dados = new Fornecedor();
                dados.setCnpj(cnpj);
                dados.setNome(rs.getString("NOME"));
                result.add(dados);
            }
        } catch (Exception ex) {
            Logger.getLogger(FornecedoresDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    return result;
    }
    
    public boolean cadastrarFornecedor(Fornecedor dados){
        boolean efetuado = false;
        con = Conexao.getConnection();
        String sql = "INSERT INTO FORNECEDORES(CNPJ, NOME)VALUES(?, ?)";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, dados.getCnpj());
            stmt.setString(2, dados.getNome());
            stmt.execute();
            efetuado = true;
        } catch (SQLException ex) {
            Logger.getLogger(FornecedoresDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return efetuado;
    }
    public ArrayList<Fornecedor> allFornecedores(){
        ArrayList<Fornecedor> result = new ArrayList<>();
        
        con  = Conexao.getConnection();
        String sql = "SELECT * FROM FORNECEDORES";
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                Fornecedor dados = new Fornecedor();
                dados.setCnpj(rs.getString("CNPJ"));
                dados.setNome(rs.getString("NOME"));
                result.add(dados);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FornecedoresDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public boolean deleteFornecedor(String cnpj){
        con = Conexao.getConnection();
        String sql = "DELETE FROM FORNECEDORES WHERE CNPJ = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cnpj);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            new Thread(){
                @Override
                public void run(){
                    JOptionPane.showMessageDialog(null, "ERRO \n codigo de erro: " + ex.getErrorCode());
                }
            }.start();
        }
        return false;
    }
    
    public boolean updateFornecedor(Fornecedor dados){
        con = Conexao.getConnection();
        String sql = "UPDATE FORNECEDORES SET NOME = ? WHERE CNPJ = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, dados.getNome());
            stmt.setString(2, dados.getCnpj());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            new Thread(){
                @Override
                public void run(){
                    JOptionPane.showMessageDialog(null, "ERRO "+ex.getMessage()+"\n codigo de erro: "+ ex.getErrorCode());
                }
            }.start();
            Logger.getLogger(FornecedoresDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return false;
    }
}
