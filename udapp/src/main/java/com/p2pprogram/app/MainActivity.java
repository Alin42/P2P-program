/*
 * Copyright (c) 2018 Vladimir L. Shabanov <virlof@gmail.com>
 *
 * Licensed under the Underdark License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://underdark.io/LICENSE.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.p2pprogram.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.p2pprogram.app.model.Node;

public class MainActivity extends AppCompatActivity
{
	private TextView peersTextView;
	private TextView framesTextView;
	private EditText text;

	Node node;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		text = (EditText) findViewById(R.id.text);
		peersTextView = (TextView) findViewById(R.id.peersTextView);
		framesTextView = (TextView) findViewById(R.id.framesTextView);

		framesTextView.setMovementMethod(new ScrollingMovementMethod());

		startService(new Intent(this, BackgroundService.class));

		node = new Node(this);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		node.start();
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		if(node != null)
			node.stop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	//private static boolean started = false;

	public void sendFrames(View view)
	{
		/*if(!started)
		{
			started = true;
			node = new Node(this);
			node.start();
			return;
		}*/

		String abc = text.getText().toString();

		node.broadcastFrame(abc.getBytes());

		/*for(int i = 0; i < 100; ++i)
		{
			byte[] frameData = new byte[100 * 1024];
			new Random().nextBytes(frameData);

			node.broadcastFrame(frameData);
		}*/
	}

	public void refreshPeers()
	{
		peersTextView.setText(node.getLinks().size() + " connected");
	}

	public void refreshFrames()
	{
		Vibrator vibrator =(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		if (vibrator.hasVibrator()){
            long mills = 1000L;
            vibrator.vibrate(mills);
		}
		framesTextView.append(node.getFramesCount() + "\n");
	}

	public void cleanFrames()
	{
		framesTextView.setText("Все ушли :(");
	}
} // MainActivity
