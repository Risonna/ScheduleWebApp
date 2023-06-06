package com.risonna.schedulewebapp.validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.util.ResourceBundle;

@FacesValidator("validatePasswordConfirmation")
public class ValidatePasswordConfirmation implements Validator<String> {

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        UIInput passwordField = (UIInput) context.getViewRoot().findComponent("registerForm:password");
        if (passwordField == null)
            throw new IllegalArgumentException(String.format("Unable to find component."));
        String password = (String) passwordField.getValue();

        if (!((String) value).equals(password)) {
            String baseName = "nls.messages"; // Without the "resources" folder and file extension
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, context.getViewRoot().getLocale());
            throw new ValidatorException(new FacesMessage(bundle.getString("password_confirmation_error")));
        }

    }
}
