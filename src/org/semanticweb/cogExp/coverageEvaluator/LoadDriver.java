package org.semanticweb.cogExp.coverageEvaluator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class LoadDriver {
	 public static void main(String[] args) {
	        try {
	            // The newInstance() call is a work around for some
	            // broken Java implementations

	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	            System.out.println("Driver loaded!");
	        } catch (Exception ex) {
	            // handle the error
	        }
	        
	        Connection conn = null;
	        
	        try {
	            conn =
	               DriverManager.getConnection("jdbc:mysql://localhost:3306/verbalizer",
	                                           "java", "java444");

	            // Do something with the Connection
	            
	            Statement stmt = conn.createStatement();
	            
	            // CREATE DATABASE verbalizer
	            // DEFAULT CHARACTER SET utf8
	            // DEFAULT COLLATE utf8_general_ci;
	            
	            //
	            // CREATE TABLE OCCURENCES (subclass VARCHAR(256), superclass VARCHAR(256), ontologypath VARCHAR (256));
	            //
	            //
	            
	            
	            String sql = "CREATE TABLE EXPLANATIONS " +
	                         "(id INTEGER, " + // (id INTEGER not NULL AUTO_INCREMENT, " +
	                         " subclass VARCHAR(256), " + 
	                         " superclass VARCHAR(256), " + 
	                         " ontologypath VARCHAR(256), " + 
	                         " corpus VARCHAR(255), " + 
	                         " solved TINYINT(1), " + 
	                         " explanation VARCHAR(2048), " + 
	                         " listing VARCHAR(2048), " + 
	                         " longlisting VARCHAR(2048), " +
	                         " equivextract INTEGER, " +
	                         " subclandequivelim INTEGER, " +
	                         " verbsteps INTEGER, " +
	                         " listingsteps INTEGER, " +
	                         " longlistingsteps INTEGER, " +
	                         " time INTEGER, " + //<== time in ms, in case problem not solved, limit time
	                         " r0 INTEGER, " +
	                         " r1 INTEGER, " +
	                         " r1neo INTEGER, " +
	                         " r2 INTEGER, " +
	                         " r3 INTEGER, " +
	                         " r5 INTEGER, " +
	                         " r5new INTEGER, " +
	                         " r5multi INTEGER, " +
	                         " r6 INTEGER, " +
	                         " r6neo INTEGER, " +
	                         " r12 INTEGER, " +
	                         " r12new INTEGER, " +
	                         " r15 INTEGER, " +
	                         " r23 INTEGER, " +
	                         " r23repeat INTEGER, " +
	                         " r34 INTEGER, " +
	                         " r35 INTEGER, " +
	                         " r37 INTEGER, " +
	                         " r42 INTEGER, " +
	                         " botintro INTEGER, " +
	                         " topintro INTEGER, " +
	                         " defdomain INTEGER, " + 
	                         "elexistsminus INTEGER, " +
	                         "applrange INTEGER, " +
	                         "propchain INTEGER, " +
	                         "forallunion INTEGER, " +
	                         " PRIMARY KEY (subclass, superclass, ontologypath))"; 

	            stmt.executeUpdate(sql);
	            
	           
	        } catch (SQLException ex) {
	            // handle any errors
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	        }
	        
	    }
}
