package com.example.lqw.androidhttptest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by lqw on 2018/4/21.
 */

public class WebServiceClient {
    private static String IP = "47.106.108.175:8080";

    // 通过 POST 方式获取HTTP服务器数据
    public static String executeHttpPost(String username, String password) {

        try {
            String path = "http://" + IP + "/AndroidHttp/LogLet";

            // 发送指令和信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);

            return sendPOSTRequest(path, params, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 处理发送数据请求
    private static String sendPOSTRequest(String path, Map<String, String> params, String encoding) throws Exception {

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, encoding);

        HttpPost post = new HttpPost(path);
        post.setEntity(entity);
        DefaultHttpClient client = new DefaultHttpClient();
        // 请求超时
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        // 读取超时
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        HttpResponse htResponse = client.execute(post);
        HttpEntity entity1 = htResponse.getEntity();
        // 判断是否成功收取信息
        if (htResponse.getStatusLine().getStatusCode() == 200) {
            String response = EntityUtils.toString(entity1, "utf-8");//将entity当中的数据转换为字符串
            return response;
        }

        // 未成功收取信息，返回空指针
        return null;
    }

    // 收取数据
    private static String getInfo(HttpResponse response) throws Exception {

        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        // 将输入流转化为byte型
        byte[] data = WebServiceURLConnection.read(is);
        // 转化为字符串
        return new String(data, "UTF-8");
    }

    public static String executeHttpGet(String username, String password) {

        try {
            String path = "http://" + IP + "/AndroidHttp/LogLet";
            //第一步：创建HttpClient对象
            HttpClient httpCient = new DefaultHttpClient();
            //第二步：创建代表请求的对象,参数是访问的服务器地址
            HttpGet httpGet = new HttpGet(path + "?username=" + username + "&password=" + password);
            //第三步：执行请求，获取服务器发还的相应对象
            HttpResponse httpResponse = httpCient.execute(httpGet);
            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //第五步：从相应对象当中取出数据，放到entity当中
                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
