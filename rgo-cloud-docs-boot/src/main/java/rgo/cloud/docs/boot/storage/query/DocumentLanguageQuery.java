package rgo.cloud.docs.boot.storage.query;

public final class DocumentLanguageQuery {
    private static final String TABLE_NAME = "document_language";

    private DocumentLanguageQuery() {
    }

    public static String findById() {
        return  "SELECT * FROM " + TABLE_NAME + " " +
                "WHERE entity_id = :entity_id";
    }

    public static String findAll() {
        return select();
    }

    public static String findByDocumentId() {
        return select() + " " +
                "WHERE d.entity_id = :document_id";
    }

    public static String findByDocumentIdAndLanguageId() {
        return select() + " " +
                "WHERE d.entity_id = :document_id " +
                "      AND l.entity_id = :language_id";
    }

    public static String findByDocumentIdAndLanguageIdWithData() {
        return  "SELECT dl.entity_id, " +
                "       dl.data, " +
                "       l.entity_id AS language_id, " +
                "       l.name AS language_name, " +
                "       d.entity_id AS document_id, " +
                "       d.full_name AS document_full_name, " +
                "       d.name AS document_name, " +
                "       d.extension AS document_extension, " +
                "       d.classification_id AS document_classification_id, " +
                "       c.name AS document_classification_name " +
                "FROM " + TABLE_NAME + " AS dl " +
                "   JOIN language AS l " +
                "       ON dl.language_id = :language_id " +
                "   JOIN document AS d " +
                "       ON dl.document_id = :document_id " +
                "   JOIN classification AS c " +
                "       ON d.classification_id = c.entity_id";
    }

    public static String findByClassificationId() {
        return select() + " " +
                "WHERE d.classification_id = :classification_id";
    }

    private static String select() {
        return  "SELECT dl.entity_id, " +
                "       l.entity_id AS language_id, " +
                "       l.name AS language_name, " +
                "       d.entity_id AS document_id, " +
                "       d.full_name AS document_full_name, " +
                "       d.name AS document_name, " +
                "       d.extension AS document_extension, " +
                "       d.classification_id AS document_classification_id, " +
                "       c.name AS document_classification_name " +
                "FROM " + TABLE_NAME + " AS dl " +
                "   JOIN language AS l " +
                "       ON dl.language_id = l.entity_id " +
                "   JOIN document AS d " +
                "       ON dl.document_id = d.entity_id " +
                "   JOIN classification AS c " +
                "       ON d.classification_id = c.entity_id";
    }

    public static String save() {
        return  "INSERT INTO " + TABLE_NAME + "(document_id, language_id, data) " +
                "VALUES (:document_id, :language_id, :data)";
    }

    public static String deleteById() {
        return  "DELETE FROM " + TABLE_NAME + " " +
                "WHERE entity_id = :entity_id";
    }
}