package com.matt2393.chivato.Vista;

import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.chrisbanes.photoview.PhotoView;
import com.matt2393.chivato.GlideApp;
import com.matt2393.chivato.Modelo.Imagen;
import com.matt2393.chivato.R;
import com.matt2393.chivato.Tools.Const;
import com.matt2393.chivato.Tools.FirebaseInit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ImageActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        ArrayList<Imagen> imagens=new ArrayList<>();
        pos=getIntent().getIntExtra(Const.POS_IMG,0);

        imagens=getIntent().getParcelableArrayListExtra(Const.IMAGES);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),imagens);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(pos);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);




    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String IMAGEN = "imagen";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(Imagen img) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putParcelable(IMAGEN,img);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_image, container, false);
            PhotoView image = rootView.findViewById(R.id.image_viewpager);

            if(getArguments()!=null) {
                Imagen img = getArguments().getParcelable(IMAGEN);
                if(img!=null) {
                    GlideApp.with(getActivity())
                            .load(FirebaseInit.getStorageRef(img.getUrl()))
                            .into(image);

                    image.setOnLongClickListener(v -> {
                        Log.e("mmm", "long_click");
                        descargarImagen(img.getUrl(), img.getExt());
                        return false;
                    });
                }
            }


            return rootView;
        }
        private void descargarImagen( String url, String ext){

            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "IND" + File.separator+File.separator+"Imagenes");
            file.mkdirs();
            final File localFile = new File(file,"IND_IMG_"+new Date().getTime()+"."+ ext);

            FirebaseInit.getStorageRef(url)
                    .getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.e("aaa",localFile.getAbsolutePath());
                        Toast.makeText(getActivity(),"Se descargo",Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(),"Ocurrio un error en la descarga",Toast.LENGTH_SHORT).show();
                    });

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public ArrayList<Imagen> imagens;
        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Imagen> imgs) {
            super(fm);
            imagens=imgs;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(imagens.get(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return imagens.size();
        }
    }
}
