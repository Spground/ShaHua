package cn.edu.dlut.chuangxin.shahua;

import java.io.FileNotFoundException;

import cn.edu.dlut.chuangxin.shahua.util.PhoneInfo;
import cn.edu.dlut.chuangxin.shahua.view.ConnectToBlueToothFragment;
import cn.edu.dlut.chuangxin.shahua.view.ContentFragment;
import cn.edu.dlut.chuangxin.shahua.view.DrawFragment;
import cn.edu.dlut.chuangxin.shahua.view.DrawView;
import cn.edu.dlut.chuangxin.shahua.view.MenuFragment;
import cn.edu.dlut.chuangxin.shahua.view.MenuFragment.OnUserSelectedListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		OnUserSelectedListener {

	@SuppressLint("HandlerLeak") Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Toast.makeText(getApplicationContext(), msg.obj.toString(),
					Toast.LENGTH_SHORT).show();
		}
	};
	String tag = "";
	String[] title = { "首页", "连接到沙画机器人", "绘图", "保存的文件", "帮助", "关于" };
	ActionBar actionBar = null;
	SlidingMenu menu = null;
	private DisplayMetrics dm = new DisplayMetrics();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_content_container);
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
		actionBar.setDisplayHomeAsUpEnabled(true);
		// 设置策划菜单
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		// menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.0f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.fragment_menu_container);
		//
		getFragmentManager().beginTransaction()
				.add(R.id.id_fragment_menu_container, new MenuFragment())
				.commit();
		getFragmentManager().beginTransaction()
				.add(R.id.id_fragment_content_container, new ContentFragment())
				.commit();
		// 得到信息
		getUserPhoneInfo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.drawmenu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.clear();
		if (tag.equals("drawfragment")) {
			getMenuInflater().inflate(R.menu.drawmenu, menu);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == android.R.id.home) {
			System.out.println("响应action");
			if (menu == null) {
				System.out.println("menu null");
				return true;
			}
			if (menu.isMenuShowing()) {
				menu.toggle();
				return true;
			}

			menu.showMenu();

		}
		switch (id) {
		case R.id.id_action_clear:
			Fragment fragment = getFragmentManager().findFragmentById(
					R.id.id_fragment_content_container);
			if (fragment != null) {
				try {
					System.out.println("fragment不为空");
					if (((DrawFragment) fragment).drawView == null)
						System.out.println("drawview空");
					else {
						((DrawFragment) fragment).drawView.clear();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			break;
		// 保存用戶圖片
		case R.id.id_action_save:
			Fragment fragment0 = getFragmentManager().findFragmentById(
					R.id.id_fragment_content_container);
			if (fragment0 != null) {
				try {
					System.out.println("fragment不为空");
					if (((DrawFragment) fragment0).drawView == null)
						System.out.println("drawview空");
					else {
						//
						final DrawView drawview = ((DrawFragment) fragment0).drawView;
						new Thread() {
							public void run() {
								Message msg = new Message();
								try {
									drawview.saveToFile();
									msg.obj = "文件保存成功！";
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									msg.obj = "文件保存失败，请重新尝试！";

								}
								handler.sendMessage(msg);
							};
						}.start();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 得到用户手机的一些配置信息，比如蓝牙是否打开，屏幕大小
	 */
	public void getUserPhoneInfo() {
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		PhoneInfo.WIDTH = dm.widthPixels;
		PhoneInfo.HEIGHT = dm.heightPixels;
	}

	@Override
	public void onUserSelected(int index) {
		// TODO Auto-generated method stub
		invalidateOptionsMenu();//
		tag = "";// 置空
		if (menu == null) {
			System.out.println("menu null");
			return;
		}
		if (menu.isMenuShowing()) {
			menu.toggle();
		}
		actionBar.setTitle(title[index]);
		Bundle bundle = new Bundle();
		bundle.putInt(ContentFragment.KEY, index);
		Fragment fragment = null;
		FragmentTransaction trans = null;
		switch (index) {
		case 0:// 首页
			fragment = new ContentFragment();
			fragment.setArguments(bundle);
			trans = getFragmentManager().beginTransaction().replace(
					R.id.id_fragment_content_container, fragment);
			trans.addToBackStack(null);
			trans.commit();
			bundle = null;
			fragment = null;
			break;

		case 1:// 连接到蓝牙
			fragment = new ConnectToBlueToothFragment();
			fragment.setArguments(bundle);
			trans = getFragmentManager().beginTransaction().replace(
					R.id.id_fragment_content_container, fragment);
			trans.addToBackStack(null);
			trans.commit();
			bundle = null;
			fragment = null;
			// startActivity(new Intent(MainActivity.this,
			// ConnectToBlueToothActivity.class));
			break;
		case 2:// 绘图
			fragment = new DrawFragment();
			fragment.setArguments(bundle);
			trans = getFragmentManager().beginTransaction().replace(
					R.id.id_fragment_content_container, fragment);
			trans.addToBackStack(null);
			trans.commit();
			bundle = null;
			fragment = null;
			tag = "drawfragment";
			invalidateOptionsMenu();
			break;
		case 3:// 保存的文件
			break;
		case 4:// 帮助
			break;
		case 5:// 关于
			break;
		default:
			fragment = new ContentFragment();
			fragment.setArguments(bundle);
			trans = getFragmentManager().beginTransaction().replace(
					R.id.id_fragment_content_container, fragment);
			trans.addToBackStack(null);
			trans.commit();
			bundle = null;
			fragment = null;
			break;

		}

	}
}
