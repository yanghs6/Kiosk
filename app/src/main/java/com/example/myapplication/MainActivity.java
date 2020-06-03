package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.ui.MenuMain.MenuActivity;
import com.example.myapplication.ui.InitActivity;
import com.example.myapplication.ui.bottomBar.InitBottomBar;

/**
 * 처음 화면
 * {@link InitActivity}에서 상속
 * onCreate, checkActivity 메소드 가짐
 */
public class MainActivity extends InitActivity {

    /**
     * activity_main 보여줌
     * @param savedInstanceState 액티비티 전환 간 데이터 전달하는 {@link Bundle} 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        displayBottomBar();

        Button exit = (Button) findViewById(R.id.btn_act_main_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button start = (Button) findViewById(R.id.btn_act_main_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, MenuActivity.class);

                // 출처: https://wingsnote.com/128 [날개의 노트 (Wing's Note)]
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
            }
        });


    }

    /**
     * {@link InitActivity#checkFunctionState()} 재정의
     * 휠체어: 화면 크기만 변경
     *
     * 돋보기: 각자 적기
     *
     * 색맹: 각자 적기
     *
     */
    @Override
    public void checkFunctionState() {
        int functionState =  getFunctionState();

        Log.d("시작화면에서", String.valueOf(functionState));

        if((functionState & InitBottomBar.WHEEL) != 0) {
            // https://stackoverrun.com/ko/q/11380534
            ConstraintLayout c = (ConstraintLayout)findViewById(R.id.lay_actMain_screen);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) c.getLayoutParams();
            layoutParams.matchConstraintPercentHeight = 0.6F;
            layoutParams.verticalBias = 0.75F;
            c.setLayoutParams(layoutParams);

            Log.d("시작화면에서", "휠체어로 변환");
        }else{
            //            https://stackoverrun.com/ko/q/11380534
            ConstraintLayout c = (ConstraintLayout)findViewById(R.id.lay_actMain_screen);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) c.getLayoutParams();
            layoutParams.matchConstraintPercentHeight = 0.9F;
            layoutParams.verticalBias = 0;
            c.setLayoutParams(layoutParams);

            Log.d("시작화면에서", "휠체어에서 일반으로 변환");
        }

        if((functionState & InitBottomBar.BIGGER) != 0){
            Log.d("시작화면에서", "돋보기 기능");
        }else{
            Log.d("시작화면에서", "돋보기 해제");
        }

        if((functionState & InitBottomBar.COLORBLIND) != 0){
            Log.d("시작화면에서", "색맹으로 전환");
        }else{
            Log.d("시작화면에서", "색맹 해제");
        }
    }
}