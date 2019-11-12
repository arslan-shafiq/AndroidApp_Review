package de.heuremo.commons.common.model;

public class AuthLoginDTO {

    private final AuthLoginResponeTypeEnum response;
    private final String message;
    private final String encodedToken;

    public AuthLoginDTO(AuthLoginResponeTypeEnum response, String message, String encodedToken) {
        this.response = response;
        this.message = message;
        this.encodedToken = encodedToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public AuthLoginResponeTypeEnum getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public String getEncodedToken() {
        return encodedToken;
    }

    public static final class Builder {
        private AuthLoginResponeTypeEnum response;
        private String message;
        private String encodedToken;

        private Builder() {
        }

        public Builder withResponse(AuthLoginResponeTypeEnum response) {
            this.response = response;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withEncodedToken(String encodedToken) {
            this.encodedToken = encodedToken;
            return this;
        }

        public AuthLoginDTO build() {
            return new AuthLoginDTO(response, message, encodedToken);
        }
    }
}
