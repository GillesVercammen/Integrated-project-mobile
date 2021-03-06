package ap.student.outlook_mobile_app.DAL.models;

/**
 * Created by alek on 12/1/17.
 */

public class Body {
    private String content;
    private String contentType;

    public Body() {
    }

    public Body(String content, String contentType) {
        this.content = content;
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "Body{" +
                "content='" + content + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
