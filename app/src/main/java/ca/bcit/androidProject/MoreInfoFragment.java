package ca.bcit.androidProject;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class MoreInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_info, container, false);


        TextView nasa_Link = view.findViewById(R.id.InfoLinkContent);
        nasa_Link.setMovementMethod(LinkMovementMethod.getInstance());

        TextView ocean_Link = view.findViewById(R.id.InfoLinkContent2);
        ocean_Link.setMovementMethod(LinkMovementMethod.getInstance());

        TextView natGeo_Link = view.findViewById(R.id.InfoLinkContent3);
        natGeo_Link.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }

}