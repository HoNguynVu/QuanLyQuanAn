package com.example.doan.UserFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.doan.R;
import com.example.doan.databinding.UserNoteChangeFragmentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class UserNoteChangeFragment extends BottomSheetDialogFragment {
    UserNoteChangeFragmentBinding binding;
    private String oldNote = "";

    public interface OnTextEnteredListener {
        void onTextEntered(String text);
    }

    private OnTextEnteredListener callback;

    public void setOnTextEnteredListener(OnTextEnteredListener listener) {
        this.callback = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = UserNoteChangeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Lấy note cũ từ arguments
        if (getArguments() != null) {
            oldNote = getArguments().getString("note", "");
            binding.editText.setText(oldNote);
            binding.editText.setSelection(oldNote.length()); // Đưa con trỏ về cuối
        }

        binding.btnConfirm.setOnClickListener(v -> {
            String note = binding.editText.getText().toString().trim();
            if (callback != null) callback.onTextEntered(note);
            dismiss();
        });

        // Tự động hiển thị bàn phím
        binding.editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.editText, InputMethodManager.SHOW_IMPLICIT);
    }
}