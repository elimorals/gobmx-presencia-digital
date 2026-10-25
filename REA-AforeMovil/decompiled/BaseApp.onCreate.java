// com.procesar.aforemovil.BaseApp — application class (android:name en el manifiesto)
// Decompilado con REA 6.x / JADX headless 0.7.1. Representación: java (sin fallback a smali).
// Extiende W8.l (clase ofuscada R8) que a su vez extiende android.app.Application e
// implementa androidx.lifecycle.DefaultLifecycleObserver (callbacks d/onStart/onStop…).
//
// Observaciones:
//  - Callback.onCreate(this): shim de instrumentacion de Dynatrace OneAgent (APM) inyectado
//    en bytecode. Ver 05-red-endpoints.md y 08-sdks-terceros.md.
//  - FirebaseCrashlytics collection habilitado de forma incondicional en el arranque.
//  - FirebaseMetrics.INSTANCE.init(this): wrapper propio de metricas sobre Firebase.
//  - V.u(this) (clase ofuscada, singleton sincronizado): inicializacion de un subsistema
//    propio (candidato a DI / red). No se resolvio su contenido en este pase.
//  - RealmConfigs.INSTANCE.initRealmConfigs(): inicializa Realm (ver 04-cripto-almacenamiento.md).
//  - DataStoreManager.INSTANCE.handleMigration(this): migracion de Jetpack DataStore.

@Override // W8.l, android.app.Application
public final void onCreate() {
    Callback.onCreate(this);
    super.onCreate();
    B.i.f23276f.a(this);
    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
    FirebaseMetrics.INSTANCE.init(this);
    Object obj = V.f39885l;
    synchronized (V.class) {
        V.u(this);
    }
    RealmConfigs.INSTANCE.initRealmConfigs();
    DataStoreManager.INSTANCE.handleMigration(this);
}
