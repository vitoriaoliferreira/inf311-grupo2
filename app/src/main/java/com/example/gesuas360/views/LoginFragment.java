package com.example.gesuas360.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gesuas360.R;
import com.example.gesuas360.SessaoUsuario;
import com.example.gesuas360.models.Participante;
import com.example.gesuas360.repositories.AuthRepository;

public class LoginFragment extends Fragment {

    private final AuthRepository authRepository = new AuthRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etSenha = view.findViewById(R.id.etSenha);
        Button btnEntrar = view.findViewById(R.id.btnEntrar);
        TextView tvErro = view.findViewById(R.id.tvErro);
        ProgressBar progressBar = view.findViewById(R.id.progressLogin);

        btnEntrar.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                tvErro.setText("Preencha todos os campos.");
                tvErro.setVisibility(View.VISIBLE);
                return;
            }

            tvErro.setVisibility(View.GONE);
            btnEntrar.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            authRepository.login(email, senha, new AuthRepository.AuthCallback() {
                @Override
                public void onSuccess(Participante participante) {
                    if (!isAdded()) return;
                    SessaoUsuario.getInstance().setParticipante(participante);
                    progressBar.setVisibility(View.GONE);
                    Navigation.findNavController(v).navigate(R.id.action_login_to_inicio);
                }

                @Override
                public void onError(String mensagem) {
                    if (!isAdded()) return;
                    progressBar.setVisibility(View.GONE);
                    btnEntrar.setEnabled(true);
                    tvErro.setText(mensagem);
                    tvErro.setVisibility(View.VISIBLE);
                }
            });
        });
    }
}
