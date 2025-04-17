package DAO;

import Entity.Emergement;
import Entity.Notification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.gestion_presence_professeurs.JAVAUtil;

import java.util.List;

public class Notification_db {
    public List<Notification> List()

    {

        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Notification> notificationList = null;
        try {
            TypedQuery<Notification> query = entityManager.createQuery("SELECT c FROM Notification  c", Notification.class);
            notificationList = query.getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return notificationList;
    }
}


