package com.risonna.schedulewebapp.validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.util.ResourceBundle;

@FacesValidator("validatePassword")
public class validatePassword implements Validator<String> {
    private static final int MINIMUM_LENGTH = 7;
    private static final int MAXIMUM_LENGTH = 25;
    private static final String PATTERN = "[a-zA-Z0-9]*";

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        String baseName = "nls.messages"; // Without the "resources" folder and file extension
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());

        if (value == null || value.length() < MINIMUM_LENGTH) {
            throw new ValidatorException(new FacesMessage(bundle.getString("password_length_min")));
        }

        if (value.length() > MAXIMUM_LENGTH) {
            throw new ValidatorException(new FacesMessage(bundle.getString("password_length_max")));
        }

        if (!value.matches(PATTERN)) {
            throw new ValidatorException(new FacesMessage(bundle.getString("password_regex")));
        }

        if (!isPasswordStrong(value)) {
            throw new ValidatorException(new FacesMessage(bundle.getString("password_weak")));
        }
    }

    private boolean isPasswordStrong(String password) {
        // Implement your logic to check if the password is strong enough
        // Return true if it is strong, false otherwise
        // You can define your own criteria for password complexity, such as requiring uppercase letters, numbers, symbols, etc.
        // Here's a simple example to check if the password contains at least one uppercase letter and one digit:
        return password.matches(".*[A-Z].*") && password.matches(".*\\d.*");
    }
}
