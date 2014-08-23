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
	//����һ��Handler����
	private static Handler handler = new Handler();
	TextToSpeech tts;
	//����get_dataΪ���Ǹ�ѡ��·�ߵ�Activity��������ֵ��1��ʾline1��2��ʾline2,3��ʾline3
	int get_data = 2;
	String line1[] = new String[] { "��ӳǶ���", "�ڶ�����", "�ߴ���", " ᯴�", " ����"};
	String line2[] = new String[] {"���ϻ�԰","�д�����","�ؽ�����D����","�ʹ彨��",};
	String line3[] = new String[] {"���ݻʹ�","��������"," ����ѧԺ"};
	String str = "�ڶ�";

	Timer tExit = new Timer();
	MyTimerTask task;
	String contextService = Context.LOCATION_SERVICE;
	
	// �������ӽ��ж�վ����
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			//Log.v(tag, "test1")

		}
	}

	// �����˼��˳�����
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				Toast.makeText(this, "�ٰ�һ�κ��˼��˳�Ӧ�ó���", Toast.LENGTH_SHORT)
						.show();
				if (task != null)
					task.cancel(); // ��ԭ����Ӷ������Ƴ�
				task = new MyTimerTask();// �½�һ������
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
				//���װ��TTS����ɹ�
				if(status == TextToSpeech.SUCCESS)
				{
					//����ʹ����ʽӢ���ʶ�(��Ȼ������������ѡ��Locale.Chinese,������֧������)
					int result = tts.setLanguage(Locale.US);
					//�����֧�����õ�����
					if(result != TextToSpeech.LANG_COUNTRY_AVAILABLE 
							&& result != TextToSpeech.LANG_AVAILABLE)
					{
						Toast.makeText(CodeView.this, "TTS��ʱ��֧�����������ʶ�", 50000).show();
					}
				}
			}
        });
		speak.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//ִ���ʶ�
				tts.speak("�೵�������� "+ str, TextToSpeech.QUEUE_ADD, null);
			}});
	}
	
	  @Override
	    public void onDestroy()
	    {
	    	//�ر�TextToSpeech����
	    	if(tts != null)
	    		tts.shutdown();
	    }
	// ��վ���ѹ��ܵ�ʵ��
	public Dialog onCreateDialog(int id, Bundle state) {
		// �ж���Ҫ�����������͵ĶԻ���
		switch (id) {
		case LIST_DIALOG1:
			Builder b = new AlertDialog.Builder(this);
			// ���öԻ����ͼ��
			b.setIcon(R.drawable.xiaoba1);
			// ���öԻ���ı���
			b.setTitle("��վ����");
			// ����һ��List����List�����Ԫ����Map
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
			// ����һ��SimpleAdapter
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
					R.layout.row, new String[] { "personName", "header" },
					new int[] { R.id.name, R.id.header });

			// Ϊ�Ի������ö���б�
			b.setAdapter(simpleAdapter
			// Ϊ�б���ĵ����¼����ü�����
					, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								final int which) {		
							
							if(get_data == 1)
							{
								Toast.makeText(CodeView.this,
										"����" + line1[which] + "�ɹ�~~",
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
										"����" + line2[which] + "�ɹ�~~",
										Toast.LENGTH_LONG).show();
										str=line2[which].substring(0,line2[which].length());
										//Log.v(tag, "ѡ��");
										
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
					                        // ʹ��postDelayed��ʽ�޸�UI���tvMessage��Text����ֵ
					                        // �����ӳ�3Sִ��
					                        handler.postDelayed(new Runnable() {
					                            
					                             @Override
					                             public void run() {
					                            	 	Toast.makeText(CodeView.this, "���߳�ִ��ing,3sһ��", Toast.LENGTH_LONG).show();
					                                 //tvMessage.setText("ʹ��Handler.postDelayed�ڹ����߳��з���һ��ִ�е���Ϣ�����У������߳����ӳ�3Sִ�С�");    
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
										"����" + line3[which] + "�ɹ�~~",
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
			// �����Ի���
			return b.create();

		}
		return null;
	}
}
