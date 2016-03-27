package com.alhaddadsoft.adabiat.MahmoodDarweesh;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.alhaddadsoft.adabiat.DownloadBook;
import com.alhaddadsoft.adabiat.R;
import com.alhaddadsoft.searchadapter.SearchAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by veinhorn on 8.3.15.
 */
public class MyAdapter extends SearchAdapter<Movie> {
    String dwnload_file_path;
    String save_file_As;
    String imageViewD;
    String getTitle;
    String entilte;

    Drawable drawable;
    ImageView imageView;
    String dwnload_file_path_Alert;
    String save_file_As_Alert;
    String imageViewD_Alert;
    String getTitle_Alert;
    String entilte_Alert;
    class ViewHolder {
        @InjectView(R.id.serial_title) TextView title;
        @InjectView(R.id.serial_original_title) TextView enTitle;
        @InjectView(R.id.serial_poster) ImageView poster;


        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public MyAdapter(List<Movie> movies, Context context) {
        super(movies, context);
    }

    @Override public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_view_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);



        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.title.setText(filteredContainer.get(position).getTitle());
        viewHolder.enTitle.setText(filteredContainer.get(position).getEnTitle());
        viewHolder.poster.setImageDrawable(context.getResources().getDrawable(filteredContainer.get(position).getPoster()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = viewHolder.title.getText().toString();
                final String titlename = viewHolder.enTitle.getText().toString();
                final String openfileas = viewHolder.title.getText().toString() +"_mahmood"+".pdf";
                 drawable = (context.getResources().getDrawable(filteredContainer.get(position).getPoster()));


                        Toast.makeText(context, viewHolder.enTitle.getText().toString(), Toast.LENGTH_LONG).show();

                dwnload_file_path = "http://www.alhaddadapps.net/adabiat/mahmood_darwish/"+title+".pdf";
                save_file_As = title +"_mahmood"+ ".pdf";
                getTitle = titlename;
                entilte =  openfileas;
                alertdialog();

                   /*
                    //Book1
                    if (filteredContainer.get(position).getEnTitle().equals("العرب والساميون والعبرانيون وبنو إسرائيل واليهود")) {
                        Toast.makeText(context, "You Clicked " , Toast.LENGTH_LONG).show();
                        dwnload_file_path = "http://alhaddadapps.net/wakeupksa/book1.pdf";
                        save_file_As = filteredContainer.get(position).getEnTitle() + ".PDF";
                        imageViewD = "book1";
                        getTitle = filteredContainer.get(position).getTitle();
                        entilte = filteredContainer.get(position).getEnTitle();
                        alertdialog();

                    //Book2
                    }else if (filteredContainer.get(position).getEnTitle().equals("تاريخ سوريا القديم")){
                        dwnload_file_path = "http://alhaddadapps.net/wakeupksa/book2.pdf";
                        save_file_As = filteredContainer.get(position).getEnTitle() + ".PDF";
                        imageViewD = "book2";
                        getTitle = filteredContainer.get(position).getTitle();
                        entilte = filteredContainer.get(position).getEnTitle();
                        alertdialog();

                    }
                    else if (filteredContainer.get(position).getEnTitle().equals("الحسبة")){
                        dwnload_file_path = "http://alhaddadapps.net/wakeupksa/book3.pdf";
                        save_file_As = filteredContainer.get(position).getEnTitle() + ".PDF";
                        imageViewD = "book2";
                        getTitle = filteredContainer.get(position).getTitle();
                        entilte = filteredContainer.get(position).getEnTitle();
                        alertdialog();

                    }*/
            }
        });
        return convertView;
    }
    public void alertdialog(){
        new AlertDialog.Builder(context)
                .setTitle(getTitle)
                .setPositiveButton("فتح", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        openMuPdf();

                    }
                })
                .setNeutralButton("مشاركة الكتاب", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "أعجبني كتاب " + getTitle + " " + "للكاتب محمود درويش واريد مشاركة الكتاب معك، تستطيع تحميل الكتاب من برنامج مكتبة العظماء للأندرويد على" +
                                " الرابط التالي\n" +
                                "https://play.google.com/store/apps/details?id=com.alhaddadsoft.adabiat";
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "محمود درويش");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }
                })
                .setNegativeButton("تحميل", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //download path
                        // file name to show in save file (save pdf)
                        // continue with delete
                        dwnload_file_path_Alert = dwnload_file_path;
                        save_file_As_Alert = save_file_As;
                        getTitle_Alert = getTitle;
                        Downloadmethode();
                    }
                })

                .setIcon(drawable)


                .show();
    }
    public void Downloadmethode(){
        Intent d = new Intent(context, DownloadBook.class);
        d.putExtra("dwnload_file_path", dwnload_file_path_Alert);
        d.putExtra("save_file_As", save_file_As_Alert);
        d.putExtra("imageViewD", imageViewD_Alert);
        d.putExtra("getTitle", getTitle_Alert);
        d.putExtra("enTilte", entilte_Alert);
        context.startActivity(d);
    }

    public void openMuPdf(){
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Download/Adabiat/" +entilte);

        Intent intent = new Intent(context, MuPDFActivity.class);

        intent.setAction(Intent.ACTION_VIEW);

        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }
}