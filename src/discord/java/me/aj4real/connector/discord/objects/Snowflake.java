/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J. If not, see <http://www.gnu.org/licenses/>.
 */
package me.aj4real.connector.discord.objects;

import java.math.BigInteger;
import java.time.Instant;

/**
 * An <i>unsigned</i> 64-bit ID that is guaranteed to be unique across all of Discord, except in some unique scenarios
 * in which child objects share their parent's ID.
 *
 * @see <a href="https://discord.com/developers/docs/reference#snowflake-ids">Snowflake IDs</a>
 */
public final class Snowflake implements Comparable<Snowflake> {

    public static final long DISCORD_EPOCH = 1420070400000L;

    public static Snowflake of(final long id) {
        return new Snowflake(id);
    }

    public static Snowflake of(final String id) {
        return new Snowflake(Long.parseUnsignedLong(id));
    }

    public static Snowflake of(final Instant timestamp) {
        return of((timestamp.toEpochMilli() - DISCORD_EPOCH) << 22);
    }

    public static Snowflake of(final BigInteger id) {
        return of(id.longValue());
    }

    public static long asLong(final String id) {
        return Long.parseUnsignedLong(id);
    }
    public static String asString(final long id) {
        return Long.toUnsignedString(id);
    }

    private final long id;

    private Snowflake(final long id) {
        this.id = id;
    }

    public long asLong() {
        return id;
    }

    public String asString() {
        return Long.toUnsignedString(id);
    }

    public Instant getTimestamp() {
        return Instant.ofEpochMilli(DISCORD_EPOCH + (id >>> 22));
    }

    public BigInteger asBigInteger() {
        return new BigInteger(asString());
    }

    @Override
    public int compareTo(Snowflake other) {
        return Long.signum((id >>> 22) - (other.id >>> 22));
    }

    @Override
    public boolean equals(final Object obj) {
        return (obj instanceof Snowflake) && (((Snowflake) obj).id == id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return asString();
    }
}