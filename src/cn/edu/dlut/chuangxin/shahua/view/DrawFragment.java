package cn.edu.dlut.chuangxin.shahua.view;

/**
 * ÓÃ»§»­Í¼µÄfragment
 *
 **/
import cn.edu.dlut.chuangxin.shahua.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DrawFragment extends Fragment {
	public DrawView drawView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_draw, container, false);
		drawView = (DrawView) view.findViewById(R.id.id_drawview);
		return view;
	}
}
