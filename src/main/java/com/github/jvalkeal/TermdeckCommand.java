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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.BiFunction;

import org.jline.utils.AttributedString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.event.EventLoop;
import org.springframework.shell.component.view.event.KeyEvent.Key;
import org.springframework.shell.component.view.screen.Screen;
import org.springframework.shell.geom.HorizontalAlign;
import org.springframework.shell.geom.Rectangle;
import org.springframework.shell.geom.VerticalAlign;
import org.springframework.shell.style.ThemeResolver;

@Command
public class TermdeckCommand {

	private final Logger log = LoggerFactory.getLogger(TermdeckCommand.class);

	@Autowired
	TerminalUIBuilder builder;

	@Autowired
	ThemeResolver themeResolver;

	// class ContentDraw implements BiFunction<Screen, Rectangle, Rectangle> {

	// 	private Deck deck;

	// 	ContentDraw(Deck deck) {
	// 		this.deck = deck;
	// 	}

	// 	@Override
	// 	public Rectangle apply(Screen screen, Rectangle rect) {
	// 		String content = deck.getCurrentSlide().getContent();
	// 		AttributedString aContent = themeResolver.evaluateExpression(content);
	// 		String content2 = aContent.toAnsi();
	// 		screen.writerBuilder()
	// 			.build()
	// 			// .text(content2, 0, 0);
	// 			// .text(content2, rect, HorizontalAlign.LEFT, VerticalAlign.TOP);
	// 			.text(content2, rect, HorizontalAlign.CENTER, VerticalAlign.CENTER);
	// 		return rect;
	// 	}


	// }

	@Command
	void termdeck(
		@Option() File file
	) {
		log.info("XXX file: {}", file);
		TerminalUI ui = builder.build();
		TextView view = new TextView();
		// BoxView view = new BoxView();
		ui.configure(view);
		Deck deck = buildDeck(file);
		update(view, deck);
		// ContentDraw contentDraw = new ContentDraw(deck);
		// view.setDrawFunction(contentDraw);

		EventLoop eventLoop = ui.getEventLoop();
		eventLoop.onDestroy(eventLoop.keyEvents()
			.doOnNext(m -> {
				log.info("XXX1: {}", m);
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
		String[] content = deck.getCurrentSlide().getContent();
		for (int i = 0; i < content.length; i++) {
			content[i] = themeResolver.evaluateExpression(content[i]).toAnsi();
		}
		view.setContent(content);
	}

	private Deck buildDeck(File file) {
		try {
			byte[] bytes = Files.readAllBytes(file.toPath());
			ModelParser modelParser = new ModelParser();
			Deck deck = modelParser.parse(new String(bytes));
			return deck;
		} catch (IOException e) {
			throw new RuntimeException("cannot read file", e);
		}
	}

	// private Deck buildDeck() {
	// 	Slide slide1 = new Slide("slide1");
	// 	Slide slide2 = new Slide("slide2");
	// 	Deck deck = new Deck(List.of(slide1, slide2));
	// 	return deck;
	// }

}
