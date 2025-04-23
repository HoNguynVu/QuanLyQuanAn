package com.example.doan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminAddMenuItem extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 101;
    private static final String IMGUR_CLIENT_ID = "8fefa7405406b7b";

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private EditText edtName, edtPrice, edtCategory;
    private ImageView imgPreview;
    private Button btnChooseImage, btnSubmit;
    private ProgressBar progressBar;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_menu_item);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtCategory = findViewById(R.id.edtCategory);
        imgPreview = findViewById(R.id.imgPreview);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnChooseImage.setOnClickListener(v -> checkPermissionAndChooseImage());
        btnSubmit.setOnClickListener(v -> uploadMenuItem());

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData(); //Lấy ra đường dẫn Uri của ảnh được chọn
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            imgPreview.setImageBitmap(bitmap); //Load ảnh vào ImageView imgPreview để người dùng xem trước
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void checkPermissionAndChooseImage() //Quyền đọc ảnh
    {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE; //Android 13+ : Android 12-

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION_CODE);
        } else {
            chooseImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) //xin quyền đọc ảnh, giống contact provider
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseImage();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void chooseImage() //Mở thư viện ảnh
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Chọn ảnh"));
    }

    private void uploadMenuItem() //check và upload
    {
        String name = edtName.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();

        if (name.isEmpty() || category.isEmpty() || priceStr.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        //gọi  upload ảnh lên imur
        uploadImageToImgur(imageUri, imageUrl -> {
            MenuItem item = new MenuItem(name, price, category, imageUrl, true);
            DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("menu"); //thêm menu vào firebase
            String key = menuRef.push().getKey();
            if (key != null) {
                menuRef.child(key).setValue(item).addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Đã thêm món", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void uploadImageToImgur(Uri uri, OnSuccessListener<String> onSuccess) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT); //chuyển ảnh thành base64

            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("image", base64Image)
                    .build();

            Request request = new Request.Builder()     //gửi ảnh qua POST tới Imur qua link client
                    .url("https://api.imgur.com/3/image")
                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Upload ảnh thất bại", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            String imageUrl = json.getJSONObject("data").getString("link"); //lấy url ảnh từ json

                            runOnUiThread(() -> onSuccess.onSuccess(imageUrl));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Upload ảnh lỗi", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
