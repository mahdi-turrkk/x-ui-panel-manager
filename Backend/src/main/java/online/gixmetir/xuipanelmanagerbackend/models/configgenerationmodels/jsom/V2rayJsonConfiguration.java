package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;


import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class V2rayJsonConfiguration {
    public Dns dns;
    public List<Inbound> inbounds;
    public Log log;
    public List<Outbound> outbounds;
    public Routing routing;

    public V2rayJsonConfiguration(String protocol, int port, String address, String uuid, String host, String sni, String network, boolean multiMode, boolean allowInsecure, List<String> alpn) {
        this.dns =
                Dns.builder()
                        .hosts(Map.of("domain:googleapis.cn", "googleapis.com"))
                        .servers(List.of(
                                "8.8.8.8",
                                DnsServer.builder()
                                        .address("223.5.5.5")
                                        .domains(List.of("domain:ir"))
                                        .expectIPs(List.of("geoip:cn"))
//                                        .port(53L)
                                        .build()))
                        .build();
        this.inbounds =
                List.of(
                        Inbound.builder()
                                .port(10808)
                                .listen("127.0.0.1")
                                .protocol("socks")
                                .settings(Settings.builder()
                                        .auth("noauth")
                                        .udp(true)
//                                        .userLevel(8L)
                                        .build())
                                .sniffing(Sniffing.builder()
                                        .destOverride(List.of("http", "tls"))
                                        .enabled(true)
                                        .build())
                                .tag("socks")
                                .build(),
                        Inbound.builder()
                                .listen("127.0.0.1")
                                .port(10809)
                                .protocol("http")
                                .settings(Settings.builder()
//                                        .userLevel(8L)
                                        .build())
                                .tag("http")
                                .build());
        this.log = Log.builder()
                .loglevel("warning")
                .build();
        this.outbounds = List.of(
                Outbound.builder()
                        .mux(Mux.builder()
                                .enabled(false)
                                .concurrency(-1)
                                .xudpConcurrency(8)
                                .xudpProxyUDP443("")
                                .build())
                        .protocol(protocol)
                        .settings(OutboundSettings.builder()
                                .vnext(List.of(
                                        Vnext.builder()
                                                .address(address)
                                                .port(port)
                                                .users(List.of(
                                                        User.builder()
                                                                .encryption("none")
                                                                .flow("")
                                                                .id(uuid)
                                                                .level(8)
                                                                .security("auto")
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .build())
                        .streamSettings(StreamSettings.builder()
                                .grpcSettings(network.equals("grpc") ?
                                        GrpcSettings.builder()
                                                .multiMode(multiMode)
                                                .serviceName("")
                                                .build() : null
                                )
                                .wsSettings(network.equals("ws") ? WsSettings.builder()
                                        .path("/")
                                        .headers(Headers.builder()
                                                .Host(host)
                                                .build())
                                        .build() : null)
                                .network(network)
                                .security("tls")
                                .tlsSettings(TlsSettings.builder()
                                        .allowInsecure(allowInsecure)
                                        .alpn(alpn)
                                        .fingerprint("chrome")
                                        .publicKey("")
                                        .serverName(sni)
                                        .shortId("")
                                        .show(false)
                                        .spiderX("")
                                        .build())
                                .build())
                        .tag("proxy")
                        .build(),
                Outbound.builder()
                        .tag("direct")
                        .protocol("freedom")
                        .settings(OutboundSettings.builder().build())
                        .build(),
                Outbound.builder()
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
                .domainStrategy("IPIfNonMatch")
                .rules(List.of(
                        Rule.builder()
                                .ip(List.of("8.8.8.8"))
                                .outboundTag("proxy")
                                .port("53")
                                .type("field")
                                .build(),
                        Rule.builder()
                                .ip(List.of("223.5.5.5"))
                                .outboundTag("direct")
                                .port("53")
                                .type("field")
                                .build(),
                        Rule.builder()
                                .domain(List.of("domain:ir"))
                                .outboundTag("direct")
                                .type("field")
                                .build(),
                        Rule.builder()
                                .ip(List.of("geoip:ir"))
                                .outboundTag("direct")
                                .type("field")
                                .build()
                ))
                .build();
    }
}


