package com.example.qihang.bpm_hw3.activity;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.qihang.bpm_hw3.R;
import com.lidong.pdf.PDFView;
import com.lidong.pdf.listener.OnDrawListener;
import com.lidong.pdf.listener.OnLoadCompleteListener;
import com.lidong.pdf.listener.OnPageChangeListener;

public class PDFActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnDrawListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.back).setOnClickListener(v -> finish());

        String filename = getIntent().getStringExtra("filename");
        String fileurl = getIntent().getStringExtra("fileurl");
        PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fileFromLocalStorage(this, this, this, fileurl, filename);
    }

    /**
     * 翻页回调
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
    }

    /**
     * 加载完成回调
     */
    @Override
    public void loadComplete(int nbPages) {
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
