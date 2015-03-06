package app.ccivigo.org;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidquery.service.MarketService;

import java.util.ArrayList;
import java.util.Locale;

public class Main extends Activity {

    //List of Fragments ID
    public static final int id_frag_home=0,id_frag_ccivigo=1,id_frag_obj=2,id_frag_predict=3,id_frag_contact=4,id_frag_oracion=5;

    private DrawerLayout menu_drawerLayout;
    private ListView menu_drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String[] tagTitles;
    private Integer fragment_selected_position;

    private Handler handler;

    private Runnable verifyAppUpgrade = new Runnable() {
        @Override
        public void run() {
            //Code goes here
       handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        //Timer for checking ... every second
        //handler = new Handler();
        //handler.postDelayed(verifyAppUpgrade, 1000);

        // Check new version
        verifyAppUpgrade();

        //Update default font of app
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/andlso.ttf");
        retartMenu();

        //First run
        if (savedInstanceState == null) {
            startFragment(id_frag_home);
            //startFragment(id_frag_oracion);
        }
    }

    public void retartMenu(){
        /////////////////////MENU
        tagTitles = getResources().getStringArray(R.array.Fragments_List);
        menu_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menu_drawerList = (ListView) findViewById(R.id.left_drawer);
        // Setear una sombra sobre el contenido principal cuando el drawer se despliegue
        //menu_drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        //Add Header layout
        //View header = getLayoutInflater().inflate(R.layout.menu_header, null);
        //menu_drawerList.addHeaderView(header);

        //Crear elementos de la lista
        ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();
        items.add(new DrawerItem(tagTitles[id_frag_home],R.drawable.ic_inicio)); //Inicio icon
        items.add(new DrawerItem(tagTitles[id_frag_ccivigo],R.drawable.ic_launcher)); //CCIVIGO icon
        items.add(new DrawerItem(tagTitles[id_frag_obj],R.drawable.ic_obj)); //Objectivos icon
        items.add(new DrawerItem(tagTitles[id_frag_predict],R.drawable.ic_predict)); //Predicacion icon
        items.add(new DrawerItem(tagTitles[id_frag_contact],R.drawable.ic_contact)); //Contacto icon
        items.add(new DrawerItem(tagTitles[id_frag_oracion],R.drawable.ic_oracion)); //Oracion icon
        // Relacionar el adaptador y la escucha de la lista del drawer
        DrawerListAdapter dataAdapter= new DrawerListAdapter(this, items);
        menu_drawerList.setAdapter(dataAdapter);
        menu_drawerList.setOnItemClickListener(new DrawerItemClickListener());
        dataAdapter.notifyDataSetChanged();
        // Habilitar el icono de la app por si hay algún estilo que lo deshabilit
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // Crear ActionBarDrawerToggle para la apertura y cierre
        drawerToggle = new ActionBarDrawerToggle(
                this,
                menu_drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                /*Usa este método si vas a modificar la action bar
                con cada fragmento
                 */
                OnMenuChange(fragment_selected_position);
                //invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getApplicationContext().getString(R.string.lab_menu_menu));

                /*Usa este método si vas a modificar la action bar
                con cada fragmento
                 */
                //invalidateOptionsMenu();
            }
        };
        //Seteamos la escucha
        menu_drawerLayout.setDrawerListener(drawerToggle);

        //Set Home Fragment at first start app

    }

    public void OnMenuChange(int position) {
        getActionBar().setTitle(tagTitles[position]);
        //Set Icon of fragment
        switch (position) {
            case id_frag_ccivigo:
                getActionBar().setIcon(R.drawable.ic_launcher);
                break;
            case id_frag_obj:
                getActionBar().setIcon(R.drawable.ic_obj);
                break;
            case id_frag_predict:
                getActionBar().setIcon(R.drawable.ic_predict);
                break;
            case id_frag_contact:
                getActionBar().setIcon(R.drawable.ic_contact);
                break;
            case id_frag_oracion:
                getActionBar().setIcon(R.drawable.ic_oracion);
                break;
            default:
                getActionBar().setIcon(R.drawable.ic_inicio);
                break;
        }
    }

    public void onClickCcivigo(View v) {
        startFragment(id_frag_ccivigo);
        OnMenuChange(fragment_selected_position);
    }
    public void onClickObjectif(View v) {
        startFragment(id_frag_obj);
        OnMenuChange(fragment_selected_position);

    }
    public void onClickPredication(View v) {
        startFragment(id_frag_predict);
        OnMenuChange(fragment_selected_position);

    }
    public void onClickContact(View v) {
        startFragment(id_frag_contact);
        OnMenuChange(fragment_selected_position);
    }

