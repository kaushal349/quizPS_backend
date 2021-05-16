package com.sapient.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import com.sapient.entity.User;
import com.sapient.utils.DbUtil;

public class UsersDao {
	
	public List<User> getAll() throws Exception{
		String sql = "SELECT * FROM users";

		List<User> userList = new ArrayList<User>();

		try(
				Connection conn = DbUtil.createConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
		) {

			while(rs.next()) {
				User user=new User();
				user.setUserID(rs.getInt(1));
				user.setEmail(rs.getString(2));
				user.setFirstName(rs.getString(3));
				user.setLastName(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setAdmin(rs.getBoolean(6));
				userList.add(user);
			}

        } catch(Exception ex) {
			ex.printStackTrace();
		}

		return userList;
	}

	public User getUserByEmail(String email) throws Exception{
		String sql = "SELECT * from users WHERE users.email = ?";
		User user = new User();
		try (
				Connection conn = DbUtil.createConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			stmt.setString(1, email);
			try(ResultSet rs = stmt.executeQuery()) {
				if(rs.next()) {
					user.setUserID(rs.getInt(1));
					user.setEmail(rs.getString(2));
					user.setFirstName(rs.getString(3));
					user.setLastName(rs.getString(4));
					user.setPassword(rs.getString(5));
					user.setAdmin(rs.getBoolean(6));
				}
			}

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return user;
	}

	public User getUserByID(int id) throws Exception{
		String sql = "SELECT * from users WHERE users.ID = ?";
		User user = new User();
		try (
				Connection conn = DbUtil.createConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
		) {
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery()) {
				if(rs.next()) {
					user.setUserID(rs.getInt(1));
					user.setEmail(rs.getString(2));
					user.setFirstName(rs.getString(3));
					user.setLastName(rs.getString(4));
					user.setPassword(rs.getString(5));
					user.setAdmin(rs.getBoolean(6));
				}
			}

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return user;
	}

	public void insertUser(User user) throws Exception{

		String sql = "INSERT INTO users (firstname, lastname, email, password, isAdmin) values (?, ?, ?, ?, ?)";
		try(
				Connection conn = DbUtil.createConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			stmt.setString(1, user.getFirstName());
			stmt.setString(2, user.getLastName());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, user.getPassword());
			stmt.setBoolean(5, user.isAdmin());

			stmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
