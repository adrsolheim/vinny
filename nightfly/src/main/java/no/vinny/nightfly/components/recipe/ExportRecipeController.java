package no.vinny.nightfly.components.recipe;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import no.vinny.nightfly.components.recipe.impl.RecipeExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/export/recipes")
public class ExportRecipeController {

    private final RecipeExportService recipeExportService;
    private final String FILENAME = "recipes";

    public ExportRecipeController(RecipeExportService recipeExportService) {
        this.recipeExportService = recipeExportService;
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> export() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", FILENAME + ".csv");

        byte[] csv = recipeExportService.recipesToCsv().getBytes();
        return new ResponseEntity<>(csv, headers, HttpStatus.OK);
    }

    @GetMapping("/excel")
    public void export(HttpServletResponse response) throws IOException {
        response.setContentType("text/xlsx");
        response.setHeader("Content-disposition", String.format("attachment;filename=%s", FILENAME + ".xlsx"));
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            recipeExportService.exportRecipesToExcel(outputStream);
        }
    }
}
