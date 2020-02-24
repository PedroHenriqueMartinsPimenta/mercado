/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Produto;
import model.ProdutosHasFornecedor;

/**
 *
 * @author Pedro Henrique
 */
public class ProdutoDAO {
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;
    
    public boolean cadastrarProduto(Produto produto){
        boolean efetuado = false;
        con = Conexao.getConnection();
        String sql = "INSERT INTO PRODUTOS (CODIGO, DESCRICAO, QUANTIDADE, VALIDADE, PRECO, FORNECEDORES_CNPJ) VALUES(?,?,?,?,?,?)";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getDescricao());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setString(4, produto.getValidade());
            stmt.setDouble(5, produto.getPreco());
            stmt.setString(6, produto.getFornecedor());
            stmt.execute();
            efetuado = true;
        } catch (Exception ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return efetuado;
    }
    public ArrayList getProduto(String codigo){
        ArrayList<Produto> result = new ArrayList<>();
        con = Conexao.getConnection();
        String sql = "SELECT * FROM PRODUTOS WHERE CODIGO = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, codigo);
            rs = stmt.executeQuery();
            while(rs.next()){
                Produto produto = new Produto();
                produto.setCodigo(rs.getString("CODIGO"));
                produto.setDescricao(rs.getString("DESCRICAO"));
                produto.setFornecedor(rs.getString("FORNECEDORES_CNPJ"));
                produto.setPreco(rs.getDouble("PRECO"));
                produto.setQuantidade(rs.getInt("QUANTIDADE"));
                produto.setValidade(rs.getString("VALIDADE"));
                result.add(produto);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    return result;
    }
    
    public ArrayList<ProdutosHasFornecedor> getAllProdutosHasFornecedor(){
        ArrayList<ProdutosHasFornecedor> result = new ArrayList<>();
        String sql = "SELECT PRODUTOS.CODIGO, PRODUTOS.DESCRICAO, PRODUTOS.QUANTIDADE, PRODUTOS.PRECO, PRODUTOS.VALIDADE, FORNECEDORES.NOME, FORNECEDORES.CNPJ FROM PRODUTOS INNER JOIN FORNECEDORES ON PRODUTOS.FORNECEDORES_CNPJ = FORNECEDORES.CNPJ";
        con = Conexao.getConnection();
        try{
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                ProdutosHasFornecedor model = new ProdutosHasFornecedor();
                model.setCodigo(rs.getString("CODIGO"));
                model.setDescricao(rs.getString("DESCRICAO"));
                model.setFornecedor(rs.getString("NOME"));
                model.setPreco(rs.getDouble("PRECO"));
                model.setQuantidade(rs.getInt("QUANTIDADE"));
                model.setValidade(rs.getDate("VALIDADE"));
                model.setCnpj(rs.getString("CNPJ"));
                result.add(model);
            }
        }catch(Exception e){
            System.out.println("Erro");
        }
        return result;
    }
     public ArrayList<ProdutosHasFornecedor> getProdutosHasFornecedor(String codigo){
        ArrayList<ProdutosHasFornecedor> result = new ArrayList<>();
        String sql = "SELECT PRODUTOS.CODIGO, PRODUTOS.DESCRICAO, PRODUTOS.QUANTIDADE, PRODUTOS.PRECO, PRODUTOS.VALIDADE, FORNECEDORES.NOME, FORNECEDORES.CNPJ FROM PRODUTOS INNER JOIN FORNECEDORES ON PRODUTOS.FORNECEDORES_CNPJ = FORNECEDORES.CNPJ WHERE PRODUTOS.CODIGO = ?";
        con = Conexao.getConnection();
        try{
            stmt = con.prepareStatement(sql);
            stmt.setString(1, codigo);
            rs = stmt.executeQuery();
            while(rs.next()){
                ProdutosHasFornecedor model = new ProdutosHasFornecedor();
                model.setCodigo(rs.getString("CODIGO"));
                model.setDescricao(rs.getString("DESCRICAO"));
                model.setFornecedor(rs.getString("NOME"));
                model.setPreco(rs.getDouble("PRECO"));
                model.setQuantidade(rs.getInt("QUANTIDADE"));
                model.setValidade(rs.getDate("VALIDADE"));
                model.setCnpj(rs.getString("CNPJ"));
                result.add(model);
            }
        }catch(Exception e){
            System.out.println("Erro");
        }
        return result;
    }
    
    public boolean deleteProduto(String codigo){
        String sql = "DELETE FROM PRODUTOS WHERE CODIGO = ?";
        con = Conexao.getConnection();
        try{
            stmt = con.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.execute();
            return true;
        }catch(SQLException e){
           if(e.getErrorCode() == 1451){
               JOptionPane.showMessageDialog(null, "Não pode deletar este item \n ele estar sendo ultilizado em uma venda \n caso necessario o numero do erro: "+ e.getErrorCode());
           }
        }
        
        return false;
    }
    
    public boolean updateProduto(ProdutosHasFornecedor dados){
        String sql = "UPDATE PRODUTOS SET DESCRICAO = ?, QUANTIDADE = ?, VALIDADE = ?, PRECO = ?, FORNECEDORES_CNPJ = ? WHERE CODIGO = ? ";
        con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, dados.getDescricao());
            stmt.setInt(2, dados.getQuantidade());
            stmt.setDate(3, new Date(dados.getValidade().getYear(), dados.getValidade().getMonth(), dados.getValidade().getDay()));
            stmt.setDouble(4, dados.getPreco());
            stmt.setString(5, dados.getCnpj());
            stmt.setString(6, dados.getCodigo());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
