package com.systemdesign.example.pastebin.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pastebin_metadata")
public class PasteMetadata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "expire_at", nullable = false)
	private Instant expireAt;

	@Column(name = "s3_path", nullable = false, length = 512)
	private String s3Path;

	@Column(name = "short_url_hash", nullable = false, unique = true, length = 16)
	private String shortUrlHash;

	public Long getId() {
		return id;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(Instant expireAt) {
		this.expireAt = expireAt;
	}

	public String getS3Path() {
		return s3Path;
	}

	public void setS3Path(String s3Path) {
		this.s3Path = s3Path;
	}

	public String getShortUrlHash() {
		return shortUrlHash;
	}

	public void setShortUrlHash(String shortUrlHash) {
		this.shortUrlHash = shortUrlHash;
	}
}
