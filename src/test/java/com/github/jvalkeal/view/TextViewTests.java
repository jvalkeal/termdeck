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

import com.github.jvalkeal.view.TextView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.shell.component.view.control.MenuView;
import org.springframework.shell.component.view.control.MenuView.MenuItem;

import static org.assertj.core.api.Assertions.assertThat;

public class TextViewTests extends AbstractViewTests {

	@Nested
	class Construction {

	}

	@Nested
	class Styling {

	}

	@Nested
	class Events {

	}

	@Nested
	class Visual {
		TextView view;

		@BeforeEach
		void setup() {
			view = new TextView();
			configure(view);
			view.setRect(0, 0, 10, 10);
		}

		@Test
		void rendersSimpleText() {
			view.setContent(new String[] { "hello" });
			view.setRect(0, 0, 80, 24);
			view.draw(screen24x80);
			assertThat(forScreen(screen24x80)).hasHorizontalText("hello", 0, 0, 5);
		}

	}

}
