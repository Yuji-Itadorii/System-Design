package com.systemdesign.example.pastebin.service;

import java.time.Clock;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.systemdesign.example.pastebin.dto.PasteReadResponse;
import com.systemdesign.example.pastebin.dto.StorePasteRequest;
import com.systemdesign.example.pastebin.dto.StorePasteResponse;
import com.systemdesign.example.pastebin.model.PasteMetadata;
import com.systemdesign.example.pastebin.repository.PasteMetadataRepository;
import com.systemdesign.example.pastebin.storage.PasteBodyStorage;

@Service
public class PasteService {

	private final PasteMetadataRepository metadataRepository;
	private final PasteBodyStorage bodyStorage;
	private final ShortUrlHashGenerator hashGenerator;
	private final String publicBaseUrl;
	private final Clock clock;

	public PasteService(
			PasteMetadataRepository metadataRepository,
			PasteBodyStorage bodyStorage,
			ShortUrlHashGenerator hashGenerator,
			@Value("${pastebin.public-base-url}") String publicBaseUrl,
			Clock clock) {
		this.metadataRepository = metadataRepository;
		this.bodyStorage = bodyStorage;
		this.hashGenerator = hashGenerator;
		this.publicBaseUrl = trimTrailingSlash(publicBaseUrl);
		this.clock = clock;
	}

	@Transactional
	public StorePasteResponse store(StorePasteRequest request) {
		if (request.getText() == null || request.getText().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "text is required");
		}
		Instant now = clock.instant();
		Instant expireAt = resolveExpireAt(request, now);
		if (!expireAt.isAfter(now)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "expire_at must be in the future");
		}

		String s3Path = bodyStorage.store(request.getText());
		String hash = hashGenerator.nextUniqueHash();

		PasteMetadata meta = new PasteMetadata();
		meta.setCreatedAt(now);
		meta.setExpireAt(expireAt);
		meta.setS3Path(s3Path);
		meta.setShortUrlHash(hash);
		metadataRepository.save(meta);

		String shortUrl = publicBaseUrl + "/" + hash;
		return new StorePasteResponse(shortUrl, "created");
	}

	@Transactional(readOnly = true)
	public PasteReadResponse readByHash(String shortUrlHash) {
		PasteMetadata meta = metadataRepository.findByShortUrlHash(shortUrlHash)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paste not found"));

		if (!meta.getExpireAt().isAfter(clock.instant())) {
			throw new ResponseStatusException(HttpStatus.GONE, "Paste has expired");
		}

		String text = bodyStorage.load(meta.getS3Path());
		return new PasteReadResponse(meta.getCreatedAt(), meta.getExpireAt(), text);
	}

	private static Instant resolveExpireAt(StorePasteRequest request, Instant now) {
		if (request.getExpireAt() != null) {
			return request.getExpireAt();
		}
		if (request.getTtlSeconds() != null) {
			if (request.getTtlSeconds() <= 0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ttl_seconds must be positive");
			}
			return now.plusSeconds(request.getTtlSeconds());
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide expire_at or ttl_seconds");
	}

	private static String trimTrailingSlash(String base) {
		if (base == null || base.isEmpty()) {
			return "";
		}
		return base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
	}
}
