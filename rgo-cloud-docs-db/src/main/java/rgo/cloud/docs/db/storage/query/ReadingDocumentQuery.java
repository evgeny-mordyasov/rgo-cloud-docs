package rgo.cloud.docs.db.storage.query;

public class ReadingDocumentQuery {
    private static final String TABLE_NAME = "reading_document";

    private ReadingDocumentQuery() {
    }

    public static String findById() {
        return  "SELECT * FROM " + TABLE_NAME + " " +
                "WHERE entity_id = :entity_id";
    }

    public static String save() {
        return  "INSERT INTO " + TABLE_NAME + "(document_id, language_id) " +
                "VALUES (:document_id, :language_id)";
    }
}
