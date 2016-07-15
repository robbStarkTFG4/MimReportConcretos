/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.session;

import com.mim.models.Usuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author marcoisaac
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "envasadoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario find(String usuario, String password) {
        TypedQuery<Usuario> query = em.createQuery("SELECT c FROM Usuario c WHERE c.usuario = :usr AND c.contrasena = :pass", Usuario.class);
        query.setParameter("usr", usuario);
        query.setParameter("pass", password);
        try {
            Usuario user = query.getSingleResult();
            return user;
        } catch (NoResultException e) {
            return null;
        }

    }

}
