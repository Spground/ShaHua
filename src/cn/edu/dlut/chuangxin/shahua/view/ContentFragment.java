package cn.edu.dlut.chuangxin.shahua.view;

import cn.edu.dlut.chuangxin.shahua.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author asus
 * 显示中心内容的fragment
 *
 */
public class ContentFragment  extends Fragment{

	public final static String KEY="INDEX";
	private int index=0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			
			break;

		default:
			break;
		}
		System.out.println(index);
		View view=inflater.inflate(R.layout.fragment_content, container, false);
		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle=getArguments();
		if(bundle!=null)
			index = bundle.getInt(ContentFragment.KEY);
		super.onCreate(savedInstanceState);
	}
}
