package com.jcraft;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPUtility {

	private static ChannelSftp getChannel(String UserName,String Password,String IP_address) throws JSchException {
		JSch jsch = new JSch();
		final int port = 22;
        int iTimeOut  = 10000; // 10 second timeout
		Session session = jsch.getSession(UserName, IP_address, port);
		session.setPassword(Password);
		session.connect(iTimeOut);
		session.connect();
		Channel channel = session.openChannel("sftp");
		channel.connect();
		return (ChannelSftp) channel;
		
		/*try {
			
		}  catch (JSchException e) {
				return(“JSchException Error : ” + e.toString());

		} catch (SftpException e) {

			return(“SftpException Error : ” + e.toString());

		} catch (Exception e) {

			return(“Exception Error : ” + e.toString());

		}*/
		}

}
