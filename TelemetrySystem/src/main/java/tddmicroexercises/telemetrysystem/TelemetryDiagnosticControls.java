package tddmicroexercises.telemetrysystem;

public class TelemetryDiagnosticControls {
    private static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

    private final ITelemetryClient telemetryClient;

    public TelemetryDiagnosticControls() {
        telemetryClient = new TelemetryClient();
    }

    public TelemetryDiagnosticControls(ITelemetryClient telemetryClient) {
        this.telemetryClient = telemetryClient;
    }

    public DiagnosticInfo checkTransmission() throws ConnectionException {
        telemetryClient.disconnect();

        int retryLeft = 3;
        while (!telemetryClient.getOnlineStatus() && retryLeft > 0) {
            telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
            retryLeft -= 1;
        }

        if (!telemetryClient.getOnlineStatus()) {
            throw new ConnectionException("Unable to connect.");
        }

        telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);

        return new DiagnosticInfo(telemetryClient.receive());
    }
}
