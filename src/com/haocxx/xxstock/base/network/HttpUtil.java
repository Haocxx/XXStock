package com.haocxx.xxstock.base.network;

import com.haocxx.xxstock.base.log.Log;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Haocxx
 * on 2021-1-24
 */
public class HttpUtil {
    private final static String TAG = "HttpUtil";

    /** http请求的编码格式 UTF-8、GBK 等 */
    private final static String CHARSET = "GBK";

    /** http连接超时时间，默认不设置 */
    public static int CONNECT_TIMEOUT = 0;

    /** http读取超时时间，默认不设置 */
    public static int READ_TIMEOUT = 0;

    public static String doGetRequest(String httpUrl) {
        return doGetRequest(httpUrl, null);
    }

    public static String doGetRequest(String httpUrl, String data) {
        return doRequest(httpUrl, data, RequestMethod.GET);
    }

    /**
     * 网络连接、请求处理
     */
    public static String doRequest(String httpUrl, String data, RequestMethod method) {
        try {
            if (data == null) data = "";
            if (method == RequestMethod.GET && !data.equals("")) {
                httpUrl += "?" + data;
                data = "";
            }

            HttpURLConnection conn = (HttpURLConnection) new URL(httpUrl).openConnection();
            conn.setRequestMethod(method.toString());                    // POST或GET
            if (CONNECT_TIMEOUT > 0) conn.setConnectTimeout(CONNECT_TIMEOUT);
            if (READ_TIMEOUT > 0) conn.setReadTimeout(READ_TIMEOUT);
            if (method == RequestMethod.POST) conn.setDoOutput(true);    // post方法，输出数据
            conn.setDoInput(true);    // 接收数据
            conn.connect();

            // 输出数据
            if (!data.equals("")) {
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), CHARSET);
                writer.write(data);
                writer.flush();
                writer.close();
            }

            // 接收数据
            StringBuilder sb = new StringBuilder();
            if (conn.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream(), CHARSET);
                int len;
                char[] buf = new char[1024];
                while ((len = reader.read(buf)) != -1) {
                    sb.append(buf, 0, len);
                }
                reader.close();
            }
            conn.disconnect();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.debug(TAG, "doRequest异常 -> " + e.toString());
            return "";
        }
    }
}
