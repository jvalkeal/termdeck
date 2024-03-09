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

import org.eclipse.tm4e.core.internal.theme.Style;
import org.w3c.css.sac.SelectorList;

public class CSSStyle extends Style {

	public final SelectorList selectorList;

	CSSStyle(final SelectorList selectorList) {
		this.selectorList = selectorList;
	}
}
