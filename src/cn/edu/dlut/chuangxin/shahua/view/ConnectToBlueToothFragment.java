package cn.edu.dlut.chuangxin.shahua.view;

/*
 * 
 * 用户通过蓝牙连接到机器人的fragment
 */
import cn.edu.dlut.chuangxin.shahua.R;
import cn.edu.dlut.chuangxin.shahua.bluetooth.BluetoothSPP;
import cn.edu.dlut.chuangxin.shahua.bluetooth.BluetoothState;
import cn.edu.dlut.chuangxin.shahua.bluetooth.DeviceList;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ConnectToBlueToothFragment extends Fragment {

	private BluetoothSPP bt = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_connect_to_bluetooth,
				container, false);
		((Button) view.findViewById(R.id.id_con_bluetooth_btn))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (bt != null) {
							Intent intent = new Intent(getActivity(),
									DeviceList.class);
							startActivityForResult(intent,
									BluetoothState.REQUEST_CONNECT_DEVICE);
						}
					}
				});
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bt = new BluetoothSPP(getActivity());// 新建一个实例
		if (!bt.isBluetoothAvailable()) {
			Toast.makeText(getActivity(), "蓝牙未打开", Toast.LENGTH_LONG).show();
		} else {

			if (!bt.isBluetoothEnabled()) {
				// Do somthing if bluetooth is disable
			} else {
				// Do something if bluetooth is already enable
				Toast.makeText(getActivity(), "蓝牙已经打开并且可以用，服务已经启动",
						Toast.LENGTH_SHORT).show();
				bt.startService(BluetoothState.DEVICE_ANDROID);
			}

		}

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}
}
