package rgo.cloud.docs.db.storage.query;

public final class ClassificationQuery {
    private static final String TABLE_NAME = "classification";

    private ClassificationQuery() {
    }

    public static String findAll() {
        return "SELECT * FROM " + TABLE_NAME;
    }

    public static String findById() {
        return  "SELECT * FROM " + TABLE_NAME + " " +
                "WHERE entity_id = :entity_id";
    }

    public static String findByName() {
        return  "SELECT * FROM " + TABLE_NAME + " " +
                "WHERE LOWER(name) = LOWER(:name)";
    }

    public static String save() {
        return  "INSERT INTO " + TABLE_NAME + "(name) " +
                "VALUES (:name)";
    }

    public static String update() {
        return  "UPDATE " + TABLE_NAME + " " +
                "SET name = :name " +
                "WHERE entity_id = :entity_id";
    }

    public static String deleteById() {
        return  "DELETE FROM " + TABLE_NAME + " " +
                "WHERE entity_id = :entity_id";
    }
}