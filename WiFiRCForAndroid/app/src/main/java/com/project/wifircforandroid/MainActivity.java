package com.project.wifircforandroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    Set<Integer> pressedKeys = new TreeSet<>();
    Button forwardBTN,reverseBTN,leftBTN,rightBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forwardBTN = (Button) findViewById(R.id.forward_BTN);
        reverseBTN = (Button) findViewById(R.id.reverse_BTN);
        leftBTN    = (Button) findViewById(R.id.left_BTN);
        rightBTN   = (Button) findViewById(R.id.right_BTN);

        Thread controlPanel = new Thread(new control_Panel());
        controlPanel.start();

    }


    public class control_Panel extends Thread{

        DataOutputStream dos;
        Socket connect;
        String address = "192.168.42.1"; //replace this ip with your raspberry ip address.

        int PORT = 15000;

        final byte forward_GO   = 10;
        final byte reverse_GO   = 11;
        final byte right_GO     = 12;
        final byte left_GO      = 13;

        final byte forward_STOP = 20;
        final byte reverse_STOP = 21;
        final byte right_STOP   = 22;
        final byte left_STOP    = 23;

        @Override
        public void run() {
            super.run();
            try {
                connect = new Socket(address,PORT);
                update_UI("Connected");
                dos = new DataOutputStream(connect.getOutputStream());


            } catch (IOException e) {
                update_UI("Not Connected");
            }

            forwardBTN.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, final MotionEvent motionEvent) {

                    switch(motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            if(pressedKeys.contains(10)){

                            }else{
                                pressedKeys.add(10);
                                new control_Commands(forward_GO).execute();

                            }
                            break;


                        case MotionEvent.ACTION_UP:
                            if(pressedKeys.contains(10)){
                                pressedKeys.remove(10);
                                new control_Commands(forward_STOP).execute();

                            }else{

                            }
                            break;
                    }
                    return false;
                }
            });

            reverseBTN.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, final MotionEvent motionEvent) {

                    switch(motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            if(pressedKeys.contains(11)){

                            }else{
                                pressedKeys.add(11);
                                new control_Commands(reverse_GO).execute();

                            }
                            break;


                        case MotionEvent.ACTION_UP:
                            if(pressedKeys.contains(11)){
                                pressedKeys.remove(11);
                                new control_Commands(reverse_STOP).execute();

                            }else{

                            }
                            break;
                    }
                    return false;
                }
            });

            leftBTN.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, final MotionEvent motionEvent) {

                    switch(motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            if(pressedKeys.contains(13)){

                            }else{
                                pressedKeys.add(13);
                                new control_Commands(left_GO).execute();

                            }
                            break;


                        case MotionEvent.ACTION_UP:
                            if(pressedKeys.contains(13)){
                                pressedKeys.remove(13);
                                new control_Commands(left_STOP).execute();
                            }else{

                            }
                            break;
                    }
                    return false;
                }
            });

            rightBTN.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, final MotionEvent motionEvent) {

                    switch(motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            if(pressedKeys.contains(12)){

                            }else{
                                pressedKeys.add(12);
                                new control_Commands(right_GO).execute();

                            }
                            break;


                        case MotionEvent.ACTION_UP:
                            if(pressedKeys.contains(12)){
                                pressedKeys.remove(12);
                                new control_Commands(right_STOP).execute();

                            }else{

                            }
                            break;
                    }
                    return false;
                }
            });

        }

        private class control_Commands extends AsyncTask{

            byte Command;

            public control_Commands(byte r_Command){
                Command = r_Command;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {

                try{
                    dos.writeByte(Command);
                    dos.flush();


                }catch (Exception e){
                    Log.v("ERROR",e.toString());
                    update_UI("Could not send Command");

                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        }


        private void update_UI(final String ui){
            if(ui.isEmpty()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"ERROR-Empty Update UI String-ERROR",Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,ui,Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }



}
