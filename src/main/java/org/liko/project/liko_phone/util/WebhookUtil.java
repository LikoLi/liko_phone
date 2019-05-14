package org.liko.project.liko_phone.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

/**
 * @Author: Liko
 * @Description:
 * @Date: Created at 10:12 2018/12/13
 */
public class WebhookUtil {
    public static String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=b25b7ade6b62755be4b245592d94c919aa133fbb9ba5aaf3234e7d5cb0eace04";

    public static void send(String content) {
        try {
            HttpClient httpclient = HttpClients.createDefault();

            HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
            httppost.addHeader("Content-Type", "application/json; charset=utf-8");

            String textMsg = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"" + content.replaceAll("\"", "^") + "\"}}";
            StringEntity se = new StringEntity(textMsg, "utf-8");
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                String result= EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(result);
            }
        } catch (IOException e) {
            System.out.println(content + ", Webhook发送失败！");
            e.printStackTrace();
        }
    }

}
