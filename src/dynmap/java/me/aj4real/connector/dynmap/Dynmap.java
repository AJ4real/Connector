package me.aj4real.connector.dynmap;

import me.aj4real.connector.dynmap.events.*;
import me.aj4real.connector.dynmap.objects.Area;
import me.aj4real.connector.dynmap.objects.Location;
import me.aj4real.connector.dynmap.objects.Marker;
import me.aj4real.connector.dynmap.objects.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.*;

public class Dynmap {
    private final int teleportThreshold = 500;
    private final DynmapConfiguration config;
    private final DynmapConnector c;
    private Map<String, Player> players = new HashMap<>();
    private Map<String, String> playersInAreas = new HashMap<>();
    private Map<String, Area> areas = new HashMap<>();
    private Map<String, String> areas2 = new HashMap<>();
    private Map<String, String> areaBounds = new HashMap<>();
    private Map<String, Marker> markersWithLabels = new HashMap<>();
    private Map<String, Marker> markersWithLocations = new HashMap<>();
    private boolean init = false;
    public Dynmap(DynmapConnector c, DynmapConfiguration config) {
        this.c = c;
        this.config = config;
    }
    public Collection<Player> getPlayers() {
        return players.values();
    }
    public DynmapConfiguration getConfiguration() {
        return this.config;
    }
    public Area getArea(int x, int z) {
        for (Area area : areas.values()) {
            if(area.getPolygon().contains(x, z)) {
                return area;
            }
        }
        return null;
    }
    public Area getArea(String label) {
        if(areas.containsKey(label)) {
            return areas.get(label);
        } else {
            return null;
        }
    }
    public Player getPlayer(String account) {
        return this.players.get(account);
    }
    public void patchWorld(JSONObject payload) {
        Map<String, Player> newPlayers = new HashMap<>();
        Map<String, Area> newAreas = new HashMap<>();
        Map<String, String> newAreas2 = new HashMap<>();
        Map<String, String> newAreaBounds = new HashMap<>();
        Map<String, String> newPlayersInAreas = new HashMap<>();
        Map<String, Marker> newMarkersWithLabels = new HashMap<>();
        Map<String, Marker> newMarkersWithLocations = new HashMap<>();
        JSONArray players = (JSONArray) payload.get("players");
        for(Object o : players) {
            JSONObject p = (JSONObject) o;
            Player plr = new Player(p);
            newPlayers.put(plr.getAccountName(), plr);
        }
        JSONArray updates = (JSONArray) payload.get("updates");
        for(Object o : updates) {
            JSONObject update = (JSONObject) o;
            if(update.containsKey("msg")) {
                if(((String)update.get("msg")).equalsIgnoreCase("markerupdated")) {
//                    System.out.println("markerupdated " + update);
                    Location location = new Location((double) update.get("x"), (double) update.get("y"), (double) update.get("z"));
                    Marker m = new Marker((String) update.get("id"), (String) update.get("label"), location, (String) update.get("icon"), (String) update.get("desc"));
                    newMarkersWithLabels.put(m.getLabel(), m);
                    newMarkersWithLocations.put(m.getLocation().toString(), m);
                }
                if(((String)update.get("msg")).equalsIgnoreCase("areaupdated")) {
                    JSONArray xs = (JSONArray) update.get("x");
                    JSONArray zs = (JSONArray) update.get("z");
                    if(xs.size() == zs.size()) {
                        int[] x = new int[xs.size()];
                        int[] z = new int[zs.size()];
                        for(int i = 0; i < xs.size(); i++) {
                            x[i] = ((Double) xs.get(i)).intValue();
                            z[i] = ((Double) zs.get(i)).intValue();
                        }
                        Polygon p = new Polygon(x, z, xs.size());
                        Area a = new Area((String) update.get("id"), (String) update.get("label"), (String) update.get("desc"), p);
                        newAreas.put(a.getLabel(), a);
                        newAreas2.put(a.getLabel(), a.toString());
                        newAreaBounds.put(a.getPolygon().getBounds().toString(), a.getLabel());
                    }
                }
            }
        }
        if(init) {
            Set<String> oldPlayerNames = this.players.keySet();
            Set<String> newPlayerNames = newPlayers.keySet();
            for(Map.Entry<String, String> e : newAreas2.entrySet()) {
                if(this.areas2.keySet().contains(e.getKey())) {
                    if(!e.getValue().equalsIgnoreCase(this.areas2.get(e.getKey()))) {
                        //area updated
                        Area oldArea = newAreas.get(e.getKey());
                        Area newArea = areas.get(e.getKey());
                        if(oldArea.getSize() > newArea.getSize()) {
                            AreaExpandedEvent event = new AreaExpandedEvent(c, oldArea, newArea);
                            c.getHandler().fire(event);
                        } else if(oldArea.getSize() < newArea.getSize()) {
                            AreaReducedEvent event = new AreaReducedEvent(c, oldArea, newArea);
                            c.getHandler().fire(event);
                        } else {
                            if(!oldArea.getDescription().equalsIgnoreCase(newArea.getDescription())) {
                                AreaDescriptionChanged event = new AreaDescriptionChanged(c, newArea, newArea.getDescription(), oldArea.getDescription());
                                c.getHandler().fire(event);
                            }
                        }
                    }
                } else {
                    if(this.areaBounds.containsKey(newAreas.get(e.getKey()).getPolygon().getBounds().toString())) {
                        AreaRenamedEvent event = new AreaRenamedEvent(c,
                                newAreas.get(e.getKey()),
                                this.areas.get(this.areaBounds.get(newAreas.get(e.getKey()).getPolygon().getBounds().toString())).getName(),
                                newAreas.get(e.getKey()).getName());
                        c.getHandler().fire(event);
                     } else {
                        AreaCreatedEvent event = new AreaCreatedEvent(c, newAreas.get(e.getKey()));
                        c.getHandler().fire(event);
                    }
                }
            }
            for (String oldPlayerName : oldPlayerNames) {
                if(!newPlayerNames.contains(oldPlayerName)) {
                    c.getHandler().fire(new PlayerLeaveServerEvent(c, this.players.get(oldPlayerName)));
                }
            }
            for (String newPlayerName : newPlayerNames) {
                if(!oldPlayerNames.contains(newPlayerName)) {
                    c.getHandler().fire(new PlayerJoinServerEvent(c, newPlayers.get(newPlayerName)));
                }
            }
            for(Map.Entry<String, Player> p : newPlayers.entrySet()) {
                if(this.players.containsKey(p.getKey())) {
                    Player newPlayer = p.getValue();
                    Player oldPlayer = this.players.get(p.getKey());
                    if(newPlayer.isHidden() && !oldPlayer.isHidden()) {
                        PlayerAppearsEvent event = new PlayerAppearsEvent(c, newPlayer);
                        c.getHandler().fire(event);
                    }
                    if(!newPlayer.isHidden() && oldPlayer.isHidden()) {
                        PlayerHidesEvent event = new PlayerHidesEvent(c, newPlayer);
                        c.getHandler().fire(event);
                    }
                    if(newPlayer.getLocation().between(oldPlayer.getLocation()) > teleportThreshold && newPlayer.getLocation().getWorld().equalsIgnoreCase(oldPlayer.getLocation().getWorld())) {
                        PlayerTeleportEvent event = new PlayerTeleportEvent(c, newPlayer, oldPlayer.getLocation(), newPlayer.getLocation());
                        c.getHandler().fire(event);
                    }
                    if(!newPlayer.isHidden() && !oldPlayer.isHidden() && !newPlayer.getLocation().getWorld().equalsIgnoreCase(oldPlayer.getLocation().getWorld())) {
                        PlayerSwitchWorldEvent event = new PlayerSwitchWorldEvent(c, newPlayer, oldPlayer.getLocation(), newPlayer.getLocation());
                        c.getHandler().fire(event);
                    }
                }
            }
            for(Player p : newPlayers.values()) {
                Area a = getArea(p.getLocation().getX(), p.getLocation().getZ());
                if(a != null) {
                    newPlayersInAreas.put(p.getAccountName(), a.getLabel());
                } else {
                    newPlayersInAreas.put(p.getAccountName(), "none");
                }
            }
            for(Map.Entry<String, String> e : this.playersInAreas.entrySet()) {
                if(newPlayersInAreas.containsKey(e.getKey())) {
                    String oldArea = e.getValue();
                    String newArea = newPlayersInAreas.get(e.getKey());
                    if(!oldArea.equalsIgnoreCase(newArea) && !oldArea.equalsIgnoreCase("none")) {
                        boolean visible = true;
                        if(!this.players.get(e.getKey()).isHidden() && !newPlayers.get(e.getKey()).isHidden()) visible = false;
                        PlayerLeavesAreaEvent event = new PlayerLeavesAreaEvent(c, this.players.get(e.getKey()), this.areas.get(oldArea), visible);
                        c.getHandler().fire(event);
                    }
                }
            }
            for(Map.Entry<String, String> e : newPlayersInAreas.entrySet()) {
                if(this.playersInAreas.containsKey(e.getKey())) {
                    String oldArea = e.getValue();
                    String newArea = this.playersInAreas.get(e.getKey());
                    if(!oldArea.equalsIgnoreCase(newArea) && !oldArea.equalsIgnoreCase("none")) {
                        boolean visible = true;
                        if(!this.players.get(e.getKey()).isHidden() && !newPlayers.get(e.getKey()).isHidden()) visible = false;
                        PlayerEntersAreaEvent event = new PlayerEntersAreaEvent(c, newPlayers.get(e.getKey()), this.areas.get(oldArea), visible);
                        c.getHandler().fire(event);
                    }
                }
            }
            for (Map.Entry<String, Marker> e : newMarkersWithLabels.entrySet()) {
                if(this.markersWithLabels.containsKey(e.getKey())) {
                    if(!e.getValue().toString().equalsIgnoreCase(this.markersWithLabels.get(e.getKey()).toString())) {
                        Marker oldMarker = this.markersWithLabels.get(e.getKey());
                        Marker newMarker = e.getValue();
                        if(!oldMarker.getIcon().equalsIgnoreCase(newMarker.getIcon())) {
                            MarkerIconChangedEvent event = new MarkerIconChangedEvent(c, newMarker, oldMarker.getIcon(), newMarker.getIcon());
                            c.getHandler().fire(event);
                        } else if(!oldMarker.getLocation().toString().equalsIgnoreCase(newMarker.getLocation().toString())) {
                            MarkerMovedEvent event = new MarkerMovedEvent(c, newMarker, oldMarker.getLocation(), newMarker.getLocation());
                            c.getHandler().fire(event);
                        } else {
                            System.out.println("marker updated");
                            System.out.println("old " + oldMarker);
                            System.out.println("new " + newMarker);
                        }
                    }
                } else {
                    MarkerCreatedEvent event = new MarkerCreatedEvent(c, e.getValue());
                    c.getHandler().fire(event);
                }
                if(this.markersWithLocations.containsKey(e.getValue().getLocation().toString())) {
                    Marker oldMarker = this.markersWithLocations.get(e.getValue().getLocation().toString());
                    Marker newMarker = e.getValue();
                    if(!oldMarker.getName().equalsIgnoreCase(newMarker.getName())) {
                        MarkerRenameEvent event = new MarkerRenameEvent(c, newMarker, oldMarker.getName(), newMarker.getName());
                        c.getHandler().fire(event);
                    }
                }
            }
        }
        this.playersInAreas = newPlayersInAreas;
        this.players = newPlayers;
        this.areas = newAreas;
        this.areas2 = newAreas2;
        this.areaBounds = newAreaBounds;
        this.markersWithLocations = newMarkersWithLocations;
        this.markersWithLabels = newMarkersWithLabels;
        init = true;
    }
    public void patchMarkers(JSONObject payload) {
        JSONObject set = (JSONObject) payload.get("sets");
        for(Object o : set.keySet()) {
            String name = (String) o;
            JSONObject markerJson = (JSONObject) set.get(name);
            JSONObject markers = (JSONObject) markerJson.get("markers");
            for (Object k : markers.keySet()) {
                JSONObject marker = (JSONObject) markers.get(k);
                Location location = new Location((double) marker.get("x"), (double) marker.get("y"), (double) marker.get("z"));
                Marker m = new Marker((String) k, (String) marker.get("label"), location, (String) marker.get("icon"), (String) marker.get("desc"));
                this.markersWithLabels.put((String) k, m);
                this.markersWithLocations.put(location.toString(), m);
            }
            JSONObject areas = (JSONObject) markerJson.get("areas");
            for(Object o2 : areas.keySet()) {
                JSONObject areaJson = (JSONObject) areas.get(o2);
                JSONArray xs = (JSONArray) areaJson.get("x");
                JSONArray zs = (JSONArray) areaJson.get("z");
                if(xs.size() == zs.size()) {
                    int[] x = new int[xs.size()];
                    int[] z = new int[zs.size()];
                    for(int i = 0; i < xs.size(); i++) {
                        x[i] = ((Double) xs.get(i)).intValue();
                        z[i] = ((Double) zs.get(i)).intValue();
                    }
                    Polygon p = new Polygon(x, z, xs.size());
                    Area a = new Area((String) o2, (String) areaJson.get("label"), (String) areaJson.get("desc"), p);
                    this.areas.put((String) o2, a);
                    this.areas2.put(a.getLabel(), a.toString());
                    this.areaBounds.put(p.getBounds().toString(), a.getLabel());
                }
            }
        }
    }
}
