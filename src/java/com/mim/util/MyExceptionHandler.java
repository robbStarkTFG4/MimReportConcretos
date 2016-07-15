/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.util;

import java.util.Iterator;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;

/**
 *
 * @author marcoisaac
 */
public class MyExceptionHandler extends ExceptionHandlerWrapper {
private ExceptionHandler wrapped;
 
   
    FacesContext fc;
 
    public MyExceptionHandler(ExceptionHandler exceptionHandler) {
        wrapped = exceptionHandler;     
        fc = FacesContext.getCurrentInstance();
    }
    @Override
    public javax.faces.context.ExceptionHandler getWrapped() {
      return wrapped;
    }
     @Override
    public void handle() {
 
        Iterator<ExceptionQueuedEvent> i = super.getUnhandledExceptionQueuedEvents().iterator();
 
        while (i.hasNext()) {
 
            Throwable t = i.next().getContext().getException();
 
            if (t instanceof ViewExpiredException) {
                try {
                    // Handle the exception, for example:
                   
                   // fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An exception occurred",
                   //         "ViewExpiredException happened!"));
                    System.out.println("performing forward");
                    fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "login");
                    System.out.println("performing forward");
                    //FacesContext.getCurrentInstance().getExternalContext().
                }
                finally {
                    i.remove();
                }
            }
 
            /*
            else if (t instanceof SomeOtherException) {
                // handle SomeOtherException
            }
            */
 
        }
 
        // let the parent handle the rest
        getWrapped().handle();
    }
}
