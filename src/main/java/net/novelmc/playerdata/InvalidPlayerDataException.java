package net.novelmc.playerdata;

public class InvalidPlayerDataException extends RuntimeException {
    public InvalidPlayerDataException() {
        super("You may not use invalid player data when caching", new Throwable());
    }
}
