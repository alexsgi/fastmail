package fastmail;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class FastMail {

    private static String HOST = null;
    private static String USERNAME = null;
    private static String PASSWORD = null;

    private static ArrayList<Exception> exceptionsList = new ArrayList<>();

    private static final String PROTOCOL = "smtps";
    private static final String PROPERTY_HOST = "mail.smtps.host";
    private static final String PROPERTY_AUTH = "mail.smtps.auth";
    private static final String PROPERTY_HEADER = "X-Mailer";
    private static final boolean ENABLE_AUTH = true;
    private static final String SUCCESS = "250 Requested mail action okay";

    public static void init(String host, String username, String password) {
        HOST = host;
        USERNAME = username;
        PASSWORD = password;
    }

    public static boolean sendMail(String subject, String header, String content, String... recipients) {
        try {
            if (!checkCredentials())
                throw new Exception("Credentials missing or == null. Don't forget calling FastMail.init();");
            Properties props = System.getProperties();
            props.put(PROPERTY_HOST, HOST);
            props.put(PROPERTY_AUTH, ENABLE_AUTH);
            Session session = Session.getInstance(props, null);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(USERNAME));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(formatRecipients(recipients), false));
            msg.setSubject(subject);
            msg.setText(content);
            msg.setHeader(PROPERTY_HEADER, header);
            msg.setSentDate(new Date());
            SMTPTransport t = (SMTPTransport) session.getTransport(PROTOCOL);
            t.connect(HOST, USERNAME, PASSWORD);
            t.sendMessage(msg, msg.getAllRecipients());
            String serverResponse = t.getLastServerResponse();
            t.close();
            return serverResponse != null && serverResponse.trim().startsWith(SUCCESS);
        } catch (Exception e) {
            exceptionsList.add(e);
            return false;
        }
    }

    public static boolean sendFastMail(String subject, String header, String content, String host, String username, String password, String... recipients) {
        try {
            if (checkChosenCredentials(host, username, password))
                throw new Exception("Credentials missing or == null.");
            Properties props = System.getProperties();
            props.put(PROPERTY_HOST, host);
            props.put(PROPERTY_AUTH, ENABLE_AUTH);
            Session session = Session.getInstance(props, null);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(formatRecipients(recipients), false));
            msg.setSubject(subject);
            msg.setText(content);
            msg.setHeader(PROPERTY_HEADER, header);
            msg.setSentDate(new Date());
            SMTPTransport t = (SMTPTransport) session.getTransport(PROTOCOL);
            t.connect(host, username, password);
            t.sendMessage(msg, msg.getAllRecipients());
            String serverResponse = t.getLastServerResponse();
            t.close();
            return serverResponse != null && serverResponse.trim().startsWith(SUCCESS);
        } catch (Exception e) {
            exceptionsList.add(e);
            return false;
        }
    }

    public static void resetExceptionList() {
        exceptionsList = new ArrayList<>();
    }

    public static ArrayList<Exception> getExceptionsList() {
        return exceptionsList;
    }

    private static String formatRecipients(String[] recipients) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : recipients) {
            stringBuilder.append(str.trim()).append(",");
        }
        return stringBuilder.toString();
    }

    private static boolean checkCredentials() {
        return checkChosenCredentials(HOST, USERNAME, PASSWORD);
    }

    private static boolean checkChosenCredentials(String host, String username, String password) {
        return host != null && !host.trim().isEmpty() && username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty();
    }

    public static String getHost() {
        return HOST;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }

}
