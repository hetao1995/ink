package xyz.itao.ink.utils;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @author hetao
 * @date 2018-12-01 23:11
 * @description
 */
public class FileUtils {
    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            assert inputChannel != null;
            inputChannel.close();
            assert outputChannel != null;
            outputChannel.close();
        }
    }
    public static String flowAutoShow(double value) {
        // Math.round 方法接收 float 和 double 类型,如果参数是 int 的话,会强转为 float,这个时候调用该方法无意义
        int kb = 1024;
        int mb = 1048576;
        int gb = 1073741824;
        double abs = Math.abs(value);
        if (abs > gb) {
            return Math.round(value / gb) + "GB";
        } else if (abs > mb) {
            return Math.round(value / mb) + "MB";
        } else if (abs > kb) {
            return Math.round(value / kb) + "KB";
        }
        return Math.round(value) + "";
    }

    /**
     * 判断文件是否是图片类型
     *
     * @param imageFile
     * @return
     */
    public static boolean isImage(InputStream imageFile) {
        try {
            Image img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件名后缀
     * @param fname 文件名
     * @return 后缀
     */
    public static String fileExt(String fname) {
        return StringUtils.isNoneEmpty(fname) && fname.indexOf('.') != -1 ? fname.substring(fname.lastIndexOf('.') + 1) : null;
    }
}
