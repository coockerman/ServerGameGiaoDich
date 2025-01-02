package ServerNew.Model;

public class PasswordReset {
    private String username;
    private String passwordOld;
    private String passwordNew;

    public PasswordReset(String username, String passwordOld, String passwordNew) {
        this.username = username;
        this.passwordOld = passwordOld;
        this.passwordNew = passwordNew;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld) {
        this.passwordOld = passwordOld;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }
}
