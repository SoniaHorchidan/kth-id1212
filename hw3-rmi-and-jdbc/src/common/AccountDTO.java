package common;

import java.io.Serializable;

public interface AccountDTO extends Serializable {
    String getUsername();

    String getPassword();
}
