package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class MenuFragment extends Fragment {

    private CardView cardKhaiVi, cardMonChinh, cardTrangMieng, cardThucUong;

    public MenuFragment() {
        super(R.layout.fragment_menu); // Dùng fragment_menu.xml
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Cho phép fragment tạo menu riêng
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_food, menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(getContext(), AdminAddFoodItem.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gán CardView
        cardKhaiVi = view.findViewById(R.id.cardKhaiVi);
        cardMonChinh = view.findViewById(R.id.cardMonChinh);
        cardTrangMieng = view.findViewById(R.id.cardTrangMieng);
        cardThucUong = view.findViewById(R.id.cardThucUong);

        // Set OnClickListener cho từng CardView
        cardKhaiVi.setOnClickListener(v -> openCategory("Khai vị"));
        cardMonChinh.setOnClickListener(v -> openCategory("Món chính"));
        cardTrangMieng.setOnClickListener(v -> openCategory("Tráng miệng"));
        cardThucUong.setOnClickListener(v -> openCategory("Thức uống"));
    }

    private void openCategory(String categoryName) {
        if (getActivity() != null) { // an toàn
            FoodByCategory fragment = new FoodByCategory();
            Bundle bundle = new Bundle();
            bundle.putString("category", categoryName);
            fragment.setArguments(bundle);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
