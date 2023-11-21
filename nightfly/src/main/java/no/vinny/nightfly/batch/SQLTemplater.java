package no.vinny.nightfly.batch;

public class SQLTemplater {

    public static String batchQuery(Boolean includeId, Boolean includeRecipe) {
        includeId     =     includeId == null ? true : includeId;
        includeRecipe = includeRecipe == null ? true : includeRecipe;
        return STR
                ."""
                SELECT \{batchColumns(includeId, includeRecipe)} \{recipeColumns(includeRecipe)} FROM batch b
                \{includeRecipe ? " LEFT JOIN recipe r on r.id = b.recipe " : ""}
                """;
    }

    public static String tapQuery() {
        return STR."""
                SELECT \{tapColumns()}, \{batchColumns(true,false)} FROM tap t
                LEFT JOIN batch b on b.id = t.batch
                ORDER BY t.id ASC
                """;
    }

    public static String batchInsert() {
        return "INSERT INTO batch (brewfather_id, name, status, tap_status, packaging, recipe, tap) VALUES (:brewfatherId, :name, :status, :packaging, :recipe, :tap)";
    }

    public static String batchUpdate() {
        return "UPDATE batch SET brewfather_id = :brewfatherId, name = :name, status = :status, packaging = :packaging, recipe = :recipe, tap = :tap WHERE id = :id ";
    }

    public static String batchCount() {
        return "SELECT COUNT(*) FROM batch";
    }

    private static String batchColumns(boolean includeId, boolean includeRecipe) {
        String columns = "b.brewfather_id, b.name, b.status, b.tap_status, b.packaging, b.tap";
        columns = includeRecipe ? columns + ", b.recipe" : columns;
        columns = includeId ? "b.id, " + columns : columns;
        return columns;
    }
    private static String recipeColumns(boolean includeRecipe) {
        return includeRecipe ? ", r.id, r.brewfather_id, r.name" : "";
    }

    private static String tapColumns() {
        return "t.id, t.batch";
    }
}
