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

import org.springframework.lang.Nullable;

/**
 * Slide represents a partial content in a {@link Deck}.
 *
 * @author Janne Valkealahti
 */
public interface Slide {

	List<String> content();

	static Slide of(List<String> content) {
		return new Slide() {

			@Override
			public List<String> content() {
				return content;
			}

		};
	}

}
// public class Slide {

// 	private String content[];
// 	private List<Section> sections = new ArrayList<>();

// 	public Slide() {
// 		this(null);
// 	}

// 	public Slide(@Nullable String[] content) {
// 		this.content = content;
// 	}

// 	public void add(Section section) {
// 		sections.add(section);
// 	}

// 	public String[] getContent() {
// 		return content;
// 	}

// 	public void setContent(String[] content) {
// 		this.content = content;
// 	}

// }
