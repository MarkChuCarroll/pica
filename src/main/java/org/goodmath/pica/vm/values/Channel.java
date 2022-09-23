package org.goodmath.pica.vm.values;

import org.goodmath.pica.ast.Identifier;

public class Channel extends Value {
    private static int idCounter = 0;

    public Channel(Identifier bosonType) {
        this.channelId = ++idCounter;
        this.bosonType = bosonType;
    }

    public Identifier getBosonType() {
        return bosonType;
    }

    public int getChannelId() {
        return channelId;
    }

    private final Identifier bosonType;
    private final int channelId;

    @Override
    public VType getType() {
        return VType.ChannelType;
    }


}
