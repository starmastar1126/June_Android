package woopy.com.juanmckenzie.caymanall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import woopy.com.juanmckenzie.caymanall.Objects.Banner;
import woopy.com.juanmckenzie.caymanall.R;
import woopy.com.juanmckenzie.caymanall.TinderType.InboxActivityTinderType;
import woopy.com.juanmckenzie.caymanall.ads.activities.AdsListActivity;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterBannerFull;
import woopy.com.juanmckenzie.caymanall.common.widgets.SliderAdapterBanners;
import woopy.com.juanmckenzie.caymanall.utils.Configs;

public class BannerAll extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_all);


        findViewById(R.id.wDismissButt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Configs configs = (Configs) getApplicationContext();
        SliderView image = findViewById(R.id.image);
        SliderAdapterBannerFull adapter = new SliderAdapterBannerFull(BannerAll.this, configs.getBanners());
        image.setSliderAdapter(adapter);
        image.setCurrentPagePosition(configs.getSelectedbanner());


    }
}
