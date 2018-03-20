package com.zab.sanke.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zab.sanke.MyApplication;
import com.zab.sanke.R;
import com.zab.sanke.R.anim;
import com.zab.sanke.R.id;
import com.zab.sanke.R.layout;
import com.zab.sanke.adapter.GridAdapter;
import com.zab.sanke.adapter.GridAdapter.OnShoppingListener;

public class ShopActivity extends BaseActivity {
	private RadioGroup shopRg;
	private GridView gv;
	private GridAdapter adapter;
	private ImageView ivBack;
	private TextView tvGold;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		initView();
		initListener();
		initGridView(data,true);
	}
	private void initGridView(int [] data,boolean isShop) {
		
		adapter=new GridAdapter(this, R.layout.grid_item, data);
		adapter.setIsShopping(isShop);
		gv.setAdapter(adapter);
		adapter.setOnShoppingListener(new OnShoppingListener() {
			@Override
			public void shoppingListener(int position) {
				MyApplication.instace.savaShopingTag(position);
				int gold=getGold();
				tvGold.setText(gold-1888);
				AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(ShopActivity.this, R.anim.gold_anim);
				tvGold.startAnimation(animationSet);
				saveGold(-1888);
				adapter.notifyDataSetInvalidated();
			}
		});
	}
	private void initListener() {
		ivBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		shopRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_body:
					initGridView(dataBody,false);
					break;
				case R.id.rb_shoped:
					initGridView(data,true);
					break;
				case R.id.rb_heard:
					initGridView(dataHeader,false);
					break;
				case R.id.rb_end:
					initGridView(dataEnd,false);
					break;
				}
			}
		});
	}
	private void initView() {
		tvGold=(TextView) findViewById(R.id.tv_gold);
		tvGold.setText(getGold());
		ivBack=(ImageView) findViewById(R.id.iv_back);
		shopRg=(RadioGroup) findViewById(R.id.shopping_rg);
		gv=(GridView) findViewById(R.id.gv);

	}

}
