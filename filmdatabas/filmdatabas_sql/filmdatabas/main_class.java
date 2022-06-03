package filmdatabas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class main_class {
	
	public static void main(String[] args) throws SQLException {
		Connection conn = DbConnect();
		
		
//		GetActors(conn);
//		deleteActor(conn, 6);
		GetActors(conn);
		updateActor(conn, 5, "female");
//		GetActors(conn)
		addNewActor(conn, "josip", "marijic", "female");
		GetActors(conn);

		
		
		
		conn.close();
	}

	private static Connection DbConnect() {
		String dbLocation = "jdbc:mysql://localhost:3306/filmdatabas";
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(dbLocation, "root", "165692jm");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("fel");
			e.printStackTrace();
		}
		
		System.out.println("databas ansluten");
		
		return conn;
		
	}
	
	private static void DumpTable(Connection cn) {
		String qry = "SHOW tables; ";

		try {
			PreparedStatement myQry = cn.prepareStatement(qry);
			ResultSet rs = myQry.executeQuery();
			//System.out.println(rs.getMetaData().getTableName(0));
			
			while(rs.next()) {  // rows
				int numCols = rs.getMetaData().getColumnCount();
				for (int i = 1; i <= numCols; i++) {  // columns
					String tableName = rs.getMetaData().getTableName(i);			
					System.out.println("Tables: " + rs.getObject(i));
				}
				
			}		
		} catch (SQLException e) {
			System.out.println("något gick fel");
			e.printStackTrace();
		}
		
	}
	
	private static ArrayList<actorBean> GetActors(Connection cn) {
		String qry = "select * from actor";

		ArrayList<actorBean> actors = new ArrayList<actorBean>();
		
		try {
			PreparedStatement myQry = cn.prepareStatement(qry);
			ResultSet rs = myQry.executeQuery();
			while(rs.next()) {  // rows
				actorBean ab = new actorBean();
				ab.setId(rs.getInt("actor_id"));
				ab.setName(rs.getString("first_name"));
				ab.set_lastName(rs.getString("last_name"));
				ab.set_gender(rs.getString("actor_gender"));
				
				

				actors.add(ab);
				
				
			}
			
			for (actorBean actorBean : actors) {
				System.out.println("ID: " + actorBean.getId() + " Namn: " + actorBean.getName() + " Efternamn: " 
			+ actorBean.get_lastName() + " Kön: " + actorBean.get_gender());
//				
			}
			
		} catch (SQLException e) {
			System.out.println("list actors exception");
			e.printStackTrace();
		}

		return actors;
	}
	
	private static void deleteActor(Connection cn, int ID) {
		String qry = "DELETE FROM actor WHERE actor_id = ?";
		
		try {
			PreparedStatement myQry = cn.prepareStatement(qry);
			myQry.setInt(1, ID);
			myQry.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("fel vid radering av actor");
			e.printStackTrace();
		}
		
		
	}
	
	private static void updateActor(Connection cn, int ID, String sex) {
		String qry = "UPDATE actor SET actor_gender = ? WHERE actor_id = ?";
		
		
		try {
			PreparedStatement myQry = cn.prepareStatement(qry);
			myQry.setString(1, sex);
			myQry.setInt(2, ID);
			myQry.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("fel vid uppdatering av actor");
			e.printStackTrace();
		}
		
		
	}
	
	private static void addNewActor(Connection cn, String name, String lastName, String gender) {
		String qry = "INSERT INTO actor VALUES (NULL, ?, ?, ?, CURRENT_TIMESTAMP)";
		
		try {
			PreparedStatement myQry = cn.prepareStatement(qry);
			myQry.setString(1, name);
			myQry.setString(2, lastName);
			myQry.setString(3, gender);
			myQry.execute();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
