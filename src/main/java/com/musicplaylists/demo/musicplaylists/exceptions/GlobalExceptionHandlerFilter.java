package com.musicplaylists.demo.musicplaylists.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.musicplaylists.demo.musicplaylists.services.ProfileUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Component
public class GlobalExceptionHandlerFilter {

    @Autowired
    private ProfileUtils profileUtils;

    public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException, ServletException {
        if (e instanceof ServletException) {
            handleServletException(request, response, (ServletException) e);
        } else {
            handleOtherException(request, response, e);
        }
    }

    private void handleServletException(HttpServletRequest request, HttpServletResponse response, ServletException e) throws IOException, ServletException {
        Throwable rootCause = e.getRootCause();
        if (rootCause instanceof IllegalArgumentException) {
            handleException(response, rootCause, HttpServletResponse.SC_BAD_REQUEST);
        } else if (rootCause instanceof HttpClientErrorException.Unauthorized || rootCause instanceof BadCredentialsException) {
            handleException(response, rootCause, HttpServletResponse.SC_UNAUTHORIZED);
        } else if (rootCause instanceof IllegalStateException) {
            handleException(response, rootCause, HttpServletResponse.SC_BAD_REQUEST);
        } else {
            throw e;
        }
    }

    private void handleException(HttpServletResponse response, Throwable cause, int statusCode) throws IOException {
        response.setStatus(statusCode);
        if (profileUtils.isLocalProfile()) {
            // If the active profile is "local", write the stack trace
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            cause.printStackTrace(pw);
            response.getWriter().write(sw.toString());
        } else {
            // Otherwise, write only the message
            response.getWriter().write(cause.getMessage());
        }
    }

    private void handleOtherException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Internal Server Error: " + e.getMessage());
    }
}
