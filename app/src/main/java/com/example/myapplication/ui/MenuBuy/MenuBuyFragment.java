package com.example.myapplication.ui.MenuBuy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Magnifier;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.orderMenuData.OrderMenu;
import com.example.myapplication.ui.InitActivity;
import com.example.myapplication.ui.MenuMain.MenuActivity;
import com.example.myapplication.ui.MenuMain.MenuWheelActivity;
import com.example.myapplication.ui.bottomBar.InitBottomBar;

/**
 * 구매 확인하는 창 띄우는 프래그먼트
 * <p>
 * 인스턴스 변수:
 * {@link MenuBuyFragment#mRecyclerView}, {@link MenuBuyFragment#mAdapter},
 * </p>
 * <p>
 * 메소드:
 * {@link MenuBuyFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
 * </p>
 */
public class MenuBuyFragment extends Fragment {
    /**
     * 리싸이클러 뷰를 저장하는 객체
     */
    RecyclerView mRecyclerView = null ;
    /**
     * 리싸이클러 뷰 어댑터를 저장하는 객체
     */
    RecyclerImageTextAdapter mAdapter = null ;

    //////////////////////////////////////////////////////////////////
    private Magnifier magnifier;
    //돋보기 레이아웃 접근
    private ConstraintLayout constraintLayout1;
    //돋보기 리스
    private View.OnTouchListener magnifierTouchListener = new View.OnTouchListener() {
        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE: {
                    final int[] viewPosition = new int[2];
                    v.getLocationOnScreen(viewPosition);
                    magnifier.show(event.getRawX() - viewPosition[0],
                            event.getRawY() - viewPosition[1]);
                    break;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP: {
                    magnifier.dismiss();
                }
            }
            return true;
        }
    };
    //////////////////////////////////////////////////////////////

    /**
     * 화면의 각종 이벤트 설정 및 리싸이클러뷰 적용
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_buy, container, false);
        int totalPrice = 0;

        //돋보기
        constraintLayout1 = view.findViewById(R.id.fragment_menuBuy);
        Magnifier.Builder builder = new Magnifier.Builder(constraintLayout1);
        builder.setSize(600, 400);
        builder.setInitialZoom(3f);
        magnifier = builder.build();
        constraintLayout1.setOnTouchListener(magnifierTouchListener);
        //돋보기

        FrameLayout c = (FrameLayout) view.findViewById(R.id.frag_menuBuy_background);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if((InitActivity.getFunctionState() & InitBottomBar.BIGGER) != 0){
            Log.d("왜", "메뉴프래그먼트의 On");
        }

        ToggleButton toGoBtn = (ToggleButton) view.findViewById(R.id.toggleBtn_fragMenuBuy_toGo);
        ToggleButton toHereBtn = (ToggleButton) view.findViewById(R.id.toggleBtn_fragMenuBuy_toHere);

        toGoBtn.setChecked(true);
        toGoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                toGoBtn.setChecked(true);
                toHereBtn.setChecked(false);
            }
        });
        toHereBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                toGoBtn.setChecked(false);
                toHereBtn.setChecked(true);
            }
        });


        Button backBtn =  (Button) view.findViewById(R.id.btn_fragMenuBuy_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().remove(MenuBuyFragment.this).commit();


                if (getFragmentManager().getBackStackEntryCount() > 0)
                    getFragmentManager().popBackStack();

//                fragmentManager.popBackStack();
            }
        });

        Button buyBtn = (Button) view.findViewById(R.id.btn_fragMenuBuy_buy);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().remove(MenuBuyFragment.this).commit();

//                getActivity().getParent().finish();
                getActivity().finish();
                Log.d("asasasas", String.valueOf(getActivity()));

                if (getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStack();
                }
//                fragmentManager.popBackStack();
            }
        });

        TextView t = (TextView) view.findViewById(R.id.text_fragMenuBuy_totalPrice);

        for(OrderMenu orderMenu: ((MenuActivity)getActivity()).getCart().getOrderMenuList())
            totalPrice += orderMenu.getQuantity() * orderMenu.getMenu().getPrice();

        t.setText(String.valueOf(totalPrice)+"원");

        if(getActivity() instanceof MenuWheelActivity){
        }

        ////////////////////////////////
        // 리사이클러 뷰
        mRecyclerView = view.findViewById(R.id.recycler_fragMenuBuy);

        if(getActivity() instanceof MenuWheelActivity){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRecyclerView.getLayoutParams();
            layoutParams.weight = 3.5F;
            mRecyclerView.setLayoutParams(layoutParams);
        }

        mAdapter = new RecyclerImageTextAdapter(((MenuActivity)getActivity()).getCart());
        mRecyclerView.setAdapter(mAdapter) ;

        mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext())) ;
        ////////////////////////////////

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((MenuActivity)getActivity()).changeCartState();
    }
}
