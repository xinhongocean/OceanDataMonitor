package net.xinhong.oceanmonitor.common.ftp;

import com.jcraft.jsch.*;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.hibernate.loader.custom.Return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Description: <br>操作sftp主要是jsch包，连接部分主要是Session；功能部分主要是channel
 *              （ChannelSftp封装好的常用功能，ChannelExec执行命令行）
 *
 * @author 作者 <a href=ds.lht@163.com>stone</a>
 * @version 创建时间：2016/8/9.
 */
public class SFTPUtil {

    private static Logger logger = LoggerFactory.getLogger(SFTPUtil.class);

    //调用demo
    public static void main(String[] args) {
        Channel channel = null;
        ChannelSftp sftp = null;
        Session session = null;
        try {
            session = getConnection("","","",0,0);
            channel = session.openChannel("sftp");
            channel.connect();
            sftp =(ChannelSftp) channel;
            SFTPUtil.getFileNames("" , sftp);   //调用 SFTPUtil 的各个方法
        } catch (JSchException e) {
            e.printStackTrace();
        }finally {
            session.disconnect();
            channel.disconnect();
        }
    }
    public static Session getConnection(String host, String user, String password,
                                           int port, int timeOut) throws JSchException {
        Session session = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            //设置第一次登陆的时候提示，可选值:(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //30秒连接超时
            session.connect(timeOut);
        } catch (JSchException e) {
            logger.error("获取SFTP连接发生异常！地址：{}", host, e);
            throw e;
        }
        return session;
    }


    /**
     * sftp上传文件(夹)
     *
     * @param directory
     * @param sftp
     * @param uploadFile
     * @throws Exception
     */
    protected static void upload(String directory, ChannelSftp sftp, String... uploadFile) throws Exception {

        createDir(directory, sftp);
        for (String fileStr : uploadFile) {
            File file = new File(fileStr);
            InputStream ins = null;
            if (file.exists()) {
                //这里有点投机取巧，因为ChannelSftp无法去判读远程linux主机的文件路径,无奈之举
                try {
                    //进入目标路径
                    sftp.cd(directory);

                    if (file.isFile()) {
                        ins = new FileInputStream(file);
                        //中文名称的
                        sftp.put(ins, new String(file.getName().getBytes(), "UTF-8"));
                    } else {
                        logger.warn("{}是文件夹，不支持文件夹上传!", fileStr);
                    }
                } catch (SftpException e) {
                    logger.error("文件上传失败,文件路径：{}", fileStr, e);
                    throw e;
                } finally {
                    if (ins != null) {
                        ins.close();
                    }
                }
            } else {
                throw new FileNotFoundException("上传的文件不存在，" + fileStr);
            }
        }
    }

    private static void createDir(String directory, ChannelSftp sftp) throws Exception {
        try {
            if (directory != null) {
                String[] dirs = directory.split("/");
                StringBuffer dirsb = new StringBuffer("/");
                if (dirs != null && dirs.length > 0) {
                    for (String dir : dirs) {
                        if (dir == null || dir.trim().equals(""))
                            continue;
                        dirsb.append(dir + "/");
                        if (!isDirExist(dirsb.toString(), sftp)) {
                            sftp.mkdir(dirsb.toString());
                        }
                        sftp.cd(dirsb.toString());
                    }
                }
            }
        } catch (SftpException e) {
            logger.error("创建目录异常！", e);
            throw e;
        }
    }

    /**
     * 只能判断 文件夹 是否存在
     * @param directory
     * @param sftp
     * @return
     */
    public static boolean isDirExist(String directory, ChannelSftp sftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
//            sftpATTRS = sftp.stat(directory);
            return sftpATTRS.isDir();
        } catch (Exception e) {
            //目录不存在
            isDirExistFlag = false;
        }
        return isDirExistFlag;
    }

    /**
     * 获取所有子目录的名字
     * @param directory
     * @param sftp
     * @return
     */
    public static List<String> getFileNames(String directory, ChannelSftp sftp){
        List<String> list = new ArrayList<>();
        try {
            Vector<ChannelSftp.LsEntry> v = sftp.ls(directory);
            for (ChannelSftp.LsEntry ch:v) {
                list.add(ch.getFilename());
            }
        } catch (Exception e) {
            logger.error("进入文件夹失败");
        }
        return list;
    }
    /**
     * 获取所有子目录 文件 (不包含文件夹) 的名字
     * @param directory
     * @param sftp
     * @return
     */
    public static List<String> getOnlyFileNames(String directory, ChannelSftp sftp ){
        List<String> list = new ArrayList<>();
        try {
            Vector<ChannelSftp.LsEntry> v = sftp.ls(directory);
            for (ChannelSftp.LsEntry ch:v) {
                String temp = ch.getFilename();
                if(isDirExist(directory+"/"+temp , sftp))continue;
                list.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //带有结尾标记的文件过滤
    public static List<String> getOnlyFileNames(String directory, ChannelSftp sftp ,String fileStyle){
        List<String> list = new ArrayList<>();
        try {
            Vector<ChannelSftp.LsEntry> v = sftp.ls(directory);
            for (ChannelSftp.LsEntry ch:v) {
                String temp = ch.getFilename();
                if(isDirExist(directory+"/"+temp , sftp))continue;
                if(temp.endsWith(fileStyle)||temp.substring(temp.length()-4).contains(fileStyle))
                list.add(temp);
            }
        } catch (Exception e) {
            logger.error("进入文件夹失败");
        }
        return list;
    }
    //封装了解释预报的某个类型的文件读取
    public static List<String> getOnlyFileNames_JSYB(String directory, ChannelSftp sftp ,String fileStyle){
        List<String> result = new ArrayList<>();
        try {
            Vector<ChannelSftp.LsEntry> v = sftp.ls(directory);
            for (ChannelSftp.LsEntry ch:v) {
                String filename = ch.getFilename();
                if (filename.contains("XHGFS_G_DB_") && filename.length() ==28 )
                    result.add(filename);
            }
        } catch (SftpException e) {
            logger.error("进入文件夹失败");
        }
        return result;
    }
    // TODO: 2018/7/3 执行exe的方法工具

}
