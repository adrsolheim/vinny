package no.vinny.nightfly.components;

public class SQLTemplater {

    public static String batchQuery(Boolean includeId, Boolean includeRecipe) {
        includeId     =     includeId == null ? true : includeId;
        includeRecipe = includeRecipe == null ? true : includeRecipe;
        return STR
                ."""
                SELECT \{batchColumns(includeId, includeRecipe)} \{batchRecipeColumns(includeRecipe)} FROM batch b
                \{includeRecipe ? "LEFT JOIN recipe r on r.id = b.recipe" : ""}
                """.replace('\n', ' ');
    }

    public static String recipeQuery() {
        return STR."SELECT \{recipeColumns()} FROM recipe r";

    }

    public static String tapQuery() {
        return STR."""
                SELECT \{tapColumns()}, \{batchColumns(true,false)} FROM tap t
                LEFT JOIN batch b on b.id = t.batch
                """.replace('\n', ' ');
    }

    public static String tapUpdate() {
        return "UPDATE tap SET batch = :batch WHERE id = :id ";
    }

    public static String batchInsert() {
        return "INSERT INTO batch (brewfather_id, name, status, tap_status, packaging, recipe, tap) VALUES (:brewfatherId, :name, :status, :tapStatus, :packaging, :recipe, :tap)";
    }

    public static String batchUpdate() {
        return "UPDATE batch SET brewfather_id = :brewfatherId, name = :name, status = :status, packaging = :packaging, recipe = :recipe, tap = :tap WHERE id = :id ";
    }

    public static String batchCount() {
        return "SELECT COUNT(*) FROM batch";
    }

    private static String batchColumns(boolean includeId, boolean includeRecipe) {
        String columns = "b.brewfather_id b_brewfather_id, b.name b_name, b.status b_status, b.tap_status b_tap_status, b.packaging b_packaging, b.tap b_tap";
        columns = includeRecipe ? columns + ", b.recipe b_recipe," : columns;
        columns = includeId ? "b.id b_id, " + columns : columns;
        return columns;
    }

    public static String recipeInsert() {
        return STR."INSERT INTO recipe (brewfather_id, name) VALUES (:brewfatherId, :name)";
    }

    private static String batchRecipeColumns(boolean includeRecipe) {
        return includeRecipe ? STR."\{recipeColumns()}" : "";
    }

    private static String recipeColumns() {
        return "r.id r_id, r.brewfather_id r_brewfather_id, r.name r_name";
    }

    private static String tapColumns() {
        return "t.id t_id, t.batch t_batch";
    }
}
