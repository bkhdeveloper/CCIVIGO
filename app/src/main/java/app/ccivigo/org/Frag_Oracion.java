package app.ccivigo.org;

import android.app.Fragment;
import android.app.PendingIntent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Frag_Oracion extends Fragment implements OnClickListener{

    static int minutes_to_show_rest = 90;
    private Handler handlerEveryMinute;
    Button leftButton,rightButton;
    private Calendar currentSelectedDate;
    private Date currentSelectedTime;
    private PendingIntent pendingIntent;
    public Frag_Oracion() {
        // Constructor vacío obligatorio
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_oracion, container, false);

        leftButton = (Button) rootView.findViewById(R.id.id_fecha_antes);
        leftButton.setOnClickListener(this);
        rightButton = (Button) rootView.findViewById(R.id.id_fecha_desp);
        rightButton.setOnClickListener(this);

        currentSelectedDate=Calendar.getInstance();
        populateView(rootView);

        handlerEveryMinute = new Handler();
        handlerEveryMinute.postDelayed(runnable, 1000);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.id_fecha_antes:
                currentSelectedDate.add(Calendar.DATE,-1);
                handlerEveryMinute.removeCallbacks(runnable);
                populateView(getView());
                if(currentSelectedDate.getTime().before(Calendar.getInstance().getTime())){
                    leftButton.setVisibility(View.INVISIBLE);
                    currentSelectedDate=Calendar.getInstance();
                    handlerEveryMinute.postDelayed(runnable, 1);
                }
                break;
            case R.id.id_fecha_desp:
                handlerEveryMinute.removeCallbacks(runnable);
                currentSelectedDate.add(Calendar.DATE,1);
                leftButton.setVisibility(View.VISIBLE);
                populateView(getView());
                break;
        }
        //populateView(getView());
    }

    @Override
    public void onDestroyView() {
        stopHandlerEveryMinute();
        super.onDestroyView();
    }

    public String capitalizeFirstLetter(String original){
        if(original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public void stopHandlerEveryMinute() {
        super.onStop();
        handlerEveryMinute.removeCallbacks(runnable);
    }

    public void populateView(View v){
        String ciudad = "Vigo";
        String FILE_NAME;
        String FAJR = "fajr";
        String CHOROK = "sunrise";
        String DHOHR = "zuhr";
        String ASR = "asr";
        String MAGHREB = "maghrib";
        String ISHA = "isha";
        String FECHA = "date";
        String PRAYER_DAY = "d";
        String dhohr_to_jomoaa_label = getResources().getString(R.string.lab_dhohr);
        String _oracionNotifLabel="Hora de Oracion";

        TextView _Fajr,_Sun, _Dhohr, _Asr, _Maghreb, _Isha, _Fecha,
                _FajrLabel, _DhohrLabel, _AsrLabel, _MaghrebLabel, _IshaLabel,_SunLabel,_ciudadLabel,
                _FajrIqama,_DhohrIqama,_AsrIqama,_MaghrebIqama,_IshaIqama;

        TableLayout _oracionPageBackground;
        TableRow  _RowBackFajr,_RowBackDhohr , _RowBackAsr ,_RowBackMaghreb ,_RowBackIsha;

        _ciudadLabel =(TextView) v.findViewById(R.id.id_ciudad);

        _Fajr =(TextView) v.findViewById(R.id.txt_fajr);
        _Sun =(TextView) v.findViewById(R.id.txt_sun);
        _Dhohr =(TextView) v.findViewById(R.id.txt_dhohr);
        _Asr =(TextView) v.findViewById(R.id.txt_asr);
        _Maghreb =(TextView) v.findViewById(R.id.txt_maghreb);
        _Isha =(TextView) v.findViewById(R.id.txt_isha);
        _Fecha =(TextView) v.findViewById(R.id.txt_fecha);

        _FajrIqama =(TextView) v.findViewById(R.id.txt_fajr_ikama);
        _DhohrIqama =(TextView) v.findViewById(R.id.txt_dhohr_ikama);
        _AsrIqama =(TextView) v.findViewById(R.id.txt_asr_ikama);
        _MaghrebIqama =(TextView) v.findViewById(R.id.txt_maghreb_ikama);
        _IshaIqama =(TextView) v.findViewById(R.id.txt_isha_ikama);

        _FajrLabel =(TextView) v.findViewById(R.id.lbl_fajr);
        _SunLabel =(TextView) v.findViewById(R.id.lbl_sun);
        _DhohrLabel =(TextView) v.findViewById(R.id.lbl_dhohr);
        _AsrLabel =(TextView) v.findViewById(R.id.lbl_asr);
        _MaghrebLabel =(TextView) v.findViewById(R.id.lbl_maghreb);
        _IshaLabel =(TextView) v.findViewById(R.id.lbl_isha);

        _oracionPageBackground =(TableLayout) v.findViewById(R.id.id_paper);

        _RowBackFajr =(TableRow) v.findViewById(R.id.id_backrowfajr);
        _RowBackDhohr =(TableRow) v.findViewById(R.id.id_backrowdhohr);
        _RowBackAsr =(TableRow) v.findViewById(R.id.id_backrowasr);
        _RowBackMaghreb =(TableRow) v.findViewById(R.id.id_backrowmaghreb);
        _RowBackIsha =(TableRow) v.findViewById(R.id.id_backrowisha);

        //INIT views

        _oracionPageBackground.setBackground(getResources().getDrawable(R.drawable.img_paper));

        if(Locale.getDefault().getLanguage().equals("ar")){
            _FajrLabel.setGravity(Gravity.LEFT);
            _SunLabel.setGravity(Gravity.LEFT);
            _DhohrLabel.setGravity(Gravity.LEFT);
            _AsrLabel.setGravity(Gravity.LEFT);
            _MaghrebLabel.setGravity(Gravity.LEFT);
            _IshaLabel.setGravity(Gravity.LEFT);
        }

        _FajrLabel.setText(R.string.lab_fajr);
        _DhohrLabel.setText(R.string.lab_dhohr);
        _AsrLabel.setText(R.string.lab_asr);
        _MaghrebLabel.setText(R.string.lab_maghreb);
        _IshaLabel.setText(R.string.lab_isha);

        _Fajr.setTypeface(_Fajr.getTypeface(), Typeface.NORMAL);
        _Sun.setTypeface(_Sun.getTypeface(), Typeface.NORMAL);
        _Dhohr.setTypeface(_Dhohr.getTypeface(), Typeface.NORMAL);
        _Asr.setTypeface(_Asr.getTypeface(), Typeface.NORMAL);
        _Maghreb.setTypeface(_Maghreb.getTypeface(), Typeface.NORMAL);
        _Isha.setTypeface(_Isha.getTypeface(), Typeface.NORMAL);
        _FajrLabel.setTypeface(_Isha.getTypeface(), Typeface.NORMAL);
        _DhohrLabel.setTypeface(_Isha.getTypeface(), Typeface.NORMAL) ;
        _AsrLabel.setTypeface(_Isha.getTypeface(), Typeface.NORMAL);
        _MaghrebLabel.setTypeface(_Isha.getTypeface(), Typeface.NORMAL);
        _IshaLabel.setTypeface(_Isha.getTypeface(), Typeface.NORMAL);

        _Fajr.setTextColor(Color.BLACK) ;
        _Sun.setTextColor(Color.BLACK) ;
        _Dhohr.setTextColor(Color.BLACK) ;
        _Asr.setTextColor(Color.BLACK) ;
        _Maghreb.setTextColor(Color.BLACK) ;
        _Isha.setTextColor(Color.BLACK) ;
        _FajrLabel.setTextColor(Color.BLACK) ;
        _DhohrLabel.setTextColor(Color.BLACK) ;
        _AsrLabel.setTextColor(Color.BLACK) ;
        _MaghrebLabel.setTextColor(Color.BLACK) ;
        _IshaLabel.setTextColor(Color.BLACK) ;

        _RowBackFajr.setBackgroundResource(0);
        _RowBackDhohr.setBackgroundResource(0);
        _RowBackAsr.setBackgroundResource(0);
        _RowBackMaghreb.setBackgroundResource(0);
        _RowBackIsha.setBackgroundResource(0);

        try {
            //URL url = new URL(URL);
            //Init parameters:
            FILE_NAME = "prayer/"+ ciudad + "/" + (currentSelectedDate.get(Calendar.MONTH)+1) +"-"+currentSelectedDate.get(Calendar.YEAR)+".xml";
            PRAYER_DAY = "d-"+currentSelectedDate.get(Calendar.DAY_OF_MONTH);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Document doc = db.parse(getActivity().getApplicationContext().getAssets().open(FILE_NAME));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName(PRAYER_DAY);

            // prayer index 0
            Node node = nodeList.item(0);
            Element fstElmnt = (Element) node;

            NodeList fajrList = fstElmnt.getElementsByTagName(FAJR);
            NodeList sunList = fstElmnt.getElementsByTagName(CHOROK);
            NodeList dhohrList = fstElmnt.getElementsByTagName(DHOHR);
            NodeList asrList = fstElmnt.getElementsByTagName(ASR);
            NodeList maghrebList = fstElmnt.getElementsByTagName(MAGHREB);
            NodeList ishaList = fstElmnt.getElementsByTagName(ISHA);

            Element fajrElement = (Element) fajrList.item(0);
            Element sunElement = (Element) sunList.item(0);
            Element dhohrElement = (Element) dhohrList.item(0);
            Element asrElement = (Element) asrList.item(0);
            Element maghrebElement = (Element) maghrebList.item(0);
            Element ishaElement = (Element) ishaList.item(0);

            fajrList = fajrElement.getChildNodes();
            sunList = sunElement.getChildNodes();
            dhohrList = dhohrElement.getChildNodes();
            asrList = asrElement.getChildNodes();
            maghrebList = maghrebElement.getChildNodes();
            ishaList = ishaElement.getChildNodes();

            FAJR = ((Node) fajrList.item(0)).getNodeValue();
            CHOROK = ((Node) sunList.item(0)).getNodeValue();
            DHOHR = ((Node) dhohrList.item(0)).getNodeValue();
            ASR = ((Node) asrList.item(0)).getNodeValue();
            MAGHREB = ((Node) maghrebList.item(0)).getNodeValue();
            ISHA = ((Node) ishaList.item(0)).getNodeValue();

            if(Locale.getDefault().getLanguage().equals("ar")){
                SimpleDateFormat arabicFechaFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy",new Locale("ar"));
                FECHA = arabicFechaFormat.format(currentSelectedDate.getTime()) + "\n" + new DateHigri(getActivity().getApplicationContext()).writeIslamicDate(currentSelectedDate);
            }else{
                SimpleDateFormat spanishFechaFormat = new SimpleDateFormat("dd MMMM yyyy",new Locale("es"));
                FECHA = capitalizeFirstLetter(new SimpleDateFormat("EEEE",new Locale("es")).format(currentSelectedDate.getTime()))
                        +", "+ spanishFechaFormat.format(currentSelectedDate.getTime()) + "\n" + new DateHigri(getActivity().getApplicationContext()).writeIslamicDate(currentSelectedDate);
            }

            //JOMOAA DAY PARAMETERS
            if (currentSelectedDate.get(Calendar.DAY_OF_WEEK)== 6){
                DHOHR=getResources().getString(R.string.lab_hora_jomaa);
                dhohr_to_jomoaa_label = getResources().getString(R.string.lab_jomaa);
                _oracionPageBackground.setBackground(getResources().getDrawable(R.drawable.img_paper_jomaa));
            }

            _ciudadLabel.setText(ciudad);
            _Fajr.setText(FAJR);
            _Sun.setText(CHOROK);
            _Dhohr.setText(DHOHR);
            _Asr.setText(ASR);
            _Maghreb.setText(MAGHREB);
            _Isha.setText(ISHA);
            _Fecha.setText(FECHA);

            Calendar cal = Calendar.getInstance(new Locale("es"));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            cal.setTime(sdf.parse(FAJR)); cal.add(Calendar.MINUTE, 20);_FajrIqama.setText("("+sdf.format(cal.getTime())+")");
            cal.setTime(sdf.parse(DHOHR)); cal.add(Calendar.MINUTE,15);
            if (currentSelectedDate.get(Calendar.DAY_OF_WEEK)== 6) {
                _DhohrIqama.setText("(" + getResources().getString(R.string.lab_hora_jomaa) + ")");
            }else{
                _DhohrIqama.setText("(" + sdf.format(cal.getTime()) + ")");
            }
            cal.setTime(sdf.parse(ASR)); cal.add(Calendar.MINUTE,15);_AsrIqama.setText("("+sdf.format(cal.getTime())+")");
            cal.setTime(sdf.parse(MAGHREB)); cal.add(Calendar.MINUTE,10);_MaghrebIqama.setText("("+sdf.format(cal.getTime())+")");
            cal.setTime(sdf.parse(ISHA)); cal.add(Calendar.MINUTE,15);_IshaIqama.setText("("+sdf.format(cal.getTime())+")");

            if (currentSelectedDate.getTime().compareTo(Calendar.getInstance().getTime())<0){
                //Show next time pray
                String[] list_times = {FAJR, DHOHR, ASR, MAGHREB, ISHA};
                //Search minimum positif difference and reutrn index
                long next_time_prayer_minutes=-1440,a,next_time_prayer_milliseconds=-86400000,past_time_prayer_milliseconds=0;
                int hora_index=10,past_hora_index=10,past_minutes_limit=20;
                currentSelectedTime= new SimpleDateFormat("hh:mm:ss").parse(currentSelectedDate.get(Calendar.HOUR_OF_DAY)+":"+currentSelectedDate.get(Calendar.MINUTE)+":"+currentSelectedDate.get(Calendar.SECOND));
                for (int i = 0; i < list_times.length; i++){
                    //a = TimeUnit.MILLISECONDS.toMinutes(t1.getTime() - t2.getTime());
                    a=currentSelectedTime.getTime() - (new SimpleDateFormat("hh:mm:ss").parse(list_times[i]+":00")).getTime();
                    if (a < 0 && next_time_prayer_milliseconds < a) {
                        //next_time_prayer_minutes = a;
                        next_time_prayer_milliseconds = a;
                        hora_index = i;
                    }
                    if (a > 0 && TimeUnit.MILLISECONDS.toMinutes(a) < past_minutes_limit) {
                        past_hora_index = i;
                        past_time_prayer_milliseconds=a;
                    }
                }
                //////////////////////////////////////////////////////////EKHDEM BEL LES MILLISECONDES

                if (hora_index==0) {
                    if (Math.abs(next_time_prayer_milliseconds) < TimeUnit.MINUTES.toMillis(minutes_to_show_rest)) {
                        _FajrLabel.setText(getResources().getString(R.string.lab_fajr)+" (" + TimeUnit.MILLISECONDS.toMinutes(next_time_prayer_milliseconds-60000) + ")");}
                    _Fajr.setTextColor(Color.parseColor("#D90D54"));
                    _FajrLabel.setTextColor(Color.parseColor("#D90D54"));
                    _RowBackFajr.setBackground(getResources().getDrawable(R.drawable.img_back_rowtable));
                    _Fajr.setTypeface(_Fajr.getTypeface(), Typeface.BOLD);
                    _FajrLabel.setTypeface(_FajrLabel.getTypeface(), Typeface.BOLD);
                }
                if (hora_index==1) {
                    _DhohrLabel.setText(dhohr_to_jomoaa_label);
                    _RowBackDhohr.setBackground(getResources().getDrawable(R.drawable.img_back_rowtable));
                    if (Math.abs(next_time_prayer_milliseconds) < TimeUnit.MINUTES.toMillis(minutes_to_show_rest)) {
                        _DhohrLabel.setText(_DhohrLabel.getText()+" (" + TimeUnit.MILLISECONDS.toMinutes(next_time_prayer_milliseconds-60000) + ")");}
                    _Dhohr.setTextColor(Color.parseColor("#D90D54"));
                    _DhohrLabel.setTextColor(Color.parseColor("#D90D54"));
                    _Dhohr.setTypeface(_Dhohr.getTypeface(), Typeface.BOLD);
                    _DhohrLabel.setTypeface(_DhohrLabel.getTypeface(), Typeface.BOLD);
                }
                if (hora_index==2) {
                    if (hora_index==2) {if (Math.abs(next_time_prayer_milliseconds) < TimeUnit.MINUTES.toMillis(minutes_to_show_rest)) {
                        _AsrLabel.setText(getResources().getString(R.string.lab_asr)+" (" + TimeUnit.MILLISECONDS.toMinutes(next_time_prayer_milliseconds-60000) + ")");}}
                    _RowBackAsr.setBackground(getResources().getDrawable(R.drawable.img_back_rowtable));
                    _Asr.setTextColor(Color.parseColor("#D90D54"));
                    _AsrLabel.setTextColor(Color.parseColor("#D90D54"));
                    _Asr.setTypeface(_Asr.getTypeface(), Typeface.BOLD);
                    _AsrLabel.setTypeface(_AsrLabel.getTypeface(), Typeface.BOLD);
                }

                if (hora_index==3) {
                    if (Math.abs(next_time_prayer_milliseconds) < TimeUnit.MINUTES.toMillis(minutes_to_show_rest)) {
                        _MaghrebLabel.setText(getResources().getString(R.string.lab_maghreb)+" (" + TimeUnit.MILLISECONDS.toMinutes(next_time_prayer_milliseconds-60000) + ")");}
                    _Maghreb.setTextColor(Color.parseColor("#D90D54"));
                    _MaghrebLabel.setTextColor(Color.parseColor("#D90D54"));
                    _RowBackMaghreb.setBackground(getResources().getDrawable(R.drawable.img_back_rowtable));
                    _Maghreb.setTypeface(_Maghreb.getTypeface(), Typeface.BOLD);
                    _MaghrebLabel.setTypeface(_MaghrebLabel.getTypeface(), Typeface.BOLD);
                }
                if (hora_index==4) {
                    if (Math.abs(next_time_prayer_milliseconds) < TimeUnit.MINUTES.toMillis(minutes_to_show_rest)) {
                        _IshaLabel.setText(getResources().getString(R.string.lab_isha)+" (" + TimeUnit.MILLISECONDS.toMinutes(next_time_prayer_milliseconds-60000) + ")");
                    }
                    _Isha.setTextColor(Color.parseColor("#D90D54"));
                    _IshaLabel.setTextColor(Color.parseColor("#D90D54"));
                    _RowBackIsha.setBackground(getResources().getDrawable(R.drawable.img_back_rowtable));
                    _Isha.setTypeface(_Isha.getTypeface(), Typeface.BOLD);
                    _IshaLabel.setTypeface(_IshaLabel.getTypeface(), Typeface.BOLD);
                }
                if (hora_index==10) {
                    // If no one of them
                }
                if (past_hora_index==0) {
                    _FajrLabel.setText(getResources().getString(R.string.lab_fajr)+" (+" + TimeUnit.MILLISECONDS.toMinutes(past_time_prayer_milliseconds+60000) + ")");
                    _Fajr.setTextColor(Color.parseColor("#3F9100"));
                    _FajrLabel.setTextColor(Color.parseColor("#3F9100"));
                    _Fajr.setTypeface(_Fajr.getTypeface(), Typeface.BOLD);
                    _FajrLabel.setTypeface(_FajrLabel.getTypeface(), Typeface.BOLD);
                }
                if (past_hora_index==1) {
                    _DhohrLabel.setText(dhohr_to_jomoaa_label+" (+" + TimeUnit.MILLISECONDS.toMinutes(past_time_prayer_milliseconds+60000) + ")");
                    _Dhohr.setTextColor(Color.parseColor("#3F9100"));
                    _DhohrLabel.setTextColor(Color.parseColor("#3F9100"));
                    _Dhohr.setTypeface(_Dhohr.getTypeface(), Typeface.BOLD);
                    _DhohrLabel.setTypeface(_DhohrLabel.getTypeface(), Typeface.BOLD);
                }
                if (past_hora_index==2) {
                    _AsrLabel.setText(getResources().getString(R.string.lab_asr)+" (+" + TimeUnit.MILLISECONDS.toMinutes(past_time_prayer_milliseconds+60000) + ")");
                    _Asr.setTextColor(Color.parseColor("#3F9100"));
                    _AsrLabel.setTextColor(Color.parseColor("#3F9100"));
                    _Asr.setTypeface(_Asr.getTypeface(), Typeface.BOLD);
                    _AsrLabel.setTypeface(_AsrLabel.getTypeface(), Typeface.BOLD);
                }
                if (past_hora_index==3) {
                    _MaghrebLabel.setText(getResources().getString(R.string.lab_maghreb)+" (+" + TimeUnit.MILLISECONDS.toMinutes(past_time_prayer_milliseconds+60000) + ")");
                    _Maghreb.setTextColor(Color.parseColor("#3F9100"));
                    _MaghrebLabel.setTextColor(Color.parseColor("#3F9100"));
                    _Maghreb.setTypeface(_Maghreb.getTypeface(), Typeface.BOLD);
                    _MaghrebLabel.setTypeface(_MaghrebLabel.getTypeface(), Typeface.BOLD);
                }
                if (past_hora_index==4) {
                    _oracionNotifLabel = getResources().getString(R.string.lab_isha);
                    _IshaLabel.setText(getResources().getString(R.string.lab_isha)+" (+" + TimeUnit.MILLISECONDS.toMinutes(past_time_prayer_milliseconds+60000) + ")");
                    _Isha.setTextColor(Color.parseColor("#3F9100"));
                    _IshaLabel.setTextColor(Color.parseColor("#3F9100"));
                    _Isha.setTypeface(_Isha.getTypeface(), Typeface.BOLD);
                    _IshaLabel.setTypeface(_IshaLabel.getTypeface(), Typeface.BOLD);
                }
                // Schedule the alarm!
                /*Calendar calcal = Calendar.getInstance();
                calcal.set(Calendar.MILLISECOND, (int) Math.abs(next_time_prayer_milliseconds));
                Intent intent = new Intent("REFRESH_THIS");
                Log.d("XXXXXXXXXXXXXXXXXXXXXXXXXX", Long.toString(calcal.getTimeInMillis()));
                pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, calcal.getTimeInMillis(),calcal.getTimeInMillis(), pendingIntent);

                OR
                String alarm = Context.ALARM_SERVICE;
                AlarmManager am = (AlarmManager) getActivity().getSystemService(alarm);

                Intent intent = new Intent( "REFRESH_THIS" );
                intent.putExtra("alarm_message", "O'Doyle Rules!");
                PendingIntent pi = PendingIntent.getBroadcast(getActivity(),0,intent,0);

                int type = AlarmManager.ELAPSED_REALTIME_WAKEUP;
                //Log.d("XXXXXXXXXXXXXXXXXXXXXXXXXX", Long.toString(next_time_prayer_milliseconds));
                //am.set(type, SystemClock.elapsedRealtime() + Math.abs(next_time_prayer_milliseconds), pi);

                long triggerTime = SystemClock.elapsedRealtime() + Math.abs(next_time_prayer_milliseconds);
                long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                am.setRepeating( type, triggerTime, triggerTime , pi );*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentSelectedDate=Calendar.getInstance();
            populateView(getView());
            handlerEveryMinute.postDelayed(this, 1000);
        }
    };
}