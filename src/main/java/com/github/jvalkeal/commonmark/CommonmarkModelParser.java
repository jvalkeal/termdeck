package com.github.jvalkeal.commonmark;

import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.model.ModelParser;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

public class CommonmarkModelParser implements ModelParser {

	@Override
	public Deck parse(String content) {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(content);
		MarkdownVisitor visitor = new MarkdownVisitor();
		document.accept(visitor);
		return visitor.getDeck();
	}

}
