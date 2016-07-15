/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.util;

import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author marcoisaac
 */
public class MyExceptionHandlerFactory extends ExceptionHandlerFactory {
 
    private ExceptionHandlerFactory parent;
 
    // This injection is handled by JSF
    public MyExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }
 
    @Override
    public MyExceptionHandler getExceptionHandler() {
            return new MyExceptionHandler(parent.getExceptionHandler());
    }
 
}
