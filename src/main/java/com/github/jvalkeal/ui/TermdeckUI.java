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
package com.github.jvalkeal.ui;

import java.util.List;
import java.util.stream.Collectors;

import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.view.TextView;

import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.component.view.event.EventLoop;
import org.springframework.shell.component.view.event.KeyEvent.Key;
import org.springframework.shell.style.ThemeResolver;
import org.springframework.util.Assert;

public class TermdeckUI {

	private final Deck deck;
	private final TerminalUIBuilder builder;
	private final ThemeResolver themeResolver;

	TermdeckUI(TerminalUIBuilder builder, ThemeResolver themeResolver, Deck deck) {
		Assert.notNull(deck, "Deck must be set");
		this.builder = builder;
		this.themeResolver = themeResolver;
		this.deck = deck;
	}

	public void run() {
		TerminalUI ui = builder.build();
		TextView view = new TextView();
		ui.configure(view);
		update(view, deck);

		EventLoop eventLoop = ui.getEventLoop();
		eventLoop.onDestroy(eventLoop.keyEvents()
			.doOnNext(m -> {
				if (m.getPlainKey() == Key.q) {
					deck.move(1);
					update(view, deck);
				}
			})
			.subscribe());

		ui.setRoot(view, true);
		ui.run();

	}

	private void update(TextView view, Deck deck) {
		List<String> content = deck.getCurrentSlide().content().stream()
			.map(c -> themeResolver.evaluateExpression(c).toAnsi())
			.collect(Collectors.toList());
		view.setContent(content);
	}

	public static void run(TerminalUIBuilder builder, ThemeResolver themeResolver, Deck deck) {
		new TermdeckUI(builder, themeResolver, deck).run();
	}
}
