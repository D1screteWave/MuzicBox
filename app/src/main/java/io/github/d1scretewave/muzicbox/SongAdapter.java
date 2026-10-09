package io.github.d1scretewave.muzicbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/** 把 Song 列表转换成 ListView 中可见的每一行。 */
public class SongAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<Song> songs;
    private int selectedPosition = -1;

    public SongAdapter(Context context, List<Song> songs) {
        inflater = LayoutInflater.from(context);
        this.songs = songs;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Song getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_song, parent, false);
            holder = new ViewHolder(
                    convertView.findViewById(R.id.text_song_title),
                    convertView.findViewById(R.id.text_song_artist),
                    convertView.findViewById(R.id.text_song_duration)
            );
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Song song = getItem(position);
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());
        holder.duration.setText(formatDuration(song.getDurationMillis()));
        convertView.setBackgroundResource(
                position == selectedPosition
                        ? R.drawable.bg_song_selected
                        : android.R.color.transparent
        );

        return convertView;
    }

    private String formatDuration(long durationMillis) {
        long totalSeconds = durationMillis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    private static class ViewHolder {
        final TextView title;
        final TextView artist;
        final TextView duration;

        ViewHolder(TextView title, TextView artist, TextView duration) {
            this.title = title;
            this.artist = artist;
            this.duration = duration;
        }
    }
}
