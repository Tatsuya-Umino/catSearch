package services;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailService {
	public void sendEmail(String title, String message, String toAdd, String fromAdd) throws EmailException {
		SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);
        email.setSSLOnConnect(true);
        email.setAuthentication("play.neko.search.tp031", "playnekosearchtp031");
		email.setFrom(fromAdd);
        email.setSubject(title);
        email.setMsg(message);
        email.setCharset("UTF-8");
        email.addTo(toAdd);
        email.send();
	}
}