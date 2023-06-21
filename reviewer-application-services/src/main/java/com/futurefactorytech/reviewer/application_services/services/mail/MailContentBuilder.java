package com.futurefactorytech.reviewer.application_services.services.mail;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Named
public class MailContentBuilder {

    private static final String SIMPLE_TEMPLATE_MESSAGE = "message";
    private final SpringTemplateEngine springTemplateEngine;

    @Inject
    public MailContentBuilder(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    public String buildSimpleTemplateMessage(String templateName, String message) {
        Context context = new Context();
        context.setVariable(SIMPLE_TEMPLATE_MESSAGE, message);
        return springTemplateEngine.process(templateName, context);
    }

    public String buildComplexTemplateMessage(String templateName, Map<String, Object> messages) {
        Context context = new Context();
        context.setVariables(messages);
        return springTemplateEngine.process(templateName, context);
    }
}
