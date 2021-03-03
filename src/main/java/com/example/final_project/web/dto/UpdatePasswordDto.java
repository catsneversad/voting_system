package com.example.final_project.web.dto;

public class UpdatePasswordDto {
    private String userEmail;
    private String currentPassword;
    private String newPassword;
    private String repeatNewPassword;

    public UpdatePasswordDto() {
    }

    public UpdatePasswordDto(String email, String currentPassword, String newPassword, String repeatNewPassword) {
        super();
        this.userEmail = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.repeatNewPassword = repeatNewPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    @Override
    public String toString() {
        return "UpdatePasswordDto{" +
                "currentPassword='" + currentPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", repeatNewPassword='" + repeatNewPassword + '\'' +
                '}';
    }
}
