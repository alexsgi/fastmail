# FastMail
![CI](https://github.com/alexsgi/fastmail/actions/workflows/maven.yml/badge.svg)
[![](https://jitpack.io/v/alexsgi/fastmail.svg)](https://jitpack.io/#alexsgi/fastmail)

FastMail - an easy-to-use library for mail communication for Java and Android. All you need is a SMTP server and to import the library.

## 1. Import

Library was uploaded via GitHub. Newest version and instructions are [here](https://github.com/alexsgi/fastmail/packages/).

## 2. Usage

Talk is cheap ... here is the code to send a mail: 

*Recommended config :*
```java
FastMail.init("smtp.example.com", "username", "password");
```
---
Send a mail :
```java
FastMail.sendMail("This is the subject", "This is the content", "to@example.com");
```
Send mail to multiple people ?
```java
FastMail.sendMail("This is the subject", "This is the content", "to@example.com", "tome@example.com", "andme@example.com"); // ...
```
Content should be in HTML ?
```java
FastMail.sendMail("This is still the subject", "<h1>This is a Heading</h1> <p style=\"color=red;\">This is a RED paragraph.</p>", true, "hello@example.com");
```
---
```java 
FastMail.sendMail(String subject, String content, String ... recipients);
```
and
```java 
FastMail.sendMail(String subject, String content, boolean isHtml, String ... recipients);
```
are static and can be called from everywhere. Don't forget to init!

---
#### Some other cool functions
Sometimes sending a mail fails. Why? 
Get a list with all exceptions:
```java
FastMail.getExceptionsList();
```
Last element is the newest exception.

---
Reset the ArrayList (Log) :
```java
FastMail.resetExceptionList();
```

---
Get the initialized host, username and password :
```java
String host = FastMail.getHost();
String username = FastMail.getUsername();
String password = FastMail.getPassword();
```
---
Check if sending email was successful:
```java
boolean sendingSuccessful = FastMail.sendMail("This is the subject", "This is the content", "to@example.com");
```

## 3. Summary
For the lazy ones :

```java
FastMail.init("smtp.example.com", "username", "password");
FastMail.sendMail("Subject", "Content", "recipient@example.com");
```
---

## 4. IMAP
Now we support IMAP !

```java
MailReader mailReader = new MailReader("imap.example.com", "username", "Password");
IMAPObject imapObject = mailReader.getMessages();
if (imapObject.getEmailFolders().size() == 0) {
    System.err.println("No directories found on server!");
    return;
}

ArrayList<EmailFolder> emailFolders = imapObject.getEmailFolders();

for (int i = 0; i < emailFolders.size(); i++) {
    EmailFolder folder = emailFolders.get(i);
    ArrayList<EmailObject> emailObjects = folder.getEmailObjects();
    System.err.println(i + ". " + folder.getFolderName());
	
    for (int j = 0; j < emailObjects.size(); j++) {
        EmailObject emailObject = emailObjects.get(j);
        System.out.println(String.format("\t%d.%d %s", i, j, emailObject.getSubject()));
        System.out.println(String.format("\t\t%s%s%s", "[", emailObject.getContent(), "]"));
        System.out.println(String.format("\t\t%s%s%s %s%s%s", "[", emailObject.getSendDate(), "]", "[", emailObject.getReceivedDate(), "]"));
    }	
}
```
