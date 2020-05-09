package com.wiates.mccadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity {

    private  static final int PICK_IMAGE=1;
    EditText sname,squantity,sprice,sdescription;
    Button submit,upload,choose;
    TextView alert;
    ArrayList<Uri> ImageList =new ArrayList<Uri>();
    private Uri ImageUri;
    private ProgressDialog progressDialog;
    private int upload_count=0;


    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("item");

        alert = findViewById(R.id.alert);
        upload = findViewById(R.id.upload);
        choose= findViewById(R.id.choose);

        progressDialog = new ProgressDialog
                (this);
        progressDialog.setMessage("Image uploading")    ;

        sname = findViewById(R.id.name);
        squantity = findViewById(R.id.quantity);
        sprice = findViewById(R.id.price);
        sdescription = findViewById(R.id.description);
        submit = findViewById(R.id.submit);


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent((Intent.ACTION_GET_CONTENT));
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,
                        true);
                startActivityForResult(intent,PICK_IMAGE);
            }});
upload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        progressDialog.show();
       alert.setText("Please Press the button again!");


        StorageReference ImageFolder= FirebaseStorage.getInstance().getReference().child("Image Folder");

for(upload_count=0;upload_count<ImageList.size();upload_count++)
{
    Uri IndividualImage= ImageList.get(upload_count);
    final StorageReference ImageName=ImageFolder.child("Image" + IndividualImage.getLastPathSegment());
    ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String url=String.valueOf(uri);
                    StoreLink(url);
                }
            });
        }
    });
}


    }
});






        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    int countClipData = data.getClipData().getItemCount();

                    int currentImageSelect = 0;
                    while (currentImageSelect < countClipData) {


                        ImageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                        ImageList.add(ImageUri);
                        currentImageSelect = currentImageSelect + 1;
                    }
                        alert.setVisibility(View.VISIBLE);
                    alert.setText("SELECTED" + ImageList.size() +"Images");
                    choose.setVisibility(View.GONE);


                    }else{
                        Toast.makeText(this, "Please select images", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }
        private void StoreLink(String url)
        {
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("UserOne");
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("ImgLink",url);
            databaseReference.push().setValue(hashMap);
            progressDialog.dismiss();
            alert.setText("Image uploaded successfully!");
            upload.setVisibility(View.GONE);


        }

        private void addItem () {

            String name = sname.getText().toString();
            String quantity = squantity.getText().toString();
            String price = sprice.getText().toString();
            String description = sdescription.getText().toString();

            if (!TextUtils.isEmpty(name)) {

                String id = mDatabaseReference.push().getKey();
                AddItem item = new AddItem(id, name, quantity, price, description);
                mDatabaseReference.child(id).setValue(item);
                Toast.makeText(this, "Details added successfully!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Enter the details", Toast.LENGTH_SHORT).show();

            }

        }

    }