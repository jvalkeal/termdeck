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

import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.control.View;
import org.springframework.shell.component.view.control.ViewEvent;
import org.springframework.shell.component.view.control.ViewEventArgs;
import org.springframework.shell.component.view.screen.Screen;
import org.springframework.shell.component.view.screen.Screen.Writer;
import org.springframework.shell.geom.Rectangle;

/**
 * {@code TextView} is a {@link View} rendering content.
 *
 * @author Janne Valkealahti
 */
public class TextView extends BoxView {

	private record TextItem(char[] data) {
	}

	private record TextRow(TextItem[] data) {
	}

	private TextRow[] rows;

	String[] content;

	public TextView() {
		this(new String[0]);
	}

	public TextView(String[] content) {
		this.content = content;

		TextRow[] ddd = new TextRow[content.length];
		for (int i = 0; i < content.length; i++) {
			TextItem[] xxx = new TextItem[content[i].length()];
			for (int j = 0; j < content[i].length(); j++) {
				xxx[i] = new TextItem(new char[]{content[j].charAt(j)});
			}
			TextRow yyy = new TextRow(xxx);
			ddd[i] = yyy;
		}
		this.rows = ddd;
	}

	public void setContent(String[] content) {
		this.content = content;
	}

	@Override
	protected void initInternal() {
		super.initInternal();
	}

	@Override
	protected void drawInternal(Screen screen) {
		Writer writer = screen.writerBuilder().layer(getLayer()).build();
		Rectangle rect = getInnerRect();
		for (int i = 0; i < rows.length; i++) {
			TextRow yyy = rows[i];
			for (int j = 0; j < yyy.data().length; j++) {
				TextItem item = yyy.data()[i];
				writer.text(new String(item.data()), rect.x(), rect.y() + i);
			}
		}
		// for (int i = 0; i < content.length; i++) {
		// 	if (i < content.length) {
		// 		writer.text(content[i], rect.x(), rect.y());
		// 	}
		// }
		super.drawInternal(screen);
	}

	/**
	 * Generic {@link ViewEventArgs}.
	 */
	public record TextViewEventArgs() implements ViewEventArgs {

		public static TextViewEventArgs of() {
			return new TextViewEventArgs();
		}
	}

	/**
	 * Generic {@link ViewEvent}.
	 *
	 * @param view the view sending an event
	 * @param args the event args
	 */
	public record TextViewEvent(View view, TextViewEventArgs args) implements ViewEvent {

		public static TextViewEvent of(View view) {
			return new TextViewEvent(view, TextViewEventArgs.of());
		}
	}

}
