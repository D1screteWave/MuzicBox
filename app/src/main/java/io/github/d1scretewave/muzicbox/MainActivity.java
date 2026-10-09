package io.github.d1scretewave.muzicbox;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MuzicBox 的主界面。
 *
 * <p>第二章在 Activity 中完成本地音乐扫描和基础播放。音乐离开当前页面后会暂停，
 * 后台播放将在下一章交给 Service 处理。</p>
 */
public class MainActivity extends Activity {

    private static final String TAG = "MuzicBox";
    private static final int REQUEST_AUDIO_PERMISSION = 1001;

    private final List<Song> songs = new ArrayList<>();
    private final ExecutorService scanExecutor = Executors.newSingleThreadExecutor();

    private SongAdapter songAdapter;
    private MediaPlayer mediaPlayer;
    private int currentSongIndex = -1;
    private boolean isPreparing;

    private TextView scanStatusText;
    private TextView currentTitleText;
    private TextView currentArtistText;
    private Button scanButton;
    private Button playPauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setupSongList();
        setupButtons();
        checkPermissionAndScan();
    }

    private void bindViews() {
        scanStatusText = findViewById(R.id.text_scan_status);
        currentTitleText = findViewById(R.id.text_current_title);
        currentArtistText = findViewById(R.id.text_current_artist);
        scanButton = findViewById(R.id.button_scan);
        playPauseButton = findViewById(R.id.button_play_pause);
    }

    private void setupSongList() {
        ListView songListView = findViewById(R.id.list_songs);
        songAdapter = new SongAdapter(this, songs);
        songListView.setAdapter(songAdapter);
        songListView.setEmptyView(findViewById(R.id.text_empty));
        songListView.setOnItemClickListener((parent, view, position, id) -> playSong(position));
    }

    private void setupButtons() {
        scanButton.setOnClickListener(view -> checkPermissionAndScan());
        findViewById(R.id.button_previous).setOnClickListener(view -> playPrevious());
        playPauseButton.setOnClickListener(view -> togglePlayPause());
        findViewById(R.id.button_next).setOnClickListener(view -> playNext());
    }

    private void checkPermissionAndScan() {
        if (hasAudioPermission()) {
            scanLocalMusic();
            return;
        }

        scanStatusText.setText(R.string.permission_needed);
        scanButton.setText(R.string.grant_permission);
        requestPermissions(
                new String[]{getAudioPermission()},
                REQUEST_AUDIO_PERMISSION
        );
    }

    private boolean hasAudioPermission() {
        return checkSelfPermission(getAudioPermission()) == PackageManager.PERMISSION_GRANTED;
    }

    private String getAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Manifest.permission.READ_MEDIA_AUDIO;
        }
        return Manifest.permission.READ_EXTERNAL_STORAGE;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_AUDIO_PERMISSION) {
            return;
        }

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scanLocalMusic();
        } else {
            scanStatusText.setText(R.string.permission_denied);
            scanButton.setText(R.string.grant_permission);
            Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_LONG).show();
        }
    }

    private void scanLocalMusic() {
        releasePlayer();
        resetCurrentSong();
        scanButton.setEnabled(false);
        scanButton.setText(R.string.scanning);
        scanStatusText.setText(R.string.scanning_local_music);

        scanExecutor.execute(() -> {
            List<Song> scannedSongs = LocalMusicScanner.scan(getContentResolver());

            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) {
                    return;
                }

                songs.clear();
                songs.addAll(scannedSongs);
                songAdapter.notifyDataSetChanged();
                scanButton.setEnabled(true);
                scanButton.setText(R.string.rescan);
                scanStatusText.setText(getString(R.string.song_count, songs.size()));

                if (songs.isEmpty()) {
                    releasePlayer();
                    resetCurrentSong();
                }
            });
        });
    }

    private void playSong(int position) {
        if (position < 0 || position >= songs.size()) {
            return;
        }

        releasePlayer();
        currentSongIndex = position;
        Song song = songs.get(position);
        updateCurrentSong(song);
        isPreparing = true;
        playPauseButton.setText(R.string.loading);
        songAdapter.setSelectedPosition(position);

        MediaPlayer newPlayer = new MediaPlayer();
        mediaPlayer = newPlayer;

        try {
            newPlayer.setDataSource(this, song.getUri());
            newPlayer.setOnPreparedListener(player -> {
                if (mediaPlayer != player) {
                    return;
                }
                isPreparing = false;
                player.start();
                playPauseButton.setText(R.string.pause);
            });
            newPlayer.setOnCompletionListener(player -> playNext());
            newPlayer.setOnErrorListener((player, what, extra) -> {
                Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
                releasePlayer();
                playPauseButton.setText(R.string.play);
                Toast.makeText(this, R.string.playback_failed, Toast.LENGTH_SHORT).show();
                return true;
            });
            newPlayer.prepareAsync();
        } catch (IOException | SecurityException exception) {
            Log.e(TAG, "Unable to play " + song.getUri(), exception);
            isPreparing = false;
            playPauseButton.setText(R.string.play);
            Toast.makeText(this, R.string.playback_failed, Toast.LENGTH_SHORT).show();
            releasePlayer();
        }
    }

    private void togglePlayPause() {
        if (songs.isEmpty()) {
            Toast.makeText(this, R.string.no_music_to_play, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mediaPlayer == null) {
            playSong(currentSongIndex >= 0 ? currentSongIndex : 0);
            return;
        }

        if (isPreparing) {
            return;
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseButton.setText(R.string.play);
        } else {
            mediaPlayer.start();
            playPauseButton.setText(R.string.pause);
        }
    }

    private void playPrevious() {
        if (songs.isEmpty()) {
            showNoMusicMessage();
            return;
        }

        int previousIndex = currentSongIndex <= 0
                ? songs.size() - 1
                : currentSongIndex - 1;
        playSong(previousIndex);
    }

    private void playNext() {
        if (songs.isEmpty()) {
            showNoMusicMessage();
            return;
        }

        int nextIndex = currentSongIndex < 0 || currentSongIndex >= songs.size() - 1
                ? 0
                : currentSongIndex + 1;
        playSong(nextIndex);
    }

    private void showNoMusicMessage() {
        Toast.makeText(this, R.string.no_music_to_play, Toast.LENGTH_SHORT).show();
    }

    private void updateCurrentSong(Song song) {
        currentTitleText.setText(song.getTitle());
        currentArtistText.setText(song.getArtist());
    }

    private void resetCurrentSong() {
        currentSongIndex = -1;
        currentTitleText.setText(R.string.no_song_selected);
        currentArtistText.setText(R.string.tap_song_to_play);
        playPauseButton.setText(R.string.play);
        songAdapter.setSelectedPosition(-1);
    }

    private void releasePlayer() {
        isPreparing = false;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 第二章暂不提供后台播放，页面离开前台后立即释放播放器。
        if (mediaPlayer != null) {
            releasePlayer();
            playPauseButton.setText(R.string.play);
        }
    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        scanExecutor.shutdownNow();
        super.onDestroy();
    }
}
