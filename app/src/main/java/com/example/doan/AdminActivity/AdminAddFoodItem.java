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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.doan.DatabaseClass.FoodItem;
import com.example.doan.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
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

public class AdminAddFoodItem extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 101;
    private static final String IMGUR_CLIENT_ID = "8fefa7405406b7b";

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private EditText edtName, edtPrice;
    private Spinner spinnerCategory;

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
        spinnerCategory=findViewById(R.id.spinnerCategory);
        imgPreview = findViewById(R.id.imgPreview);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // Tạo danh sách các loại món
        String[] categories = {"Khai vị", "Món chính", "Tráng miệng", "Thức uống"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,   // 👈 dùng layout custom
                categories
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnChooseImage.setOnClickListener(v -> checkPermissionAndChooseImage());
        btnSubmit.setOnClickListener(v -> uploadFoodItem());

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

    private void uploadFoodItem() //check và upload
    {
        String name = edtName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().trim();
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
            // name, category, price, available, imageUrl, description, ratingAvg
            FoodItem item = new FoodItem(name, category, price, true, imageUrl, "", 0.0f);


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