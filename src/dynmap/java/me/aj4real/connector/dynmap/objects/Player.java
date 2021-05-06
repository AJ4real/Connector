package me.aj4real.connector.dynmap.objects;

import org.json.simple.JSONObject;

public class Player {
    private final JSONObject data;
    public Player(JSONObject data) {
        this.data = data;
    }
    public String getAccountName() {
        return (String) data.get("account");
    }
    public String getUsername() {
        return (String) data.get("name");
    }
    public boolean hasNickname() {
        return !getAccountName().equalsIgnoreCase(getUsername());
    }
    public Location getLocation() {
        return new Location((String) data.get("world"), ((Double) data.get("x")).intValue(), ((Double) data.get("y")).intValue(), ((Double) data.get("z")).intValue());
    }
    public boolean isHidden() {
        return getLocation().getWorld().equalsIgnoreCase("-some-other-bogus-world-");
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Player{");
        sb.append("Account=" + getAccountName() + ";");
        if(hasNickname()) {
            sb.append("Nickname=" + getUsername() + ";");
        }
        if(isHidden()) {
            sb.append("Hidden=true;");
        } else {
            sb.append("Location=" + getLocation() + ";");
        }
        return sb.toString();
    }
}
