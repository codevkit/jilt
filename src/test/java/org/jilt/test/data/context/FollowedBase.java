package org.jilt.test.data.context;

public abstract class FollowedBase {
    public FollowSource followed;

    @SuppressWarnings("unchecked")
    public <T extends FollowedBase> T follow(FollowSource source) {
        this.followed = source;
        return (T) this;
    }
}
