package com.yyh.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

/**
 * 系统本机环境变量参数
 * @author ningquan
 *
 */
public abstract class SystemEnvParams {
	private static final Logger log = LoggerFactory.getLogger(SystemEnvParams.class);
	/**主机名称*/
	public static String HOST_NAME 	 = "";
	/**主机IP地址*/
	public static String HOST_ADDRESS ="";
	/**主机MAC地址*/
	public static String MAC_ADDRESS = "";
	static{
		try {
			InetAddress ia = InetAddress.getLocalHost();
			if(ia == null){
				throw new RuntimeException("当前服务器ipAddress配置错误，请检查服务器配置！");
			}
			HOST_NAME = ia.getHostName();
			HOST_ADDRESS = ia.getHostAddress();
			MAC_ADDRESS = getLocalMacAddress(ia);
		} catch (UnknownHostException e) {
			log.error("error",e);
		}
	}
	
	/**
	 * 获取当前机器的mac地址
	 * @author ningquan
	 * @throws Exception
	 */
	public static String getMacAddress()
	{
		try {
			InetAddress ia = InetAddress.getLocalHost();
			if(ia == null){
				throw new RuntimeException("当前服务器ipAddress配置错误，请检查服务器配置！");
			}
			HOST_NAME = ia.getHostName();
			HOST_ADDRESS = ia.getHostAddress();
			String localStr = getLocalMacAddress(ia);
			log.info("localStr===:{}",localStr);
			return localStr;
		} catch (UnknownHostException e) {
			log.error("error",e);
			return "";
		}
	}
	public static String getLocalMacAddress(InetAddress ia){
		String  OS_TYPE=System.getProperties().getProperty("os.name").toLowerCase();
		boolean LIUX_OS = OS_TYPE.contains("linux")?true:false;
		if(LIUX_OS){
			return getLinuxMacAddress();
		}
		return getWindowsMacAddress(ia);
	}
	
	public static String  getWindowsMacAddress(InetAddress ia){
		try {
			byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
			StringBuffer sb = new StringBuffer("");
			for(int i=0; i<mac.length; i++) {
				if(i!=0) {
					sb.append("-");
				}
				int temp = mac[i]&0xff;
				String str = Integer.toHexString(temp);
				if(str.length()==1) {
					sb.append("0"+str);
				}else {
					sb.append(str);
				}
			}
			return sb.toString().toUpperCase();
		} catch (Exception e) {
			log.error("error",e);
		}
		return null;
	}
	
	public static String getWindowsMacAddress(){
		 String address = "";
		 try {
             String command = "cmd.exe /c ipconfig /all";
             Process p = Runtime.getRuntime().exec(command);
             BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(),"UTF-8"));
             String line;
             while ((line = br.readLine()) != null) {
                 if (line.indexOf("Physical Address") > 0) {
                     int index = line.indexOf(":");
                     index += 2;
                     address = line.substring(index);
                     break;
                 }
             }
             br.close();
             return address.trim();
         } catch (IOException e) {
        	 log.error("error",e);
         }
		return address;
	}
	public static String getLinuxMacAddress(){
		String address = "";
		String command = "/bin/sh -c ifconfig -a";
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(),"UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.indexOf("HWaddr") > 0) {
                    int index = line.indexOf("HWaddr") + "HWaddr".length();
                    address = line.substring(index);
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
        	log.error("error",e);
        }
        log.info("linux address:{}",address);
        return address;
	}
	public static String getLinuxMacAddress2() {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try {            
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream(),"UTF-8"));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                index = line.toLowerCase().indexOf("HWaddr");
                if (index != -1) {
                    mac = line.substring(index + 4).trim();
                    break;
                }
            }
        } catch (IOException e) {
        	log.error("error",e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
            	log.error("error",e1);
            }
            bufferedReader = null;
            process = null;
        }
        return mac;
    }
}