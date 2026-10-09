package io.github.d1scretewave.muzicbox;

import android.net.Uri;

/** 保存从系统媒体库中读取到的一首本地音乐。 */
public class Song {

    private final long id;
    private final String title;
    private final String artist;
    private final long durationMillis;
    private final Uri uri;

    public Song(long id, String title, String artist, long durationMillis, Uri uri) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationMillis = durationMillis;
        this.uri = uri;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public Uri getUri() {
        return uri;
    }
}
