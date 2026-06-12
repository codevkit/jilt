package org.jilt.test;

import org.jilt.test.data.context.DollarNamedFollowedValue;
import org.jilt.test.data.context.DollarNamedFollowedValue$;
import org.jilt.test.data.context.FollowSource;
import org.jilt.test.data.context.FunctionalFollowedValue;
import org.jilt.test.data.context.FollowedValue;
import org.jilt.test.data.context.FollowedValueBuilder;
import org.jilt.test.data.context.PrivateFollowedValue;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jilt.test.data.context.FunctionalFollowedValueBuilder.follow;
import static org.jilt.test.data.context.FunctionalFollowedValueBuilder.functionalFollowedValue;
import static org.jilt.test.data.context.FunctionalFollowedValueBuilder.name;

public class ContextBuilderTest {
    @Test
    public void test_context_parameter_is_passed_to_target_context_method() {
        FollowSource source = new FollowSource("source");

        FollowedValue value = FollowedValueBuilder.follow(source)
                .name("value")
                .build();

        assertThat(value.name).isEqualTo("value");
        assertThat(value.followed).isSameAs(source);
    }

    @Test
    public void test_context_parameter_can_be_passed_as_a_functional_setter() {
        FollowSource source = new FollowSource("source");

        FunctionalFollowedValue value = functionalFollowedValue(
                follow(source),
                name("value"));

        assertThat(value.name).isEqualTo("value");
        assertThat(value.followed).isSameAs(source);
    }

    @Test
    public void test_outer_interface_name_supports_target_class_placeholder() {
        FollowSource source = new FollowSource("source");

        DollarNamedFollowedValue value = DollarNamedFollowedValue$.follow(source)
                .name("value")
                .build();

        assertThat(value.name).isEqualTo("value");
        assertThat(value.followed).isSameAs(source);
    }

    @Test
    public void test_context_parameter_works_with_abstract_builder() {
        FollowSource source = new FollowSource("source");

        PrivateFollowedValue value = PrivateFollowedValue.builder(source)
                .name("value")
                .build();

        assertThat(value.name).isEqualTo("value");
        assertThat(value.followed).isSameAs(source);
    }
}
