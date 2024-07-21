package com.github.jvalkeal.model;

public abstract class MarkdownSettings {

	public final static String STYLE_HEADING_TEXT = "style-heading-text";
	public final static String STYLE_HEADING_PREFIX = "style-heading-prefix";
	public final static String STYLE_HEADING_POSTFIX = "style-heading-postfix";
	public final static String TEMPLATE_HEADING_TEXT = "template-heading-text";
	public final static String TEMPLATE_HEADING_PREFIX = "template-heading-prefix";
	public final static String TEMPLATE_HEADING_POSTFIX = "template-heading-postfix";

	public final static String STYLE_H1_TEXT = "style-h1-text";
	public final static String STYLE_H1_PREFIX = "style-h1-prefix";
	public final static String STYLE_H1_POSTFIX = "style-h1-postfix";
	public final static String TEMPLATE_H1_TEXT = "template-h1-text";
	public final static String TEMPLATE_H1_PREFIX = "template-h1-prefix";
	public final static String TEMPLATE_H1_POSTFIX = "template-h1-postfix";

	public final static String STYLE_H2_TEXT = "style-h2-text";
	public final static String STYLE_H2_PREFIX = "style-h2-prefix";
	public final static String STYLE_H2_POSTFIX = "style-h2-postfix";
	public final static String TEMPLATE_H2_TEXT = "template-h2-text";
	public final static String TEMPLATE_H2_PREFIX = "template-h2-prefix";
	public final static String TEMPLATE_H2_POSTFIX = "template-h2-postfix";

	public final static String STYLE_H3_TEXT = "style-h3-text";
	public final static String STYLE_H3_PREFIX = "style-h3-prefix";
	public final static String STYLE_H3_POSTFIX = "style-h3-postfix";
	public final static String TEMPLATE_H3_TEXT = "template-h3-text";
	public final static String TEMPLATE_H3_PREFIX = "template-h3-prefix";
	public final static String TEMPLATE_H3_POSTFIX = "template-h3-postfix";

	public final static String STYLE_H4_TEXT = "style-h4-text";
	public final static String STYLE_H4_PREFIX = "style-h4-prefix";
	public final static String STYLE_H4_POSTFIX = "style-h4-postfix";
	public final static String TEMPLATE_H4_TEXT = "template-h4-text";
	public final static String TEMPLATE_H4_PREFIX = "template-h4-prefix";
	public final static String TEMPLATE_H4_POSTFIX = "template-h4-postfix";

	public final static String STYLE_H5_TEXT = "style-h5-text";
	public final static String STYLE_H5_PREFIX = "style-h5-prefix";
	public final static String STYLE_H5_POSTFIX = "style-h5-postfix";
	public final static String TEMPLATE_H5_TEXT = "template-h5-text";
	public final static String TEMPLATE_H5_PREFIX = "template-h5-prefix";
	public final static String TEMPLATE_H5_POSTFIX = "template-h5-postfix";

	public final static String STYLE_H6_TEXT = "style-h6-text";
	public final static String STYLE_H6_PREFIX = "style-h6-prefix";
	public final static String STYLE_H6_POSTFIX = "style-h6-postfix";
	public final static String TEMPLATE_H6_TEXT = "template-h6-text";
	public final static String TEMPLATE_H6_PREFIX = "template-h6-prefix";
	public final static String TEMPLATE_H6_POSTFIX = "template-h6-postfix";

		// return "bold";
		// return "bold,fg:bright-cyan";
		// return "bold,fg:red,bg:green";

	public String styleHeadingText() {
		return "default";
	}

	public String styleHeadingPrefix() {
		return "default";
	}

	public String styleHeadingPostfix() {
		return "default";
	}

	public String templateHeadingText() {
		return "%s";
	}

	public String templateHeadingPrefix() {
		return "";
	}

	public String templateHeadingPostfix() {
		return "";
	}

	public String styleH1Text() {
		return "bold";
	}

	public String styleH1Prefix() {
		return "fg:red";
	}

	public String styleH1Postfix() {
		return "default";
	}

	public String templateH1Text() {
		return "%s";
	}

	public String templateH1Prefix() {
		return "██ ";
	}

	public String templateH1Postfix() {
		return "";
	}

	public String styleH2Text() {
		return "default";
	}

	public String styleH2Prefix() {
		return "default";
	}

	public String styleH2Postfix() {
		return "default";
	}

	public String templateH2Text() {
		return "%s";
	}

	public String templateH2Prefix() {
		return "▓▓▓ ";
	}

	public String templateH2Postfix() {
		return "";
	}

	public String styleH3Text() {
		return "default";
	}

	public String styleH3Prefix() {
		return "default";
	}

	public String styleH3Postfix() {
		return "default";
	}

	public String templateH3Text() {
		return "%s";
	}

	public String templateH3Prefix() {
		return "▒▒▒▒ ";
	}

