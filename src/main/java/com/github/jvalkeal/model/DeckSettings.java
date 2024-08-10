package com.github.jvalkeal.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DeckSettings {

	public static final String KEY_AUTHOR = "author";
	public static final String KEY_SLIDECOUNT = "slideCount";

	private String author;
	private String slideCount = "%s / %s";

	public static DeckSettings from(Map<String, List<String>> values) {
		DeckSettings settings = new DeckSettings();
		for (Entry<String, List<String>> entry : values.entrySet()) {
			switch (entry.getKey()) {
				case KEY_AUTHOR -> settings.bindAuthor(entry.getValue());
				case KEY_SLIDECOUNT -> settings.bindSlideCount(entry.getValue());
			}
		}
		return settings;
	}

	private void bindAuthor(List<String> values) {
		if (values.size() > 0) {
			setAuthor(values.getFirst());
		}
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	private void bindSlideCount(List<String> values) {
		if (values.size() == 0) {
			setSlideCount(null);
		}
		else if (values.size() > 0) {
			setSlideCount(values.getFirst());
		}
	}

	public String getSlideCount() {
		return slideCount;
	}

	public void setSlideCount(String slideCount) {
		this.slideCount = slideCount;
	}
}
