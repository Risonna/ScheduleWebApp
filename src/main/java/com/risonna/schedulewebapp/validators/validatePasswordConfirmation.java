package com.risonna.schedulewebapp.validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.util.ResourceBundle;

@FacesValidator("validatePasswordConfirmation")
public class validatePasswordConfirmation implements Validator<String> {

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        String password = (String) component.getAttributes().get("password");

        if (password == null || !password.equals(value)) {
            String baseName = "nls.messages"; // Without the "resources" folder and file extension
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());
            throw new ValidatorException(new FacesMessage(bundle.getString("password_confirmation_error")));
        }
    }
}
