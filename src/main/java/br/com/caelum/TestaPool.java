package br.com.caelum;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestaPool {
	public static void main(String[] args) throws PropertyVetoException, SQLException {
		ComboPooledDataSource dataSource = (ComboPooledDataSource) new JpaConfigurator().getDataSource();
		
		
//		Connection connnection2 = dataSource.getConnection();
//		Connection connnection3 = dataSource.getConnection();
//		Connection connnection4 = dataSource.getConnection();
//		Connection connnection5 = dataSource.getConnection();
//		Connection connnection6 = dataSource.getConnection();
		
		for (int i = 0; i <= 20; i++) {
			Connection connnection = dataSource.getConnection();
			System.out.println(dataSource.getNumBusyConnections());
			System.out.println(dataSource.getNumIdleConnections());
			System.out.println();
		}
		
		
		
	}
}
