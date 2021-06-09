package fastmail.objects;

import java.util.ArrayList;

public class IMAPObject {

    private final String host;
    private final ArrayList<EmailFolder> emailFolders;

    public IMAPObject(String host, ArrayList<EmailFolder> list) {
        this.emailFolders = list;
        this.host = host;
    }

    public ArrayList<EmailFolder> getEmailFolders() {
        return emailFolders;
    }

    public String getHost() {
        return host;
    }

}
