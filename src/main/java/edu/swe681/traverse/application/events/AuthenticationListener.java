package edu.swe681.traverse.application.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import edu.swe681.traverse.persistence.dao.UsersDao;

/**
 * This listener class intercepts success and bad credential events from login.
 */
@Component
public class AuthenticationListener
{
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationListener.class);
	private final static int MAX_ATTEMPTS = 4;
	
	@Autowired
	private UsersDao usersDao;
	
    @EventListener
    public void handleSuccessEvent(InteractiveAuthenticationSuccessEvent event) {
    	UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
        usersDao.resetAuthAttempts(userDetails.getUsername());
    }
    
    @EventListener
    public void handleFailureEvent(AuthenticationFailureBadCredentialsEvent event)
    { 
    	String username = (String) event.getAuthentication().getPrincipal();
    	if (usersDao.doesUsernameExist(username))
    	{
        	usersDao.incrementAuthAttempts(username);
        	final int authAttempts = usersDao.getUserByUsername(username).getAuthAttempts();
        	LOG.warn(String.format("User: %s - Authentication attempt %d failed. ",
        			username, authAttempts));
        	if (authAttempts >= MAX_ATTEMPTS)
        	{
        		LOG.info(String.format("User %s exceeded authentication attempts - disabling user", username));
        		usersDao.disableUser(username);
        	}
    	}
    }
}
