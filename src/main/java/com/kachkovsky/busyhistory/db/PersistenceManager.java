package com.kachkovsky.busyhistory.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public enum PersistenceManager {
    INSTANCE;

    public static final String HIBERNATE_DDL_MODE_KEY = "hibernate.hbm2ddl.auto";
    public static final String JDBC_URL_KEY = "javax.persistence.jdbc.url";

    private EntityManagerFactory emFactory;

    private PersistenceManager() {
        // "jpa-example" was the value of the name attribute of the
        // persistence-unit element.

        Map<String, String> properties = new HashMap<>();
        properties.put(JDBC_URL_KEY, getJDBCUrl("127.0.0.1","busy"));
        emFactory = Persistence.createEntityManagerFactory("busyhistorypu", properties);
    }

    private static String getJDBCUrl(String ip, String name) {
        return "jdbc:mysql://" + ip + "/" + name;
    }

    public EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }

    public void close() {
        emFactory.close();
    }
}