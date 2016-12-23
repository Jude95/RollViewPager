package com.jude.rollviewpagerdome;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.jude.rollviewpager.hintview.IconHintView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetImageActivity extends AppCompatActivity {
    private RollPagerView mLoopViewPager;
    private Button mBtnPre,mBtnAfter;
    private TestLoopAdapter mLoopAdapter;
    OkHttpClient client = new OkHttpClient();

    int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_image);
        mLoopViewPager= (RollPagerView) findViewById(R.id.loop_view_pager);
        mLoopViewPager.setPlayDelay(1000);
        mLoopViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mLoopViewPager));
        mLoopViewPager.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));
        mLoopViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(NetImageActivity.this,"Item "+position+" clicked",Toast.LENGTH_SHORT).show();
            }
        });

        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnAfter = (Button) findViewById(R.id.after);

        mBtnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPage>1)
                getData(--mPage);
            }
        });
        mBtnAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(++mPage);
            }
        });
        getData(mPage);
    }


    public void getData(final int page){
        Request request = new Request.Builder()
                .url("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/"+5+"/"+page)
                .get()
                .build();
        Call call =  client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("NetImageActivity","error:"+e.getMessage());
                Toast.makeText(NetImageActivity.this,"网络请求失败，error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String content = response.body().string();
                    Log.i("NetImageActivity","raw data:"+content);

                    JSONObject jsonObject = new JSONObject(content);
                    JSONArray strArr = jsonObject.getJSONArray("results");
                    final String[] imgs  = new String[strArr.length()];
                    for (int i = 0; i < strArr.length(); i++) {
                        JSONObject obj = strArr.getJSONObject(i);
                        imgs[i] = obj.getString("url");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLoopAdapter.setImgs(imgs);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class TestLoopAdapter extends LoopPagerAdapter{
        String[] imgs = new String[0];

        public void setImgs(String[] imgs){
            this.imgs = imgs;
            notifyDataSetChanged();
        }


        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            Log.i("RollViewPager","getView:"+imgs[position]);

            ImageView view = new ImageView(container.getContext());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("RollViewPager","onClick");
                }
            });
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(NetImageActivity.this)
                    .load(imgs[position])
                    .placeholder(R.drawable.loading)
                    .into(view);
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }

    }
}
