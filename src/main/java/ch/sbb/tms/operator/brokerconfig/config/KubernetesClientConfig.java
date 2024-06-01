package ch.sbb.tms.operator.brokerconfig.config;

import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@ConfigurationProperties("kubernetes")
@Setter
public class KubernetesClientConfig {

	private String url;
	private String namespace;
	private String authToken;

	public static String KUB_CA_CERT_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt";
	private static String KUB_AUTH_TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";
	private static String KUB_AUTH_NAMESPACE_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/namespace";
	private static String KUB_INTERNAL_HOSTNAME = "kubernetes.default.svc.cluster.local";

	public boolean isConfiguredOrLocalCluster() {
		return (isConfigured() || isLocalCluster());
	}

	public boolean isConfigured() {
		return Strings.isNotEmpty(url) && Strings.isNotEmpty(namespace) && Strings.isNotEmpty(authToken);
	}

	private boolean isLocalCluster() {
		return new File(KUB_AUTH_TOKEN_FILE).canRead();
	}

	public String getUrl() {
		return !Strings.isEmpty(url) ? url : KUB_INTERNAL_HOSTNAME;
	}

	public String getNamespace() throws IOException {
		return !Strings.isEmpty(namespace) ? namespace : getFileContent(KUB_AUTH_NAMESPACE_FILE);
	}

	public String getAuthToken() throws IOException {
		return !Strings.isEmpty(authToken) ? authToken : getFileContent(KUB_AUTH_TOKEN_FILE);
	}

	private String getFileContent(String filename) throws IOException {
		return new String(Files.readAllBytes(Paths.get(filename).toRealPath()), StandardCharsets.UTF_8);
	}
}
