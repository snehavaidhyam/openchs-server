package org.openchs.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openchs.web.IndividualController;
import org.openchs.web.ProgramEncounterController;
import org.openchs.web.ProgramEnrolmentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.Iterator;

@Component
public class Importer implements CommandLineRunner {
    private IndividualController individualController;
    private ProgramEnrolmentController programEnrolmentController;
    private ProgramEncounterController programEncounterController;

    @Autowired
    public Importer(IndividualController individualController, ProgramEnrolmentController programEnrolmentController, ProgramEncounterController programEncounterController) {
        this.individualController = individualController;
        this.programEnrolmentController = programEnrolmentController;
        this.programEncounterController = programEncounterController;
    }

    public static void rowImport(Row row, ContentType contentType, RowProcessor rowProcessor, String programName) throws ParseException {
        if (contentType == ContentType.RegistrationHeader) {
            rowProcessor.readRegistrationHeader(row);
        } else if (contentType == ContentType.Registration) {
            rowProcessor.processRegistration(row);
        } else if (contentType == ContentType.EnrolmentHeader) {
            rowProcessor.readEnrolmentHeader(row);
        } else if (contentType == ContentType.Enrolment) {
            rowProcessor.processEnrolment(row, programName);
        } else if (contentType == ContentType.ProgramEncounterHeader) {
            rowProcessor.readProgramEncounterHeader(row);
        } else if (contentType == ContentType.ProgramEncounter) {
            rowProcessor.processProgramEncounter(row);
        }
    }

    public void run(String... strings) throws Exception {
        FileInputStream inputStream = new FileInputStream(new File("input/TransactionData.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        try {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = workbook.getSheetAt(i);
                ContentTypeSequence contentTypeSequence = new ContentTypeSequence();

                System.out.println("READING SHEET: " + sheet.getSheetName());

                RowProcessor rowProcessor = new RowProcessor(individualController, programEnrolmentController, programEncounterController);
                Iterator<Row> iterator = sheet.iterator();
                ContentType contentType = null;
                String programName = sheet.getSheetName();
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    String firstCellTextInGroup = ExcelUtil.getText(row, 0);
                    contentType = contentTypeSequence.getNextType(contentType, firstCellTextInGroup);
                    Importer.rowImport(row, contentType, rowProcessor, programName);
                }
                System.out.println("COMPLETED SHEET: " + sheet.getSheetName());
            }
        } finally {
            workbook.close();
            inputStream.close();
        }
    }
}