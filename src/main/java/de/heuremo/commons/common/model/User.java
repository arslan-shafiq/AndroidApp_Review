package de.heuremo.commons.common.model;

public class User {

    private final String username;

    private final String password;

    private String token;

    private User(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static final class Builder {
        private String username;
        private String password;
        private String token;

        private Builder() {
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        public User build() {
            return new User(username, password, token);
        }
    }
}
