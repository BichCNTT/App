package com.example.ominext.mediaplayerapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//1. List bài hát: 1 recyclerView trong đó có: row (row là fragment) , mỗi row có 1 cái ctrúc dliệu, 1 cái adapter, dữ liệu sẽ được lưu trong sqlite
//view pager with fragment để chuyển đổi giữa 2 màn hình (màn hình tiếp theo cũng là fragment)
//1 nút play, 1 nút pause, 1 nút next bài, 1 nút prev, 1 nút lặp lại, 1 nút cho phát ngẫu nhiên
public class MainActivity extends AppCompatActivity {
    MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this);

    @BindView(R.id.button_random)
    Button buttonRandom;
    @BindView(R.id.button_back)
    Button buttonBack;
    @BindView(R.id.button_play)
    Button buttonPlay;
    @BindView(R.id.button_next)
    Button buttonNext;
    @BindView(R.id.button_replay)
    Button buttonReplay;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.tvCurrentTime)
    TextView tvCurrentTime;
    @BindView(R.id.tvTotalTime)
    TextView tvTotalTime;

    Handler handler;
    MediaPlayer mediaPlayer = new MediaPlayer();
    @BindView(R.id.title)
    TextView title;
    private int totalTime = 0;
    boolean audioAvailable = false;
    private int counter = 0;
    int check = 0;
    boolean play = true;//biến ktra xem có cho phép bật hay không
    int i = 0;
    boolean bl = false;
    RecyclerView.LayoutManager layoutManager;
    List<MyData> listSong = new ArrayList<>();
    MyAdapterRecyclerView adapter;
    int yep = 0;

    //Trước khi load xong ấn play thì sẽ bị lỗi
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        handler = new Handler();
        //chèn dliệu vào sqlite
//        insertData();
        //tạo 1 dòng
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        List<MyData> list = mySQLiteHelper.getAllSong();
        listSong.addAll(list);
        adapter = new MyAdapterRecyclerView(this, listSong);
        recyclerView.setAdapter(adapter);
        buttonPlay.setBackground(getResources().getDrawable(R.drawable.play));
    }

    //Có dữ liệu trong data rồi
    //Có các hàm xử lý cho seekbar rồi
    //Hiển thị tất dữ liệu ra recyclerView <-
    //Bắt sự kiện cho item
    //Kích vào 1 item thì gọi hàm initMedia
    //Kích vào nút play lần 1 thì phát và hiển thị nút pause
    //Kích vào nút play (nút pause) lần 2 thì dừng hiển thị nút play
    public void insertData() {
        //chèn dữ liệu vào 1 danh sách list, chèn dữ liệu vào trong database.
        MyData data = new MyData();
        data.setName("Good morning");
        data.setUrl("http://31.210.87.4/truetones/mp3_max/Good_Morning_01.mp3");
        data.setTime("22");
        mySQLiteHelper.insert(data);

        data = new MyData();
        data.setName("Good afternoon");
        data.setTime("24");
        data.setUrl("http://31.210.87.4/truetones/mp3_max/Love_Music.mp3");
        mySQLiteHelper.insert(data);

        data = new MyData();
        data.setName("Good evening");
        data.setTime("22");
        data.setUrl("http://31.210.87.4/truetones/mp3_max/Good_Morning_01.mp3");
        mySQLiteHelper.insert(data);
    }

    @OnClick({R.id.button_random, R.id.button_back, R.id.button_play, R.id.button_next, R.id.button_replay})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.button_random:
                if (check == 1) {
                    mediaPlayer.pause();
                }
                break;
            case R.id.button_back:
