package fastmail.objects;

import java.util.ArrayList;

public class EmailFolder {

    private String folderName;
    private ArrayList<EmailObject> emailObjects;

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
