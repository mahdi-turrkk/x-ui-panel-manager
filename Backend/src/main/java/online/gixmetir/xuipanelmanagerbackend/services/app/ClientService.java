package online.gixmetir.xuipanelmanagerbackend.services.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.gixmetir.xuipanelmanagerbackend.entities.ClientEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.InboundEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.ServerEntity;
import online.gixmetir.xuipanelmanagerbackend.entities.SubscriptionEntity;
import online.gixmetir.xuipanelmanagerbackend.models.DeviceValidationModel;
import online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.*;
import online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom.FragmentConfiguration;
import online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom.V2rayJsonConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ClientService {
    @Value("${app.url}")
    private String appUrl;

    public String generateClientString(ClientEntity client, DeviceValidationModel deviceValidationModel, boolean includeFragment) throws IOException {
        String link = "";
        switch (client.getInbound().getProtocol()) {
            case "vmess":
                link = generateVmessLink(client, deviceValidationModel, includeFragment);
                break;
            case "vless":
                link = generateVlessLink(client, deviceValidationModel, includeFragment);
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

    private String generateVlessLink(ClientEntity client, DeviceValidationModel deviceValidationModel, boolean includeFragment) throws IOException {
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
                if (deviceValidationModel.isApplyFragment() && includeFragment) {
                    values.put("fragment", "10-20,10-20,tlshello");
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
                if (deviceValidationModel.isApplyFragment() && includeFragment) {
                    values.put("fragment", "10-20,10-20,tlshello");
                }
            }
        }
        String sni = "";
        switch (inboundStreamSettings.getSecurity()) {
            case "tls" -> {
                values.put("security", "tls");
                values.put("fp", inboundStreamSettings.getTlsSettings().getSettings().getFingerprint());
                values.put("alpn", URLEncoder.encode(String.join(",", inboundStreamSettings.getTlsSettings().getAlpn()), StandardCharsets.UTF_8));
                if (inboundStreamSettings.getTlsSettings().getSettings().isAllowInsecure()) {
                    values.put("allowInsecure", "1");
                }
                if (inboundStreamSettings.getExternalProxy() != null && inboundStreamSettings.getExternalProxy().length > 0) {
                    address = inboundStreamSettings.getExternalProxy()[0].getDest();
                } else if (!inboundStreamSettings.getTlsSettings().getServerName().isEmpty()) {
                    address = inboundStreamSettings.getTlsSettings().getServerName();
                }
                if (inboundStreamSettings.getTlsSettings().getSettings().getServerName() != null && !inboundStreamSettings.getTlsSettings().getSettings().getServerName().isEmpty()) {
                    sni = inboundStreamSettings.getTlsSettings().getSettings().getServerName();
                    values.put("sni", sni);
                } else {
                    sni = inboundStreamSettings.getTlsSettings().getServerName();
                    values.put("sni", sni);
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
        if (inbound.getStreamSettingsObj().getExternalProxy().length > 0) {
            StringBuilder link = new StringBuilder();
            for (ExternalProxy proxy : inbound.getStreamSettingsObj().getExternalProxy())
                link.append(getVlessLink(client, values, uuid, inbound, proxy.getDest(), proxy.getPort())).append("\r\n");
            return link.toString();
        } else {
            return getVlessLink(client, values, uuid, inbound, address, inbound.getPort());
        }

    }

    private String getVlessLink(ClientEntity client, Map<String, String> values, String uuid, InboundEntity inbound, String address, String port) {
        String link = inbound.getProtocol() + "://" + uuid + "@" + address + ":" + port;
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

    public String generateFragmentLink(ClientEntity client) {
        return appUrl + "/api/v1/subscriptions/frag/" + client.getUuid();
    }

    private String generateVmessLink(ClientEntity client, DeviceValidationModel deviceValidationModel, boolean includeFragment) throws IOException {
        InboundEntity inbound = client.getInbound();
        StreamSettings streamSettings = inbound.getStreamSettingsObj();
        StringBuilder config = new StringBuilder();
        for (ExternalProxy proxy : streamSettings.getExternalProxy()) {
            config.append(generateVmessLinkCore(
                    client,
                    client.getInbound(),
                    deviceValidationModel,
                    includeFragment,
                    proxy.getPort(),
                    proxy.getDest(),
                    streamSettings
            )).append("\r\n");
        }


        return config.toString();
    }

    private String generateVmessLinkCore(
            ClientEntity client,
            InboundEntity inbound,
            DeviceValidationModel deviceValidationModel,
            boolean includeFragment,
            String port,
            String add,
            StreamSettings streamSettings
    ) throws IOException {
        ServerEntity server = client.getInbound().getServer();

        Map<String, Object> obj = new HashMap<>();
        obj.put("v", "2");
        if (port.isBlank()) {
            if (streamSettings.getTlsSettings().getServerName() != null && !streamSettings.getTlsSettings().getServerName().isEmpty())
                obj.put("add", streamSettings.getTlsSettings().getServerName());
            else
                obj.put("add", server.getUrl());
        } else {
            obj.put("add", add);
        }
        if (port.isBlank()) {
            obj.put("port", inbound.getPort());

        } else {
            obj.put("port", port);

        }

        obj.put("type", "none");
        String network = inbound.getStreamSettingsObj().getNetwork();
        obj.put("net", network);
        switch (network) {
            case "tcp":
                TcpSettings tcpSettings = inbound.getStreamSettingsObj().getTcpSettings();
                String typeStr = tcpSettings.getHeader().getType();
                obj.put("type", typeStr);
                if (typeStr.equals("http")) {
                    TcpRequest request = tcpSettings.getHeader().getRequest();
                    obj.put("path", request.getPath());
                    obj.put("host", request.getHeaders().getHost());
                }
                break;
            case "kcp":
                KcpSettings kcpSettings = inbound.getStreamSettingsObj().getKcpSettings();
                obj.put("type", kcpSettings.getHeader().getType());
                obj.put("path", kcpSettings.getSeed());
                break;
            case "ws":
                WsSettings wsSettings = inbound.getStreamSettingsObj().getWsSettings();
                obj.put("path", wsSettings.getPath());
                obj.put("host", wsSettings.getHeaders().getHost());
                if (deviceValidationModel.isApplyFragment() && includeFragment) {
                    obj.put("fragment", "10-20,10-20,tlshello");
                }
                break;
            case "http":
                HttpSettings httpSettings = inbound.getStreamSettingsObj().getHttpSettings();
                obj.put("net", "h2");
                obj.put("path", httpSettings.getPath());
                obj.put("host", httpSettings.getHost().get(0) != null ? httpSettings.getHost().get(0) : "");
                break;
            case "quic":
                QuicSettings quicSettings = inbound.getStreamSettingsObj().getQuicSettings();
                obj.put("type", quicSettings.getHeader().getType());
                obj.put("host", quicSettings.getSecurity());
                obj.put("path", quicSettings.getKey());
                break;
            case "grpc":
                GrpcSettings grpcSettings = inbound.getStreamSettingsObj().getGrpcSettings();
                obj.put("path", grpcSettings.getServiceName());
                if (grpcSettings.isMultiMode()) {
                    obj.put("type", "multi");
                }
                if (deviceValidationModel.isApplyFragment() && includeFragment) {
                    obj.put("fragment", "10-20,10-20,tlshello");
                }
                break;
        }
        String security = inbound.getStreamSettingsObj().getSecurity();
        List<String> domains = new ArrayList<>();
        obj.put("tls", security);
        if (security.equals("tls")) {
            TlsSettings tlsSettings = inbound.getStreamSettingsObj().getTlsSettings();
            List<String> alpns = Arrays.stream(tlsSettings.getAlpn()).toList();
            if (!alpns.isEmpty()) {
                List<String> alpn = new ArrayList<>(alpns);
                obj.put("alpn", String.join(",", alpn));
            }
            TlsSettingsInner tlsSettingsInner = tlsSettings.getSettings();
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
        }
        obj.put("id", client.getUuid());
        if (!domains.isEmpty()) {
            StringBuilder links = new StringBuilder();
            for (int i = 0; i < domains.size(); i++) {
                obj.put("ps", generateClientName(client));
                obj.put("add", domains.get(i));
                if (i > 0) {
                    links.append("\n");
                }
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(obj);
                links.append("vmess://").append(Base64.getEncoder().encodeToString(json.getBytes()));
            }
            return links.toString();
        }
        obj.put("ps", generateClientName(client));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(obj);
        return "vmess://" + Base64.getEncoder().encodeToString(json.getBytes());
    }

    private String generateClientName(ClientEntity clientEntity) {
        SubscriptionEntity subscription = clientEntity.getSubscription();
        String name = clientEntity.getInbound().getRemark() + "-" + subscription.getTitle();
        return name;
    }

    public Object generateClientJson(ClientEntity client, DeviceValidationModel deviceValidationModel, boolean includeFragment) throws IOException {
        switch (client.getInbound().getProtocol()) {
            case "vmess":
                return createVmessJsonObj(client, deviceValidationModel, includeFragment);
            case "vless":
                return createVlessJsonObj(client, deviceValidationModel, includeFragment);
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
        return null;
    }

    public Object createVmessJsonObj(ClientEntity client, DeviceValidationModel deviceValidationModel, boolean includeFragment) throws IOException {
        InboundEntity inbound = client.getInbound();
        ServerEntity server = client.getInbound().getServer();
        String address;
        if (inbound.getStreamSettingsObj().getTlsSettings().getServerName() != null && !inbound.getStreamSettingsObj().getTlsSettings().getServerName().isEmpty())
            address = inbound.getStreamSettingsObj().getTlsSettings().getServerName();
        else
            address = server.getUrl();
        String security = inbound.getStreamSettingsObj().getSecurity();
        List<String> domains = new ArrayList<>();
        String sni = "";
        if (security.equals("tls")) {
            TlsSettings tlsSettings = inbound.getStreamSettingsObj().getTlsSettings();
            TlsSettingsInner tlsSettingsInner = tlsSettings.getSettings();
            if (tlsSettingsInner != null) {
                if (tlsSettingsInner.getServerName() != null) {
                    sni = tlsSettingsInner.getServerName();
                }
                if (tlsSettingsInner.getDomains() != null) {
                    domains = Arrays.stream(tlsSettingsInner.getDomains()).toList();
                }
            }
        }
        if (!domains.isEmpty()) {
            for (String domain : domains) {
                address = domain;
            }
        }
        String path = "";
        String network = client.getInbound().getStreamSettingsObj().getNetwork();
        switch (network) {
            case "tcp":
                TcpSettings tcpSettings = inbound.getStreamSettingsObj().getTcpSettings();
                String typeStr = tcpSettings.getHeader().getType();
                if (typeStr.equals("http")) {
                    TcpRequest request = tcpSettings.getHeader().getRequest();
                    path = request.getPath();
                }
                break;
            case "kcp":
                KcpSettings kcpSettings = inbound.getStreamSettingsObj().getKcpSettings();
                path = kcpSettings.getSeed();
                break;
            case "ws":
                WsSettings wsSettings = inbound.getStreamSettingsObj().getWsSettings();
                path = wsSettings.getPath();
                break;
            case "http":
                HttpSettings httpSettings = inbound.getStreamSettingsObj().getHttpSettings();
                path = httpSettings.getPath();
                break;
            case "quic":
                QuicSettings quicSettings = inbound.getStreamSettingsObj().getQuicSettings();
                path = quicSettings.getKey();
                break;
            case "grpc":
                GrpcSettings grpcSettings = inbound.getStreamSettingsObj().getGrpcSettings();
                path = grpcSettings.getServiceName();
                break;
        }
        int port = Integer.parseInt(inbound.getPort());
        if (includeFragment) {
            return new FragmentConfiguration("vmess", port, address, client.getUuid(), path, sni, network, deviceValidationModel);
        } else {
            return new V2rayJsonConfiguration("vmess", port, address, client.getUuid(), sni, sni, network, inbound.getStreamSettingsObj().getGrpcSettings().isMultiMode(), inbound.getStreamSettingsObj().getTlsSettings().getSettings().isAllowInsecure(), Arrays.stream(inbound.getStreamSettingsObj().getTlsSettings().getAlpn()).toList());
        }
    }

    public Object createVlessJsonObj(ClientEntity client, DeviceValidationModel deviceValidationModel, boolean includeFragment) throws IOException {
        String uuid = String.valueOf(client.getUuid());
        InboundEntity inbound = client.getInbound();
        String address = URI.create(inbound.getServer().getUrl()).getHost();
        if (inbound.getListen() != null && !inbound.getListen().equals("0.0.0.0")) {
            address = inbound.getListen();
        }
        StreamSettings inboundStreamSettings = inbound.getStreamSettingsObj();
        String sni = "";
        if (inboundStreamSettings.getSecurity().equals("tls")) {
            if (inboundStreamSettings.getExternalProxy() != null && inboundStreamSettings.getExternalProxy().length > 0) {
                address = inboundStreamSettings.getExternalProxy()[0].getDest();
            } else if (!inboundStreamSettings.getTlsSettings().getServerName().isEmpty()) {
                address = inboundStreamSettings.getTlsSettings().getServerName();
            }
            if (inboundStreamSettings.getTlsSettings().getSettings().getServerName() != null && !inboundStreamSettings.getTlsSettings().getSettings().getServerName().isEmpty()) {
                sni = inboundStreamSettings.getTlsSettings().getSettings().getServerName();
            } else {
                sni = inboundStreamSettings.getTlsSettings().getServerName();
            }
        }
        String path = "";
        String network = inboundStreamSettings.getNetwork();
        switch (network) {
            case "tcp" -> {
                TcpSettings tcp = inboundStreamSettings.getTcpSettings();
                if (tcp.getHeader().getType().equals("http")) {
                    TcpRequest request = tcp.getHeader().getRequest();
                    path = request.getPath();
                }
            }
            case "ws" -> {
                WsSettings ws = inboundStreamSettings.getWsSettings();
                path = ws.getPath();
            }
            case "http" -> {
                HttpSettings http = inboundStreamSettings.getHttpSettings();
                path = http.getPath();
            }
        }
        int port = Integer.parseInt(inbound.getPort());
        if (includeFragment) {
            return new FragmentConfiguration("vless", port, address, uuid, path, sni, network, deviceValidationModel);
        } else {
            boolean multiMode = inbound.getStreamSettingsObj().getGrpcSettings() != null && inbound.getStreamSettingsObj().getGrpcSettings().isMultiMode();
            return new V2rayJsonConfiguration("vless", port, address, uuid, sni, sni, network, multiMode, inbound.getStreamSettingsObj().getTlsSettings().getSettings().isAllowInsecure(), Arrays.stream(inbound.getStreamSettingsObj().getTlsSettings().getAlpn()).toList());
        }
    }
}

