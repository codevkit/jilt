package org.jilt.test.data.context;

import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.BuilderStyle;

@Builder(
        style = BuilderStyle.STAGED,
        factoryMethod = "follow",
        contextType = FollowSource.class,
        className = "*$")
@BuilderInterfaces(outerName = "*$$")
public final class DollarNamedFollowedValue extends FollowedBase {
    public final String name;

    DollarNamedFollowedValue(String name) {
        this.name = name;
    }
}
