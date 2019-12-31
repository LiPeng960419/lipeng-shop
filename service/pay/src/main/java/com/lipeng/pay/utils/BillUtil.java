package com.lipeng.pay.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/31 15:05
 */
@Slf4j
public class BillUtil {

    private BillUtil() {
    }

    public static void downloadBill(String urlPath, HttpServletResponse response,
            String fileName) {
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        OutputStream outputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = HttpUrlUtil.getConnection(urlPath, "GET");
            outputStream = response.getOutputStream();
            IOUtils.copy(httpURLConnection.getInputStream(), outputStream);
        } catch (IOException e) {
            log.error("downloadBill IOException", e);
        } finally {
            httpURLConnection.disconnect();
            IOUtils.closeQuietly(outputStream);
        }
    }

}