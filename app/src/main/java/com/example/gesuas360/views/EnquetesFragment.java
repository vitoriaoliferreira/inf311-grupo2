package com.example.gesuas360.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.adapters.EnqueteAdapter;
import com.example.gesuas360.models.Enquete;
import com.example.gesuas360.models.OpcaoEnquete;
import com.example.gesuas360.repositories.EnqueteRepository;

import java.util.List;

/** Tela com todas as enquetes ativas do evento, abertas para votação. */
public class EnquetesFragment extends BaseFragment {

    private EnqueteAdapter adapter;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enquetes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmpty = view.findViewById(R.id.tv_empty_enquetes);

        RecyclerView rv = view.findViewById(R.id.rvEnquetes);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EnqueteAdapter(
                EnqueteRepository.getInstance().getEnquetesAtivas(),
                true,
                this::votar);
        rv.setAdapter(adapter);

        atualizarEstadoVazio();
    }

    private void votar(Enquete enquete, OpcaoEnquete opcao) {
        EnqueteRepository.getInstance().votar(enquete.getId(), opcao.getId(),
                new EnqueteRepository.EnqueteCallback() {
                    @Override
                    public void onSuccess(String mensagem) {
                        if (adapter != null) adapter.notifyDataSetChanged();
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

    private void atualizarEstadoVazio() {
        if (tvEmpty == null) return;
        List<Enquete> ativas = EnqueteRepository.getInstance().getEnquetesAtivas();
        tvEmpty.setVisibility(ativas.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected String getTitulo() { return "Enquetes"; }

    @Override
    protected boolean exibirBotaoVoltar() { return true; }

    @Override
    protected boolean exibirBotaoNotificacoes() { return false; }
}
