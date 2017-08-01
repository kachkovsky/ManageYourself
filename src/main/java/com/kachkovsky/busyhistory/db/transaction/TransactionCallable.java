package com.kachkovsky.busyhistory.db.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.concurrent.Callable;

public abstract class TransactionCallable<T> implements Callable<T> {

    private EntityManager em;

    public TransactionCallable(EntityManager em) {
        this.em = em;
    }

    protected abstract T doTransaction(EntityManager em);

    @Override
    public T call() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            T result = doTransaction(em);
            transaction.commit();
            return result;
        } catch (Throwable t) {
            transaction.rollback();
            throw new RuntimeException(t);
        }
    }
}
