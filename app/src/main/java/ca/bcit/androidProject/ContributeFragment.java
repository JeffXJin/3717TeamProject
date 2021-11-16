package ca.bcit.androidProject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.braintreepayments.cardform.view.CardForm;

public class ContributeFragment extends Fragment {

    CardForm cardForm;
    Button buy;
    AlertDialog.Builder alertBuilder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contribute, container, false);
        Button button = view.findViewById(R.id.btnDonate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreditFormActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

}