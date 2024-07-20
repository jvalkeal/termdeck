package com.github.jvalkeal.model;

public abstract class MarkdownSettings {

	public final static String TAG_HEADING = "markdown-heading";

	public final static String TEMPLATE_HEADING = "template-heading";

	public String heading() {
		// return "default";
		// return "bold";
		// return "bold,fg:bright-cyan";
		return "bold,fg:red,bg:green";
	}

	public String templateHeading() {
		return "%s";
	}

	public String resolveTemplate(String template) {
		switch (template) {
			case TEMPLATE_HEADING:
				return templateHeading();
		}
		throw new IllegalArgumentException(String.format("Unknown tag '%s'", template));
	}

	public String resolveTag(String tag) {
		switch (tag) {
			case TAG_HEADING:
				return heading();
		}
		throw new IllegalArgumentException(String.format("Unknown tag '%s'", tag));
	}

	public static MarkdownSettings defaults() {
		return new DefaultMarkdownSettings();
	}

	private static class DefaultMarkdownSettings extends MarkdownSettings {
	}

}
