package me.aj4real.connector.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventHandler {
    List<Thread> listeners;
    Map<String,List<Consumer<? extends Event>>> subscribed;
    public EventHandler() {
        this.listeners = new ArrayList<>();
        this.subscribed = new HashMap<String,List<Consumer<? extends Event>>>();
    }

    public void clearHandlers(Class<? extends Event> clazz) {
        if (subscribed.containsKey(clazz)) subscribed.remove(clazz);
    }

    public void clearHandlers() {
        subscribed.clear();
    }

    public <T extends Listener> void listen(T listener) {
        listener.listen();
    }

    public <T extends Event> void subscribe(Class<T> eventClass, Consumer<T> consumer) {
        List<Consumer<? extends Event>> events;
        if (subscribed.containsKey(eventClass)) {
            events = subscribed.get(eventClass);
        } else {
            events = new ArrayList<>();
        }
        events.add(consumer);
        subscribed.put(eventClass.getName(), events);
    }

    public <T extends Event> void fire(T event) {
        if (subscribed.containsKey(event.getClass().getName()))  {
            for(Consumer<? extends Event> s : subscribed.get(event.getClass().getName())) {
                Consumer<T> sub = (Consumer<T>) s;
                Thread t = new Thread(() -> {
                    sub.accept((T) event);
                });
                t.run();
            }
        }
    }
}
