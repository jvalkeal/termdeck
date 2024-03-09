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
package com.github.jvalkeal.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.github.jvalkeal.flexmark.FlexmarkParser;
import com.github.jvalkeal.model.Deck;
import com.github.jvalkeal.ui.TermdeckUI;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.style.ThemeResolver;

@Command
public class TermdeckCommand {

	private final TerminalUIBuilder builder;
	private final ThemeResolver themeResolver;

	TermdeckCommand(TerminalUIBuilder builder, ThemeResolver themeResolver) {
		this.builder = builder;
		this.themeResolver = themeResolver;
	}

	@Command
	void termdeck(@Option File file) {
		Deck deck = buildDeck(file);
		TermdeckUI.run(builder, themeResolver, deck);
	}

	private Deck buildDeck(File file) {
		try {
			byte[] bytes = Files.readAllBytes(file.toPath());
			FlexmarkParser modelParser = new FlexmarkParser();
			Deck deck = modelParser.parse2(new String(bytes));
			return deck;
		} catch (IOException e) {
			throw new RuntimeException("cannot read file", e);
		}
	}

}
