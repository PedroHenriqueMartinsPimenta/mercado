/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.sun.media.jfxmedia.logging.Logger;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Pedro Henrique
 */
public class Mail {
     public boolean enviarEmail(String para,String assunto, String mensagem){
         boolean result = false;
         Properties props = new Properties();
            /** Parâmetros de conexão com servidor Gmail */
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
 
            Session session = Session.getDefaultInstance(props,
                        new javax.mail.Authenticator() {
                             protected PasswordAuthentication getPasswordAuthentication() 
                             {
                                   return new PasswordAuthentication("trydeveloper70@gmail.com", "trydeveloper123");
                             }
                        });
 
            /** Ativa Debug para sessão */
            session.setDebug(true);
 
            try {
 
                  Message message = new MimeMessage(session);
                  message.setFrom(new InternetAddress("trydeveloper70@gmail.com")); //Remetente
 
                  Address[] toUser = InternetAddress.parse(para);  
 
                  message.setRecipients(Message.RecipientType.TO, toUser);
                  message.setSubject(assunto);//Assunto
                  message.setText(mensagem);
                  /**Método para enviar a mensagem criada*/
                  Transport.send(message);
 
                  result = true;
 
             } catch (MessagingException e) {
                 System.out.println(e.getCause());
                 result = false;
            }
            return result;
     }
     
     public boolean sendEmail(String destinatario, String assunto, String mensagem){
         String parametros = "email="+destinatario+" &assunto="+assunto+" &mensagem="+mensagem;
          try {
                URL url = new URL("https://pedro-henrique34.000webhostapp.com/acess/sendMail.php");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dtStream = new DataOutputStream(connection.getOutputStream());
                dtStream.writeBytes(parametros);
                dtStream.flush();
                dtStream.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                String result = responseOutput.toString();
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
     }
    
}
