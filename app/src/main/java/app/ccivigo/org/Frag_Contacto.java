package app.ccivigo.org;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.app.Fragment;
import android.util.Log;
import android.view.View.OnClickListener;
import 	android.widget.Button;

public class Frag_Contacto extends Fragment implements OnClickListener{
	private EditText _mEdName;
	private EditText _mEdObjet;
	private EditText _mEdMessage;
	boolean lIsDataValid=true;

    public Frag_Contacto() {
        // Constructor vacío obligatorio
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_contacto, container, false);

        _mEdName=(EditText) rootView.findViewById(R.id.AAccontacto_EdName);
        _mEdObjet=(EditText) rootView.findViewById(R.id.AAccontacto_EdObjet);
        _mEdMessage=(EditText) rootView.findViewById(R.id.AAccontacto_EdMessage);

        Button b = (Button) rootView.findViewById(R.id.btnEnviarMail);
        b.setOnClickListener(this);

        // Google map
        /*CustomMapFragment mapFragment = new CustomMapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.map_container, mapFragment);
        transaction.commit();*/

        return rootView;
    }

    public void onClick(View v){
        if (v.getId() == R.id.btnEnviarMail){
            if(isDataValid()){
                final EditText etAsunto = (EditText) getView().findViewById(R.id.AAccontacto_EdObjet);
                final EditText etMensaje = (EditText) getView().findViewById(R.id.AAccontacto_EdMessage);
                //String to = etDe.getText().toString();
                String subject = etAsunto.getText().toString();
                String message = etMensaje.getText().toString();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@ccivigo.org"});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);
                // need this to prompts email client only
                email.setType("message/rfc822");
                getActivity().startActivity(Intent.createChooser(email, "Seleciona un cliente de correo"));
            }
        }
    }

	private boolean isDataValid(){
        boolean lIsDataValid = true;
        if(_mEdName.getText().toString().isEmpty()){
			_mEdName.setError(getActivity().getResources().getString(R.string.lab_error_emptyName));
			lIsDataValid=false;
		}
		if(_mEdObjet.getText().toString().isEmpty()){
			_mEdObjet.setError(getActivity().getResources().getString(R.string.lab_error_emptyObjet));
			lIsDataValid=false;
		}
		if(_mEdMessage.getText().toString().isEmpty()){
			_mEdMessage.setError(getActivity().getResources().getString(R.string.lab_error_emptyMessage));
			lIsDataValid=false;
		}
		return lIsDataValid;
	}
}
