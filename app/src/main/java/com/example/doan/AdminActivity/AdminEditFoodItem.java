package com.example.doan.AdminActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminEditFoodItem extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 101;
    private static final String IMGUR_CLIENT_ID = "8fefa7405406b7b";

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private EditText edtName, edtPrice;
    private Spinner spinnerCategory;
    private ImageView imgPreview;
    private Button btnChooseImage, btnSubmit, btnCancel;
    private ProgressBar progressBar;
    private Uri imageUri;
    private String key, currentImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        edtName = findViewById(R.id.editName);
        edtPrice = findViewById(R.id.editPrice);
        spinnerCategory = findViewById(R.id.spinnerEditCategory);
        imgPreview = findViewById(R.id.EditimgPreview);
        btnChooseImage = findViewById(R.id.btnEditChooseImage);
        btnSubmit = findViewById(R.id.btnEditSubmit);
        btnCancel = findViewById(R.id.btnEditCancel);
        progressBar = findViewById(R.id.EditprogressBar);
        progressBar.setVisibility(View.GONE);

        key = getIntent().getStringExtra("key");
        edtName.setText(getIntent().getStringExtra("name"));
        edtPrice.setText(String.valueOf(getIntent().getIntExtra("price", 0)));
        currentImageUrl = getIntent().getStringExtra("imageUrl");

        String[] categories = {"Khai vị", "Món chính", "Tráng miệng", "Thức uống"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

// Set spinner đúng theo category được truyền
        String categoryFromIntent = getIntent().getStringExtra("category");
        if (categoryFromIntent != null) {
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equalsIgnoreCase(categoryFromIntent)) {
                    spinnerCategory.setSelection(i); // chọn đúng loại món
                    break;
                }
            }
        }


        Glide.with(this).load(currentImageUrl).into(imgPreview);

        btnChooseImage.setOnClickListener(v -> checkPermissionAndChooseImage());
        btnSubmit.setOnClickListener(v -> uploadFoodItem());
        btnCancel.setOnClickListener(v -> finish());

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            imgPreview.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void checkPermissionAndChooseImage() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                android.Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION_CODE);
        } else {
            chooseImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseImage();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Chọn ảnh"));
    }

    private void uploadFoodItem() {
        String name = edtName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();

        if (name.isEmpty() || category.isEmpty() || priceStr.isEmpty()) {
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

        if (imageUri != null) {
            uploadImageToImgur(imageUri, imageUrl -> saveToFirebase(name, category, price, imageUrl));
        } else {
            saveToFirebase(name, category, price, currentImageUrl);
        }
    }

    private void saveToFirebase(String name, String category, int price, String imageUrl) {
        FoodItem updatedItem = new FoodItem(name, price, category, imageUrl, true);
        FirebaseDatabase.getInstance().getReference("menu").child(key).setValue(updatedItem)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Đã cập nhật món", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void uploadImageToImgur(Uri uri, OnSuccessListener<String> onSuccess) {
        new Thread(() -> {
            try {
                // B1: Đọc ảnh từ URI mà người dùng đã chọn
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // B2: Resize ảnh về kích thước tối ưu để upload (800x800)
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 800, 800, true);

                // B3: Nén ảnh lại với chất lượng 70% để giảm dung lượng
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);

                // B4: Chuyển ảnh sang định dạng base64 để gửi đi bằng HTTP
                byte[] imageBytes = outputStream.toByteArray();
                String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                // B5: Tạo request gửi ảnh base64 đến Imgur bằng OkHttp
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("image", base64Image)
                        .build();

                Request request = new Request.Builder()
                        .url("https://api.imgur.com/3/image")
                        .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                        .post(body)
                        .build();

                runOnUiThread(() -> progressBar.setProgress(10));

                // B6: Gửi request bất đồng bộ và xử lý kết quả trả về từ Imgur
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
                                // B7: Trích xuất đường dẫn ảnh từ JSON trả về của Imgur
                                JSONObject json = new JSONObject(response.body().string());
                                String imageUrl = json.getJSONObject("data").getString("link");

                                // B8: Gọi callback sau khi upload thành công
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
        }).start();
    }
}