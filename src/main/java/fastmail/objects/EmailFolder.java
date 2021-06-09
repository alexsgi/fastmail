package fastmail.objects;

import java.util.ArrayList;

public class EmailFolder {

    private final String folderName;
    private final ArrayList<EmailObject> emailObjects;

    public EmailFolder(String folderName, ArrayList<EmailObject> list) {
        this.folderName = folderName;
        this.emailObjects = list;
    }

    public String getFolderName() {
        return folderName;
    }

    public ArrayList<EmailObject> getEmailObjects() {
        return emailObjects;
    }
}
