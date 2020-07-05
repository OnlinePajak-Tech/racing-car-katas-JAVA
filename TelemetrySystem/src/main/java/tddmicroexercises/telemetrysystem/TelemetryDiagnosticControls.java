package tddmicroexercises.telemetrysystem;

public class TelemetryDiagnosticControls {
    private static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

    private final Client telemetryClient;
    private String diagnosticInfo = "";

    public TelemetryDiagnosticControls() {
        this(new TelemetryClient());
    }

    public TelemetryDiagnosticControls(Client client) {
        telemetryClient = client;
    }

    public String getDiagnosticInfo() {
        return diagnosticInfo;
    }

    public void setDiagnosticInfo(String diagnosticInfo) {
        this.diagnosticInfo = diagnosticInfo;
    }

    public void checkTransmission() throws Exception {
        diagnosticInfo = "";

        telemetryClient.disconnect();

        int retryLeft = 3;
        while (!telemetryClient.getOnlineStatus() && retryLeft > 0) {
            telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
            retryLeft -= 1;
        }

        if (!telemetryClient.getOnlineStatus()) {
            throw new Exception("Unable to connect.");
        }

        telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
        setDiagnosticInfo(telemetryClient.receive());
    }
}
