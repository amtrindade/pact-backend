package br.ce.wcaquino.taskbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value ="/")
public class RootController {

	@GetMapping
	public String hello() {
		return "Hello World!";
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@PostMapping(value = "/barrigaPactStateChange", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> stateChage(@RequestBody Map<String, Object> body) {
		String TOKEN = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODQ2fQ.T0Zmoerb0vE8Zt0a0VMuLFDcYY5RY5Kni_4prZHqGnE";
		Map<String, String> response = new HashMap<String, String>();
		String state = (String) body.get("state");
		
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", TOKEN);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		rest.exchange("https://barrigarest.wcaquino.me/reset", HttpMethod.GET, entity, Object.class);
		response.put("reset", "ok");
		
		switch (state) {
		case "I have an accountId":
			ResponseEntity<List> respAccount = rest.exchange("https://barrigarest.wcaquino.me/contas", HttpMethod.GET, entity, List.class);
			Map<String, Object> firstAccount = (Map<String, Object>) respAccount.getBody().get(0);
			String accountId = firstAccount.get("id").toString();
			response.put("accountId", accountId);
			break;

		default:
			break;
		}
		System.out.println(response);
		return new ResponseEntity<Map<String, String>>(response, HttpStatus.OK);
	}
}
