/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mim.session;

import com.mim.models.Orden;
import com.mim.models.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author marcoisaac
 */
@Stateless
public class OrdenFacade extends AbstractFacade<Orden> {

    @PersistenceContext(unitName = "envasadoPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrdenFacade() {
        super(Orden.class);
    }

    public List<Orden> findAll(String linea) {
        TypedQuery<Orden> query = em.createQuery("SELECT c FROM Orden c WHERE c.equipoIdequipo.lugarIdlugar.nombre = :name and c.numeroOrden != :tag AND c.endDate IS NOT NULL ORDER BY c.startDate DESC", Orden.class);
        query.setParameter("name", linea);
        query.setParameter("tag", "n/a");
        List<Orden> res = query.getResultList();
        // Collections.reverse(res);
        return res;
    }

    public List<Orden> findAllImprovements(String linea) {
        TypedQuery<Orden> query = em.createQuery("SELECT c FROM Orden c WHERE c.equipoIdequipo.lugarIdlugar.nombre = :name and c.numeroOrden = :tag  AND c.endDate IS NOT NULL ORDER BY c.startDate DESC", Orden.class);
        query.setParameter("name", linea);
        query.setParameter("tag", "n/a");
        List<Orden> res = query.getResultList();
        // Collections.reverse(res);
        return res;
    }

    public List<Orden> findARecent(Usuario user) {
        if ((user.getQuintana() == 1) && (user.getTabasco() == 1)) {
            TypedQuery<Orden> query = em.createQuery("SELECT c FROM Orden c  WHERE c.endDate IS NOT NULL ORDER BY c.idorden DESC", Orden.class);
            query.setMaxResults(10);
            return query.getResultList();
        } else if (user.getQuintana() == 1) {
            TypedQuery<Orden> query = em.createQuery("SELECT c FROM Orden c  WHERE c.endDate IS NOT NULL  AND c.equipoIdequipo.lugarIdlugar.estado = :es ORDER BY c.idorden DESC", Orden.class);
            query.setParameter("es", "quintana");
            query.setMaxResults(10);
            return query.getResultList();
        } else if (user.getTabasco() == 1) {
            TypedQuery<Orden> query = em.createQuery("SELECT c FROM Orden c  WHERE c.endDate IS NOT NULL  AND c.equipoIdequipo.lugarIdlugar.estado = :es ORDER BY c.idorden DESC", Orden.class);
            query.setParameter("es", "tabasco");
            query.setMaxResults(10);
            return query.getResultList();
        } else {
            return null;
        }
    }

    public long countValid() {
        Query query = em.createQuery("SELECT COUNT(c.idorden) FROM Orden c WHERE c.endDate IS NOT NULL");
        return (Long) query.getSingleResult();
    }

    public Orden findOrder(String id) {
        TypedQuery<Orden> query = em.createQuery("SELECT c FROM Orden c WHERE c.idorden = :id", Orden.class);
        query.setParameter("id", Integer.parseInt(id));
        Orden res = query.getSingleResult();
        res.getHistorialDetallesList().size();
        res.getFotosList().size();
        return res;
    }
}
