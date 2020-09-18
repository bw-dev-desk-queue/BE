package com.lambdaschool.devdesk.queue.exceptions;

import com.lambdaschool.devdesk.queue.services.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomErrorDetails extends DefaultErrorAttributes {

    @Autowired
    HelperFunctions helperFunctions;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        var errorAttribs = super.getErrorAttributes(webRequest, includeStackTrace);


        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("title", errorAttribs.get("error"));
        errorDetails.put("status", errorAttribs.get("status"));
        errorDetails.put("detail", errorAttribs.get("message"));
        errorDetails.put("timestamp", errorAttribs.get("timestamp"));
        errorDetails.put("developerMessage", String.format("Path: %s", errorAttribs.get("path")));
        errorDetails.put("errors", helperFunctions.getConstraintViolation(this.getError(webRequest)));

        return errorDetails;
    }
}
