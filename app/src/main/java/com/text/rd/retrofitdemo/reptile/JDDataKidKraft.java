package com.text.rd.retrofitdemo.reptile;

import com.text.rd.retrofitdemo.bean.TaoBaoBean;
import com.text.rd.retrofitdemo.retrofit.RetrofitUtil;
import com.text.rd.retrofitdemo.retrofit.TaoBaoApi;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rd on 2016/11/24.
 */
public class JDDataKidKraft {

    private static ArrayList<TaoBaoBean> dataList;
    private static int netSucceedCount;
    private static int attrErrorCount;

    public static void main(String args[]) {
        doJd();
    }


    public static void doJd() {
        RetrofitUtil.getInstance(TaoBaoApi.class, "https://www.taobao.com")
                .getUrlDetail("https://module-jshop.jd.com/module/getModuleHtml.html?" +
                        "appId=645183&orderBy=5&pageNo=2&direction=1&categoryId=0&pageSize=24" +
                        "&domainKey=kidkraft&pagePrototypeId=8&pageInstanceId=53652731" +
                        "&moduleInstanceId=58135294&prototypeId=68&templateId=401682" +
                        "&layoutInstanceId=58135294&origin=0&shopId=615110&venderId=622112" +
                        "&callback=jshop_module_render_callback&_=1480301731333")
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String s = StringEscapeUtils.unescapeHtml4(response.body());
                        String s1 = StringUtils.substringBetween(s, "moduleText\":\"", "\",\"moduleInstanceId\"");
                        s1 = s1.replace("\\\"", "\"").replaceAll("\\r\\n", "").replaceAll("\\t", "");
                        Document document = Jsoup.parse(s1);
                        Elements items = document.getElementsByTag("a");
                        dataList = new ArrayList<>();
                        for (Element element : items) {
                            TaoBaoBean taoBaoBean = new TaoBaoBean();
                            taoBaoBean.toyName = element.text().trim();
                            if (taoBaoBean.toyName.contains("KidKraft")) {
                                taoBaoBean.toyUrl = "https:" + element.attr("href");
                                System.out.println(taoBaoBean.toyName + taoBaoBean.toyUrl);
                                dataList.add(taoBaoBean);
                            }
                        }
                        getDetail(dataList);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }

    private static void getDetail(final ArrayList<TaoBaoBean> dataList) {
        for (final TaoBaoBean baoBean : dataList) {
            RetrofitUtil.getInstance(TaoBaoApi.class, "https://www.taobao.com")
                    .getUrlDetail(baoBean.toyUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    ArrayList<String> topImageList = new ArrayList<>();
                    ArrayList<String> detailImageList = new ArrayList<>();

                    String s = StringEscapeUtils.unescapeHtml4(response.body());
                    s = s.replaceAll("\\\\\"", "\"");
                    Document document = Jsoup.parse(s);
                    //属性
                    Element attr = document.getElementById("parameter2");
                    if (attr != null) {
                        Elements li = attr.getElementsByTag("li");
                        ArrayList<String> attrs = new ArrayList<String>();

                        for (Element element : li) {
                            String trim = element.text().trim();
                            attrs.add(trim);
                        }
                        baoBean.attributes = attrs;
                    } else {
                        attrErrorCount += 1;
                    }
                    //获取头图
                    Elements elementsByTag = document.getElementsByClass("spec-items");
                    Elements imgs = elementsByTag.get(0).getElementsByTag("img");
                    for (Element element : imgs) {
                        String src = element.attr("src").replace("n5", "n0");
                        src = "https:" + src;
                        topImageList.add(src);
                        System.out.println("头图: " + src);
                    }
                    baoBean.topImageList = topImageList;
                    //请求详情图
                    Elements script = document.getElementsByTag("script");
                    String s1 = "https:" + StringUtils.substringBetween(s, ",                desc: '", "',        foot");
                    try {
                        Document detailDocument = Jsoup.connect(s1).ignoreContentType(true).get();
                        Elements detailImages = detailDocument.getElementsByTag("img");
                        for (Element element : detailImages) {
                            String src = element.attr("data-lazyload");
                            src = "https:" + src.replace("\\\"", "").replace("\\\"", "");
                            detailImageList.add(src);
                            System.out.println("详情图: " + src);
                        }
                        baoBean.detailImageList = detailImageList;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    netSucceedCount += 1;
                    System.out.println(baoBean.toyName + "生成完毕 bean 数量:" + netSucceedCount + "/" + dataList.size() + "   失 败:" + attrErrorCount);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("waiting call back Complete  dataListSize:" + dataList.size() + "    now:succeed" + netSucceedCount + "  error:" + attrErrorCount);
            if (dataList.size() == netSucceedCount + attrErrorCount) {
                break;
            }
        }
        downImages(dataList);
        System.out.println(getGson(dataList));
    }

    private static String getGson(ArrayList<TaoBaoBean> dataList) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (TaoBaoBean baoBean : dataList) {
            sb.append(baoBean.toJson());
            sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static void downImages(List<TaoBaoBean> dataList) {
        System.out.println("START DOWNImage");
        String xianbapath = "/Users/rd/rixi/";
        File file = new File(xianbapath);
        if (!file.exists()) {
            file.mkdirs();
        }
        int downloadToyCount = 0;
        for (TaoBaoBean bean : dataList) {
            if (bean == null) {
                continue;
            }
            System.out.println(bean.toyName);
            String pathname = xianbapath + bean.toyName;
            File file1 = new File(pathname);
            if (!file1.exists()) {
                file1.mkdirs();
            } else {
                String s = String.valueOf(System.currentTimeMillis());
                pathname = xianbapath + bean.toyName + s.substring(2, 9);
                File file2 = new File(pathname);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
            }
            if (bean.topImageList == null) {
                continue;
            }
            for (String url : bean.topImageList) {
                File top = new File(pathname + "/top");
                if (!top.exists()) {
                    top.mkdirs();
                }
                String[] split = url.split("/");
                downImage(url, pathname + "/top/" + split[split.length - 1]);
            }
            if (bean.detailImageList == null) {
                continue;
            }
            for (String url : bean.detailImageList) {
                File detail = new File(pathname + "/detail");
                if (!detail.exists()) {
                    detail.mkdirs();
                }
                String[] split = url.split("/");
                downImage(url, pathname + "/detail/" + split[split.length - 1]);
            }
            downloadToyCount += 1;
            System.out.println("已下载:" + downloadToyCount);
        }
    }

    public static void downImage(String surl, String filePath) {
        // 网络请求所需变量
        try {
            URL url = new URL(surl);
            InputStream in = null;
            try {
                in = url.openStream();
                if (in == null) {
                    System.out.println("图片不存在!");
                }

            } catch (Exception e) {
                System.out.println(filePath + "图片不存在!");
                return;
            }
            DataInputStream dataInputStream = new DataInputStream(in);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            System.out.println("当前正在下载图片" + filePath);
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}