package cn.cqu.edu.LightningDataInterface.Tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class HttpConnectUtil {

	/**
	 * get方式
	 * @param url
	 * @author www.yoodb.com
	 * @return
	 */
	public static String getHttpFile(String url,String FilePath) {
		String responseMsg = "";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		File file=new File(FilePath);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		try {
			if(!file.exists()){
	            file.createNewFile();
	        }
			OutputStream os = new FileOutputStream(file);
			
			httpClient.executeMethod(getMethod);
			
			InputStream in = getMethod.getResponseBodyAsStream();
			int len = 0;
			byte[] buf = new byte[1024];
			while((len=in.read(buf))!=-1){
				
				os.write(buf, 0, len);
			}
			os.flush();
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//释放连接
			getMethod.releaseConnection();
		}
		return responseMsg;
	}

	/**
     * 程序中访问http数据接口，该方法为雷电数据接口专用
     * 其它访问http数据接口可以使用上方的方法getHttp
     */
    public static String getURLContent(String urlStr) {
        /** 网络的url地址 */
        URL url = null;
        /** http连接 */
        HttpURLConnection httpConn = null;
        /**//** 输入流 */
        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        try {
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String str = null;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception ex) {
        		
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        String result = sb.toString();
       // System.out.println(result);
        return result;
    }
}