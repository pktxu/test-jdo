package org.datanucleus.test;

import static org.junit.Assert.fail;

import javax.jdo.*;

import mydomain.model.Person;
import org.datanucleus.util.NucleusLogger;
import org.junit.Test;

public class MultithreadTest
{

    @Test
    public void testMulti()
    {
        NucleusLogger.GENERAL.info(">> test START");
        final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        try
        {
            // do NOT persist any data, since we are pointing at initialization issues

            // Create the Threads
            int THREAD_SIZE = 500;
            final String[] threadErrors = new String[THREAD_SIZE];
            Thread[] threads = new Thread[THREAD_SIZE];
            for (int i = 0; i < THREAD_SIZE; i++)
            {
                final int threadNo = i;
                threads[i] = new Thread(new Runnable()
                {
                    public void run()
                    {
                        String errorMsg = performTest(pmf);
                        threadErrors[threadNo] = errorMsg;
                    }
                });
            }

            // Run the threads
            NucleusLogger.GENERAL.info(">> Starting threads");
            for (int i = 0; i < THREAD_SIZE; i++)
            {
                threads[i].start();
            }
            for (int i = 0; i < THREAD_SIZE; i++)
            {
                try
                {
                    threads[i].join();
                }
                catch (InterruptedException e)
                {
                    fail(e.getMessage());
                }
            }
            NucleusLogger.GENERAL.info(">> Completed threads");

            // Process any errors from Threads and fail the test if any failed
            for (String error : threadErrors)
            {
                if (error != null)
                {
                    fail(error);
                }
            }
        }
        finally
        {
            // [Clean up data]
        }

        pmf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }

    /**
     * Method to perform the test for a Thread.
     * @param pmf The PersistenceManagerFactory
     * @return A string which is null if the PM operations are successful
     */
    protected String performTest(PersistenceManagerFactory pmf)
    {
        PersistenceManager pm = null;
        Transaction tx = null;
        for (int i = 0; i < 100; i++) {
            long id = Long.MIN_VALUE;
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            // store
            try {
                tx.begin();

                Person p = new Person(String.valueOf(Math.random()));
                p = pm.makePersistent(p);
                id = p.getId();

                tx.commit();
            }
            catch (Throwable thr)
            {
                NucleusLogger.GENERAL.error("Exception performing test", thr);
                return "Exception performing test : " + thr.getMessage();
            }
            finally {
                if (tx.isActive())
                {
                    tx.rollback();
                }
                pm.close();
            }
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            // fetch
            try {
                tx.begin();
                pm.getObjectById(Person.class, id);
                tx.commit();
            }
            catch (JDOObjectNotFoundException e)
            {
                NucleusLogger.GENERAL.info("No person with id " + id);
            }
            catch (Throwable thr)
            {
                String s = new String();
                thr.printStackTrace();
                NucleusLogger.GENERAL.error("Exception performing test", thr);
                return "Exception performing test : " + thr.getMessage();
            }
            finally
            {
                if (tx.isActive())
                {
                    tx.rollback();
                }
                pm.close();
            }
        }
    return null;
    }
}
