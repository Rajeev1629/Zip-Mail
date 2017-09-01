package mail;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class EmailAttachmentSender {

	public static void sendEmailWithAttachments(String host, String port,
			final String userName, final String password, String[] toAddress,
			String subject, String message, String[] attachFiles,String mailStatus)
			throws AddressException, MessagingException {
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);
		properties.put("mail.status", mailStatus);

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);
        
		msg.setFrom(new InternetAddress(userName));
		//InternetAddress[] toAddresses = { new InternetAddress(toAddress[]) };
		InternetAddress[] toAddresses = new InternetAddress[toAddress.length] ;
		
		//msg.setRecipients(Message.RecipientType.TO, toAddresses);
		for (int i = 0; i < toAddress.length; i++) {
			toAddresses[i] = new InternetAddress(toAddress[i]);
				
		}

		msg.addRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// adds attachments
		if (attachFiles != null && attachFiles.length > 0) {
			for (String filePath : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();

				try {
					attachPart.attachFile(filePath);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				multipart.addBodyPart(attachPart);
			}
		}

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);
		
		// sends the e-mail
		Transport.send(msg);
		
		

	}

	/**
	 * Test sending e-mail with attachments
	 */
	public static void main(String[] args) {
		
		
		
		Properties prop = new Properties();
		InputStream input = null;
		String OUTPUT_ZIP_FILE = "Timesheets.zip";
	    String SOURCE_FOLDER ;
	   

		try {

			input = EmailAttachmentSender.class.getClass().getResourceAsStream("/mail.properties");
			
			// load a properties file
			prop.load(input);
			SOURCE_FOLDER = prop.getProperty("mail.attachment");
			Zip appZip = new Zip();
			 System.out.println(SOURCE_FOLDER);
	        appZip.generateFileList(new File(SOURCE_FOLDER), SOURCE_FOLDER);
	        System.out.println(appZip.getFileList());
	        appZip.zipIt(SOURCE_FOLDER, OUTPUT_ZIP_FILE);
	       
			
	        try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// get the property value and print it out
			System.out.println(prop.getProperty("mail.smtp.host"));
			System.out.println(prop.getProperty("mail.smtp.socketFactory.port"));
			System.out.println(prop.getProperty("mail.login.username"));
			System.out.println(prop.getProperty("mail.login.password"));
			System.out.println(prop.getProperty("mail.to")+"skjgfuh");
			System.out.println(prop.getProperty("mail.status"));
			
			

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		// SMTP info
		String status = prop.getProperty("mail.status");
		System.out.println(status);
		String host = prop.getProperty("mail.smtp.host");
		String port = prop.getProperty("mail.smtp.socketFactory.port");
		String mailFrom = prop.getProperty("mail.login.username");
		String password = prop.getProperty("mail.login.password");
		String[] mailsTo = prop.getProperty("mail.to").split(",");

		// message info
		
		int mailsID= mailsTo.length;
		System.out.println(mailsID);
		for (int i = 0; i < mailsTo.length; i++) {
			String[]  mailTo= prop.getProperty("mail.to").split(",");
			System.out.println(mailTo);
		}
		
			 
		
		String subject = "New email with attachments";
		String message = "I have some attachments for you." ;

		// attachments
		String[] attachFiles = new String[1];
		attachFiles[0]=OUTPUT_ZIP_FILE;
		
		
		//attachFiles[0] = "d:/1.png";
		
		if(status.equals("true")){

		try {
			
			sendEmailWithAttachments(host, port, mailFrom, password, mailsTo,
				subject, message, attachFiles,status);
			System.out.println("Email sent.");
		
		} catch (Exception ex) {
			System.out.println("Could not send email.");
			ex.printStackTrace();
		}
		}else{System.out.println("Email Disabled");}
		
		
	}
}