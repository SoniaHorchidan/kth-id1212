package se.kth.id1212.project.sonia.restful_news_feed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import se.kth.id1212.project.sonia.restful_news_feed.controller.exceptions.*;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * handler for custom exceptions
     */
    @ExceptionHandler({UnauthorizedException.class, RegistrationFailedException.class, ResourceNotFoundException.class,
            InsertFailedException.class, UpdateFailedException.class, DeleteFailedException.class})
    public ModelAndView handle(HttpServletRequest req, CustomException ex){
        ModelAndView model = new ModelAndView();
        model.addObject("errorMessage", ex.getMessage());
        model.setViewName("error");
        model.setStatus(ex.getStatus());
        return model;
    }

    /**
     * handlers for default exceptions
     */
    @ExceptionHandler({MissingServletRequestParameterException.class, BindException.class})
    public ModelAndView handleMissingInput(HttpServletRequest req, Exception ex){
        ModelAndView model = new ModelAndView();
        model.addObject("errorMessage", "Some fields are missing. Please enter the requested information.");
        model.setViewName("error");
        model.setStatus(HttpStatus.BAD_REQUEST);
        return model;
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ModelAndView handleUnsupportedHttpRequests(HttpServletRequest req, Exception ex){
        ModelAndView model = new ModelAndView();
        model.addObject("errorMessage", "Request not allowed");
        model.setViewName("error");
        model.setStatus(HttpStatus.BAD_REQUEST);
        return model;
    }

    /**
     * handler for unknown errors
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleUnknownErrors(HttpServletRequest request, Exception exception)   {
        ModelAndView model = new ModelAndView();
        model.addObject("errorMessage", "Internal server error.");
        model.setViewName("error");
        model.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return model;
    }
}
