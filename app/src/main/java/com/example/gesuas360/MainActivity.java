package com.example.gesuas360;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.gesuas360.models.Participante;
import com.example.gesuas360.repositories.ParticipanteRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        criarCanalNotificacao();

        drawerLayout = findViewById(R.id.drawerLayout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationView navigationView = findViewById(R.id.navigationView);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) return;

        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            boolean isAuthScreen = id == R.id.splashFragment || id == R.id.loginFragment;

            bottomNavigationView.setVisibility(isAuthScreen ? View.GONE : View.VISIBLE);
            drawerLayout.setDrawerLockMode(
                    isAuthScreen ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED
            );

            if (id == R.id.inicioFragment) {
                atualizarCabecalhoDrawer(navigationView);
                registrarFcmToken();
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_programacao) {
                bottomNavigationView.setSelectedItemId(R.id.programacaoFragment);
            } else if (id == R.id.nav_palestrantes) {
                bottomNavigationView.setSelectedItemId(R.id.palestrantesFragment);
            } else if (id == R.id.nav_mensagens) {
                bottomNavigationView.setSelectedItemId(R.id.mensagensFragment);
            } else if (id == R.id.nav_enquetes) {
                navController.navigate(R.id.enquetesFragment);
            } else if (id == R.id.nav_notificacoes) {
                navController.navigate(R.id.notificacoesFragment);
            } else if (id == R.id.nav_favoritos) {
                navController.navigate(R.id.favoritosFragment);
            } else if (id == R.id.nav_sair) {
                SessaoUsuario.getInstance().limpar();
                navController.navigate(R.id.loginFragment);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Botão "voltar": fecha o drawer se estiver aberto; senão segue o fluxo padrão.
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                    setEnabled(true);
                }
            }
        });
    }

    public void abrirMenu() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    // ── Drawer ────────────────────────────────────────────────────────────────

    private void atualizarCabecalhoDrawer(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        if (header == null) return;

        Participante participante = SessaoUsuario.getInstance().getParticipante();
        if (participante == null) return;

        TextView tvNome  = header.findViewById(R.id.tvDrawerNome);
        TextView tvEmail = header.findViewById(R.id.tvDrawerEmail);

        if (tvNome != null) tvNome.setText(participante.getNome());
        if (tvEmail != null) tvEmail.setText(participante.getEmail());
    }

    // ── Notificações ──────────────────────────────────────────────────────────

    private void criarCanalNotificacao() {
        NotificationChannel canal = new NotificationChannel(
                "gesuas_channel",
                "GeSUAS 360 - Notificações",
                NotificationManager.IMPORTANCE_HIGH
        );
        canal.setDescription("Notificações dos organizadores do SUAS 360");
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) manager.createNotificationChannel(canal);
    }

    /**
     * Obtém o token FCM do dispositivo e o envia à API.
     * Deve ser chamado após o login (quando o participante está disponível na sessão).
     * Para enviar push, o backend usa a FCM Server Key — não o token do dispositivo.
     */
    private void registrarFcmToken() {
        try {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
                Participante participante = SessaoUsuario.getInstance().getParticipante();
                if (participante != null && token != null) {
                    ParticipanteRepository.getInstance()
                            .registrarFcmToken(token, participante, null);
                }
            });
        } catch (Exception ignored) {
            // Firebase não conectado (google-services.json ainda é placeholder)
        }
    }
}
