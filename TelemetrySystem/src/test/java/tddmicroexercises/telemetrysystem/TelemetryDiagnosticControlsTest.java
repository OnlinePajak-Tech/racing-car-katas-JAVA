package tddmicroexercises.telemetrysystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

@ExtendWith(MockitoExtension.class)
public class TelemetryDiagnosticControlsTest {
    @Mock
    private TelemetryClient telemetryClient;
    @InjectMocks
    private TelemetryDiagnosticControls telemetryDiagnosticControls;

    @Test
    public void CheckTransmission_should_send_a_diagnostic_message_and_receive_a_status_message_response() throws Exception {
        //When
        telemetryDiagnosticControls.checkTransmission();
        //Then
        Mockito.verify(telemetryClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
    }
}