    public void onClickOracion(View v) {
        startFragment(id_frag_oracion);
        OnMenuChange(fragment_selected_position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        Context context = getApplicationContext();
        switch (item.getItemId()) {
            case R.id.id_donate:
                AlertDialog.Builder alertDialogDonate = new AlertDialog.Builder(Main.this);
                // Setting Dialog Message
                alertDialogDonate.setTitle(context.getString(R.string.title_donar));
                alertDialogDonate.setMessage(context.getString(R.string.lab_donar));
                alertDialogDonate.setCancelable(true);
                alertDialogDonate.setPositiveButton(context.getString(R.string.btn_donar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Uri.Builder uriBuilder = new Uri.Builder();
                        uriBuilder.scheme("https").authority("www.paypal.com").path("cgi-bin/webscr");
                        uriBuilder.appendQueryParameter("cmd", "_donations");
                        uriBuilder.appendQueryParameter("business", "direccion@ccivigo.org");
                        uriBuilder.appendQueryParameter("lc", "EUR");
                        uriBuilder.appendQueryParameter("item_name", "centroculturalislamicovigo");
                        uriBuilder.appendQueryParameter("no_note", "1");
                        uriBuilder.appendQueryParameter("no_shipping", "1");
                        uriBuilder.appendQueryParameter("currency_code", "EUR");
                        Uri payPalUri = uriBuilder.build();
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, payPalUri);
                        startActivity(viewIntent);
                    }
                });
                alertDialogDonate.show();
                break;
            case R.id.id_acerca_de:
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                AlertDialog.Builder alertDialogAcercaDe = new AlertDialog.Builder(Main.this);
                alertDialogAcercaDe.setTitle(R.string.lbl_acerca_de);
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
                String versionName = pInfo.versionName;
                alertDialogAcercaDe.setMessage(context.getString(R.string.lab_acerca_de) + versionName + '\n'+ df.format("yyyy", new java.util.Date()));
                alertDialogAcercaDe.setCancelable(true);
                alertDialogAcercaDe.show();
                break;
            case R.id.id_idioma:
                updateDefaultLanguage();
                break;
            case R.id.id_compartir:
                shareApp();
                break;
            case R.id.id_exit_app:
                quit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void shareApp(){
        try
        {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name_long));
            String sAux = "\n"+getResources().getString(R.string.lbl_compartir_msg)+"\n";
            sAux = sAux + "http://play.google.com/store/apps/details?id=app.ccivigo.org\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, getResources().getString(R.string.lbl_compartir_title)));
        }
        catch(Exception e)
        { //e.toString();
        }
    }
    private void updateDefaultLanguage(){
        Configuration config = getBaseContext().getResources().getConfiguration();
        if (Locale.getDefault().getLanguage().equals("ar")){
            Locale.setDefault(new Locale("es"));
            config.locale = new Locale("es");
        }else{
            Locale.setDefault(new Locale("ar"));
            config.locale = new Locale("ar");
        }
        getApplicationContext().getResources().updateConfiguration(config, null);
        refreshAllContetnt();
    }

    public void refreshAllContetnt(){
        Intent intent= new Intent(getApplicationContext(), Main.class);
        startActivity(getIntent());
        startFragment(fragment_selected_position);
        invalidateOptionsMenu();
        retartMenu();
    }

    /* La escucha del ListView en el Drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startFragment(position);
        }
    }

    @Override
    public void onBackPressed() {
        int fragments = getFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        }
        super.onBackPressed();
    }


    private void startFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case id_frag_home:
                fragment = new Frag_Accueil();
                break;
            case id_frag_ccivigo:
                fragment = new Frag_CCIVIGO();
                break;
            case id_frag_obj:
                fragment = new Frag_Objectivos();
                break;
            case id_frag_predict:
                fragment = new Frag_Predicacion();
                break;
            case id_frag_contact:
                fragment = new Frag_Contacto();
                break;
            case id_frag_oracion:
                fragment = new Frag_Oracion();
                break;
            default:
                break;
        }
        if (fragment != null) {
            Bundle args = new Bundle();
            args.putInt("fragment_position", position);
            fragment.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //ft.replace(R.id.content_frame, fragment).commit();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            // Se actualiza el item seleccionado y el título, después de cerrar el drawer
            menu_drawerList.setItemChecked(position, true);
            menu_drawerLayout.closeDrawer(menu_drawerList);
            fragment_selected_position = position;
            getActionBar().setTitle(getApplicationContext().getString(R.string.lab_menu));
        } else {
            // error in creating fragment
            Log.e("CCIVIGO", "Main Activity - Error cuando se creo el fragment");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sincronizar el estado del drawer
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Cambiar las configuraciones del drawer si hubo modificaciones
        drawerToggle.onConfigurationChanged(newConfig);
    }
    public void quit() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
    }

    private void verifyAppUpgrade(){
        MarketService ms = new MarketService(this);
        ms.locale(Locale.getDefault().getLanguage());
        ms.level(MarketService.MINOR).checkVersion();
        //ms.force(true);
    }

    public void onClickShare(View v) {
        shareApp();
    }
    public void onClickFb(View v) {
        Intent intUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/734882893266864"));
        startActivity(intUrl);
    }
    public void onClickWeb(View v) {
        Intent intUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ccivigo.org"));
        startActivity(intUrl);
    }
    public void onClickYoutube(View v) {
        Intent intUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/user/CCIVIGO"));
        startActivity(intUrl);
    }
    public void onClickTwitter(View v) {
        Intent intUrl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/"));
        startActivity(intUrl);
    }
}