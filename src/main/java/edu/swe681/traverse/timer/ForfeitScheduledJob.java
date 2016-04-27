package edu.swe681.traverse.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * A timer class that will run and automatically forfeit a user if they have
 * taken too long to make a move in their game.
 */
@Component
public class ForfeitScheduledJob {
	private static final Logger LOG = LoggerFactory.getLogger(ForfeitScheduledJob.class);
	
	/**
	 * Forfeit process method. Takes 5 minutes to fire after startup and runs every 5 minutes after that.
	 */
	@Scheduled(fixedRate = 300000)
	public void processForfeits() {
		LOG.info("Running forfeit process job");
		// System.out.println("Running forfeit process job");
		// TODO: algorithm to cancel expired games 
	}
}
