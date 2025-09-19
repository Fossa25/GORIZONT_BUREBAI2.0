package com.example.proburok;

import java.io.InputStream;

public class TemplateResource {
    private final InputStream inputStream;
    private final String templatePath;

    public TemplateResource(InputStream inputStream, String templatePath) {
        this.inputStream = inputStream;
        this.templatePath = templatePath;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getTemplatePath() {
        return templatePath;
    }
}
