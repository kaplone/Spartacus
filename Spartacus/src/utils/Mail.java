package utils;

import java.util.Date; 
import java.util.Properties; 

import javax.activation.CommandMap; 
import javax.activation.MailcapCommandMap; 
import javax.mail.BodyPart; 
import javax.mail.MessagingException;
import javax.mail.Multipart; 
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeBodyPart; 
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMultipart; 
import javax.mail.Message ;
 
 
public class Mail extends javax.mail.Authenticator { 

  private String _from; 
 
  private String _port; 
  private String _sport; 
 
  private String _host;
  
  private String _subject; 
  private String _body; 
 
  private boolean _auth; 
   
  private boolean _debuggable; 
 
  private Multipart _multipart; 
  
  private Message msg;
  
  private Session session ;
  
  private Properties props ;
  
  private GMailAuthenticator auth;
 
 
  public Mail() { 
    this._host = "smtp.gmail.com"; // default smtp server 
    this._port = "465"; // default smtp port 
    this._sport = "465"; // default socketfactory port 

    this._from = ""; // email sent from  	
    this._subject = ""; // email subject 
    this._body = ""; // email body 
 
    this._debuggable = false; // debug mode on or off - default off 
    this._auth = true; // smtp authentication - default on 
 
    this._multipart = new MimeMultipart(); 
 
    // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added. 
    MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
    mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
    mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
    mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
    mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
    mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
    CommandMap.setDefaultCommandMap(mc); 
  } 
 
  public void send(final Mail mail, final String user, final String pass, final String dest) throws Exception { 
    
	Thread t_send = new Thread(){
		 public void run(){
			 
			try{ 
		    	
		      props = _setProperties(); 
		      
		      auth = new GMailAuthenticator(user, pass);
		    	
		      session = Session.getInstance(props, auth);
		    	
		      msg = new MimeMessage(session);
		      
		      msg.setFrom(new InternetAddress(mail._from)); 
		       
		      InternetAddress addressTo = new InternetAddress(dest); 
		      
		      msg.setRecipient(MimeMessage.RecipientType.TO, addressTo); 
		 
		      msg.setSubject( mail._subject); 
		      
		      msg.setSentDate(new Date()); 
		 
		      // setup message body 
		      BodyPart messageBodyPart = new MimeBodyPart(); 
		      messageBodyPart.setText(mail._body); 
		      mail._multipart.addBodyPart(messageBodyPart); 
		 
		      // Put parts in message 
		      msg.setContent(mail._multipart); 
		 
		      // send email 
		      Transport.send(msg); 
				      
			}catch (MessagingException m){
				m.printStackTrace();
			}
		  }};
		  t_send.start();
  } 

  private Properties _setProperties() { 
	  
	props = System.getProperties();  
 
    props.put("mail.smtp.host", _host); 
 
    if(_debuggable) { 
      props.put("mail.debug", "true"); 
    } 
 
    if(_auth) { 
      props.put("mail.smtp.auth", "true"); 
    } 
 
    props.put("mail.smtp.port", _port); 
    props.put("mail.smtp.socketFactory.port", _sport); 
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
    props.put("mail.smtp.socketFactory.fallback", "true"); 
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.EnableSSL.enable", "true");
    props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.setProperty("mail.smtp.socketFactory.fallbac k", "false");
    
 
    return props; 
  } 
 
  // the getters and setters 
  
	public String get_from() {
		return _from;
	}

	public void set_from(String _from) {
		this._from = _from;
	}

	public String get_host() {
		return _host;
	}

	public void set_host(String _host) {
		this._host = _host;
	}

	public String get_subject() {
		return _subject;
	}

	public void set_subject(String _subject) {
		this._subject = _subject;
	}

	public String get_body() {
		return _body;
	}

	public void set_body(String _body) {
		this._body = _body;
	} 
} 