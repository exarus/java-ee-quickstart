package ua.kpi.ip31.jee.gunawardana.controller;

import lombok.extern.log4j.Log4j2;
import ua.kpi.ip31.jee.gunawardana.model.Comics;
import ua.kpi.ip31.jee.gunawardana.repository.ComicsRepository;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Log4j2
@Model
public class NewComicsForm {
    final FacesContext facesContext;
    final ComicsRepository repository;

    @Produces
    @Named
    final Comics newComics = new Comics();

    @Inject
    public NewComicsForm(FacesContext facesContext, ComicsRepository repository) {
        this.facesContext = facesContext;
        this.repository = repository;
    }

    public void save() {
        try {
            repository.save(newComics);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            log.warn(errorMessage, e);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Registration unsuccessful"));
        }
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }
}