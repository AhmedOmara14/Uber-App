package com.example.uberapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uberapp.R;
import com.example.uberapp.data.Repositry;
import com.example.uberapp.databinding.ActivitySettingCustomerBinding;
import com.example.uberapp.databinding.ActivitySettingsBinding;
import com.example.uberapp.pojo.info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;

public class setting_customer extends AppCompatActivity {
    ActivitySettingCustomerBinding binding;
    Repositry repositry;
    private static final int bic=1;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    StorageReference storage2= FirebaseStorage.getInstance().getReference().child("image");;
    private String currentuserid=auth.getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_customer);
        binding.customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, bic);
            }
        });
        repositry = ViewModelProviders.of(this).get(Repositry.class);
        retreiveinfo();
        binding.updateBtnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateinfoofdriver();
            }
        });
    }

    private void updateinfoofdriver() {
        String driver_name = binding.nameCusTo.getText().toString();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", driver_name);
        reference.child("Customers").child(auth.getCurrentUser().getUid()).
                updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(setting_customer.this, "Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(setting_customer.this, cus_map.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == bic && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    StorageReference storagReference = storage2.child(currentuserid + ".jpg");
                    storagReference.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    reference.child("Customers").child(currentuserid).
                                            child("image").setValue(uri.toString()).addOnCompleteListener
                                            (new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(setting_customer.this,
                                                                "Successfull !!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void retreiveinfo() {
        Toast.makeText(this, "ok ", Toast.LENGTH_SHORT).show();
        repositry.getData_customer(auth.getCurrentUser().getUid()).observe(this, new Observer<List<info>>() {
            @Override
            public void onChanged(List<info> infos) {
                binding.nameCusTo.setText(infos.get(0).getName());
                Picasso.get().load(infos.get(0).getImage()).placeholder(R.drawable.profile_image)
                        .error(R.drawable.profile_image).into(binding.customer);

            }
        });

    }
}
