package com.github.jvalkeal.model.chunk;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.jvalkeal.model.Chunk;
import com.github.jvalkeal.model.MarkdownSettings;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import org.springframework.shell.style.ThemeResolver;
import org.springframework.shell.style.ThemeResolver.ResolvedValues;

public class HeadingChunk extends Chunk {

	private String header;
	private int level;

	public HeadingChunk(String content, int level) {
		// super(Collections.emptyList());
		header = content;
		this.level = level;
	}

	public List<String> resolveContent(ResolveContentContext context) {
		ThemeResolver themeResolver = context.themeResolver();
		MarkdownSettings markdownSettings = context.markdownSettings();
		String prefixStyle = deducePrefixStyle(markdownSettings);
		String textStyle = deduceTextStyle(markdownSettings);
		String prefixTemplate = deducePrefixTemplate(markdownSettings);
		String textTemplate = deduceTextTemplate(markdownSettings);
		AttributedStyle as1 = themeResolver.resolveStyle(prefixStyle);
		AttributedStyle as2 = themeResolver.resolveStyle(textStyle);
		String f1 = String.format(prefixTemplate, header);
		String f2 = String.format(textTemplate, header);
		AttributedString astr1 = new AttributedString(f1, as1);
		AttributedString astr2 = new AttributedString(f2, as2);
		return Arrays.asList(astr1.toAnsi() + astr2.toAnsi(), "");
		// String tag = markdownSettings.resolveStyle(MarkdownSettings.STYLE_HEADING_TEXT);
		// AttributedStyle style = themeResolver.resolveStyle(tag);

		// String template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_HEADING_TEXT);
		// String format = String.format(template, header);

		// AttributedString as = new AttributedString(format, style);

		// String content = as.toAnsi();
		// return Arrays.asList(content, "");
		// return null;
	}

	private String deduceTextStyle(MarkdownSettings markdownSettings) {
		String style = null;
		if (level == 1) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H1_TEXT);
		}
		else if (level == 2) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H2_TEXT);
		}
		else if (level == 3) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H3_TEXT);
		}
		else if (level == 4) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H4_TEXT);
		}
		else if (level == 5) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H5_TEXT);
		}
		else if (level == 6) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H6_TEXT);
		}
		if (style == null) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_HEADING_TEXT);
		}
		if (style == null) {
			style = "default";
		}
		return style;
	}

	private String deducePrefixStyle(MarkdownSettings markdownSettings) {
		String style = null;
		if (level == 1) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H1_PREFIX);
		}
		else if (level == 2) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H2_PREFIX);
		}
		else if (level == 3) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H3_PREFIX);
		}
		else if (level == 4) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H4_PREFIX);
		}
		else if (level == 5) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H5_PREFIX);
		}
		else if (level == 6) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_H6_PREFIX);
		}
		if (style == null) {
			style = markdownSettings.resolveStyle(MarkdownSettings.STYLE_HEADING_PREFIX);
		}
		if (style == null) {
			style = "default";
		}
		return style;
	}

	private String deduceTextTemplate(MarkdownSettings markdownSettings) {
		String template = null;
		if (level == 1) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H1_TEXT);
		}
		else if (level == 2) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H2_TEXT);
		}
		else if (level == 3) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H3_TEXT);
		}
		else if (level == 4) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H4_TEXT);
		}
		else if (level == 5) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H5_TEXT);
		}
		else if (level == 6) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H6_TEXT);
		}
		if (template == null) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_HEADING_TEXT);
		}
		if (template == null) {
			template = "";
		}
		return template;
	}

	private String deducePrefixTemplate(MarkdownSettings markdownSettings) {
		String template = null;
		if (level == 1) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H1_PREFIX);
		}
		else if (level == 2) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H2_PREFIX);
		}
		else if (level == 3) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H3_PREFIX);
		}
		else if (level == 4) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H4_PREFIX);
		}
		else if (level == 5) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H5_PREFIX);
		}
		else if (level == 6) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_H6_PREFIX);
		}
		if (template == null) {
			template = markdownSettings.resolveTemplate(MarkdownSettings.TEMPLATE_HEADING_PREFIX);
		}
		if (template == null) {
			template = "";
		}
		return template;
	}


	// @Override
	// public List<String> content() {
	// 	String prefix = "#".repeat(level + 1);
	// 	String content = prefix + " " + header;
	// 	return Arrays.asList(content, "");
	// }

}
