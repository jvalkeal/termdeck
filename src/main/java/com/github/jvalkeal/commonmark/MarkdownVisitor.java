package com.github.jvalkeal.commonmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.jvalkeal.model.Block;
import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.model.Slide;
import org.commonmark.node.BlockQuote;
import org.commonmark.node.BulletList;
import org.commonmark.node.Code;
import org.commonmark.node.CustomBlock;
import org.commonmark.node.CustomNode;
import org.commonmark.node.Document;
import org.commonmark.node.Emphasis;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.HardLineBreak;
import org.commonmark.node.Heading;
import org.commonmark.node.HtmlBlock;
import org.commonmark.node.HtmlInline;
import org.commonmark.node.Image;
import org.commonmark.node.IndentedCodeBlock;
import org.commonmark.node.Link;
import org.commonmark.node.LinkReferenceDefinition;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.node.Paragraph;
import org.commonmark.node.SoftLineBreak;
import org.commonmark.node.StrongEmphasis;
import org.commonmark.node.Text;
import org.commonmark.node.ThematicBreak;
import org.commonmark.node.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarkdownVisitor implements Visitor {

	private final Logger log = LoggerFactory.getLogger(MarkdownVisitor.class);

	@Override
	public void visit(BlockQuote blockQuote) {
		visitChildren(blockQuote);
	}

	@Override
	public void visit(BulletList bulletList) {
		visitChildren(bulletList);
	}

	@Override
	public void visit(Code code) {
		visitChildren(code);
	}

	private void enterDocument(Document document) {
		log.debug("Enter {}", document);
		startSlide();
	}

	private void exitDocument(Document document) {
		log.debug("Exit {}", document);
		endSlide();
	}

	@Override
	public void visit(Document document) {
		enterDocument(document);
		visitChildren(document);
		exitDocument(document);
	}

	@Override
	public void visit(Emphasis emphasis) {
		visitChildren(emphasis);
	}

	@Override
	public void visit(FencedCodeBlock fencedCodeBlock) {
		visitChildren(fencedCodeBlock);
	}

	@Override
	public void visit(HardLineBreak hardLineBreak) {
		visitChildren(hardLineBreak);
	}

	private void enterHeading(Heading heading) {
		log.debug("Enter {}", heading);
		startBlock();
	}

	private void exitHeading(Heading heading) {
		log.debug("Exit {}", heading);
		endBlock();
	}

	@Override
	public void visit(Heading heading) {
		enterHeading(heading);
		visitChildren(heading);
		exitHeading(heading);
	}

	private void enterThematicBreak(ThematicBreak thematicBreak) {
		log.debug("Enter {}", thematicBreak);
	}

	private void exitThematicBreak(ThematicBreak thematicBreak) {
		log.debug("Exit {}", thematicBreak);
		endSlide();
		startSlide();
	}

	@Override
	public void visit(ThematicBreak thematicBreak) {
		enterThematicBreak(thematicBreak);
		visitChildren(thematicBreak);
		exitThematicBreak(thematicBreak);
	}

	@Override
	public void visit(HtmlInline htmlInline) {
		visitChildren(htmlInline);
	}

	@Override
	public void visit(HtmlBlock htmlBlock) {
		visitChildren(htmlBlock);
	}

	@Override
	public void visit(Image image) {
		visitChildren(image);
	}

	@Override
	public void visit(IndentedCodeBlock indentedCodeBlock) {
		visitChildren(indentedCodeBlock);
	}

	@Override
	public void visit(Link link) {
		visitChildren(link);
	}

	@Override
	public void visit(ListItem listItem) {
		visitChildren(listItem);
	}

	@Override
	public void visit(OrderedList orderedList) {
		visitChildren(orderedList);
	}

	private void enterParagraph(Paragraph paragraph) {
		log.debug("Enter {}", paragraph);
		startBlock();
	}

	private void exitParagraph(Paragraph paragraph) {
		log.debug("Exit {}", paragraph);
		endBlock();
	}

	@Override
	public void visit(Paragraph paragraph) {
		enterParagraph(paragraph);
		visitChildren(paragraph);
		exitParagraph(paragraph);
	}

	@Override
	public void visit(SoftLineBreak softLineBreak) {
		visitChildren(softLineBreak);
	}

	@Override
	public void visit(StrongEmphasis strongEmphasis) {
		visitChildren(strongEmphasis);
	}

	private void enterText(Text text) {
		log.debug("Enter {}", text);
		append(text.getLiteral());

	}

	private void exitText(Text text) {
		log.debug("Exit {}", text);

	}

	@Override
	public void visit(Text text) {
		enterText(text);
		visitChildren(text);
		exitText(text);
	}

	@Override
	public void visit(LinkReferenceDefinition linkReferenceDefinition) {
		visitChildren(linkReferenceDefinition);
	}

	@Override
	public void visit(CustomBlock customBlock) {
		visitChildren(customBlock);
	}

	@Override
	public void visit(CustomNode customNode) {
		visitChildren(customNode);
	}

    protected void visitChildren(Node parent) {
		log.debug("Visit start {}", parent);
        Node node = parent.getFirstChild();
        while (node != null) {
            // A subclass of this visitor might modify the node, resulting in getNext returning a different node or no
            // node after visiting it. So get the next node before visiting.
            Node next = node.getNext();
            node.accept(this);
            node = next;
        }
		log.debug("Visit end {}", parent);
    }

	List<List<String>> pages = new ArrayList<>();
	private List<String> currentBlock;

	private List<Slide> xSlides = new ArrayList<>();
	private List<Block> xBlocks; // = new ArrayList<>();

	public Deck getDeck() {
		return new Deck(xSlides);
	}

	public void startSlide() {
		List<String> slide = new ArrayList<>();
		pages.add(slide);
		currentBlock = slide;

		xBlocks = new ArrayList<>();
	}

	public void endSlide() {
		if (xBlocks != null) {
			Slide slide = Slide.of(xBlocks);
			xSlides.add(slide);
		}
		xBlocks = null;
	}

	public void append(String text) {
		// currentSlide.add(text);
		if (buf != null) {
			buf.append(text);
		}
	}

	private StringBuilder buf;

	public void startBlock() {
		if (xBlocks == null) {
			xBlocks = new ArrayList<>();
		}
		buf = new StringBuilder();
	}

	public void endBlock() {
		currentBlock.add(buf.toString());
		Block block = Block.of(Arrays.asList(buf.toString()));
		xBlocks.add(block);
	}


}
