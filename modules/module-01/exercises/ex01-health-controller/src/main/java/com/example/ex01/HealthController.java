package com.example.ex01;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {
	@Value("${project.appVersion:unknown}")
	private String appVersion;

	@Value("${project.appStatus:unknown}")
	private String appStatus;


	@GetMapping
	public String health() {
		return "OK!";
	}

	@GetMapping("/info")
	public HealthResponse info() {
		return new HealthResponse(appVersion, appStatus, System.getProperty("java.version"));
	}

}
