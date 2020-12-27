package me.aj4real.connector.github.objects.enumerations;

public enum RepositoryPermissions {
    READ("read"),
    READ_WRITE("write"),
    READ_WRITE_ADMIN("admin"),
    NONE("none");

    private final String id;
    RepositoryPermissions(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
}
