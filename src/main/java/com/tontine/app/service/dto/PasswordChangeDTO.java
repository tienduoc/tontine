package com.tontine.app.service.dto;

import java.io.Serializable;

public class PasswordChangeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String currentPassword;
    private String newPassword;
    public PasswordChangeDTO() {}
    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
    public String getCurrentPassword() {
        return currentPassword;
    }
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
