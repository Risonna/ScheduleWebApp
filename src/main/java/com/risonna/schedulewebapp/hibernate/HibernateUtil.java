package com.risonna.schedulewebapp.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        System.out.println("registry is " + registry.toString());
        try{
            return new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Throwable ex){
            System.err.println("Initial SessionFactory creation failed." + ex);
            StandardServiceRegistryBuilder.destroy( registry );
            throw new ExceptionInInitializerError();

        }
    }

    public static SessionFactory getSessionFactory(){return sessionFactory;}

    public static void close(){
        getSessionFactory().close();
    }
}
