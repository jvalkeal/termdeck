package com.github.jvalkeal.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DeckSettings {

	public static final String KEY_AUTHOR = "author";
	public static final String KEY_SLIDECOUNT = "slideCount";
	public static final String KEY_ELAPSEDTIME = "elapsedTime";

	private String author;
	private String slideCount = "%s / %s";
	private String elapsedTime = "%2d:%02d:%02d";

	public static DeckSettings from(Map<String, List<String>> values) {
		DeckSettings settings = new DeckSettings();
		for (Entry<String, List<String>> entry : values.entrySet()) {
			switch (entry.getKey()) {
				case KEY_AUTHOR -> settings.bindAuthor(entry.getValue());
				case KEY_SLIDECOUNT -> settings.bindSlideCount(entry.getValue());
				case KEY_ELAPSEDTIME -> settings.bindElapsedTime(entry.getValue());
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

	private void bindElapsedTime(List<String> values) {
		if (values.size() == 0) {
			setElapsedTime(null);
		}
		else if (values.size() > 0) {
			setElapsedTime(values.getFirst());
		}
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
}
