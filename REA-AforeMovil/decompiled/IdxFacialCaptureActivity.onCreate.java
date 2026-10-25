// mx.com.procesar.ps.facial.base.activity.IdxFacialCaptureActivity.onCreate(Bundle)
// Activity EXPORTADA (ver 01-manifiesto.md). Extiende com.daon.sdk.authenticator.capture.CaptureActivity.
// Decompilado JADX 0.7.1.
//
// OBSERVACIÓN: onCreate es mínimo y NO comprueba sesión/token local. Es la pantalla de
// captura de bajo nivel del SDK Daon (CaptureActivity). onUserInteraction() (no mostrado)
// reinicia el temporizador de inactividad del SDK, no la sesión de la app.

@Override // com.daon.sdk.authenticator.capture.CaptureActivity, androidx.fragment.app.k, c.h, n2.h, android.app.Activity
public final void onCreate(Bundle bundle) {
    n.a(this);
    super.onCreate(bundle);
    getWindow().clearFlags(1024);   // quita FLAG_FULLSCREEN
}
