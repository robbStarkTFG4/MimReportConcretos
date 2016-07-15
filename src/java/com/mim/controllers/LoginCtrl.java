/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.controllers;

import com.mim.models.Usuario;
import com.mim.session.UsuarioFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author marcoisaac
 */
@Named("login")
@SessionScoped
public class LoginCtrl implements Serializable {

    private String usuario;
    private String password;

    @EJB
    UsuarioFacade userFacade;
    private Usuario user;

    @Inject
    OrdenCtrl ordenCtrl;

    public String executeLogin() {
        if ((usuario.length() > 0) && (password.length() > 0)) {
            //authentificate user
            System.out.println("autentifica usuario");

            user = userFacade.find(usuario, password);
            if (user != null) {
                ordenCtrl.setUser(user);
                ordenCtrl.validateZones();
                return "home.xhtml";
            } else {
                return null;
            }
        } else {
            //notify user about entering their credentials
            System.out.println("clicked");
            return null;
        }
    }

    public String logOut() {
        //System.out.println("cieeeeeeeraaaaa sesioooooon!!!!!");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login.xhtml?faces-redirect=true";
    }

    public void isAdmin() {
        if (user == null) {
            System.out.println("redireccionar");
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("faces/login.xhtml?faces-redirect=true");
            } catch (IOException ex) {
                Logger.getLogger(LoginCtrl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void onIdle() {
        try {
            password = "";
            user = null;
            FacesContext.getCurrentInstance().getExternalContext().redirect("faces/login.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(LoginCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
