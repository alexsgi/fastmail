package fastmail.objects;

import java.util.ArrayList;

public class IMAPObject {

    private String host;
    private ArrayList<EmailFolder> emailFolders;

    public ArrayList<EmailFolder> getEmailFolders() {
        return emailFolders;
    }

    public String getHost() {
        return host;
    }

    public IMAPObject(String host, ArrayList<EmailFolder> list) {
        this.emailFolders = list;
        this.host = host;
    }

}
