package com.example.doan.AdminFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class AdminProfileFragment extends Fragment {

    private ImageView imgAvatar;
    private TextView txtUserName, txtPhone;
    private TextView txtOrder, txtSettings, txtLogout;

    private DatabaseReference userRef;

    public AdminProfileFragment() {
        super(com.example.doan.R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ view
        imgAvatar = view.findViewById(com.example.doan.R.id.imgAvatar);
        txtUserName = view.findViewById(com.example.doan.R.id.txtUserName);
        txtPhone = view.findViewById(com.example.doan.R.id.txtPhone);
        txtOrder = view.findViewById(com.example.doan.R.id.txtOrder);
        txtSettings = view.findViewById(com.example.doan.R.id.txtSettings);
        txtLogout = view.findViewById(R.id.txtLogout);

        // Lấy UID người dùng hiện tại
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        // Lắng nghe thay đổi dữ liệu người dùng
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);

                txtUserName.setText(name != null ? name : "Chưa có tên");
                txtPhone.setText("SĐT: " + (phone != null ? phone : "Chưa có SĐT"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi tải hồ sơ: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Các sự kiện click
        txtOrder.setOnClickListener(v ->
                Toast.makeText(getContext(), "🛍️ Đơn mua được chọn", Toast.LENGTH_SHORT).show());

        txtSettings.setOnClickListener(v ->
                Toast.makeText(getContext(), "⚙️ Cài đặt được chọn", Toast.LENGTH_SHORT).show());

        txtLogout.setOnClickListener(v -> {
//            new AlertDialog.Builder(requireContext())
//                    .setTitle("Xác nhận đăng xuất")
//                    .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
//                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
//                        // Đăng xuất khỏi Firebase
//                        FirebaseAuth.getInstance().signOut();
//
//                        // Chuyển sang màn hình đăng nhập
//                        Intent intent = new Intent(requireActivity(), LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//
//                        // Đóng activity hiện tại
//                        requireActivity().finish();
//                    })
//                    .setNegativeButton("Huỷ", null)
//                    .show();
        });


    }
}
