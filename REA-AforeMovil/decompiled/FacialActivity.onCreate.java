// mx.com.procesar.ps.facial.ui.main.FacialActivity.onCreate(Bundle)
// Activity EXPORTADA (ver 01-manifiesto.md). Decompilado JADX 0.7.1.
//
// OBSERVACIÓN sobre guard de sesión: onCreate NO comprueba sesión/token local antes de
// montar la UI. Arma la pila de identidad vía Dagger (DaggerWrapper) e inicializa FIDO.
// Los casos de uso llaman al backend IdentityX, que inyecta credenciales de usuario
// (idxfacial.services.di.NetworkModule_ProvideUserCredentialsFactory) — la protección real
// depende del backend/evaluación, no de un chequeo en esta pantalla.
//
// Casos de uso cableados (pipeline de alta/identidad):
//   RegisterRepository:  CreateRegistrationChallenge, UpdateRegistrationChallenge, SearchUser, ArchiveUser
//   AuthenticateUserRepository: CreateAuthenticationRequest, UpdateAuthenticationRequest
//   UserRepository: GetLifeTests, CreateIdCheck, SearchFace, UploadPhoto, CreateEvaluation
//   DocumentRepository: GetDocuments, UploadDocument, DocumentOcrData
//   + Fido.getInstance(getApplicationContext())  (Daon FIDO)

@Override // Zj.a, androidx.fragment.app.k, c.h, n2.h, android.app.Activity
public final void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    nk.a aVarA = DaggerWrapper.f44496a.a(this);
    RegisterRepository registerRepositoryC = aVarA.c();
    CreateRegistrationChallenge createRegistrationChallenge = new CreateRegistrationChallenge(registerRepositoryC);
    UpdateRegistrationChallenge updateRegistrationChallenge = new UpdateRegistrationChallenge(aVarA.c());
    SearchUser searchUser = new SearchUser(aVarA.d());
    this.f44508r = new S.a(new lk.c(createRegistrationChallenge, updateRegistrationChallenge, searchUser, new ArchiveUser(aVarA.d())));
    CreateAuthenticationRequest createAuthenticationRequest = new CreateAuthenticationRequest(aVarA.a());
    UpdateAuthenticationRequest updateAuthenticationRequest = new UpdateAuthenticationRequest(aVarA.a());
    this.f44509s = new i.a(new lk.a(createAuthenticationRequest, updateAuthenticationRequest,
            new CreateRegistrationChallenge(aVarA.c()), new UpdateRegistrationChallenge(aVarA.c())));
    GetLifeTests getLifeTests = new GetLifeTests(aVarA.d());          // prueba de vida
    CreateIdCheck createIdCheck = new CreateIdCheck(aVarA.d());       // verificación de identidad
    SearchFace searchFace = new SearchFace(aVarA.d());               // match facial
    UploadPhoto uploadPhoto = new UploadPhoto(aVarA.d());            // subida de foto
    CreateEvaluation createEvaluation = new CreateEvaluation(aVarA.d());
    GetDocuments getDocuments = new GetDocuments(aVarA.b());
    UploadDocument uploadDocument = new UploadDocument(aVarA.b());
    this.f44510t = new m.a(new b(getLifeTests, createIdCheck, searchFace, uploadPhoto, createEvaluation,
            getDocuments, uploadDocument, new DocumentOcrData(aVarA.b())));
    this.f44511u = new C.a();
    if (Aa.b.f798b == null) {
        Aa.b.f798b = Fido.getInstance(getApplicationContext());     // Daon FIDO
    }
    e.a(this, new f(1255440735, new s(7, this), true));             // setContent (Compose)
}
