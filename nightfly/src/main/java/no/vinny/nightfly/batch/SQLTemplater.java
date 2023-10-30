package no.vinny.nightfly.batch;

public class SQLTemplater {
    public static String batchQuery(Boolean includeId, Boolean includeRecipe) {
        includeId = includeId == null ? true : includeId;
        includeRecipe = includeRecipe == null ? true : includeRecipe;
        return STR
                ."""
                SELECT \{batchColumns(includeId)}, \{recipeColumns(includeRecipe)} FROM batch b
                \{includeRecipe ? " LEFT JOIN recipe r on r.id = b.recipe" : ""}
                """;
    }

    public static String batchInsert() {
        return "INSERT INTO batch (brewfather_id, name, status, recipe) VALUES (:brewfatherId, :name, :status, :recipe)";
    }

    public static String batchUpdate() {
        return "UPDATE batch SET brewfather_id = :brewfatherId, name = :name, status = :status, recipe = :recipe WHERE id = :id ";
    }

    public static String batchCount() {
        return "SELECT COUNT(*) FROM batch";
    }

    private static String batchColumns(boolean includeId) {
        String columns = "b.brewfather_id, b.name, b.status, b.recipe";
        return includeId ? "b.id, " + columns : columns;
    }
    private static String recipeColumns(boolean includeRecipe) {
        return includeRecipe ? "r.id, r.brewfather_id, r.name" : "";
    }
}
