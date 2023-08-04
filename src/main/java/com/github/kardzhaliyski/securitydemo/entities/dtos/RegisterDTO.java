package com.github.kardzhaliyski.securitydemo.entities.dtos;

import java.util.regex.Pattern;

public record RegisterDTO(String name, String email, String password, String passwordRepeat) {
    private static final Pattern EMAIL_MATCHER = Pattern.compile("\\b[A-Za-z0-9][A-Za-z0-9\\-._]+[A-Za-z0-9]@[A-Za-z][A-Za-z-]*[A-Za-z](\\.[a-zA-Z]+){1,5}\\b");
    public boolean isValid() {
        return nameIsValid() && emailIsValid() && passwordIsValid() && password.equals(passwordRepeat);
    }

    private boolean nameIsValid() {
        return this.name != null &&
               this.name.trim().equals(this.name) &&
               this.name.length() > 3;
    }

    private boolean emailIsValid() {
        return this.email != null &&
               this.email.trim().equals(this.email) &&
               EMAIL_MATCHER.matcher(email).matches();
    }

    private boolean passwordIsValid() {
        return this.password != null &&
               this.password.length() >= 8;
    }
}
