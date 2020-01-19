package fastmail;

import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * @author Alexander Sagorski
 * @version 1.0
 */
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

    /**
     * @param host     host of the SMTP server (e.g. smtp.example.com)
     * @param username username for the SMTP server login
     * @param password password for the SMTP server login
     */
    public static void init(String host, String username, String password) {
        HOST = host;
        USERNAME = username;
        PASSWORD = password;
    }

    /**
     * @param subject    subject of the mail
     * @param header     header of the mail; normally not such important
     * @param content    content of the mail; plain text; not HTML
     * @param recipients array of recipients
     * @return returns true if sending successful
     * for this method you need to call init at first
     */
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

    /**
     * @param subject    subject of the mail
     * @param header     header of the mail; normally not such important
     * @param content    content of the mail; plain text; not HTML
     * @param isHtml     if true, content may be HTML code; if false content is plain text
     * @param recipients array of recipients
     * @return returns true if sending successful
     * for this method you need to call init at first
     */
    public static boolean sendMail(String subject, String header, String content, boolean isHtml, String... recipients) {
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
            if (isHtml) {
                msg.setContent(content, "text/html");
            } else {
                msg.setText(content);
            }
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

    /**
     * @param subject    subject of the mail
     * @param header     header of the mail; normally not such important
     * @param content    content of the mail; plain text; not HTML
     * @param host       host of SMTP server
     * @param username   username for SMTP login
     * @param password   password for SMTP login
     * @param recipients array of recipients
     * @return returns true if sending successful
     * for this method you needn't to call init
     */
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

    /**
     * @param subject    subject of the mail
     * @param header     header of the mail; normally not such important
     * @param content    content of the mail; plain text; not HTML
     * @param host       host of SMTP server
     * @param username   username for SMTP login
     * @param password   password for SMTP login
     * @param isHtml     if true, content may be HTML code; if false content is plain text
     * @param recipients array of recipients
     * @return returns true if sending successful
     * for this method you needn't to call init
     */
    public static boolean sendFastMail(String subject, String header, String content, String host, String username, String password, boolean isHtml, String... recipients) {
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
            if (isHtml) {
                msg.setContent(content, "text/html");
            } else {
                msg.setText(content);
            }
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

    /**
     * @param recipients array with all recipients
     * @return returns a String containing all recipients
     */
    private static String formatRecipients(String[] recipients) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : recipients) {
            stringBuilder.append(str.trim()).append(",");
        }
        return stringBuilder.toString();
    }

    /**
     * @return returns true if all credentials are available (otherwise call init())
     */
    private static boolean checkCredentials() {
        return checkChosenCredentials(HOST, USERNAME, PASSWORD);
    }

    /**
     * @param host     host of SMTP server
     * @param username username for SMTP login
     * @param password password for SMTP login
     * @return returns true if credentials for SMTP login are okay
     */
    private static boolean checkChosenCredentials(String host, String username, String password) {
        return host != null && !host.replace(" ", "").isEmpty() && username != null && !username.replace(" ", "").isEmpty() && password != null && !password.replace(" ", "").isEmpty();
    }

    /**
     * @return returns the definded SMTP host via FastMail.init()
     */
    public static String getHost() {
        return HOST;
    }

    /**
     * @return returns the definded SMTP username via FastMail.init()
     */
    public static String getUsername() {
        return USERNAME;
    }

    /**
     * @return returns the definded SMTP password via FastMail.init()
     */
    public static String getPassword() {
        return PASSWORD;
    }

    /**
     * resets the ArrayList with all exceptions
     */
    public static void resetExceptionList() {
        exceptionsList = new ArrayList<>();
    }

    /**
     * @return returns the ArrayList with all exceptions
     * (important e.g. when sending mail fails)
     */
    public static ArrayList<Exception> getExceptionsList() {
        return exceptionsList;
    }

}
