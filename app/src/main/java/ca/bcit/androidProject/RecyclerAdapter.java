package ca.bcit.androidProject;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context _context;
    private ArrayList<States> stateArrayList;

    private final static int RED_ZONE = Color.RED;

    private final static int YELLOW_ZONE = Color.YELLOW;

    private final static int GREEN_ZONE = Color.GREEN;

    public RecyclerAdapter(Context context, ArrayList<States> toonArrayList) {
        _context = context;
        stateArrayList = toonArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView _cardView;

        public ViewHolder(CardView v) {
            super(v);
            _cardView = v;
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(_context)
                .inflate(R.layout.card_item, parent, false);

        return new ViewHolder(cv);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        final CardView cardView = holder._cardView;

        States currentItem = stateArrayList.get(position);
        Float currentSLR = Float.parseFloat(currentItem.getSlrRate());

        TextView tvId = cardView.findViewById(R.id.id);
        TextView tvName = cardView.findViewById(R.id.stateName);
        TextView tvSLR = cardView.findViewById(R.id.slrRate);

        tvId.setText(String.valueOf(currentItem.getId()));
        tvName.setText(currentItem.getStateName());


        tvSLR.setText("SLR: " + currentItem.getSlrRate());

        if (currentSLR <= 1) {
            tvSLR.setTextColor(GREEN_ZONE);
        } else if (currentSLR > 1 && currentSLR <= 2) {
            tvSLR.setTextColor(YELLOW_ZONE);
        } else if (currentSLR > 2) {
            tvSLR.setTextColor(RED_ZONE);
        }

    }

    @Override
    public int getItemCount() {
        return stateArrayList.size();
    }
}