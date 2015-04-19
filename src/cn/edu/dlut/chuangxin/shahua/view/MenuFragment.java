package cn.edu.dlut.chuangxin.shahua.view;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.dlut.chuangxin.shahua.R;
/**
 * 
 * @author asus
 *
 */
public class MenuFragment extends Fragment {

	private OnUserSelectedListener mCallBack;// 回调接口

	public interface OnUserSelectedListener {
		public void onUserSelected(int index);// 自定义接口便于回调，以通知activity事件发生了
	}

	private String[] menu_title = { "首页", "连接到沙画机器人", "绘图", "保存的文件", "帮助", "关于" };
	private int[] rid = { R.drawable.ic_action_home,
			R.drawable.ic_action_bluetooth_connected,
			R.drawable.ic_action_brush, R.drawable.ic_action_save,
			R.drawable.ic_action_help, R.drawable.ic_action_about };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		ListView listView = (ListView) view.findViewById(R.id.id_menu_listview);
		if (listView != null)
			listView.setAdapter(new MenuListViewAdapter());
		System.out.println(listView == null);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (mCallBack != null)
					mCallBack.onUserSelected(arg2);

			}
		});
		return view;
	}

	// 获取回调的接口
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mCallBack = (OnUserSelectedListener) activity;

		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnUserSelectedListener");
		}

	}

	private class MenuListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 6;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.row_view_for_menulistview, parent, false);
				holder.tV = (TextView) convertView
						.findViewById(R.id.id_textview_row_view);
				holder.draw = getResources().getDrawable(rid[position]);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			holder.tV.setText(menu_title[position]);
			holder.draw.setBounds(0, 0, holder.draw.getMinimumWidth(),
					holder.draw.getMinimumHeight()); // 设置边界
			holder.tV.setCompoundDrawables(holder.draw, null, null, null);

			return convertView;
		}

		final class ViewHolder {
			public TextView tV = null;
			public Drawable draw = null;
		}

	}
}
