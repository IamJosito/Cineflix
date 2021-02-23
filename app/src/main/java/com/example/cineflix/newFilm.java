package com.example.cineflix;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newFilm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newFilm extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public newFilm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newFilm.
     */
    // TODO: Rename and change types and number of parameters
    public static newFilm newInstance(String param1, String param2) {
        newFilm fragment = new newFilm();
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

    public static final int PICK_IMAGE = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_film, container, false);
    }
    TextView goBack, guardar;
    ImageView img;
    EditText nom_pelicula;
    ArrayList sesiones = new ArrayList();
    ArrayList<ArrayList> horarios = new ArrayList();
    ArrayAdapter adapterSessions, adapterHours;
    Spinner sessionSpinner, hoursSpinner;
    ImageView foto;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SQLite sqlite = new SQLite(getContext(), "cine", null, 1);
        goBack = view.findViewById(R.id.viewFilm);
        img = view.findViewById(R.id.foto);
        sessionSpinner = view.findViewById(R.id.spinnerSala);
        hoursSpinner = view.findViewById(R.id.spinnerHoras);
        guardar = view.findViewById(R.id.aceptar);
        nom_pelicula = view.findViewById(R.id.nom_pelicula);
        foto = view.findViewById(R.id.foto);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.mainScreen);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        SQLiteDatabase db = sqlite.getWritableDatabase();
        Cursor filasSesiones = db.rawQuery("SELECT * FROM salas", null);
        if(filasSesiones.getColumnCount() != 0){
            while(filasSesiones.moveToNext()){
                sesiones.add(filasSesiones.getString(1));
                Cursor filasHorarios = db.rawQuery("SELECT * FROM horarios WHERE sala = " + filasSesiones.getInt(0), null);
                if(filasHorarios.getColumnCount() != 0){
                    horarios.add(new ArrayList());
                    while(filasHorarios.moveToNext()){
                        horarios.get(horarios.size()-1).add(filasHorarios.getString(1));
                    }
                }
            }
        }

        if(sesiones.size() != 0 || horarios.size() != 0){
            adapterSessions = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, sesiones);
            adapterHours = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, horarios.get(0));

            adapterSessions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sessionSpinner.setAdapter(adapterSessions);
            hoursSpinner.setAdapter(adapterHours);


            sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    adapterHours = new ArrayAdapter<String>(getContext(),R.layout.spinner_items, horarios.get(position));
                    hoursSpinner.setAdapter(adapterHours);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) foto.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageInByte = baos.toByteArray();

                String nomPeli = nom_pelicula.getText().toString();
                String sala = sessionSpinner.getSelectedItem().toString();
                String hora = hoursSpinner.getSelectedItem().toString();
                sqlite.insertFilm(nomPeli,sala,hora,imageInByte);
                SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_STANDARD)
                        .setText("Pelicula añadida!")
                        .setDuration(Style.DURATION_SHORT)
                        .setFrame(Style.FRAME_KITKAT)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE))
                        .setAnimations(Style.ANIMATIONS_POP).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }
}