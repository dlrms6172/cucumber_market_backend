package com.cucumber.market.api.controller.region;

import com.cucumber.market.api.dto.region.RegionDto;
import com.cucumber.market.api.mapper.region.RegionMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegionAddController {

    @Autowired
    RegionMapper regionMapper;

    @GetMapping("/excel")
    public String excel() {
        return "excel";
    }

    @PostMapping("/excel/region")
    public String regionAdd(@RequestParam("file") MultipartFile file, Model model) throws IOException {

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        // 엑셀 시트 전체 개수
        int sheetCnt = workbook.getNumberOfSheets();

        for(int i = 0; i < sheetCnt; i++){
            Sheet workSheet = workbook.getSheetAt(i);

            List<RegionDto.regionAdd> dataList = new ArrayList<>();

            for(int j = 1; j <workSheet.getPhysicalNumberOfRows(); j++){
                Row row = workSheet.getRow(j);

                RegionDto.regionAdd data = new RegionDto.regionAdd();

                data.setSiDo(row.getCell(0).getStringCellValue());

                if(row.getCell(1) != null){
                    data.setSiGunGu(row.getCell(1).getStringCellValue());
                }

                if(row.getCell(2) != null){
                    data.setEupMyeonDongGu(row.getCell(2).getStringCellValue());
                }

                if(row.getCell(3) != null){
                    data.setEupMyeonLiDong(row.getCell(3).getStringCellValue());
                }

                if(row.getCell(4) != null){
                    data.setLi(row.getCell(4).getStringCellValue());
                }

                if(row.getCell(5) != null){
                    data.setLatiTude((float)row.getCell(5).getNumericCellValue());
                }

                if(row.getCell(6) != null){
                    data.setLongiTude((float)row.getCell(6).getNumericCellValue());
                }

                dataList.add(data);
            }

            int insertRegionAdd = regionMapper.insertRegionAdd(dataList);
        }
        return "excel";
    }

}
