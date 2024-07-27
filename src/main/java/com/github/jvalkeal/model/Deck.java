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
package com.github.jvalkeal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * Deck is a container for a collection of {@link Slide}'s.
 *
 * @author Janne Valkealahti
 */
public class Deck {

	// private final Map<String, List<String>> frontMatterValues = new HashMap<>();
	private final DeckSettings deckSettings;
	private final List<Slide> slides;
	private int index;

	public Deck() {
		this(List.of());
	}

	public Deck(@NonNull List<Slide> slides) {
		Assert.notNull(slides, "slides cannot be null");
		this.slides = new ArrayList<>(slides);
		this.deckSettings = null;
	}

	public Deck(@NonNull List<Slide> slides, DeckSettings deckSettings) {
		Assert.notNull(slides, "slides cannot be null");
		this.slides = new ArrayList<>(slides);
		this.deckSettings = deckSettings;
	}

	public List<Slide> getSlides() {
		return slides;
	}

	public DeckSettings getDeckSettings() {
		return deckSettings;
	}

	public void addSlide(Slide slide) {
		Assert.notNull(slide, "slide cannot be null");
		this.slides.add(slide);
	}

	public Slide getCurrentSlide() {
		return slides.get(index);
	}

	public void move(int i) {
		int newIndex = index + i;
		if (newIndex < 0) {
			index = 0;
		}
		else if (newIndex >= slides.size()) {
			index = slides.size() - 1;
		}
		else {
			index = newIndex;
		}
	}

}
