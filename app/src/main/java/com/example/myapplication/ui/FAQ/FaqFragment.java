package com.example.myapplication.ui.FAQ;

import android.animation.LayoutTransition;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

public class FaqFragment extends Fragment {

    TextView title,description,title2,description2,title3,description3;
    LinearLayout linearLayout,linearLayout2,linearLayout3;
    CardView cardView,cardView2,cardView3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_faq, container, false);
        title=root.findViewById(R.id.title);
        description=root.findViewById(R.id.details);
        linearLayout=root.findViewById(R.id.layout);
        cardView=root.findViewById(R.id.cardView);
        title2=root.findViewById(R.id.title2);
        description2=root.findViewById(R.id.details2);
        linearLayout2=root.findViewById(R.id.layout2);
        cardView2=root.findViewById(R.id.cardView2);
        title3=root.findViewById(R.id.title3);
        description3=root.findViewById(R.id.details3);
        linearLayout3=root.findViewById(R.id.layout3);
        cardView3=root.findViewById(R.id.cardView3);
        linearLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linearLayout2.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linearLayout3.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = (description.getVisibility()==View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(linearLayout, new AutoTransition());
                description.setVisibility(v);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = (description2.getVisibility()==View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(linearLayout2, new AutoTransition());
                description2.setVisibility(v);
            }
        });
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int v = (description3.getVisibility()==View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(linearLayout3, new AutoTransition());
                description3.setVisibility(v);
            }
        });

        return root;
    }
}