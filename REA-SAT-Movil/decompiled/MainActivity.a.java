// mx.gob.sat.MainActivity — JADX headless 0.7.1 (vía REA)
// APK base mx.gob.sat.apk · sha256 ab3fcd6df84d1d45ce62fd9c5ceb79349e8d8b0958604f3d7e6fb92511e39939
//
// MainActivity extiende FlutterActivity. R8 minimizó nombres: la clase solo tiene
// 2 métodos (<init> y a()). a() es el override de configureFlutterEngine(FlutterEngine):
// registra los plugins (f.x(cVar)) y crea UN MethodChannel propio cuyo nombre vive en
// el campo f12552u (String). Toda la lógica de negocio está en el snapshot Dart
// (libapp.so), no en el DEX. Esto es un embedding Flutter estándar, nada propio del SAT.

@Override // e5.e  (io.flutter.embedding.android.FlutterActivity.configureFlutterEngine)
public final void a(c cVar) {              // cVar = FlutterEngine
    g.e(cVar, "flutterEngine");
    j jVar = this.f9322s;
    if (jVar == null || !jVar.f9318p0.f9291g) {
        f.x(cVar);                          // GeneratedPluginRegistrant.registerWith(engine)
    }
    new s((Y.f) cVar.f9615c.f9828x,         // MethodChannel(binaryMessenger,
           this.f12552u)                    //   name = f12552u)
        .b(new a(this, 16));                // setMethodCallHandler(...)
}
