package edu.swe681.traverse.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PingController {

	@RequestMapping(value="/api/ping", method = RequestMethod.GET)
	@ResponseBody
	public String ping(){
		return "System up";
	}
}
