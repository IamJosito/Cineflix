package com.example.cineflix;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link clickOnFilm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class clickOnFilm extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public clickOnFilm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment clickOnFilm.
     */
    // TODO: Rename and change types and number of parameters
    public static clickOnFilm newInstance(String param1, String param2) {
        clickOnFilm fragment = new clickOnFilm();
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
        return inflater.inflate(R.layout.fragment_click_on_film, container, false);
    }

    ImageView img;
    TextView nombreTv, salaTv, horaTv, modificar, borrar;
    String salaToMod, horaToMod, nombreToMod;
    byte[] imgToMod;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        img = view.findViewById(R.id.foto);
        nombreTv = view.findViewById(R.id.nomPelicula);
        salaTv = view.findViewById(R.id.sala);
        horaTv = view.findViewById(R.id.hora);
        modificar = view.findViewById(R.id.modificar);
        borrar = view.findViewById(R.id.borrar);

        SQLite sqlite = new SQLite(getContext(), "cine", null,1);
        SQLiteDatabase db = sqlite.getWritableDatabase();

        String sqlPeli = "SELECT * FROM peliculas WHERE codigo = "+bundle.getInt("peli")+"";
        String sqlSala = "SELECT * FROM salas WHERE codigo = (SELECT cancion FROM horarios WHERE codigo = "+bundle.getInt("hora")+")";
        String sqlHora = "SELECT * FROM horarios WHERE codigo = " + bundle.getInt("hora");
        Cursor peli = db.rawQuery(sqlPeli,null);
        Cursor sala = db.rawQuery(sqlSala,null);
        Cursor hora = db.rawQuery(sqlHora, null);

        peli.moveToFirst();
        sala.moveToFirst();
        hora.moveToFirst();

        System.out.println("Hora: " + bundle.getInt("hora") + " Peli: " + bundle.getInt("peli"));
        nombreTv.setText(peli.getString(2));
        byte[] imgByte = peli.getBlob(1);
        Bitmap bmp = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        img.setImageBitmap(bmp);
        salaTv.setText(sala.getString(1));
        horaTv.setText(hora.getString(1));

        salaToMod = sala.getString(1);
        horaToMod = hora.getString(1);
        nombreToMod = peli.getString(2);
        imgToMod = imgByte;

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle2 = new Bundle();
                bundle2.putString("sala",salaToMod);
                bundle2.putString("hora",horaToMod);
                bundle2.putString("nombre",nombreToMod);
                bundle2.putByteArray("img", imgToMod);
                bundle2.putInt("idPeli", bundle.getInt("peli"));
                Navigation.findNavController(v).navigate(R.id.modFilm, bundle2);
            }
        });

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlite.deleteFilm(bundle.getInt("peli"));
                SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_STANDARD)
                        .setText("Pelicula "+nombreToMod+" borrada con éxito!")
                        .setDuration(Style.DURATION_SHORT)
                        .setFrame(Style.FRAME_KITKAT)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE))
                        .setAnimations(Style.ANIMATIONS_POP).show();
                Navigation.findNavController(v).navigate(R.id.mainScreen);
            }
        });


    }
}