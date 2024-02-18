package online.gixmetir.xuipanelmanagerbackend.models.ConfigGenerationModels;


import lombok.*;
import online.gixmetir.xuipanelmanagerbackend.models.DeviceValidationModel;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FragmentConfiguration {
    public Log log;
    public List<Inbound> inbounds;
    public List<Outbound> outbounds;
    public Routing routing;

    public FragmentConfiguration(String protocol, int port, String address, String uuid, String path, String sni, String network, DeviceValidationModel deviceValidationModel) {
//        FragmentConfiguration configuration = new FragmentConfiguration();
        boolean applyFragment = false;
        if (deviceValidationModel != null)
            applyFragment = deviceValidationModel.isApplyFragment();
        this.log = Log.builder()
                .access("")
                .error("")
                .loglevel("warning")
                .build();
        this.inbounds = List.of(
                Inbound.builder()
                        .tag("socks")
                        .port(10808)
                        .listen("127.0.0.1")
                        .protocol("socks")
                        .sniffing(Sniffing1.builder()
                                .enabled(true)
                                .destOverride(List.of("http", "tls"))
                                .routeOnly(false)
                                .build())
                        .settings(InboundSettings.builder()
                                .auth("noauth")
                                .udp(true)
                                .allowTransparent(false)
                                .build())
                        .build()
                ,
                Inbound.builder()
                        .tag("http")
                        .port(10809)
                        .listen("127.0.0.1")
                        .protocol("http")
                        .sniffing(Sniffing1.builder()
                                .enabled(true)
                                .destOverride(List.of("http", "tls"))
                                .routeOnly(false)
                                .build())
                        .settings(InboundSettings.builder()
                                .auth("noauth")
                                .udp(true)
                                .allowTransparent(false)
                                .build())
                        .build()
        );
        this.outbounds = List.of(
                Outbound.builder()
                        .tag("proxy")
                        .protocol(protocol)
                        .settings(OutboundSettings.builder()
                                .vnext(List.of(
                                        Vnext.builder()
                                                .address(address)
                                                .port(port)
                                                .users(List.of(
                                                        User.builder()
                                                                .id(uuid)
                                                                .alterId(0)
                                                                .email("t@t.tt")
                                                                .security("auto")
                                                                .encryption("none")
                                                                .flow("")
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .servers(List.of(
                                        OutBoundServer.builder()
                                                .address(address)
                                                .level(1)
                                                .flow("")
                                                .method("chacha20-poly1305")
                                                .ota(false)
                                                .password(uuid)
                                                .port(port)
                                                .build()
                                ))
                                .build())
                        .streamSettings(StreamSettings1.builder()
                                .network(network)
                                .security("tls")
                                .tlsSettings(TlsSettings1.builder()
                                        .allowInsecure(true)
                                        .serverName(sni)
                                        .alpn(List.of("h2", "http/1.1"))
                                        .fingerprint("chrome")
                                        .show(true)
                                        .build())
                                .wsSettings(WsSettings1.builder()
                                        .path(path + "/?ed=2048")
                                        .headers(Headers1.builder()
                                                .Host(sni)
                                                .build())
                                        .build())
                                .grpcSettings(
                                        GrpcSettings1.builder()
                                                .multiMode(false)
                                                .serviceName("")
                                                .build()
                                )
                                .sockopt(SocketOptions.builder()
                                        .dialerProxy("fragment")
                                        .tcpKeepAliveIdle(100)
                                        .mark(255)
                                        .tcpNoDelay(true)
                                        .build())
                                .build())
                        .mux(Mux.builder()
                                .enabled(false)
                                .concurrency(-1)
                                .xudpConcurrency(-1)
                                .xudpProxyUDP443("reject")
                                .build())
                        .build(),
                Outbound.builder()
                        .tag("fragment")
                        .protocol("freedom")
                        .settings(OutboundSettings.builder()
                                .domainStrategy("AsIs")
                                .fragment(applyFragment ?
                                        FragmentModel.builder()
                                                .packets(deviceValidationModel.getPackets())
                                                .interval(deviceValidationModel.getFragmentInterval())
                                                .length(deviceValidationModel.getFragmentLength())
                                                .build()
                                        : null
                                )
                                .build())
                        .streamSettings(StreamSettings1.builder()
                                .sockopt(SocketOptions.builder()
                                        .tcpNoDelay(true)
                                        .tcpKeepAliveIdle(100)
                                        .build())
                                .build())
                        .build(),
                Outbound.builder()
                        .tag("direct")
                        .protocol("freedom")
                        .settings(OutboundSettings.builder().build())
                        .build()
                , Outbound.builder()
                        .tag("block")
                        .protocol("blackhole")
                        .settings(OutboundSettings.builder()
                                .response(Response.builder()
                                        .type("http")
                                        .build())
                                .build())
                        .build()
        );
        this.routing = Routing.builder()
                .domainStrategy("AsIs")
                .rules(List.of(
                        Rule.builder()
                                .type("field")
                                .inboundTag(List.of("api"))
                                .outboundTag("api")
                                .enabled(true)
                                .build(),
                        Rule.builder()
                                .id("5465425548310166497")
                                .type("field")
                                .outboundTag("direct")
                                .domain(List.of("domain:ir", "geosite:cn"))
                                .enabled(true)
                                .build(),
                        Rule.builder()
                                .id("5425034033205580637")
                                .type("field")
                                .outboundTag("direct")
                                .ip(List.of("geoip:private", "geoip:cn", "geoip:ir"))
                                .enabled(true)
                                .build(),
                        Rule.builder()
                                .id("5627785659655799759")
                                .type("field")
                                .port("0-65535")
                                .outboundTag("proxy")
                                .enabled(true)
                                .build()
                ))
                .build();
    }
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Log {
    public String access;
    public String error;
    public String loglevel;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Inbound {
    public String tag;
    public int port;
    public String listen;
    public String protocol;
    public Sniffing1 sniffing;
    public InboundSettings settings;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Sniffing1 {
    public boolean enabled;
    public List<String> destOverride;
    public boolean routeOnly;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class InboundSettings {
    public String auth;
    public boolean udp;
    public boolean allowTransparent;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Outbound {
    public String tag;
    public String protocol;
    public OutboundSettings settings;
    public StreamSettings1 streamSettings;
    public Mux mux;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class OutboundSettings {
    public List<Vnext> vnext;
    public String domainStrategy;
    public FragmentModel fragment;
    public Response response;
    public List<OutBoundServer> servers;

}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class OutBoundServer {
    public String address;
    public int level;
    public String flow;
    public String method;
    public boolean ota;
    public String password;
    public int port;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Response {
    public String type;
}


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class FragmentModel {
    public String packets;
    public String length;
    public String interval;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Vnext {
    public String address;
    public int port;
    public List<User> users;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class User {
    public String id;
    public int alterId;
    public String email;
    public String security;
    public String encryption;
    public String flow;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class StreamSettings1 {
    public String network;
    public String security;
    public TlsSettings1 tlsSettings;
    public WsSettings1 wsSettings;
    public GrpcSettings1 grpcSettings;
    public SocketOptions sockopt;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class TlsSettings1 {
    public boolean allowInsecure;
    public String serverName;
    public List<String> alpn;
    public String fingerprint;
    public boolean show;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class WsSettings1 {
    public String path;
    public Headers1 headers;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class GrpcSettings1 {
    public boolean multiMode;
    public String serviceName;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Headers1 {
    public String Host;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class SocketOptions {
    public String dialerProxy;
    public int tcpKeepAliveIdle;
    public int mark;
    public boolean tcpNoDelay;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Mux {
    public boolean enabled;
    public int concurrency;
    public int xudpConcurrency;
    public String xudpProxyUDP443;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Routing {
    public String domainStrategy;
    public List<Rule> rules;
}

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Rule {
    public String id;
    public String type;
    public List<String> inboundTag;
    public String outboundTag;
    public boolean enabled;
    public List<String> domain;
    public List<String> ip;
    public String port;
}