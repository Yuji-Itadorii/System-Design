package com.systemdesign.example.pastebin.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasteReadResponse {

	@JsonProperty("created_at")
	private final Instant createdAt;

	@JsonProperty("expire_at")
	private final Instant expireAt;

	private final String text;

	public PasteReadResponse(Instant createdAt, Instant expireAt, String text) {
		this.createdAt = createdAt;
		this.expireAt = expireAt;
		this.text = text;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getExpireAt() {
		return expireAt;
	}

	public String getText() {
		return text;
	}
}
