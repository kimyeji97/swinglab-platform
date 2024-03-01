package com.dailystudy.swinglab.service.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * FileUtil 클래스 파일과 관련된 유틸리티
 */
@Slf4j
public class FileUtil
{

    private FileUtil()
    {
    }

    /**
     * 파일의 확장자를 추출한다.
     * 
     * @param file_name
     *            파일명
     * @return 파일 확장자명
     */
    public static String getExtension(String file_name)
    {
        if (file_name != null && !file_name.trim().equals(""))
        {
            String file_separator = "\\"; // Windows format
            if (file_name.startsWith("/"))
            { // Unix format
                file_separator = "/";
            }
            if (file_name.lastIndexOf(file_separator) != -1)
            {
                file_name = file_name.substring(file_name.lastIndexOf(file_separator) + 1);
            }
            String file_ext = "";
            if (file_name.lastIndexOf(".") != -1)
            {
                file_ext = file_name.substring(file_name.lastIndexOf("."));
                return file_ext;
            }
        }

        return "";
    }

    /**
     * 파일을 복사 한다.
     * 
     * @param src_fp
     *            원본 파일
     * @param dest_fp
     *            복사 대상 파일
     * @return 성공여부 (boolean)
     */
    public final static boolean copy(File src_fp, File dest_fp)
    {
        FileInputStream in = null;
        FileOutputStream out = null;
        BufferedInputStream inBuffer = null;
        BufferedOutputStream outBuffer = null;
        try
        {
            in = new FileInputStream(src_fp);
            out = new FileOutputStream(dest_fp);
            inBuffer = new BufferedInputStream(in);
            outBuffer = new BufferedOutputStream(out);

            int numofbytes = 0;
            byte[] buffer = new byte[8192];
            while ((numofbytes = inBuffer.read(buffer, 0, buffer.length)) > -1)
            {
                outBuffer.write(buffer, 0, numofbytes);
            }
            outBuffer.flush();
            // cleanupif files are not the same length
            if (src_fp.length() != dest_fp.length())
            {
                dest_fp.delete();
                return false;
            }

            return true;
        } catch (Exception e)
        {
            dest_fp.delete();
            return false;
        } finally
        {
            if (outBuffer != null)
            {
                try
                {
                    outBuffer.close();
                } catch (Exception ex)
                {
                } finally
                {
                    outBuffer = null;
                }
            }
            if (inBuffer != null)
            {
                try
                {
                    inBuffer.close();
                } catch (Exception ex)
                {
                } finally
                {
                    inBuffer = null;
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                } catch (Exception ex)
                {
                } finally
                {
                    out = null;
                }
            }
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (Exception ex)
                {
                } finally
                {
                    in = null;
                }
            }
        }
    }

    /**
     * 파일을 이동 한다.
     * 
     * @param src_fp
     *            원본 파일
     * @param dest_fp
     *            이동 대상 파일
     * @return 성공여부 (boolean)
     */
    public final static boolean move(File src_fp, File dest_fp)
    {
        if (src_fp.renameTo(dest_fp))
        {
            return true;
        }

        try
        {
            if (dest_fp.createNewFile())
            {
                // delete if copy was successful, otherwise move will fail
                if (copy(src_fp, dest_fp))
                {
                    return src_fp.delete();
                }
            }
        } catch (IOException ex)
        {
            return false;
        }

        return false;
    }

    /**
     * 파일을 이동 한다.
     * 
     * @param fp
     *            원본 파일
     * @param path
     *            이동 대상 Path
     * @return 성공여부 (boolean)
     */
    public static boolean move(File fp, String path)
    {
        File new_fp = new File(path);
        if (fp.renameTo(new_fp))
        {
            return true;
        }

        try
        {
            if (new_fp.createNewFile())
            {
                // delete if copy was successful, otherwise move will fail
                if (copy(fp, new_fp))
                {
                    return fp.delete();
                }
            }
        } catch (IOException ex)
        {
            return false;
        }

        return false;
    }

    /**
     * 디렉토리를 생성한다. (하위 디렉토리가 없다면 생성한다.)
     * 
     * @param path
     *            생성할 디렉토리 path
     * @return 성공여부 (boolean)
     */
    public static boolean makeDirs(String path)
    {
        if (path == null)
        {
            return false;
        }
        File fp = new File(path);
        if (fp.exists())
        {
            return true;
        }
        return fp.mkdirs();
    }

    /**
     * 디렉토리를 생성한다.
     * 
     * @param path
     *            생성할 디렉토리 path
     * @param create
     *            true이면 하위 디렉토리가 없다면 생성하고, false면 생성하지 않는다.
     * @return 성공여부 (boolean)
     */
    public static boolean makeDir(String path, boolean create)
    {
        if (path == null)
        {
            return false;
        }
        File fp = new File(path);

        if (create == true)
        {
            if (fp.exists())
            {
                return true;
            }
            return fp.mkdirs();
        } else
        {
            return fp.mkdir();
        }
    }

    /**
     * 파일을 삭제한다.
     * 
     * @param file
     *            삭제할 파일 path
     * @return 성공여부 (boolean)
     */
    public static boolean remove(String file)
    {
        if (file == null)
        {
            return true;
        }
        File fp = new File(file);

        if (fp.exists())
        {
            return fp.delete();
        }
        return true;
    }

    /**
     * 파일을 삭제한다.
     * 
     * @param fp
     *            삭제할 파일
     * @return 성공여부 (boolean)
     */
    public static boolean remove(File fp)
    {
        if (fp != null && fp.exists())
        {
            return fp.delete();
        }
        return true;
    }

    /**
     * 파일이 존재하는 지 확인한다.
     * 
     * @param file
     *            확인할 파일 Path
     * @return 존재 여부 (boolean)
     */
    public static boolean exists(String file)
    {
        if (file == null)
        {
            return false;
        }
        File fp = new File(file);
        return fp.exists();
    }

    /**
     * 파일의 크기를 확인한다.
     * 
     * @param file
     *            확인할 파일 Path
     * @return 파일크기
     */
    public static long size(String file)
    {
        if (file == null)
        {
            return 0;
        }
        File fp = new File(file);
        if (fp.exists())
        {
            return fp.length();
        }
        return 0;
    }

    /**
     * MD5 체크섬을 확인한다.
     * 
     * @param fp
     *            확인할 파일
     * @return 체크섬 byte
     */
    public static byte[] getMd5(File fp)
    {
        MessageDigest md = null;
        BufferedInputStream in = null;
        byte[] buffer = null;
        try
        {
            md = MessageDigest.getInstance("MD5");
            in = new BufferedInputStream(new FileInputStream(fp));
            int numofbytes = 0;
            buffer = new byte[8192];
            while ((numofbytes = in.read(buffer, 0, buffer.length)) > -1)
            {
                md.update(buffer, 0, numofbytes);
            }
            return md.digest();
        } catch (Exception ex)
        {
            return new byte[0];
        } finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (Exception ex)
                {
                } finally
                {
                    in = null;
                }
            }
            md = null;
            buffer = null;
        }
    }

    /**
     * MD5 체크섬을 확인한다.
     * 
     * @param filePath
     *            확인할 파일 Path
     * @return 체크섬 byte
     */
    public static byte[] getMd5(String filePath)
    {
        MessageDigest md = null;
        BufferedInputStream in = null;
        byte[] buffer = null;
        try
        {
            md = MessageDigest.getInstance("MD5");
            in = new BufferedInputStream(new FileInputStream(new File(filePath)));
            int numofbytes = 0;
            buffer = new byte[8192];
            while ((numofbytes = in.read(buffer, 0, buffer.length)) > -1)
            {
                md.update(buffer, 0, numofbytes);
            }
            return md.digest();
        } catch (Exception ex)
        {
            return new byte[0];
        } finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (Exception ex)
                {
                } finally
                {
                    in = null;
                }
            }
            md = null;
            buffer = null;
        }
    }

    /**
     * 파일의 내용을 byte로 리턴한다.
     * 
     * @param path
     *            확인할 파일 Path
     * @return 파일 내용 byte
     */
    public static byte[] toBytes(String path) throws Exception
    {
        if (path == null)
        {
            return new byte[0];
        }
        ByteArrayOutputStream bao = null;
        byte[] buffer = new byte[8192];
        int numOfBytes = -1;
        BufferedInputStream bsi = null;
        try
        {
            bao = new ByteArrayOutputStream();
            bsi = new BufferedInputStream(new FileInputStream(path));
            while ((numOfBytes = bsi.read(buffer, 0, buffer.length)) != -1)
            {
                bao.write(buffer, 0, numOfBytes);
            }
            return bao.toByteArray();
        } catch (Exception ex)
        {
            throw ex;
        } finally
        {
            if (bao != null)
            {
                bao.close();
            }
            bao = null;
            if (bsi != null)
            {
                bsi.close();
            }
            bsi = null;
        }
    }

    /**
     * 파일의 내용을 byte로 리턴한다.
     * 
     * @param in
     *            확인할 파일의 InputStream
     * @return 파일 내용 byte
     */
    public static byte[] streamToBytes(InputStream in) throws IOException
    {
        ByteArrayOutputStream bao = null;
        byte[] buffer = new byte[8192];
        int numOfBytes = -1;
        BufferedInputStream bsi = null;
        try
        {
            bao = new ByteArrayOutputStream();
            bsi = new BufferedInputStream(in);
            while ((numOfBytes = bsi.read(buffer, 0, buffer.length)) != -1)
            {
                bao.write(buffer, 0, numOfBytes);
            }
            return bao.toByteArray();
        } catch (Exception ex)
        {
            return new byte[0];
        } finally
        {
            if (bao != null)
            {
                bao.close();
            }
            bao = null;
            if (bsi != null)
            {
                bsi.close();
            }
            bsi = null;
        }
    }

    /**
     * 파일의 내용을 byte로 리턴한다.
     * 
     * @param fp
     *            확인할 파일
     * @return 파일 내용 byte
     */
    public static byte[] fileToBytes(File fp) throws IOException
    {
        ByteArrayOutputStream bao = null;
        byte[] buffer = new byte[8192];
        int numOfBytes = -1;
        BufferedInputStream bsi = null;
        try
        {
            bao = new ByteArrayOutputStream();
            bsi = new BufferedInputStream(new FileInputStream(fp));
            while ((numOfBytes = bsi.read(buffer, 0, buffer.length)) != -1)
            {
                bao.write(buffer, 0, numOfBytes);
            }
            return bao.toByteArray();
        } catch (Exception ex)
        {
            return new byte[0];
        } finally
        {
            if (bao != null)
            {
                bao.close();
            }
            bao = null;
            if (bsi != null)
            {
                bsi.close();
            }
            bsi = null;
        }
    }

    /**
     * 파일의 내용을 String 으로 리턴한다.
     * 
     * @param fp
     *            확인할 파일
     * @return 파일 내용
     */
    public static String fileToString(File fp) throws IOException
    {
        return fileToString(fp, Charset.forName("UTF-8"));
    }

    /**
     * 파일의 내용을 String 으로 리턴한다.
     * 
     * @param fp
     *            확인할 파일
     * @param charset
     *            charset
     * @return 파일 내용
     */
    public static String fileToString(File fp, Charset charset) throws IOException
    {
        ByteArrayOutputStream bao = null;
        byte[] buffer = new byte[8192];
        int numOfBytes = -1;
        BufferedInputStream bsi = null;
        try
        {
            bao = new ByteArrayOutputStream();
            bsi = new BufferedInputStream(new FileInputStream(fp));
            while ((numOfBytes = bsi.read(buffer, 0, buffer.length)) != -1)
            {
                bao.write(buffer, 0, numOfBytes);
            }
            return new String(bao.toByteArray(), charset);
        } catch (Exception ex)
        {
            return "";
        } finally
        {
            if (bao != null)
            {
                bao.close();
            }
            bao = null;
            if (bsi != null)
            {
                bsi.close();
            }
            bsi = null;
        }
    }

    /**
     * 파일을 저장한다.
     * 
     * @param path
     *            저장할 파일 path
     * @param bytes
     *            파일의 내용
     */
    public static void saveBytesToFile(String path, byte[] bytes) throws Exception
    {
        saveBytesToFile(path, bytes, false);
    }

    /**
     * 파일을 저장한다.
     * 
     * @param path
     *            저장할 파일 path
     * @param bytes
     *            파일의 내용
     * @param append
     *            존재하는 파일에 내용을 append 할지 여부
     */
    public static void saveBytesToFile(String path, byte[] bytes, boolean append) throws Exception
    {
        if (path == null)
        {
            return;
        }
        BufferedOutputStream bos = null;
        try
        {
            bos = new BufferedOutputStream(new FileOutputStream(path, append));
            bos.write(bytes);
            bos.flush();
        } finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                } finally
                {
                    bos = null;
                }
            }
        }
    }

    /**
     * 파일을 저장한다.
     * 
     * @param path
     *            저장할 파일 path
     * @param in
     *            파일 InputStream
     */
    public static void saveStreamToFile(String path, InputStream in) throws IOException
    {
        if (in == null)
        {
            return;
        }
        BufferedOutputStream bos = null;
        BufferedInputStream inBuffer = null;
        try
        {
            bos = new BufferedOutputStream(new FileOutputStream(path));
            inBuffer = new BufferedInputStream(in);
            int numofbytes = 0;
            byte[] buffer = new byte[8192];
            while ((numofbytes = inBuffer.read(buffer, 0, buffer.length)) > -1)
            {
                bos.write(buffer, 0, numofbytes);
            }
            bos.flush();
        } finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                } catch (Exception ex)
                {
                } finally
                {
                    bos = null;
                }
            }
            if (inBuffer != null)
            {
                try
                {
                    inBuffer.close();
                } catch (Exception ex)
                {
                } finally
                {
                    inBuffer = null;
                }
            }
        }

    }

    /**
     * 파일의 스트림을 닫는다.
     * 
     * @param in
     *            파일 InputStream
     */
    public static void closeStream(InputStream in)
    {
        try
        {
            if (in != null)
            {
                in.close();
            }
        } catch (Exception ex)
        {
        }
        in = null;
    }

    /**
     * 파일의 스트림을 닫는다.
     * 
     * @param out
     *            파일 OutputStream
     */
    public static void closeStream(OutputStream out)
    {
        try
        {
            if (out != null)
            {
                out.flush();
            }
        } catch (Exception ex)
        {
        }
        try
        {
            if (out != null)
            {
                out.close();
            }
        } catch (Exception ex)
        {
        }
        out = null;
    }

    /**
     * 파일의 Reader를 닫는다.
     * 
     * @param in
     *            파일 Reader
     */
    public static void closeStream(Reader in)
    {
        try
        {
            if (in != null)
            {
                in.close();
            }
        } catch (Exception ex)
        {
        }
        in = null;
    }

    /**
     * 파일의 Writer를 닫는다.
     * 
     * @param out
     *            파일 Writer
     */
    public static void closeStream(Writer out)
    {
        try
        {
            if (out != null)
            {
                out.flush();
            }
        } catch (Exception ex)
        {
        }
        try
        {
            if (out != null)
            {
                out.close();
            }
        } catch (Exception ex)
        {
        }
        out = null;
    }

    /**
     * 파일의 Path를 UnixFormat 으로 Convert한다.
     * 
     * @param path
     *            변경할 Path
     */
    public static String convertPathToUnixFormat(String path)
    {
        if (path == null)
        {
            return "";
        }
        path = path.trim();
        path = path.replaceAll("\\\\", "/");
        path = path.replaceAll("(/)+", "/");
        return path;
    }

    public static final String SORT_NAME = "name";
    public static final String SORT_MODIFIED_DT = "date";

    /**
     * read files in directory
     * 
     * @param path
     * @param filter
     * @return
     */
    public static File[] readFiles(String path, FilenameFilter filter)
    {
        if (StringUtils.isEmpty(path))
        {
            return null;
        }

        File dir = new File(path);

        if (dir.exists() == false)
        {
            return null;
        }

        File[] files = null;
        if (filter == null)
        {
            files = dir.listFiles();
        } else
        {
            files = dir.listFiles(filter);
        }

        log.info(path + " > find file count : " + files.length);
        return files;
    }

    /**
     * 파일 리스트를 이름 또는 수정날짜로 정렬한다. desc가 true이면 내림차순, false면 오름차순 정렬 type은 {@link FileUtil#SORT_NAME} 이름 또는
     * {@link FileUtil#SORT_MODIFIED_DT} 수정날짜
     * 
     * @param files
     * @param type
     * @param desc
     */
    public static void sortFiles(File[] files, String type, boolean desc)
    {
        if (ObjectUtils.isEmpty(files) || files.length == 1)
        {
            return;
        }

        Arrays.sort(files, new Comparator<File>()
        {

            @Override
            public int compare(File arg0, File arg1)
            {
                String s1 = "";
                String s2 = "";

                if (type.equals(SORT_NAME))
                {
                    s1 = arg0.getName();
                    s2 = arg1.getName();
                } else if (type.equals(SORT_MODIFIED_DT))
                {
                    s1 = String.valueOf(arg0.lastModified());
                    s2 = String.valueOf(arg1.lastModified());
                }

                if (desc)
                {
                    return s2.compareTo(s1);
                } else
                {
                    return s1.compareTo(s2);
                }
            }

        });
    }

    /**
     * 파일들을 해당 경로로 이동시킨다. -> 디렉토리가 존재하지 않으면 생성
     * 
     * @param path
     * @param listFile
     * @throws IOException
     */
    public static void moveFiles(String path, List<File> listFile)
    {
        if (StringUtils.isEmpty(path))
        {
            log.error("file move target path is empty.");
            return;
        }

        if (com.dailystudy.swinglab.service.framework.utils.FileUtil.exists(path) == false)
        {
            com.dailystudy.swinglab.service.framework.utils.FileUtil.makeDir(path, true);
        }

        if (ObjectUtils.isEmpty(listFile))
        {
            return;
        }

        for (File file : listFile)
        {
            // com.dailystudy.swinglab.service.framework.utils.FileUtil.move(file, path);
            try
            {
                Files.move(Paths.get(file.getAbsolutePath()), Paths.get(path, file.getName()));
            } catch (IOException e)
            {
                log.error(e.getMessage(), e);
                return;
            }
        }
    }

    /**
     * 파일을 해당 경로로 이동시킨다. -> 디렉토리가 존재하지 않으면 생성한다.
     * 
     * @param path
     * @param file
     */
    public static void moveFile(String path, File file)
    {
        if (StringUtils.isEmpty(path))
        {
            log.error("file move target path is empty.");
            return;
        }

        if (com.dailystudy.swinglab.service.framework.utils.FileUtil.exists(path) == false)
        {
            com.dailystudy.swinglab.service.framework.utils.FileUtil.makeDir(path, true);
        }

        try
        {
            Files.move(Paths.get(file.getAbsolutePath()), Paths.get(path, file.getName()));
        } catch (IOException e)
        {
            log.error(e.getMessage(), e);
            return;
        }

    }

}
