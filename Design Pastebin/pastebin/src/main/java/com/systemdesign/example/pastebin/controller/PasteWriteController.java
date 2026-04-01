package com.systemdesign.example.pastebin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systemdesign.example.pastebin.dto.StorePasteRequest;
import com.systemdesign.example.pastebin.dto.StorePasteResponse;
import com.systemdesign.example.pastebin.service.PasteService;

@RestController
@RequestMapping("/api/v1")
public class PasteWriteController {

	private final PasteService pasteService;

	public PasteWriteController(PasteService pasteService) {
		this.pasteService = pasteService;
	}

	@PostMapping("/store")
	@ResponseStatus(HttpStatus.CREATED)
	public StorePasteResponse store(@RequestBody StorePasteRequest request) {
		return pasteService.store(request);
	}
}
