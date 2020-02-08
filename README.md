# FastMail
[![](https://jitpack.io/v/alexsgi/fastmail.svg)](https://jitpack.io/#alexsgi/fastmail)

FastMail - an easy-to-use library for mail communication for Java and Android. All you need is a SMTP server and to import the library.

## 1. Import
**Gradle:**
```gradle
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```
```gradle
dependencies {
	implementation 'com.github.alexsgi:fastmail:1.5'
}
```
**Maven:**
```maven
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
```maven
<dependencies>
	<dependency>
	    <groupId>com.github.alexsgi</groupId>
	    <artifactId>fastmail</artifactId>
	    <version>1.5</version>
	</dependency>
</dependencies>
```
(Instead of "1.5" you can insert every available version → check under "releases").

## 2. Usage

Talk is cheap ... here is the code to send a mail : 

*Recommended config :*
```java
FastMail.init("smtp.example.com", "username", "password");
```
Of course you have to fill in the data of your SMTP server.

---
Send a mail :
```java
FastMail.sendMail("This is the subject", "X-Mailer header", "This is the content", "to@example.com");
```
Send mail to multiple people ?
```java
FastMail.sendMail("This is the subject", "X-Mailer header", "This is the content", "to@example.com", "tome@example.com", "andme@example.com"); // ...
```
Content should be in HTML ?
```java
FastMail.sendMail("This is still the subject", "X-Mailer header", "<h1>This is a Heading</h1> <p style=\"color=red;\">This is a RED paragraph.</p>", true);
```
---
```java 
FastMail.sendMail(String subject, String header, String content, String ... recipients);
```
and
```java 
FastMail.sendMail(String subject, String header, String content, boolean isHtml, String ... recipients);
```
are static and can be called from (almost) everywhere. Don't forget to init !

---
You do not want to init ? There is another option, but I wouldn't recommend it :
```java
FastMail.sendFastMail(String subject, String header, String content, String host, String username, String password, String ... recipients)
```
or
```java
FastMail.sendFastMail(String subject, String header, String content, String host, String username, String password, boolean isHtml, String ... recipients)
```
---
#### Some other cool functions 
```java 
FastMail.init(String host, String username, String password);
```
can be called always.

---
Sometimes sending a mail fails. But why ? 
Get a list with all exceptions :
```java
FastMail.getExceptionsList();
```
returns an ArrayList<Exception\>.

Hopefully I don't have to explain how to get the last element of an ArrayList.

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
Oh, I almost forgot :
How to check if it was successful ?
```java
boolean sendingSuccessful = FastMail.sendMail("This is the subject", "X-Mailer header", "This is the content", "to@example.com");
```
Right, "sendMail" and "sendFastMail" returns a boolean. "True" if the sending was successful.

## 3. Summary
For the lazy ones :

```java
FastMail.init("smtp.example.com", "username", "password");
FastMail.sendMail("Subject", "Header", "Content", "recipient@example.com");
```
---

## 4. IMAP
Now we support IMAP - example, which shows everything:

```java
MailReader mailReader = new MailReader("imap.example.com", "username", "Password");
IMAPObject imapObject = mailReader.getMessages();
if (imapObject.getEmailFolders().size() == 0) {
    System.err.println("No directories !");
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
