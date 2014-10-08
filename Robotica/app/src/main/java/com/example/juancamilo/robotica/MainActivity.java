package com.example.juancamilo.robotica;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
    private SeekBar velocidad=null;
    private SeekBar aceleracion=null;
    private ToggleButton tg1= null;
    private ToggleButton tg2= null;
    private ToggleButton tg3 = null;
    private EditText ip = null;
    private ToggleButton connect = null;

    protected char B1,B2,B3 = 0;
    protected char S1,S2 = 127; //la mitad de 254


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aceleracion = (SeekBar) findViewById(R.id.seekBar2);
        velocidad = (SeekBar) findViewById(R.id.seekBar);
        tg1= (ToggleButton) findViewById(R.id.toggleButton);
        tg2= (ToggleButton) findViewById(R.id.toggleButton2);
        tg3= (ToggleButton) findViewById(R.id.toggleButton3);
        ip = (EditText)findViewById(R.id.editTextIP);
        connect= (ToggleButton) findViewById(R.id.connect);
        connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean connected) {
            // Acá debe ir el intento de conexión al servidor
            if(connected){
                Log.i("Antes de conectar: ","IP: "+ip.getText().toString());
                new connectTask().execute();
            }
            }
        });
        aceleracion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                TextView acelText=(TextView) findViewById(R.id.accel);
                acelText.setText(i+"");
                TextView acelText2=(TextView) findViewById(R.id.acceleration);
                acelText2.setText(i+"");
                ToggleButton connect=(ToggleButton) findViewById(R.id.connect);
                if(connect.isChecked()){
                    S1 = (char) i;
                    generarMensaje();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        velocidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                TextView velText=(TextView) findViewById(R.id.vel);
                velText.setText(progress+"");
                TextView velText2=(TextView) findViewById(R.id.velocity);
                velText2.setText(progress+"");
                ToggleButton connect=(ToggleButton) findViewById(R.id.connect);
                if(connect.isChecked()){
                    S2 = (char) progress;
                    generarMensaje();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        tg1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                TextView toggleText=(TextView) findViewById(R.id.toggle1);
                toggleText.setText( (b?"ON":"OFF"));
                ToggleButton connect=(ToggleButton) findViewById(R.id.connect);
                if(connect.isChecked()){
                    B1= (char) (b ? 1:0);
                    generarMensaje();
                }
            }
        });tg2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                TextView toggleText=(TextView) findViewById(R.id.toggle2);
                toggleText.setText((b?"ON":"OFF"));
                ToggleButton connect=(ToggleButton) findViewById(R.id.connect);
                if(connect.isChecked()){
                    B2= (char) (b ? 1:0);
                    generarMensaje();
                }
            }
        });tg3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                TextView toggleText = (TextView) findViewById(R.id.toggle3);
                toggleText.setText((b ? "ON" : "OFF"));
                ToggleButton connect = (ToggleButton) findViewById(R.id.connect);
                if (connect.isChecked()) {
                    B3= (char) (b ? 1:0);
                    generarMensaje();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*************************************************************/

    public String generarMensaje(){


        String message= Character.toString((char) 126)+
                        Character.toString(B1)+
                        Character.toString(B2)+
                        Character.toString(B3)+
                        Character.toString(S1)+
                        Character.toString(S2);


        TextView data = (TextView) findViewById(R.id.textView8);
        data.setText("Datos: "+B1+B2+B3+S1+S2);

        Conection conn = Conection.getInstance();

        conn.enviarMensaje(message);




        return null;
    }

    //Clase interna para manejar la conexión de manea asincrónica
    public class connectTask extends AsyncTask<String,String,Conection> {



        @Override
        protected Conection doInBackground(String... laIP) {

            Conection conn = Conection.getInstance();

            conn.addListener(new OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });


            conn.conectar(ip.getText().toString());


            return null;
        }



    }
}
