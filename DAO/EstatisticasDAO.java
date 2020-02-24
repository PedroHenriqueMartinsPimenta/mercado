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
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.EstastiticaAnual;
import model.EstatisticaHorario;

/**
 *
 * @author Pedro Henrique
 */
public class EstatisticasDAO {
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;
    private int meses[] = {1,2,3,4,5,6,7,8,9,10,11,12};
    private int horas[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23};
    
    public double[] getMesMelhorVenda(){
        Date date = new Date();
        String sql = "SELECT SUM(PRODUTOS.PRECO * ITENS.QUANTIDADE) AS TOTAL FROM ITENS INNER JOIN PRODUTOS ON ITENS.PRODUTOS_CODIGO = PRODUTOS.CODIGO INNER JOIN VENDA ON ITENS.VENDA_CODIGO = VENDA.CODIGO WHERE MONTH(VENDA.DATA) = ? AND YEAR(VENDA.DATA) = ?";
        double result[] = new double[meses.length];
        con = Conexao.getConnection();
        for(int i = 0; i < meses.length;i++){
            try {
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, meses[i]);
                stmt.setInt(2, date.getYear()+ 1900);
                rs = stmt.executeQuery();
                while(rs.next()){
                    result[i] = rs.getDouble("TOTAL");
                  
                }
            } catch (SQLException ex) {
                Logger.getLogger(EstatisticasDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    public ArrayList<EstastiticaAnual> getEstatiticaAnual(){
        ArrayList<EstastiticaAnual> result = new ArrayList<>();
        con = Conexao.getConnection();
        int ano = new LoginDAO().getAno();
        int anoAtual = new Date().getYear() + 1900;
        String sql = "SELECT SUM(PRODUTOS.PRECO * ITENS.QUANTIDADE) AS TOTAL FROM ITENS INNER JOIN PRODUTOS ON ITENS.PRODUTOS_CODIGO = PRODUTOS.CODIGO INNER JOIN VENDA ON ITENS.VENDA_CODIGO = VENDA.CODIGO WHERE YEAR(VENDA.DATA) = ?";
        while(ano <= anoAtual){
            try {
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, ano);
                rs = stmt.executeQuery();
                while(rs.next()){
                    EstastiticaAnual anual = new EstastiticaAnual();
                    anual.setAno(ano);
                    anual.setValor(rs.getDouble("TOTAL"));
                    result.add(anual);
                }
                ano++;
            } catch (SQLException ex) {
                Logger.getLogger(EstatisticasDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
   
    public ArrayList<EstatisticaHorario> getEstatiticaHorario(){
        ArrayList<EstatisticaHorario> result = new ArrayList<>();
        int ano = new Date().getYear()+1900;
        con = Conexao.getConnection();
        String sql = "SELECT SUM(PRODUTOS.PRECO * ITENS.QUANTIDADE) AS TOTAL FROM ITENS INNER JOIN PRODUTOS ON ITENS.PRODUTOS_CODIGO = PRODUTOS.CODIGO INNER JOIN VENDA ON ITENS.VENDA_CODIGO = VENDA.CODIGO WHERE HOUR(VENDA.DATA) = ? AND YEAR(VENDA.DATA) = ?";
       for(int i = 0; i < horas.length; i++){
            try {
                stmt = con.prepareStatement(sql);
                stmt.setInt(1, horas[i]);
                stmt.setInt(2, ano);
                rs = stmt.executeQuery();
                while(rs.next()){
                    EstatisticaHorario hora = new EstatisticaHorario();
                    hora.setHora(horas[i]);
                    hora.setValor(rs.getDouble("TOTAL"));
                    result.add(hora);
                }
            } catch (SQLException ex) {
                Logger.getLogger(EstatisticasDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
        return result;
    }
}
