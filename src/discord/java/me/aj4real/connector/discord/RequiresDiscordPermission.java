package me.aj4real.connector.discord;

import me.aj4real.connector.discord.objects.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequiresDiscordPermission {
    Role.Permission[] permission() default Role.Permission.ADMINISTRATOR;
}
