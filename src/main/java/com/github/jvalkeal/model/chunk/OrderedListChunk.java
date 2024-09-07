package com.github.jvalkeal.model.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.github.jvalkeal.model.Chunk;
import com.github.jvalkeal.model.MarkdownSettings;

import org.springframework.shell.style.ThemeResolver;

public class OrderedListChunk extends Chunk {

	private final List<String> content;

	public OrderedListChunk(List<String> content) {
		this.content = new ArrayList<>(content);
	}

	@Override
	public List<String> resolveContent(ResolveContentContext context) {
		AtomicInteger index = new AtomicInteger(1);
		return content.stream()
			.map(c -> String.format("  %s. %s", index.getAndIncrement(), c))
			.collect(Collectors.toList());
	}

}
