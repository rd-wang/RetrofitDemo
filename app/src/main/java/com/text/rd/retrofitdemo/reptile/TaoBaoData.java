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
public class TaoBaoData {

    private static ArrayList<TaoBaoBean> xianbaList;
    private static int netSucceedCount;
    private static ArrayList<TaoBaoBean> rixiList;

    public static void main(String args[]) {
        doRiXi();
    }


    public static void doXianba() {
        RetrofitUtil.getInstance(TaoBaoApi.class, "https://www.taobao.com")
                .getXianBa().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s = StringEscapeUtils.unescapeHtml4(response.body());
                s = s.replace("jsonp364(\"", "").replace("\")", "").replaceAll("\\\\\"", "\"");
                Document document = Jsoup.parse(s);
                Elements items = document.getElementsByClass("item-name");
                xianbaList = new ArrayList<>();
                for (Element element : items) {
                    TaoBaoBean taoBaoBean = new TaoBaoBean();
                    taoBaoBean.toyName = element.text().trim();
                    taoBaoBean.toyUrl = "https:" + element.attr("href");
                    System.out.println(taoBaoBean.toyName + taoBaoBean.toyUrl);
                    xianbaList.add(taoBaoBean);
                }
                getDetail(xianbaList);
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
                    Element attr = document.getElementById("J_AttrUL");
                    if (attr == null) {
                        netSucceedCount = 0;
                        getDetail(dataList);
                    }
                    Elements li = attr.getElementsByTag("li");
                    ArrayList<String> attrs = new ArrayList<String>();

                    for (Element element : li) {
                        String trim = element.text().trim();
                        attrs.add(trim);
                    }
                    baoBean.attributes = attrs;

                    //获取头图
                    Element id = document.getElementById("J_UlThumb");
                    Elements imgs = id.getElementsByTag("img");
                    for (Element element : imgs) {
                        String src = element.attr("src").replace("_60x60q90.jpg", "");
                        src = "https:" + src;
                        topImageList.add(src);
                        System.out.println("头图: " + src);
                    }
                    baoBean.topImageList = topImageList;
                    //请求详情图
                    Elements script = document.getElementsByTag("script");
                    String s1 = "https:" + StringUtils.substringBetween(s, "\"httpsDescUrl\":\"", "\"},");
                    try {
                        Document detailDocument = Jsoup.connect(s1).get();
                        Elements detailImages = detailDocument.getElementsByTag("img");
                        for (Element element : detailImages) {
                            String src = element.attr("src");
                            detailImageList.add(src);
                            System.out.println("详情图: " + src);
                        }
                        baoBean.detailImageList = detailImageList;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    netSucceedCount += 1;
                    System.out.println(baoBean.toyName + "生成完毕 bean 数量:" + netSucceedCount + "/" + dataList.size());
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
            System.out.println("waiting call back Complete  dataListSize:" + dataList.size() + "    now:" + netSucceedCount);
            if (dataList.size() == netSucceedCount) {
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
            if(bean == null){
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
            if(bean.topImageList == null){
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
            if(bean.detailImageList == null){
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

    private void doYaYa() {
        RetrofitUtil.getInstance(TaoBaoApi.class, "https://www.taobao.com")
                .getYaYa().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void doFeiXia() {
        RetrofitUtil.getInstance(TaoBaoApi.class, "https://www.taobao.com")
                .getFeiXia().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private static int rixiListcount = 0;

    private static void doRiXi() {
        rixiList = new ArrayList<>();
        for (int i = 1; i < 60; i++) {
            String url = "https://uitox.tmall.hk/category-1129809897.htm?spm=a1z10.5-b-s.w4011-14465380858.157.8Rjojd&search=y&catName=%CD%E6%BE%DF%2F%C4%A3%CD%E6&catId=1129809897&pageNo=" + i + "#anchor";
            RetrofitUtil.getInstance(TaoBaoApi.class, "https://www.taobao.com")
                    .getUrlDetail(url).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String s = StringEscapeUtils.unescapeHtml4(response.body());
                    s = s.replaceAll("\\\\\"", "\"");
                    Document document = Jsoup.parse(s);
                    Elements items = document.getElementsByClass("photo");
                    for (int i = 0; i < items.size(); i++) {
                        if (i > items.size() - 8 - 1) {
                            break;
                        }
                        TaoBaoBean taoBaoBean = new TaoBaoBean();
                        Elements elementsByClass = items.get(i).getElementsByTag("a");
                        Element itemElement = elementsByClass.get(0);
                        taoBaoBean.toyUrl = "https:" + itemElement.attr("href");
                        Elements img = itemElement.getElementsByTag("img");
                        taoBaoBean.toyName = img.attr("alt");
                        System.out.println(taoBaoBean.toyName + taoBaoBean.toyUrl);
                        rixiList.add(taoBaoBean);
                    }
                    rixiListcount += 1;
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
            System.out.println("waiting call back Complete  total" + 59 + "   now: " + rixiListcount);
            if (rixiListcount == 59) {
                break;
            }
        }
        System.out.println("sku totle " + rixiList.size());
        getRiXiDetail(rixiList);
    }

    private static void getRiXiDetail(final ArrayList<TaoBaoBean> dataList) {
        int start = 3200;
        int end = 3510;
        netSucceedCount = start;
        for (int i = start; i < end; i++) {
            final TaoBaoBean baoBean = dataList.get(i);
            RetrofitUtil.getInstance(TaoBaoApi.class, "https://www.taobao.com")
                    .getUrlDetail(baoBean.toyUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    ArrayList<String> topImageList = new ArrayList<>();
                    ArrayList<String> detailImageList = new ArrayList<>();

                    String s = StringEscapeUtils.unescapeHtml4(response.body());
                    s = s.replaceAll("\\\\\"", "\"");
                    Document document = Jsoup.parse(s);
                    //获取头图
                    Element id = document.getElementById("J_UlThumb");
                    Elements imgs = id.getElementsByTag("img");
                    for (Element element : imgs) {
                        String src = element.attr("src").replace("_60x60q90.jpg", "");
                        if (!src.startsWith("https:")) {
                            src = "https:" + src;
                        }
                        topImageList.add(src);
                        System.out.println("头图: " + src);
                    }
                    baoBean.topImageList = topImageList;
                    //请求详情图
                    Elements script = document.getElementsByTag("script");
                    String s1 = "https:" + StringUtils.substringBetween(s, "\"httpsDescUrl\":\"", "\"},");
                    try {
                        Document detailDocument = Jsoup.connect(s1).get();
                        Elements detailImages = detailDocument.getElementsByTag("img");
                        for (Element element : detailImages) {
                            String src = element.attr("src");
                            if (!src.startsWith("https:")) {
                                src = "https:" + src;
                            }
                            detailImageList.add(src);
                            System.out.println("详情图: " + src);
                        }
                        baoBean.detailImageList = detailImageList;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    netSucceedCount += 1;
                    System.out.println(baoBean.toyName + "生成完毕 bean 数量:" + netSucceedCount + "/" + dataList.size());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("waiting call back Complete  dataListSize:" + dataList.size() + "    now:" + netSucceedCount);
            if (end == netSucceedCount) {
                break;
            }
        }
        List<TaoBaoBean> taoBaoBeen = dataList.subList(start, end);
        downImages(taoBaoBeen);
        System.out.println(getGson(dataList));
    }
}