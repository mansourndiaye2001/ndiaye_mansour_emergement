package DAO;

import Entity.Cours;
import Entity.Emergement;
import Entity.Utulisateur;
import Interface.IRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Interface.IRepository;
import Services.EmailService;
import Services.Notification;
import Services.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.gestion_presence_professeurs.JAVAUtil;

public class CoursImplemts implements IRepository<Cours> {
    @Override
    public void add(Cours cours) {

    }

    @Override
    public void update(Cours cours) {

    }

    @Override
    public void delete(Cours cours) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            cours = entityManager.find(Cours.class, cours.getId());

            TypedQuery<Emergement> query = entityManager.createQuery(
                    "SELECT e FROM Emergement e WHERE e.cours = :cours", Emergement.class);
            query.setParameter("cours", cours);
            List<Emergement> emergements = query.getResultList();

            for (Emergement emergement : emergements) {
                entityManager.remove(emergement);
            }

            entityManager.remove(cours);

            entityManager.getTransaction().commit();

        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ex.printStackTrace();
        } finally {

            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }


    @Override
    public List<Cours> list() {
            EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            List<Cours> coursList = new ArrayList<>(); // Initialisation d'une liste vide
            try {
                TypedQuery<Cours> query = entityManager.createQuery("SELECT c FROM Cours c", Cours.class);
                coursList = query.getResultList();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                entityManager.close();
            }
            return coursList;
    }
    public boolean addCours(Cours cours, String jour) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();


            cours.setJour(jour);


            if (checkProfesseurConflit(entityManager, cours)) {
                Notification.NotifiError(" ", "Le professeur est occupé à cette heure-là.");
                transaction.rollback();
                return false;
            }


            if (checkSalleConflit(entityManager, cours)) {
                Notification.NotifiError(" ", "La salle est occupée à cette heure-là.");
                transaction.rollback();
                return false;
            }

            entityManager.persist(cours);

            // Notifications et emails
            EmailService emailService = new EmailService();
            Utulisateur professeur = entityManager.find(Utulisateur.class, cours.getProfesseur().getId());
            emailService.envoyerEmail(professeur, cours);

            Entity.Notification notification = new Entity.Notification();
            notification.setProfesseur(professeur);
            notification.setMessage("Affectation d'un cours de " + cours.getNom() + " le " + cours.getJour());
            notification.setDate_envoie(LocalDateTime.now());
            entityManager.persist(notification);

            Notification.NotifiSuccess("Succès", "Ajout avec succès");
            transaction.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            return false;
        } finally {
            entityManager.close();
        }
    }


    private boolean checkCoursConflit(EntityManager entityManager, Cours nouveauCours) {
        String queryStr = "SELECT COUNT(c) FROM Cours c WHERE " +
                "c.jour = :jour AND " +
                "((c.heureDebut <= :heureDebut AND c.heureFin > :heureDebut) OR " +
                "(c.heureDebut < :heureFin AND c.heureFin >= :heureFin) OR " +
                "(c.heureDebut >= :heureDebut AND c.heureFin <= :heureFin))";

        TypedQuery<Long> query = entityManager.createQuery(queryStr, Long.class);
        query.setParameter("jour", nouveauCours.getJour());
        query.setParameter("heureDebut", nouveauCours.getHeureDebut());
        query.setParameter("heureFin", nouveauCours.getHeureFin());

        return query.getSingleResult() > 0;
    }

    private boolean checkProfesseurConflit(EntityManager entityManager, Cours nouveauCours) {
        String queryStr = "SELECT COUNT(c) FROM Cours c WHERE " +
                "c.professeur = :professeur AND " +
                "c.jour = :jour AND " +
                "((c.heureDebut <= :heureDebut AND c.heureFin > :heureDebut) OR " +
                "(c.heureDebut < :heureFin AND c.heureFin >= :heureFin) OR " +
                "(c.heureDebut >= :heureDebut AND c.heureFin <= :heureFin))";

        TypedQuery<Long> query = entityManager.createQuery(queryStr, Long.class);
        query.setParameter("professeur", nouveauCours.getProfesseur());
        query.setParameter("jour", nouveauCours.getJour());
        query.setParameter("heureDebut", nouveauCours.getHeureDebut());
        query.setParameter("heureFin", nouveauCours.getHeureFin());

        return query.getSingleResult() > 0;
    }

    private boolean checkSalleConflit(EntityManager entityManager, Cours nouveauCours) {
        String queryStr = "SELECT COUNT(c) FROM Cours c WHERE " +
                "c.salle = :salle AND " +
                "c.jour = :jour AND " +
                "((c.heureDebut <= :heureDebut AND c.heureFin > :heureDebut) OR " +
                "(c.heureDebut < :heureFin AND c.heureFin >= :heureFin) OR " +
                "(c.heureDebut >= :heureDebut AND c.heureFin <= :heureFin))";

        TypedQuery<Long> query = entityManager.createQuery(queryStr, Long.class);
        query.setParameter("salle", nouveauCours.getSalle());
        query.setParameter("jour", nouveauCours.getJour());
        query.setParameter("heureDebut", nouveauCours.getHeureDebut());
        query.setParameter("heureFin", nouveauCours.getHeureFin());

        return query.getSingleResult() > 0;
    }









}
