package ap.student.outlook_mobile_app.DAL.models;

/**
 * Created by alek on 11/30/17.
 */

public class Calendar {
    private boolean canEdit;
    private boolean canShare;
    private boolean canViewPrivateItems;
    private String changeKey;
    private String color;
    private String id;
    private String name;
    private String owner;

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanShare() {
        return canShare;
    }

    public void setCanShare(boolean canShare) {
        this.canShare = canShare;
    }

    public boolean isCanViewPrivateItems() {
        return canViewPrivateItems;
    }

    public void setCanViewPrivateItems(boolean canViewPrivateItems) {
        this.canViewPrivateItems = canViewPrivateItems;
    }

    public String getChangeKey() {
        return changeKey;
    }

    public void setChangeKey(String changeKey) {
        this.changeKey = changeKey;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
