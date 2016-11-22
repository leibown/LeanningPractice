package com.leibown.practiceprojects;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private CobwebView cobwebView;
    private List<CobWebData> cobWebDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cobwebView = (CobwebView) findViewById(R.id.cobweb_view);
        cobWebDatas.add(new CobWebData("输出", 60));
        cobWebDatas.add(new CobWebData("体力", 80));
        cobWebDatas.add(new CobWebData("活力", 30));
        cobWebDatas.add(new CobWebData("灵力", 90));
        cobWebDatas.add(new CobWebData("技能", 50));
        cobWebDatas.add(new CobWebData("速度", 90));
        cobWebDatas.add(new CobWebData("决策", 70));
        cobWebDatas.add(new CobWebData("轻功", 80));
        cobwebView.setMaxValue(100);
        cobwebView.setPolygonCount(3);
        cobwebView.setData(cobWebDatas);
    }
}
