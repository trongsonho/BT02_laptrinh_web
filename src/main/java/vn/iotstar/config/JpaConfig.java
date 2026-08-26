package vn.iotstar.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaConfig {

    private static volatile EntityManagerFactory factory;

    private static EntityManagerFactory getFactory() {
        if (factory == null) {
            synchronized (JpaConfig.class) {
                if (factory == null) {
                    factory = Persistence.createEntityManagerFactory("jpa-hibernate-mysql");
                }
            }
        }
        return factory;
    }

    public static EntityManager getEntityManager() {
        return getFactory().createEntityManager();
    }
}