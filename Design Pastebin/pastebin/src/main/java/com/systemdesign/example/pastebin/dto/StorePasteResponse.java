package com.systemdesign.example.pastebin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StorePasteResponse {

	@JsonProperty("short_url")
	private final String shortUrl;

	private final String status;

	public StorePasteResponse(String shortUrl, String status) {
		this.shortUrl = shortUrl;
		this.status = status;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public String getStatus() {
		return status;
	}
}
