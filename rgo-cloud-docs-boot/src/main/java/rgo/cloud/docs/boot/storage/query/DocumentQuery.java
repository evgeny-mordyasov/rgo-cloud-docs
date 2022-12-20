package rgo.cloud.docs.boot.storage.query;

public final class DocumentQuery {
    private static final String TABLE_NAME = "DOCUMENT";

    private DocumentQuery() {
    }

    public static String findByIdAndFetchClassification() {
        return "SELECT d.entity_id, " +
                "      d.full_name, " +
                "      d.name, " +
                "      d.extension, " +
                "      d.classification_id, " +
                "      c.name as classification_name " +
                "FROM " + TABLE_NAME + " as d " +
                "JOIN CLASSIFICATION as c " +
                "ON d.entity_id = :entity_id " +
                "   AND d.classification_id = c.entity_id";
    }

    public static String save() {
        return "INSERT INTO " + TABLE_NAME + "(full_name, name, extension, classification_id) VALUES (:full_name, :name, :extension, :classification_id)";
    }

    public static String deleteById() {
        return "DELETE FROM " + TABLE_NAME + " WHERE entity_id = :entity_id";
    }
}