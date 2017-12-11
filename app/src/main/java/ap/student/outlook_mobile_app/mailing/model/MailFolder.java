package ap.student.outlook_mobile_app.mailing.model;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Gilles on 10/12/2017.
 */

public class MailFolder implements Serializable {
    private String id;
    private String displayName;
    private int unreadItemCount;
    private int totalItemCount;

    public MailFolder() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getUnreadItemCount() {
        return unreadItemCount;
    }

    public void setUnreadItemCount(int unreadItemCount) {
        this.unreadItemCount = unreadItemCount;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
