package com.leibown.practiceprojects;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    //    private CobwebView cobwebView;
//    private List<CobWebData> cobWebDatas = new ArrayList<>();
//    private RadioGroup radioGroup;
//    private Bezier3 bezier3;

//    private PathMeasureView pathView;

//    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        bezier3 = (Bezier3) findViewById(R.id.bezier3);
//        radioGroup = (RadioGroup) findViewById(R.id.rg_1);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rb1:
//                        bezier3.setMode(true);
//                        break;
//                    case R.id.rb2:
//                        bezier3.setMode(false);
//                        break;
//                }
//            }
//        });
//        cobwebView = (CobwebView) findViewById(R.id.cobweb_view);
//        cobWebDatas.add(new CobWebData("输出", 60));
//        cobWebDatas.add(new CobWebData("体力", 80));
//        cobWebDatas.add(new CobWebData("活力", 30));
//        cobWebDatas.add(new CobWebData("灵力", 90));
//        cobWebDatas.add(new CobWebData("技能", 50));
//        cobWebDatas.add(new CobWebData("速度", 90));
//        cobWebDatas.add(new CobWebData("决策", 70));
//        cobWebDatas.add(new CobWebData("轻功", 80));
//        cobwebView.setMaxValue(100);
//        cobwebView.setPolygonCount(3);
//        cobwebView.setData(cobWebDatas);

//        pathView = (PathMeasureView) findViewById(R.id.pathView);
//        searchView = (SearchView) findViewById(R.id.searchView);
//        searchView.setDuration(1500);
//        searchView.setColor(Color.BLACK);
    }

    public void doClick(View v) {
        Log.i("leibown","点击了");
//        pathView.startRotation(300);
//        switch (v.getId()){
//            case R.id.btn1:
////                searchView.startSearch();
//                break;
//            case R.id.btn2:
////                searchView.endSearch();
//                break;
//        }
    }
}