//                mediaPlayer.pause();
//                play = true;
//                buttonPlay.setBackground(getResources().getDrawable(R.drawable.play));
                if (i >= 1) {
                    i--;
                    tvTotalTime.setText("00:00");
                    tvCurrentTime.setText("00:00");
//                    buttonPlay.setVisibility(View.INVISIBLE);
                    title.setText(listSong.get(i).getName());
                    buttonPlay.setEnabled(false);
                    initMedia(listSong.get(i).getUrl(), i);
//                        buttonPlay.setBackground(getResources().getDrawable(R.drawable.play));
//                        play = true;
                }
                break;
            case R.id.button_play:
                    //nếu cho phép bật thì bật, sau đó cho phép pause
                    if (play == true) {
                        if (audioAvailable) {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                                //chạy current time
                                countTimer();
                                seekBar.setOnSeekBarChangeListener(seekBarChange);
                                check = 1;//set flag Đã từng ấn nút play
                                buttonPlay.setBackground(getResources().getDrawable(R.drawable.pause));
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Vui lòng chờ trong ít phút", Toast.LENGTH_LONG).show();
                        }
                        play = false;//không cho phép bật
                    } else {//ngược lại nếu không cho phép bật thì dừng+kích hoạt chế độ cho phép bật
                        mediaPlayer.pause();
                        buttonPlay.setBackground(getResources().getDrawable(R.drawable.play));
                        play = true;
                }
                break;
            case R.id.button_next:
                //1. xử lý với trường hợp trong lúc load dữ liệu kích nút play và pause:
                //cần: chỉ khi nào load xong thì mới cho phép kích play (kích play ms có tác dung)
                //lỗi: kích play và pause liên tục sẽ mắc lỗi không chạy được nhạc

                //2. Khi kích next thì không bật play, load xong mới bật play
                //Trong khi kích next gọi đến hàm initMedia, gọi hàm xong lấy được source, lấy được source, load đc giây mới cho phép sử dụng nút play
                if (i < listSong.size() - 1) {
                    i++;
                    tvTotalTime.setText("00:00");
                    tvCurrentTime.setText("00:00");
                    title.setText(listSong.get(i).getName());
                    buttonPlay.setEnabled(false);
                    initMedia(listSong.get(i).getUrl(), i);
                }
                break;
            case R.id.button_replay:
                if (check == 1) {
                    mediaPlayer.setLooping(true);
                    Toast.makeText(getApplicationContext(), "Đã kích hoạt chế độ lặp lại", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void initMedia(String dataSource, int position) {//sẽ gọi lại khi lấy được đường link
        //tạo mới 1 media với đường dẫn
        i = position;
        if (check == 1) {//nếu đã từng bật
            mediaPlayer.pause();
            buttonPlay.setBackground(getResources().getDrawable(R.drawable.play));
            play = true;
        }
        mediaPlayer = new MediaPlayer();
//          mediaPlayer.setLooping(true);//lặp
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(dataSource);
            //Get song link
            mediaPlayer.setOnBufferingUpdateListener(onBufferingLoading);//audio load vào bộ đệm và đang load đến phần nào
            mediaPlayer.setOnPreparedListener(onPrepareAudio);
            mediaPlayer.prepareAsync();
//            buttonPlay.setClickA
//            buttonPlay.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Không load được dữ liệu...", Toast.LENGTH_LONG).show();
        }
    }

    private SeekBar.OnSeekBarChangeListener seekBarChange = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    };
    private MediaPlayer.OnBufferingUpdateListener onBufferingLoading = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
            //i: là phần trăm audio đã load được khác với phần đang play
            seekBar.setSecondaryProgress(i);
        }
    };
    private MediaPlayer.OnPreparedListener onPrepareAudio = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            //audio sẵn sàng play
            //lấy đc total+time
            totalTime = mediaPlayer.getDuration();//time tính theo milliseconds
            int minute = totalTime / 1000 / 60;
            int second = totalTime / 1000 % 60;
            tvTotalTime.setText(minute + ":" + second);
            audioAvailable = true;//flag để đánh dấu mediaPlayer sẵn sàng
            buttonPlay.setEnabled(true);
        }
    };

    private void countTimer() {
        handler.postDelayed(timerCounter, 1000);//sau 1000 millisecond sẽ thay đổi giá trị 1 lần; timerCounter thuộc kiểu runnable
    }

    private Runnable timerCounter = new Runnable() {
        @Override
        public void run() {
            //lấy thời gian hiện tại
            counter = mediaPlayer.getCurrentPosition();
            int minute = (int) counter / 1000 / 60;
            int second = (int) counter / 1000 % 60;
            //định dạng
            //   java.text.SimpleDateFormat simpleDateFormat=new java.text.SimpleDateFormat("mm:ss");
            tvCurrentTime.setText(minute + ":" + second);
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            //Dùng đệ quy để lặp lại
            countTimer();
        }
    };
}
