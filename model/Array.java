/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Pedro Henrique
 */
public class Array {
    public ArrayList somarArrayQtdAndPreco(ArrayList<Venda> model, ArrayList<Venda> result){
       
        double []preco = new double[model.size()];
        
        for(int i =0; i < model.size();i++){
                double d = 0;
            for(int a = 0; a < model.size();a++){
                if(model.get(i).getCodigo() == result.get(a).getCodigo()){
                    d += result.get(a).getProduto().getPreco() * result.get(a).getProduto().getQuantidade();
                }
            }
            preco[i] = d;
        }
       int codigo = 0;
       
        ArrayList<Venda> retorno = new ArrayList<>();
        int []mod = new int[model.size()];
       for(int i = 0; i < model.size();i++){
          model.get(i).setPrecoTotal(preco[i]);
          mod[i] = model.get(i).getCodigo();
      }
      Arrays.sort(mod);
      for(int i = 0; i < mod.length;i++){
          System.out.println(codigo +" = "+ mod[i]);
          if(codigo < mod[i]){
              Venda venda = new Venda();
                venda = model.get(i);
              retorno.add(venda);
              codigo = mod[i];
          }
        
      }
      for(Venda a : retorno){
          System.out.println(a.getCodigo());
      }
        return retorno;
    }
    
}
