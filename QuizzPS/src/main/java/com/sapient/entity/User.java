package com.sapient.entity;

import lombok.Data;

@Data
public class User {
	private int userID;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private boolean isAdmin;
}
