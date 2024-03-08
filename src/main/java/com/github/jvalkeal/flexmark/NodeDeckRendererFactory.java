package com.github.jvalkeal.flexmark;

import com.vladsch.flexmark.util.data.DataHolder;

public interface NodeDeckRendererFactory {
	NodeDeckRenderer create(DataHolder options);
}
