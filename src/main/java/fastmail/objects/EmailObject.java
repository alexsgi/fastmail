package fastmail.objects;

import java.util.Date;

public class EmailObject {

    private final String description, subject, content;
    private final Date sendDate, receivedDate;

    private EmailObject(Builder builder) {
        this.description = builder.description;
        this.subject = builder.subject;
        this.content = builder.content;
        this.sendDate = builder.sendDate;
        this.receivedDate = builder.receivedDate;
    }


    public String getDescription() {
        return description;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public static class Builder {

        private String description;
        private String subject;
        private String content;
        private Date sendDate;
        private Date receivedDate;

        public Builder() {
        }

        Builder(String description, String subject, String content, Date sendDate, Date receivedDate) {
            this.description = description;
            this.subject = subject;
            this.content = content;
            this.sendDate = sendDate;
            this.receivedDate = receivedDate;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return Builder.this;
        }

        public Builder setSubject(String subject) {
            this.subject = subject;
            return Builder.this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return Builder.this;
        }

        public Builder setSendDate(Date sendDate) {
            this.sendDate = sendDate;
            return Builder.this;
        }

        public Builder setReceivedDate(Date receivedDate) {
            this.receivedDate = receivedDate;
            return Builder.this;
        }

        public EmailObject build() {
            return new EmailObject(this);
        }
    }

}
