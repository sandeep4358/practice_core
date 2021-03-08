import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
/**
 * @author sandeep.kumar
 * This class will handled the SFTP interaction with remove SFTP location.
 */
public class SFTPUtility01 {
    public static String VR_SUCCESSFUL="SUCCESS_PUT_GET";
    public static String VR_EXCEPTION="SFTP_EXCEPTION_JSCH_JAVA :: ";
    public static String VR_DIR_NOT_FOUND="SFTP_DIR_NOT_FOUND ::";
    
    public static void main(String args[]) {
         //System.out.println(SFTP_GET_File("FTP.CVT.INTTRA.COM", "a0257199", "0rFUUXpM","/outbound/IFTMIN","/localfilelocation"));         
       // System.out.println(SFTP_PUT_File("FTP.CVT.INTTRA.COM", "a0257199", "0rFUUXpM","/localfilelocation/Close_BL_ISSUE_UAT - Copy (5).edi","/outbound/IFTMIN/Close_BL_ISSUE_UAT - Copy (5).edi","/outbound/IFTMIN"));
         
         System.out.println(SFTP_GET_File("FTP.CARGOO.COM", "arkastest", "48ioJXNCwVptYG","/inbox/iftmbf","/localfilelocation"));

    }

    
    /**
     * @param hostname (remove IP address of the client location.)
     * @param username (credential username)
     * @param password (credential password).
     * @param sftp_server_path remove    (SFTP path of the client where the file is to be send of pick.)
     * @param processing_dir_local_path    (Processing directory path a local server where relational database is install.)
     */
    public static String SFTP_PUT_File(String hostname, String username, String password, String local_dir_fileName,
            String sftp_file_name,String remoteDirecory) {
        JSch ssh = new JSch();
        String resultString = "FILE_PLACED_SUCCEFULLY";
        String result ="";
        Session session = null;
         try {
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");

                session = ssh.getSession(username, hostname, 22);
                session.setConfig(config);
                session.setPassword(password);
                session.connect();
                
                checkDirectoryPresent(session, remoteDirecory); // check the Sftp directory present or not if not then throw exception
                
                result = doCommand("put", local_dir_fileName, sftp_file_name, session);                    
                if (!result.equals(VR_SUCCESSFUL))                    
                 throw new Exception(result);       
                                
            }catch (JSchException e) {                
                resultString = VR_EXCEPTION+e.getMessage();                
            }catch (IOException e) {
                resultString = VR_EXCEPTION+e.getMessage();
            }catch(Exception e) {
                resultString = VR_EXCEPTION+e.getMessage();
            }
         
         if (isSessionConnected(session)) {
             session.disconnect();
         }
        
         return resultString;

    }
    
    /**
     * @param hostname (remove IP address of the client location.)
     * @param username (credential username)
     * @param password (credential password).
     * @param sftp_server_path remove    (SFTP path of the client where the file is to be send of pick.)
     * @param processing_dir_local_path    (Processing directory path a local server where relational database is install.)
     */
    @SuppressWarnings("unchecked")
    public static String SFTP_GET_File(String hostname, String username, String password, String sftp_server_path,
            String processing_dir_local_path ) {
        JSch ssh = new JSch();
        String resutlString = "NO_FILE_PRESENT";
        String sftp_server_fileName = "";
        String local_processing_dir_fileName = "";
        Session session = null;

         try {
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");

                session = ssh.getSession(username, hostname, 22);
                session.setConfig(config);
                session.setPassword(password);
                session.connect();
                //condition check
                checkDirectoryPresent(session, sftp_server_path); //will check weather the directory present or not if not then throw error
                
                List<String> fileList = getFileList(sftp_server_path, session);
                        if(fileList.size()>0) { 
                            for (int counter = 0; counter < fileList.size(); counter++) {
                                  resutlString= fileList.get(0);  // will fetch the first file in the ArrayList; Now it contain the file name
                            }
                            sftp_server_fileName = sftp_server_path + "/" + resutlString;
                            local_processing_dir_fileName = processing_dir_local_path + "/" + resutlString;
                            String result = doCommand("get", sftp_server_fileName, local_processing_dir_fileName, session);
                            if (result.equals(VR_SUCCESSFUL))
                                removeFile(sftp_server_fileName, session); // function will remove the file once it is cleared.
                            else
                                throw new Exception(result);
                       } 
                    System.out.println(resutlString);                    
                                
            }catch (JSchException e) {                
                resutlString =VR_EXCEPTION+e.getMessage();                
            }catch (IOException e) {
                resutlString =VR_EXCEPTION+e.getMessage();
            }catch(Exception e) {
                resutlString =VR_EXCEPTION+e.getMessage();
            }
         
         if (isSessionConnected(session)) {
             session.disconnect();  // disconnect the session
         }
         
         return resutlString;
    }

    

