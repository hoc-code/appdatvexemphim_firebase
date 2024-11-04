package com.example.appdatvexemphim.Activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.appdatvexemphim.Adapters.CastListAdapter;
import com.example.appdatvexemphim.Adapters.CategoryEachFilmAdapter;
import com.example.appdatvexemphim.Domains.Film;
import com.example.appdatvexemphim.R;
import com.example.appdatvexemphim.databinding.ActivityDetailTest2Binding;

import eightbitlab.com.blurview.RenderScriptBlur;

public class DetailTestActivity2 extends AppCompatActivity {
    private ActivityDetailTest2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTest2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setVariable();
    }

    private void setVariable() {
        Film item = (Film) getIntent().getSerializableExtra("object");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new GranularRoundedCorners(0, 0, 50, 50));
        assert item != null;
        Glide.with(this)
                .load(item.getPoster())
                .apply(requestOptions)
                .into(binding.filmPic);

        binding.titelTxt.setText(item.getTitle());
        binding.imdbTxt.setText("IMDB " + item.getImdb());
        binding.movieTimeTxt.setText(item.getYear() + " - " + item.getTime());
        binding.movieSumary.setText(item.getDescription());

        binding.watchTrailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = item.getTrailer().replace("https://www.youtube.com/watch?v=", "");
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getTrailer()));

                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });

        binding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        float radius = 10f;
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);

        Drawable windowsBackground = decorView.getBackground();
        binding.blurView.setupWith(rootView, new RenderScriptBlur(DetailTestActivity2.this))
                .setFrameClearDrawable(windowsBackground)
                .setBlurRadius(radius);
        binding.blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        binding.blurView.setClipToOutline(true);

        if (item.getGenre() != null) {
            binding.genreView.setAdapter(new CategoryEachFilmAdapter(item.getGenre()));
            binding.genreView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
        if (item.getCasts() != null) {
            binding.castView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.castView.setAdapter(new CastListAdapter(item.getCasts()));
        }
//        binding.video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                video_detail_onclick(Gravity.CENTER);
//            }
//        });
    }
//    private void video_detail_onclick(int center) {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature((Window.FEATURE_NO_TITLE));
//        dialog.setContentView((R.layout.video));
//
//        Window window = dialog.getWindow();
//        if(window ==null){
//            return;
//        }
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        WindowManager.LayoutParams wLayoutParams = window.getAttributes();
//        wLayoutParams.gravity=center;
//        window.setAttributes(wLayoutParams);
//
//        String urlvideo=Film.getTraler();
//        WebView videoweb= dialog.findViewById(R.id.webview);
//        String url="<iframe width=\"100%\" height=\"100%\" src=\""+urlvideo+"\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
//        videoweb.loadData(url,"text/html","UTF-8");
//        videoweb.getSettings().setJavaScriptEnabled(true);
//        videoweb.setWebChromeClient(new WebChromeClient());
//
//        dialog.show();
//    }
}
