package tp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import tp.model.Animal;

public class DaoService {
	private Connection connection;
	private final static String url = "jdbc:mysql://us-cdbr-iron-east-03.cleardb.net/ad_c43aa0d722fddb8";
	private final static String userName = "b31e7b85621dcb";
	private final static String password = "cea86aad";

	public DaoService() throws Exception {
        connection = null;

        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(url, userName, password);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS animals");
        stmt.executeUpdate("DROP TABLE IF EXISTS cages");
        stmt.executeUpdate("CREATE TABLE animals (id varchar(100), name varchar(100), cage varchar(100), species varchar(100), PRIMARY KEY (id))");
    }

	public void addAnimal(Animal animal) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO animals VALUES(?,?,?,?)");
		preparedStatement.setString(1, animal.getId().toString());
		preparedStatement.setString(2, animal.getName());
		preparedStatement.setString(3, animal.getCage());
		preparedStatement.setString(4, animal.getSpecies());
		preparedStatement.executeUpdate();
	}

	public List<Animal> getAnimals() throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM animals");
		List<Animal> animals = new ArrayList<Animal>();
		while (rs.next()) {
			animals.add(new Animal(rs.getString("name"), rs.getString("cage"), rs.getString("species"),
					UUID.fromString(rs.getString("id"))));
		}
		return animals;
	}


}
