package com.matt2393.chivato;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessaging;
import com.matt2393.chivato.Modelo.Imagen;
import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.TipoNoticia;
import com.matt2393.chivato.Modelo.Usuario;
import com.matt2393.chivato.Presentador.Interactor.MenuInteractor;
import com.matt2393.chivato.Presentador.Interactor.SliderInteractor;
import com.matt2393.chivato.Presentador.Interactor.UserInitInteractor;
import com.matt2393.chivato.Presentador.Presenter.MenuPresenter;
import com.matt2393.chivato.Presentador.Presenter.SliderPresenter;
import com.matt2393.chivato.Presentador.Presenter.UserInitPresenter;
import com.matt2393.chivato.Tools.Const;
import com.matt2393.chivato.Tools.FirebaseInit;
import com.matt2393.chivato.Tools.Tools;
import com.matt2393.chivato.Vista.Dialog.DialogMiembro;
import com.matt2393.chivato.Vista.Fragment.FragmentHistoria;
import com.matt2393.chivato.Vista.Fragment.FragmentMiembros;
import com.matt2393.chivato.Vista.Fragment.FragmentNoticia;
import com.matt2393.chivato.Vista.ImageActivity;
import com.matt2393.chivato.Vista.LoginActivity;
import com.matt2393.chivato.Vista.View.MenuView;
import com.matt2393.chivato.Vista.View.SliderView;
import com.matt2393.chivato.Vista.View.UserInitView;
import com.matt2393.chivato.Vista.ViewHolder.ViewHolderMenu;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MenuView, UserInitView, MainAct,
        SliderView {


    private Fragment fr;


    private LinearLayout conten_menu_aux;
    private CardView card_menu_aux;
    private TextView menuAux1, menuAux2, menuAux3;
    private CoordinatorLayout contenedor_layout_main;

    private UserInitPresenter userInitPresenter;
    private MenuPresenter menuPresenter;
    private ArrayList<RolUsuario> rolesUsuarios;
    private RolUsuario rolUsuario;
    private Usuario usuario;
    private boolean is_primero;
    private TipoNoticia tipoNoticiaPrimero;
    private EditText buscar;

    private ArrayList<Fragment> fragments;


    private ArrayList<TextView> menus;
    private ArrayList<TipoNoticia> tiposNoticia;
    private ArrayList<Integer> menusIndex;

    private boolean menu_aux_abierto;
    private boolean busqueda_abierta;

    private LinearLayout contenedor_busqueda;
    private CarouselView carouselView;

    private SliderPresenter sliderPresenter;

    private Menu menu;


    private ArrayList<String> titulos;
    private CollapsingToolbarLayout collapsing;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        collapsing=findViewById(R.id.collapsingToolbar);


        menuAux1 = findViewById(R.id.menu_bottom_1);
        menuAux2 = findViewById(R.id.menu_bottom_2);
        menuAux3 = findViewById(R.id.menu_bottom_3);
        conten_menu_aux = findViewById(R.id.contenedor_menu_aux);
        card_menu_aux = findViewById(R.id.card_menu_aux);

        contenedor_busqueda = findViewById(R.id.contenedor_busqueda);
        carouselView = findViewById(R.id.carousel_main);

        buscar = findViewById(R.id.buscar_frag);

        contenedor_layout_main = findViewById(R.id.contenedor_layout_main);

        titulos=new ArrayList<>();

        menu_aux_abierto = false;
        busqueda_abierta = false;

        if(Tools.tiposNoticias!=null && Tools.tiposNoticias.size()>0)
            tiposNoticia=Tools.tiposNoticias;
        else
            tiposNoticia=new ArrayList<>();

        sliderPresenter = new SliderPresenter(this, new SliderInteractor());
        sliderPresenter.load();

        if (!Tools.tienePermiso(this))
            Tools.pedirPermisos(this);
        fragments = new ArrayList<>();

        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (fr != null) {
                    ((Filter) fr).startFilter();
                    ((Filter) fr).filter(s.toString());
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        card_menu_aux.setOnClickListener(v -> {

            if (menu_aux_abierto) {
                card_menu_aux.setVisibility(View.GONE);
                menu_aux_abierto = false;
            }
        });

        is_primero = true;
        crearCanalNotif();

        setSupportActionBar(toolbar);

        userInitPresenter = new UserInitPresenter(this, new UserInitInteractor());
        menuPresenter = new MenuPresenter(this, new MenuInteractor());

        changeViewLogin();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.usuario_menu:
                if (FirebaseInit.getUser() == null) {
                    startActivityForResult(new Intent(this, LoginActivity.class),
                            Const.CODE_LOGIN_ACT);
                } else {
                    startActivityForResult(new Intent(this, LoginActivity.class),
                            Const.CODE_LOGOUT);
                }
                break;
            case R.id.busqueda_menu:
                if (busqueda_abierta)
                    contenedor_busqueda.setVisibility(View.GONE);
                else
                    contenedor_busqueda.setVisibility(View.VISIBLE);
                busqueda_abierta = !busqueda_abierta;
                break;
            case R.id.miembros_menu:
                fragAdd(FragmentMiembros.newInstance(FragmentMiembros.PUBLICO),
                        FragmentMiembros.TAG);
                addTitulo("Docentes");
                break;
            case R.id.historia_menu:
                fragAdd(FragmentHistoria.newInstance(FragmentHistoria.PUBLICO),
                        FragmentHistoria.TAG);
                addTitulo("Historia");
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        renovarMenuyUser();

        Log.e("MAtt", "resume");

        if (sliderPresenter != null)
            sliderPresenter.addListener();
    }


    @Override
    protected void onPause() {
        super.onPause();
        userInitPresenter.removeListenerRoles();

        if (FirebaseInit.getUser() != null)
            userInitPresenter.removeListenerUser();
        menuPresenter.removeListener();

        if (sliderPresenter != null)
            sliderPresenter.removeListener();

    }

    @Override
    public void onBackPressed() {
      /*  if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {*/
        if (menu_aux_abierto) {
            card_menu_aux.setVisibility(View.GONE);
            menu_aux_abierto = false;
        } else {
            if (fragments.size() > 1) {
                fragments.remove(fragments.size() - 1);
                if(titulos.size()>1) {
                    titulos.remove(titulos.size() - 1);
                    toolbar.setSubtitle(titulos.get(titulos.size() - 1));
                }
                fr = fragments.get(fragments.size() - 1);
                if (menusIndex.size() > 0) {
                    menusIndex.remove(menusIndex.size() - 1);
                    int iii = menusIndex.size() - 1;
                    quitarFondo(menus, iii > 2 ? 2 : iii, iii > 2 ? 3 + iii : -1);
                }
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    private void changeViewLogin() {
        if (FirebaseInit.getUser() != null) {
            //    conten_login_menu.setVisibility(View.GONE);
            //    conten_user_menu.setVisibility(View.VISIBLE);
            // logout.setVisibility(View.VISIBLE);

        } else {
            //  conten_login_menu.setVisibility(View.VISIBLE);
            //  conten_user_menu.setVisibility(View.GONE);
            //  logout.setVisibility(View.GONE);
        }
    }

    private void renovarMenuyUser() {

        menuPresenter.removeListener();


        if (FirebaseInit.getUser() != null) {
            userInitPresenter.removeListenerUser();
            userInitPresenter.loadUser(FirebaseInit.getUser().getUid());
            userInitPresenter.addListenerUser();
            Log.e("MATT", "hay token");
        } else {
            menuPresenter.loadMenuAux(null, false);
            menuPresenter.addListener();
            Log.e("MATT", "no hay token");

        }
        userInitPresenter.removeListenerRoles();
        userInitPresenter.loadRoles();
        userInitPresenter.addListenerRoles();

        changeIconUs();

    }

    /**
     * Métodos de la interface MenuView
     */

    @Override
    public ViewHolderMenu OnCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderMenu(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_menu, parent, false));
    }

    @Override
    public void populateViewHolderMenu(ViewHolderMenu menu, TipoNoticia tnot, int pos) {
        final TipoNoticia tt = tnot;
        if (pos == 0 && is_primero) {
            tipoNoticiaPrimero = tt;
            is_primero = false;
            iniciarNot();
        }
        if (tnot.getNombre() != null) {
            menu.item.setText(tnot.getNombre());
            menu.item.setOnClickListener(v -> {
                fragAdd(FragmentNoticia.newInstance(tt, rolUsuario, usuario),
                        FragmentNoticia.TAG);
            });
        }
        if (tnot.getUrlIcon() != null) {
            GlideApp.with(this)
                    .load(FirebaseInit.getStorageRef(tnot.getUrlIcon()))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            menu.item.setCompoundDrawables(resource, null, null, null);
                        }
                    });
        }
    }

    @Override
    public void errorMenu() {

    }

    @Override
    public void changeDataMenu(ArrayList<TipoNoticia> tt) {
        conten_menu_aux.removeAllViews();

        tiposNoticia=tt;
        Tools.tiposNoticias=tt;
        menus = new ArrayList<>();
        menusIndex = new ArrayList<>();

        for (TipoNoticia t:tiposNoticia) {
            FirebaseMessaging.getInstance()
                    .subscribeToTopic(t.getKey());
        }

        if (tt.size() > 0) {
            menuAux1.setVisibility(View.VISIBLE);
            menuAux1.setText(tt.get(0).getNombre());
            if (tt.get(0).getUrlIcon() != null) {
                GlideApp.with(this)
                        .load(FirebaseInit.getStorageRef(tt.get(0).getUrlIcon()))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                menuAux1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, resource, null, null);
                            }
                        });
            }
            menuAux1.setOnClickListener(v -> {
                fragAdd(FragmentNoticia.newInstance(tt.get(0), rolUsuario, usuario),
                        FragmentNoticia.TAG);
                quitarFondo(menus, 0, -1);
                menusIndex.add(0);
                if (menu_aux_abierto) {
                    card_menu_aux.setVisibility(View.GONE);
                    menu_aux_abierto = false;
                }
                addTitulo(tt.get(0).getNombre());
            });
            menus.add(menuAux1);

            if (fragments.size() == 0) {
                fr = FragmentNoticia.newInstance(tt.get(0), rolUsuario, usuario);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedor_main, fr, FragmentNoticia.TAG)
                        .commit();
                fragments.add(fr);
                menuAux1.setBackgroundResource(R.drawable.fondo_menu_select);
                menusIndex.add(0);
                addTitulo(tt.get(0).getNombre());
            }


            if (tt.size() > 1) {

                menuAux2.setVisibility(View.VISIBLE);
                menuAux2.setText(tt.get(1).getNombre());
                if (tt.get(1).getUrlIcon() != null) {
                    GlideApp.with(this)
                            .load(FirebaseInit.getStorageRef(tt.get(1).getUrlIcon()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    menuAux2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, resource, null, null);
                                }
                            });
                }
                menus.add(menuAux2);
                menuAux2.setOnClickListener(v -> {
                    fragAdd(FragmentNoticia.newInstance(tt.get(1), rolUsuario, usuario),
                            FragmentNoticia.TAG);
                    quitarFondo(menus, 1, -1);
                    menusIndex.add(1);
                    if (menu_aux_abierto) {
                        card_menu_aux.setVisibility(View.GONE);
                        menu_aux_abierto = false;
                    }
                    addTitulo(tt.get(1).getNombre());
                });
                if (tt.size() > 2) {
                    menuAux3.setVisibility(View.VISIBLE);
                    if (tt.size() == 3) {

                        menuAux3.setText(tt.get(2).getNombre());
                        if (tt.get(2).getUrlIcon() != null) {
                            GlideApp.with(this)
                                    .load(FirebaseInit.getStorageRef(tt.get(2).getUrlIcon()))
                                    .into(new SimpleTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            menuAux3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, resource, null, null);
                                        }
                                    });
                        }
                        menus.add(menuAux3);
                        menuAux3.setOnClickListener(v -> {
                            fragAdd(FragmentNoticia.newInstance(tt.get(2), rolUsuario, usuario),
                                    FragmentNoticia.TAG);
                            quitarFondo(menus, 2, -1);
                            menusIndex.add(2);
                            addTitulo(tt.get(2).getNombre());
                        });
                    } else {

                        menus.add(menuAux3);
                        menuAux3.setText("Más");
                        menuAux3.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_baseline_menu, 0, 0);

                        menuAux3.setOnClickListener(v -> {
                            if (menu_aux_abierto)
                                card_menu_aux.setVisibility(View.GONE);
                            else
                                card_menu_aux.setVisibility(View.VISIBLE);
                            menu_aux_abierto = !menu_aux_abierto;
                        });

                        for (int i = 2; i < tt.size(); i++) {
                            final int jj = i;
                            TextView t = new TextView(this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            t.setText(tt.get(i).getNombre());
                            t.setPadding(40, 40, 40, 40);
                            if (tt.get(i).getUrlIcon() != null) {
                                GlideApp.with(this)
                                        .load(FirebaseInit.getStorageRef(tt.get(i).getUrlIcon()))
                                        .into(new SimpleTarget<Drawable>() {
                                            @Override
                                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                t.setCompoundDrawablesRelativeWithIntrinsicBounds(resource, null, null, null);
                                                t.setCompoundDrawablePadding(30);
                                            }
                                        });
                            }
                            t.setLayoutParams(params);
                            t.setOnClickListener(v -> {
                                fragAdd(FragmentNoticia.newInstance(tt.get(jj), rolUsuario, usuario),
                                        FragmentNoticia.TAG);
                                card_menu_aux.setVisibility(View.GONE);
                                menu_aux_abierto = false;
                                quitarFondo(menus, 2, jj + 1);
                                menusIndex.add(jj + 1);
                                addTitulo(tt.get(jj).getNombre());
                            });
                            conten_menu_aux.addView(t);
                            menus.add(t);
                        }
                    }
                } else
                    menuAux3.setVisibility(View.GONE);
            } else {
                menuAux2.setVisibility(View.GONE);
                menuAux3.setVisibility(View.GONE);
            }
        }
    }

    private void quitarFondo(ArrayList<TextView> menus, int i, int k) {
        for (int j = 0; j < menus.size(); j++) {
            if (j != i && j != k)
                menus.get(j).setBackgroundResource(R.drawable.ripple_menu);
            else {
                if (j == i)
                    menus.get(j).setBackgroundResource(R.drawable.fondo_menu_select);
                if (k > 2)
                    menus.get(k).setBackgroundResource(R.drawable.fondo_menu_select);
            }
        }

    }

    private void fragAdd(Fragment frag, String tag) {
        fr = frag;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_frag, R.anim.exit_frag, R.anim.enter_frag, R.anim.exit_frag)
                .replace(R.id.contenedor_main, fr, tag)
                .addToBackStack(null)
                .commit();
        fragments.add(fr);
    }

    /**
     * Métodos de la interface UserInitView
     */
    @Override
    public void loadRol(ArrayList<RolUsuario> roles) {
        Tools.roles = roles;
        rolesUsuarios = roles;
        iniciarNot();
    }

    @Override
    public void loadUser(Usuario user) {
        if (user.isActivo()) {
            user.setKey(FirebaseInit.getUser().getUid());
            usuario = user;

            user.setEmail(FirebaseInit.getUser().getEmail());
            Tools.usuario = user;



            changeIconUs();
            iniciarNot();
        } else {
            FirebaseInit.getAuth().signOut();
            FirebaseInit.setUser();
            Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show();
            while (fragments.size() > 1) {
                fragments.remove(fragments.size() - 1);
                fr = fragments.get(fragments.size() - 1);
                getSupportFragmentManager().popBackStack();
            }
            fragments.remove(0);
            renovarMenuyUser();
        }

    }

    private void changeIconUs() {
        if (menu != null) {
            final MenuItem item = menu.findItem(R.id.usuario_menu);
            if (Tools.usuario != null && Tools.usuario.getUrlImage() != null) {

                GlideApp.with(this)
                        .load(FirebaseInit.getStorageRef(Tools.usuario.getUrlImage()))
                        .apply(RequestOptions.circleCropTransform())
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                item.setIcon(resource);
                            }
                        });
            } else {
                item.setIcon(R.drawable.ic_outline_person_outline);
            }
        }
    }

    @Override
    public void errorRoles(String mess) {

    }

    @Override
    public void errorUser(String mess) {

    }

    private void iniciarNot() {

        if (usuario != null && rolesUsuarios != null) {
            for (RolUsuario rr : rolesUsuarios) {
                if (rr.getKey().equals(usuario.getKeyRol())) {
                    rolUsuario = rr;
                    Tools.rolUsuario = rolUsuario;
                    break;
                }
            }

        }
        if (tipoNoticiaPrimero == null) {
            Log.e("MA", "tiponotnull");
            if (rolUsuario != null) {
                menuPresenter.removeListener();
                menuPresenter.loadMenuAux(rolUsuario.getKey(), rolUsuario.isAdmin());
                //  rec_menu.setAdapter(menuPresenter.getAdapter());
                menuPresenter.addListener();
                is_primero = true;
            }
        }
        if (tipoNoticiaPrimero != null) {
            fr = FragmentNoticia.newInstance(tipoNoticiaPrimero, rolUsuario, usuario);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_main, fr, FragmentNoticia.TAG)
                    .commit();
            fragments.add(fr);
        }
    }

    private void crearCanalNotif() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notifChannel = new NotificationChannel(
                    "IND_FCM",
                    "IND APP",
                    NotificationManager.IMPORTANCE_HIGH
            );

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes auAttr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            notifChannel.setDescription("Notificaciones de la Aplicación IND, de la carrere de Ingeniería Industrial");
            notifChannel.enableVibration(true);
            notifChannel.enableLights(true);
            notifChannel.setLightColor(Color.BLUE);
            notifChannel.setVibrationPattern(new long[]{500, 500, 500});
            notifChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notifChannel.setSound(uri, auAttr);


            NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notifManager != null)
                notifManager.createNotificationChannel(notifChannel);

        }
    }

    @Override
    public void addFrag(Fragment ff) {
        fr = ff;
        fragments.add(ff);
    }

    private void loadImage(final ArrayList<Imagen> images) {
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {

                imageView.setOnClickListener(v -> {
                    startActivity(new Intent(MainActivity.this, ImageActivity.class).putExtra(Const.IMAGES, images));
                });

                GlideApp.with(MainActivity.this)
                        .load(FirebaseInit.getStorageRef(images.get(position).getUrl()))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (carouselView.getVisibility() == View.INVISIBLE) {
                                  /*  lottieMain.pauseAnimation();
                                    lottieMain.setVisibility(View.GONE);*/
                                    carouselView.setVisibility(View.VISIBLE);
                                }

                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (carouselView.getVisibility() == View.INVISIBLE) {
                                   /* lottieMain.pauseAnimation();
                                    lottieMain.setVisibility(View.GONE);*/
                                    carouselView.setVisibility(View.VISIBLE);
                                }
                                return false;
                            }
                        })
                        .into(imageView);
            }
        });
        carouselView.setPageCount(images.size());
    }


    @Override
    public void loadSliders(ArrayList<Imagen> imagenes) {
        loadImage(imagenes);
    }

    private void addTitulo(String tit){

        toolbar.setSubtitle(tit);
        titulos.add(tit);
    }
}
