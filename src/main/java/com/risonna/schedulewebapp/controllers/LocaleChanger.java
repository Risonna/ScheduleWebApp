package com.risonna.schedulewebapp.controllers;

import jakarta.enterprise.context.SessionScoped;
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
    private List<String>  supportedLocales;

    public void setSupportedLocales(List<String> supportedLocales) {
        this.supportedLocales = supportedLocales;
    }

    public List<String> getSupportedLocales() {
        List<String> listOfLocales = Arrays.asList("en", "ru");
        setSupportedLocales(listOfLocales);
        return supportedLocales;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public LocaleChanger(){

    }


    private Locale currentLocale;

    public void changeLocale(String localeCode) {
        currentLocale = new Locale(localeCode);

    }

    public Locale getCurrentLocale() {


        if (currentLocale == null) {
            currentLocale = new Locale("ru");// по-умолчанию ставим русский язык
        }

        return currentLocale;

    }
}