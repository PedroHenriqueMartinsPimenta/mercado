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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.LoginModel;

/**
 *
 * @author Pedro Henrique
 */
public class LoginDAO {
    private Connection con;
    private PreparedStatement tpmt;
    private ResultSet rs;
    
   public List<LoginModel> getDados(String cpf){
       ArrayList<LoginModel> model = new ArrayList<>();
       con = Conexao.getConnection();
        try {
            tpmt = con.prepareStatement("SELECT * FROM USUARIO WHERE CPF = ?");
            tpmt.setString(1, cpf);
            rs = tpmt.executeQuery();
            while(rs.next()){
                LoginModel dados = new LoginModel();
                dados.setCpf(cpf);
                dados.setSenha(rs.getString("SENHA"));
                model.add(dados);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        getAno();
       return model;
   } 
   
   public int getAno(){
       int ano = 0;
       con = Conexao.getConnection();
       String sql = "SELECT * FROM ANO";
        try {
            tpmt = con.prepareStatement(sql);
            rs = tpmt.executeQuery();
            System.out.println(rs);
            if(rs != null){
                while(rs.next()){
                     ano = rs.getInt("ANO.ANO");
                }
            }else{
                sql = "INSERT INTO ANO(ANO)VALUE(?);";
                tpmt = con.prepareStatement(sql);
                tpmt.setInt(1, new Date().getYear()+1900);
                tpmt.execute();
                ano = (new Date().getYear()+1900);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
       return ano;
   }
}
