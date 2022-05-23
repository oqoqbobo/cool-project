package com.oqoqbobo.web.service.excelHandle;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ExcelHandleService {
    public List readExcel(String path);
}
