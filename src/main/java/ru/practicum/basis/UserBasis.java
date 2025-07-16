package ru.practicum.basis;

public class UserBasis {
    private String email;
    private String password;
    private String name;

    public UserBasis(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserBasis() {
    }

    public String getUserName() {
        return name;
    }

    public void setUserName (String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return email;
    }

    public void setUserEmail (String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return password;
    }

    public void setUserPassword (String password) {
        this.password = password;
    }
}
