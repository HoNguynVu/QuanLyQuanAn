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
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;
import com.example.doan.DatabaseClass.GenericResponse;
import com.example.doan.AdminActivity.ImageUploadCallback;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminEditFoodItem extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 101;
    private static final String UPLOAD_URL = "http://192.168.1.47/restaurantapi/upload_image.php"; //đổi thành link ip của ae

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private EditText edtName, edtPrice, edtAmount, edtDescription;
    private Spinner spinnerCategory;
    private ImageView imgPreview;
    private Button btnChooseImage, btnSubmit, btnCancel;
    private ProgressBar progressBar;
    private Uri imageUri;
    private int id;
    private String currentImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);
        initViews();
        setupSpinner();
        setupImagePicker();
        setListeners();
    }
    private void initViews() {
        edtName = findViewById(R.id.editName);
        edtPrice = findViewById(R.id.editPrice);
        edtAmount = findViewById(R.id.editAmount);
        edtDescription = findViewById(R.id.editDescription);
        spinnerCategory = findViewById(R.id.spinnerEditCategory);
        imgPreview = findViewById(R.id.EditimgPreview);
        btnChooseImage = findViewById(R.id.btnEditChooseImage);
        btnSubmit = findViewById(R.id.btnEditSubmit);
        btnCancel = findViewById(R.id.btnEditCancel);
        progressBar = findViewById(R.id.EditprogressBar);
        progressBar.setVisibility(View.GONE);
        // Nhận dữ liệu từ Intent
        id = getIntent().getIntExtra("id", -1);
        edtName.setText(getIntent().getStringExtra("name"));
        edtPrice.setText(String.valueOf(getIntent().getDoubleExtra("price", 0.0)));
        currentImageUrl = getIntent().getStringExtra("imageUrl");
        edtAmount.setText(String.valueOf(getIntent().getIntExtra("amount", 0)));
        edtDescription.setText(getIntent().getStringExtra("description"));
    }
    private void setupSpinner() {
        String[] categories = {"Khai vị", "Món chính", "Tráng miệng", "Thức uống"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        String categoryFromIntent = getIntent().getStringExtra("category");
        if (categoryFromIntent != null) {
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equalsIgnoreCase(categoryFromIntent)) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
        }
    }
    private void setupImagePicker() {
        Glide.with(this).load(currentImageUrl).into(imgPreview);
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
    private void setListeners() {
        btnChooseImage.setOnClickListener(v -> checkPermissionAndChooseImage());
        btnSubmit.setOnClickListener(v -> uploadFoodItem());
        btnCancel.setOnClickListener(v -> finish());
    }
    private void checkPermissionAndChooseImage() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION_CODE);
        } else {
            chooseImage();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chooseImage();
        } else {
            Toast.makeText(this, "Bạn cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Chọn ảnh"));
    }
    private void uploadFoodItem() {
        String name = edtName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String amountStr = edtAmount.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (name.isEmpty() || category.isEmpty() || priceStr.isEmpty() || amountStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if (imageUri != null) {
            uploadImageToLocalServer(imageUri, new ImageUploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    updateToServer(name, category, price, imageUrl, amount, description);
                }

                @Override
                public void onFailure(String error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminEditFoodItem.this, "Lỗi upload ảnh: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updateToServer(name, category, price, currentImageUrl, amount, description);
        }
    }
    private void updateToServer(String name, String category, double price, String imageUrl, int amount, String description) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        apiService.updateFood(id, name, price, category, imageUrl, amount, description).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                progressBar.setVisibility(View.GONE);
                Log.d("UPDATE", "response: " + response.code() + ", body: " + new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AdminEditFoodItem.this, response.body().message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdminEditFoodItem.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminEditFoodItem.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("loi update", t.getMessage());
            }
        });
    }
    private void uploadImageToLocalServer(Uri uri, ImageUploadCallback callback) {
        new Thread(() -> {  // Chạy khối code upload ảnh trong luồng riêng để tránh chặn UI
            try {
                // 1. Lấy ảnh từ URI được chọn
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // 2. Resize ảnh về kích thước chuẩn 800x800 pixel
                bitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true);

                // 3. Nén ảnh thành JPEG với chất lượng 70% để giảm dung lượng
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                byte[] imageBytes = outputStream.toByteArray();

                // 4. Mã hóa ảnh thành chuỗi base64 để gửi qua HTTP
                String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                // 5. Tạo request HTTP POST gửi đến file PHP upload
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("image", base64Image)  // field name 'image' phải khớp với $_POST['image'] bên PHP
                        .build();

                Request request = new Request.Builder()
                        .url(UPLOAD_URL)  // URL API PHP xử lý upload
                        .post(body)
                        .build();

                // 6. Gửi request bất đồng bộ bằng OkHttp
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                        // Nếu lỗi mạng, gọi callback báo lỗi về giao diện
                        runOnUiThread(() -> callback.onFailure(e.getMessage()));
                    }

                    @Override
                    public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                        String responseBody = response.body() != null ? response.body().string() : "";
                        try {
                            // 7. Phân tích JSON trả về từ server
                            JSONObject json = new JSONObject(responseBody);
                            if (json.getBoolean("success")) {
                                // Lấy đường dẫn ảnh đã lưu nếu upload thành công
                                String imageUrl = json.getString("url");
                                runOnUiThread(() -> callback.onSuccess(imageUrl));
                            } else {
                                runOnUiThread(() -> callback.onFailure("Upload ảnh thất bại"));
                            }
                        } catch (JSONException e) {
                            runOnUiThread(() -> callback.onFailure("Lỗi JSON"));
                        }
                    }
                });
            } catch (IOException e) {
                // Nếu lỗi khi đọc hoặc xử lý ảnh trước khi gửi
                runOnUiThread(() -> callback.onFailure("Lỗi xử lý ảnh"));
            }
        }).start();  // Bắt đầu luồng upload
    }

}