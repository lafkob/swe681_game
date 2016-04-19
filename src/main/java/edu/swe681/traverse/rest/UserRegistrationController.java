package edu.swe681.traverse.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.swe681.traverse.persistence.dao.UsersDao;
import edu.swe681.traverse.rest.dto.request.UserRegistrationDto;

/**
 * REST controller for the registration API.
 */
@RestController
@RequestMapping(value="/api/register")
public class UserRegistrationController {
	
	@Autowired
	private UsersDao usersDao;
	private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

	/**
	 * Registers a user for the system.
	 * 
	 * @param requestDto Contains the username and password
	 * @return 200
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> registerUser(@ModelAttribute @Valid UserRegistrationDto requestDto) {
		
		if(usersDao.getUserByUsername(requestDto.getUsername())  != null) {
			// TODO: exception for user already exists
		}
		
		if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
			// TODO: exception for passwords do not match
		}
		
		usersDao.saveUser(requestDto.getUsername(), encoder.encode(requestDto.getPassword()));
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
