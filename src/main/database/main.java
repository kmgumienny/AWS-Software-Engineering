package main.database;

import java.sql.Connection;

public class main {
	public static void main (String args[]) throws Exception {
		DatabaseConnect conn = new DatabaseConnect();
		Connection connnn = conn.connect();
		if (connnn != null) {
			System.out.println("connected");
			System.out.println(connnn.isValid(15));
		}
		else
			System.out.println("not connected");
	}

}
