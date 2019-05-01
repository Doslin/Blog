package com.java.util;


import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FetchRet;

import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;



public class QinNiuUtils {

    /**

     @ClassName: QiniuUtils
     @Description: 七牛操作工具类
     @author Lanxiaowei(736031305@qq.com)
     @date 2017年6月6日 上午10:56:32
`       */




     private static final String ACCESS_KEY = "7Mh_BChcpiZ3UN_e3yEW73fCfOM34p5Vsu0cTrM-";
     private static final String SECRET_KEY = "-m55M9aOCnWxRC11cez8Umd90cqPg2CMgh3ZPDjw";
     //默认上传空间
     private static final String BUCKET_NAME = "doude_blog_file";// 默认到其他类中
     /* 空间默认域名 */
    private static  Configuration cfg = new Configuration(Zone.zone0());
    private static final String BUCKET_HOST_NAME = "doude_blog_file";
    private static final String HOST_URL = "http://img.takeing.cn/";

    private static UploadManager uploadManager = new UploadManager(cfg);

    private static int LIMIT_SIZE = 1000;

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: listBucket
     * @Description: 返回七牛帐号的所有空间
     * @param @return
     * @param @throws Exception
     * @return String[]
     * @throws
     */
    public static String[] listBucket() throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        return bucketManager.buckets();
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: listFileOfBucket
     * @Description: 获取指定空间下的文件列表
     * @param bucketName
     *            空间名称
     * @param prefix
     *            文件名前缀
     * @param limit
     *            每次迭代的长度限制，最大1000，推荐值 100[即一个批次从七牛拉多少条]
     * @param @return
     * @return List<FileInfo>
     * @throws
     */
   /* public static List<FileInfo> listFileOfBucket(String bucketName,
                                                  String prefix, int limit) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        BucketManager.FileListIterator it = bucketManager
                .createFileListIterator(bucketName, prefix, limit, null);
        List<FileInfo> list = new ArrayList<FileInfo>();
        while (it.hasNext()) {
            FileInfo[] items = it.next();
            if (null != items && items.length > 0) {
                list.addAll(Arrays.asList(items));
            }
        }
        return list;
    }*/

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: uploadFile
     * @Description: 七牛图片上传
     * @param @param inputStream 待上传文件输入流
     * @param @param bucketName 空间名称
     * @param @param key 空间内文件的key
     * @param @param mimeType 文件的MIME类型，可选参数，不传入会自动判断
     * @param @return
     * @param @throws IOException
     * @return String
     * @throws
     */
    public static String uploadFile(InputStream inputStream, String bucketName,
                                    String key, String mimeType) throws IOException {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String token = auth.uploadToken(bucketName);
        byte[] byteData = IOUtils.toByteArray(inputStream);
        Response response = uploadManager.put(byteData, key, token, null,
                mimeType, false);
        inputStream.close();
        return response.bodyString();
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: uploadFile
     * @Description: 七牛图片上传
     * @param @param inputStream 待上传文件输入流
     * @param @param bucketName 空间名称
     * @param @param key 空间内文件的key
     * @param @return
     * @param @throws IOException
     * @return String
     * @throws
     */
    public static String uploadFile(InputStream inputStream,
                                    String key) throws IOException {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String token = auth.uploadToken(BUCKET_HOST_NAME);

        byte[] byteData = IOUtils.toByteArray(inputStream);
        Response response = uploadManager.put(byteData, key, token, null, null,
                false);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        String path = HOST_URL+ putRet.key;
        inputStream.close();
        return path;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: uploadFile
     * @Description: 七牛图片上传
     * @param filePath
     *            待上传文件的硬盘路径
     * @param fileName
     *            待上传文件的文件名
     * @param bucketName
     *            空间名称
     * @param key
     *            空间内文件的key
     * @param @return
     * @param @throws IOException
     * @return String
     * @throws
     */
    public static String uploadFile(String filePath, String fileName,
                                    String bucketName, String key) throws IOException {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String token = auth.uploadToken(bucketName);
        InputStream is = new FileInputStream(new File(filePath + fileName));
        byte[] byteData = IOUtils.toByteArray(is);
        Response response = uploadManager.put(byteData,
                (key == null || "".equals(key)) ? fileName : key, token);
        is.close();
        return response.bodyString();
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: uploadFile
     * @Description: 七牛图片上传[若没有指定文件的key,则默认将fileName参数作为文件的key]
     * @param filePath
     *            待上传文件的硬盘路径
     * @param fileName
     *            待上传文件的文件名
     * @param bucketName
     *            空间名称
     * @param @return
     * @param @throws IOException
     * @return String
     * @throws
     */
    public static String uploadFile(String filePath, String fileName,
                                    String bucketName) throws IOException {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String token = auth.uploadToken(bucketName);
        InputStream is = new FileInputStream(new File(filePath + fileName));
        byte[] byteData = IOUtils.toByteArray(is);
        Response response = uploadManager.put(byteData, fileName, token);
        is.close();
        return response.bodyString();
    }

    /**
     * @throws Exception
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: fetchToBucket
     * @Description: 提取网络资源并上传到七牛空间里
     * @param url
     *            网络上一个资源文件的URL
     * @param bucketName
     *            空间名称
     * @param key
     *            空间内文件的key[唯一的]
     * @param @return
     * @return String
     * @throws
     */
    public static String fetchToBucket(String url, String bucketName, String key)
            throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        FetchRet fetch = bucketManager.fetch(url, bucketName);
        FetchRet putret = bucketManager.fetch(url, bucketName, key);
        return putret.key;
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: fetchToBucket
     * @Description: 提取网络资源并上传到七牛空间里,不指定key，则默认使用url作为文件的key
     * @param url
     * @param bucketName
     * @param @return
     * @param @throws Exception
     * @return String
     * @throws
     */
    public static String fetchToBucket(String url, String bucketName)
            throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        FetchRet putret = bucketManager.fetch(url, bucketName);
       // DefaultPutRet putret = bucketManager.fetch(url, bucketName);
        return putret.key;
    }

    /**
     * @throws Exception
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: copyFile
     * @Description: 七牛空间内文件复制
     * @param bucket
     *            源空间名称
     * @param key
     *            源空间里文件的key(唯一的)
     * @param targetBucket
     *            目标空间
     * @param targetKey
     *            目标空间里文件的key(唯一的)
     * @return void
     * @throws
     */
    public static void copyFile(String bucket, String key, String targetBucket,
                                String targetKey) throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        bucketManager.copy(bucket, key, targetBucket, targetKey);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: moveFile
     * @Description: 七牛空间内文件剪切
     * @param bucket
     *            源空间名称
     * @param key
     *            源空间里文件的key(唯一的)
     * @param targetBucket
     *            目标空间
     * @param targetKey
     *            目标空间里文件的key(唯一的)
     * @param @throws Exception
     * @return void
     * @throws
     */
    public static void moveFile(String bucket, String key, String targetBucket,
                                String targetKey) throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        bucketManager.move(bucket, key, targetBucket, targetKey);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: renameFile
     * @Description: 七牛空间内文件重命名
     * @param bucket
     * @param key
     * @param targetKey
     * @param @throws Exception
     * @return void
     * @throws
     */
    public static void renameFile(String bucket, String key, String targetKey)
            throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        bucketManager.rename(bucket, key, targetKey);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: deleteFile
     * @Description: 七牛空间内文件删除
     * @param bucket
     *            空间名称
     * @param key
     *            空间内文件的key[唯一的]
     * @param @throws Exception
     * @return void
     * @throws
     */
    public static void deleteFile(String bucket, String key)
            throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        bucketManager.delete(bucket, key);
    }

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: findFiles
     * @Description: 返回指定空间下的所有文件信息
     * @param @param bucketName 空间名称
     * @param @param prefix 文件key的前缀
     * @param @param limit 批量提取的最大数目
     * @param @return
     * @param @throws Exception
     * @return FileInfo[]
     * @throws
     */
   /* public static FileInfo[] findFiles(String bucketName, String prefix,
                                       int limit) throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        FileListing listing = bucketManager.listFiles(bucketName, prefix, null,
                limit, null);
        if (listing == null || listing.items == null
                || listing.items.length <= 0) {
            return null;
        }
        return listing.items;
    }*/

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: findFiles
     * @Description: 返回指定空间下的所有文件信息
     * @param @param bucketName 空间名称
     * @param @param prefix 文件key的前缀
     * @param @return
     * @param @throws Exception
     * @return FileInfo[]
     * @throws
     */
   /* public static FileInfo[] findFiles(String bucketName, String prefix)
            throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        FileListing listing = bucketManager.listFiles(bucketName, prefix, null,
                LIMIT_SIZE, null);
        if (listing == null || listing.items == null
                || listing.items.length <= 0) {
            return null;
        }
        return listing.items;
    }
*/
    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: findFiles
     * @Description: 返回指定空间下的所有文件信息
     * @param @param bucketName
     * @param @param key
     * @param @return
     * @param @throws Exception
     * @return FileInfo[]
     * @throws
     */
    /*public static FileInfo[] findFiles(String bucketName) throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        FileListing listing = bucketManager.listFiles(bucketName, null, null,
                LIMIT_SIZE, null);
        if (listing == null || listing.items == null
                || listing.items.length <= 0) {
            return null;
        }
        return listing.items;
    }*/

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: findOneFile
     * @Description: 返回指定空间下的某个文件
     * @param @param bucketName
     * @param @param key
     * @param @param limit
     * @param @return
     * @param @throws Exception
     * @return FileInfo
     * @throws
     */
    /*public static FileInfo findOneFile(String bucketName, String key, int limit)
            throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        FileListing listing = bucketManager.listFiles(bucketName, key, null,
                limit, null);
        if (listing == null || listing.items == null
                || listing.items.length <= 0) {
            return null;
        }
        return (listing.items)[0];
    }*/

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: findOneFile
     * @Description: 返回指定空间下的某个文件(重载)
     * @param @param bucketName
     * @param @param key
     * @param @return
     * @param @throws Exception
     * @return FileInfo
     * @throws
     */
   /* public static FileInfo findOneFile(String bucketName, String key)
            throws Exception {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        BucketManager bucketManager = new BucketManager(auth,cfg);
        FileListing listing = bucketManager.listFiles(bucketName, key, null,
                LIMIT_SIZE, null);
        if (listing == null || listing.items == null
                || listing.items.length <= 0) {
            return null;
        }
        return (listing.items)[0];
    }*/

    /**
     * @Author: Lanxiaowei(736031305@qq.com)
     * @Title: getFileAccessUrl
     * @Description: 返回七牛空间内指定文件的访问URL
     * @param @param key
     * @param @return
     * @param @throws Exception
     * @return String
     * @throws
     */
    public static String getFileAccessUrl(String key) throws Exception {
        return BUCKET_HOST_NAME + "/" + key;
    }

    public static void mains(String[] args) throws IOException {
        FileInputStream in = new FileInputStream("C:\\Users\\doude\\Pictures\\empty.png");
        String s = uploadFile(in,null);
        System.out.println(s);

        /*
         * String[] buckets = listBucket(); for(String bucket : buckets) {
         * System.out.println(bucket); }
         */

        /*
         * List<FileInfo> list = listFileOfBucket(BUCKET_NAME, null, 1000);
         * for(FileInfo fileInfo : list) { System.out.println("key：" +
         * fileInfo.key); System.out.println("hash：" + fileInfo.hash);
         * System.out.println("................"); }
         */

        // copyFile(BUCKET_NAME, "images-test", BUCKET_NAME,
        // "images-test-1111");

        // renameFile(BUCKET_NAME, "images-test-1111", "images-test-2222.jpg");

        // deleteFile(BUCKET_NAME, "images-test-2222.jpg");

        // fetchToBucket("http://www.nanrenwo.net/uploads/allimg/121026/14-1210261JJD03.jpg",
        // BUCKET_NAME,"1111111111111111.jpg");
        /*try{
            FileInfo[] fileInfos = findFiles(BUCKET_NAME, "10", LIMIT_SIZE);
            for (FileInfo fileInfo : fileInfos) {
                System.out.println(fileInfo.key);
                System.out.println(fileInfo.hash);
                System.out.println("..............");
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }
    /**
     * 删除七牛上的文件
     * @param path  数据库中文件的路径
     * @param space 所存空间名称
     */
 /*   public boolean deleteImageFromQiNiu(String path, String space) {

        //if ((path ==null || path ==""))
        if (!(path ==null || path =="")) {
            String[] split = path.split(".com/");
            if (split.length>=2) {
                FileInfo findOneFile=null;
                try {//查询云端是否有该文件
                    findOneFile = QinNiuUtil.findOneFile(space,split[1]);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                if (findOneFile!=null) {//删除云端文件
                    try {
                        //.deleteFile(空间名, 文件名);
                        QinNiuUtil.deleteFile(space, split[1]);
                        return true;
                        //结束返回成功
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        //出现异常返回fase
                        return false;
                    }
                }else {
                    //如果七牛中没有该图片则返回成功
                    return true;
                }
            }else {
                //如果截取的数组小于2则是无效数据返回成功
                return true;
            }
        }else {
            //如果数据库中存储的问空则直接返回成功
            return true;
        }
    }*/

}



