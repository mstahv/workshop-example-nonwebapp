
package org.vaadin.swingersclub;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.vaadin.domain.model.Person;

/**
 * Wraps all JPA stuff inside
 */
public class JPAFacade {
    
    EntityManager em;
    private final EntityManagerFactory emf;

    public JPAFacade() {
        emf =  Persistence.createEntityManagerFactory("mydb");
        em = emf.createEntityManager();
    }
    
    List<Person> findAll() {
        return em.createQuery("select p from Person p", Person.class).getResultList();
    }

    void remove(Person editedCustomer) {
        em.getTransaction().begin();
        em.remove(editedCustomer);
        em.getTransaction().commit();
    }

    void save(Person c) {
        em.getTransaction().begin();
        if(c.getId() == null) {
            em.persist(c);
        } else {
            em.merge(c);
        }
        em.getTransaction().commit();
    }

}
