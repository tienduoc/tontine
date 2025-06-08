package com.tontine.app.service.dto;

import com.tontine.app.domain.User;
import java.io.Serializable;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String login;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "id='" + id + '\'' + ", login='" + login + '\'' + "}";
    }
}
