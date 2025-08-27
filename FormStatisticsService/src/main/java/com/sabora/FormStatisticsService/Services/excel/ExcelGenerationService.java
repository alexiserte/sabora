package com.sabora.FormStatisticsService.Services.excel;

import com.sabora.FormStatisticsService.Models.excel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;

import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ExcelGenerationService {

    private final byte[] saboraYellowByte = new byte[]{(byte)0xBF, (byte)0xFF, (byte)0x0C};
    XSSFColor saboraYellow = new XSSFColor(saboraYellowByte, null);

    public byte[] generateGeneralSummaryExcel(ExcelStatisticsRequest data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Resumen General");

            createHeader(sheet, workbook, "aisefab");
            addSaboraLogo(workbook,sheet);
            List<FormData> formData = data.getData();
            createTable(sheet, workbook, formData);

            createResponsesPerFormChart(sheet, workbook, 3, 4, 1);
            int startRow = 17;
            int startColumn = 10;

            createResponsesPerFoodChart(sheet, workbook, startRow, startColumn, data.getStats().getResponsesPerFood());
            startRow += (data.getStats().getResponsesPerFood() != null ? data.getStats().getResponsesPerFood().size() + 15 : 15);

            createResponsesPerScenarioChart(sheet, workbook, startRow, startColumn, data.getStats().getResponsesPerScenario());
            startRow += (data.getStats().getResponsesPerScenario() != null ? data.getStats().getResponsesPerScenario().size() + 15 : 15);

            createResponseTrendChart(sheet, workbook, startRow, startColumn, data.getStats().getResponseTrend());

            autoSizeColumns(sheet, 6);

            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                workbook.write(bos);
                return bos.toByteArray();
            }
        }
    }

    private void createHeader(Sheet sheet, Workbook workbook, String username) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 48);
        titleStyle.setFont(titleFont);
        titleStyle.setFillForegroundColor(saboraYellow);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle subtitleStyle = workbook.createCellStyle();
        subtitleStyle.setAlignment(HorizontalAlignment.CENTER);
        subtitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        subtitleStyle.setFillForegroundColor(saboraYellow);
        subtitleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(5);
        titleCell.setCellValue("Resumen General de Formularios");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 17));

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


    private void createTable(Sheet sheet, Workbook workbook,List<FormData> data) {
    String[] columns = {"ID", "Nombre del formulario", "Usuario creador", "Fecha creación", "Nº respuestas","Alimento","Escenario","Sonido"};

        int firstColumn = 1;
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(saboraYellow);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row headerRow = sheet.createRow(3);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i + firstColumn);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 4;
        for (var formData : data) {
            Row row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(firstColumn);
            cell.setCellValue(formData.getId());

            cell = row.createCell(firstColumn + 1);
            cell.setCellValue(formData.getName());

            cell = row.createCell(firstColumn + 2);
            cell.setCellValue(formData.getUsername());

            cell = row.createCell(firstColumn + 3);
            cell.setCellValue(formData.getCreationDate().toString());

            cell = row.createCell(firstColumn + 4);
            cell.setCellValue(formData.getNumberOfAnswers());

            cell = row.createCell(firstColumn + 5);
            cell.setCellValue(formData.getFood());

            cell = row.createCell(firstColumn + 6);
            cell.setCellValue(formData.getScenario());

            cell = row.createCell(firstColumn + 7);
            cell.setCellValue(formData.getSound());
        }
    }

    private void autoSizeColumns(Sheet sheet, int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void addSaboraLogo(Workbook workbook, Sheet sheet) throws IOException {
       String imagePath = "src/main/resources/sabora.png";
        FileInputStream is = new FileInputStream(imagePath);
        byte[] bytes = Files.readAllBytes(Paths.get(imagePath));
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        is.close();

        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = new XSSFClientAnchor();

        anchor.setCol1(3);
        anchor.setRow1(0);
        anchor.setCol2(4);
        anchor.setRow2(1);
        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

        drawing.createPicture(anchor, pictureIdx);
    }

    private void createResponsesPerFormChart(Sheet sheet, Workbook workbook, int headerRowIdx, int firstDataRowIdx, int firstColumn) {
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                firstColumn + 9, 3,   // columna inicial, fila inicial
                firstColumn + 16, 15  // columna final, fila final
        );

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Respuestas por Formulario");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Formulario");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("Nº Respuestas");
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        bottomAxis.setTickLabelPosition(AxisTickLabelPosition.NEXT_TO);

        int lastRow = sheet.getLastRowNum();
        if (lastRow < firstDataRowIdx) return; // nada que graficar

        XDDFDataSource<String> xs = XDDFDataSourcesFactory.fromStringCellRange(
                (XSSFSheet) sheet,
                new CellRangeAddress(firstDataRowIdx, lastRow, firstColumn + 1, firstColumn + 1)
        );

        XDDFNumericalDataSource<Double> ys = XDDFDataSourcesFactory.fromNumericCellRange(
                (XSSFSheet) sheet,
                new CellRangeAddress(firstDataRowIdx, lastRow, firstColumn + 4, firstColumn + 4)
        );

        XDDFBarChartData data = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);

        XDDFBarChartData.Series series = (XDDFBarChartData.Series) data.addSeries(xs, ys);
        series.setTitle("Nº Respuestas", null);

        chart.plot(data);

        XDDFBarChartData bar = (XDDFBarChartData) data;
        bar.setBarDirection(BarDirection.COL);
        bar.setBarGrouping(BarGrouping.CLUSTERED);


    }

    private void createResponsesPerFoodChart(Sheet sheet, Workbook workbook, int firstRow, int firstColumn, List<FoodResponse> responses) {
        if (responses == null || responses.isEmpty()) return;

        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        int lastRow = firstRow + responses.size() - 1;

        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                firstColumn, firstRow,
                firstColumn + 10, firstRow + 15
        );

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Respuestas por Alimento");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Alimento");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("Nº respuestas");
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        for (int i = 0; i < responses.size(); i++) {
            Row row = sheet.createRow(firstRow + i);
            row.createCell(firstColumn).setCellValue(responses.get(i).getFoodName());
            row.createCell(firstColumn + 1).setCellValue(responses.get(i).getResponseCount());
        }

        XDDFDataSource<String> xs = XDDFDataSourcesFactory.fromStringCellRange(
                (XSSFSheet) sheet,
                new CellRangeAddress(firstRow, firstRow + responses.size() - 1, firstColumn, firstColumn)
        );

        XDDFNumericalDataSource<Double> ys = XDDFDataSourcesFactory.fromNumericCellRange(
                (XSSFSheet) sheet,
                new CellRangeAddress(firstRow, firstRow + responses.size() - 1, firstColumn + 1, firstColumn + 1)
        );

        XDDFBarChartData data = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        XDDFBarChartData.Series series = (XDDFBarChartData.Series) data.addSeries(xs, ys);
        series.setTitle("Respuestas", null);

        chart.plot(data);

        XDDFBarChartData bar = (XDDFBarChartData) data;
        bar.setBarDirection(BarDirection.COL);
        bar.setBarGrouping(BarGrouping.CLUSTERED);
    }

    private void createResponsesPerScenarioChart(Sheet sheet, Workbook workbook, int firstRow, int firstColumn, List<ScenarioResponse> responses) {
        if (responses == null || responses.isEmpty()) return;

        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        int lastRow = firstRow + responses.size() - 1;

        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                firstColumn, firstRow,
                firstColumn + 10, firstRow + 15
        );

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Respuestas por Escenario");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Escenario");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("Nº respuestas");
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        for (int i = 0; i < responses.size(); i++) {
            Row row = sheet.createRow(firstRow + i);
            row.createCell(firstColumn).setCellValue(responses.get(i).getScenarioName());
            row.createCell(firstColumn + 1).setCellValue(responses.get(i).getResponseCount());
        }

        XDDFDataSource<String> xs = XDDFDataSourcesFactory.fromStringCellRange(
                (XSSFSheet) sheet,
                new CellRangeAddress(firstRow, firstRow + responses.size() - 1, firstColumn, firstColumn)
        );

        XDDFNumericalDataSource<Double> ys = XDDFDataSourcesFactory.fromNumericCellRange(
                (XSSFSheet) sheet,
                new CellRangeAddress(firstRow, firstRow + responses.size() - 1, firstColumn + 1, firstColumn + 1)
        );

        XDDFBarChartData data = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        XDDFBarChartData.Series series = (XDDFBarChartData.Series) data.addSeries(xs, ys);
        series.setTitle("Respuestas", null);

        chart.plot(data);

        XDDFBarChartData bar = (XDDFBarChartData) data;
        bar.setBarDirection(BarDirection.COL);
        bar.setBarGrouping(BarGrouping.CLUSTERED);
    }

    private void createResponseTrendChart(Sheet sheet, Workbook workbook, int firstRow, int firstColumn, List<TrendResponse> trendResponses) {
        if (trendResponses == null || trendResponses.isEmpty()) return;

        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        int lastRow = firstRow + trendResponses.size() - 1;

        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                firstColumn, firstRow,
                firstColumn + 10, firstRow + 15
        );

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Tendencia de Respuestas");
        chart.setTitleOverlay(false);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);


        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        bottomAxis.setTitle("Fecha");
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("Nº respuestas");
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int i = 0; i < trendResponses.size(); i++) {
            Row row = sheet.createRow(firstRow + i);
            row.createCell(firstColumn).setCellValue(trendResponses.get(i).getStartDate().format(formatter));
            row.createCell(firstColumn + 1).setCellValue(trendResponses.get(i).getResponseCount());
        }

        XDDFDataSource<String> xs = XDDFDataSourcesFactory.fromStringCellRange(
                (XSSFSheet) sheet,
                new CellRangeAddress(firstRow, firstRow + trendResponses.size() - 1, firstColumn, firstColumn)
        );

        XDDFNumericalDataSource<Double> ys = XDDFDataSourcesFactory.fromNumericCellRange(
                (XSSFSheet) sheet,
                new CellRangeAddress(firstRow, firstRow + trendResponses.size() - 1, firstColumn + 1, firstColumn + 1)
        );

        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(xs, ys);
        series.setTitle("Respuestas", null);

        chart.plot(data);
    }



    public static void main(String[] args) throws IOException {
        ExcelGenerationService service = new ExcelGenerationService();

        List<List<Object>> data2 = List.of(
                List.of(1L, "Encuesta Satisfacción 2025", "Laura", "2025-02-15", 132,"Fresa","Playa","Glam Rock"),
                List.of(2L, "Test Cata Sensorial VR", "Alex", "2025-03-01", 56,"Fresa","Playa","Glam Rock"),
                List.of(3L, "Opinión sobre Productos", "Marta", "2025-03-12", 24,"Fresa","Playa","Glam Rock")
        );

        // --- Ejemplo de formularios ---
        List<FormData> formData = List.of(
                FormData.builder()
                        .id(1)
                        .name("Encuesta 1")
                        .username("aisefab")
                        .creationDate(LocalDate.now())
                        .numberOfAnswers(414)
                        .food("Fresa")
                        .scenario("Playa")
                        .sound("Eminem")
                        .build(),
                FormData.builder()
                        .id(2)
                        .name("Encuesta 2")
                        .username("bperesq")
                        .creationDate(LocalDate.now().plusDays(4))
                        .numberOfAnswers(777)
                        .food("Canelones")
                        .scenario("Montaña")
                        .sound("Bruno Mars")
                        .build(),
                FormData.builder()
                        .id(3)
                        .name("Encuesta 3")
                        .username("aisefab")
                        .creationDate(LocalDate.now())
                        .numberOfAnswers(414)
                        .food("Fresa")
                        .scenario("Playa")
                        .sound("Eminem")
                        .build(),
                FormData.builder()
                        .id(4)
                        .name("Encuesta 4")
                        .username("bperesq")
                        .creationDate(LocalDate.now().plusDays(4))
                        .numberOfAnswers(777)
                        .food("Canelones")
                        .scenario("Montaña")
                        .sound("Bruno Mars")
                        .build()
        );

        List<FormResponse> responsesPerForm = List.of(
                new FormResponse(1, 414),
                new FormResponse(3, 777),
                new FormResponse(4, 414),
                new FormResponse(5, 777)
        );

        List<FoodResponse> responsesPerFood = List.of(
                new FoodResponse("Fresa", 828),
                new FoodResponse("Canelones", 1554)
        );

        List<ScenarioResponse> responsesPerScenario = List.of(
                new ScenarioResponse("Playa", 828),
                new ScenarioResponse("Montaña", 1700)
        );

        List<TrendResponse> responseTrend = List.of(
                new TrendResponse(LocalDate.now().minusDays(3), LocalDate.now().minusDays(2), 200),
                new TrendResponse(LocalDate.now().minusDays(2), LocalDate.now().minusDays(1), 300),
                new TrendResponse(LocalDate.now().minusDays(1), LocalDate.now(), 500),
                new TrendResponse(LocalDate.now(), LocalDate.now().plusDays(1), 600)

        );
        ExcelStatisticsRequest data = ExcelStatisticsRequest.builder()
                .data(formData)
                .stats(new FormStatistics(responsesPerForm, responsesPerFood, responsesPerScenario, responseTrend))
                .build();

        String outputPath = String.format("src/main/resources/generated-excels/resumen_general-%s.xlsx", LocalDateTime.now());
        service.generateGeneralSummaryExcel(data);

        System.out.println("✅ Excel generado en: " + outputPath);
    }
}
