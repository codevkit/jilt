package org.jilt.test.data.context;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(style = BuilderStyle.STAGED, factoryMethod = "follow", contextType = FollowSource.class)
public final class FollowedValue extends FollowedBase {
    public final String name;

    FollowedValue(String name) {
        this.name = name;
    }
}
