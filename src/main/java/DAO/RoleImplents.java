package DAO;

import Entity.Role;
import Interface.IRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.gestion_presence_professeurs.JAVAUtil;

import java.util.List;

public class RoleImplents implements IRepository<Role> {
    @Override
    public void add(Role role) {

    }

    @Override
    public void update(Role role) {

    }

    @Override
    public void delete(Role role) {

    }

    @Override
    public List<Role> list() {
        EntityManagerFactory entityManagerFactory = JAVAUtil.getEntityManagerFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Role>roleList = null;
        try {
            TypedQuery<Role> query = entityManager.createQuery("SELECT c FROM Role c", Role.class);
            roleList = query.getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return roleList;
    }
    }

