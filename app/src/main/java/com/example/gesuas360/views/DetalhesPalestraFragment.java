package com.example.gesuas360.views;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gesuas360.R;
import com.example.gesuas360.SessaoUsuario;
import com.example.gesuas360.models.ConfirmacaoPresenca;
import com.example.gesuas360.models.Palestra;
import com.example.gesuas360.models.Participante;
import com.example.gesuas360.repositories.PresencaRepository;
import com.google.android.material.button.MaterialButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;

public class DetalhesPalestraFragment extends BaseFragment {

    public static final String ARG_PALESTRA = "palestra";

    private Palestra palestra;
    private MaterialButton btnConfirmar;
    private TextView tvStatus;
    private boolean presencaConfirmada = false;

    private ActivityResultLauncher<ScanOptions> qrLauncher;

    private enum EstadoPresenca { FORA_DO_HORARIO, DISPONIVEL, PROCESSANDO, CONFIRMADO }

    // registerForActivityResult deve ser chamado antes de onStart (requisito do ActivityResult API)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() == null) {
                atualizarEstado(EstadoPresenca.DISPONIVEL);
                return;
            }
            confirmarPresencaComQR(result.getContents());
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalhes_palestra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;

        palestra = (Palestra) args.getSerializable(ARG_PALESTRA);
        if (palestra == null) return;

        btnConfirmar = view.findViewById(R.id.btnConfirmarPresenca);
        tvStatus     = view.findViewById(R.id.tv_status_presenca);

        preencherDetalhes(view);
        configurarBotaoConfirmar();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reavalia disponibilidade sempre que o usuário retorna ao fragment
        configurarBotaoConfirmar();
    }

    // ── Preenchimento de UI ────────────────────────────────────────────────────

    private void preencherDetalhes(View view) {
        TextView tvTitulo  = view.findViewById(R.id.tvDetalheTitulo);
        TextView tvHorario = view.findViewById(R.id.tvDetalheHorario);
        TextView tvSala    = view.findViewById(R.id.tvDetalheSala);
        TextView tvDesc    = view.findViewById(R.id.tvDetalheDescricao);
        TextView tvNome    = view.findViewById(R.id.tvDetalheNomePalestrante);
        TextView tvCargo   = view.findViewById(R.id.tvDetalheCargoPalestrante);
        View secPalestrante = view.findViewById(R.id.tvLabelPalestrante);
        View cardPalestrante = view.findViewById(R.id.cvDetalhePalestrante);

        tvTitulo.setText(palestra.getTitulo());
        tvHorario.setText(palestra.getHorario());
        tvSala.setText(palestra.getLocal());
        tvDesc.setText(palestra.getDescricao());

        boolean temPalestrante = palestra.getPalestranteNome() != null
                && !palestra.getPalestranteNome().isEmpty();
        if (temPalestrante) {
            tvNome.setText(palestra.getPalestranteNome());
            tvCargo.setText(palestra.getPalestranteBio());
        }
        int vis = temPalestrante ? View.VISIBLE : View.GONE;
        if (secPalestrante != null) secPalestrante.setVisibility(vis);
        if (cardPalestrante != null) cardPalestrante.setVisibility(vis);
    }

    // ── Botão de confirmação ───────────────────────────────────────────────────

    private void configurarBotaoConfirmar() {
        if (btnConfirmar == null || palestra == null) return;

        if (presencaConfirmada) {
            atualizarEstado(EstadoPresenca.CONFIRMADO);
            return;
        }

        boolean noHorario = isHorarioDaPalestra(palestra.getHorario());
        atualizarEstado(noHorario ? EstadoPresenca.DISPONIVEL : EstadoPresenca.FORA_DO_HORARIO);

        btnConfirmar.setOnClickListener(v -> {
            if (!isHorarioDaPalestra(palestra.getHorario())) return;
            ScanOptions options = new ScanOptions()
                    .setPrompt("Aponte para o QR code de presença")
                    .setBeepEnabled(true)
                    .setOrientationLocked(false);
            qrLauncher.launch(options);
        });
    }

    private void atualizarEstado(EstadoPresenca estado) {
        if (btnConfirmar == null) return;
        switch (estado) {
            case FORA_DO_HORARIO:
                btnConfirmar.setEnabled(false);
                btnConfirmar.setText("Confirmar Presença");
                btnConfirmar.setIconResource(R.drawable.ic_camera);
                setarCorBotao("#9E9E9E");
                tvStatus.setText("Disponível apenas no horário da palestra (" + palestra.getHorario() + ")");
                tvStatus.setVisibility(View.VISIBLE);
                break;

            case DISPONIVEL:
                btnConfirmar.setEnabled(true);
                btnConfirmar.setText("Escanear QR Code de Presença");
                btnConfirmar.setIconResource(R.drawable.ic_camera);
                setarCorBotao("#689F38");
                tvStatus.setVisibility(View.GONE);
                break;

            case PROCESSANDO:
                btnConfirmar.setEnabled(false);
                btnConfirmar.setIcon(null);
                btnConfirmar.setText("Confirmando...");
                setarCorBotao("#9E9E9E");
                tvStatus.setVisibility(View.GONE);
                break;

            case CONFIRMADO:
                // Usa setClickable para preservar a cor sem o alpha de desabilitado
                btnConfirmar.setEnabled(true);
                btnConfirmar.setClickable(false);
                btnConfirmar.setFocusable(false);
                btnConfirmar.setIcon(null);
                btnConfirmar.setText("Presença Confirmada ✓");
                setarCorBotao("#4CAF50");
                tvStatus.setVisibility(View.GONE);
                break;
        }
    }

    private void setarCorBotao(String hexColor) {
        btnConfirmar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(hexColor)));
    }

    // ── Lógica de horário ──────────────────────────────────────────────────────

    /**
     * MODO_DEMO = true → botão sempre disponível (para gravação / demonstração).
     * Para produção: setar false. A janela real é 15 min antes até 90 min após o início.
     */
    private static final boolean MODO_DEMO = true;

    private boolean isHorarioDaPalestra(String horario) {
        if (MODO_DEMO) return true;

        if (horario == null || !horario.contains(":")) return false;
        try {
            String[] partes = horario.split(":");
            int horaEvento = Integer.parseInt(partes[0]);
            int minEvento  = Integer.parseInt(partes[1]);

            Calendar agora = Calendar.getInstance();
            int totalAgora  = agora.get(Calendar.HOUR_OF_DAY) * 60 + agora.get(Calendar.MINUTE);
            int totalEvento = horaEvento * 60 + minEvento;

            return totalAgora >= (totalEvento - 15) && totalAgora <= (totalEvento + 90);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ── Confirmação com QR ─────────────────────────────────────────────────────

    private void confirmarPresencaComQR(String qrToken) {
        atualizarEstado(EstadoPresenca.PROCESSANDO);

        Participante participante = SessaoUsuario.getInstance().getParticipante();
        ConfirmacaoPresenca dados = new ConfirmacaoPresenca(
                participante != null ? participante.getId()    : "",
                participante != null ? participante.getNome()  : "",
                participante != null ? participante.getEmail() : "",
                palestra.getTitulo(),
                palestra.getHorario(),
                palestra.getData(),
                palestra.getLocal(),
                qrToken
        );

        PresencaRepository.getInstance().confirmarPresenca(dados, new PresencaRepository.PresencaCallback() {
            @Override
            public void onSuccess(String mensagem) {
                presencaConfirmada = true;
                atualizarEstado(EstadoPresenca.CONFIRMADO);
                if (getContext() != null)
                    Toast.makeText(requireContext(), mensagem, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String mensagem) {
                atualizarEstado(EstadoPresenca.DISPONIVEL);
                if (getContext() != null)
                    Toast.makeText(requireContext(), mensagem, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected String getTitulo() {
        return "Detalhes da Palestra";
    }

    @Override
    protected boolean exibirBotaoVoltar() {
        return true;
    }
}
