package cn.edu.dlut.chuangxin.shahua.bluetooth;

import cn.edu.dlut.chuangxin.shahua.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ConnectToBlueToothActivity extends Activity {

	private BluetoothSPP bt=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 bt = new BluetoothSPP(getApplicationContext());
		setContentView(R.layout.fragment_connect_to_bluetooth);
((Button)findViewById(R.id.id_con_bluetooth_btn)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(bt!=null){
					Intent intent = new Intent(getApplicationContext(), DeviceList.class);
					startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
				}
			}
		});
             
              if(!bt.isBluetoothAvailable())
      		{
      			Toast.makeText(this, "蓝牙未打开", Toast.LENGTH_LONG).show();
      		}
      		else{
      			
      			
      			
      		}
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(!bt.isBluetoothEnabled()) {
		        // Do somthing if bluetooth is disable
		    } else {
		        // Do something if bluetooth is already enable
		    	Toast.makeText(this, "蓝牙已经打开并且可以用，服务已经启动", Toast.LENGTH_SHORT).show();
		    	bt.startService(BluetoothState.DEVICE_ANDROID);
		    }
		
	}
}
