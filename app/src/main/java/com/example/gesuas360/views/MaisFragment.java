package com.example.gesuas360.views;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gesuas360.R;

public class MaisFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mais, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvNome = view.findViewById(R.id.tvNomeUsuario);
        TextView tvTag = view.findViewById(R.id.tvTagUsuario);

        tvNome.setText("Ana Silva");
        tvTag.setText("Participante");

        setupMenuItem(view.findViewById(R.id.itemEmail), "Email", "anasilva@email.com", R.drawable.ic_mail, "#15438B");
        setupMenuItem(view.findViewById(R.id.itemTelefone), "Telefone", "(11) 99999-9999", R.drawable.ic_phone, "#689F38");
        setupMenuItem(view.findViewById(R.id.itemCidade), "Cidade", "Viçosa / MG", R.drawable.ic_location_on, "#15438B");

        setupMenuItem(view.findViewById(R.id.itemFavoritos), "Meus Favoritos", null, R.drawable.ic_bookmark, "#15438B");
        setupMenuItem(view.findViewById(R.id.itemOffline), "Conteúdo Offline", null, R.drawable.ic_cloud_download, "#689F38");
        setupMenuItem(view.findViewById(R.id.itemSobre), "Sobre o SUAS 360", null, R.drawable.ic_info, "#15438B");

        View btnSair = view.findViewById(R.id.btnSair);
        if (btnSair != null) {
            btnSair.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Fazendo logout...", Toast.LENGTH_SHORT).show());
        }
    }

    private void setupMenuItem(View includeView, String title, String subtitle, int iconRes, String colorHex) {
        if (includeView == null) return;

        TextView tvTitle = includeView.findViewById(R.id.tvItemTitle);
        TextView tvSubtitle = includeView.findViewById(R.id.tvItemSubtitle);
        ImageView ivIcon = includeView.findViewById(R.id.ivItemIcon);
        View cvIcon = includeView.findViewById(R.id.cvIcon);

        if (tvTitle != null) tvTitle.setText(title);
        
        if (tvSubtitle != null) {
            if (subtitle != null) {
                tvSubtitle.setText(subtitle);
                tvSubtitle.setVisibility(View.VISIBLE);
            } else {
                tvSubtitle.setVisibility(View.GONE);
            }
        }

        if (ivIcon != null) ivIcon.setImageResource(iconRes);
        if (cvIcon != null) cvIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colorHex)));
    }

    @Override
    protected String getTitulo() {
        return "Mais";
    }
}
