package com.gea.common.control;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Test {

    public static String doPost(String requestUrl, Map<String, String> requestParams){
        String result = "";
        try {
            URL url = new URL(requestUrl);
            //创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置请求方法 get/post/delete......
            connection.setRequestMethod("POST");
            //设置连接超时 ms
            connection.setConnectTimeout(5 * 1000);
            //设置读取超时 ms
            connection.setReadTimeout(5 * 1000);
            //设置是否写入数据到URL连接 默认false 如需传参 必须设为true
            connection.setDoOutput(true);
            //设置是否从URL连接中读取数据 默认true
            connection.setDoInput(true);
            //设置是否使用缓存
            connection.setUseCaches(false);
            //设置通用请求参数
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            StringBuilder paramsBuilder = new StringBuilder();
            int i = 0;
            //设置请求参数 有参数
            if (requestParams != null){
                //拼接参数
                for (String key : requestParams.keySet()) {
                    if (i != 0){
                        paramsBuilder.append("&").append(key).append("=").append(URLEncoder.encode(requestParams.get(key),"utf-8"));
                    }
                    else {
                        paramsBuilder.append(key).append("=").append(URLEncoder.encode(requestParams.get(key),"utf-8"));
                    }
                    i++;
                }
                //写入请求参数
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(paramsBuilder.toString().getBytes());
                outputStream.flush();
                outputStream.close();
            }
            //获取响应码(会自行调用连接服务器方法) 请求成功处理
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                result = stream2string(connection.getInputStream());
                System.out.println("result = " + result);
            }
            //关闭连接
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String stream2string(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder resultBuilder = new StringBuilder();
        String temp = null;
        while ( (temp = bufferedReader.readLine()) != null){
            resultBuilder.append(temp);
        }
        inputStreamReader.close();
        bufferedReader.close();
        return resultBuilder.toString();
    }


    public static void main(String[] args) {

        Map<String,String> requestParams =new HashMap<String,String>();
        requestParams.put("Salesreport","{\"actPayments\":111,\"chop\":\"kaka\",\"dataSource\":\"广州\",\"id\":1}");
        String s1 = doPost("http://192.168.1.184/report",requestParams);
        System.out.println("s1 = " + s1);
    }
}
