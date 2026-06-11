# Context Builder Design

This document records the design for Jilt's context builder support.

## Goal

Jilt already supports several ways to customize generated builders:

- `factoryMethod` controls the generated builder entry point.
- `buildMethod` controls the final method that creates the target instance.
- `toBuilder` creates a builder pre-populated from an existing target instance.
- `BuilderStyle.FUNCTIONAL` presents the same builder model as a static factory with functional setters.

Some target objects need one extra construction context value that is not one of the target constructor fields. A common
shape is:

```java
Target target = TargetBuilder.context(context)
        .first(...)
        .second(...)
        .build();
```

where the builder first creates `Target` from normal properties, then applies the context to the created target:

```java
Target target = new Target(first, second);
return target.context(context);
```

This should stay aligned with Jilt's core principles:

- The target class is not modified.
- Jilt remains a compile-time code generator with no runtime dependency.
- The generated builder remains explicit Java source.
- The feature is generic and does not depend on any domain-specific type or method name.

## Public API

Two attributes are added to `@Builder`:

```java
Class<?> contextType() default Void.class;

String contextMethod() default "";
```

`contextType` enables the feature. `Void.class` means there is no context.

`contextMethod` names the context entry method and the target post-construction method. If it is empty, it defaults to
`factoryMethod`.

## Rules

The validation rules are:

- `contextMethod` without `contextType` is invalid.
- `contextType` with `toBuilder` is invalid.

The first rule keeps `contextMethod` meaningful: without `contextType`, Jilt does not know the parameter type and should
not generate any context behavior.

The second rule avoids ambiguous copy semantics. `toBuilder(existing)` starts from an existing target object, but the
context value is not a target property and has no natural source in that copy operation.

## Classic And Staged Builders

For classic and staged builders, context support changes the generated static factory method.

Example:

```java
@Builder(
        style = BuilderStyle.STAGED,
        factoryMethod = "follow",
        contextType = Source.class)
public final class Event {
    public final String id;

    Event(String id) {
        this.id = id;
    }

    public Event follow(Source source) {
        // apply source
        return this;
    }
}
```

The generated builder shape is:

```java
public class EventBuilder implements EventBuilders.Id, EventBuilders.Optionals {
    private Source contextValue;
    private String id;

    private EventBuilder(Source contextValue) {
        this.contextValue = contextValue;
    }

    private EventBuilder() {
    }

    public static EventBuilders.Id follow(Source contextValue) {
        return new EventBuilder(contextValue);
    }

    public EventBuilder id(String id) {
        this.id = id;
        return this;
    }

    public Event build() {
        Event target = new Event(id);
        return target.follow(contextValue);
    }
}
```

If `contextMethod` is not specified, `factoryMethod` is used as the context method. This keeps the common staged form
concise:

```java
Event event = EventBuilder.follow(source)
        .id("event-1")
        .build();
```

If `contextMethod` is specified, the generated context factory method uses `contextMethod` instead of `factoryMethod`.
This is mainly useful when a builder style needs a different main factory name and context method name.

## Functional Builders

Functional builders already represent builder inputs as generated functional setters. Context support follows the same
model.

Example:

```java
@Builder(
        style = BuilderStyle.FUNCTIONAL,
        factoryMethod = "event",
        contextType = Source.class,
        contextMethod = "follow")
public final class Event {
    public final String id;

    Event(String id) {
        this.id = id;
    }

    public Event follow(Source source) {
        // apply source
        return this;
    }
}
```

The generated usage is:

```java
import static example.EventBuilder.event;
import static example.EventBuilder.follow;
import static example.EventBuilder.id;

Event event = event(
        follow(source),
        id("event-1"));
```

The generated builder shape is:

```java
public class EventBuilder {
    private Source contextValue;
    private String id;

    public static Event event(EventBuilders.Setter follow, EventBuilders.Id id) {
        EventBuilder builder = new EventBuilder();
        follow.accept(builder);
        id.accept(builder);
        return builder.build();
    }

    public static EventBuilders.Setter follow(Source contextValue) {
        return builder -> builder.contextValue = contextValue;
    }

    public static EventBuilders.Id id(String id) {
        return builder -> builder.id = id;
    }

    public Event build() {
        Event target = new Event(id);
        return target.follow(contextValue);
    }
}
```

If `contextMethod` is omitted, it defaults to `factoryMethod`. For functional builders this is legal, but often less
readable:

```java
@Builder(
        style = BuilderStyle.FUNCTIONAL,
        factoryMethod = "event",
        contextType = Source.class)
```

This generates:

```java
Event event = event(
        event(source),
        id("event-1"));
```

For functional builders, a distinct `contextMethod` is usually recommended when the default method name would read poorly.

## Interaction With Existing Jilt Features

### `factoryMethod`

`factoryMethod` keeps its existing meaning as the main builder factory name.

For classic and staged builders, if context support is enabled and `contextMethod` is not specified, the context factory
method is the same as `factoryMethod`.

For functional builders, `factoryMethod` always names the main factory that returns the built target. `contextMethod`
names the generated functional setter.

### `buildMethod`

`buildMethod` still names the final builder method. Context support changes the body of that method, not its name:

```java
Target target = new Target(...);
return target.contextMethod(contextValue);
```

### `toBuilder`

`toBuilder` is not supported together with `contextType`.

The context value is intentionally not modeled as a target property. Copying an existing target cannot reconstruct the
original context value in a general way.

### Private Constructors And Abstract Builders

Jilt's existing private-constructor support works by generating an abstract builder and letting user code provide a
concrete nested builder. Context support follows the same model.

When `contextType` is used with an abstract generated builder, Jilt generates a `protected contextValue` field.

Jilt still does not generate a static factory method for the abstract builder, because it cannot instantiate the abstract
builder. User code remains responsible for creating the concrete builder and setting `contextValue`.

Example:

```java
public final class Event {
    public final String id;

    @Builder(
            style = BuilderStyle.STAGED,
            contextType = Source.class,
            contextMethod = "follow")
    private Event(String id) {
        this.id = id;
    }

    public Event follow(Source source) {
        // apply source
        return this;
    }

    private static final class InnerBuilder extends EventBuilder {
        @Override
        public Event build() {
            return new Event(id).follow(contextValue);
        }
    }

    public static EventBuilders.Id builder(Source source) {
        InnerBuilder builder = new InnerBuilder();
        builder.contextValue = source;
        return builder;
    }
}
```

The user-defined factory method does not have to use `contextMethod` as its name. In Java, a static factory on the target
class can conflict with an inherited instance context method of the same signature, so a neutral name like `builder` can
be clearer for this mode.

### Meta-Annotations

`contextType` and `contextMethod` are ordinary `@Builder` attributes, so they can be used in a meta-annotation with the
same source-set limitations as other `@Builder` attributes.

## Implementation Notes

The generated builder stores the context in a `contextValue` field. The field is private for concrete generated builders
and protected for abstract generated builders, matching Jilt's existing field visibility model.

The annotation member `contextType` is a `Class<?>` value. Annotation processors must read it through the standard
`MirroredTypeException` path instead of loading the class directly.

The context target call is intentionally generated as a normal Java method call:

```java
return target.<contextMethod>(contextValue);
```

This means normal Java compilation checks method existence, accessibility, parameter type compatibility, return type
compatibility, and generic inference. Jilt does not need a separate reflection or runtime validation layer.

## Non-Goals

This feature does not:

- Add target methods or modify target classes.
- Make context a constructor property.
- Support multiple context values.
- Support `contextType` with `toBuilder`.
- Add runtime behavior or runtime dependencies.
