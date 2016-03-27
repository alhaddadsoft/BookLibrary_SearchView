package com.alhaddadsoft.adabiat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadBook extends Activity {
    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    int per = 0;
    TextView cur_val;
    Context context;

    ImageView imageView;
    TextView textView;

    TextView loadingcomplete ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);


        cur_val = (TextView) findViewById(R.id.cur_pg_tv);
        cur_val.setText("جاري بدء التحميل");

        loadingcomplete = (TextView)findViewById(R.id.textView7);
        loadingcomplete.setText("جاري تحميل كتاب" );

       // imageView = (ImageView)findViewById(R.id.imageView7);
        textView = (TextView)findViewById(R.id.textView8);

        pb = (ProgressBar)findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(ContextCompat.getDrawable(DownloadBook.this, R.drawable.green_progress));


        new Thread(new Runnable() {
            public void run() {

                downloadFile();

            }
        }).start();


    }
    void downloadFile(){

        Intent intent = getIntent();
        final String save_file_As = intent.getExtras().getString("save_file_As");
        final String dwnload_file_path = intent.getExtras().getString("dwnload_file_path");
        //final String imageViewD = intent.getExtras().getString("imageViewD");
        final String getTitle = intent.getExtras().getString("getTitle");


        textView.setText(getTitle);
        //int id = getResources().getIdentifier(imageViewD, "drawable", getPackageName());
       // Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
      //  if(Build.VERSION.SDK_INT >= 16) {
       //     imageView.setBackground(drawable);

      //  } else {
         //   imageView.setBackgroundDrawable(drawable);

      //  }



        try {
            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file
            File root = new File(Environment.getExternalStorageDirectory()+File.separator+"/Download/", "Adabiat");
            if (!root.exists())
            {
                root.mkdirs();
            }
            File file = new File(SDCardRoot,"/Download/Adabiat/" + save_file_As);

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                runOnUiThread(new Runnable() {
                    public void run() {
                         //float  per = (downloadedSize/totalSize) * 100;
                        cur_val.setText("Downloaded " + downloadedSize + " / " + totalSize );
                        pb.setProgress(downloadedSize);

                    }
                });
            }

            //close the output stream when complete //
            fileOutput.close();
            runOnUiThread(new Runnable() {
                public void run() {
                    cur_val.setText("انتهى التحميل");
                    Toast.makeText(DownloadBook.this, "تم إظافة  الكتاب للمكتبة تستطيع الآن فتح الكتاب ", Toast.LENGTH_SHORT).show();

                    //SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
                    //Date now = new Date();

                //    String fileName =  getTitle+".txt";//like 2016_01_12.txt


                 /*   try {
                        File root = new File(Environment.getExternalStorageDirectory()+File.separator+"/Download/Adabiat", "WakeUpKsa");
                        //File root = new File(Environment.getExternalStorageDirectory(), "Notes");
                        if (!root.exists())
                        {
                            root.mkdirs();
                        }
                        File gpxfile = new File(root, fileName);


                        FileWriter writer = new FileWriter(gpxfile,true);

                        //System.out.println('"'+s+'"');           // Prints: "s"
                    //    movies.add(new Downloaded("12 обезьян", "12 Monkeys", R.drawable.poster___monkeys));

                        writer.append("done");
                        writer.flush();
                        writer.close();
                        Toast.makeText(DownloadBook.this, "تم إظافة  الكتاب للمكتبة", Toast.LENGTH_SHORT).show();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();

                    } */

                }

            });



            runOnUiThread(new Runnable() {
                public void run() {
                    // pb.dismiss(); // if you want close it..
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            showError("Error : الرجاء التأكد من اتصالك بالإنترنت " + e);
        }
    }
    void showError(final String err){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(DownloadBook.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void openactivity(){


            // pb.dismiss(); // if you want close it..

    }
}






