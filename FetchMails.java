package com.example.MailEx;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.SearchTerm;

public class FetchMails {

	public static void main(String[] args) {

		String user = "naveenchinna077@gmail.com";
		String password = "pvzj goms ninm gkpy";

		Scanner s = new Scanner(System.in);

		System.out.println("Enter a date in the given format (yyyy-MM-dd): ");
		String date = s.next();

		Properties props = new Properties();
		props.put("mail.store.protocol", "imaps");
		props.put("mail.imaps.ssl.enable", true);
		props.put("mail.imaps.host", "imap.gmail.com");
		props.put("mail.imaps.port", "993");

		try {
			Session session = Session.getInstance(props);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", user, password);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date targetDate = sdf.parse(date);
			SearchTerm search = new ReceivedDateTerm(ComparisonTerm.EQ, targetDate);

			System.out.println("Received Emails:");
			fetchEmails(store, "INBOX", search);

			System.out.println("Sent Emails:");
			fetchEmails(store, "[Gmail]/Sent Mail", search);

			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
	}

	private static void fetchEmails(Store store, String folderName, SearchTerm searchTerm) throws MessagingException {
		Folder folder = store.getFolder(folderName);

		if (!folder.exists()) {
			System.out.println("Folder " + folderName + " does not exist.");
			return;
		}

		folder.open(Folder.READ_ONLY);
		Message[] messages = folder.search(searchTerm);

		System.out.println("Emails in " + folderName + " on given date: " + messages.length);
		for (Message message : messages) {
			try {
				System.out.println("------------------------------------------------------------");
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + Arrays.toString(message.getFrom()));
				System.out.println("To: " + Arrays.toString(message.getAllRecipients()));
				System.out.println("Received Date: " + message.getReceivedDate());

				String content = getTextFromMessage(message);
				System.out.println("Message: " + content);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		folder.close();
	}
	

	private static String getTextFromMessage(Message message) throws Exception {
		if (message.isMimeType("text/plain")) {
			return message.getContent().toString();
		} else if (message.isMimeType("multipart/*")) {
			return getTextFromMimeMultipart((MimeMultipart) message.getContent());
		}
		return "[No Plain Text Found]";
	}

	private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < mimeMultipart.getCount(); i++) {
			Part part = mimeMultipart.getBodyPart(i);
			if (part.isMimeType("text/plain")) {
				result.append(part.getContent().toString());
			} else if (part.isMimeType("multipart/*")) {
				result.append(getTextFromMimeMultipart((MimeMultipart) part.getContent()));
			}
		}
		return result.toString().trim();
	}
}