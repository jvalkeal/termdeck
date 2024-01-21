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

import org.springframework.shell.component.view.control.AbstractControl;
import org.springframework.shell.component.view.screen.Screen;
import org.springframework.shell.geom.Dimension;
import org.springframework.shell.geom.Rectangle;

public class HeadingControl extends AbstractControl implements SlideControl {

	private String content;

	@Override
	public void draw(Screen screen) {
		Rectangle rect = getRect();
	}

	@Override
	public Dimension preferredDimension(Dimension dim) {
		throw new UnsupportedOperationException("Unimplemented method 'preferredDimension'");
	}


}
