package rgo.cloud.docs.boot.storage.query;

public final class DocumentLanguageQuery {
    private static final String TABLE_NAME = "DOCUMENT_LANGUAGE";

    private DocumentLanguageQuery() {
    }

    public static String findById() {
        return "SELECT * FROM " + TABLE_NAME + " WHERE entity_id = :entity_id";
    }

    public static String findAll() {
        return select() +
                "JOIN LANGUAGE l ON dl.language_id = l.entity_id " +
                "JOIN DOCUMENT d ON dl.document_id = d.entity_id " +
                "JOIN CLASSIFICATION c ON d.classification_id = c.entity_id";
    }

    public static String findByDocumentId() {
        return select() +
                "JOIN LANGUAGE l ON dl.language_id = l.entity_id " +
                "JOIN DOCUMENT d ON dl.document_id = :document_id " +
                "JOIN CLASSIFICATION c ON d.classification_id = c.entity_id";
    }

    public static String findByDocumentIdAndLanguageId() {
        return select() +
                "JOIN LANGUAGE l ON dl.language_id = :language_id " +
                "JOIN DOCUMENT d ON dl.document_id = :document_id " +
                "JOIN CLASSIFICATION c ON d.classification_id = c.entity_id";
    }

    public static String findByDocumentIdAndLanguageIdWithData() {
        return "SELECT dl.entity_id, " +
                "      dl.data, " +
                "      l.entity_id as language_id, " +
                "      l.name as language_name, " +
                "      d.entity_id as document_id, " +
                "      d.full_name as document_full_name, " +
                "      d.name as document_name, " +
                "      d.extension as document_extension, " +
                "      d.classification_id as document_classification_id, " +
                "      c.name as document_classification_name " +
                "FROM " + TABLE_NAME + " as dl " +
                "JOIN LANGUAGE l ON dl.language_id = :language_id " +
                "JOIN DOCUMENT d ON dl.document_id = :document_id " +
                "JOIN CLASSIFICATION c ON d.classification_id = c.entity_id";
    }

    public static String findByClassificationId() {
        return select() +
                "JOIN LANGUAGE l ON dl.language_id = l.entity_id " +
                "JOIN DOCUMENT d ON dl.document_id = d.entity_id " +
                "JOIN CLASSIFICATION c ON d.classification_id = :classification_id";
    }

    private static String select() {
        return "SELECT dl.entity_id, " +
                "      l.entity_id as language_id, " +
                "      l.name as language_name, " +
                "      d.entity_id as document_id, " +
                "      d.full_name as document_full_name, " +
                "      d.name as document_name, " +
                "      d.extension as document_extension, " +
                "      d.classification_id as document_classification_id, " +
                "      c.name as document_classification_name " +
                "FROM " + TABLE_NAME + " as dl ";
    }

    public static String save() {
        return "INSERT INTO " + TABLE_NAME + "(document_id, language_id, data) VALUES (:document_id, :language_id, :data)";
    }

    public static String deleteById() {
        return "DELETE FROM " + TABLE_NAME + " WHERE entity_id = :entity_id";
    }
}