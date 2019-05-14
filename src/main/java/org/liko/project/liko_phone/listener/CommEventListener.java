package org.liko.project.liko_phone.listener;

import gnu.io.*;
import org.liko.project.liko_phone.entity.Sms;
import org.liko.project.liko_phone.repository.SmsRepository;
import org.liko.project.liko_phone.util.MailUtil;
import org.liko.project.liko_phone.util.StringUtil;
import org.liko.project.liko_phone.util.WebhookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.TooManyListenersException;

@Component
public class CommEventListener implements SerialPortEventListener {

    private static final String MSG_INDEX_PRE = "+CMTI: \"SM\",";

    private static boolean isBackup = false;

    private CommPortIdentifier commPortIdentifier = null;
    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private SmsRepository smsRepository;

    public CommEventListener() {
        try {
            //获取串口、打开窗串口、获取串口的输入流。
            commPortIdentifier = CommPortIdentifier.getPortIdentifier("COM9");
            serialPort = (SerialPort) commPortIdentifier.open("SIM800C", 1000);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();

            // 初始化
            sendAT("ATE0");
            sendAT("AT+CMGF=1");
            sendAT("AT+CSCS=\"GSM\"");
            sendAT("AT+CNMI=2,1");

            //向串口添加事件监听对象。
            serialPort.addEventListener(this);
            //设置当端口有可用数据时触发事件，此设置必不可少。
            serialPort.notifyOnDataAvailable(true);

        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        //定义用于缓存读入数据的数组
        byte[] cache = new byte[1024];
        //记录已经到达串口COM21且未被读取的数据的字节（Byte）数。
        int availableBytes = 0;

        //如果是数据可用的时间发送，则进行数据的读写
        if(event.getEventType() == SerialPortEvent.DATA_AVAILABLE){
            try {
                String result = read();
                if (result.replaceAll("\r\n", "").indexOf(MSG_INDEX_PRE) != -1) {
                    sendAT("AT+CMGR=" + result.substring(result.indexOf(MSG_INDEX_PRE) + MSG_INDEX_PRE.length()));
                } else if (result.replaceAll("\r\n", "").indexOf("+CMTI: \"ME\",") != -1) {
                    sendAT("AT+CMGR=" + result.substring(result.indexOf("+CMTI: \"ME\",") + "+CMTI: \"ME\",".length()));
                } else if (result.indexOf("+CMGR") != -1) {
                    System.out.println("收到信息: " + result);
                    System.out.println("收到短信: " + StringUtil.analyseStr(result.split("\r\n")[2]));
                    saveSms(result);
                    mailUtil.send("715471748@qq.com", "备用手机收到了一条短信", StringUtil.analyseStr(result.split("\r\n")[2]));
                    WebhookUtil.send(result);
                    WebhookUtil.send(StringUtil.analyseStr(result.split("\r\n")[2]));

                } else {
                    handleUnknowResult(result);
                }
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveSms(String result) {
        String content = StringUtil.analyseStr(result.split("\r\n")[2]);
        System.out.println(content);
        String phoneNum = result.substring(result.indexOf("\"", 19) + 1, result.indexOf("\"", result.indexOf("\"", 19) + 1));
        System.out.println(phoneNum);
        Sms sms = new Sms(phoneNum, content);
        smsRepository.save(sms);
    }

    private void handleUnknowResult(String result) {
        System.out.println("UnknowResult: " + result);
        if (!result.replaceAll("\r\n", "").equals("OK")) {
            mailUtil.send("715471748@qq.com", "备用手机收到一条无法解析的信息", result);
            WebhookUtil.send(result);
        }
    }


    /**
     * 向串口发送AT指令
     * @param atcommand 指令内容
     * @return 指令返回结果
     * @throws java.rmi.RemoteException
     */
    public void sendAT(String atcommand) throws IOException {
        System.out.println("SEND:" + atcommand);
        outputStream.write(atcommand.getBytes());
        outputStream.write('\r');
        outputStream.flush();
    }

    /**
     * 读取COM命令的返回字符串
     * @return 结果字符串
     * @throws Exception
     */
    public String read() throws Exception {
        char c;
        int n, i;
        String answer = "";
        for (i = 0; i < 100; i++) {
            while (new InputStreamReader(inputStream).ready()) {
                n = inputStream.read();
                if (n != -1) {
                    c = (char) n;
                    answer = answer + c;
                    Thread.sleep(1);
                } else {
                    break;
                }
            }
            if (answer.indexOf("OK") != -1) {
                break;
            }
            Thread.sleep(100);
        }
        return answer;
    }

}
