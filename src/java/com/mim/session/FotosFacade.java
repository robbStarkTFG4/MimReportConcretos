/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.session;

import com.mim.models.Fotos;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author marcoisaac
 */
@Stateless
public class FotosFacade extends AbstractFacade<Fotos> {

    @PersistenceContext(unitName = "envasadoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FotosFacade() {
        super(Fotos.class);
    }

    public List<Fotos> findAllByOrder(Integer idorden) {
        TypedQuery<Fotos> query = em.createQuery("SELECT c FROM Fotos c WHERE c.ordenIdorden.idorden = :id", Fotos.class);
        query.setParameter("id", idorden);
        return query.getResultList();
    }

}
