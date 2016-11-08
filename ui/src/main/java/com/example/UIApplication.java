package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UIApplication {

	public static void main(String[] args) {
		SpringApplication.run(UIApplication.class, args);
	}

	@GetMapping("/public/hello")
	public ResponseEntity<?> home() {
		return ResponseEntity.ok("Hello Every one!");
	}
	
	@GetMapping("/admin/hello")
	public ResponseEntity<?> admin() {
		return ResponseEntity.ok("Hello Admin!");
	}
}
