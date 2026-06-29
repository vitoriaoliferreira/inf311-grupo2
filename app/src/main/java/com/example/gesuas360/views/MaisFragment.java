package com.example.gesuas360.views;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;

import com.example.gesuas360.R;
import com.example.gesuas360.SessaoUsuario;
import com.example.gesuas360.models.Participante;
import com.example.gesuas360.repositories.ParticipanteRepository;
import com.example.gesuas360.repositories.ParticipanteRepository.AtualizarCallback;

public class MaisFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mais, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Participante participante = SessaoUsuario.getInstance().getParticipante();

        TextView tvNome = view.findViewById(R.id.tvNomeUsuario);
        TextView tvTag  = view.findViewById(R.id.tvTagUsuario);

        tvNome.setText(participante != null ? participante.getNome() : "Participante");
        tvTag.setText("Participante");

        configurarContato(view, participante);
        configurarConfiguracoes(view);
        configurarBtnSair(view);
    }

    // ── Seção Contato ────────────────────────────────────────────────────────

    private void configurarContato(View view, Participante participante) {
        View itemEmail    = view.findViewById(R.id.itemEmail);
        View itemTelefone = view.findViewById(R.id.itemTelefone);
        View itemCidade   = view.findViewById(R.id.itemCidade);

        setupMenuItem(itemEmail,    "Email",    participante != null ? participante.getEmail() : null,     R.drawable.ic_mail,        "#15438B");
        setupMenuItem(itemTelefone, "Telefone", participante != null ? participante.getTelefone() : null,  R.drawable.ic_phone,       "#689F38");
        setupMenuItem(itemCidade,   "Cidade",   participante != null ? participante.getCidade() : null,    R.drawable.ic_location_on, "#15438B");

        itemEmail.setOnClickListener(v -> {
            if (participante == null) return;
            exibirDialogEditar(
                "Editar e-mail",
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                participante.getEmail(),
                "seu@email.com",
                (valor, dlg, btn) ->
                    ParticipanteRepository.getInstance().atualizarEmail(valor, participante,
                        new AtualizarCallback() {
                            @Override public void onSuccess() {
                                dlg.dismiss();
                                atualizarSubtitle(itemEmail, participante.getEmail());
                                toast("E-mail atualizado!");
                            }
                            @Override public void onError(String msg) { restaurarBtn(btn); toast(msg); }
                        })
            );
        });

        itemTelefone.setOnClickListener(v -> {
            if (participante == null) return;
            exibirDialogEditar(
                "Editar telefone",
                InputType.TYPE_CLASS_PHONE,
                participante.getTelefone(),
                "(00) 00000-0000",
                (valor, dlg, btn) ->
                    ParticipanteRepository.getInstance().atualizarTelefone(valor, participante,
                        new AtualizarCallback() {
                            @Override public void onSuccess() {
                                dlg.dismiss();
                                atualizarSubtitle(itemTelefone, participante.getTelefone());
                                toast("Telefone atualizado!");
                            }
                            @Override public void onError(String msg) { restaurarBtn(btn); toast(msg); }
                        })
            );
        });

        itemCidade.setOnClickListener(v -> {
            if (participante == null) return;
            exibirDialogEditar(
                "Editar cidade",
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS,
                participante.getCidade(),
                "Sua cidade",
                (valor, dlg, btn) ->
                    ParticipanteRepository.getInstance().atualizarCidade(valor, participante,
                        new AtualizarCallback() {
                            @Override public void onSuccess() {
                                dlg.dismiss();
                                atualizarSubtitle(itemCidade, participante.getCidade());
                                toast("Cidade atualizada!");
                            }
                            @Override public void onError(String msg) { restaurarBtn(btn); toast(msg); }
                        })
            );
        });
    }

    // ── Seção Configurações ──────────────────────────────────────────────────

    private void configurarConfiguracoes(View view) {
        View itemFavoritos = view.findViewById(R.id.itemFavoritos);
        View itemOffline   = view.findViewById(R.id.itemOffline);
        View itemSobre     = view.findViewById(R.id.itemSobre);

        setupMenuItem(itemFavoritos, "Meus Favoritos",   null, R.drawable.ic_bookmark,      "#15438B");
        setupMenuItem(itemOffline,   "Conteúdo Offline", null, R.drawable.ic_cloud_download, "#689F38");
        setupMenuItem(itemSobre,     "Sobre o SUAS 360", null, R.drawable.ic_info,           "#15438B");

        itemFavoritos.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_mais_to_favoritos));

        itemSobre.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://suas360.com.br"))));
    }

    // ── Sair ─────────────────────────────────────────────────────────────────

    private void configurarBtnSair(View view) {
        View btnSair = view.findViewById(R.id.btnSair);
        if (btnSair != null) {
            btnSair.setOnClickListener(v -> {
                SessaoUsuario.getInstance().limpar();
                Navigation.findNavController(v).navigate(R.id.loginFragment);
            });
        }
    }

    // ── Dialog de edição ──────────────────────────────────────────────────────

    interface DialogSalvarListener {
        void onSalvar(String valor, AlertDialog dialog, Button btnSalvar);
    }

    private void exibirDialogEditar(String titulo, int inputType, String valorAtual,
                                     String hint, DialogSalvarListener listener) {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_editar_campo, null);
        EditText etInput = dialogView.findViewById(R.id.et_dialog_input);
        etInput.setInputType(inputType);
        etInput.setHint(hint);
        if (valorAtual != null && !valorAtual.isEmpty()) {
            etInput.setText(valorAtual);
            etInput.setSelection(valorAtual.length());
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(titulo)
                .setView(dialogView)
                .setPositiveButton("Salvar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button btnSalvar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnSalvar.setTextColor(Color.parseColor("#15438B"));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#888888"));

            btnSalvar.setOnClickListener(v -> {
                String valor = etInput.getText().toString().trim();
                btnSalvar.setEnabled(false);
                btnSalvar.setText("Salvando...");
                listener.onSalvar(valor, dialog, btnSalvar);
            });

            etInput.requestFocus();
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        dialog.show();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void atualizarSubtitle(View itemView, String valor) {
        TextView tvSubtitle = itemView.findViewById(R.id.tvItemSubtitle);
        if (tvSubtitle == null) return;
        if (valor != null && !valor.isEmpty()) {
            tvSubtitle.setText(valor);
            tvSubtitle.setVisibility(View.VISIBLE);
        } else {
            tvSubtitle.setVisibility(View.GONE);
        }
    }

    private void restaurarBtn(Button btn) {
        btn.setEnabled(true);
        btn.setText("Salvar");
    }

    private void toast(String msg) {
        if (getContext() != null)
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void setupMenuItem(View includeView, String title, String subtitle,
                                int iconRes, String colorHex) {
        if (includeView == null) return;
        TextView tvTitle    = includeView.findViewById(R.id.tvItemTitle);
        TextView tvSubtitle = includeView.findViewById(R.id.tvItemSubtitle);
        ImageView ivIcon    = includeView.findViewById(R.id.ivItemIcon);
        View cvIcon         = includeView.findViewById(R.id.cvIcon);

        if (tvTitle != null) tvTitle.setText(title);
        if (tvSubtitle != null) {
            if (subtitle != null && !subtitle.isEmpty()) {
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
