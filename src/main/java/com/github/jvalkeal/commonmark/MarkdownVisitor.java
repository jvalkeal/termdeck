package com.github.jvalkeal.commonmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.jvalkeal.model.Block;
import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.model.HeadingBlock;
import com.github.jvalkeal.model.ListBlock;
import com.github.jvalkeal.model.Slide;
import com.github.jvalkeal.model.TextBlock;
import org.commonmark.ext.front.matter.YamlFrontMatterBlock;
import org.commonmark.ext.front.matter.YamlFrontMatterNode;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TableBody;
import org.commonmark.ext.gfm.tables.TableCell;
import org.commonmark.ext.gfm.tables.TableHead;
import org.commonmark.ext.gfm.tables.TableRow;
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

	private void enterBulletList(BulletList bulletList) {
		log.debug("Enter {}", bulletList);
	}

	private static <T> List<T> extractFirstChilds(Node node, Class<T> type) {
		ArrayList<T> list = new ArrayList<T>();
		Node child = node.getFirstChild();
		while(child != null) {
			if (child.getClass().equals(type)) {
				list.add((T)child);
			}
			child = child.getNext();
		}
		return list;
	}

	private static <T> Stream<T> extractFirstChildsStream(Node node, Class<T> type) {
		return extractFirstChilds(node, type).stream();
	}

	private void exitBulletList(BulletList bulletList) {
		log.debug("Exit {}", bulletList);
		List<String> items = extractFirstChildsStream(bulletList, ListItem.class)
			.flatMap(list -> extractFirstChildsStream(list, Paragraph.class))
			.flatMap(list -> extractFirstChildsStream(list, Text.class))
			.map(text -> text.getLiteral())
			.collect(Collectors.toList());
		blocks.add(new ListBlock(items));
	}

	@Override
	public void visit(BulletList bulletList) {
		enterBulletList(bulletList);
		visitChildren(bulletList);
		exitBulletList(bulletList);
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
		HeadingBlock block = new HeadingBlock(buf.toString(), heading.getLevel());
		blocks.add(block);
		// endBlock();
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

	private void enterListItem(ListItem listItem) {
		log.debug("Enter {}", listItem);
	}

	private void exitListItem(ListItem listItem) {
		log.debug("Exit {}", listItem);
	}

	@Override
	public void visit(ListItem listItem) {
		enterListItem(listItem);
		visitChildren(listItem);
		exitListItem(listItem);
	}

	private void enterOrderedList(OrderedList orderedList) {
		log.debug("Enter {}", orderedList);
	}

	private void exitOrderedList(OrderedList orderedList) {
		log.debug("Exit {}", orderedList);
		List<String> items = extractFirstChildsStream(orderedList, ListItem.class)
			.flatMap(list -> extractFirstChildsStream(list, Paragraph.class))
			.flatMap(list -> extractFirstChildsStream(list, Text.class))
			.map(text -> text.getLiteral())
			.collect(Collectors.toList());
		blocks.add(new ListBlock(items));
	}

	@Override
	public void visit(OrderedList orderedList) {
		enterOrderedList(orderedList);
		visitChildren(orderedList);
		exitOrderedList(orderedList);
	}

	private void enterParagraph(Paragraph paragraph) {
		log.debug("Enter {}", paragraph);
		startBlock();
	}

	private void exitParagraph(Paragraph paragraph) {
		// org.commonmark.node.Block parent = paragraph.getParent();
		log.debug("Exit {}", paragraph);

		if (paragraph.getParent() instanceof Document) {
			TextBlock block = new TextBlock(Arrays.asList(buf.toString()));
			blocks.add(block);
		}

		// endBlock();
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
	}

	private void exitText(Text text) {
		log.debug("Exit {}", text);
		append(text.getLiteral());
		if (texts != null) {
			texts.add(text.getLiteral());
		}
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

	private List<String> tableHeaderData = null;
	private List<List<String>> tableRowData = null;

	private void enterTableBlock(TableBlock tableBlock) {
		log.debug("Enter {}", tableBlock);
		tableHeaderData = new ArrayList<>();
		tableRowData = new ArrayList<>();
	}

	private void exitTableBlock(TableBlock tableBlock) {
		log.debug("Exit {}", tableBlock);

		com.github.jvalkeal.model.TableBlock tb = new com.github.jvalkeal.model.TableBlock(tableHeaderData, tableRowData);
		blocks.add(tb);
		tableHeaderData = null;
		tableRowData = null;
	}

	private void enterTableHead(TableHead tableHead) {
		log.debug("Enter {}", tableHead);
		List<String> items = extractFirstChildsStream(tableHead, TableRow.class)
			.flatMap(list -> extractFirstChildsStream(list, TableCell.class))
			.flatMap(list -> extractFirstChildsStream(list, Text.class))
			.map(text -> text.getLiteral())
			.collect(Collectors.toList());
		tableHeaderData.addAll(items);
	}

	private void exitTableHead(TableHead tableHead) {
		log.debug("Exit {}", tableHead);
	}

	private void enterTableBody(TableBody tableBody) {
		log.debug("Enter {}", tableBody);
		// List<String> items = extractFirstChildsStream(tableBody, TableRow.class)
		// 	.flatMap(list -> extractFirstChildsStream(list, TableCell.class))
		// 	.flatMap(list -> extractFirstChildsStream(list, Text.class))
		// 	.map(text -> text.getLiteral())
		// 	.collect(Collectors.toList());

		List<TableRow> rows = extractFirstChilds(tableBody, TableRow.class);
		rows.forEach(row -> {
			List<String> cells = extractFirstChildsStream(row, TableCell.class)
				.flatMap(list -> extractFirstChildsStream(list, Text.class))
				.map(text -> text.getLiteral())
				.collect(Collectors.toList());
				;
			tableRowData.add(cells);
		});

		// tableRowData.addAll(items);
	}

	private void exitTableBody(TableBody tableBody) {
		log.debug("Exit {}", tableBody);
	}

	private void enterTableRow(TableRow tableRow) {
		log.debug("Enter {}", tableRow);
	}

	private void exitTableRow(TableRow tableRow) {
		log.debug("Exit {}", tableRow);
	}

	private void enterTableCell(TableCell tableCell) {
		log.debug("Enter {}", tableCell);
	}

	private void exitTableCell(TableCell tableCell) {
		log.debug("Exit {}", tableCell);
	}

	private Map<String, List<String>> frontMatterValues;

	private void enterYamlFrontMatterBlock(YamlFrontMatterBlock yamlFrontMatterBlock) {
		log.debug("Enter {}", yamlFrontMatterBlock);
		frontMatterValues = new HashMap<>();
	}

	private void exitYamlFrontMatterBlock(YamlFrontMatterBlock yamlFrontMatterBlock) {
		log.debug("Exit {}", yamlFrontMatterBlock);
	}

	private void enterYamlFrontMatterNode(YamlFrontMatterNode yamlFrontMatterNode) {
		log.debug("Enter {}", yamlFrontMatterNode);
		if (frontMatterValues != null) {
			frontMatterValues.put(yamlFrontMatterNode.getKey(), yamlFrontMatterNode.getValues());
		}
	}

	private void exitYamlFrontMatterNode(YamlFrontMatterNode yamlFrontMatterNode) {
		log.debug("Exit {}", yamlFrontMatterNode);
	}

// YamlFrontMatterNode{} YamlFrontMatterBlock{}

	@Override
	public void visit(CustomBlock customBlock) {
		switch (customBlock) {
			case TableBlock tableBlock -> enterTableBlock(tableBlock);
			case YamlFrontMatterBlock yamlFrontMatterBlock -> enterYamlFrontMatterBlock(yamlFrontMatterBlock);
			default -> {}
		}
		visitChildren(customBlock);
		switch (customBlock) {
			case TableBlock tableBlock -> exitTableBlock(tableBlock);
			case YamlFrontMatterBlock yamlFrontMatterBlock -> exitYamlFrontMatterBlock(yamlFrontMatterBlock);
			default -> {}
		}
	}

	@Override
	public void visit(CustomNode customNode) {
		switch (customNode) {
			case TableHead tableHead -> enterTableHead(tableHead);
			case TableBody tableBody -> enterTableBody(tableBody);
			case TableRow tableRow -> enterTableRow(tableRow);
			case TableCell tableCell -> enterTableCell(tableCell);
			case YamlFrontMatterNode yamlFrontMatterNode -> enterYamlFrontMatterNode(yamlFrontMatterNode);
			default -> {}
		}
		visitChildren(customNode);
		switch (customNode) {
			case TableHead tableHead -> exitTableHead(tableHead);
			case TableBody tableBody -> exitTableBody(tableBody);
			case TableRow tableRow -> exitTableRow(tableRow);
			case TableCell tableCell -> exitTableCell(tableCell);
			case YamlFrontMatterNode yamlFrontMatterNode -> exitYamlFrontMatterNode(yamlFrontMatterNode);
			default -> {}
		}
	}

    protected void visitChildren(Node parent) {
		log.trace("Visit start {} {}", parent, parent.getParent());
        Node node = parent.getFirstChild();
        while (node != null) {
            Node next = node.getNext();
            node.accept(this);
            node = next;
        }
		log.trace("Visit end {}", parent);
    }

	private List<List<String>> pages = new ArrayList<>();
	private List<Slide> slides = new ArrayList<>();
	private List<Block> blocks;

	public Deck getDeck() {
		return new Deck(slides, frontMatterValues);
	}

	private void startSlide() {
		List<String> slide = new ArrayList<>();
		pages.add(slide);

		blocks = new ArrayList<>();
	}

	private void endSlide() {
		if (blocks != null) {
			Slide slide = Slide.of(blocks);
			slides.add(slide);
		}
		blocks = null;
	}

	private void append(String text) {
		if (buf != null) {
			buf.append(text);
		}
	}

	private StringBuilder buf;
	private List<String> texts;

	private void startBlock() {
		if (blocks == null) {
			blocks = new ArrayList<>();
		}
		buf = new StringBuilder();
		texts = new ArrayList<>();
	}

	private void endBlock() {
		// currentBlock.add(buf.toString());
		// Block block = Block.of(Arrays.asList(buf.toString()));
		// blocks.add(block);
	}

}
