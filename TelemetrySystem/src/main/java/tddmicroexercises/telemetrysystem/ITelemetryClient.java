package tddmicroexercises.telemetrysystem;

public interface ITelemetryClient {
    void connect(String telemetryServerConnectionString);

    void disconnect();

    void send(String message);

    boolean getOnlineStatus();

    String receive();
}
