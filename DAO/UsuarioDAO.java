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
import model.Usuario;

/**
 *
 * @author Pedro Henrique
 */
public class UsuarioDAO {
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;
    
    public boolean cadastrarUser(Usuario user){
        boolean efetuado = false;
        con = Conexao.getConnection();
        String sql = "INSERT INTO USUARIO (CPF, NOME_USER, SENHA, EMAIL) VALUES (?,?,?,?)";
        
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, user.getCpf());
            stmt.setString(2, user.getNome_user());
            stmt.setString(3, user.getSenha());
            stmt.setString(4, user.getEmail());
            stmt.execute();
            efetuado = true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    return efetuado;
    }
    
    public ArrayList<Usuario> allUsers(){
        ArrayList<Usuario> result = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO";
        con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                Usuario dados = new Usuario();
                dados.setCpf(rs.getString("CPF"));
                dados.setEmail(rs.getString("EMAIL"));
                dados.setNome_user(rs.getString("NOME_USER"));
                dados.setSenha(rs.getString("SENHA"));
                result.add(dados);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    public ArrayList<Usuario> getUser(String cpf){
        ArrayList<Usuario> result = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO WHERE CPF = ?";
        con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
            while(rs.next()){
                Usuario dados = new Usuario();
                dados.setCpf(rs.getString("CPF"));
                dados.setEmail(rs.getString("EMAIL"));
                dados.setNome_user(rs.getString("NOME_USER"));
                dados.setSenha(rs.getString("SENHA"));
                result.add(dados);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public boolean deleteUser(String cpf){
            String sql = "DELETE FROM USUARIO WHERE CPF = ?";
            con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cpf);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO \n codigo de erro: "+ ex.getErrorCode());
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public boolean updateUser(Usuario dados){
        String sql = "UPDATE USUARIO SET NOME_USER = ?, EMAIL = ? WHERE CPF = ?";
        con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, dados.getNome_user());
            stmt.setString(2, dados.getEmail());
            stmt.setString(3, dados.getCpf());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO \n codigo do erro: "+ ex.getErrorCode());
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
