/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.print.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Pedro Henrique
 */
public class Printer {
    private static PrintService impressora;
    public Printer(){
        detectaImpressora();
    }
    public void detectaImpressora(){
        DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        PrintService []print = PrintServiceLookup.lookupPrintServices(df, null);
        for(PrintService p : print){
            System.out.println("Impressora encontrada: "+p.getName());
            if(p.getName().contains("Text") || p.getName().contains("Generic")){
                System.out.println("Impressora selecionada: "+ p.getName());
                impressora = p;
                break;
            }
        } 
    }
    
    public synchronized boolean imprime(String texto) throws PrintException{
        if(impressora == null){
            new Thread(){
                @Override
                public void run(){
                    JOptionPane.showMessageDialog(null, "Impressora não encontrada");
                }
            }.start();
        }else{
            DocPrintJob dpj = impressora.createPrintJob();
            InputStream strem = new ByteArrayInputStream(texto.getBytes());
            DocFlavor  flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc doc = new SimpleDoc(strem, flavor, null);
            dpj.print(doc, null);
        }
        return false;
    }
}
