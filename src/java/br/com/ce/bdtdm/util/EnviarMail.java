/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.esp.util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author francisco
 */
public class EnviarMail {

    public void sendMail(String email, String nome) throws MessagingException {
        Properties p = new Properties();
        p.put("mail.host", "mail.esp.ce.gov.br");
        Session session = Session.getDefaultInstance(p, null);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("noreply@esp.ce.gov.br"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        msg.setSentDate(new Date());
        msg.setSubject("Atualização de Senha no BDTDM");
        msg.setContent("Sua senha foi alterada com sucesso."
                + "<br/><br/>Nome: " + nome + "<br/>Senha: 123456"
                + "<br/><br/>Entre no sistema e altere novamente."
                + "<br/><br/><br/>Escola de Saúde Pública do Ceará - ESP/CE"
                + "<br/>Governo do Estado do Ceará"
                + "<br/><br/>(E-mail automático. Não responda.) ", "text/html");
        // evniando mensagem ("tentando")
        Transport.send(msg);
    }
}
