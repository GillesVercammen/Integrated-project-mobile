package ap.student.outlook_mobile_app.mailing.model;

public class Attachment {

    private String id;
    private String name;
    private String contentType;
    private int size;
    private String contentBytes;

    public Attachment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(String contentBytes) {
        this.contentBytes = contentBytes;
    }
}
