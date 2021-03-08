package com.jcraft;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class TSISftpOracleFileTransfer {


	
    public static void main(){

        String hostname = "10.32.11.129";
        String login = "oracle";
        String password = "oracle";
        String path = "/home/oracle/edi/test/book_ftp";

        try {
        	java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            JSch ssh = new JSch();
            Session session = ssh.getSession(login, hostname, 22);
            session.setConfig(config);
            session.setPassword(password);
            session.connect();

            List<String> fileList = getFileList(path, session);
            for (String string : fileList) {
                System.out.println(string);
            }

            if (isSessionConnected(session)) {
                session.disconnect();
            }
            /*
             * Channel channel = session.openChannel("sftp"); channel.connect();
             * 
             * ChannelSftp sftp = (ChannelSftp) channel; sftp.cd(directory); Vector
             * files = sftp.ls("*");
             * 
             * 
             * System.out.printf("Found %d files in dir %s%n", files.size(),
             * directory);
             * 
             * for (ChannelSftp.LsEntry file : files) { if (file.getAttrs().isDir())
             * { continue; } System.out.printf("Reading file : %s%n",
             * file.getFilename()); BufferedReader bis = new BufferedReader(new
             * InputStreamReader(sftp.get(file.getFilename()))); String line = null;
             * while ((line = bis.readLine()) != null) { System.out.println(line); }
             * bis.close(); }
             * 
             * channel.disconnect(); session.disconnect();
             */
		} catch (JSchException e) {
		}catch (IOException e) {
		}
        
        

    }

    /**
     * To check if the session is still connected with remote server.
     **/
    public static boolean isSessionConnected(Session session) {
        return (session != null) && session.isConnected();
    }

    /**
     * Get a sftp channel from the session
     **/
    public static ChannelSftp getChannelToSftpServer(Session session) throws IOException {
        ChannelSftp channelSftp = null;
        if (isSessionConnected(session)) {
            try {
                Channel channel = session.openChannel("sftp");
                channel.connect();
                channelSftp = (ChannelSftp) channel;
            } catch (JSchException e) {
                throw new IOException("Falied to create channel " + e.getMessage());
            }
        }
        return channelSftp;
    }

    /**
     * close disconnect the sftp channel
     **/

    public static void disconnectChanneltoSftpServer(ChannelSftp channelSftp) {
        if (channelSftp != null) {
            channelSftp.disconnect();
        }
    }

    /**
     * Executes ls in remote sftp server and get the list of files in the
     * specified folder, here path is the path to remote folder where we want to
     * excutes the ls command
     **/

    public static List<String> getFileList(String path, Session session) throws IOException {
        List<String> fileList = null;
        ChannelSftp channelSftp = getChannelToSftpServer(session);
        if (channelSftp != null) {
            try {
                Vector<String> _channelVector = channelSftp.ls(path);
                if (_channelVector != null) {
                    fileList = new ArrayList<String>();
                    for (int i = 0; i < _channelVector.size(); i++) {
                        Object obj = _channelVector.elementAt(i);
                        if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                            LsEntry _file = (LsEntry) obj;
                            if (!_file.getAttrs().isDir()) // only file will be
                            {
                                fileList.add(_file.getFilename());
                                System.out.printf("Reading file : %s%n", _file.getFilename());
                                BufferedReader _bis = new BufferedReader(
                                        new InputStreamReader(channelSftp.get(path + "/" + _file.getFilename())));
                                String line = null;
                                while ((line = _bis.readLine()) != null) {
                                    System.out.println(line);
                                }
                                _bis.close();
                            } else {
                                System.out.printf("Reading file : %s%n", _file.getFilename());
                            }
                        }
                    }
                }
            } catch (SftpException e) {
                throw new IOException("Falied " + e.getMessage());
            } finally {
                disconnectChanneltoSftpServer(channelSftp);
            }
        }
        return fileList;
    }

    /**
     * 
     * @param fileName
     * @param session
     * @return put the file in the directories
     */
    public static boolean dropFileIntheDirecoty(String path, Session session) throws IOException {
        ChannelSftp channelSftp = null;
        List<String> fileList = null;

        try {
            channelSftp = getChannelToSftpServer(session);

            if (channelSftp != null) {
                try {
                    Vector<String> vv = channelSftp.ls(path);
                    if (vv != null) {
                        fileList = new ArrayList<String>();
                        for (int i = 0; i < vv.size(); i++) {
                            Object obj = vv.elementAt(i);
                            if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                                LsEntry entry = (LsEntry) obj;
                                if (!entry.getAttrs().isDir()) {
                                    fileList.add(entry.getFilename());
                                }
                            }
                        }
                    }
                } catch (SftpException e) {
                    throw new IOException("Falied " + e.getMessage());
                } finally {
                    disconnectChanneltoSftpServer(channelSftp);
                }
            }
        } catch (Exception e) {
            throw new IOException("Falied " + e.getMessage());
        } finally {
            disconnectChanneltoSftpServer(channelSftp);
        }

        return true;
    }

}

