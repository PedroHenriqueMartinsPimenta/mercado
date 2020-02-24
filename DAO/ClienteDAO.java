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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cliente;

/**
 *
 * @author Pedro Henrique
 */
public class ClienteDAO {
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;
    
    public ArrayList getClient(String cpf){
        ArrayList<Cliente> result = new ArrayList<>();
        con = Conexao.getConnection();
        String sql = "SELECT * FROM CLIENTE WHERE CPF = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
            while(rs.next()){
                Cliente cliente = new Cliente();
                cliente.setCidade(rs.getString("CIDADE"));
                cliente.setCpf(cpf);
                cliente.setEstado(rs.getString("ESTADO"));
                cliente.setNome(rs.getString("NOME"));
                cliente.setRua(rs.getString("RUA"));
                cliente.setSobrenome(rs.getString("SOBRENOME"));
                result.add(cliente);
            }
        } catch (Exception ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    public boolean cadastrar(Cliente dados){
        con = Conexao.getConnection();
        boolean efetuado = false;
        Date data = new Date();
        String sql = "INSERT INTO CLIENTE (CPF, NOME, SOBRENOME, RUA, CIDADE, ESTADO, DATA_CADASTRO) VALUES(?, ?, ?, ?, ?, ?,?)";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, dados.getCpf());
            stmt.setString(2, dados.getNome());
            stmt.setString(3, dados.getSobrenome());
            stmt.setString(4, dados.getRua());
            stmt.setString(5, dados.getCidade());
            stmt.setString(6, dados.getEstado());
            stmt.setDate(7, new java.sql.Date(data.getYear(),data.getMonth(),data.getDate()));
            stmt.execute();
            
            efetuado = true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    return efetuado;
    }
    
    public ArrayList<Cliente> allClientes(){
    ArrayList<Cliente> result = new ArrayList<>();
        con = Conexao.getConnection();
        String sql = "SELECT * FROM CLIENTE";
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                Cliente cliente = new Cliente();
                cliente.setCpf(rs.getString("CPF"));
                cliente.setCidade(rs.getString("CIDADE"));
                cliente.setEstado(rs.getString("ESTADO"));
                cliente.setNome(rs.getString("NOME"));
                cliente.setRua(rs.getString("RUA"));
                cliente.setSobrenome(rs.getString("SOBRENOME"));
                result.add(cliente);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    return result;
    
    }
    
    public boolean updateCliente(Cliente dados){
        con = Conexao.getConnection();
        String sql = "UPDATE CLIENTE SET NOME = ?, SOBRENOME = ?, RUA = ?, CIDADE = ?, ESTADO = ? WHERE CPF = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, dados.getNome());
            stmt.setString(2, dados.getSobrenome());
            stmt.setString(3, dados.getRua());
            stmt.setString(4, dados.getCidade());
            stmt.setString(5, dados.getEstado());
            stmt.setString(6, dados.getCpf());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
