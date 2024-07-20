package com.github.jvalkeal.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import org.springframework.shell.style.ThemeResolver;
import org.springframework.shell.style.ThemeResolver.ResolvedValues;

public class HeadingBlock extends Block {

	private String header;
	private int level;

	public HeadingBlock(String content, int level) {
		// super(Collections.emptyList());
		header = content;
	}

	public List<String> resolveContent(ThemeResolver themeResolver, MarkdownSettings markdownSettings) {
		String tag = markdownSettings.resolveTag(MarkdownSettings.TAG_HEADING);
		String template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_HEADING);
		AttributedStyle style = themeResolver.resolveStyle(tag);
		// ResolvedValues values = themeResolver.resolveValues(style);
		String format = String.format(template, header);
		AttributedString as = new AttributedString(format, style);
		String content = as.toAnsi();
		// String prefix = "#".repeat(level + 1);
		// String content = prefix + " " + header;
		return Arrays.asList(content, "");
	}


	// @Override
	// public List<String> content() {
	// 	String prefix = "#".repeat(level + 1);
	// 	String content = prefix + " " + header;
	// 	return Arrays.asList(content, "");
	// }

}
