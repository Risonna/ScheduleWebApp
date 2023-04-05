package com.risonna.schedulewebapp.database;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleDatabase {
    private static Connection conn;
    private static InitialContext ic;
    private static DataSource ds;

    public static Connection getConnection() {
        try {
            ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:comp/env/jdbc/webschedule");
            conn = ds.getConnection();
        } catch (SQLException | NamingException ex) {
            Logger.getLogger(ScheduleDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;
    }


}
