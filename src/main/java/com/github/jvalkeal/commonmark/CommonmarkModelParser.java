package com.github.jvalkeal.commonmark;

import java.util.List;

import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.model.ModelParser;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

public class CommonmarkModelParser implements ModelParser {

	@Override
	public Deck parse(String content) {
		List<Extension> extensions = List.of(TablesExtension.create());
		Parser parser = Parser.builder()
			.extensions(extensions)
			.build();
		Node document = parser.parse(content);
		MarkdownVisitor visitor = new MarkdownVisitor();
		document.accept(visitor);
		return visitor.getDeck();
	}

}
