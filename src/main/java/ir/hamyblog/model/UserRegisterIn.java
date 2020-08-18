package ir.hamyblog.model;

public class UserRegisterIn {
    private final String username;
    private final String password;
    private final String fullName;

    public UserRegisterIn(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
}
