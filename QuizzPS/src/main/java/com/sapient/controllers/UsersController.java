package com.sapient.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sapient.dao.AttemptsQuizDao;
import com.sapient.entity.AttemptsQuiz;
import com.sapient.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sapient.entity.User;
import com.sapient.dao.UsersDao;

@RestController
public class UsersController {
	
	
	@GetMapping("/api/test")
	public ResponseEntity<?> test(){
		Map<String, Object> map = new HashMap<>();
		map.put("res", "success");
		return ResponseEntity.ok(map);
	}

	@PostMapping("/api/login")
	public ResponseEntity<?> login(@RequestBody User user) throws Exception {

		UsersDao usersDao = new UsersDao();

		User registeredUser = usersDao.getUserByEmail(user.getEmail());
		if(user.getPassword().equals(registeredUser.getPassword())) {
			Map<String, Object> map = new HashMap<>();
			int id = registeredUser.getUserID();
			String firstname = registeredUser.getFirstName();
			map.put("id", id);
			map.put("firstname", firstname);
			map.put("token", JwtUtil.createToken(id, firstname));

			return ResponseEntity.ok(map);
		} else {
		    Map<String, Object> errorMap = new HashMap<>();
		    errorMap.put("error", "Invalid email/password.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
		}
	}


	@PostMapping("/api/register")
	public ResponseEntity<?> register(@RequestBody User user) throws Exception {

		UsersDao usersDao = new UsersDao();

		User registeredUser = usersDao.getUserByEmail(user.getEmail());

		if(registeredUser.getEmail() == null) {
			usersDao.insertUser(user);
			Map<String, Object> map = new HashMap<>();

			map.put("success", true);

			return ResponseEntity.ok(map);
		} else {
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("error", "Email already registered.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
		}
	}

	@GetMapping("/api/profile")
	public ResponseEntity<?> profile(@RequestHeader(name = "Authorization", required = false) String authHeader) {
		if(authHeader == null) {
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("error", "Authorization token is missing.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
		}

		try {
			String token = authHeader.split(" ")[1];

			int userID = JwtUtil.verify(token);

			UsersDao usersDao = new UsersDao();

			User registeredUser = usersDao.getUserByID(userID);

			Map<String, Object> map = new HashMap<>();

			if(registeredUser.isAdmin()) {

				map.put("success", true);
				map.put("userID", userID);
				map.put("firstName", registeredUser.getFirstName());
				map.put("lastName", registeredUser.getLastName());
				map.put("email", registeredUser.getEmail());

			}else {
				AttemptsQuizDao attemptsQuizDao = new AttemptsQuizDao();
				List<AttemptsQuiz> performance = attemptsQuizDao.getUserPerformanceByUserID(userID);

				map.put("success", true);
				map.put("userID", userID);
				map.put("firstName", registeredUser.getFirstName());
				map.put("lastName", registeredUser.getLastName());
				map.put("email", registeredUser.getEmail());
				map.put("performance", performance);
			}

			return ResponseEntity.ok(map);

		} catch (Exception ex) {
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("error", "Authorization token is invalid or " + ex.getMessage());

            ex.printStackTrace();

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
		}

	}


}

