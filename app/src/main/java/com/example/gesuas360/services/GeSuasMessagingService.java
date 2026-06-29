package com.example.gesuas360.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.gesuas360.MainActivity;
import com.example.gesuas360.R;
import com.example.gesuas360.SessaoUsuario;
import com.example.gesuas360.models.Notificacao;
import com.example.gesuas360.models.Participante;
import com.example.gesuas360.repositories.NotificacaoRepository;
import com.example.gesuas360.repositories.ParticipanteRepository;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class GeSuasMessagingService extends FirebaseMessagingService {

    private static final String CANAL_ID = "gesuas_channel";

    /**
     * Chamado quando uma mensagem FCM é recebida com o app em foreground.
     *
     * O backend (API) deve enviar notificações via FCM usando a FCM Server Key
     * (disponível em Firebase Console → Project Settings → Cloud Messaging).
     * A Server Key NUNCA deve estar no app — fica apenas no servidor.
     *
     * Payload esperado (data message):
     * POST https://fcm.googleapis.com/fcm/send
     * Authorization: key=<FCM_SERVER_KEY>
     * {
     *   "to": "<fcmToken do participante>",
     *   "data": {
     *     "titulo": "Aviso dos organizadores",
     *     "corpo":  "Texto da notificação",
     *     "tipo":   "0"   // 0=info, 1=mensagem, 2=palestra, 3=alerta
     *   }
     * }
     *
     * Para notificações em background, incluir também "notification" junto com "data"
     * para compatibilidade com app morto/background.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        String titulo = data.containsKey("titulo") ? data.get("titulo") : "";
        String corpo  = data.containsKey("corpo")  ? data.get("corpo")  : "";
        int tipo = Notificacao.TIPO_INFO;
        try {
            tipo = Integer.parseInt(data.getOrDefault("tipo", "0"));
        } catch (Exception ignored) {}

        // Fallback para notification payload (recebido em background)
        if ((titulo == null || titulo.isEmpty()) && remoteMessage.getNotification() != null) {
            titulo = remoteMessage.getNotification().getTitle();
            corpo  = remoteMessage.getNotification().getBody();
        }

        if (titulo == null || titulo.isEmpty()) return;

        String horario = new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(new Date());

        Notificacao notificacao = new Notificacao(
                titulo, corpo != null ? corpo : "", horario, tipo);
        NotificacaoRepository.getInstance().addNotificacao(notificacao);
        mostrarNotificacaoSistema(titulo, corpo != null ? corpo : "");
    }

    /**
     * Chamado quando o FCM gera ou renova o token do dispositivo.
     * Este token deve ser enviado à API para que o backend possa enviar
     * push notifications segmentadas ao participante correto.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        getSharedPreferences("gesuas_prefs", MODE_PRIVATE)
                .edit().putString("fcm_token", token).apply();

        Participante participante = SessaoUsuario.getInstance().getParticipante();
        if (participante != null) {
            ParticipanteRepository.getInstance().registrarFcmToken(token, participante, null);
        }
    }

    private void mostrarNotificacaoSistema(String titulo, String corpo) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CANAL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(titulo)
                .setContentText(corpo)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(corpo))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), notification);
        }
    }
}
