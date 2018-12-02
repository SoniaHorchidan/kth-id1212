package common;

import java.io.Serializable;

public interface FileDTO extends Serializable {
    String getName();

    int getSize();

    String getOwner();

    boolean hasReadPermission();

    boolean hasWritePermission();

    String toString();
}
