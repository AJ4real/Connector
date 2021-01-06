package me.aj4real.connector.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventHandler {
    List<Thread> listeners;
    Map<Class<? extends Event>,List<Consumer<? extends Event>>> subscribed;
    public EventHandler() {
        this.listeners = new ArrayList<>();
        this.subscribed = new HashMap<Class<? extends Event>,List<Consumer<? extends Event>>>();
    }

    public <T extends Listener> void listen(T listener) {
        listener.listen();
    }

    public <T extends Event> void subscribe(Class<T> eventClass, Consumer<T> consumer) {
        if (subscribed.containsKey(eventClass)) {
            List<Consumer<? extends Event>> events = subscribed.get(eventClass);
            events.add(consumer);
            subscribed.put(eventClass, events);
        } else {
            List<Consumer<? extends Event>> events = new ArrayList<Consumer<? extends Event>>();
            events.add(consumer);
            subscribed.put(eventClass, events);
        }
    }

    public <T extends Event> void fire(T event) {
        if (subscribed.containsKey(event.getClass()))  {
            for(Consumer<? extends Event> s : subscribed.get(event.getClass())) {
                Consumer<T> sub = (Consumer<T>) s;
                Thread t = new Thread(() -> {
                    sub.accept((T) event);
                });
                t.run();
            }
        }
    }
}
