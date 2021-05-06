package me.aj4real.connector.dynmap.objects;

public class Location {
    private final String world;
    private final int x, y, z;
    public Location(int x, int y, int z) {
        this("", x, y, z);
    }
    public Location(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Location(double x, double y, double z) {
        this("", x, y, z);
    }
    public Location(String world, double x, double y, double z) {
        this.world = world;
        this.x = ((Double)x).intValue();
        this.y = ((Double)y).intValue();
        this.z = ((Double)z).intValue();
    }
    public String getWorld() {
        return this.world;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getZ() {
        return this.z;
    }
    public double between(Location other) {
        return Math.sqrt((other.getZ() - z) * (other.getZ() - z) + (other.getX() - x) * (other.getX() - x));
    }
    public int getChunkX() {
        if(((int) (getX() / 16)) < 0) {
            return (int) (getX() / 16) - 1;
        }
        return (int) (getX() / 16);
    }

    public int getChunkZ() {
        if (((int) (getZ() / 16)) < 0) {
            return (int) (getZ() / 16) - 1;
        }
        return (int) (getZ() / 16);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Location{");
        sb.append("World=" + getWorld() + ";");
        sb.append("X=" + getX() + ";");
        sb.append("Y=" + getY() + ";");
        sb.append("Z=" + getZ() + ";");
        sb.append("Chunk_X=" + getChunkX() + ";");
        sb.append("Chunk_Z=" + getChunkZ());
        sb.append("}");
        return sb.toString();
    }

}
