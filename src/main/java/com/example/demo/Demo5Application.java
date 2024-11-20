package com.example.demo;

import com.sun.tools.javac.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Scanner;

@SpringBootApplication
public class Demo5Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Demo5Application.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		RestTemplate restTemplate = new RestTemplate();
		String baseUrl = "http://127.0.0.1:8001";

		while (true) {
			String[] cmd = scanner.nextLine().split(" ");
			if (cmd[0].equals("create")) {
				String indexName = cmd[1];
				createIndex(restTemplate, baseUrl, indexName);
			} else if (cmd[0].equals("upload")) {
				String indexName = cmd[1];
				String title = cmd[2];
				String path = String.join(" ", Arrays.copyOfRange(cmd, 3, cmd.length));
				String content = getHwpText(path);
				uploadIndex(restTemplate, baseUrl, indexName, title, content);
			} else {
				String indexName = cmd[1];
				String text = String.join(" ", Arrays.copyOfRange(cmd, 2, cmd.length));
				chat(restTemplate, baseUrl, indexName, text);
			}
		}
	}

	public void createIndex(RestTemplate restTemplate, String baseUrl, String indexName) {
		String url = baseUrl + "/create?index_name=" + indexName;
		ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
		if (response.getBody() != null && response.getBody().equals("true")) {
			System.out.println("Index created successfully");
		}
	}

	public void uploadIndex(RestTemplate restTemplate, String baseUrl, String indexName, String title, String content) {
		String url = baseUrl + "/upload";
		String requestBody = "index_name=" + indexName + "&title=" + title + "&content=" + content;
		ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);
		if (response.getBody() != null && response.getBody().equals("true")) {
			System.out.println("Index uploaded successfully");
		}
	}

	public void chat(RestTemplate restTemplate, String baseUrl, String indexName, String text) {
		String url = baseUrl + "/chat";
		String requestBody = "index_name=" + indexName + "&text=" + text;
		ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);
		if (response.getBody() != null) {
			System.out.println("\nyou: " + text);
			System.out.println("\ncat-gpt: " + response.getBody() + "\n");
		}
	}

	public String getHwpText(String path) {
		// 파일에서 텍스트를 읽어오는 로직 구현
		return "sample content"; // 임시로 샘플 컨텐츠 반환
	}
}
