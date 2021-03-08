import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

public class Sftp {
	private static final String PASS = "oracle";
	private static final String KNOWN_HOSTS = "C:\\Users\\Mike\\Documents\\KNOWN_HOSTS";

	private static ChannelSftp getChannel() throws JSchException {
		JSch jsch = new JSch();
		jsch.setKnownHosts(KNOWN_HOSTS);
		final String user = "oracle";
		final String host = "10.32.11.129";
		final int port = 22;
		Session session = jsch.getSession(user, host, port);
		session.setPassword(PASS);
		UserInfo ui = new MyUserInfo();
		session.setUserInfo(ui);
		session.connect();
		Channel channel = session.openChannel("sftp");
		channel.connect();
		return (ChannelSftp) channel;
	}

	private static void finalizeConnection(ChannelSftp channel, Session session) {
		channel.exit();
		session.disconnect();
	}

	

	public static class MyUserInfo implements UserInfo {
		public String getPassword() {
			return passwd;
		}

		public boolean promptYesNo(String str) {
			// TODO: enter sane code
			return true;
		}

		String passwd;

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			throw new UnsupportedOperationException();
		}

		public void showMessage(String message) {
			throw new UnsupportedOperationException("No dialogs suppported");
		}
	}

	public static String get(String src, String dst) {
		return doCommand("get", src, dst);
	}

	public static String doCommand(String command, String src, String dst) {
		String errMsg = null;
		ChannelSftp c = null;
		Session s = null;
		try {
			c = getChannel();
			s = c.getSession();
			if (command.equals("get")) {
				c.get(src, dst);
			} else {
				c.put(src, dst);
			}
		} catch (SftpException e) {
			errMsg = e.toString();
			System.out.println(e.toString());
		} catch (JSchException e) {
			errMsg = e.toString();
			System.out.println(e.toString());
		} finally {
			finalizeConnection(c, s);
		}
		return errMsg == null ? "Ok" : errMsg;
	}

	public static String put(String src, String dst) {
		return doCommand("put", src, dst);
	}

	
}