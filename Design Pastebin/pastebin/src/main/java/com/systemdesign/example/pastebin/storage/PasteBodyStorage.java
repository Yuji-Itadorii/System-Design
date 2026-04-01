package com.systemdesign.example.pastebin.storage;

/**
 * Stores paste bodies keyed by {@code s3Path} (S3 object key in production; local file path segment here).
 */
public interface PasteBodyStorage {

	/**
	 * Persists UTF-8 text and returns the storage key (same semantics as {@code s3_path} in metadata).
	 */
	String store(String text);

	String load(String s3Path);
}
