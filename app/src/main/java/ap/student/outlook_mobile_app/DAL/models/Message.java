package ap.student.outlook_mobile_app.DAL.models;

import java.util.Date;
import java.util.List;

/**
 * Created by Gilles on 27-11-2017.
 */

public class Message {

    private String id;
    private Date receivedDateTime;
    private Recipient from;
    private Boolean isRead;
    private String subject;
    private String bodyPreview;
    private Body body;
    private List<Recipient> toRecipients;
    private List<Recipient> ccRecipients;
    private List<Recipient> bccRecipients;

    public Message() {
    }

    public Message(String subject, Body body, List<Recipient> toRecipients) {
        this.subject = subject;
        this.body = body;
        this.toRecipients = toRecipients;
    }

    public Message(String subject, Body body, List<Recipient> toRecipients, List<Recipient> ccRecipients) {
        this.subject = subject;
        this.body = body;
        this.toRecipients = toRecipients;
        this.ccRecipients = ccRecipients;
    }

    public Message(Body body, List<Recipient> toRecipients, List<Recipient> bccRecipients, String subject) {
        this.body = body;
        this.toRecipients = toRecipients;
        this.bccRecipients = bccRecipients;
        this.subject = subject;

    }

    public Message(String subject, Body body, List<Recipient> toRecipients, List<Recipient> ccRecipients, List<Recipient> bccRecipients) {
        this.subject = subject;
        this.body = body;
        this.toRecipients = toRecipients;
        this.ccRecipients = ccRecipients;
        this.bccRecipients = bccRecipients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(Date receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }

    public Recipient getFrom() {
        return from;
    }

    public void setFrom(Recipient from) {
        this.from = from;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyPreview() {
        return bodyPreview;
    }

    public void setBodyPreview(String bodyPreview) {
        this.bodyPreview = bodyPreview;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public List<Recipient> getToRecipients() {
        return toRecipients;
    }

    public void setToRecipients(List<Recipient> toRecipients) {
        this.toRecipients = toRecipients;
    }

    public List<Recipient> getCcRecipients() {
        return ccRecipients;
    }

    public void setCcRecipients(List<Recipient> ccRecipients) {
        this.ccRecipients = ccRecipients;
    }

    public List<Recipient> getBccRecipients() {
        return bccRecipients;
    }

    public void setBccRecipients(List<Recipient> bccRecipients) {
        this.bccRecipients = bccRecipients;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", receivedDateTime=" + receivedDateTime +
                ", from=" + from +
                ", isRead=" + isRead +
                ", subject='" + subject + '\'' +
                ", bodyPreview='" + bodyPreview + '\'' +
                ", body=" + body +
                ", toRecipients=" + toRecipients +
                ", ccRecipients=" + ccRecipients +
                ", bccRecipients=" + bccRecipients +
                '}';
    }
}
