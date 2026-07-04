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
import androidx.core.os.BundleCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.SessaoUsuario;
import com.example.gesuas360.adapters.EnqueteAdapter;
import com.example.gesuas360.models.ConfirmacaoPresenca;
import com.example.gesuas360.models.Enquete;
import com.example.gesuas360.models.OpcaoEnquete;
import com.example.gesuas360.models.Palestra;
import com.example.gesuas360.models.Participante;
import com.example.gesuas360.repositories.EnqueteRepository;
import com.example.gesuas360.repositories.PresencaRepository;
import com.google.android.material.button.MaterialButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;
import java.util.List;

public class DetalhesPalestraFragment extends BaseFragment {

    public static final String ARG_PALESTRA = "palestra";

    private Palestra palestra;
    private MaterialButton btnConfirmar;
    private TextView tvStatus;
    private boolean presencaConfirmada = false;

    private EnqueteAdapter enqueteAdapter;

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

        palestra = BundleCompat.getSerializable(args, ARG_PALESTRA, Palestra.class);
        if (palestra == null) return;

        btnConfirmar = view.findViewById(R.id.btnConfirmarPresenca);
        tvStatus     = view.findViewById(R.id.tv_status_presenca);

        preencherDetalhes(view);
        configurarBotaoConfirmar();
        configurarEnquetes(view);
        configurarAvaliacao(view);
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

    // ── Enquetes da palestra ───────────────────────────────────────────────────

    private void configurarEnquetes(View view) {
        TextView tvLabel = view.findViewById(R.id.tvLabelEnquetesPalestra);
        RecyclerView rv  = view.findViewById(R.id.rvEnquetesPalestra);
        if (rv == null) return;

        List<Enquete> enquetes = EnqueteRepository.getInstance()
                .getEnquetesByPalestra(palestra.getTitulo());

        boolean temEnquetes = !enquetes.isEmpty();
        if (tvLabel != null) tvLabel.setVisibility(temEnquetes ? View.VISIBLE : View.GONE);
        rv.setVisibility(temEnquetes ? View.VISIBLE : View.GONE);
        if (!temEnquetes) return;

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        // Vínculo com a palestra é implícito aqui, então não repetimos o título.
        enqueteAdapter = new EnqueteAdapter(enquetes, false, this::votarEnquete);
        rv.setAdapter(enqueteAdapter);
    }

    private void votarEnquete(Enquete enquete, OpcaoEnquete opcao) {
        EnqueteRepository.getInstance().votar(enquete.getId(), opcao.getId(),
                new EnqueteRepository.EnqueteCallback() {
                    @Override
                    public void onSuccess(String mensagem) {
                        if (enqueteAdapter != null) enqueteAdapter.notifyDataSetChanged();
                        if (getContext() != null)
                            Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String mensagem) {
                        if (getContext() != null)
                            Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ── Avaliação (ao final da palestra) ───────────────────────────────────────

    /**
     * MODO_DEMO_AVALIACAO = true → botão "Avaliar" sempre visível (para demonstração).
     * Em produção, a avaliação só aparece após o término da palestra.
     */
    private static final boolean MODO_DEMO_AVALIACAO = true;

    private void configurarAvaliacao(View view) {
        View label     = view.findViewById(R.id.tvLabelAvaliar);
        View subtitulo = view.findViewById(R.id.tvAvaliarSubtitulo);
        MaterialButton btnAvaliar = view.findViewById(R.id.btnAvaliarPalestra);
        if (btnAvaliar == null) return;

        boolean encerrada = isPalestraEncerrada(palestra.getHorario());
        int vis = encerrada ? View.VISIBLE : View.GONE;
        if (label != null) label.setVisibility(vis);
        if (subtitulo != null) subtitulo.setVisibility(vis);
        btnAvaliar.setVisibility(vis);

        btnAvaliar.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(AvaliacaoFragment.ARG_PALESTRA, palestra);
            Navigation.findNavController(v).navigate(R.id.avaliacaoFragment, bundle);
        });
    }

    /** Uma palestra é considerada encerrada 90 min após o seu horário de início. */
    private boolean isPalestraEncerrada(String horario) {
        if (MODO_DEMO_AVALIACAO) return true;

        if (horario == null || !horario.contains(":")) return false;
        try {
            String[] partes = horario.split(":");
            int totalEvento = Integer.parseInt(partes[0]) * 60 + Integer.parseInt(partes[1]);
            Calendar agora = Calendar.getInstance();
            int totalAgora = agora.get(Calendar.HOUR_OF_DAY) * 60 + agora.get(Calendar.MINUTE);
            return totalAgora > (totalEvento + 90);
        } catch (NumberFormatException e) {
            return false;
        }
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
