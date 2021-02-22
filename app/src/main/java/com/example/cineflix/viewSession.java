package com.example.cineflix;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link viewSession#newInstance} factory method to
 * create an instance of this fragment.
 */
public class viewSession extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public viewSession() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment viewSession.
     */
    // TODO: Rename and change types and number of parameters
    public static viewSession newInstance(String param1, String param2) {
        viewSession fragment = new viewSession();
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
        return inflater.inflate(R.layout.fragment_view_session, container, false);

    }

    SQLite sqlite;
    LinearLayout ll;
    LinearLayout layoutSessions;
    TextView goBack, nuevaSesion;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutSessions = view.findViewById(R.id.layoutSessions);
        nuevaSesion = view.findViewById(R.id.new_session);
        goBack = view.findViewById(R.id.goBack);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.mainScreen);
            }
        });

        nuevaSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.newSession);
            }
        });

        if(savedInstanceState == null){
            sqlite = new SQLite(getContext(), "cine", null, 1);
        }else{
            sqlite = (SQLite) savedInstanceState.getSerializable("cine");
        }

        SQLiteDatabase db = sqlite.getWritableDatabase();
        Cursor filasSalas = db.rawQuery("SELECT * FROM salas", null);
        if(filasSalas.getColumnCount() != 0){
            while(filasSalas.moveToNext()){

                if(filasSalas.getInt(0)%3 == 1){
                    //Creamos un nuevo linear layout para agregarlo al que ya tenemos en nuestro scroll view.
                    ll = new LinearLayout(view.getContext());
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    layoutSessions.addView(ll);
                }


                //Una vez agregado, creamos unos parametros para asignarselos al imageView
                LinearLayout.LayoutParams lpTxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //Ponemos el margen, usamos el | (int) ((int) X*getContext().getResources().getDisplayMetrics().density) | para convertilos a PX y podamos hacerlo bien
                lpTxt.setMargins((int) ((int) 10*getContext().getResources().getDisplayMetrics().density), 0, 0,0);


                //Creamos el texto para que salga el titulo de la peli.
                TextView txt = new TextView(getContext());
                txt.setLayoutParams(lpTxt);
                txt.setText("Sala: " + filasSalas.getString(1));
                txt.setGravity(Gravity.CENTER);
                txt.setTextColor(Color.WHITE);
                txt.getLayoutParams().height = (int) ((int) 40*getContext().getResources().getDisplayMetrics().density);
                txt.getLayoutParams().width = (int) ((int) 110*getContext().getResources().getDisplayMetrics().density);
                ll.addView(txt);
            }
        }
    }
}