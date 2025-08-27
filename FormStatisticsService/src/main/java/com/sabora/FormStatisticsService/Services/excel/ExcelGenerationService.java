package com.sabora.FormStatisticsService.Services.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ExcelGenerationService {

    private final byte[] saboraYellowByte = new byte[]{(byte)0xBF, (byte)0xFF, (byte)0x0C};
    XSSFColor saboraYellow = new XSSFColor(saboraYellowByte, null);

    /**
     * Genera un Excel resumen con los datos de formularios
     */
    public void generateGeneralSummaryExcel(List<List<Object>> data, String outputPath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Resumen General");

            // Crear encabezado (título + subtítulo)
            createHeader(sheet, workbook, "aisefab");

            // Crear tabla con datos
            createTable(sheet, workbook, data);

            // Ajustar columnas automáticamente
            autoSizeColumns(sheet, 6);

            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
                workbook.write(fileOut);
            }
        }
    }

    /**
     * Crea el título y subtítulo en el Excel
     */
    private void createHeader(Sheet sheet, Workbook workbook, String username) {
        // Estilo título
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 48);
        titleStyle.setFont(titleFont);
        titleStyle.setFillForegroundColor(saboraYellow);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Estilo subtítulo
        CellStyle subtitleStyle = workbook.createCellStyle();
        subtitleStyle.setAlignment(HorizontalAlignment.CENTER);
        subtitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        subtitleStyle.setFillForegroundColor(saboraYellow);
        subtitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Fila título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(5);
        titleCell.setCellValue("Resumen General de Formularios");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 17));

        // Fila subtítulo
        Row subtitleRow = sheet.createRow(1);
        Cell generationDate = subtitleRow.createCell(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd 'de' MMMM 'del' yyyy 'a las' HH:mm",
                Locale.forLanguageTag("es-ES")
        );

        generationDate.setCellValue("Generado el " + LocalDateTime.now().format(formatter));
        generationDate.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 11));

        Cell creationUser = subtitleRow.createCell(12);
        creationUser.setCellValue("Resumen generado por: " + username);
        creationUser.setCellStyle(subtitleStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 12, 17));
    }

    /**
     * Crea la tabla con cabeceras y datos
     */
    private void createTable(Sheet sheet, Workbook workbook, List<List<Object>> data) {
        String[] columns = {"ID", "Nombre del formulario", "Usuario creador", "Fecha creación", "Nº respuestas"};

        int firstColumn = 1;
        // Estilo cabecera
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(saboraYellow);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Crear fila de cabeceras
        Row headerRow = sheet.createRow(3);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i + firstColumn);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Insertar datos
        int rowNum = 4;
        for (List<Object> rowData : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < rowData.size(); i++) {
                Cell cell = row.createCell(i + firstColumn);
                cell.setCellValue(rowData.get(i).toString());
            }
        }
    }

    /**
     * Ajusta automáticamente el ancho de las columnas
     */
    private void autoSizeColumns(Sheet sheet, int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    // ==== Ejemplo de uso ====
    public static void main(String[] args) throws IOException {
        ExcelGenerationService service = new ExcelGenerationService();

        List<List<Object>> data = List.of(
                List.of(1L, "Encuesta Satisfacción 2025", "Laura", "2025-02-15", 132),
                List.of(2L, "Test Cata Sensorial VR", "Alex", "2025-03-01", 56),
                List.of(3L, "Opinión sobre Productos", "Marta", "2025-03-12", 24)
        );

        String outputPath = String.format("src/main/resources/generated-excels/resumen_general-%s.xlsx", LocalDateTime.now());
        service.generateGeneralSummaryExcel(data, outputPath);

        System.out.println("✅ Excel generado en: " + outputPath);
    }
}
