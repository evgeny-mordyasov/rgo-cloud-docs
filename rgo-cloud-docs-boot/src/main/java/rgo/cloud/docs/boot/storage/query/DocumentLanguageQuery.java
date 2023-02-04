package rgo.cloud.docs.boot.storage.query;

public final class DocumentLanguageQuery {
    private static final String TABLE_NAME = "document_language";
    private static final String WHERE_MODIFIER = "#WHERE#";

    private DocumentLanguageQuery() {
    }

    public static String findById() {
        return  "SELECT * FROM " + TABLE_NAME + " " +
                "WHERE entity_id = :entity_id";
    }

    public static String findAll() {
        return select()
                .replace(WHERE_MODIFIER, "");
    }

    public static String findByDocumentId() {
        String where = "WHERE d.entity_id = :document_id";
        return select()
                .replace(WHERE_MODIFIER, where);
    }

    public static String findByDocumentIdAndLanguageId() {
        String where =
                "WHERE d.entity_id = :document_id" +
                "      AND l.entity_id = :language_id";
        return select()
                .replace(WHERE_MODIFIER, where);
    }

    public static String findByDocumentIdAndLanguageIdWithData() {
        return  "SELECT dl.data, " +
                "       d.full_name AS document_full_name " +
                "FROM " + TABLE_NAME + " AS dl " +
                "    JOIN document AS d " +
                "        ON dl.document_id = :document_id " +
                "WHERE language_id = :language_id";
    }

    public static String findByClassificationId() {
        String where = "WHERE d.classification_id = :classification_id";
        return select()
                .replace(WHERE_MODIFIER, where);
    }
    
    private static String select() {
        return "WITH document_cnt AS (" +
                "    SELECT dl.entity_id        AS dl_entity_id," +
                "           l.entity_id         AS language_id," +
                "           l.name              AS language_name," +
                "           d.entity_id         AS document_id," +
                "           d.full_name         AS document_full_name," +
                "           d.name              AS document_name," +
                "           d.extension         AS document_extension," +
                "           d.classification_id AS document_classification_id," +
                "           c.name              AS document_classification_name," +
                "           rd.language_id      AS rd_language_id," +
                "           COUNT(*)            AS cnt" +
                "    FROM " + TABLE_NAME + " AS dl" +
                "             JOIN language AS l" +
                "                  ON dl.language_id = l.entity_id" +
                "             JOIN document AS d" +
                "                  ON dl.document_id = d.entity_id" +
                "             JOIN classification AS c" +
                "                  ON d.classification_id = c.entity_id" +
                "             LEFT JOIN reading_document rd" +
                "                  ON l.entity_id = rd.language_id" +
                "                     AND d.entity_id = rd.document_id" +
                " " + WHERE_MODIFIER +
                "    GROUP BY dl.entity_id," +
                "             l.entity_id," +
                "             l.name," +
                "             d.entity_id," +
                "             d.full_name," +
                "             d.name," +
                "             d.extension," +
                "             d.classification_id," +
                "             c.name," +
                "             rd.language_id" +
                ")" +
                "SELECT dl_entity_id," +
                "       language_id," +
                "       language_name," +
                "       document_id," +
                "       document_full_name," +
                "       document_name," +
                "       document_extension," +
                "       document_classification_id," +
                "       document_classification_name," +
                "       CASE" +
                "           WHEN rd_language_id IS NULL THEN 0" +
                "           ELSE cnt" +
                "       END AS downloads " +
                "FROM document_cnt";
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