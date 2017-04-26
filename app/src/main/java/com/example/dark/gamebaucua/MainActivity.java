package com.example.dark.gamebaucua;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Image;

import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
public class MainActivity extends AppCompatActivity {

    GridView gridView;
    Custom_GridView_BanCo adapter;
    Integer[] dsHinh = {R.drawable.nai, R.drawable.bau, R.drawable.ga, R.drawable.ca, R.drawable.cua, R.drawable.tom};
    AnimationDrawable cdXingau1, cdXiNgau2, cdXiNgau3;
    ImageView hinhXiNgau1, hinhXiNgau2, hinhXiNgau3;

    CheckBox checkSound;
    Random randomXiNgau;

    int GTXingau1, GTXingau2, GTXingau3;
    public static Integer[] giatiendatcuoc=new Integer[6];
    int tongtiencu, tongtienmoi;
    TextView tvTien;
    Timer timer=new Timer();
    Handler handler;
    int tienThuong, kiemtra, idamthanh;
    SharedPreferences luuTru;

    private SoundPool soundPool;
    private AudioManager audioManager;

    private static final int MAX_STREAMS=5;

    private static final int streamtype=AudioManager.STREAM_MUSIC;

    private boolean loaded;
    private int soundIddice;
    private float volume;
    MediaPlayer nhacnen = new MediaPlayer();


    Handler.Callback callback=new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            RandomXiNgau1();
            RandomXiNgau2();
            RandomXiNgau3();

            for(int i=0;i<giatiendatcuoc.length;i++){
                if(giatiendatcuoc[i]!=0){
                    if(i==GTXingau1){
                        tienThuong+=giatiendatcuoc[i];
                    }
                    if(i==GTXingau2){
                        tienThuong+=giatiendatcuoc[i];
                    }
                    if(i==GTXingau3){
                        tienThuong+=giatiendatcuoc[i];
                    }
                    if(i != GTXingau1 && i != GTXingau2 && i != GTXingau3){
                        tienThuong-=giatiendatcuoc[i];
                    }
                }
            }

