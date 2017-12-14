package ap.student.outlook_mobile_app.mailing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable{

    private int autoId;
    private String id;
    private String receivedDateTime;
    private Recipient from;
    private ArrayList<Recipient> toRecipients;
    private ArrayList<Recipient> ccRecipients;
    private String subject;
    private String bodyPreview;
    private String hasAttachments;
    private String isRead;
    private Body body;
    private String importance;
    private int color = -1;

    public Message() {
        autoId++;
    }

    public int getAutoId() {
        return autoId;
    }

    public void setAutoId(int autoId) {
        this.autoId = autoId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(String receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }

    public Recipient getFrom() {
        return from;
    }

    public void setFrom(Recipient from) {
        this.from = from;
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

    public String getHasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(String hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public ArrayList<Recipient> getToRecipients() {
        return toRecipients;
    }

    public void setToRecipients(ArrayList<Recipient> toRecipients) {
        this.toRecipients = toRecipients;
    }

    public ArrayList<Recipient> getCcRecipients() {
        return ccRecipients;
    }

    public void setCcRecipients(ArrayList<Recipient> ccRecipients) {
        this.ccRecipients = ccRecipients;
    }
}
