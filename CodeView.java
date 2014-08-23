package org.crazyit.codeview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.Locale;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;


public class CodeView extends Activity {
	final int LIST_DIALOG1 = 0x111;
	private static Boolean isExit = false;
	private String tag = "CodeView";
	//声明一个Handler对象
	private static Handler handler = new Handler();
	TextToSpeech tts;
	//定义get_data为从那个选择路线的Activity传过来的值，1表示line1，2表示line2,3表示line3
	int get_data = 2;
	String line1[] = new String[] { "天河城东门", "岗顶麦当劳", "暨大北门", " 岑村", " 凌塘"};
	String line2[] = new String[] {"江南花园","中大南门","鹭江地铁D出口","客村建行",};
	String line3[] = new String[] {"嘉逸皇冠","奥体中心"," 岭南学院"};
	String str = "岗顶";

	Timer tExit = new Timer();
	MyTimerTask task;
	String contextService = Context.LOCATION_SERVICE;
	
	// 调用闹钟进行定站提醒
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			//Log.v(tag, "test1")

		}
	}

	// 按后退键退出程序
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "再按一次后退键退出应用程序", Toast.LENGTH_SHORT)
						.show();
				if (task != null)
					task.cancel(); // 将原任务从队列中移除
				task = new MyTimerTask();// 新建一个任务
				tExit.schedule(task, 2000);
			} else {
				finish();
				System.exit(0);
			}
		}
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button bt0 = (Button) findViewById(R.id.alert);

		bt0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(LIST_DIALOG1);
			}
		});
		
		Button speak = (Button)findViewById(R.id.read);
		tts = new TextToSpeech(this,new OnInitListener()
        {
			@Override
			public void onInit(int status) {
				//如果装载TTS引擎成功
				if(status == TextToSpeech.SUCCESS)
				{
					//设置使用美式英语朗读(虽然设置里有中文选项Locale.Chinese,但并不支持中文)
					int result = tts.setLanguage(Locale.US);
					//如果不支持设置的语言
					if(result != TextToSpeech.LANG_COUNTRY_AVAILABLE 
							&& result != TextToSpeech.LANG_AVAILABLE)
					{
						Toast.makeText(CodeView.this, "TTS暂时不支持这种语言朗读", 50000).show();
					}
				}
			}
        });
		speak.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//执行朗读
				tts.speak("班车即将到达 "+ str, TextToSpeech.QUEUE_ADD, null);
			}});
	}
	
	  @Override
	    public void onDestroy()
	    {
	    	//关闭TextToSpeech对象
	    	if(tts != null)
	    		tts.shutdown();
	    }
	// 定站提醒功能的实现
	public Dialog onCreateDialog(int id, Bundle state) {
		// 判断需要生成哪种类型的对话框
		switch (id) {
		case LIST_DIALOG1:
			Builder b = new AlertDialog.Builder(this);
			// 设置对话框的图标
			b.setIcon(R.drawable.xiaoba1);
			// 设置对话框的标题
			b.setTitle("定站提醒");
			// 创建一个List对象，List对象的元素是Map
			List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
			if(get_data == 1)
			{
				for (int i = 0; i < line1.length; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					// listItem.put("header", imageIds[i]);
					listItem.put("personName", line1[i]);
					listItems.add(listItem);
				}
			}
			else if(get_data == 2)
			{
				for (int i = 0; i < line2.length; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					// listItem.put("header", imageIds[i]);
					listItem.put("personName", line2[i]);
					listItems.add(listItem);
				}
			}
			else if(get_data == 3)
			{
				for (int i = 0; i < line3.length; i++) {
					Map<String, Object> listItem = new HashMap<String, Object>();
					// listItem.put("header", imageIds[i]);
					listItem.put("personName", line3[i]);
					listItems.add(listItem);
				}
			}
			// 创建一个SimpleAdapter
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
					R.layout.row, new String[] { "personName", "header" },
					new int[] { R.id.name, R.id.header });

			// 为对话框设置多个列表
			b.setAdapter(simpleAdapter
			// 为列表项的单击事件设置监听器
					, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								final int which) {		
							
							if(get_data == 1)
							{
								Toast.makeText(CodeView.this,
										"设置" + line1[which] + "成功~~",
										Toast.LENGTH_LONG).show();
										str=line1[which].substring(0,line1[which].length());
										  new Timer().schedule(new TimerTask() { 
											  @Override       
											  public void run() { 
												  Log.v(tag, "test");     
												  }    
											  }, 0,1000);  
							}
							else if(get_data == 2)
							{
								Toast.makeText(CodeView.this,
										"设置" + line2[which] + "成功~~",
										Toast.LENGTH_LONG).show();
										str=line2[which].substring(0,line2[which].length());
										//Log.v(tag, "选中");
										
									  new Timer().schedule(new TimerTask() { 
										  @Override       
										  public void run() { 
											  Log.v(tag, "test");     
											  }    
										  }, 0,1000);       
										
										
										
										/*
										new Thread(new Runnable() {                    
					                     @Override
					                    public void run() {
					                        // 使用postDelayed方式修改UI组件tvMessage的Text属性值
					                        // 并且延迟3S执行
					                        handler.postDelayed(new Runnable() {
					                            
					                             @Override
					                             public void run() {
					                            	 	Toast.makeText(CodeView.this, "多线程执行ing,3s一次", Toast.LENGTH_LONG).show();
					                                 //tvMessage.setText("使用Handler.postDelayed在工作线程中发送一段执行到消息队列中，在主线程中延迟3S执行。");    
					                                 Log.v(tag, "test");
					                             }
					                         }, 1000);                        
					                     }
					                 }).start();
					                 
					                 */

							}
							else if(get_data == 3)
							{
								Toast.makeText(CodeView.this,
										"设置" + line3[which] + "成功~~",
										Toast.LENGTH_LONG).show();
										str=line3[which].substring(0,line3[which].length());
										  new Timer().schedule(new TimerTask() { 
											  @Override       
											  public void run() { 
												  Log.v(tag, "test");     
												  }    
											  }, 0,1000);  
							}
						}
					});
			// 创建对话框
			return b.create();

		}
		return null;
	}
}
