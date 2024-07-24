package com.github.jvalkeal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.shell.style.ThemeResolver;

public class ListBlock extends Block {

	private final List<String> content;

	public ListBlock(List<String> content) {
		this.content = new ArrayList<>(content);
	}

	@Override
	public List<String> resolveContent(ThemeResolver themeResolver, MarkdownSettings markdownSettings) {
		return content.stream()
			.map(c -> "  " + "- " + c)
			.collect(Collectors.toList());
		// return content;
	}

}
