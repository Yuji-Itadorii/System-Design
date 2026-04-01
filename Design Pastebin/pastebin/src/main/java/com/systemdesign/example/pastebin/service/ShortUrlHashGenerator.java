package com.systemdesign.example.pastebin.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.systemdesign.example.pastebin.repository.PasteMetadataRepository;

@Component
public class ShortUrlHashGenerator {

	private static final char[] ALPHANUM = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ23456789".toCharArray();
	private static final int LENGTH = 8;

	private final SecureRandom random = new SecureRandom();
	private final PasteMetadataRepository repository;

	public ShortUrlHashGenerator(PasteMetadataRepository repository) {
		this.repository = repository;
	}

	public String nextUniqueHash() {
		for (int attempt = 0; attempt < 32; attempt++) {
			String candidate = randomToken();
			if (!repository.existsByShortUrlHash(candidate)) {
				return candidate;
			}
		}
		throw new IllegalStateException("Could not allocate unique short URL hash");
	}

	private String randomToken() {
		char[] buf = new char[LENGTH];
		for (int i = 0; i < LENGTH; i++) {
			buf[i] = ALPHANUM[random.nextInt(ALPHANUM.length)];
		}
		return new String(buf);
	}
}
