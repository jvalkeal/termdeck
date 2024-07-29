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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.model.MarkdownSettings;
import com.github.jvalkeal.view.TextView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.shell.component.message.ShellMessageBuilder;
import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.component.view.control.AppView;
import org.springframework.shell.component.view.control.GridView;
import org.springframework.shell.component.view.control.MenuBarView;
import org.springframework.shell.component.view.control.MenuBarView.MenuBarItem;
import org.springframework.shell.component.view.control.MenuView.MenuItem;
import org.springframework.shell.component.view.control.MenuView.MenuItemCheckStyle;
import org.springframework.shell.component.view.control.StatusBarView;
import org.springframework.shell.component.view.control.StatusBarView.StatusItem;
import org.springframework.shell.component.view.event.EventLoop;
import org.springframework.shell.component.view.event.KeyEvent;
import org.springframework.shell.component.view.event.KeyEvent.Key;
import org.springframework.shell.component.view.event.KeyEvent.KeyMask;
import org.springframework.shell.style.ThemeResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Handles {@link TerminalUI} logic.
 *
 * @author Janne Valkealahti
 */
public class TermdeckUI {

	private final Logger log = LoggerFactory.getLogger(TermdeckUI.class);
	private final Deck deck;
	private final TerminalUIBuilder builder;
	private final ThemeResolver themeResolver;
	private TerminalUI ui;
	private EventLoop eventLoop;
	private AppView app;
	private TextView deckView;

	TermdeckUI(TerminalUIBuilder builder, ThemeResolver themeResolver, Deck deck) {
		Assert.notNull(deck, "Deck must be set");
		this.builder = builder;
		this.themeResolver = themeResolver;
		this.deck = deck;
	}

	public static void run(TerminalUIBuilder builder, ThemeResolver themeResolver, Deck deck) {
		new TermdeckUI(builder, themeResolver, deck).run();
	}

	public void run() {
		ui = builder.build();
		app = buildMainView(eventLoop, ui);
		ui.configure(app);
		update(deckView, deck);

		eventLoop = ui.getEventLoop();
		eventLoop.onDestroy(eventLoop.keyEvents()
			.doOnNext(m -> {
				log.info("keyevent {}, plain {}", m, m.getPlainKey());
				if (m.getPlainKey() == Key.q && m.hasCtrl()) {
					eventLoop.dispatch(ShellMessageBuilder.ofInterrupt());
					return;
				}
				switch (m.key()) {
					case Key.CursorUp:
						log.info("Cursor up");
						deck.move(-1);
						update(deckView, deck);
						break;
					case Key.CursorDown:
						log.info("Cursor down");
						deck.move(1);
						update(deckView, deck);
						break;
					case Key.CursorRight:
						log.info("Cursor right");
						break;
					case Key.f10:
						log.info("F10");
						break;
					case Key.s | KeyEvent.KeyMask.CtrlMask:
						app.toggleStatusBarVisibility();
						break;
					case Key.n | KeyEvent.KeyMask.CtrlMask:
						app.toggleMenuBarVisibility();
						break;
					default:
						break;
				}
			})
			.subscribe());

		ui.setRoot(app, true);
		ui.run();
	}

	private AppView buildMainView(EventLoop eventLoop, TerminalUI component) {
		MenuBarView menuBar = buildMenuBar(eventLoop);
		StatusBarView statusBar = buildStatusBar(eventLoop);
		deckView = new TextView();
		ui.configure(deckView);
		AppView app = new AppView(deckView, menuBar, statusBar);
		app.setMenuBarVisible(false);
		component.configure(app);
		return app;
	}

	private void requestQuit() {
		eventLoop.dispatch(ShellMessageBuilder.ofInterrupt());
	}

	private MenuBarView buildMenuBar(EventLoop eventLoop) {
		Runnable quitAction = () -> requestQuit();
		MenuBarView menuBar = MenuBarView.of(
			MenuBarItem.of("File",
					MenuItem.of("Quit", MenuItemCheckStyle.NOCHECK, quitAction))
				.setHotKey(Key.f | KeyMask.AltMask)
		);

		ui.configure(menuBar);
		return menuBar;
	}

	private StatusBarView buildStatusBar(EventLoop eventLoop) {
		// Runnable quitAction = () -> requestQuit();
		Runnable visibilyAction = () -> app.toggleStatusBarVisibility();
		List<StatusItem> statusItems = new ArrayList<>();
		if (deck.getDeckSettings() != null && StringUtils.hasText(deck.getDeckSettings().getAuthor())) {
			statusItems.add(StatusItem.of(deck.getDeckSettings().getAuthor()));
		}
		// statusItems.add(StatusItem.of("F10 Status Bar", visibilyAction, KeyEvent.Key.f10));
		StatusBarView statusBar = new StatusBarView(statusItems);
		// StatusBarView statusBar = new StatusBarView(new StatusItem[] {
		// 	StatusItem.of("CTRL-Q Quit", quitAction),
		// 	StatusItem.of("F10 Status Bar", visibilyAction, KeyEvent.Key.f10)
		// });
		ui.configure(statusBar);
		return statusBar;
	}


	private void update(TextView view, Deck deck) {

		List<String> content = deck.getCurrentSlide().blocks().stream()
			.flatMap(blocks -> blocks.resolveContent(themeResolver, MarkdownSettings.defaults()).stream()
				// .map(c -> themeResolver.evaluateExpression(c).toAnsi())
			)
			.collect(Collectors.toList());

		// List<String> content = deck.getCurrentSlide().blocks().stream()
		// 	.flatMap(blocks -> blocks.content().stream()
		// 		.map(c -> themeResolver.evaluateExpression(c).toAnsi())
		// 	)
		// 	.collect(Collectors.toList());
		log.debug("Update content {}", content);
		view.setContent(content);
	}
}
