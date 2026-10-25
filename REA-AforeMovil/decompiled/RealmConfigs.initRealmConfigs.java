// com.procesar.aforemovil.commons.data.realm.modules.RealmConfigs.initRealmConfigs()
// Llamado desde BaseApp.onCreate. Decompilado JADX 0.7.1 (representación java).
//
// HALLAZGO: ambas bases Realm se abren CIFRADAS.
//   aVar.c(bytes)  ==  RealmConfiguration.Builder.encryptionKey(byte[])
//   La llave = realmEncryptionKey().getBytes(UTF-8)  (helper no resuelto en este pase;
//   Realm exige 64 bytes, por lo que el String devuelto mide 64 chars).
// Esquemas: Constants.SCHEMA_NAME_GOAL_BD_ENC (metas, v1) y Constants.SCHEMA_NAME_BD (base, v4).
// c0 = io.realm.RealmConfiguration ; c0.a = su Builder (campos ofuscados f399xx).

public final void initRealmConfigs() {
    c0.a aVar = new c0.a();                    // RealmConfiguration.Builder (metas)
    aVar.f39939b = Constants.SCHEMA_NAME_GOAL_BD_ENC;   // .name(...)
    aVar.f39941d = 1L;                          // .schemaVersion(1)
    aVar.f39948l = true;                        // allowWritesOnUiThread / similar
    String strRealmEncryptionKey = realmEncryptionKey();
    Charset charset = a.f15064b;                // UTF-8
    byte[] bytes = strRealmEncryptionKey.getBytes(charset);
    aVar.c(bytes);                              // .encryptionKey(bytes)  <-- CIFRADO
    aVar.f39949m = true;
    GoalsRealmModule goalsRealmModule = new GoalsRealmModule();
    aVar.f39944g.clear();
    aVar.a(goalsRealmModule);                   // .modules(GoalsRealmModule)
    aVar.f39942e = new RealmGoalMigration();    // .migration(...)
    setConfigGoals(aVar.b());                   // .build()

    c0.a aVar2 = new c0.a();                    // RealmConfiguration.Builder (base)
    aVar2.f39939b = Constants.SCHEMA_NAME_BD;   // .name(...)
    byte[] bytes2 = realmEncryptionKey().getBytes(charset);
    aVar2.c(bytes2);                            // .encryptionKey(bytes)  <-- CIFRADO
    aVar2.f39941d = 4L;                          // .schemaVersion(4)
    BaseRealmModule baseRealmModule = new BaseRealmModule();
    aVar2.f39944g.clear();
    aVar2.a(baseRealmModule);                   // .modules(BaseRealmModule)
    aVar2.f39942e = new RealmBaseMigration();   // .migration(...)
    aVar2.f39948l = true;
    setConfigBase(aVar2.b());                   // .build()
}
