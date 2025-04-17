package DAO;

import Entity.Cours;
import Entity.Emergement;
import Entity.Utulisateur;
import Services.Notification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.gestion_presence_professeurs.JAVAUtil;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Prof_db {
    public enum JourSemaine {
        LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE
    }
    public List<Cours> getCoursDuJour(Utulisateur professeur) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Cours> coursDuJour = new ArrayList<>();

        try {
            if (professeur == null) {
                System.out.println("Erreur : professeur est null !");
                return coursDuJour;
            }

            if (professeur.getRole() == null || professeur.getRole().getId() != 3) {
                System.out.println("Erreur : rôle invalide ou non défini !");
                return coursDuJour;
            }

            SimpleDateFormat format = new SimpleDateFormat("EEEE", Locale.FRENCH);
            String jourActuel = format.format(new Date()).toLowerCase();

            String queryStr = "SELECT c FROM Cours c WHERE c.professeur.id = :professeurId " +
                    "AND LOWER(c.jour) = :jourActuel";

            TypedQuery<Cours> query = entityManager.createQuery(queryStr, Cours.class);
            query.setParameter("professeurId", professeur.getId());
            query.setParameter("jourActuel", jourActuel);

            coursDuJour = query.getResultList();

            if (coursDuJour.isEmpty()) {
                System.out.println("⚠ Aucun cours trouvé pour " + professeur.getNom() + " le " + jourActuel);
            } else {
                System.out.println("✅ Cours trouvés : " + coursDuJour);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la récupération des cours du jour.");
        } finally {
            entityManager.close();
        }

        return coursDuJour;
    }




    public boolean emerger(Utulisateur professeur, Cours cours) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            DayOfWeek jourActuel = LocalDate.now().getDayOfWeek();
            JourSemaine jourSemaine = convertirDayOfWeekEnJourSemaine(jourActuel);

            // 🔹 Vérifier si le cours a bien lieu aujourd'hui
            if (!cours.getJour().equalsIgnoreCase(jourSemaine.toString())) {
                Notification.NotifiError("", "Ce cours n'est pas prévu pour aujourd'hui !");
                transaction.rollback();
                return false;
            }

            LocalTime maintenant = LocalTime.now();

            LocalTime heureMinEmargement = cours.getHeureDebut().minusMinutes(15);
            LocalTime heureMaxEmargement = cours.getHeureDebut().plusMinutes(15);

            if (maintenant.isBefore(heureMinEmargement)) {
                Notification.NotifiError("", "L'émargement sera possible 15 minutes avant le début du cours.");
                transaction.rollback();
                return false;
            }

            if (maintenant.isAfter(heureMaxEmargement)) {
                // Si le professeur n'a pas émargé, on l'émarge automatiquement avec le statut "Absent"
                if (!emargementExistePourAujourdhui(entityManager, professeur, cours)) {
                    Emergement emerger = new Emergement();
                    emerger.setDate_emergement(LocalDateTime.now());
                    emerger.setStatut("Absent");
                    emerger.setCours(cours);
                    emerger.setProfesseur(professeur);
                    entityManager.persist(emerger);
                    transaction.commit();
                    Notification.NotifiSuccess("Succès", "Le professeur a été émargé automatiquement avec le statut 'Absent'.");
                    return true;
                } else {
                    Notification.NotifiError("", "La période d'émargement est terminée (15 minutes après le début du cours).");
                    transaction.rollback();
                    return false;
                }
            }

            if (emargementExistePourAujourdhui(entityManager, professeur, cours)) {
                Notification.NotifiError("", "Vous avez déjà émargé pour ce cours aujourd'hui.");
                transaction.rollback();
                return false;
            }

            // Si le professeur émarge dans la période autorisée
            Emergement emerger = new Emergement();
            emerger.setDate_emergement(LocalDateTime.now());
            emerger.setStatut("Présent");
            emerger.setCours(cours);
            emerger.setProfesseur(professeur);
            entityManager.persist(emerger);

            // 🔹 Valider la transaction
            transaction.commit();
            Notification.NotifiSuccess("Succès", "Émargement effectué avec succès !");
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



    // Méthode pour convertir DayOfWeek en JourSemaine
    private JourSemaine convertirDayOfWeekEnJourSemaine(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return JourSemaine.LUNDI;
            case TUESDAY: return JourSemaine.MARDI;
            case WEDNESDAY: return JourSemaine.MERCREDI;
            case THURSDAY: return JourSemaine.JEUDI;
            case FRIDAY: return JourSemaine.VENDREDI;
            case SATURDAY: return JourSemaine.SAMEDI;
            case SUNDAY: return JourSemaine.DIMANCHE;
            default: throw new IllegalArgumentException("Jour inconnu: " + dayOfWeek);
        }
    }

    // Méthode pour vérifier si un émargement existe déjà pour aujourd'hui
    private boolean emargementExistePourAujourdhui(EntityManager entityManager, Utulisateur professeur, Cours cours) {
        LocalDateTime debutJour = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime finJour = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        String queryStr = "SELECT COUNT(e) FROM Emergement e WHERE " +
                "e.professeur = :professeur AND " +
                "e.cours = :cours AND " +
                "e.date_emergement BETWEEN :debut AND :fin";

        TypedQuery<Long> query = entityManager.createQuery(queryStr, Long.class);
        query.setParameter("professeur", professeur);
        query.setParameter("cours", cours);
        query.setParameter("debut", debutJour);
        query.setParameter("fin", finJour);

        return query.getSingleResult() > 0;
    }

    public List<Emergement> list(Utulisateur professeur) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Emergement> emergements = null;

        try {

            TypedQuery<Emergement> query = entityManager.createQuery(
                    "SELECT e FROM Emergement e WHERE e.professeur = :professeur",
                    Emergement.class
            );
            query.setParameter("professeur", professeur);
            emergements = query.getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }

        return emergements;
    }

    public List  <Emergement>List_emergement()

    {

        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Emergement> emergements = null;
        try {
            TypedQuery<Emergement> query = entityManager.createQuery("SELECT c FROM Emergement  c", Emergement.class);
            emergements = query.getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return emergements;
    }
    public List<Object[]> getEmargementsParProfesseur() {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String query = "SELECT e.professeur, COUNT(e) FROM Emergement e GROUP BY e.professeur";
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query, Object[].class);
        return typedQuery.getResultList();
    }
    public List<Object[]> getEmargementsParDate() {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String jpql = "SELECT e.date_emergement, COUNT(e) " +
                "FROM Emergement e " +
                "GROUP BY e.date_emergement " +
                "ORDER BY e.date_emergement";


        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);

        return query.getResultList();

    }
    public List<Object[]> getTauxPresenceParCours() {
        EntityManager em = JAVAUtil.getEntityManagerFactory().createEntityManager();
        String jpql = "SELECT e.cours.nom, " +
                "SUM(CASE WHEN e.statut = 'Présent' THEN 1 ELSE 0 END), " +
                "COUNT(e) " +
                "FROM Emergement e " +
                "GROUP BY e.cours.nom";

        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        return query.getResultList();
    }


}







