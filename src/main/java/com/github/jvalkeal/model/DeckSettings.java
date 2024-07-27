package com.github.jvalkeal.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DeckSettings {

	private String author;
	public static final String KEY_AUTHOR = "author";

	public static DeckSettings from(Map<String, List<String>> values) {
		DeckSettings settings = new DeckSettings();
		for (Entry<String, List<String>> entry : values.entrySet()) {
			switch (entry.getKey()) {
				case KEY_AUTHOR -> settings.bindAuthor(entry.getValue());
			}
		}
		return settings;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	private void bindAuthor(List<String> values) {
		if (values.size() > 0) {
			setAuthor(values.getFirst());
		}
	}
}
