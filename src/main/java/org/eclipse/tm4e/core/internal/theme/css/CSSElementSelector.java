/**
 * Copyright (c) 2015-2017 Angelo ZERR.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.core.internal.theme.css;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tm4e.core.internal.theme.css.util.AbstractElementSelector;

final class CSSElementSelector extends AbstractElementSelector implements ExtendedSelector {

	CSSElementSelector(final @Nullable String uri, final @Nullable String name) {
		super(uri, name);
	}

	@Override
	public short getSelectorType() {
		return SAC_ELEMENT_NODE_SELECTOR;
	}

	@Override
	public int nbClass() {
		return 0;
	}

	@Override
	public int nbMatch(final String... cssClassNames) {
		return 0;
	}
}
