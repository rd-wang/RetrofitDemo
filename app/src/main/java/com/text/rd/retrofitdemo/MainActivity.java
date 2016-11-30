package com.text.rd.retrofitdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.text.rd.retrofitdemo.bean.HomeBean;
import com.text.rd.retrofitdemo.reptile.TaoBaoData;
import com.text.rd.retrofitdemo.retrofit.ApiService;
import com.text.rd.retrofitdemo.retrofit.RetrofitUtil;
import com.text.rd.retrofitdemo.retrofit.TaoBaoApi;
import com.text.rd.retrofitdemo.retrofit.converter.CustomGsonConverterFactory;
import com.text.rd.retrofitdemo.utils.LogCat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.text_main)
    TextView textMain;
    @BindView(R.id.xianba)
    Button xianba;
    @BindView(R.id.rixi)
    Button rixi;
    @BindView(R.id.feixia)
    Button feixia;
    @BindView(R.id.yaya)
    Button yaya;
    @BindView(R.id.button_layout)
    LinearLayout buttonLayout;
    private LogCat L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L = LogCat.createInstance(this);
        ButterKnife.bind(this);

//        doRetrofitRequest();

    }

    private static void doXianba() {
        TaoBaoData.doXianba();
    }

    private void doYaYa() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        RetrofitUtil.getInstance(TaoBaoApi.class, "https://www.taobao.com")
                .getYaYa().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                textMain.setText(body);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void doFeiXia() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        RetrofitUtil.getInstance(TaoBaoApi.class, "https://www.taobao.com")
                .getFeiXia().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                textMain.setText(body);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void doRiXi() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        RetrofitUtil.getInstance(TaoBaoApi.class, "https://www.taobao.com")
                .getRiXi().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                textMain.setText(body);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void doRetrofitRequest() {
        //实例化retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.testHost)
                .addConverterFactory(CustomGsonConverterFactory.create())
                .build();
        //实例化service
        ApiService apiService = retrofit.create(ApiService.class);
        //call.execute() 则采用同步方式
        //异步调用
        apiService.getHomeList().enqueue(new Callback<List<HomeBean>>() {
            @Override
            public void onResponse(Call<List<HomeBean>> call, Response<List<HomeBean>> response) {
                List<HomeBean> body = response.body();
                textMain.setText(body.get(0).title);
            }

            @Override
            public void onFailure(Call<List<HomeBean>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick({R.id.xianba, R.id.rixi, R.id.feixia, R.id.yaya})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xianba:
                buttonLayout.setVisibility(View.GONE);
                doXianba();
                break;
            case R.id.rixi:
                buttonLayout.setVisibility(View.GONE);
                doRiXi();
                break;
            case R.id.feixia:
                buttonLayout.setVisibility(View.GONE);
                doFeiXia();
                break;
            case R.id.yaya:
                buttonLayout.setVisibility(View.GONE);
                doYaYa();
                break;
        }
    }

    public static void main(String args[]) {
        doXianba();
    }
}
