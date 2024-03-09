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
package com.github.jvalkeal;

import java.util.List;

import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.view.TextView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.component.view.event.EventLoop;
import org.springframework.shell.component.view.event.KeyEvent.Key;
import org.springframework.shell.style.ThemeResolver;
import org.springframework.util.Assert;

class TermdeckUI {

	private final Logger log = LoggerFactory.getLogger(TermdeckUI.class);
	private final Deck deck;
	private final TerminalUIBuilder builder;
	private final ThemeResolver themeResolver;

	TermdeckUI(TerminalUIBuilder builder, ThemeResolver themeResolver, Deck deck) {
		Assert.notNull(deck, "Deck must be set");
		this.builder = builder;
		this.themeResolver = themeResolver;
		this.deck = deck;
	}

	void run() {
		TerminalUI ui = builder.build();
		TextView view = new TextView();
		ui.configure(view);
		// Deck deck = buildDeck(file);
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
		// String[] content = deck.getCurrentSlide().getContent();
		// for (int i = 0; i < content.length; i++) {
		// 	content[i] = themeResolver.evaluateExpression(content[i]).toAnsi();
		// }
		// view.setContent(Arrays.asList(content));
		List<String> content = deck.getCurrentSlide().content();
		view.setContent(content);
	}

	static void run(TerminalUIBuilder builder, ThemeResolver themeResolver, Deck deck) {
		new TermdeckUI(builder, themeResolver, deck).run();
	}
}
