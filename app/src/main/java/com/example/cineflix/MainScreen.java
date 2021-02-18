package com.example.cineflix;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static MainScreen newInstance(String param1, String param2) {
        MainScreen fragment = new MainScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false);
    }

    Spinner sessionSpinner, hoursSpinner;
    ArrayAdapter<String> adapterSessions, adapterHours;
    LinearLayout layoutFilms;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionSpinner = view.findViewById(R.id.sessionsSpinner);
        hoursSpinner = view.findViewById(R.id.hoursSpinner);
        layoutFilms = view.findViewById(R.id.layoutFilms);

        String[] sessions = new String[] {
                "Sesion 1", "Sesion 2", "Sesion 3", "Sesion 4", "Sesion 5", "Sesion 6", "Sesion 7"
        };

        String[][] hours = new String[][] {
                {"11:50","12:50"},
                {"12:50","13:50"},
                {"10:50","12:50"},
                {"9:50","12:50"},
                {"15:50","17:50"},
                {"12:50","23:50"},
                {"19:50","14:50"}
        };
        adapterSessions = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, sessions);
        adapterHours = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, hours[0]);

        adapterSessions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sessionSpinner.setAdapter(adapterSessions);
        hoursSpinner.setAdapter(adapterHours);

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapterHours = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, hours[position]);
                hoursSpinner.setAdapter(adapterHours);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Creamos un nuevo linear layout para agregarlo al que ya tenemos en nuestro scroll view.
        LinearLayout ll = new LinearLayout(view.getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        layoutFilms.addView(ll);

        //Una vez agregado, creamos unos parametros para asignarselos al imageView
        LinearLayout.LayoutParams lpImages = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //Ponemos el margen, usamos el | (int) ((int) X*getContext().getResources().getDisplayMetrics().density) | para convertilos a PX y podamos hacerlo bien
        lpImages.setMargins((int) ((int) 20*getContext().getResources().getDisplayMetrics().density), (int) ((int) 10*getContext().getResources().getDisplayMetrics().density), 0,(int) ((int) 10*getContext().getResources().getDisplayMetrics().density));

        //Ahora hacemos el image view.
        ImageView img = new ImageView(getContext());
        img.setLayoutParams(lpImages);
        img.getLayoutParams().height = (int) ((int) 100*getContext().getResources().getDisplayMetrics().density);
        img.getLayoutParams().width = (int) ((int) 100*getContext().getResources().getDisplayMetrics().density);
        img.setBackgroundColor(Color.RED);

        //Finalmente lo agremaos al Linear Layout que hemos creado.
        ll.addView(img);

        //Una vez agregado, creamos unos parametros para asignarselos al imageView
        LinearLayout.LayoutParams lpTxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //Ponemos el margen, usamos el | (int) ((int) X*getContext().getResources().getDisplayMetrics().density) | para convertilos a PX y podamos hacerlo bien
        lpTxt.setMargins((int) ((int) -101*getContext().getResources().getDisplayMetrics().density), (int) ((int) 110*getContext().getResources().getDisplayMetrics().density), 0,0);


        //Creamos el texto para que salga el titulo de la peli.
        TextView txt = new TextView(getContext());
        txt.setLayoutParams(lpTxt);
        txt.setText("DINAMICO");
        txt.setGravity(Gravity.CENTER);
        txt.setTextColor(Color.WHITE);
        txt.getLayoutParams().height = (int) ((int) 20*getContext().getResources().getDisplayMetrics().density);
        txt.getLayoutParams().width = (int) ((int) 100*getContext().getResources().getDisplayMetrics().density);
        ll.addView(txt);

    }
}