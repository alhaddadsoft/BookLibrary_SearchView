package com.alhaddadsoft.adabiat.MahmoodDarweesh;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.alhaddadsoft.adabiat.AppRater;
import com.alhaddadsoft.adabiat.Nizar.Main2Activity;
import com.alhaddadsoft.adabiat.R;
import com.alhaddadsoft.searchadapter.SearchAdapter;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "lbpN0bUzTaPOBGwcBg3lHQsQp";
    private static final String TWITTER_SECRET = "DgRfxF14ZTD4aMeBIeWZmAa9rBzc8QndbieHhqrD712KK5FazB";

    public List<Movie> movies = new ArrayList<>();

    @InjectView(R.id.grid_view) GridView gridView;
    @InjectView(R.id.search_edit_text) EditText editText;
    Button nizar;
    Button contactus;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Light.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        AppRater.app_launched(this);

        showdialog();



       try {
            ListView   twittera   = (ListView) findViewById(R.id.listView);

            final UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName("@Darwishiat")
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                    .setTimeline(userTimeline)
                    .build();
            twittera.setAdapter(adapter);

        }catch (Throwable throwable){
            Toast.makeText(MainActivity.this, "مرحبا", Toast.LENGTH_LONG).show();
        }

        fillList(movies);
        final SearchAdapter adapter = new MyAdapter(movies, this).registerFilter(Movie.class, "enTitle")
                .setIgnoreCase(true);
        gridView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contactus = (Button)findViewById(R.id.contatus);
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DisplayName = "alhaddadsoft";
                String MobileNumber = "+966593710400";

                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                //------------------------------------------------------ Names
                if (DisplayName != null) {
                    ops.add(ContentProviderOperation.newInsert(
                            ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(
                                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                    DisplayName).build());
                }

                //------------------------------------------------------ Mobile Number
                if (MobileNumber != null) {
                    ops.add(ContentProviderOperation.
                            newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                            .build());
                }


                // Asking the Contact provider to create a new contact
                try {
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                showmakecontactdialog();
            }
        });
        nizar = (Button)findViewById(R.id.nizar);
        nizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("مكتبة نزار قباني")
                        .setMessage("عزيزي سيتم توفير المكتبة في القريب العاجل تحقق لاحقا من توفر تحديث للبرنامج في متجر جوجل")
                        .setIcon(R.drawable.nizar_logo_alert_dialog)
                        .setPositiveButton("حسنا", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .show();
                // Intent d= new Intent(MainActivity.this, Main2Activity.class);
                //startActivity(d);
            }
        });

    }

    private void fillList(List<Movie> movies) {

        movies.add(new Movie("book1", "أثر الفراشة", R.drawable.book1));
        movies.add(new Movie("book2", "كزهر اللوز أو أبعد", R.drawable.book2));
        movies.add(new Movie("book3", "لا تعتذر عما فعلت", R.drawable.book3));
        movies.add(new Movie("book4", "سرير الغريبة", R.drawable.book4));
        movies.add(new Movie("book5", "لماذا تركت الحصان وحيدا", R.drawable.book5));
        movies.add(new Movie("book6", "أحد عشر كوكبا", R.drawable.book6));
        movies.add(new Movie("book7", "أرى ما أريد", R.drawable.book7));
        movies.add(new Movie("book8", "ورد أقل", R.drawable.book8));
        movies.add(new Movie("book9", "هي أغنية، هي أغنية", R.drawable.book9));
        movies.add(new Movie("book10", "حصار لمدائح البحر", R.drawable.book10));
        movies.add(new Movie("book11", "حالة حصار", R.drawable.book11));
        movies.add(new Movie("book12", "جدارية", R.drawable.book12));
        movies.add(new Movie("book13", "مديح الظل العالي", R.drawable.book13));
        movies.add(new Movie("book14", "أعراس", R.drawable.book14));
        movies.add(new Movie("book15", "تلك صورتها وهذا انتحار العاشق", R.drawable.book15));
        movies.add(new Movie("book16", "محاولة رقم 7", R.drawable.book16));
        movies.add(new Movie("book17", "أحبك أو لا أحبك", R.drawable.book17));
        movies.add(new Movie("book18", "حبيبتي تنهض من نومها", R.drawable.book18));
        movies.add(new Movie("book19", "العصافير تموت في الجليل", R.drawable.book19));
        movies.add(new Movie("book20", "آخر الليل", R.drawable.book20));
        movies.add(new Movie("book21", "عاشق من فلسطين", R.drawable.book21));
        movies.add(new Movie("book22", "أوراق الزيتون", R.drawable.book22));
        movies.add(new Movie("book23", "ذاكرة للنسيان", R.drawable.book23));


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


       public void showdialog(){

            Toast.makeText(MainActivity.this, "جاري تحميل التغريدات ...()", Toast.LENGTH_LONG).show();

            final Dialog dialog = new Dialog(this);

    dialog.setContentView(R.layout.twitteraccount);
    dialog.setTitle("تغريدات محمود درويش");
            ListView   twitter   = (ListView) dialog.findViewById(R.id.listView);

            final UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName("@Darwishiat")
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                    .setTimeline(userTimeline)
                    .build();
            twitter.setAdapter(adapter);

            // final TextView partonetext = (TextView) dialog.findViewById(R.id.Textpartone);

    dialog.show();
}
   public void showmakecontactdialog(){

       final Dialog dialog = new Dialog(this);

       dialog.setContentView(R.layout.makecontact);
       dialog.setTitle("تواصل مع :  مطور البرنامج");
      // ImageButton adabiatIns   = (ImageButton) dialog.findViewById(R.id.imageButton);
      // ImageButton adabiatSnap   = (ImageButton) dialog.findViewById(R.id.imageButton2);
      // ImageButton hamfarTwitt   = (ImageButton) dialog.findViewById(R.id.imageButton3);
       ImageButton hamfarWhat   = (ImageButton) dialog.findViewById(R.id.imageButton4);

     /* adabiatIns.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Uri uri = Uri.parse("http://instagram.com/_u/adabiat");
               Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

               likeIng.setPackage("com.instagram.android");

               try {
                   startActivity(likeIng);
               } catch (ActivityNotFoundException e) {
                   startActivity(new Intent(Intent.ACTION_VIEW,
                           Uri.parse("http://instagram.com/adabiat")));

               }
           }

       } );

       adabiatSnap.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(MainActivity.this, "عزيزي، قم بإظافة المعرف Adabiat بشكل يدوي في سناب شات", Toast.LENGTH_LONG).show();

           }
       });

       hamfarTwitt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + "hamfarouk")));
               }catch (Exception e) {
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + "hamfarouk")));
               }

          /*     try {
                   // Check if the Twitter app is installed on the phone.
                   getPackageManager().getPackageInfo("com.twitter.android", 0);

                   Intent intent = new Intent(Intent.ACTION_VIEW);
                   intent.setClassName("com.twitter.android", "com.twitter.android.ProfileActivity");
                   // Don't forget to put the "L" at the end of the id.
                   intent.putExtra("user_id", 269883973);
                   startActivity(intent);
               } catch (PackageManager.NameNotFoundException e) {
                   // If Twitter app is not installed, start browser.
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/hamfarouk")));
               }

           }
       }); */
       hamfarWhat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               String Eyad = "+966537340281";
               openConversationWithWhatsapp(Eyad);
               Toast.makeText(MainActivity.this, "إذا واجهتك رسالة خطأ تأكد من اتصالك بالإنترنت وقم بتحديث جهات الاتصال في الواتس آب  ", Toast.LENGTH_LONG).show();

           }
       });
           // final TextView partonetext = (TextView) dialog.findViewById(R.id.Textpartone);

           dialog.show();
       }
    private void openConversationWithWhatsapp(String Eyad){
        String whatsappId = Eyad+"@s.whatsapp.net";
        Uri uri = Uri.parse("smsto:" + whatsappId);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setPackage("com.whatsapp");

        //intent.putExtra(Intent.EXTRA_TEXT, "text");
       // intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
       // intent.putExtra(Intent.EXTRA_TITLE, "title");
       // intent.putExtra(Intent.EXTRA_EMAIL, "email");
       // intent.putExtra("sms_body", "The text goes here");
       // intent.putExtra("text","asd");
       // intent.putExtra("body","body");
       // intent.putExtra("subject","subjhect");

        startActivity(intent);
    }



}

