package online.gixmetir.xuipanelmanagerbackend.services.app;

import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class ClientService {

    public String generateClientString(ClientEntity client) throws IOException {
        Map<String, String> values = new HashMap<>();
        String uuid = String.valueOf(client.getUuid());
        InboundEntity inbound = client.getInbound();
        String address = URI.create(inbound.getServer().getUrl()).getHost();
        if (inbound.getListen() != null && !inbound.getListen().equals("0.0.0.0")) {
            address = inbound.getListen();
        }

        StreamSettings inboundStreamSettings = inbound.getStreamSettingsObj();
        String type = inboundStreamSettings.getNetwork();
        values.put("type", type);

        switch (inboundStreamSettings.getNetwork()) {
            case "tcp" -> {
                TcpSettings tcp = inboundStreamSettings.getTcpSettings();
                if (tcp.getHeader().getType().equals("http")) {
                    TcpRequest request = tcp.getHeader().getRequest();
                    values.put("path", String.join("", request.getPath()));
                    if (!request.getHeaders().getHost().isEmpty()) {
                        values.put("host", request.getHeaders().getHost());
                    }
                    values.put("headerType", tcp.getHeader().getType());
                }
            }
            case "kcp" -> {
                KcpSettings kcp = inboundStreamSettings.getKcpSettings();
                values.put("headerType", kcp.getHeader().getType());
                values.put("seed", kcp.getSeed());
            }
            case "ws" -> {
                WsSettings ws = inboundStreamSettings.getWsSettings();
                values.put("path", ws.getPath());
                if (!ws.getHeaders().getHost().isEmpty()) {
                    values.put("host", ws.getHeaders().getHost());
                }
            }
            case "http" -> {
                HttpSettings http = inboundStreamSettings.getHttpSettings();
                values.put("path", http.getPath());
            }
            case "quic" -> {
                QuicSettings quic = inboundStreamSettings.getQuicSettings();
                values.put("quicSecurity", quic.getSecurity());
                values.put("key", quic.getKey());
                values.put("headerType", "quic");
            }
            case "grpc" -> {
                GrpcSettings grpc = inboundStreamSettings.getGrpcSettings();
                values.put("serviceName", grpc.getServiceName());
                if (grpc.isMultiMode()) {
                    values.put("mode", "multi");
                }
            }
        }

        switch (inboundStreamSettings.getSecurity()) {
            case "tls" -> {
                values.put("security", "tls");
                values.put("fp", inboundStreamSettings.getTlsSettings().getSettings().getFingerprint());
                values.put("alpn", URLEncoder.encode(String.join(",", inboundStreamSettings.getTlsSettings().getAlpn()), StandardCharsets.UTF_8));
                if (inboundStreamSettings.getTlsSettings().getSettings().isAllowInsecure()) {
                    values.put("allowInsecure", "1");
                }
                if (!inboundStreamSettings.getTlsSettings().getServerName().isEmpty()) {
                    address = inboundStreamSettings.getTlsSettings().getServerName();
                }
                if (!inboundStreamSettings.getTlsSettings().getSettings().getServerName().isEmpty()) {
                    values.put("sni", inboundStreamSettings.getTlsSettings().getSettings().getServerName());
                }
                if (type.equals("tcp") && !client.getFlow().isEmpty()) {
                    values.put("flow", client.getFlow());
                }
            }
            case "xtls" -> {
                values.put("security", "xtls");
                values.put("alpn", URLEncoder.encode(String.join(",", inboundStreamSettings.getTlsSettings().getAlpn()), StandardCharsets.UTF_8));

                if (inboundStreamSettings.getXtlsSettings().getSettings().isAllowInsecure()) {
                    values.put("allowInsecure", "1");
                }
                if (!inboundStreamSettings.getTlsSettings().getServerName().isEmpty()) {
                    address = inboundStreamSettings.getTlsSettings().getServerName();
                }
                if (!inboundStreamSettings.getXtlsSettings().getSettings().getServerName().isEmpty()) {
                    values.put("sni", inboundStreamSettings.getXtlsSettings().getSettings().getServerName());
                }
                values.put("flow", client.getFlow());
            }
            case "reality" -> {
                values.put("security", "reality");
                values.put("fp", inboundStreamSettings.getRealitySettings().getSettings().getFp());
                values.put("pbk", inboundStreamSettings.getRealitySettings().getSettings().getPublicKey());
                if (inboundStreamSettings.getRealitySettings().getServerNames().length > 0) {
                    values.put("sni", inboundStreamSettings.getRealitySettings().getServerNames()[0]);
                }
                if (type.equals("tcp") && !client.getFlow().isEmpty()) {
                    values.put("flow", client.getFlow());
                }
                if (inboundStreamSettings.getRealitySettings().getSettings().getServerName() != null
                        && !inboundStreamSettings.getRealitySettings().getSettings().getServerName().isEmpty()) {
                    address = inboundStreamSettings.getRealitySettings().getSettings().getServerName();
                }
            }
        }

        String link = inbound.getProtocol() + "://" + uuid + "@" + address + ":" + inbound.getPort();

        if (!values.isEmpty()) {
            link += "?";
        }

        String queryParams = String.join("&", values.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .toArray(String[]::new));

        link += queryParams;

        if (!inbound.getRemark().isEmpty() || !client.getEmail().isEmpty()) {
            link += "#" + URI.create(inbound.getRemark() + "-" + client.getEmail().replace(" ","")).toASCIIString();
        }

        return link;
    }
}
