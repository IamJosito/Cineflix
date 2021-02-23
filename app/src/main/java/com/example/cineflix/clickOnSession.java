package com.example.cineflix;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link clickOnSession#newInstance} factory method to
 * create an instance of this fragment.
 */
public class clickOnSession extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public clickOnSession() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment clickOnSession.
     */
    // TODO: Rename and change types and number of parameters
    public static clickOnSession newInstance(String param1, String param2) {
        clickOnSession fragment = new clickOnSession();
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
        return inflater.inflate(R.layout.fragment_click_on_session, container, false);
    }

    Spinner horas;
    TextView nomSesion, borrar, goBack, modificar;
    ArrayList horarios = new ArrayList();
    ArrayAdapter adapterHours;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        horas = view.findViewById(R.id.spinnerHoras);
        nomSesion = view.findViewById(R.id.nomSesion);
        borrar = view.findViewById(R.id.borrar);
        modificar = view.findViewById(R.id.modificar);
        goBack = view.findViewById(R.id.goBack);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.mainScreen);
            }
        });
        Bundle bundle = getArguments();
        int idSesion = bundle.getInt("idSala");

        SQLite sqlite = new SQLite(getContext(), "cine", null, 1);

        SQLiteDatabase db = sqlite.getWritableDatabase();
        Cursor filasSalas = db.rawQuery("SELECT * FROM salas WHERE codigo = " + idSesion, null);
        filasSalas.moveToLast();
        nomSesion.setText(filasSalas.getString(1));

        if(filasSalas.getColumnCount() != 0){
            Cursor filasHorarios = db.rawQuery("SELECT * FROM horarios WHERE sala = " + idSesion, null);
            if(filasHorarios.getColumnCount() != 0){
                while(filasHorarios.moveToNext()){
                    horarios.add(filasHorarios.getString(1));
                }
            }
        }

        if(horarios.size() != 0){
            adapterHours = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, horarios);
            adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            horas.setAdapter(adapterHours);
        }

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlite.deleteSession(idSesion);
                SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_STANDARD)
                        .setText("Sesion borrada con éxito!")
                        .setDuration(Style.DURATION_SHORT)
                        .setFrame(Style.FRAME_KITKAT)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE))
                        .setAnimations(Style.ANIMATIONS_POP).show();
                Navigation.findNavController(v).navigate(R.id.viewSession);

            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle2 = new Bundle();
                bundle2.putInt("idSala", idSesion);
                Navigation.findNavController(v).navigate(R.id.modSes, bundle2);
            }
        });
    }
}