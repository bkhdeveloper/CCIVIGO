package app.ccivigo.org;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Frag_Accueil extends Fragment{

    private static final int[] ITEM_DRAWABLES = {R.drawable.share, R.drawable.fb, R.drawable.web, R.drawable.twitter,  R.drawable.youtube};
    private String urlstr;
    public Frag_Accueil() {
        // Constructor vacío obligatorio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.frag_accueil, container, false);
        return rootView;
    }

}