package DAO;

import Entity.Cours;
import Entity.Salle;
import Entity.Utulisateur;
import Interface.IRepository;
import Services.Notification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.gestion_presence_professeurs.JAVAUtil;

import java.util.List;

public class SalleImplemt implements IRepository<Salle> {
    @Override
    public void add(Salle salle) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(salle);
            entityManager.getTransaction().commit();
            entityManager.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Salle salle) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Salle salle1 = entityManager.find(Salle.class, salle.getId());

            if (salle1 != null) {
                salle1.setNom(salle.getNom());

                entityManager.merge(salle);
                entityManager.getTransaction().commit();
                entityManager.close();
            }



        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void delete(Salle salle) {

        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // Trouver la Salle dans la base de données
            salle = entityManager.find(Salle.class, salle.getId());

            // Vérifier si la Salle est associée à un Cours
            TypedQuery<Cours> query = entityManager.createQuery(
                    "SELECT c FROM Cours c WHERE c.salle = :salle", Cours.class);
            query.setParameter("salle", salle);
            List<Cours> coursList = query.getResultList();

            if (!coursList.isEmpty()) {
                Notification.NotifiError("error", "La salle est associée à un ou plusieurs cours et ne peut pas être supprimée.");

                throw new IllegalStateException("La salle est associée à un ou plusieurs cours et ne peut pas être supprimée.");

            }

            entityManager.getTransaction().begin();
            Notification.NotifiSuccess("", "La salle  supprimée.");
            entityManager.remove(salle);
            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            // En cas d'erreur, annuler la transaction et afficher l'exception
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ex.printStackTrace();
        } finally {
            // Toujours fermer l'EntityManager à la fin
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }


    @Override
    public List<Salle> list() {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Salle>salleList = null;
        try {
            TypedQuery<Salle> query = entityManager.createQuery("SELECT c FROM Salle  c", Salle.class);
            salleList = query.getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return salleList;
    }
    public boolean existeSalleParNom(String nom) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String jpql = "SELECT COUNT(c) FROM Salle c WHERE c.nom = :nom";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("nom", nom)
                .getSingleResult();
        return count > 0;  // Si count > 0, cela signifie que la classe existe déjà
    }

}

