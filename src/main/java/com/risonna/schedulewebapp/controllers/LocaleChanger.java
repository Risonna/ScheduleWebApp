package com.risonna.schedulewebapp.controllers;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Named
@SessionScoped
public class LocaleChanger implements Serializable {
    private String selectedLanguage;
    private List<String> supportedLocales;

    public void setSupportedLocales(List<String> supportedLocales) {
        this.supportedLocales = supportedLocales;
    }

    public List<String> getSupportedLocales() {
        List<String> listOfLocales = Arrays.asList("ru", "en");
        setSupportedLocales(listOfLocales);
        return supportedLocales;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public void changeLocale(String localeCode) {
        currentLocale = new Locale(localeCode);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(currentLocale);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("locale", localeCode);
    }

    private Locale currentLocale;

    public Locale getCurrentLocale() {
        if (currentLocale == null) {
            currentLocale = new Locale("ru"); // Default locale
            Object storedLocale = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("locale");
            if (storedLocale != null) {
                currentLocale = new Locale(storedLocale.toString());
            }
        }
        return currentLocale;
    }
}
