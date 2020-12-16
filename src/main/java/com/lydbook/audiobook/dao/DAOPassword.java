package com.lydbook.audiobook.dao;

/**
 * Password.
 */
public class DAOPassword {

    private String currentPassword;
    private String newPassword;

    /**
     * Gets current password.
     *
     * @return the current password
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * Sets current password.
     *
     * @param currentPassword the current password
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * Gets new password.
     *
     * @return the new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets new password.
     *
     * @param newPassword the new password
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
