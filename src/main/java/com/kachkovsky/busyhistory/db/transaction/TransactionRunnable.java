package com.kachkovsky.busyhistory.db.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class TransactionRunnable implements Runnable {

    private EntityManager em;

    public TransactionRunnable(EntityManager em) {
        this.em = em;
    }

    protected abstract void doTransaction(EntityManager em);

    @Override
    public void run() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            doTransaction(em);
            transaction.commit();
        } catch (Throwable t) {
            transaction.rollback();
            throw new RuntimeException(t);
        }
    }
}
