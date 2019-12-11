//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.uubee.prepay.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.unispark.R;
import com.uubee.prepay.model.PayResult;

public class PayResultActivity extends BaseActivity {
    public PayResultActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.uubee_pay_result);
        PayResult result = (PayResult)this.getIntent().getParcelableExtra("pay_result");
        ImageView ivPayResult = (ImageView)this.findViewById(R.id.iv_pay_result);
        if("000000".equals(result.getRet_code())) {
            ivPayResult.setImageResource(R.drawable.img_pay_success);
        }

        TextView tvPayInfo = (TextView)this.findViewById(R.id.tv_pay_info);
        tvPayInfo.setText(result.getRet_msg());
        Button btnBack = (Button)this.findViewById(R.id.return_button);
        btnBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PayResultActivity.this.finish();
            }
        });
    }
}
