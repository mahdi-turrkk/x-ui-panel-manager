package online.gixmetir.xuipanelmanagerbackend.models.configgenerationmodels.jsom;


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
    public List<FragmentInbound> inbounds;
    public List<Outbound> outbounds;
    public Routing routing;

    public FragmentConfiguration(String protocol, int port, String address, String uuid, String path, String sni, String network, DeviceValidationModel deviceValidationModel) {
        boolean applyFragment = false;
        if (deviceValidationModel != null)
            applyFragment = deviceValidationModel.isApplyFragment();
        this.log = Log.builder()
                .access("")
                .error("")
                .loglevel("warning")
                .build();
        this.inbounds = List.of(
                FragmentInbound.builder()
                        .tag("socks")
                        .port(10808)
                        .listen("127.0.0.1")
                        .protocol("socks")
                        .sniffing(Sniffing.builder()
                                .enabled(true)
                                .destOverride(List.of("http", "tls"))
                                .routeOnly(false)
                                .build())
                        .settings(FragmentInboundSettings.builder()
                                .auth("noauth")
                                .udp(true)
                                .allowTransparent(false)
                                .build())
                        .build()
                ,
                FragmentInbound.builder()
                        .tag("http")
                        .port(10809)
                        .listen("127.0.0.1")
                        .protocol("http")
                        .sniffing(Sniffing.builder()
                                .enabled(true)
                                .destOverride(List.of("http", "tls"))
                                .routeOnly(false)
                                .build())
                        .settings(FragmentInboundSettings.builder()
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
                                        OutboundServer.builder()
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
                        .streamSettings(StreamSettings.builder()
                                .network(network)
                                .security("tls")
                                .tlsSettings(TlsSettings.builder()
                                        .allowInsecure(true)
                                        .serverName(sni)
                                        .alpn(List.of("h2", "http/1.1"))
                                        .fingerprint("chrome")
                                        .show(true)
                                        .build())
                                .wsSettings(network.equals("ws") ? WsSettings.builder()
                                        .path((path.isEmpty() ? "/" : path) + "/?ed=2048")
                                        .headers(Headers.builder()
                                                .Host(sni)
                                                .build())
                                        .build() : null)
                                .grpcSettings(network.equals("grpc") ?
                                        GrpcSettings.builder()
                                                .multiMode(false)
                                                .serviceName("")
                                                .build() : null
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
//                                .xudpConcurrency(-1)
//                                .xudpProxyUDP443("reject")
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
                        .streamSettings(StreamSettings.builder()
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






