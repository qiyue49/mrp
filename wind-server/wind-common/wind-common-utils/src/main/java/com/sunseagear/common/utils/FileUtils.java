package com.sunseagear.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作类
 *
 * @author Administrator
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    protected static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static final String[] IMAGES_SUFFIXES = {"bmp", "jpg", "jpeg", "gif", "png", "tiff"};

    public static boolean isAbsolutePath(String path) {
        return path.startsWith("/") || path.contains(":");
    }

    // 创建单个文件
    public static File createFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {// 判断文件是否存在
            return file;
        }
        if (file.isDirectory()) {// 判断文件是否为目录
            throw new RuntimeException("目标文件不能为目录");
        }
        if (!file.getParentFile().exists()) {// 判断目标文件所在的目录是否存在
            if (!file.getParentFile().mkdirs()) {// 判断创建目录是否成功
                return null;
            }
        }
        try {
            file.createNewFile();
        } catch (IOException e) {// 捕获异常
            e.printStackTrace();
            throw new RuntimeException("创建文件失败");
        }
        return file;
    }

    // 创建临时文件
    public static String createTempFile(String prefix, String suffix, String dirName) {
        File tempFile = null;
        if (dirName == null) {// 目录如果为空
            try {
                tempFile = File.createTempFile(prefix, suffix);// 在默认文件夹下创建临时文件
                return tempFile.getCanonicalPath();// 返回临时文件的路径
            } catch (IOException e) {// 捕获异常
                e.printStackTrace();
                System.out.println("创建临时文件失败：" + e.getMessage());
                return null;
            }
        } else {
            // 指定目录存在
            File dir = new File(dirName);// 创建目录
            if (!dir.exists()) {
                // 如果目录不存在则创建目录
                if (FileUtils.mkDir(dirName)) {
                    System.out.println("创建临时文件失败，不能创建临时文件所在的目录！");
                    return null;
                }
            }
            try {
                tempFile = File.createTempFile(prefix, suffix, dir);// 在指定目录下创建临时文件
                return tempFile.getCanonicalPath();// 返回临时文件的路径
            } catch (IOException e) {// 捕获异常
                e.printStackTrace();
                System.out.println("创建临时文件失败!" + e.getMessage());
                return null;
            }
        }
    }

    /**
     * 在指定的位置创建指定的文件
     *
     * @param filePath 完整的文件路径
     * @param mkdir    是否创建相关的文件夹
     */
    public static void mkFile(String filePath, boolean mkdir) throws Exception {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        file.createNewFile();
        file = null;
    }

    /**
     * 在指定的位置创建文件夹
     *
     * @param dirPath 文件夹路径
     * @return 若创建成功，则返回True；反之，则返回False
     */
    public static boolean mkDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists()) {// 判断目录是否存在
            System.out.println("创建目录失败，目标目录已存在！");
            return false;
        }
        if (!dirPath.endsWith(File.separator)) {// 结尾是否以"/"结束
            dirPath = dirPath + File.separator;
        }
        if (dir.mkdirs()) {// 创建目标目录
            System.out.println("创建目录成功！" + dirPath);
            return true;
        } else {
            System.out.println("创建目录失败！");
            return false;
        }
    }

    /**
     * 删除指定的文件
     *
     * @param filePath 文件路径
     * @return 若删除成功，则返回True；反之，则返回False
     */
    public static boolean delFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {// 路径为文件且不为空则进行删除
            file.delete();// 文件删除
            return true;
        }
        return false;
    }

    /**
     * 删除指定的文件目录，可以是文件也可以是文件夹
     *
     * @param filePath 文件路径
     * @return 若删除成功，则返回True；反之，则返回False
     */
    public static boolean delDir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return true;
        }

        String[] list = file.list();
        if (list == null) {
            return file.delete();
        }

        for (String s : list) {
            delDir(filePath + File.separator + s);
        }

        return file.delete();
    }

    /**
     * 复制文件/文件夹 若要进行文件夹复制，请勿将目标文件夹置于源文件夹中
     *
     * @param source   源文件（夹）
     * @param target   目标文件（夹）
     * @param isFolder 若进行文件夹复制，则为True；反之为False
     */
    public static void copy(String source, String target, boolean isFolder) throws IOException {
        if (isFolder) {
            Files.createDirectories(Paths.get(target));
            File a = new File(source);
            String[] files = a.list();
            for (String s : Objects.requireNonNull(files)) {
                String srcFilePath = source.endsWith(File.separator) ? source + s : source + File.separator + s;
                String destFilePath = target + File.separator + s;
                File temp = new File(srcFilePath);
                if (temp.isFile()) {
                    Files.copy(temp.toPath(), new File(destFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else if (temp.isDirectory()) {
                    copy(srcFilePath, destFilePath, true);
                }
            }
        } else {
            File oldFile = new File(source);
            if (oldFile.exists()) {
                Files.copy(oldFile.toPath(), new File(target).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /**
     * 移动指定的文件（夹）到目标文件（夹）
     *
     * @param source   源文件（夹）
     * @param target   目标文件（夹）
     * @param isFolder 若为文件夹，则为True；反之为False
     */
    public static boolean move(String source, String target, boolean isFolder) throws Exception {
        copy(source, target, isFolder);
        if (isFolder) {
            return delDir(source);
        } else {
            return delFile(source);
        }
    }

    /**
     * 将文件转字节数组
     */
    public static byte[] toByteArray(String filePath) throws IOException {
        FileInputStream in = null;
        File file = new File(filePath);
        in = new FileInputStream(file);
        byte[] bytes = IOUtils.toByteArray(in);
        in.close();
        return bytes;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void saveFile(byte[] bfile, String filePath) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Objects.requireNonNull(createFile(filePath))))) {
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件写入失败");
        }
    }

    public static String[] readDirFilesName(String filePath, boolean isAll) {
        String[] rs = null;
        List<File> fs = readDirFiles(filePath, isAll);
        int i;
        rs = new String[fs.size()];
        i = 0;
        for (File file : fs) {
            rs[i] = file.getName();
            i++;
        }

        return rs;
    }

    public static List<File> readDirFilesFilter(String filePath, boolean isAll, String filterName) {
        List<File> fs = readDirFiles(filePath, isAll);
        List<File> rs = new ArrayList<>();
        for (File file : fs) {
            String fName = file.getName();
            if (!fName.isEmpty() && fName.toLowerCase().endsWith(filterName.toLowerCase())) {
                rs.add(file);
            }
        }

        return rs;
    }

    public static List<File> readDirFiles(String filepath, boolean isAll) {
        List<File> fs = new ArrayList<>();
        try {
            File file = new File(filepath);
            if ((!isAll) && (file.isDirectory())) {
                fs.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
                return fs;
            }
            if (!file.isDirectory()) {
                fs.add(file);
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (String s : Objects.requireNonNull(filelist)) {
                    File readfile = new File(filepath + "\\" + s);
                    if (!readfile.isDirectory()) {
                        fs.add(readfile);
                    } else if (readfile.isDirectory()) {
                        readDirFiles(filepath + "\\" + s, isAll);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return fs;
    }

    public static void zipFile(String srcFileName, String zipFileName) {
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
            fileZip(out, new File(srcFileName), "");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fileZip(ZipOutputStream out, File file, String base) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            base = base + "/";
            for (File tmpFile : Objects.requireNonNull(files)) {
                fileZip(out, tmpFile, base + tmpFile.getName());
            }
        } else {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file), 10240)) {
                out.putNextEntry(new ZipEntry(base));

                byte[] buf = new byte[10240];
                int read;
                while ((read = bis.read(buf, 0, 10240)) != -1) {
                    out.write(buf, 0, read);
                }
            }
            out.closeEntry();
        }
    }

    public void unZipFile(String unZipBeforeFile, String unZipAfterFile) {
        try {
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(unZipBeforeFile))) {
                File f = new File(unZipAfterFile);
                f.mkdirs();
                fileUnZip(zis, f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fileUnZip(ZipInputStream zis, File file) throws Exception {
        ZipEntry zip;
        while ((zip = zis.getNextEntry()) != null) {
            String name = zip.getName();
            File f = new File(file.getAbsolutePath() + "/" + name);

            if (zip.isDirectory()) {
                f.mkdirs();
            } else {
                f.createNewFile();
                try (FileOutputStream fos = new FileOutputStream(f)) {
                    byte[] b = new byte[10240];
                    int aa;
                    while ((aa = zis.read(b)) != -1) {
                        fos.write(b, 0, aa);
                    }
                }
            }
            fileUnZip(zis, file);
        }
    }

    public static byte[] getFileContent(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new byte[0];
        }

        try (InputStream fr = new FileInputStream(file)) {
            byte[] content = new byte[(int) file.length()];

            int length = fr.read(content);
            if (length == file.length()) {
                return content;
            } else {
                logger.info("File: " + filePath + " getFileContent error. Only read " + length + " bytes out of " + file.length());
                return new byte[0];
            }
        } catch (IOException e) {
            logger.info("File: " + filePath + " getFileContent error", e);
        }

        return new byte[0];
    }

    public static String doCopyFile(String source, String targetPath) {
        File sFile = new File(source);
        String tFileName = sFile.getName();
        File targetFile = new File(targetPath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        if (!sFile.exists()) {
            System.err.println("需要复制的文件不存在，请检查！");
            return null;
        }

        try (FileInputStream fis = new FileInputStream(sFile);
             FileOutputStream outputStream = new FileOutputStream(targetPath + "\\" + tFileName);) {
            byte[] buff = new byte[2048];
            int temp = 0;
            while ((temp = fis.read(buff, 0, 2048)) != -1) {
                outputStream.write(buff, 0, temp);
            }
            return targetPath + "\\" + tFileName;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void fileDownload(HttpServletResponse response, String filePath, String fileName) throws Exception {
        byte[] data = toByteArray(filePath);
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);

        response.setContentType("application/x-download");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
        response.flushBuffer();
    }

    public static void safeCloseReader(Reader reader) {
        if (null != reader) {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error("close file error:" + e.getMessage(), e);
            }
        }
    }

    public static void safeCloseOutputStream(OutputStream out) {
        if (null != out) {
            try {
                out.close();
            } catch (IOException e) {
                logger.error("close file error:" + e.getMessage(), e);
            }
        }
    }

    public static void safeCloseInputStream(InputStream in) {
        if (null != in) {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("close file error:" + e.getMessage(), e);
            }
        }
    }

    public static void safeFlushAndCloseOutputStream(OutputStream out) {
        if (null != out) {
            try {
                out.flush();
            } catch (IOException e) {
                logger.error("close file error:" + e.getMessage(), e);
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("close file error:" + e.getMessage(), e);
                }
            }
        }
    }

    public static String getFileType(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }
        int pos = url.lastIndexOf(".") + 1;
        return url.substring(pos);
    }

    //BASE64解码成File文件
    public static boolean base64ToFile(String destPath, String base64, String fileName) {
        File dir = new File(destPath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }

        File file = new File(destPath + "/" + fileName);

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
             ByteArrayOutputStream baos = new ByteArrayOutputStream(Base64.getDecoder().decode(base64).length)) {

            baos.write(Base64.getDecoder().decode(base64));
            baos.writeTo(bos);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * @return String
     * @description 将文件转base64字符串
     * @date 2018年3月20日
     * @author changyl
     * File转成编码成BASE64
     */

    public static String fileToBase64(String path) {
        String base64 = null;
        try (InputStream in = new FileInputStream(new File(path));) {
            byte[] bytes = new byte[(int) new File(path).length()];
            in.read(bytes);
            base64 = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static String fileToBase64(MultipartFile multipartFile) {
        String base64 = null;
        try {
            base64 = Base64.getEncoder().encodeToString(multipartFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    /**
     * 是否是图片附件
     */
    public static boolean isImage(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return false;
        }
        return ArrayUtils.contains(IMAGES_SUFFIXES, FilenameUtils.getExtension(filename).toLowerCase());
    }

    public static void main(String[] args) {
//        String dirName = "E:/createFile/";// 创建目录
//        FileUtils.mkDir(dirName);// 调用方法创建目录
//        String fileName = dirName + "/file1.txt";// 创建文件
//        FileUtils.createFile(fileName);// 调用方法创建文件
//        String prefix = "temp";// 创建临时文件
//        String surfix = ".txt";// 后缀
//        for (int i = 0; i < 10; i++) {// 循环创建多个文件
//            System.out.println("创建临时文件: "// 调用方法创建临时文件
//                    + FileUtils.createTempFile(prefix, surfix, dirName));
//        }
        FileUtils.delDir("C:\\Users\\dothe\\Desktop\\源代码\\wind.zip");
    }


}
