package com.example.deezer;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.deezer.members.Song;
import com.example.root.R;


public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        Song song = (Song) dataFromActivity.getSerializable("song");
        View result = inflater.inflate(R.layout.activity_details_fragment, container, false);

        ImageView albumCover = result.findViewById(R.id.albumCover);
        albumCover.setImageBitmap(dataFromActivity.getParcelable("cover"));

        TextView title = result.findViewById(R.id.songTitle);
        title.setText(song.getTitle());

        TextView duration = result.findViewById(R.id.songDuration);
        duration.setText(song.getDuration());

        TextView album = result.findViewById(R.id.songAlbum);
        album.setText(song.getAlbumName());

        TextView artist = result.findViewById(R.id.songArtist);
        artist.setText(song.getArtist());

        Button finishButton = (Button) result.findViewById(R.id.finishButton);
        finishButton.setOnClickListener(clk -> {
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            getActivity().finish();
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) context;
    }
}