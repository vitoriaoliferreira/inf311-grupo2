package com.example.gesuas360.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.models.Enquete;
import com.example.gesuas360.models.OpcaoEnquete;

import java.util.List;

/**
 * Renderiza cada enquete. Antes do voto, as opções aparecem como botões clicáveis;
 * depois do voto (ou se a enquete estiver encerrada), aparecem como barras de
 * resultado com o percentual de cada opção.
 */
public class EnqueteAdapter extends RecyclerView.Adapter<EnqueteAdapter.ViewHolder> {

    public interface OnVotarListener {
        void onVotar(Enquete enquete, OpcaoEnquete opcao);
    }

    private static final int COR_AZUL  = Color.parseColor("#15438B");
    private static final int COR_VERDE = Color.parseColor("#689F38");

    private List<Enquete> enquetes;
    private final boolean mostrarVinculoPalestra;
    private final OnVotarListener listener;

    public EnqueteAdapter(List<Enquete> enquetes, boolean mostrarVinculoPalestra,
                          OnVotarListener listener) {
        this.enquetes = enquetes;
        this.mostrarVinculoPalestra = mostrarVinculoPalestra;
        this.listener = listener;
    }

    public void updateData(List<Enquete> lista) {
        this.enquetes = lista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_enquete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Enquete enquete = enquetes.get(position);
        Context ctx = holder.itemView.getContext();

        holder.tvPergunta.setText(enquete.getPergunta());

        boolean temVinculo = mostrarVinculoPalestra && !enquete.isGeral();
        if (temVinculo) {
            holder.tvPalestra.setVisibility(View.VISIBLE);
            holder.tvPalestra.setText("Palestra: " + enquete.getPalestraTitulo());
        } else {
            holder.tvPalestra.setVisibility(View.GONE);
        }

        boolean mostrarResultado = enquete.isJaVotou() || !enquete.isAtiva();

        holder.llOpcoes.removeAllViews();
        for (OpcaoEnquete opcao : enquete.getOpcoes()) {
            View opcaoView = mostrarResultado
                    ? criarLinhaResultado(ctx, enquete, opcao)
                    : criarBotaoOpcao(ctx, enquete, opcao);
            holder.llOpcoes.addView(opcaoView);
        }

        if (enquete.isJaVotou()) {
            holder.tvInfo.setText("Você votou · " + enquete.getTotalVotos() + " votos");
        } else if (!enquete.isAtiva()) {
            holder.tvInfo.setText("Enquete encerrada · " + enquete.getTotalVotos() + " votos");
        } else {
            holder.tvInfo.setText(enquete.getEncerraEm());
        }
    }

    @Override
    public int getItemCount() {
        return enquetes.size();
    }

    // ── Construção das opções ────────────────────────────────────────────────────

    /** Opção clicável exibida antes do voto. */
    private View criarBotaoOpcao(Context ctx, Enquete enquete, OpcaoEnquete opcao) {
        TextView tv = new TextView(ctx);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(ctx, 8);
        tv.setLayoutParams(lp);
        tv.setText(opcao.getTexto());
        tv.setTextColor(COR_AZUL);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setBackgroundResource(R.drawable.bg_opcao_enquete);
        int padH = dp(ctx, 14), padV = dp(ctx, 12);
        tv.setPadding(padH, padV, padH, padV);
        tv.setClickable(true);
        tv.setFocusable(true);
        tv.setOnClickListener(v -> {
            if (listener != null) listener.onVotar(enquete, opcao);
        });
        return tv;
    }

    /** Barra de resultado exibida após o voto ou com a enquete encerrada. */
    private View criarLinhaResultado(Context ctx, Enquete enquete, OpcaoEnquete opcao) {
        boolean escolhida = opcao.getId().equals(enquete.getOpcaoVotadaId());
        int percentual = enquete.getPercentual(opcao);
        int cor = escolhida ? COR_VERDE : COR_AZUL;

        LinearLayout container = new LinearLayout(ctx);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(ctx, 10);
        container.setLayoutParams(lp);

        // Linha: texto (esquerda) + percentual (direita)
        LinearLayout linha = new LinearLayout(ctx);
        linha.setOrientation(LinearLayout.HORIZONTAL);
        linha.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView tvTexto = new TextView(ctx);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        tvTexto.setLayoutParams(tp);
        String texto = escolhida ? opcao.getTexto() + "  ✓" : opcao.getTexto();
        tvTexto.setText(texto);
        tvTexto.setTextColor(Color.parseColor("#333333"));
        tvTexto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvTexto.setTypeface(null, escolhida ? Typeface.BOLD : Typeface.NORMAL);

        TextView tvPct = new TextView(ctx);
        tvPct.setText(percentual + "%");
        tvPct.setTextColor(cor);
        tvPct.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvPct.setTypeface(null, Typeface.BOLD);
        tvPct.setGravity(Gravity.END);

        linha.addView(tvTexto);
        linha.addView(tvPct);

        ProgressBar barra = new ProgressBar(ctx, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(ctx, 8));
        bp.topMargin = dp(ctx, 4);
        barra.setLayoutParams(bp);
        barra.setMax(100);
        barra.setProgress(percentual);
        barra.setProgressTintList(ColorStateList.valueOf(cor));

        container.addView(linha);
        container.addView(barra);
        return container;
    }

    private int dp(Context ctx, int value) {
        return Math.round(value * ctx.getResources().getDisplayMetrics().density);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPalestra, tvPergunta, tvInfo;
        LinearLayout llOpcoes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPalestra = itemView.findViewById(R.id.tvEnquetePalestra);
            tvPergunta = itemView.findViewById(R.id.tvEnquetePergunta);
            tvInfo     = itemView.findViewById(R.id.tvEnqueteInfo);
            llOpcoes   = itemView.findViewById(R.id.llOpcoesEnquete);
        }
    }
}
