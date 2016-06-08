package com.ywg.simplereader.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.ywg.simplereader.R;

public class QrcodeCardActivity extends Activity {

    private ImageView mQrcodeCardImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_card);

        mQrcodeCardImage = (ImageView) findViewById(R.id.iv_qrcode_card);
        Bitmap bitmap = EncodingUtils.createQRCode("App开发者", 500, 500, null);
        mQrcodeCardImage.setImageBitmap(bitmap);
    }
}
