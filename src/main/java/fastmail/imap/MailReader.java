package fastmail.imap;

import com.sun.mail.imap.IMAPFolder;
import fastmail.objects.EmailFolder;
import fastmail.objects.EmailObject;
import fastmail.objects.IMAPObject;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class MailReader {

    private static final String PROTOCOL = "imaps";
    private static final String MAIL_STORE_PROTOCOL = "mail.store.protocol";
    private String HOST;
    private String USERNAME;
    private String PASSWORD;

    public MailReader(String host, String username, String password) {
        this.HOST = host;
        this.USERNAME = username;
        this.PASSWORD = password;
    }

    private static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(html);
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

    public String getHost() {
        return HOST;
    }

    public String getUsername() {
        return USERNAME;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public IMAPObject getMessages() throws MessagingException, IOException {
        ArrayList<EmailFolder> emailFolderArrayList;
        Properties props = System.getProperties();
        props.setProperty(MAIL_STORE_PROTOCOL, PROTOCOL);
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore(PROTOCOL);
        store.connect(HOST, USERNAME, PASSWORD);
        Folder[] folders = store.getDefaultFolder().list();
        emailFolderArrayList = readAllFolders(folders, store);
        store.close();
        return new IMAPObject(HOST, emailFolderArrayList);
    }

    private ArrayList<EmailFolder> readAllFolders(Folder[] folders, Store store) throws MessagingException, IOException {
        if (folders == null) return null;
        ArrayList<EmailFolder> emailFolderArrayList = new ArrayList<>();
        for (Folder value : folders) {
            ArrayList<EmailObject> messageArrayList = new ArrayList<>();
            String folderName = value.getName();
            IMAPFolder folder = (IMAPFolder) store.getFolder(folderName);
            if (!folder.isOpen()) folder.open(Folder.READ_ONLY);
            long largestUid = folder.getUIDNext() - 1;
            int chunkSize = 500;
            for (long offset = 0; offset < largestUid; offset += chunkSize) {
                long start = Math.max(1, largestUid - offset - chunkSize + 1);
                long end = Math.max(1, largestUid - offset);
                Message[] messages = folder.getMessagesByUID(start, end);
                if (messages.length == 0) break;
                FetchProfile metadataProfile = new FetchProfile();
                metadataProfile.add(FetchProfile.Item.FLAGS);
                metadataProfile.add(FetchProfile.Item.ENVELOPE);
                folder.fetch(messages, metadataProfile);
                for (int i = messages.length - 1; i >= 0; i--) {
                    Message message = messages[i];
                    messageArrayList.add(new EmailObject.Builder()
                            .setContent(getTextFromMessage(message).trim())
                            .setSendDate(message.getSentDate())
                            .setReceivedDate(message.getReceivedDate())
                            .setDescription(message.getDescription())
                            .setSubject(message.getSubject())
                            .build()
                    );
                }
            }
            emailFolderArrayList.add(new EmailFolder(folderName, messageArrayList));
        }
        return emailFolderArrayList;
    }

}