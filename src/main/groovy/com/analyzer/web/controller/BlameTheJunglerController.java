package com.analyzer.web.controller;

import com.riot.api.RiotApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@CrossOrigin(origins = "*")
@RestController
public class BlameTheJunglerController
{
	private RiotApi api;

	private static Logger logger = LoggerFactory.getLogger(BlameTheJunglerController.class);

	@Inject
	BlameTheJunglerController()
	{
		this.api = new RiotApi(System.getProperty("api.key"));
	}

	@RequestMapping("/")
	String helloWorld()
	{
		return "Hello world from the Blame the Jungler Analyzer!";
	}
}
