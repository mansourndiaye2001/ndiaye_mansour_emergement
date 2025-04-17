package DAO;

import Entity.Utulisateur;
import Interface.IRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.gestion_presence_professeurs.JAVAUtil;

import java.util.ArrayList;
import java.util.List;

public class UserImplent implements IRepository<Utulisateur> {
    @Override
    public void add(Utulisateur utulisateur) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(utulisateur);
            entityManager.getTransaction().commit();
            entityManager.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Utulisateur utulisateur) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            Utulisateur utulisateur1 = entityManager.find(Utulisateur.class, utulisateur.getId());

            if (utulisateur1 != null) {
                utulisateur1.setNom(utulisateur.getNom());
                utulisateur1.setEmail(utulisateur.getEmail());
                utulisateur1.setPassword(utulisateur.getPassword());
                utulisateur1.setRole(utulisateur.getRole());
                utulisateur1.setPrenom(utulisateur.getPrenom());
                entityManager.merge(utulisateur);
            }

            entityManager.getTransaction().commit();
            entityManager.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void delete(Utulisateur utulisateur) {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            utulisateur = entityManager.find(utulisateur.getClass(), utulisateur.getId());
            entityManager.getTransaction().begin();
            entityManager.remove(utulisateur);
            entityManager.getTransaction().commit();
            entityManager.close();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public List<Utulisateur> list() {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Utulisateur> utulisateurList = null;
        try {
            TypedQuery<Utulisateur> query = entityManager.createQuery("SELECT c FROM Utulisateur  c", Utulisateur.class);
            utulisateurList = query.getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return utulisateurList;
    }

    public List<Utulisateur> list_Prof() {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Utulisateur> utulisateurList = new ArrayList<>(); // Initialisation de la liste ici
        try {
            TypedQuery<Utulisateur> query = entityManager.createQuery("SELECT c FROM Utulisateur c WHERE c.role.id = 3", Utulisateur.class);

            utulisateurList = query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return utulisateurList;  // UtulisateurList ne sera jamais null, même en cas d'erreur
    }


    public Utulisateur verifierConnexion(String email, String password) {
        Utulisateur utilisateur = null;
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {

            utilisateur = entityManager.createQuery(
                    "SELECT u FROM Utulisateur u WHERE u.email = :email AND u.password = :password",
                            Utulisateur.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();




        } catch (Exception e) {
            e.printStackTrace();


        }
        return utilisateur;
    }
}

