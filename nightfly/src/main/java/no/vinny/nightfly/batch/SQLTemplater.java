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
        String columns = "b.brewfather_id b_brewfather_id, b.name b_name, b.status b_status, b.tap_status b_tap_status, b.packaging b_packaging, b.tap b_tap";
        columns = includeRecipe ? columns + ", b.recipe b_recipe" : columns;
        columns = includeId ? "b.id b_id, " + columns : columns;
        return columns;
    }
    private static String recipeColumns(boolean includeRecipe) {
        return includeRecipe ? ", r.id r_id, r.brewfather_id r_brewfather_id, r.name r_name" : "";
    }

    private static String tapColumns() {
        return "t.id t_id, t.batch t_batch";
    }
}
