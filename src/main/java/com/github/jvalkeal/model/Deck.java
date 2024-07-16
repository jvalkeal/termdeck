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
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * Deck is a container for a collection of {@link Slide}'s.
 *
 * @author Janne Valkealahti
 */
public class Deck {

	private final List<Slide> slides;
	private int index;

	public Deck() {
		this(List.of());
	}

	public Deck(@NonNull List<Slide> slides) {
		Assert.notNull(slides, "slides cannot be null");
		this.slides = new ArrayList<>(slides);
	}

	public List<Slide> getSlides() {
		return slides;
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
		if (i < 0) {
			if (newIndex < 0) {
				index = 0;
				return;
			}
		}
		else if (i > 0) {
			if (newIndex >= slides.size()) {
				index = slides.size() - 1;
				return;
			}
		}
		index = newIndex;
		// index = index + i;
	}

}
