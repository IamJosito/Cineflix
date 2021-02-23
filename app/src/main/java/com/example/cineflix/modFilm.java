package com.example.cineflix;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link modFilm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class modFilm extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public modFilm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment modFilm.
     */
    // TODO: Rename and change types and number of parameters
    public static modFilm newInstance(String param1, String param2) {
        modFilm fragment = new modFilm();
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
        return inflater.inflate(R.layout.fragment_mod_film, container, false);
    }

    String nombreAntiguo, horaAntiguo, salaAntiguo;
    byte[] img;
    ArrayList sesiones = new ArrayList();
    ArrayList<ArrayList> horarios = new ArrayList();
    ArrayAdapter adapterSessions, adapterHours;
    Spinner sessionSpinner, hoursSpinner;
    ImageView icon;
    EditText etNomPelicula;
    TextView modificar, goBack;
    int idPeli;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();

        icon = view.findViewById(R.id.foto);
        etNomPelicula = view.findViewById(R.id.nom_pelicula);
        modificar = view.findViewById(R.id.modificar);

        goBack = view.findViewById(R.id.goBack);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.mainScreen);
            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        nombreAntiguo = bundle.getString("nombre");
        horaAntiguo = bundle.getString("hora");
        salaAntiguo = bundle.getString("sala");
        img = bundle.getByteArray("img");
        idPeli = bundle.getInt("idPeli");

        Bitmap bmp = BitmapFactory.decodeByteArray(img,0,img.length);
        icon.setImageBitmap(bmp);

        etNomPelicula.setText(nombreAntiguo);

        sessionSpinner = view.findViewById(R.id.spinnerSala);
        hoursSpinner = view.findViewById(R.id.spinnerHoras);

        SQLite sqlite = new SQLite(getContext(), "cine", null, 1);

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

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) icon.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageInByte = baos.toByteArray();
                sqlite.updateFilm(etNomPelicula.getText().toString(), imageInByte, idPeli);

                SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_STANDARD)
                        .setText("Modificado con exito!")
                        .setDuration(Style.DURATION_SHORT)
                        .setFrame(Style.FRAME_KITKAT)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE))
                        .setAnimations(Style.ANIMATIONS_POP).show();

                Navigation.findNavController(v).navigate(R.id.mainScreen);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Uri imageUri = data.getData();
            icon.setImageURI(imageUri);
        }
    }
}