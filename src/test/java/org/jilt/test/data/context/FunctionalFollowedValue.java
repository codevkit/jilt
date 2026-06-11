package org.jilt.test.data.context;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(
        style = BuilderStyle.FUNCTIONAL,
        factoryMethod = "functionalFollowedValue",
        contextType = FollowSource.class,
        contextMethod = "follow")
public final class FunctionalFollowedValue extends FollowedBase {
    public final String name;

    FunctionalFollowedValue(String name) {
        this.name = name;
    }
}
