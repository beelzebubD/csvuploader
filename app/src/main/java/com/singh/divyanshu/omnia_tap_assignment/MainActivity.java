package com.singh.divyanshu.omnia_tap_assignment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private  final int FILE_SELECT_CODE = 0;
    private Button select;
    public  Uri globalUri;
    public Uri uri;
    public String Url="",Uri="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        select = findViewById(R.id.getFileDirectory);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        }

    private void showFileChooser() {

        String[] mimeTypes = {"text/csv"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        Toast.makeText(getApplicationContext(),"Okay",Toast.LENGTH_SHORT).show();
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode==RESULT_OK) {

                    // Get the Uri of the selected file
                    uri = data.getData();
                    globalUri=uri;
                    Uri=String.valueOf(uri);
                    Log.d("File Uri: ",uri.toString());
                    // Get the path
                    try{
                        String path=uri.getPath();
                        Url=path;
                    }catch (Exception e){
                        Log.d("ErroronAcitvityResult",e.getMessage());
                    }
                }
                Log.d("searchAcitvityResult",Url);
                Toast.makeText(getApplicationContext(),"File : "+Url.replaceAll("/.+/","") + " Selected",Toast.LENGTH_SHORT).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
        intent.putExtra("URL",Url);
        intent.putExtra("URI",uri);
        startActivity(intent);
    }
}
