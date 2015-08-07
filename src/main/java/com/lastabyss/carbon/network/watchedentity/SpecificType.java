package com.lastabyss.carbon.network.watchedentity;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.entity.EntityType;

public enum SpecificType {

    // TODO: more types
    NONE(EType.NONE, -1),
    LIVING(EType.NONE, -1, RemappingEntry.of(6, 6, "<>", 7, 7, "<>", 8, 8, "<>", 9, 9, "<>", 15, 10)), //health, potion color, potion ambient, arrows, no-ai
    PLAYER(EType.NONE, -1, SpecificType.LIVING, RemappingEntry.of(17, 10, "<>", 18, 11, "<>", 10, 12)), //absorption hearts, score, skinflags
    AGEABLE(EType.NONE, -1, SpecificType.LIVING, new RemappingEntry(12, 11)), //age
    TAMEABLE(EType.NONE, -1, SpecificType.AGEABLE, new RemappingEntry(16, 12), new RemappingEntry(17, 13)), //tame flags, owner
    HORSE(EType.MOB, EntityType.HORSE, SpecificType.AGEABLE, RemappingEntry.of(16, 12, "<>", 19, 13, "<>", 20, 14, "<>", 21, 15, "<>", 22, 16)), //info flags, type, color/variant, owner, armor
    BAT(EType.MOB, EntityType.BAT, SpecificType.LIVING, new RemappingEntry(16, 11)), //hanging
    OCELOT(EType.MOB, EntityType.OCELOT, SpecificType.TAMEABLE, new RemappingEntry(18, 14)), //type
    WOLF(EType.MOB, EntityType.WOLF, SpecificType.TAMEABLE, RemappingEntry.of(18, 14, "<>", 19, 15, "<>", 18, 14)), //health, begging, collar color
    PIG(EType.MOB, EntityType.PIG, SpecificType.AGEABLE, new RemappingEntry(16, 12)), //has saddle
    RABBIT(EType.MOB, EntityType.RABBIT, SpecificType.AGEABLE, new RemappingEntry(18, 12)), //type
    SHEEP(EType.MOB, EntityType.SHEEP, SpecificType.AGEABLE, new RemappingEntry(16, 12)), //info flags (color + sheared)
    VILLAGER(EType.MOB, EntityType.VILLAGER, SpecificType.AGEABLE, new RemappingEntry(16, 12)), //profession
    ENDERMAN(EType.MOB, EntityType.ENDERMAN, SpecificType.LIVING, RemappingEntry.of(16, 11, "<>", 17, 12, "<>", 18, 13)), //carried block id, carried data id, screaming
    ZOMBIE(EType.MOB, EntityType.ZOMBIE, SpecificType.LIVING, RemappingEntry.of(12, 11, "<>", 13, 12, "<>", 14, 13)), //is baby, is villager, is converting
    ZOMBIE_PIGMAN(EType.MOB, EntityType.PIG_ZOMBIE, SpecificType.ZOMBIE),
    BLAZE(EType.MOB, EntityType.BLAZE, SpecificType.LIVING, new RemappingEntry(16, 11)), //on fire
    SPIDER(EType.MOB, EntityType.SPIDER, SpecificType.LIVING, new RemappingEntry(16, 11)), //is climbing
    CAVE_SPIDER(EType.MOB, EntityType.CAVE_SPIDER, SpecificType.SPIDER),
    CREEPER(EType.MOB, EntityType.CREEPER, SpecificType.LIVING, RemappingEntry.of(16, 11, "<>", 17, 12, "<>", 18, 13)), //state, is powered, ignited
    ITEM(EType.OBJECT, 2, new RemappingEntry(10, 5)), //item
    ITEM_FRAME(EType.OBJECT, 71, new RemappingEntry(8, 5), new RemappingEntry(9, 6)); //item, rotation

    private static final SpecificType[] OBJECT_BY_TYPE_ID = new SpecificType[256];
    private static final SpecificType[] MOB_BY_TYPE_ID = new SpecificType[256];

    static {
        Arrays.fill(OBJECT_BY_TYPE_ID, SpecificType.NONE);
        Arrays.fill(MOB_BY_TYPE_ID, SpecificType.NONE);
        for (SpecificType type : values()) {
            switch (type.etype) {
                case OBJECT: {
                    OBJECT_BY_TYPE_ID[type.typeId] = type;
                    break;
                }
                case MOB: {
                    MOB_BY_TYPE_ID[type.typeId] = type;
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    protected static SpecificType getObjectByTypeId(int objectTypeId) {
        return OBJECT_BY_TYPE_ID[objectTypeId];
    }

    protected static SpecificType getMobByTypeId(int mobTypeId) {
        return MOB_BY_TYPE_ID[mobTypeId];
    }

    private EType etype;
    private int typeId;
    private RemappingEntry[] entries;

    @SuppressWarnings("deprecation")
    SpecificType(EType etype, EntityType type, RemappingEntry... entries) {
        this(etype, type.getTypeId(), entries);
    }

    SpecificType(EType etype, int typeId, RemappingEntry... entries) {
        this.etype = etype;
        this.typeId = typeId;
        this.entries = entries;
    }

    @SuppressWarnings("deprecation")
    SpecificType(EType etype, EntityType type, SpecificType superType, RemappingEntry... entries) {
        this(etype, type.getTypeId(), superType, entries);
    }

    SpecificType(EType etype, int typeId, SpecificType superType, RemappingEntry... entries) {
        this(etype, typeId, combine(superType.getRemaps(), entries));
    }

    public RemappingEntry[] getRemaps() {
        return entries;
    }

    public static class RemappingEntry {
        private int from;
        private int to;
        private RemappingEntry(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        private static RemappingEntry[] of(Object... pairsWithSeparator) {
            ArrayList<RemappingEntry> entries = new ArrayList<RemappingEntry>();
            for (int i = 0; i < pairsWithSeparator.length; i += 3) {
                entries.add(new RemappingEntry((int) pairsWithSeparator[i], (int) pairsWithSeparator[i + 1]));
            }
            return entries.toArray(new RemappingEntry[entries.size()]);
        }

        @Override
        public int hashCode() {
            return from * 31 + to;
        }

        @Override
        public boolean equals(Object otherObj) {
            if (!(otherObj instanceof RemappingEntry)) {
                return false;
            }
            RemappingEntry other = (RemappingEntry) otherObj;
            return from == other.from && to == other.to;
        }

    }

    private static enum EType {
        NONE, OBJECT, MOB;
    }

    private static RemappingEntry[] combine(RemappingEntry[]... entriesArray) {
        ArrayList<RemappingEntry> aentries = new ArrayList<RemappingEntry>();
        for (RemappingEntry[] entries : entriesArray) {
            aentries.addAll(Arrays.asList(entries));
        }
        return aentries.toArray(new RemappingEntry[aentries.size()]);
    }

}