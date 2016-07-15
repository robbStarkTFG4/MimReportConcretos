/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.session;

import com.mim.models.HistorialDetalles;
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
public class HistorialDetallesFacade extends AbstractFacade<HistorialDetalles> {

    @PersistenceContext(unitName = "envasadoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HistorialDetallesFacade() {
        super(HistorialDetalles.class);
    }

    public List<HistorialDetalles> findAllByOrder(Integer idorden) {
        TypedQuery<HistorialDetalles> query = em.createQuery("SELECT c FROM HistorialDetalles c WHERE c.ordenIdorden.idorden = :id", HistorialDetalles.class);
        query.setParameter("id", idorden);
        return query.getResultList();
    }

}
