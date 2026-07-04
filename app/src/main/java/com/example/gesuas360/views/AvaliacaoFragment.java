package com.example.gesuas360.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.os.BundleCompat;
import androidx.navigation.Navigation;

import com.example.gesuas360.R;
import com.example.gesuas360.SessaoUsuario;
import com.example.gesuas360.models.Avaliacao;
import com.example.gesuas360.models.Palestra;
import com.example.gesuas360.models.Participante;
import com.example.gesuas360.repositories.AvaliacaoRepository;
import com.google.android.material.button.MaterialButton;

/** Tela exibida ao final da palestra para avaliar a palestra e o palestrante. */
public class AvaliacaoFragment extends BaseFragment {

    public static final String ARG_PALESTRA = "palestra";

    private Palestra palestra;
    private RatingBar rbPalestra;
    private RatingBar rbPalestrante;
    private EditText etComentario;
    private boolean temPalestrante;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_avaliacao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;
        palestra = BundleCompat.getSerializable(args, ARG_PALESTRA, Palestra.class);
        if (palestra == null) return;

        TextView tvTitulo         = view.findViewById(R.id.tvAvaliacaoPalestraTitulo);
        TextView tvLabelPalestrante = view.findViewById(R.id.tvLabelNotaPalestrante);
        rbPalestra    = view.findViewById(R.id.rbNotaPalestra);
        rbPalestrante = view.findViewById(R.id.rbNotaPalestrante);
        etComentario  = view.findViewById(R.id.etComentarioAvaliacao);

        tvTitulo.setText(palestra.getTitulo());

        temPalestrante = palestra.getPalestranteNome() != null
                && !palestra.getPalestranteNome().isEmpty();
        int visPalestrante = temPalestrante ? View.VISIBLE : View.GONE;
        tvLabelPalestrante.setVisibility(visPalestrante);
        rbPalestrante.setVisibility(visPalestrante);
        if (temPalestrante) {
            tvLabelPalestrante.setText("Como você avalia " + palestra.getPalestranteNome() + "?");
        }

        MaterialButton btnEnviar = view.findViewById(R.id.btnEnviarAvaliacao);
        btnEnviar.setOnClickListener(v -> enviar());
    }

    private void enviar() {
        int notaPalestra    = Math.round(rbPalestra.getRating());
        int notaPalestrante = temPalestrante ? Math.round(rbPalestrante.getRating()) : 0;

        if (notaPalestra < 1) {
            Toast.makeText(requireContext(), "Dê uma nota para a palestra.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (temPalestrante && notaPalestrante < 1) {
            Toast.makeText(requireContext(), "Dê uma nota para o palestrante.", Toast.LENGTH_SHORT).show();
            return;
        }

        Participante participante = SessaoUsuario.getInstance().getParticipante();
        Avaliacao dados = new Avaliacao(
                participante != null ? participante.getId()   : "",
                participante != null ? participante.getNome() : "",
                palestra.getTitulo(),
                palestra.getData(),
                palestra.getPalestranteNome(),
                notaPalestra,
                notaPalestrante,
                etComentario.getText().toString().trim());

        AvaliacaoRepository.getInstance().enviarAvaliacao(dados,
                new AvaliacaoRepository.AvaliacaoCallback() {
                    @Override
                    public void onSuccess(String mensagem) {
                        if (getContext() != null)
                            Toast.makeText(requireContext(), mensagem, Toast.LENGTH_LONG).show();
                        if (getView() != null)
                            Navigation.findNavController(getView()).popBackStack();
                    }

                    @Override
                    public void onError(String mensagem) {
                        if (getContext() != null)
                            Toast.makeText(requireContext(), mensagem, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected String getTitulo() { return "Avaliação"; }

    @Override
    protected boolean exibirBotaoVoltar() { return true; }

    @Override
    protected boolean exibirBotaoNotificacoes() { return false; }
}
