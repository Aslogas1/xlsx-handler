package com.example.xlsx_handler.controller;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/numbers")
public class NumberController {


    @PostMapping("/nth-minimum")
    public ResponseEntity<Integer> getNthMinimum(@RequestParam("file") MultipartFile file, @RequestParam int n) throws IOException {
        if (findMinValueByRowNumber(file, n) != null) {
            return ResponseEntity.ok(findMinValueByRowNumber(file, n));
        }
        return ResponseEntity.noContent().build();
    }

    private Integer findMinValueByRowNumber(MultipartFile file, int rowNumber) throws IOException {
        List<Integer> numbers = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(rowNumber - 1);
        boolean hasValueInRow = false;
        if (row != null) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.NUMERIC) {
                    numbers.add((int) cell.getNumericCellValue());
                    hasValueInRow = true;
                }
                if (!hasValueInRow) {
                    break;
                }
            }
        }
        if (!numbers.isEmpty()) {
            numbers.sort(Integer::compare);
            return numbers.get(0);
        }
        return null;
    }
}
