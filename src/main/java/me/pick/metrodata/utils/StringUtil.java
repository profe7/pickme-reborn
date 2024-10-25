package me.pick.metrodata.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Random;

public class StringUtil {
	public static String generateRandomEmail() {
		String[] domains = { "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "example.com" };
		Random random = new Random();

		int randomLength = 10 + random.nextInt(10); // Panjang acak untuk bagian sebelum "@" (minimal 10 karakter)
		StringBuilder randomPart = new StringBuilder();
		for (int i = 0; i < randomLength; i++) {
			char randomChar = (char) (97 + random.nextInt(26)); // 97 is ASCII code for 'a', generate lowercase letters
			randomPart.append(randomChar);
		}

		String randomDomain = domains[random.nextInt(domains.length)];

		String randomEmail = randomPart.toString() + "@" + randomDomain;

		return randomEmail;
	}

	public static String decodeUrlParameter(String param) {
		try {
			String decodedValue = URLDecoder.decode(param, "UTF-8");
			return (decodedValue);
		} catch (UnsupportedEncodingException e) {
			throw new HttpServerErrorException (HttpStatus.INTERNAL_SERVER_ERROR, "Failed to decode uri");
		}
	}

	public static String capitalizeString(String input) {
		if (input == null || input.isEmpty()) {
			return "";
		}
		return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
	}
}