package com.github.jvalkeal;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.ast.ThematicBreak;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.ast.VisitHandler;
import org.jetbrains.annotations.NotNull;

public class VisitorSample {

	NodeVisitor visitor = new NodeVisitor(
		// new VisitHandler<>(Node.class, this::visit)
	) {
		@Override
		protected void processNode(Node node, boolean withChildren, java.util.function.BiConsumer<Node,com.vladsch.flexmark.util.ast.Visitor<Node>> processor) {
			System.out.println("Visiting node:" + node);
			super.processNode(node, withChildren, processor);
		};
	};

	void xxx() {
		// Heading
		// Text
		// Paragraph
		// ThematicBreak
	}

	// void visit(Node node) {
	// 	System.out.println("Visiting node:" + node);
	// 	System.out.println(node);
	// }

	void parse() {
		String html = """
				---
				Key1: Value1
				Key2: Value2

				---

				# Slide1
				hello from slide 1

				---
				# Slide2
				hello1 from slide 2
				hello2 from slide 2
				""";

		Parser parser = Parser.builder().build();
		Document document = parser.parse(html);
		visitor.visit(document);
	}

    public static void main(String[] args) {
		VisitorSample sample = new VisitorSample();
		sample.parse();

	}

}
