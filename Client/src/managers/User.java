package managers;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private boolean signIn;

    private int id;

    public User(String username, String password, boolean signIn){
        this.username = username;
        this.password = password;
        this.signIn = signIn;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSignIn() {
        return signIn;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
