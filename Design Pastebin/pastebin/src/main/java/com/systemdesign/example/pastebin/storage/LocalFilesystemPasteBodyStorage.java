package com.systemdesign.example.pastebin.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocalFilesystemPasteBodyStorage implements PasteBodyStorage {

	private final Path root;

	public LocalFilesystemPasteBodyStorage(@Value("${pastebin.blob.storage-root}") String storageRoot) {
		this.root = Path.of(storageRoot).toAbsolutePath().normalize();
	}

	@Override
	public String store(String text) {
		String key = "pastes/" + UUID.randomUUID();
		Path target = root.resolve(key).normalize();
		if (!target.startsWith(root)) {
			throw new IllegalStateException("Invalid storage path");
		}
		try {
			Files.createDirectories(target.getParent());
			Files.writeString(target, text, StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			throw new IllegalStateException("Failed to store paste body", e);
		}
		return key;
	}

	@Override
	public String load(String s3Path) {
		Path target = root.resolve(s3Path).normalize();
		if (!target.startsWith(root)) {
			throw new IllegalArgumentException("Invalid blob key");
		}
		try {
			return Files.readString(target, StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			throw new IllegalStateException("Failed to read paste body", e);
		}
	}
}
