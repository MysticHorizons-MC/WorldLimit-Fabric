package com.mystichorizonsmc.worldlimit.logic;

public class AccessResult {
    public final boolean allowed;
    public final boolean chargedXp;
    public final String failReason;

    public AccessResult(boolean allowed, boolean chargedXp, String failReason) {
        this.allowed = allowed;
        this.chargedXp = chargedXp;
        this.failReason = failReason;
    }

    public static AccessResult allow() {
        return new AccessResult(true, false, null);
    }

    public static AccessResult deny(String reason) {
        return new AccessResult(false, false, reason);
    }
}
