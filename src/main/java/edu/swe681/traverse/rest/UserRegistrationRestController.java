package edu.swe681.traverse.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.swe681.traverse.application.exception.BadRequestException;
import edu.swe681.traverse.persistence.dao.UsersDao;
import edu.swe681.traverse.rest.dto.request.UserRegistrationDto;

/**
 * REST controller for the registration API.
 */
@RestController
@RequestMapping(value="/api/register")
public class UserRegistrationRestController {
	
	@Autowired
	private UsersDao usersDao;
	private final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

	/**
	 * Registers a user for the system.
	 * 
	 * @param requestDto Contains the username and password
	 * @return 200
	 * @throws BadRequestException 
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationDto requestDto) throws BadRequestException {
		
		if(usersDao.doesUsernameExist(requestDto.getUsername())) {
			throw new BadRequestException("Username is already in use");
		}
		
		if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
			throw new BadRequestException("Passwords do not match");
		}
		
		// TODO:
		// @Pattern(regexp="", message="") on the requestDto for passwords and usernames
		
		usersDao.saveUser(requestDto.getUsername(), encoder.encode(requestDto.getPassword()));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
