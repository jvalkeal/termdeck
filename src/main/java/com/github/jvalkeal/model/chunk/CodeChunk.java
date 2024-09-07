package com.github.jvalkeal.model.chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.github.jvalkeal.model.Chunk;
import com.github.jvalkeal.model.MarkdownSettings;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import org.springframework.shell.style.ThemeResolver;
import org.springframework.shell.treesitter.TreeSitterLanguages;
import org.springframework.shell.treesitter.TreeSitterQueryMatch;
import org.springframework.util.StringUtils;

public class CodeChunk extends Chunk {

	// private final List<String> content;
	// private final String info;

	// public CodeChunk(List<String> content) {
	// 	this(content, null);
	// }

	// public CodeChunk(List<String> content, String info) {
	// 	this.content = new ArrayList<>(content);
	// 	this.info = info;
	// }

	// @Override
	// public List<String> resolveContent(ThemeResolver themeResolver, MarkdownSettings markdownSettings) {
	// 	return content;
	// }
	private final String content;
	private final String info;

	public CodeChunk(String content) {
		this(content, null);
	}

	public CodeChunk(String content, String info) {
		this.content = content;
		this.info = info;
	}

	@Override
	public List<String> resolveContent(ResolveContentContext context) {
		return highlight(context.treeSitterLanguages()).lines().collect(Collectors.toList());
		// return content.lines().collect(Collectors.toList());
	}

	private String highlight(TreeSitterLanguages treeSitterLanguages) {
		if (treeSitterLanguages == null || !StringUtils.hasText(info)) {
			return content;
		}
		if (!treeSitterLanguages.getSupportedLanguages().contains(info)) {
			return content;
		}

		byte[] bytes = content.getBytes();

		List<TreeSitterQueryMatch> matches = treeSitterLanguages.languageMatch(info, bytes);

		List<HighlightData> highlights = new ArrayList<>();
		int hIndex = -1;

		for (TreeSitterQueryMatch treeSitterQueryMatch : matches) {
			treeSitterQueryMatch.getCaptures().forEach(c -> {
				int startByte = c.getNode().getStartByte();
				int endByte = c.getNode().getEndByte();
				if (endByte > hIndex) {
					highlights.add(new HighlightData(treeSitterQueryMatch.getNames().getLast(), startByte, endByte));
				}

			});
		}

		StringBuilder buf = new StringBuilder();
		int ti = 0;

		for (HighlightData data : highlights) {
			int startByte = data.start();
			int endByte = data.end();
			String hKey = data.key();
			if (startByte >= ti) {
				byte[] x1 = new byte[startByte - ti];
				System.arraycopy(bytes, ti, x1, 0, startByte - ti);
				buf.append(new String(x1));
				byte[] x2 = new byte[endByte - startByte];
				System.arraycopy(bytes, startByte, x2, 0, endByte - startByte);

				HighlightValue highlightData = findHighlightData(hKey);

				AttributedStringBuilder asb = new AttributedStringBuilder();
				if (highlightData != null) {
					AttributedStyle style = new AttributedStyle();
					if (highlightData.color() > -1) {
						style = style.foregroundRgb(highlightData.color());
					}
					if (highlightData.bold()) {
						style = style.bold();
					}
					if (highlightData.italic()) {
						style = style.italic();
					}
					if (highlightData.underline()) {
						style = style.underline();
					}
					asb.style(style);
				}
				asb.append(new String(x2));
				buf.append(asb.toAnsi());

				ti = endByte;
			}
		}

		if (ti < bytes.length) {
			byte[] x = new byte[bytes.length - ti];
			System.arraycopy(bytes, ti, x, 0, bytes.length - ti);
			buf.append(new String(x));
		}

		String out = buf.toString();
		return out;
	}

	private record HighlightData(String key, int start, int end) {
	}

	private record HighlightValue(int color, boolean bold, boolean italic, boolean underline) {

		@Override
		public String toString() {
			return String.format("HighlightValue [color=%s, bold=%s, italic=%s, underline=%s]", color, bold, italic, underline);
		}

	}

	private static Map<String, HighlightValue> highlightValues = new HashMap<>();

	private static HighlightValue findHighlightData(String key) {
		HighlightValue v = null;
		int s = 0;
		for (Entry<String, HighlightValue> e : highlightValues.entrySet()) {
			if (e.getKey().startsWith(key)) {
				int size = e.getKey().split("\\.").length;
				if (size > s) {
					v = e.getValue();
					s = size;
				}
			}
		}
		// log.debug(String.format("XXX key %s picking %s", key, v));
		return v;
	}

	static {
		highlightValues.put("attribute", new HighlightValue(0xaf0000, false, true, false));
		highlightValues.put("comment", new HighlightValue(0x8a8a8a, false, true, false));
		highlightValues.put("constant.builtin", new HighlightValue(0x875f00, true, false, false));
		highlightValues.put("constant", new HighlightValue(0x875f00, false, false, false));
		highlightValues.put("constructor", new HighlightValue(0xaf8700, false, false, false));
		highlightValues.put("embedded", new HighlightValue(-1, false, false, false));
		highlightValues.put("function.builtin", new HighlightValue(0x005fd7, true, false, false));
		highlightValues.put("function", new HighlightValue(0x005fd7, false, false, false));
		highlightValues.put("keyword", new HighlightValue(0x5f00d7, false, false, false));
		highlightValues.put("number", new HighlightValue(0x875f00, true, false, false));
		highlightValues.put("module", new HighlightValue(0xaf8700, false, false, false));
		highlightValues.put("property", new HighlightValue(0xaf0000, false, false, false));
		highlightValues.put("operator", new HighlightValue(0x4e4e4e, true, false, false));
		highlightValues.put("punctuation.bracket", new HighlightValue(0x4e4e4e, false, false, false));
		highlightValues.put("punctuation.delimiter", new HighlightValue(0x4e4e4e, false, false, false));
		highlightValues.put("string.special", new HighlightValue(0x008787, false, false, false));
		highlightValues.put("string", new HighlightValue(0x008700, false, false, false));
		highlightValues.put("tag", new HighlightValue(0x000087, false, false, false));
		highlightValues.put("type", new HighlightValue(0x005f5f, false, false, false));
		highlightValues.put("type.builtin", new HighlightValue(0x005f5f, true, false, false));
		highlightValues.put("variable.builtin", new HighlightValue(-1, true, false, false));
		highlightValues.put("variable.parameter", new HighlightValue(-1, false, false, true));
	}

}
