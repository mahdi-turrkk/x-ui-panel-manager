package online.gixmetir.xuipanelmanagerbackend.services.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels.*;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class ClientService {

    public String generateClientString(ClientEntity client, long days, double remainingFlow) throws IOException {
        String link = "";
        switch (client.getInbound().getProtocol()) {
            case "vmess":
                link = generateVmessLink(client);
                break;
            case "vless":
                link = generateVlessLink(client);
                break;
            case "trojan":
                //todo
//                link = generateTrojanLink(client);
                break;
            case "shadowsocks":
                //todo
//                link = generateShadowsocksLink(client);
                break;
            case "wireguard":
                //todo
//                link = generateWireguardLink(client);
                break;
        }
        return link;
    }

    private String generateVlessLink(ClientEntity client) throws IOException {
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
            link += "#" + generateClientName(client);
        }
        return link;
    }

    private String generateVmessLink(ClientEntity client) throws IOException {
        InboundEntity inbound = client.getInbound();
        ServerEntity server = client.getInbound().getServer();
        Map<String, Object> obj = new HashMap<>();
        obj.put("v", "2");
        if (inbound.getStreamSettingsObj().getTlsSettings().getServerName() != null && !inbound.getStreamSettingsObj().getTlsSettings().getServerName().isEmpty())
            obj.put("add", inbound.getStreamSettingsObj().getTlsSettings().getServerName());
        else
            obj.put("add", server.getUrl());
        obj.put("port", inbound.getPort());
        obj.put("port", inbound.getPort());
        obj.put("type", "none");

        Map<String, Object> stream = new HashMap<>();
//        Json.unmarshal(inbound.getStreamSettings().getBytes(), stream);

        String network = inbound.getStreamSettingsObj().getNetwork();
        obj.put("net", network);

        switch (network) {
            case "tcp":
                TcpSettings tcpSettings = inbound.getStreamSettingsObj().getTcpSettings();
//                Map<String, Object> tcp = (Map<String, Object>) stream.get("tcpSettings");
//                Map<String, Object> header = (Map<String, Object>) tcp.get("header");
                String typeStr = tcpSettings.getHeader().getType();
                obj.put("type", typeStr);

                if (typeStr.equals("http")) {
//                    Map<String, Object> request = (Map<String, Object>) header.get("request");
//                    List<Object> requestPath = (List<Object>) request.get("path");
//                    obj.put("path", requestPath.get(0));
                    TcpRequest request = tcpSettings.getHeader().getRequest();
                    obj.put("path", request.getPath());
                    obj.put("host", request.getHeaders().getHost());

//                    Map<String, Object> request = (Map<String, Object>) header.get("request");
//                    List<Object> requestPath = (List<Object>) request.get("path");
//                    obj.put("path", requestPath.get(0));
//
//                    Map<String, Object> headers = (Map<String, Object>) request.get("headers");
//                    obj.put("host", searchHost(headers));
                }
                break;

            case "kcp":
                KcpSettings kcpSettings = inbound.getStreamSettingsObj().getKcpSettings();
//                Map<String, Object> kcp = (Map<String, Object>) stream.get("kcpSettings");
//                Map<String, Object> kcpHeader = (Map<String, Object>) kcp.get("header");
                obj.put("type", kcpSettings.getHeader().getType());
                obj.put("path", kcpSettings.getSeed());
                break;

            case "ws":
                WsSettings wsSettings = inbound.getStreamSettingsObj().getWsSettings();
//                Map<String, Object> ws = (Map<String, Object>) stream.get("wsSettings");
                obj.put("path", wsSettings.getPath());
//                Map<String, Object> wsHeaders = (Map<String, Object>) ws.get("headers");
                obj.put("host", wsSettings.getHeaders().getHost());
                break;

            case "http":
                HttpSettings httpSettings = inbound.getStreamSettingsObj().getHttpSettings();
                obj.put("net", "h2");
//                Map<String, Object> http = (Map<String, Object>) stream.get("httpSettings");
                obj.put("path", httpSettings.getPath());
//                obj.put("host", searchHost(http));
                obj.put("host", httpSettings.getHost().get(0) != null ? httpSettings.getHost().get(0) : "");
                break;

            case "quic":
                QuicSettings quicSettings = inbound.getStreamSettingsObj().getQuicSettings();
//                Map<String, Object> quic = (Map<String, Object>) stream.get("quicSettings");
//                Map<String, Object> quicHeader = (Map<String, Object>) quic.get("header");
                obj.put("type", quicSettings.getHeader().getType());
                obj.put("host", quicSettings.getSecurity());
                obj.put("path", quicSettings.getKey());
                break;

            case "grpc":
//                Map<String, Object> grpc = (Map<String, Object>) stream.get("grpcSettings");
                GrpcSettings grpcSettings = inbound.getStreamSettingsObj().getGrpcSettings();
                obj.put("path", grpcSettings.getServiceName());
                if (grpcSettings.isMultiMode()) {
                    obj.put("type", "multi");
                }
                break;
        }

//        String security = (String) stream.get("security");
        String security = inbound.getStreamSettingsObj().getSecurity();

        List<String> domains = new ArrayList<>();
        obj.put("tls", security);

        if (security.equals("tls")) {
            TlsSettings tlsSettings = inbound.getStreamSettingsObj().getTlsSettings();
//            Map<String, Object> tlsSetting = (Map<String, Object>) stream.get("tlsSettings");
//            List<Object> alpns = (List<Object>) tlsSetting.get("alpn");
            List<String> alpns = Arrays.stream(tlsSettings.getAlpn()).toList();


            if (!alpns.isEmpty()) {
                List<String> alpn = new ArrayList<>(alpns);
                obj.put("alpn", String.join(",", alpn));
            }
            TlsSettingsInner tlsSettingsInner = tlsSettings.getSettings();
//            Map<String, Object> tlsSettings = (Map<String, Object>) searchKey(tlsSetting, "settings");
            if (tlsSettingsInner != null) {
                if (tlsSettingsInner.getServerName() != null) {
                    obj.put("sni", tlsSettingsInner.getServerName());
                }
                if (tlsSettingsInner.getFingerprint() != null) {
                    obj.put("fp", tlsSettingsInner.getFingerprint());
                }
                if (tlsSettingsInner.isAllowInsecure()) {
                    obj.put("allowInsecure", true);
                }
                if (tlsSettingsInner.getDomains() != null) {
                    domains = Arrays.stream(tlsSettingsInner.getDomains()).toList();
                }
            }

//            String serverName = (String) tlsSetting.get("serverName");
//            if (tlsSettingsInner.getServerName() != null) {
//                obj.put("add", tlsSettingsInner.getServerName());
//            }
        }

        obj.put("id", client.getUuid());

        if (!domains.isEmpty()) {
            StringBuilder links = new StringBuilder();
            for (int i = 0; i < domains.size(); i++) {
//                Map<String, Object> domain = (Map<String, Object>) domains.get(i);
                obj.put("ps", generateClientName(client));
                obj.put("add", domains.get(i));

                if (i > 0) {
                    links.append("\n");
                }
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(obj);
//                String jsonStr = Json.marshalIndent(obj, "", " ");
                links.append("vmess://").append(Base64.getEncoder().encodeToString(json.getBytes()));
            }

            return links.toString();
        }

        obj.put("ps", generateClientName(client));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
//        String jsonStr = json.marshalIndent(obj, "", " ");
        String s = "vmess://" + Base64.getEncoder().encodeToString(json.getBytes());
        return s;
    }

    private String generateClientName(ClientEntity clientEntity) {
        SubscriptionEntity subscription = clientEntity.getSubscription();

        long numOfDays = 0;
        if (subscription.getExpireDate() == null) {
            numOfDays = subscription.getPeriodLength();
        } else numOfDays = ChronoUnit.DAYS.between(LocalDateTime.now(), subscription.getExpireDate());

        System.out.println(numOfDays);
        Long remainingAmount = (subscription.getTotalFlow() == null ? 0 : subscription.getTotalFlow()) - (subscription.getTotalUsed() == null ? 0 : subscription.getTotalUsed());
        double remainingAmountGB = new Helper().byteToGB(remainingAmount);
        DecimalFormat df = new DecimalFormat("#.##");
        String formatted = df.format(remainingAmountGB);

        String name = clientEntity.getInbound().getRemark() + "-" + subscription.getTitle() + "-" + formatted + "-GB-" + numOfDays + "-Days";
        return name;

    }
}

