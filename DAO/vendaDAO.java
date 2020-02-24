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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Cliente;
import model.Itens;
import model.Produto;
import model.Venda;

/**
 *
 * @author Pedro Henrique
 */
public class vendaDAO {
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;
    
    public boolean vender(ArrayList<Produto> produtos, Cliente cliente){
        con = Conexao.getConnection();
        boolean result = setVenda(cliente.getCpf(), cliente.getPago(), con);
        if(result){
            result = setItens(getUltimoCodigo(), produtos, con);
                if(result){
                    return true;
                }
        }
        return false;
    }
    
    private boolean setVenda(String cpf, double valor, Connection con){
           java.util.Date data_dados = new java.util.Date();
           Date data = new Date(data_dados.getYear(), data_dados.getMonth(), data_dados.getDate());
            String strDate = (data_dados.getYear()+1900) +"-"+ (data_dados.getMonth()+1) +"-"+ data_dados.getDate()+" "+data_dados.getHours()+":"+data_dados.getMinutes()+":"+data_dados.getSeconds();
           
           String sql = "INSERT INTO VENDA(DATA, CLIENTE_CPF, PAGO) VALUES('"+strDate+"', ?, ?)";
           try {
               stmt = con.prepareStatement(sql);
               stmt.setString(1, cpf);
               stmt.setDouble(2, valor);
               stmt.execute();
               return true;
           } catch (SQLException ex) {
               Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       
    return false;
    }
    private boolean setItens(int venda, ArrayList<Produto> produtos, Connection con){
        boolean finsh = false;
       for(Produto produto : produtos){
           String sql = "INSERT INTO ITENS(VENDA_CODIGO, PRODUTOS_CODIGO, QUANTIDADE) VALUES(?, ?, ?)";
           try {
               stmt = con.prepareStatement(sql);
               stmt.setInt(1, venda);
               stmt.setString(2, produto.getCodigo());
               stmt.setInt(3, produto.getQuantidade());
               stmt.execute();
               retirarQtd(produto.getCodigo(), produto.getQuantidade(), con);
               finsh = true;
           } catch (SQLException ex) {
               Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
        return finsh;
    }
    private void retirarQtd(String codigo, int qtd, Connection con){
        String sql = "UPDATE PRODUTOS SET QUANTIDADE = QUANTIDADE - "+qtd+" WHERE CODIGO = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getUltimoCodigo(){
        con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement("SELECT CODIGO FROM VENDA ORDER BY CODIGO DESC LIMIT 1");
            rs = stmt.executeQuery();
            while(rs.next()){
                return rs.getInt("CODIGO");
            }
        } catch (SQLException ex) {
            Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public ArrayList<Venda> allVendas(){
        ArrayList<Venda> result = new ArrayList<>();
        String sql = "SELECT DISTINCT VENDA.CODIGO, VENDA.PAGO, PRODUTOS.CODIGO AS PRODUTO_CODIGO, CLIENTE.CPF, CLIENTE.NOME, CLIENTE.SOBRENOME, CLIENTE.RUA, CLIENTE.CIDADE, CLIENTE.ESTADO, VENDA.DATA, ITENS.QUANTIDADE, PRODUTOS.DESCRICAO, PRODUTOS.PRECO FROM itens INNER JOIN PRODUTOS ON ITENS.PRODUTOS_CODIGO = PRODUTOS.CODIGO INNER JOIN VENDA ON ITENS.VENDA_CODIGO = VENDA.CODIGO INNER JOIN CLIENTE ON CLIENTE.CPF = VENDA.CLIENTE_CPF GROUP BY VENDA.CODIGO ORDER BY DATA DESC";
        con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                Venda venda = new Venda();
                Cliente cliente = new Cliente();
                Produto produto = new Produto();
               venda.setCodigo(rs.getInt("CODIGO"));
               cliente.setCpf(rs.getString("CPF"));
               cliente.setNome(rs.getString("NOME"));
               cliente.setSobrenome(rs.getString("SOBRENOME"));
               cliente.setRua(rs.getString("RUA"));
               cliente.setCidade(rs.getString("CIDADE"));
               cliente.setEstado(rs.getString("ESTADO"));
               venda.setData(rs.getDate("DATA"));
               produto.setQuantidade(rs.getInt("QUANTIDADE"));
               produto.setDescricao(rs.getString("DESCRICAO"));
               produto.setCodigo(rs.getString("PRODUTO_CODIGO"));
               produto.setPreco(rs.getDouble("PRECO"));
               venda.setCliente(cliente);
               venda.setProduto(produto);
               venda.setPago(rs.getDouble("PAGO"));
               result.add(venda);
            }
        } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERRO \n Codigo de erro: "+ ex.getErrorCode());
            Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    
    public ArrayList<Venda> getVendas(String cpf){
        ArrayList<Venda> result = new ArrayList<>();
        String sql = "SELECT DISTINCT VENDA.CODIGO,VENDA.PAGO,PRODUTOS.CODIGO AS PRODUTO_CODIGO, CLIENTE.CPF, CLIENTE.NOME, CLIENTE.SOBRENOME, CLIENTE.RUA, CLIENTE.CIDADE, CLIENTE.ESTADO, VENDA.DATA, ITENS.QUANTIDADE, PRODUTOS.DESCRICAO, PRODUTOS.PRECO FROM itens INNER JOIN PRODUTOS ON ITENS.PRODUTOS_CODIGO = PRODUTOS.CODIGO INNER JOIN VENDA ON ITENS.VENDA_CODIGO = VENDA.CODIGO INNER JOIN CLIENTE ON CLIENTE.CPF = VENDA.CLIENTE_CPF WHERE CLIENTE.CPF = ? GROUP BY VENDA.CODIGO ORDER BY DATA DESC";
        con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
            while(rs.next()){
               Venda venda = new Venda();
               Cliente cliente = new Cliente();
               Produto produto = new Produto();
               venda.setCodigo(rs.getInt("CODIGO"));
               cliente.setCpf(rs.getString("CPF"));
               cliente.setNome(rs.getString("NOME"));
               cliente.setSobrenome(rs.getString("SOBRENOME"));
               cliente.setRua(rs.getString("RUA"));
               cliente.setCidade(rs.getString("CIDADE"));
               cliente.setEstado(rs.getString("ESTADO"));
               venda.setData(rs.getDate("DATA"));
               produto.setQuantidade(rs.getInt("QUANTIDADE"));
               produto.setDescricao(rs.getString("DESCRICAO"));
               produto.setPreco(rs.getDouble("PRECO"));
               produto.setCodigo(rs.getString("PRODUTO_CODIGO"));
               venda.setCliente(cliente);
               venda.setProduto(produto);
               venda.setPago(rs.getDouble("PAGO"));
               result.add(venda);
               
            }
        } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERRO \n Codigo de erro: "+ ex.getErrorCode());
            Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    private boolean addQtdProdutos(int qtd, String codigo){
        con = Conexao.getConnection();
        String sql = "UPDATE PRODUTOS SET QUANTIDADE = QUANTIDADE + ? WHERE CODIGO = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, qtd);
            stmt.setString(2, codigo);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERRO \n Codigo de erro: "+ ex.getErrorCode());
            Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public boolean deleteItens(int codigoVenda, String codigoProduto, int qtd){
        boolean result = addQtdProdutos(qtd, codigoProduto);
        if(result){
            con = Conexao.getConnection();
            String sql = "DELETE FROM ITENS WHERE VENDA_CODIGO = ? AND PRODUTOS_CODIGO = ? AND QUANTIDADE = ? LIMIT 1";
            try {
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, codigoVenda);
                stmt.setString(2, codigoProduto);
                stmt.setInt(3, qtd);
                stmt.execute();
               return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERRO \n Codigo de erro: "+ ex.getErrorCode());
                Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        return false;
    }
    public boolean deleteVenda(int codigoVenda,ArrayList<String> codigoProduto, int qtd){
        boolean result = false;
        for(String codigo : codigoProduto){
             result = deleteItens(codigoVenda, codigo, qtd);
             
             if(!result){
                 break;
             }
        }
        if(result){
            con = Conexao.getConnection();
            String sql = "DELETE FROM VENDA WHERE CODIGO = ?";
            try {
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, codigoVenda);
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "ERRO \n Codigo de erro: "+ ex.getErrorCode());
                Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public ArrayList<Itens> getItensHasProdutos(int codigoVenda){
        ArrayList<Itens> dados = new ArrayList<>();
        String sql = "SELECT * FROM ITENS INNER JOIN PRODUTOS ON ITENS.PRODUTOS_CODIGO = PRODUTOS.CODIGO WHERE ITENS.VENDA_CODIGO = ?";
        con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, codigoVenda);
            rs = stmt.executeQuery();
            while(rs.next()){
                Produto produto = new Produto();
                Itens itens = new Itens();
                produto.setCodigo(rs.getString("CODIGO"));
                produto.setDescricao(rs.getString("DESCRICAO"));
                produto.setFornecedor(rs.getString("FORNECEDORES_CNPJ"));
                produto.setPreco(rs.getDouble("PRECO"));
                itens.setQuantidade(rs.getInt("ITENS.QUANTIDADE"));
                itens.setProduto(produto);
                itens.setCodigoVenda(codigoVenda);
                dados.add(itens);
            }
        } catch (SQLException ex) {
            Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dados;
    }
    
    public boolean updateVenda(String cpf,double pago, int codigo){
        String sql = "UPDATE VENDA SET CLIENTE_CPF = ?, PAGO = ? WHERE CODIGO = ?";
        con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, cpf);
            stmt.setDouble(2, pago);
            stmt.setInt(3, codigo);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO: \n Codigo de erro: "+ex.getErrorCode());
            Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean updateItem(Itens antigo, Itens novo){
        boolean result = addQtdProdutos(antigo.getQuantidade(), antigo.getProduto().getCodigo());
        if(result){
            String sql = "UPDATE ITENS SET PRODUTOS_CODIGO = ?, QUANTIDADE = ? WHERE PRODUTOS_CODIGO = ? AND VENDA_CODIGO = ? AND QUANTIDADE = ? ";
           con = Conexao.getConnection();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, novo.getCodigoProduto());
            stmt.setInt(2, novo.getQuantidade());
            stmt.setString(3, antigo.getProduto().getCodigo());
            stmt.setInt(4, antigo.getCodigoVenda());
            stmt.setInt(5, antigo.getQuantidade());
            stmt.execute();
            retirarQtd(novo.getCodigoProduto(), novo.getQuantidade(), con);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(vendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
           return false;
    }
}
