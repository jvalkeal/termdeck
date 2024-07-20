/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jvalkeal.view;

import java.util.ArrayList;
import java.util.List;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.screen.Screen;
import org.springframework.shell.component.view.screen.Screen.Writer;
import org.springframework.shell.geom.Rectangle;
import org.springframework.shell.style.ThemeResolver.ResolvedValues;

/**
 * {@code TextView} is used to draw a text.
 *
 * @author Janne Valkealahti
 */
public class TextView extends BoxView {

	private final Logger log = LoggerFactory.getLogger(TextView.class);
	private final List<String> content = new ArrayList<>();

	public TextView() {
	}

	public void setContent(List<String> text) {
		content.clear();
		content.addAll(text);
	}

	public void setContent(String text) {
		content.clear();
		if (text == null) {
			return;
		}
		String[] lines = text.split(System.lineSeparator());
		for (int i = 0; i < lines.length; i++) {
			content.add(lines[i]);
		}
	}

	@Override
	protected void drawInternal(Screen screen) {
		Rectangle rect = getRect();
		log.debug("Drawing content to {}", rect);
		// Writer writer = screen.writerBuilder().build();
		for (int i = 0; i < content.size() && rect.y() < content.size(); i++) {
			String line = content.get(i);
			AttributedString fromAnsi = AttributedString.fromAnsi(line);
			for (int j = 0; j < fromAnsi.length(); j++) {
				AttributedStyle styleAt = fromAnsi.styleAt(j);
				ResolvedValues values = getThemeResolver().resolveValues(styleAt);
				Writer writer2 = screen.writerBuilder()
					.color(values.foreground())
					.style(values.style())
					.build();
				String string = new String(new char[]{fromAnsi.charAt(j)});
				writer2.text(string, j, i);
				writer2.background(new Rectangle(j, i, 1, 1), values.background());
			}
			// writer.text(line, 0, i);
		}

		super.drawInternal(screen);
	}

}
