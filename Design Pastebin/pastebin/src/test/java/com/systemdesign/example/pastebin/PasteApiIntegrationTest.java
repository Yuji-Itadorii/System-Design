package com.systemdesign.example.pastebin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class PasteApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void storeThenReadByPath() throws Exception {
		String body = """
				{"text":"hello world","ttl_seconds":3600}
				""";
		MvcResult created = mockMvc.perform(post("/api/v1/store")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.short_url").isString())
				.andExpect(jsonPath("$.status").value("created"))
				.andReturn();

		String shortUrl = extractShortUrl(created.getResponse().getContentAsString());
		String hash = shortUrl.substring(shortUrl.lastIndexOf('/') + 1);
		assertThat(hash).hasSize(8);

		mockMvc.perform(get("/" + hash))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.text").value("hello world"))
				.andExpect(jsonPath("$.created_at").exists())
				.andExpect(jsonPath("$.expire_at").exists());
	}

	@Test
	void readByQueryParam() throws Exception {
		String body = """
				{"text":"q","expire_at":"2099-01-01T00:00:00Z"}
				""";
		MvcResult created = mockMvc.perform(post("/api/v1/store")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
				.andExpect(status().isCreated())
				.andReturn();
		String shortUrlFull = extractShortUrl(created.getResponse().getContentAsString());
		String hash = shortUrlFull.substring(shortUrlFull.lastIndexOf('/') + 1);

		mockMvc.perform(get("/read").param("hash", hash))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.text").value("q"));
	}

	private static String extractShortUrl(String json) {
		String key = "\"short_url\":\"";
		int i = json.indexOf(key);
		assertThat(i).isGreaterThanOrEqualTo(0);
		int start = i + key.length();
		int end = json.indexOf('"', start);
		assertThat(end).isGreaterThan(start);
		return json.substring(start, end);
	}
}
