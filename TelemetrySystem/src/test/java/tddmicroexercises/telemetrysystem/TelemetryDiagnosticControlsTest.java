package tddmicroexercises.telemetrysystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TelemetryDiagnosticControlsTest {

    @Test
    public void checkTransmission_should_send_a_diagnostic_message_and_receive_a_status_message_response() throws Exception {
        // Given
        var mockTelemetryClient = Mockito.mock(TelemetryClient.class);

        var expectedDiagnosticMessage = "Blah";

        when(mockTelemetryClient.getOnlineStatus()).thenReturn(true);
        when(mockTelemetryClient.receive()).thenReturn(expectedDiagnosticMessage);
        doNothing().when(mockTelemetryClient).disconnect();

        var control = new TelemetryDiagnosticControls(mockTelemetryClient);

        // When
        control.checkTransmission();

        // Then
        Assertions.assertEquals(expectedDiagnosticMessage, control.getDiagnosticInfo());
        verify(mockTelemetryClient).send("AT#UD");
    }

    @Test
    public void checkTransmission_should_throw_an_exception_when_status_is_not_online_after_retry() {
        // Given
        var mockTelemetryClient = Mockito.mock(TelemetryClient.class);

        when(mockTelemetryClient.getOnlineStatus()).thenReturn(false);
        doNothing().when(mockTelemetryClient).disconnect();

        var control = new TelemetryDiagnosticControls(mockTelemetryClient);

        // When / Then
        assertThrows(ConnectionException.class, () -> control.checkTransmission(), "Unable to connect.");
    }

    @Test
    public void checkTransmission_should_disconnect_then_reconnect_when_online_status_is_false() throws Exception {
        // Given
        var mockTelemetryClient = Mockito.mock(TelemetryClient.class);

        doNothing().when(mockTelemetryClient).disconnect();
        when(mockTelemetryClient.receive()).thenReturn("expectedDiagnosticMessage");
        doReturn(false)
                .doReturn(false)
                .doReturn(true)
                .when(mockTelemetryClient)
                .getOnlineStatus();
        var control = new TelemetryDiagnosticControls(mockTelemetryClient);

        // When
        control.checkTransmission();

        // Then
        verify(mockTelemetryClient).disconnect();
        verify(mockTelemetryClient, times(2))
                .connect("*111#");
    }
}
