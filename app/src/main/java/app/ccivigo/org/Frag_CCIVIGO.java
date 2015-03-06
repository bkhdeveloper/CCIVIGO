package app.ccivigo.org;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.graphics.Color;

public class Frag_CCIVIGO extends Fragment {
    WebView mWebView;
    public Frag_CCIVIGO() {
        // Constructor vacío obligatorio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_ccivigo, container, false);
        return rootView;
    }
}
