package rgo.cloud.docs.boot.storage.query;

public final class LanguageQuery {
    private static final String TABLE_NAME = "LANGUAGE";

    private LanguageQuery() {
    }

    public static String findAll() {
        return "SELECT * FROM " + TABLE_NAME;
    }

    public static String findById() {
        return "SELECT * FROM " + TABLE_NAME + " WHERE entity_id = :entity_id";
    }

    public static String findByName() {
        return "SELECT * FROM " + TABLE_NAME + " WHERE LOWER(name) = LOWER(:name)";
    }

    public static String save() {
        return "INSERT INTO " + TABLE_NAME + "(name) VALUES (:name)";
    }

    public static String update() {
        return  "UPDATE " + TABLE_NAME + " SET name = :name WHERE entity_id = :entity_id";
    }
}