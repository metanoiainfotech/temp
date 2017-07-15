package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdbc.DBUtility;
import com.model.restaurant;

public class CrudDao {

private final Connection dbConnection;
private PreparedStatement pStmt;

public CrudDao() {
	dbConnection = DBUtility.getConnection();
}

public void addrestaurant(restaurant re) {
	String insertQuery = "INSERT INTO restaurant(id, name, " +
			"type) VALUES (?,?,?)";
	try {
		pStmt = dbConnection.prepareStatement(insertQuery);
		pStmt.setInt(1, re.getId());
		pStmt.setString(2, re.getName());
		pStmt.setString(3, re.getType());
		pStmt.executeUpdate();
	} catch (SQLException e) {
		System.err.println(e.getMessage());
	}
}

public void deleterestaurant(int id) {
	String deleteQuery = "DELETE FROM restaurant WHERE id = ?";
	try {
		pStmt = dbConnection.prepareStatement(deleteQuery);
		pStmt.setInt(1, id);
		pStmt.executeUpdate();
	} catch (SQLException e) {
		System.err.println(e.getMessage());
	}
}

public void updaterestaurant(restaurant re)  {
	String updateQuery = "UPDATE restaurant SET name = ?, " +
			"type = ? WHERE id = ?";
	try {
		pStmt = dbConnection.prepareStatement(updateQuery);		
		pStmt.setString(1, re.getName());
		pStmt.setString(2, re.getType());
		pStmt.setInt(4, re.getId());
		pStmt.executeUpdate();

	} catch (SQLException e) {
		System.err.println(e.getMessage());
	}
}

public List<restaurant> getAllrestaurants() {
	List<restaurant> res = new ArrayList<restaurant>();

	String query = "SELECT * FROM restaurant ORDER BY id";
	try {
		Statement stmt = dbConnection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			restaurant re = new restaurant();

			re.setId(rs.getInt("id"));
			re.setName(rs.getString("name"));
			re.setType(rs.getString("type"));
			res.add(re);
		}
	} catch (SQLException e) {
		System.err.println(e.getMessage());
	}
	return res;
}
}