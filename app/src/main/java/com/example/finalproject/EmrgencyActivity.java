package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalproject.RDatabase.Rdb;
import com.example.finalproject.RDatabase.UserDatabase;
import com.example.finalproject.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmrgencyActivity extends AppCompatActivity {
    Rdb rdb;
    EditText name,mail,number,em1,em2,em3;
    List<UserDatabase> dummy;
    CircleImageView image_profile;

    DatabaseReference reference;
    FirebaseUser fuser;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emrgency);
        name = findViewById(R.id.ename);
        mail = findViewById(R.id.emmail);
        number = findViewById(R.id.emnum);
        em1 = findViewById(R.id.emnum1);
        em2 = findViewById(R.id.emnum2);
        em3 = findViewById(R.id.emnum3);
        image_profile = findViewById(R.id.dp);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        rdb = Room.databaseBuilder(this, Rdb.class,"MYROOM").allowMainThreadQueries().build();
       dummy = rdb.myDao().getAllData();
       if (dummy.size()!=0){
           name.setText(dummy.get(0).getName());
           mail.setText(dummy.get(0).getMail());
           number.setText(dummy.get(0).getNum());
           em1.setText(dummy.get(0).getEn1());
           em2.setText(dummy.get(0).getEn2());
           em3.setText(dummy.get(0).getEn3());
       }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                                if (user.getImageURL().equals("default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(EmrgencyActivity.this).load(user.getImageURL()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     image_profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openImage();
        }
    });

}

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", ""+mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(EmrgencyActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EmrgencyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(EmrgencyActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(EmrgencyActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }



    public void submit(View view) {
        UserDatabase d = new UserDatabase();
        String p="^[6-9][0-9]{9}$";
        String n = name.getText().toString().trim();
        String m = mail.getText().toString().trim();
        String num = number.getText().toString().trim();
        String e1 = em1.getText().toString().trim();
        String e2 = em2.getText().toString().trim();
        String e3 = em3.getText().toString().trim();
        if(n.isEmpty()||m.isEmpty()||num.isEmpty()||e1.isEmpty()||e2.isEmpty()||e3.isEmpty()){
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
        }
        else if(!num.matches(p)||!e1.matches(p)||!e2.matches(p)||!e3.matches(p)){
            Toast.makeText(this, "Enter valid numbers without country code or zero", Toast.LENGTH_SHORT).show();
        }
        else if(num.equals(e1)||num.equals(e2)||num.equals(e3)||e1.equals(e2)||e1.equals(e3)||e2.equals(e3)){
            Toast.makeText(this, "Can't add same numbers", Toast.LENGTH_SHORT).show();
        }
        else {
            d.setName(n);
            d.setMail(m);
            d.setNum(num);
            d.setEn1(e1);
            d.setEn2(e2);
            d.setEn3(e3);
            if(dummy.size()==0){
                rdb.myDao().insertData(d);
                Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                rdb.myDao().updateData(d);
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            }


            startActivity(new Intent(this, SecondActivity.class));
            finish();
        }
    }

        }


