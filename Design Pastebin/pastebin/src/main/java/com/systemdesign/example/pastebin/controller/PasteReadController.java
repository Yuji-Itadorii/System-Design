package com.systemdesign.example.pastebin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.systemdesign.example.pastebin.dto.PasteReadResponse;
import com.systemdesign.example.pastebin.service.PasteService;

@RestController
public class PasteReadController {

	private final PasteService pasteService;

	public PasteReadController(PasteService pasteService) {
		this.pasteService = pasteService;
	}

	/**
	 * Path-style short link: {@code GET /{shortUrlHash}}. Pattern avoids collisions with routes like {@code /h2-console}.
	 */
	@GetMapping("/{shortUrlHash:[a-zA-Z0-9]{8}}")
	public PasteReadResponse readByPath(@PathVariable String shortUrlHash) {
		return pasteService.readByHash(shortUrlHash);
	}

	@GetMapping(value = "/read", params = "hash")
	public PasteReadResponse readByQuery(@RequestParam("hash") String shortUrlHash) {
		return pasteService.readByHash(shortUrlHash);
	}
}
