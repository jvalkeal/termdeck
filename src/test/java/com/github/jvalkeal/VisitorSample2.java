package com.github.jvalkeal;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Document;
import org.commonmark.node.Node;
import org.commonmark.node.ThematicBreak;
import org.commonmark.parser.Parser;

public class VisitorSample2 {

	static class MyVisitor extends AbstractVisitor {

		@Override
		public void visit(ThematicBreak thematicBreak) {
			super.visit(thematicBreak);
			System.out.println("XXX:" + thematicBreak);
		}
	}

	void parse() {
		String html = """
				hello

				---
				hello
				""";

		Parser parser = Parser.builder().build();
		Node node = parser.parse(html);
		MyVisitor myVisitor = new MyVisitor();
		node.accept(myVisitor);
	}

    public static void main(String[] args) {
		VisitorSample2 sample = new VisitorSample2();
		sample.parse();

	}

}
