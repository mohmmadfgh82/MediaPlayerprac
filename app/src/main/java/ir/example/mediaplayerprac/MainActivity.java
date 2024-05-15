package ir.example.mediaplayerprac;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;//Media Player referansı
    private Button playButton;
    private SeekBar musicSeekBar;

    private Runnable runnable;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        Uri mediaUri = intent.getData();

        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.kerm);

        playButton = (Button) findViewById(R.id.playButton);
        musicSeekBar = (SeekBar)findViewById(R.id.musicSeekBar);
        musicSeekBar.setMax(mediaPlayer.getDuration());

        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler(Looper.getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                int currentPosition = mediaPlayer.getCurrentPosition();
                musicSeekBar.setProgress(currentPosition);
                handler.postDelayed(this, 100);
            }
        };

        handler.post(runnable);



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    pauseMusic();
                }else{
                    playMusic();
                }

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int sure = mp.getDuration();
                String sureString = String.valueOf(sure / 1000);
                Toast.makeText(getApplicationContext(),"Etmam music" + sureString, Toast.LENGTH_LONG).show();
                playButton.setText("Play");
            }
        });





    }

    @Override
    protected void onDestroy() {

        if(mediaPlayer !=null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        super.onDestroy();
    }

    private void pauseMusic(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
            playButton.setText("Play");
            handler.removeCallbacks(runnable);
        }


    }
    private void playMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.start();
            playButton.setText("Pause");
            handler.post(runnable);
        }
    }



}