package com.ya;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierCredentials {

    private String login;
    private String password;

    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
