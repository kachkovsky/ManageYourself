package com.kachkovsky.busyhistory;

import com.kachkovsky.busyhistory.data.BusyItem;
import com.kachkovsky.busyhistory.db.PersistenceManager;
import com.kachkovsky.busyhistory.db.transaction.TransactionRunnable;
import com.kachkovsky.javafx.WindowHelper;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class Main extends Application {

//    public static void main(String[] args) {
//        EntityManager em = PersistenceManager.INSTANCE.getEntityManager();
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<BusyItem> q = cb.createQuery(BusyItem.class);
//        Root<BusyItem> root = q.from(BusyItem.class);
//        CriteriaQuery<BusyItem> select = q.select(root);
//        List<BusyItem> resultList = em.createQuery(select).getResultList();
//        System.out.print(resultList.size());
//
//        BusyItem gg = new BusyItem(LocalDate.now(), 8., "GG");
//        new TransactionRunnable(em) {
//            @Override
//            protected void doTransaction(EntityManager em) {
//                em.persist(gg);
//            }
//        }.run();
//        em.close();
//    }


    @Override
    public void start(Stage stage) throws Exception {

        long longValue = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
        System.out.println(longValue);
        LocalDate localDate = Instant.ofEpochMilli(longValue).atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println(localDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond());
        //stage.setTitle(WindowHelper.DEFAULT_TITLE);
        //stage.setMaximized(true);
        WindowHelper.showStageForFXML(this.getClass(), stage, "/fxml/form/table.fxml");
    }

    @Override
    public void stop() {
        System.out.println("Stage closing");
        // Save file
    }
}