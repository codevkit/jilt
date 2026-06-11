package org.jilt.test.data.context;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

public final class PrivateFollowedValue extends FollowedBase {
    public final String name;

    @Builder(style = BuilderStyle.STAGED, contextType = FollowSource.class, contextMethod = "follow")
    private PrivateFollowedValue(String name) {
        this.name = name;
    }

    private static final class InnerBuilder extends PrivateFollowedValueBuilder {
        @Override
        public PrivateFollowedValue build() {
            return new PrivateFollowedValue(name).follow(contextValue);
        }
    }

    public static PrivateFollowedValueBuilders.Name builder(FollowSource source) {
        InnerBuilder builder = new InnerBuilder();
        builder.contextValue = source;
        return builder;
    }
}