    /**
     * Executes ls in remote sftp server and get the list of files in the specified
     * folder, here path is the path to remote folder where we want to excutes the
     * ls command
     **/

    /**
     * 
     * @param sftp_server_path
     * @param session
     * @return Will provide the list of the file that are in the SFTP sever location(remote location). 
     * @throws IOException
     * 
     * Executes ls in remote sftp server and get the list of files in the specified
     * folder, here path is the path to remote folder where we want to executes the
     * @throws JSchException 
     */
    
    @SuppressWarnings("unchecked")
    public static  List<String> getFileList(String sftp_server_path, Session session) throws IOException, JSchException {
        List<String> fileList = null;
        ChannelSftp channelSftp = getChannelToSftpServer(session);
        if (channelSftp != null) {
            try {
                Vector<String> _channelVector = channelSftp.ls(sftp_server_path);  // this command will provide the list of the file in SFTP location.
                if (_channelVector != null) {
                    fileList = new ArrayList<String>();
                    for (int i = 0; i < _channelVector.size(); i++) {
                        Object obj = _channelVector.elementAt(i);
                        if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
                            LsEntry _file = (LsEntry) obj;
                            if (!_file.getAttrs().isDir()) // only file will be
                            {
                                fileList.add(_file.getFilename());                                
                                //System.out.printf("Reading file : %s%n", _file.getFilename());                                
                            } 
                        }
                    }
                }
            }
            catch (SftpException e) {
                throw new IOException(" 4358004::"+e.getMessage());
            } catch (Exception e) {
                throw new IOException(" 4358004::"+e.getMessage());
            } finally {
                disconnectChanneltoSftpServer(channelSftp);
            }
        }
        return fileList;
    }
    
    /**
     * 
     * @param command        (get :- Will fetch the file from remote directory to the local directory)(put :-  will put the file in the remote directory)
     * @param src            (source :-  Where file is picked, weather is it local processing or remote SFTP location)
     * @param dst             (destination :-  Where file is picked, weather is it local processing or remote SFTP location)
     * @param session
     * @return
     * @throws IOException
     * @throws JSchException 
     */
    public static String doCommand(String command, String src, String dst, Session session) throws IOException,SftpException, JSchException {
        String errMsg = null;
        ChannelSftp channelSftp =  null;
        try {
            channelSftp =  getChannelToSftpServer(session);
            if (command.equals("get")) {
                channelSftp.get(src, dst);
            } else {
                channelSftp.put(src, dst);
            }
        } catch (SftpException e) {
            //e.printStackTrace();
            errMsg = "4358005:: Fail to PUT/GET file ( "+e.toString()+" ) ";            
        }  finally {
            disconnectChanneltoSftpServer(channelSftp);
        }
        return errMsg == null ? VR_SUCCESSFUL : errMsg;
    }
        

    /**
     * 
     * @param fileName
     * @param session
     * @return
     * @throws Exception 
     */
    public static String removeFile(String fileName, Session session) throws Exception {
        String errMsg = null;
        ChannelSftp channelSftp =  null;
        try {
            channelSftp =  getChannelToSftpServer(session);            
            channelSftp.rm(fileName);
            
        }catch (Exception e) {
            throw new Exception(" 4358002 :: File Removing Error");
        }
        finally {
            disconnectChanneltoSftpServer(channelSftp);
        }
        return  "RMF"; // will maintain the code for futue
    }
        
    /**
     * 
     * @param session
     * @return
     */
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
                throw new IOException(VR_EXCEPTION+"4358003:: Falied to create channel " + e.getMessage());
            }
        }
        return channelSftp;
    }

    /**
     * close disconnect the sftp channel
     **/

    private static void disconnectChanneltoSftpServer(ChannelSftp channelSftp) throws JSchException {
        if (channelSftp != null) {
            channelSftp.disconnect();
        }
    }
    
    /**
     * method will check if the directory(remote only) is present or not
     * @param session
     * @param directory
     * @throws Exception 
     */
    private static void checkDirectoryPresent(Session session,String directory) throws Exception {
    ChannelSftp channelSftp=null;
        try {
            channelSftp =  getChannelToSftpServer(session);    
            SftpATTRS stat = channelSftp.stat(directory);
        } catch (Exception e) {
              throw new Exception("4358001 :: "+VR_DIR_NOT_FOUND);
        }
        //return errorMessage;
    }
}
;

