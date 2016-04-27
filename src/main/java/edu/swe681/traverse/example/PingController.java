package edu.swe681.traverse.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PingController {

	private static final Logger LOG = LoggerFactory.getLogger(PingController.class);
	
	@RequestMapping(value="/api/ping", method = RequestMethod.GET)
	@ResponseBody
	public String ping(){
		LOG.debug("Executed ping.");
		return "System up";
	}
}
