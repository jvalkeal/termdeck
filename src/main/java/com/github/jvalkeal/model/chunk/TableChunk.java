package com.github.jvalkeal.model.chunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.github.jvalkeal.model.Chunk;
import com.github.jvalkeal.model.MarkdownSettings;

import org.springframework.shell.style.ThemeResolver;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.CellMatchers;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

public class TableChunk extends Chunk {

	private List<String> tableHeaderData = null;
	private List<List<String>> tableRowData = null;

	public TableChunk(List<String> tableHeaderData, List<List<String>> tableRowData) {
		this.tableHeaderData = tableHeaderData;
		this.tableRowData = tableRowData;
	}

	@Override
	public List<String> resolveContent(ThemeResolver themeResolver, MarkdownSettings markdownSettings) {

		Stream<List<String>> s1 = Stream.of(tableHeaderData);
		Stream<List<String>> s2 = tableRowData.stream();
		String[][] array = Stream.concat(s1, s2)
			.map(x -> x.toArray(new String[0]))
			.toArray(String[][]::new)
			;
		TableModel model = new ArrayTableModel(array);
		Table table = new TableBuilder(model)
			.addFullBorder(BorderStyle.fancy_light)
			.build();
		String result = table.render(80);
		String[] split = result.split(System.lineSeparator());
		return Arrays.asList(split);
	}

}
