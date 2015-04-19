package cn.edu.dlut.chuangxin.shahua.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import cn.edu.dlut.chuangxin.shahua.util.PhoneInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DrawView extends View {

	private Context context = null;
	private Paint paint;
	private Canvas cacheCanvas;
	private Bitmap cachebBitmap;
	private Path path;
	private int clr_bg, clr_fg;

	public DrawView(Context context) {
		super(context);
		this.context = context;
	}

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
		clr_bg = Color.WHITE;
		clr_fg = Color.CYAN;

		paint = new Paint();
		paint.setAntiAlias(true); // �����
		paint.setStrokeWidth(6); // �������
		paint.setStyle(Paint.Style.STROKE); // ������
		paint.setColor(clr_fg); // ��ɫ
		path = new Path();
		// ����һ����Ļ��С��λͼ����Ϊ����
		cachebBitmap = Bitmap.createBitmap(PhoneInfo.WIDTH, PhoneInfo.HEIGHT,
				Config.ARGB_8888);
		cacheCanvas = new Canvas(cachebBitmap);
		cacheCanvas.drawColor(clr_bg);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(clr_bg);
		// ������һ�εģ���������
		canvas.drawBitmap(cachebBitmap, 0, 0, null);
		canvas.drawPath(path, paint);
	}

	/**
	 * �������
	 */
	public void clear() {
		path.reset();
		cacheCanvas.drawColor(clr_bg);
		invalidate();
	}

	/*
	 * �¼���Ӧ
	 */
	private float cur_x, cur_y;
	private boolean isMoving;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			cur_x = x;
			cur_y = y;
			path.moveTo(cur_x, cur_y);
			isMoving = true;
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			if (!isMoving)
				break;

			// �������߷�ʽ����
			path.quadTo(cur_x, cur_y, x, y);
			// �����������ò�Ƹ�����һ��
			// path.lineTo(x, y);
			cur_x = x;
			cur_y = y;
			break;
		}

		case MotionEvent.ACTION_UP: {
			// ��굯�𱣴����״̬
			cacheCanvas.drawPath(path, paint);
			path.reset();
			isMoving = false;
			break;
		}
		}

		// ֪ͨˢ�½���
		invalidate();

		return true;
	}

	/**
	 * �����Ñ����ɵ��ļ�
	 * 
	 * @throws FileNotFoundException
	 */
	public void saveToFile() throws FileNotFoundException {
		// �����ļ���
		String filename = generateDrawFileName();
		File f = new File(filename);
		if (f.exists())
			throw new RuntimeException("�ļ���" + filename + " �Ѵ��ڣ�");

		FileOutputStream fos = new FileOutputStream(new File(filename));
		// �� bitmap ѹ����������ʽ��ͼƬ����
		cachebBitmap.compress(CompressFormat.PNG, 50, fos);
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ֪ͨ������
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(f);
		intent.setData(uri);
		context.sendBroadcast(intent);
	}

	/**
	 * @return �ļ���
	 */
	private String generateDrawFileName() {

		// try {
		String sdState = Environment.getExternalStorageState(); // �ж�sd���Ƿ����

		// ���SD���Ƿ����
		if (!sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this.context, "SD��δ׼���ã�", Toast.LENGTH_SHORT).show();
			return null;
		}

		// ��ȡϵͳͼƬ�洢·��
		// ��ȡsdcard�ľ���·��
		String cardPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File path = new File(cardPath + "/ShaHua/DrawFile");
		// File path =
		// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		// Make sure the Pictures directory exists.
		path.mkdirs();

		// ���ݵ�ǰʱ������ͼƬ����
		Calendar c = Calendar.getInstance();
		String name = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH)
				+ c.get(Calendar.DAY_OF_MONTH) + c.get(Calendar.HOUR_OF_DAY)
				+ c.get(Calendar.MINUTE) + c.get(Calendar.SECOND) + ".png";

		// �ϳ�����·����ע�� / �ָ���
		String string = path.getPath() + "/" + name;
		Log.v("LOG", string);
		return string;
		// view.saveToFile(string);
		// Toast.makeText(this.context, "����ɹ���\n�ļ������ڣ�" + string,
		// Toast.LENGTH_LONG).show();
		// } catch (FileNotFoundException e) {
		// Toast.makeText(this.context, "����ʧ�ܣ�\n" + e,
		// Toast.LENGTH_LONG).show();
		// }

	}

}
