package com.systemdesign.example.pastebin.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StorePasteRequest {

	private String text;

	@JsonProperty("expire_at")
	private Instant expireAt;

	@JsonProperty("ttl_seconds")
	private Long ttlSeconds;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Instant getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(Instant expireAt) {
		this.expireAt = expireAt;
	}

	public Long getTtlSeconds() {
		return ttlSeconds;
	}

	public void setTtlSeconds(Long ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
	}
}
