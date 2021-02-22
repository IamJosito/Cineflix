package com.example.cineflix;

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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newSession#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newSession extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public newSession() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newSession.
     */
    // TODO: Rename and change types and number of parameters
    public static newSession newInstance(String param1, String param2) {
        newSession fragment = new newSession();
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
        return inflater.inflate(R.layout.fragment_new_session, container, false);
    }

    EditText etNombreSesion, etHora;
    Spinner spinner;
    Button btnGuardarHora, btnBorrarHora;
    TextView guardar, goBack;
    ArrayList horas = new ArrayList();
    SQLite sqlite;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goBack = view.findViewById(R.id.goBack);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.viewSession);
            }
        });

        etNombreSesion = view.findViewById(R.id.etSesion);
        etHora = view.findViewById(R.id.etHoras);
        spinner = view.findViewById(R.id.spinner);
        btnBorrarHora = view.findViewById(R.id.btnBorrarHora);
        btnGuardarHora = view.findViewById(R.id.btnGuardarHora);
        guardar = view.findViewById(R.id.aceptar);

        sqlite = new SQLite(getContext(), "cine", null, 1);

        btnGuardarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horas.add(etHora.getText().toString());
                ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, horas);
                spinner.setAdapter(adapter);
                etHora.setText("");
            }
        });

        btnBorrarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horas.remove(spinner.getSelectedItemPosition());
                ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, horas);
                spinner.setAdapter(adapter);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etNombreSesion.getText().toString().isEmpty() || !etHora.getText().toString().isEmpty()){
                    sqlite.insertSession(etNombreSesion.getText().toString(), horas);
                }
            }
        });

    }
}