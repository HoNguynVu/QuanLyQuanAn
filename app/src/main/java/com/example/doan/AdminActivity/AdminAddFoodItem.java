package com.example.doan.AdminActivity;

// Các import thư viện cần thiết
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.doan.DatabaseClassResponse.GenericResponse;
import com.example.doan.Network.APIService;
import com.example.doan.Network.RetrofitClient;
import com.example.doan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddFoodItem extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 101;
    private static final String UPLOAD_URL = "http://10.0.2.2/restaurantapi/upload_image.php";

    // Khai báo các thành phần giao diện
    private EditText edtName, edtPrice, edtAmount, edtDescription;
    private Spinner spinnerCategory;
    private ImageView imgPreview;
    private Button btnChooseImage, btnSubmit, btnCancel;
    private ProgressBar progressBar;
    private Uri imageUri;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_menu_item);

        // Gọi các hàm khởi tạo
        initViews();
        setupSpinner();
        setupImagePicker();
        setListeners();
    }

    // Ánh xạ view từ layout
    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtAmount = findViewById(R.id.edtAmount);
        edtDescription = findViewById(R.id.edtDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imgPreview = findViewById(R.id.imgPreview);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    // Cấu hình spinner hiển thị loại món
    private void setupSpinner() {
        String[] categories = {"Khai vị", "Món chính", "Tráng miệng", "Thức uống"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    // Cấu hình chọn ảnh từ thư viện
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            imgPreview.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            Toast.makeText(this, "Không thể hiển thị ảnh", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Gán các sự kiện click cho nút
    private void setListeners() {
        btnChooseImage.setOnClickListener(v -> checkPermissionAndChooseImage());
        btnSubmit.setOnClickListener(v -> uploadFoodItem());
        btnCancel.setOnClickListener(v -> finish());
    }

    // Kiểm tra quyền truy cập ảnh
    private void checkPermissionAndChooseImage() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                Manifest.permission.READ_MEDIA_IMAGES :
                Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_PERMISSION_CODE);
        } else {
            chooseImage();
        }
    }

    // Mở giao diện chọn ảnh
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Chọn ảnh"));
    }

    // Xử lý kết quả yêu cầu quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chooseImage();
        } else {
            Toast.makeText(this, "Cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    // Xử lý khi người dùng nhấn nút "Thêm món"
    private void uploadFoodItem() {
        String name = edtName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String priceStr = edtPrice.getText().toString().trim();
        String availableStr = edtAmount.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        // Kiểm tra dữ liệu nhập
        if (name.isEmpty() || priceStr.isEmpty() || imageUri == null || availableStr.isEmpty() || description.isEmpty()) {
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

        int avail;
        try {
            avail = Integer.parseInt(availableStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;}

        progressBar.setVisibility(View.VISIBLE);

        // Upload ảnh trước → gọi API thêm món
        uploadImageToLocalServer(imageUri, new ImageUploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                apiService.addFood(name, price, category, imageUrl, avail, description).enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {

                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && "success".equalsIgnoreCase(response.body().status)) {
                            Toast.makeText(AdminAddFoodItem.this, "Thêm món thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AdminAddFoodItem.this, "Không thể thêm món", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("loi", t.getMessage());
                        Toast.makeText(AdminAddFoodItem.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminAddFoodItem.this, "Lỗi upload ảnh: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Gửi ảnh dạng base64 lên server PHP, trả về đường dẫn ảnh
    private void uploadImageToLocalServer(Uri uri, ImageUploadCallback callback) {
        new Thread(() -> {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                byte[] imageBytes = outputStream.toByteArray();
                String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("image", base64Image)
                        .build();

                Request request = new Request.Builder()
                        .url(UPLOAD_URL)
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                        runOnUiThread(() -> callback.onFailure(e.getMessage()));
                    }

                    @Override
                    public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                        String responseBody = response.body() != null ? response.body().string() : "";
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            if (json.getBoolean("success")) {
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
                runOnUiThread(() -> callback.onFailure("Lỗi xử lý ảnh"));
            }
        }).start();
    }
}