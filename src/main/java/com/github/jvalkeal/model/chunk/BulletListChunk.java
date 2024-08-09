package com.github.jvalkeal.model.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jvalkeal.model.Chunk;
import com.github.jvalkeal.model.MarkdownSettings;

import org.springframework.shell.style.ThemeResolver;

public class BulletListChunk extends Chunk {

	private final List<String> content;

	public BulletListChunk(List<String> content) {
		this.content = new ArrayList<>(content);
	}

	@Override
	public List<String> resolveContent(ThemeResolver themeResolver, MarkdownSettings markdownSettings) {
		return content.stream()
			.map(c -> "  " + "- " + c)
			.collect(Collectors.toList());
	}

}
