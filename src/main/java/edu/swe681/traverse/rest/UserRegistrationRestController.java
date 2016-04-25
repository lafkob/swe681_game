package edu.swe681.traverse.rest;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import edu.swe681.traverse.rest.dto.request.UserRegistrationRequestDto;

/**
 * REST controller for the registration API.
 */
@RestController
@RequestMapping(value="/api/register")
public class UserRegistrationRestController {

	private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationRestController.class);
	
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
	public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto) throws BadRequestException {
		
		if(usersDao.doesUsernameExist(requestDto.getUsername())) {
			LOG.info("Attempt to register username that is already in use: " + requestDto.getUsername());
			throw new BadRequestException("Username is already in use");
		}
		
		if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
			LOG.info("Attempt to register with non-matching passwords for user: " + requestDto.getUsername());
			throw new BadRequestException("Passwords do not match");
		}
		
		usersDao.saveUser(requestDto.getUsername(), encoder.encode(requestDto.getPassword()));
		LOG.info("Registered new user! Username: " + requestDto.getUsername());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
