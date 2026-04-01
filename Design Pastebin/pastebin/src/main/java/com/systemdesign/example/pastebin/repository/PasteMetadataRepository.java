package com.systemdesign.example.pastebin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.systemdesign.example.pastebin.model.PasteMetadata;

public interface PasteMetadataRepository extends JpaRepository<PasteMetadata, Long> {

	Optional<PasteMetadata> findByShortUrlHash(String shortUrlHash);

	boolean existsByShortUrlHash(String shortUrlHash);
}
