package com.github.jvalkeal.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.shell.style.ThemeResolver;

public class TextBlock extends Block {

	private final List<String> content;

	public TextBlock(List<String> content) {
		// super(content);
		this.content = new ArrayList<>(content);
	}

	@Override
	public List<String> resolveContent(ThemeResolver themeResolver, MarkdownSettings markdownSettings) {
		return content;
	}

}
