package server.model;

import common.FileDTO;

public class File implements FileDTO {
    private String name;
    private String owner;
    private int size;
    private boolean writePermission;
    private boolean readPermission;

    public File(String name, String owner, int size, boolean writePermission, boolean readPermission) {
        this.owner = owner;
        this.name = name;
        this.writePermission = writePermission;
        this.readPermission = readPermission;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public String getOwner() {
        return owner;
    }

    public boolean hasReadPermission() {
        return readPermission;
    }

    public boolean hasWritePermission() {
        return writePermission;
    }

    public String toString() {
        return "Name: " + name +
                " \nOwner: " + owner +
                "  |  Size: " + size +
                "  |  Read permission: " + readPermission +
                "  |  Write permission: " + writePermission;
    }

    public void setWritePermission(boolean writePermission) {
        this.writePermission = writePermission;
    }

    public void setReadPermission(boolean readPermission) {
        this.readPermission = readPermission;
    }
}
