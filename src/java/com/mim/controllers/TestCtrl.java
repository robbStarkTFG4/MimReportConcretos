/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.controllers;

import java.io.Serializable;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author marcoisaac
 */
@Named("test")
@ViewScoped
public class TestCtrl implements Serializable {

    String route = "http://cemex-5266592.jl.serv.net.mx/rest/webresources/videoGeneric/video/MOV_008.mp4";

    public String getName() {
        final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, String> parameterMap = (Map<String, String>) externalContext.getRequestParameterMap();
        String param = parameterMap.get("paramName");
        if (param != null) {
            return param;
        }

        

        return route;
    }
}