	public String templateH3Postfix() {
		return "";
	}

	public String styleH4Text() {
		return "default";
	}

	public String styleH4Prefix() {
		return "default";
	}

	public String styleH4Postfix() {
		return "default";
	}

	public String templateH4Text() {
		return "%s";
	}

	public String templateH4Prefix() {
		return "░░░░░ ";
	}

	public String templateH4Postfix() {
		return "";
	}

	public String styleH5Text() {
		return "default";
	}

	public String styleH5Prefix() {
		return "default";
	}

	public String styleH5Postfix() {
		return "default";
	}

	public String templateH5Text() {
		return "%s";
	}

	public String templateH5Prefix() {
		return "";
	}

	public String templateH5Postfix() {
		return "";
	}

	public String styleH6Text() {
		return "default";
	}

	public String styleH6Prefix() {
		return "default";
	}

	public String styleH6Postfix() {
		return "default";
	}

	public String templateH6Text() {
		return "%s";
	}

	public String templateH6Prefix() {
		return "";
	}

	public String templateH6Postfix() {
		return "";
	}

	public String resolveStyle(String styleTag) {
		switch (styleTag) {
			case STYLE_HEADING_TEXT:
				return styleHeadingText();
			case STYLE_HEADING_PREFIX:
				return styleHeadingPrefix();
			case STYLE_HEADING_POSTFIX:
				return styleHeadingPostfix();
			case STYLE_H1_TEXT:
				return styleH1Text();
			case STYLE_H1_PREFIX:
				return styleH1Prefix();
			case STYLE_H1_POSTFIX:
				return styleH1Postfix();
			case STYLE_H2_TEXT:
				return styleH2Text();
			case STYLE_H2_PREFIX:
				return styleH2Prefix();
			case STYLE_H2_POSTFIX:
				return styleH2Postfix();
			case STYLE_H3_TEXT:
				return styleH3Text();
			case STYLE_H3_PREFIX:
				return styleH3Prefix();
			case STYLE_H3_POSTFIX:
				return styleH3Postfix();
			case STYLE_H4_TEXT:
				return styleH4Text();
			case STYLE_H4_PREFIX:
				return styleH4Prefix();
			case STYLE_H4_POSTFIX:
				return styleH4Postfix();
			case STYLE_H5_TEXT:
				return styleH5Text();
			case STYLE_H5_PREFIX:
				return styleH5Prefix();
			case STYLE_H5_POSTFIX:
				return styleH5Postfix();
			case STYLE_H6_TEXT:
				return styleH6Text();
			case STYLE_H6_PREFIX:
				return styleH6Prefix();
			case STYLE_H6_POSTFIX:
				return styleH6Postfix();
		}
		throw new IllegalArgumentException(String.format("Unknown style tag '%s'", styleTag));
	}

	public String resolveTemplate(String templateTag) {
		switch (templateTag) {
			case TEMPLATE_HEADING_TEXT:
				return templateHeadingText();
			case TEMPLATE_HEADING_PREFIX:
				return templateHeadingPrefix();
			case TEMPLATE_HEADING_POSTFIX:
				return templateHeadingPostfix();
			case TEMPLATE_H1_TEXT:
				return templateH1Text();
			case TEMPLATE_H1_PREFIX:
				return templateH1Prefix();
			case TEMPLATE_H1_POSTFIX:
				return templateH1Postfix();
			case TEMPLATE_H2_TEXT:
				return templateH2Text();
			case TEMPLATE_H2_PREFIX:
				return templateH2Prefix();
			case TEMPLATE_H2_POSTFIX:
				return templateH2Postfix();
			case TEMPLATE_H3_TEXT:
				return templateH3Text();
			case TEMPLATE_H3_PREFIX:
				return templateH3Prefix();
			case TEMPLATE_H3_POSTFIX:
				return templateH3Postfix();
			case TEMPLATE_H4_TEXT:
				return templateH4Text();
			case TEMPLATE_H4_PREFIX:
				return templateH4Prefix();
			case TEMPLATE_H4_POSTFIX:
				return templateH4Postfix();
			case TEMPLATE_H5_TEXT:
				return templateH5Text();
			case TEMPLATE_H5_PREFIX:
				return templateH5Prefix();
			case TEMPLATE_H5_POSTFIX:
				return templateH5Postfix();
			case TEMPLATE_H6_TEXT:
				return templateH6Text();
			case TEMPLATE_H6_PREFIX:
				return templateH6Prefix();
			case TEMPLATE_H6_POSTFIX:
				return templateH6Postfix();
		}
		throw new IllegalArgumentException(String.format("Unknown template tag '%s'", templateTag));
	}

	public static MarkdownSettings defaults() {
		return new DefaultMarkdownSettings();
	}

	private static class DefaultMarkdownSettings extends MarkdownSettings {
	}

}
