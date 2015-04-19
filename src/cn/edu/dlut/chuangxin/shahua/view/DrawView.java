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
		paint.setAntiAlias(true); // 抗锯齿
		paint.setStrokeWidth(6); // 线条宽度
		paint.setStyle(Paint.Style.STROKE); // 画轮廓
		paint.setColor(clr_fg); // 颜色
		path = new Path();
		// 创建一张屏幕大小的位图，作为缓冲
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
		// 绘制上一次的，否则不连贯
		canvas.drawBitmap(cachebBitmap, 0, 0, null);
		canvas.drawPath(path, paint);
	}

	/**
	 * 清除画布
	 */
	public void clear() {
		path.reset();
		cacheCanvas.drawColor(clr_bg);
		invalidate();
	}

	/*
	 * 事件响应
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

			// 二次曲线方式绘制
			path.quadTo(cur_x, cur_y, x, y);
			// 下面这个方法貌似跟上面一样
			// path.lineTo(x, y);
			cur_x = x;
			cur_y = y;
			break;
		}

		case MotionEvent.ACTION_UP: {
			// 鼠标弹起保存最后状态
			cacheCanvas.drawPath(path, paint);
			path.reset();
			isMoving = false;
			break;
		}
		}

		// 通知刷新界面
		invalidate();

		return true;
	}

	/**
	 * 保存用戶生成的文件
	 * 
	 * @throws FileNotFoundException
	 */
	public void saveToFile() throws FileNotFoundException {
		// 生成文件名
		String filename = generateDrawFileName();
		File f = new File(filename);
		if (f.exists())
			throw new RuntimeException("文件：" + filename + " 已存在！");

		FileOutputStream fos = new FileOutputStream(new File(filename));
		// 将 bitmap 压缩成其他格式的图片数据
		cachebBitmap.compress(CompressFormat.PNG, 50, fos);
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 通知相册更新
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(f);
		intent.setData(uri);
		context.sendBroadcast(intent);
	}

	/**
	 * @return 文件名
	 */
	private String generateDrawFileName() {

		// try {
		String sdState = Environment.getExternalStorageState(); // 判断sd卡是否存在

		// 检查SD卡是否可用
		if (!sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this.context, "SD卡未准备好！", Toast.LENGTH_SHORT).show();
			return null;
		}

		// 获取系统图片存储路径
		// 获取sdcard的绝对路径
		String cardPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File path = new File(cardPath + "/ShaHua/DrawFile");
		// File path =
		// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		// Make sure the Pictures directory exists.
		path.mkdirs();

		// 根据当前时间生成图片名称
		Calendar c = Calendar.getInstance();
		String name = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH)
				+ c.get(Calendar.DAY_OF_MONTH) + c.get(Calendar.HOUR_OF_DAY)
				+ c.get(Calendar.MINUTE) + c.get(Calendar.SECOND) + ".png";

		// 合成完整路径，注意 / 分隔符
		String string = path.getPath() + "/" + name;
		Log.v("LOG", string);
		return string;
		// view.saveToFile(string);
		// Toast.makeText(this.context, "保存成功！\n文件保存在：" + string,
		// Toast.LENGTH_LONG).show();
		// } catch (FileNotFoundException e) {
		// Toast.makeText(this.context, "保存失败！\n" + e,
		// Toast.LENGTH_LONG).show();
		// }

	}

}
