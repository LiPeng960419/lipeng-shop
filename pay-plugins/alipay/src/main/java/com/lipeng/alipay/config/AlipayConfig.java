package com.lipeng.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016092100564758";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDDscuXBHhd7XKrOaLG+H+ViWX2GKbGKbJKUQg+G/gLDt6/F7BqBB5ExxBN94fVGZVazf8/3zeBf9Zso0hICEf8BeAf4MQwlS3JQDMxALntcO8FTG4cELdkSyn+fCzvzkL4j4cXJa4B459D74PvvixXU/6jNS1gRyTgjHg5r/7eluJADTSfGyr1Qx6I1Uota/vcD34AnEF9NkmogmomPyjArNZFZoVHEjh6H8/dyV6kRa2Ml858Mrmm28Fnn2EEUyfL96BTL4ufQzYYr2RMWhHAhsNPxagaUV6rKqBtW3a7OOIAR6RC87vSYinkzD7eGc7CX02ZFP7jzkh0rO37WmbvAgMBAAECggEBALMwFNR2MmUHLC1evA7msaRSSDt8PRFMUsW7xwlgANLXTdy6Kz4YAUNG50YM9qKJJ3obD7MDiEaasNOO+3uBobBCDQQDgoFdx6v0jis7mAOzmhlG77fhEpvSO2SbfWYjfq0sdy0ZwmhWbZn+MdgPUXixH4OACMmUu06lMNzZq01STa10zoZ0Ndrs2RfJKC9NWR7HRpJcn03NFb9ya7e8/VuuTFYWDkYnpBF6bxYi2L6BFTyjpQKuPm6/0RDJUiuUS+LPBS09/Xmq4lJJw/gHt7JuSCD46h1qAZ4FUmKEvPpjQhKRTpk5HipWvbD68KS778mPTcgQ+YkEV46Bk3p65akCgYEA9cerW2kovcGc1prDLBgszY6rSrDkPTM2qbsk9Q/Yfuha9z+0oopNQs7HOk3ST94P95gz/MUZwSTGXpbJuysbyssHbCz3HyDO//OrtaYeq9Kyk9xQ/lh9XazC9UcbUoF6kdrjZgJYmwIkffTz67/9Kz0RfP5k1GPx5TXdI4axLrMCgYEAy9T3Ok3sVVdeyEQzZrOcu6gUyFeiv23nbgMuSYbD4Ud2zVaCQQQDK4/AUIS//gqZZn8vg9Mg5pqJDBxHEIrRR5EBtifErMaiMwZ/xoxqVYrmV+v1Prvikn2inEqhddNZZwrl3rVKopex81nzgmhth2fN0vY3f7af2JZbx9PdRNUCgYA7QuukXNS//EEO1b4dka6r2ayfk8tUnlad0kv08t8mqmvUlf29+07pvuX2vvDo5QhAkOhoGDY/hdh20jDN+nT3DsE5ahYj4UtIF77dS7K1h1YbpNG6oieIQLG5Zm3GFQYyyO5vmD05EuktnbR6sY4fsNfYpZY2R+r5c2VgvYdXVQKBgDMU5ABQlvGsO6ykqt0KFjVRIiQzEi43NNB1K5xxkZVbe4JqM6pnfwPjuqZzHkpkDZ2KMjYvKwefosjOLWEP6PKufGdSAA/sl6mB73OqrNtTO/a8w3E9VMKxiZbxfRUyKi2QpZryhAnLSmVaHZSI5yoP8Zz052OyOMzmoYmqpsEhAoGAIjy3oonLnrbo6K3fePbSNoc88FsNYU+CFx1LUG/HOnWWUxJuU+l66PIhkYj6yNeXfCDULqHddfb1vGxd/rmYUV941YRiXXwY9FE/JfizXYerr6/k6R9rxgHMtOM6pXcv/OWahYJmScjb4g7klFb7f0Txt0wKdpjxc+vpCXknqIE=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1U7j3Bl5QtyF6ZGouhkUFivy4lA10SQlZFP4CBY9uigwMDljVnhhQ/8yfRCHokJ6ZYaHfRivNWxm0zjtmKCNILN8HNnPtEx7lSChPSLkbwAvCtwhgPgtQFXYDlRvRACDfLw9uetUZXP7z0sd1RH8X7LVJ0d/+KN3ODlPLCcjCWXR08uHW7ys4Yrtv2AePnntvSFNNUtGOLG3mbv+sWW14I/++10KwejhnkBaMiBkx8FLcOWvT4bjArLH8TynT4Gd66GJjbCXY1jHELvKuLguBRWL6Ya22iN1udq99GQY6l8Z5trtQhOoCCj0MiAp3V8DRlwVTvg9Hn2raTENpCiJNQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

