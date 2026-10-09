package io.github.d1scretewave.muzicbox;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/** 通过 MediaStore 查询手机媒体库中的本地音乐。 */
public final class LocalMusicScanner {

    private static final String TAG = "LocalMusicScanner";

    private LocalMusicScanner() {
    }

    public static List<Song> scan(ContentResolver contentResolver) {
        List<Song> songs = new ArrayList<>();
        Uri collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
                + " AND " + MediaStore.Audio.Media.DURATION + " > 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " COLLATE NOCASE ASC";

        try (Cursor cursor = contentResolver.query(
                collection,
                projection,
                selection,
                null,
                sortOrder
        )) {
            if (cursor == null) {
                return songs;
            }

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String title = valueOrFallback(cursor.getString(titleColumn), "未知歌曲");
                String artist = valueOrFallback(cursor.getString(artistColumn), "未知艺术家");
                long duration = cursor.getLong(durationColumn);
                Uri songUri = ContentUris.withAppendedId(collection, id);

                songs.add(new Song(id, title, artist, duration, songUri));
            }
        } catch (SecurityException | IllegalArgumentException exception) {
            Log.e(TAG, "Unable to query local music", exception);
        }

        return songs;
    }

    private static String valueOrFallback(String value, String fallback) {
        if (value == null || value.trim().isEmpty() || "<unknown>".equals(value)) {
            return fallback;
        }
        return value;
    }
}
