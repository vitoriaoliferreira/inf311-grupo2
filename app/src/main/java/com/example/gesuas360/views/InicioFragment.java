package com.example.gesuas360.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.MainActivity;
import com.example.gesuas360.R;
import com.example.gesuas360.SessaoUsuario;
import com.example.gesuas360.adapters.ProximaPalestraAdapter;
import com.example.gesuas360.models.Enquete;
import com.example.gesuas360.models.Notificacao;
import com.example.gesuas360.models.Palestra;
import com.example.gesuas360.models.Participante;
import com.example.gesuas360.repositories.EnqueteRepository;
import com.example.gesuas360.repositories.NotificacaoRepository;
import com.example.gesuas360.repositories.PalestraRepository;

import java.util.List;
import java.util.stream.Collectors;

public class InicioFragment extends BaseFragment {

    public static final String ARG_PALESTRA = "palestra";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // super configura badge automático via BaseFragment (fl_notif_badge_wrapper / tv_badge_notificacoes)
        super.onViewCreated(view, savedInstanceState);

        configurarSaudacao(view);
        configurarPalestraAgora(view);
        configurarProximasPalestras(view);
        configurarAvisosRecentes(view);
        configurarEnquetesAtivas(view);
        configurarBotoes(view);
    }

    private void configurarSaudacao(View view) {
        TextView tvOla = view.findViewById(R.id.tvOlaUsuario);
        if (tvOla == null) return;

        Participante participante = SessaoUsuario.getInstance().getParticipante();
        String primeiroNome = participante != null ? primeiroNome(participante.getNome()) : "Participante";
        tvOla.setText("Olá, " + primeiroNome + "!");
    }

    private void configurarPalestraAgora(View view) {
        List<Palestra> palestrasHoje = PalestraRepository.getInstance().getPalestrasByData("11");
        Palestra palestraAgora = null;
        for (Palestra p : palestrasHoje) {
            if (!p.getPalestranteNome().isEmpty()) {
                palestraAgora = p;
                break;
            }
        }
        if (palestraAgora == null && !palestrasHoje.isEmpty()) {
            palestraAgora = palestrasHoje.get(0);
        }
        if (palestraAgora == null) return;

        TextView tvHorario    = view.findViewById(R.id.tvPalestraAgoraHorario);
        TextView tvLocal      = view.findViewById(R.id.tvPalestraAgoraLocal);
        TextView tvTitulo     = view.findViewById(R.id.tvPalestraAgoraTitulo);
        TextView tvDescricao  = view.findViewById(R.id.tvPalestraAgoraDescricao);
        TextView tvPalestrante = view.findViewById(R.id.tvPalestraAgoraPalestrante);

        if (tvHorario != null)    tvHorario.setText(palestraAgora.getHorario());
        if (tvLocal != null)      tvLocal.setText(palestraAgora.getLocal());
        if (tvTitulo != null)     tvTitulo.setText(palestraAgora.getTitulo());
        if (tvDescricao != null)  tvDescricao.setText(palestraAgora.getDescricao());
        if (tvPalestrante != null) {
            String nome = palestraAgora.getPalestranteNome();
            tvPalestrante.setText(nome);
            tvPalestrante.setVisibility(nome.isEmpty() ? View.GONE : View.VISIBLE);
        }

        final Palestra palestraFinal = palestraAgora;
        View btnMaisInfo = view.findViewById(R.id.btnMaisInfo);
        if (btnMaisInfo != null) {
            btnMaisInfo.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ARG_PALESTRA, palestraFinal);
                Navigation.findNavController(v).navigate(R.id.action_inicio_to_detalhes_palestra, bundle);
            });
        }
    }

    private void configurarProximasPalestras(View view) {
        List<Palestra> todasPalestras = PalestraRepository.getInstance().getPalestras();
        List<Palestra> proximas = todasPalestras.stream()
                .filter(p -> !p.getPalestranteNome().isEmpty())
                .limit(4)
                .collect(Collectors.toList());

        RecyclerView rvProximas = view.findViewById(R.id.rvProximasPalestras);
        if (rvProximas != null) {
            rvProximas.setLayoutManager(new LinearLayoutManager(getContext()));
            rvProximas.setAdapter(new ProximaPalestraAdapter(proximas, palestra -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ARG_PALESTRA, palestra);
                Navigation.findNavController(view).navigate(R.id.action_inicio_to_detalhes_palestra, bundle);
            }));
        }
    }

    private void configurarAvisosRecentes(View view) {
        // Preenche o card de aviso com a notificação mais recente (índice 0 do repositório).
        List<Notificacao> notificacoes = NotificacaoRepository.getInstance().getNotificacoes();
        if (!notificacoes.isEmpty()) {
            Notificacao maisRecente = notificacoes.get(0);

            TextView tvTitulo = view.findViewById(R.id.tvAvisoTitle);
            TextView tvCorpo  = view.findViewById(R.id.tvAvisoBody);
            if (tvTitulo != null) tvTitulo.setText(maisRecente.getTitulo());
            if (tvCorpo != null)  tvCorpo.setText(maisRecente.getCorpo());
        }

        // Clicar no card ou em "Ver todos" abre a página de notificações.
        View.OnClickListener abrirNotificacoes = v ->
                Navigation.findNavController(v).navigate(R.id.notificacoesFragment);

        View cardAviso = view.findViewById(R.id.cardAvisoDash);
        if (cardAviso != null) cardAviso.setOnClickListener(abrirNotificacoes);

        View btnVerTodos = view.findViewById(R.id.btnVerTodosAvisos);
        if (btnVerTodos != null) btnVerTodos.setOnClickListener(abrirNotificacoes);
    }

    private void configurarEnquetesAtivas(View view) {
        // Preenche o card de destaque com a enquete ativa mais recente.
        List<Enquete> ativas = EnqueteRepository.getInstance().getEnquetesAtivas();
        if (!ativas.isEmpty()) {
            Enquete destaque = ativas.get(0);

            TextView tvTitulo = view.findViewById(R.id.tvEnqueteTitle);
            TextView tvInfo   = view.findViewById(R.id.tvEnqueteInfo);
            if (tvTitulo != null) tvTitulo.setText(destaque.getPergunta());
            if (tvInfo != null)   tvInfo.setText(destaque.getEncerraEm());
        }

        // Clicar no card ou em "Ver todas" abre a tela de enquetes ativas.
        View.OnClickListener abrirEnquetes = v ->
                Navigation.findNavController(v).navigate(R.id.enquetesFragment);

        View cardEnquete = view.findViewById(R.id.cardEnqueteDash);
        if (cardEnquete != null) cardEnquete.setOnClickListener(abrirEnquetes);

        View btnVerTodas = view.findViewById(R.id.btnVerTodosEnquetes);
        if (btnVerTodas != null) btnVerTodas.setOnClickListener(abrirEnquetes);
    }

    private void configurarBotoes(View view) {
        View btnMenu = view.findViewById(R.id.btnMenuHamb);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> ((MainActivity) requireActivity()).abrirMenu());
        }

        // btnNotificacoesCabecalho é o novo ID do sino (BaseFragment já cuida do click via setupHeader)

        View btnVerProgramacao = view.findViewById(R.id.btnVerProgramacao);
        if (btnVerProgramacao != null) {
            btnVerProgramacao.setOnClickListener(v -> {
                com.google.android.material.bottomnavigation.BottomNavigationView bottomNav =
                        requireActivity().findViewById(R.id.bottom_navigation);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.programacaoFragment);
                } else {
                    Navigation.findNavController(v).navigate(R.id.programacaoFragment);
                }
            });
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String primeiroNome(String nomeCompleto) {
        if (nomeCompleto == null || nomeCompleto.isEmpty()) return "Participante";
        int espaco = nomeCompleto.indexOf(' ');
        return espaco > 0 ? nomeCompleto.substring(0, espaco) : nomeCompleto;
    }

    @Override
    protected String getTitulo() {
        return "Início";
    }
}
