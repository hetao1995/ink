package xyz.itao.ink.utils;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @author hetao
 * @date 2018-12-01 23:11
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
     * @param imageFile file
     * @return 是否是图片
     */
    public static boolean isImage(InputStream imageFile) {
        try {
            Image img = ImageIO.read(imageFile);
            return img != null && img.getWidth(null) > 0 && img.getHeight(null) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取文件名后缀
     *
     * @param fname 文件名
     * @return 后缀
     */
    public static String fileExt(String fname) {
        return StringUtils.isNoneEmpty(fname) && fname.indexOf('.') != -1 ? fname.substring(fname.lastIndexOf('.') + 1) : null;
    }

    /**
     * 根据stream获取后缀名称
     *
     * @param inputStream 输入流
     * @return ext
     * @throws IOException exception
     */
    public static String fileExt(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        byte[] magic = new byte[28];
        int read = inputStream.read(magic, 0, 28);
        String magicStr = DatatypeConverter.printHexBinary(magic);
        for (FileType fileType : FileType.values()) {
            if (magicStr.startsWith(fileType.getMagic())) {
                return fileType.getSuffix();
            }
        }
        return null;
    }

    public static String fileExt(InputStream inputStream, String fname) throws IOException {
        String ext = fileExt(inputStream);
        if (ext == null) {
            ext = fileExt(fname);
        }
        return ext;
    }
}

/**
 * 文件类型枚取
 */
enum FileType {

    /**
     * JEPG.
     */
    JPEG("FFD8FF", "jpg"),

    /**
     * PNG.
     */
    PNG("89504E47", "png"),

    /**
     * GIF.
     */
    GIF("47494638", "gif"),

    /**
     * TIFF.
     */
    TIFF("49492A00", "tiff"),

    /**
     * Windows Bitmap.
     */
    BMP("424D", "bmap"),

    /**
     * CAD.
     */
    DWG("41433130", "dwg"),

    /**
     * Adobe Photoshop.
     */
    PSD("38425053", "psd"),

    /**
     * Rich Text Format.
     */
    RTF("7B5C727466", "rtf"),

    /**
     * XML.
     */
    XML("3C3F786D6C", "xml"),

    /**
     * HTML.
     */
    HTML("68746D6C3E", "html"),

    /**
     * Email [thorough only].
     */
    EML("44656C69766572792D646174653A", "eml"),

    /**
     * Outlook Express.
     */
    DBX("CFAD12FEC5FD746F", "dbx"),

    /**
     * Outlook (pst).
     */
    PST("2142444E", "pst"),

    /**
     * MS Word/Excel.
     */
    XLS_DOC("D0CF11E0", "xls"),

    /**
     * MS Access.
     */
    MDB("5374616E64617264204A", "mdb"),

    /**
     * WordPerfect.
     */
    WPD("FF575043", "wpd"),

    /**
     * Postscript.
     */
    EPS("252150532D41646F6265", "eps"),

    /**
     * Adobe Acrobat.
     */
    PDF("255044462D312E", "pdf"),

    /**
     * Quicken.
     */
    QDF("AC9EBD8F", "qdf"),

    /**
     * Windows Password.
     */
    PWL("E3828596", "pwl"),

    /**
     * ZIP Archive.
     */
    ZIP("504B0304", "zip"),

    /**
     * RAR Archive.
     */
    RAR("52617221", "rar"),

    /**
     * Wave.
     */
    WAV("57415645", "wav"),

    /**
     * AVI.
     */
    AVI("41564920", "avi"),

    /**
     * Real Audio.
     */
    RAM("2E7261FD", "ram"),

    /**
     * Real Media.
     */
    RM("2E524D46", "rm"),

    /**
     * MPEG (mpg).
     */
    MPG("000001BA", "mpg"),

    /**
     * Quicktime.
     */
    MOV("6D6F6F76", "mov"),

    /**
     * Windows Media.
     */
    ASF("3026B2758E66CF11", "asf"),

    /**
     * MIDI.
     */
    MID("4D546864", "midi");

    private String magic;
    private String suffix;


    /**
     * Constructor.
     */
    FileType(String magic, String suffix) {
        this.magic = magic;
        this.suffix = suffix;
    }

    public String getMagic() {
        return magic;
    }

    public String getSuffix() {
        return suffix;
    }
}
