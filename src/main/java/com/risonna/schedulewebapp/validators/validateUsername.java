package com.risonna.schedulewebapp.validators;

import com.risonna.schedulewebapp.controllers.usernamesChecker;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FacesValidator("validateUsername")
public class validateUsername implements Validator<String> {
    private static final int MINIMUM_LENGTH = 6;
    private static final int MAXIMUM_LENGTH = 20;
    private static final String PATTERN = "[a-zA-Z0-9]*";

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        String baseName = "nls.messages"; // Without the "resources" folder and file extension
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());

        if (value == null || value.length() < MINIMUM_LENGTH) {
            throw new ValidatorException(new FacesMessage(bundle.getString("username_length_min")));
        }

        if (value.length() > MAXIMUM_LENGTH) {
            throw new ValidatorException(new FacesMessage(bundle.getString("username_length_max")));
        }

        if (!value.matches(PATTERN)) {
            throw new ValidatorException(new FacesMessage(bundle.getString("username_regex")));
        }

        // Check if the username already exists (replace this with your own logic)
        if (isUsernameAlreadyExists(value)) {
            throw new ValidatorException(new FacesMessage(bundle.getString("username_exists")));
        }
    }

    private boolean isUsernameAlreadyExists(String username) {
        // Implement your logic to check if the username already exists in the system
        // Return true if it exists, false otherwise
        usernamesChecker usernames = new usernamesChecker();
        List<String> users = usernames.getUsernames();
        usernames = null;
        return users.contains(username);// Replace this with your own implementation
    }
}
