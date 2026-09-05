package com.hogv1.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/** Tiny synchronous client-thread event bus for extensions. */
public final class EventBus {
    private final List<Consumer<ClientEvent>> listeners = new CopyOnWriteArrayList<>();
    public void subscribe(Consumer<ClientEvent> listener) { listeners.add(listener); }
    public void unsubscribe(Consumer<ClientEvent> listener) { listeners.remove(listener); }
    public void post(ClientEvent event) { for (var listener : listeners) listener.accept(event); }
    public sealed interface ClientEvent permits Tick, Render {}
    public record Tick() implements ClientEvent {}
    public record Render() implements ClientEvent {}
}
