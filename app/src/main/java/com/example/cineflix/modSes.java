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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link modSes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class modSes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public modSes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment modSes.
     */
    // TODO: Rename and change types and number of parameters
    public static modSes newInstance(String param1, String param2) {
        modSes fragment = new modSes();
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
        return inflater.inflate(R.layout.fragment_mod_ses, container, false);
    }

    TextView goBack, modificar;
    EditText etNombre, etHoras;
    Spinner spinnerHoras;
    Button btnGuardarHora, btnBorrarHora;
    ArrayList horas = new ArrayList();
    ArrayAdapter adapterHours;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnBorrarHora = view.findViewById(R.id.btnBorrarHora);
        btnGuardarHora = view.findViewById(R.id.btnGuardarHora);
        goBack = view.findViewById(R.id.goBack);
        etNombre = view.findViewById(R.id.etNombre);
        etHoras = view.findViewById(R.id.etHoras);
        spinnerHoras = view.findViewById(R.id.spinner);
        modificar = view.findViewById(R.id.modificar);

        Bundle bundle = getArguments();
        int idSala = bundle.getInt("idSala");

        SQLite sqlite = new SQLite(getContext(), "cine", null, 1);

        SQLiteDatabase db = sqlite.getWritableDatabase();
        Cursor filasSalas = db.rawQuery("SELECT * FROM salas WHERE codigo = " + idSala, null);
        filasSalas.moveToLast();
        System.out.println(idSala);

        etNombre.setText(filasSalas.getString(1));

        if(filasSalas.getColumnCount() != 0){
            Cursor filasHorarios = db.rawQuery("SELECT * FROM horarios WHERE sala = " + idSala, null);
            if(filasHorarios.getColumnCount() != 0){
                while(filasHorarios.moveToNext()){
                    horas.add(filasHorarios.getString(1));
                }
            }
        }

        if(horas.size() != 0){
            adapterHours = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, horas);
            adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerHoras.setAdapter(adapterHours);
        }

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.viewSession);
            }
        });

        btnGuardarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horas.add(etHoras.getText().toString());
                ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, horas);
                spinnerHoras.setAdapter(adapter);
                etHoras.setText("");
            }
        });

        btnBorrarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horas.remove(spinnerHoras.getSelectedItemPosition());
                ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, horas);
                spinnerHoras.setAdapter(adapter);
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreSesion = etNombre.getText().toString();
                sqlite.updateSession(idSala,nombreSesion,horas );
                SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_STANDARD)
                        .setText("Modificado con exito!")
                        .setDuration(Style.DURATION_SHORT)
                        .setFrame(Style.FRAME_KITKAT)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE))
                        .setAnimations(Style.ANIMATIONS_POP).show();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("idSala",idSala);
                Navigation.findNavController(v).navigate(R.id.clickOnSession, bundle2);
            }
        });

    }
}