            if(tienThuong>0){
                Toast.makeText(getApplicationContext(),"Lụm lúa! Zô được "+tienThuong,Toast.LENGTH_SHORT).show();
            }else if(tienThuong==0){
                Toast.makeText(getApplicationContext(),"May quá! Huề vốn đấy!",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),"Đen vãi! Mất mịa "+tienThuong*(-1)+" rồi",Toast.LENGTH_SHORT).show();
            }

            LuuDuLieuNguoiDung(tienThuong);
            tvTien.setText(String.valueOf(tongtienmoi));

            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager=(AudioManager) getSystemService(AUDIO_SERVICE);
        float currentVolumeIndex=(float)audioManager.getStreamVolume(streamtype);
        float maxVolumeIndex=(float)audioManager.getStreamMaxVolume(streamtype);
        this.volume=currentVolumeIndex/maxVolumeIndex;
        this.setVolumeControlStream(streamtype);
        if(Build.VERSION.SDK_INT>=21){
            AudioAttributes audioAttrib= new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
            SoundPool.Builder builder=new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);
            this.soundPool=builder.build();
        }
        else {
            this.soundPool=new SoundPool(MAX_STREAMS,AudioManager.STREAM_MUSIC,0);
        }

        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded=true;
            }
        });

        this.soundIddice=this.soundPool.load(this,R.raw.dice,1);
        hinhXiNgau1 = (ImageView) findViewById(R.id.xingau1);
        hinhXiNgau2 = (ImageView) findViewById(R.id.xingau2);
        hinhXiNgau3 = (ImageView) findViewById(R.id.xingau3);
        tvTien=(TextView)findViewById(R.id.tvTien);

        checkSound=(CheckBox)findViewById(R.id.chksound);

        gridView = (GridView) findViewById(R.id.grdbanco);
        adapter = new Custom_GridView_BanCo(this, R.layout.custom_banco, dsHinh);
        gridView.setAdapter(adapter);

        luuTru=getSharedPreferences("luutruthongtin", Context.MODE_PRIVATE);
        tongtiencu=luuTru.getInt("TongTien",5000);
        tvTien.setText(String.valueOf(tongtiencu));

        nhacnen=MediaPlayer.create(this,R.raw.lncpmd);
        nhacnen.start();

        checkSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean kt) {
                if (kt) {
                    nhacnen.stop();
                } else {
                    try {
                        nhacnen.prepare();
                        nhacnen.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        handler=new Handler(callback);
    }


    private void LuuDuLieuNguoiDung(int tienthuong){
        SharedPreferences.Editor edit=luuTru.edit();
        tongtienmoi=tongtiencu+tienthuong;
        edit.putInt("TongTien",tongtienmoi);
        edit.commit();
        tongtiencu=tongtienmoi;
        Log.d("KetQua "," tien thuong "+tienThuong+" Tong Tien Moi "+tongtienmoi+" Tong Tien cu "+tongtiencu);
    }


    public void LacXiNgau(View v) {
        hinhXiNgau1.setImageResource(R.drawable.hinhdongxingau);
        hinhXiNgau2.setImageResource(R.drawable.hinhdongxingau);
        hinhXiNgau3.setImageResource(R.drawable.hinhdongxingau);

        cdXingau1 = (AnimationDrawable) hinhXiNgau1.getDrawable();
        cdXiNgau2 = (AnimationDrawable) hinhXiNgau2.getDrawable();

        cdXiNgau3 = (AnimationDrawable) hinhXiNgau3.getDrawable();
        kiemtra=0;
        for(int i=0 ; i<giatiendatcuoc.length ; i++){
            kiemtra+=giatiendatcuoc[i];
        }

        if (kiemtra==0){
            Toast.makeText(getApplicationContext(),"Bạn vui lòng đặt cược!",Toast.LENGTH_SHORT).show();
        }else {
            if (kiemtra>tongtiencu){
                Toast.makeText(getApplicationContext(),"Bạn không đủ tiền đặt cược!",Toast.LENGTH_SHORT).show();
            }else {
                if (loaded){
                    float leftVolumn=volume;
                    float rightVolumn=volume;
                    int streamId=this.soundPool.play(this.soundIddice,leftVolumn,rightVolumn,1,0,1f);
                }
                cdXingau1.start();
                cdXiNgau2.start();
                cdXiNgau3.start();

                tienThuong=0;
                timer.schedule(new LacXiNgau(),1000);
            }
        }
    }

    class LacXiNgau extends TimerTask{

        @Override
        public void run() {
            handler.sendEmptyMessage(0);

        }
    }

    private void RandomXiNgau1() {

        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd) {
            case 0:
                hinhXiNgau1.setImageResource(dsHinh[0]);
                GTXingau1 = rd;
                break;
            case 1:
                hinhXiNgau1.setImageResource(dsHinh[1]);
                GTXingau1 = rd;
                break;
            case 2:
                hinhXiNgau1.setImageResource(dsHinh[2]);
                GTXingau1 = rd;
                break;
            case 3:
                hinhXiNgau1.setImageResource(dsHinh[3]);
                GTXingau1 = rd;
                break;
            case 4:
                hinhXiNgau1.setImageResource(dsHinh[4]);
                GTXingau1 = rd;
                break;
            case 5:
                hinhXiNgau1.setImageResource(dsHinh[5]);
                GTXingau1 = rd;
                break;

        }
    }

    private void RandomXiNgau2() {

        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd) {
            case 0:
                hinhXiNgau2.setImageResource(dsHinh[0]);
                GTXingau2 = rd;
                break;
            case 1:
                hinhXiNgau2.setImageResource(dsHinh[1]);
                GTXingau2 = rd;
                break;
            case 2:
                hinhXiNgau2.setImageResource(dsHinh[2]);
                GTXingau2 = rd;
                break;
            case 3:
                hinhXiNgau2.setImageResource(dsHinh[3]);
                GTXingau2 = rd;
                break;
            case 4:
                hinhXiNgau2.setImageResource(dsHinh[4]);
                GTXingau2 = rd;
                break;
            case 5:
                hinhXiNgau2.setImageResource(dsHinh[5]);
                GTXingau2 = rd;
                break;

        }
    }

    private void RandomXiNgau3() {

        randomXiNgau = new Random();
        int rd = randomXiNgau.nextInt(6);
        switch (rd) {
            case 0:
                hinhXiNgau3.setImageResource(dsHinh[0]);
                GTXingau3 = rd;
                break;
            case 1:
                hinhXiNgau3.setImageResource(dsHinh[1]);
                GTXingau3 = rd;
                break;
            case 2:
                hinhXiNgau3.setImageResource(dsHinh[2]);
                GTXingau3 = rd;
                break;
            case 3:
                hinhXiNgau3.setImageResource(dsHinh[3]);
                GTXingau3 = rd;
                break;
            case 4:
                hinhXiNgau3.setImageResource(dsHinh[4]);
                GTXingau3 = rd;
                break;
            case 5:
                hinhXiNgau3.setImageResource(dsHinh[5]);
                GTXingau3 = rd;
                break;

        }
    }
}
