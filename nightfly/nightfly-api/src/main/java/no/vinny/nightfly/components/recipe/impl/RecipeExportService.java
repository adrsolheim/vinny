package no.vinny.nightfly.components.recipe.impl;

import jakarta.servlet.ServletOutputStream;
import no.vinny.nightfly.components.recipe.RecipeService;
import no.vinny.nightfly.domain.Recipe;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RecipeExportService {

    private static final String CSV_HEADER = "BrewfatherId,Name";
    private final RecipeService recipeService;


    public RecipeExportService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public String recipesToCsv() {
        StringBuilder csv = new StringBuilder(CSV_HEADER);
        List<Recipe> recipes = recipeService.getAll(null);
        for (Recipe recipe : recipes) {
            csv.append(String.format("%s,%s", recipe.getBrewfatherId(), recipe.getName()));
        }
        return csv.toString();
    }

    public void exportRecipesToExcel(ServletOutputStream servletOutputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("recipes");

            Row header = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Calibri");
            font.setFontHeightInPoints((short) 12);
            font.setBold(true);
            headerStyle.setFont(font);

            Cell headerCell = header.createCell(0);
            headerCell.setCellValue("Id");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(1);
            headerCell.setCellValue("BrewfatherId");
            headerCell.setCellStyle(headerStyle);

            headerCell = header.createCell(2);
            headerCell.setCellValue("Name");
            headerCell.setCellStyle(headerStyle);

            fillRows(sheet);
            workbook.write(servletOutputStream);
        }
    }

    private void fillRows(Sheet sheet) {
        List<Recipe> recipes = recipeService.getAll(null);
        int row = 1;
        for (Recipe recipe : recipes) {
            Row current = sheet.createRow(row);
            Cell rowCell = current.createCell(0);
            rowCell.setCellValue(recipe.getId());

            rowCell = current.createCell(1);
            rowCell.setCellValue(recipe.getBrewfatherId());

            rowCell = current.createCell(2);
            rowCell.setCellValue(recipe.getName());
            row++;
        }
    }
}