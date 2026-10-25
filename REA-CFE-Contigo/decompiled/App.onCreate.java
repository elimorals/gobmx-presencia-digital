.method public final onCreate()V
    .registers 50

    move-object/from16 v0, p0

    invoke-static {}, Lo/ppp0070pppp;->RemoteActionCompatParcelizer()Z

    move-result v1

    if-eqz v1, :cond_c

    invoke-super/range {p0 .. p0}, Landroid/app/Application;->onCreate()V

    return-void

    :cond_c
    const/4 v1, 0x2

    .line 3097
    rem-int v2, v1, v1

    .line 0
    const-string v2, "android.app.ActivityThread"

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v2

    const-string v3, "currentApplication"

    const/4 v4, 0x0

    new-array v5, v4, [Ljava/lang/Class;

    invoke-virtual {v2, v3, v5}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v2

    const/4 v3, 0x0

    move-object v5, v3

    check-cast v5, [Ljava/lang/Object;

    invoke-virtual {v2, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/content/Context;

    invoke-virtual {v2}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v2

    invoke-virtual {v2}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    const v5, 0x7f130151

    invoke-virtual {v2, v5}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v2

    const/4 v5, 0x4

    invoke-virtual {v2, v4, v5}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v2

    add-int/lit8 v2, v2, -0x4

    const/16 v6, 0x1e

    new-array v6, v6, [C

    fill-array-data v6, :array_3f78

    const/4 v7, 0x1

    new-array v8, v7, [Ljava/lang/Object;

    invoke-static {v2, v6, v8}, Lmx/com/cfe/cfecontigo/App;->b(I[C[Ljava/lang/Object;)V

    aget-object v2, v8, v4

    check-cast v2, Ljava/lang/String;

    invoke-static {}, Landroid/view/ViewConfiguration;->getLongPressTimeout()I

    move-result v6

    const/16 v8, 0x10

    shr-int/2addr v6, v8

    const/16 v9, 0x16

    new-array v9, v9, [C

    fill-array-data v9, :array_3f9a

    new-array v10, v7, [Ljava/lang/Object;

    invoke-static {v6, v9, v10}, Lmx/com/cfe/cfecontigo/App;->b(I[C[Ljava/lang/Object;)V

    aget-object v6, v10, v4

    check-cast v6, Ljava/lang/String;

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v9

    new-array v10, v4, [Ljava/lang/Class;

    invoke-virtual {v9, v6, v10}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v9

    invoke-virtual {v9, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v9

    check-cast v9, Landroid/content/Context;

    invoke-virtual {v9}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v9

    invoke-virtual {v9}, Landroid/content/Context;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;

    move-result-object v9

    iget v9, v9, Landroid/content/pm/ApplicationInfo;->targetSdkVersion:I

    add-int/lit8 v9, v9, -0xe

    invoke-static {v4}, Landroid/os/Process;->getThreadPriority(I)I

    move-result v10

    add-int/lit8 v10, v10, 0x14

    const/4 v11, 0x6

    shr-int/2addr v10, v11

    rsub-int/lit8 v10, v10, 0x24

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v12

    new-array v13, v4, [Ljava/lang/Class;

    invoke-virtual {v12, v6, v13}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v12

    invoke-virtual {v12, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v12

    check-cast v12, Landroid/content/Context;

    invoke-virtual {v12}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v12

    invoke-virtual {v12}, Landroid/content/Context;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;

    move-result-object v12

    iget v12, v12, Landroid/content/pm/ApplicationInfo;->targetSdkVersion:I

    const v13, 0x83a8

    add-int/2addr v12, v13

    int-to-char v12, v12

    new-array v13, v7, [Ljava/lang/Object;

    invoke-static {v9, v10, v12, v13}, Lmx/com/cfe/cfecontigo/App;->a(IIC[Ljava/lang/Object;)V

    aget-object v9, v13, v4

    check-cast v9, Ljava/lang/String;

    invoke-virtual {v9}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v9

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v10

    new-array v12, v4, [Ljava/lang/Class;

    invoke-virtual {v10, v6, v12}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v10

    invoke-virtual {v10, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v10

    check-cast v10, Landroid/content/Context;

    invoke-virtual {v10}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v10

    invoke-virtual {v10}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v10

    const v12, 0x7f1300b5

    invoke-virtual {v10, v12}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v10

    invoke-virtual {v10, v4, v5}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v10

    const/4 v12, 0x3

    invoke-virtual {v10, v12}, Ljava/lang/String;->codePointAt(I)I

    move-result v10

    add-int/lit8 v10, v10, -0x15

    const/16 v13, 0x12

    new-array v13, v13, [C

    fill-array-data v13, :array_3fb4

    new-array v14, v7, [Ljava/lang/Object;

    invoke-static {v10, v13, v14}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    aget-object v10, v14, v4

    check-cast v10, Ljava/lang/String;

    invoke-virtual {v10}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v10

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v13

    new-array v14, v4, [Ljava/lang/Class;

    invoke-virtual {v13, v6, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v13

    invoke-virtual {v13, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v13

    check-cast v13, Landroid/content/Context;

    invoke-virtual {v13}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v13

    invoke-virtual {v13}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v13

    const v14, 0x7f1300d3

    invoke-virtual {v13, v14}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v13

    invoke-virtual {v13, v4, v5}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v13

    invoke-virtual {v13, v7}, Ljava/lang/String;->codePointAt(I)I

    move-result v13

    add-int/lit8 v13, v13, -0x1f

    invoke-static {}, Landroid/view/ViewConfiguration;->getScrollDefaultDelay()I

    move-result v14

    shr-int/2addr v14, v8

    rsub-int/lit8 v14, v14, 0x3a

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v15

    new-array v11, v4, [Ljava/lang/Class;

    invoke-virtual {v15, v6, v11}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v11

    invoke-virtual {v11, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v11

    check-cast v11, Landroid/content/Context;

    invoke-virtual {v11}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v11

    invoke-virtual {v11}, Landroid/content/Context;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;

    move-result-object v11

    iget v11, v11, Landroid/content/pm/ApplicationInfo;->targetSdkVersion:I

    add-int/lit16 v11, v11, 0x151a

    int-to-char v11, v11

    new-array v15, v7, [Ljava/lang/Object;

    invoke-static {v13, v14, v11, v15}, Lmx/com/cfe/cfecontigo/App;->a(IIC[Ljava/lang/Object;)V

    aget-object v11, v15, v4

    check-cast v11, Ljava/lang/String;

    invoke-virtual {v11}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v11

    const-string v13, ""

    invoke-static {v13}, Landroid/view/KeyEvent;->keyCodeFromString(Ljava/lang/String;)I

    move-result v14

    rsub-int/lit8 v14, v14, 0x10

    const/16 v15, 0x12

    new-array v15, v15, [C

    fill-array-data v15, :array_3fca

    new-array v12, v7, [Ljava/lang/Object;

    invoke-static {v14, v15, v12}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    aget-object v12, v12, v4

    check-cast v12, Ljava/lang/String;

    invoke-virtual {v12}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v12

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v14

    new-array v15, v4, [Ljava/lang/Class;

    invoke-virtual {v14, v6, v15}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v14

    invoke-virtual {v14, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v14

    check-cast v14, Landroid/content/Context;

    invoke-virtual {v14}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v14

    invoke-virtual {v14}, Landroid/content/Context;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;

    move-result-object v14

    iget v14, v14, Landroid/content/pm/ApplicationInfo;->targetSdkVersion:I

    add-int/lit8 v14, v14, -0xa

    invoke-static {}, Landroid/view/ViewConfiguration;->getTouchSlop()I

    move-result v15

    shr-int/lit8 v15, v15, 0x8

    rsub-int/lit8 v15, v15, 0x4a

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    new-array v8, v4, [Ljava/lang/Class;

    invoke-virtual {v1, v6, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    invoke-virtual {v1, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/content/Context;

    invoke-virtual {v1}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v1

    invoke-virtual {v1}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/String;->length()I

    move-result v1

    add-int/lit16 v1, v1, 0x4ae8

    int-to-char v1, v1

    new-array v8, v7, [Ljava/lang/Object;

    invoke-static {v14, v15, v1, v8}, Lmx/com/cfe/cfecontigo/App;->a(IIC[Ljava/lang/Object;)V

    aget-object v1, v8, v4

    check-cast v1, Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v1

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v8

    new-array v14, v4, [Ljava/lang/Class;

    invoke-virtual {v8, v6, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v8

    invoke-virtual {v8, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v8

    check-cast v8, Landroid/content/Context;

    invoke-virtual {v8}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v8

    invoke-virtual {v8}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v8

    const v14, 0x7f1300a1

    invoke-virtual {v8, v14}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v8, v4, v5}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v8

    invoke-virtual {v8}, Ljava/lang/String;->length()I

    move-result v8

    add-int/lit8 v8, v8, 0xe

    const/16 v14, 0x14

    new-array v14, v14, [C

    fill-array-data v14, :array_3fe0

    new-array v15, v7, [Ljava/lang/Object;

    invoke-static {v8, v14, v15}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    aget-object v8, v15, v4

    check-cast v8, Ljava/lang/String;

    invoke-virtual {v8}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v8

    .line 55
    sget-object v14, Lo/onPreparePanel;->MediaBrowserCompatCustomActionResultReceiver:[Ljava/lang/reflect/Member;

    .line 61
    aget-object v14, v14, v4

    check-cast v14, Ljava/lang/reflect/Field;

    invoke-virtual {v14, v3}, Ljava/lang/reflect/Field;->getInt(Ljava/lang/Object;)I

    move-result v14

    invoke-static/range {p0 .. p0}, Ljava/lang/System;->identityHashCode(Ljava/lang/Object;)I

    move-result v15

    not-int v5, v15

    const v18, -0x34be5027    # -1.2693465E7f

    or-int v5, v18, v5

    not-int v5, v5

    const v19, 0x71af298e

    or-int v5, v5, v19

    mul-int/lit16 v5, v5, -0xeb

    const v20, -0x3748a8b6

    add-int v20, v20, v5

    or-int v5, v18, v15

    not-int v5, v5

    or-int v5, v5, v19

    mul-int/lit16 v5, v5, -0x1d6

    add-int v20, v20, v5

    const v5, -0x4105021

    or-int/2addr v5, v15

    not-int v5, v5

    const v15, 0x41012988

    or-int/2addr v5, v15

    mul-int/lit16 v5, v5, 0xeb

    add-int v5, v20, v5

    if-ne v14, v5, :cond_3f70

    .line 82
    sget-object v5, Lo/onMenuOpened;->IconCompatParcelizer:[Ljava/lang/reflect/Member;

    aget-object v5, v5, v4

    check-cast v5, Ljava/lang/reflect/Field;

    .line 88
    invoke-virtual {v5, v3}, Ljava/lang/reflect/Field;->getInt(Ljava/lang/Object;)I

    move-result v5

    invoke-static/range {p0 .. p0}, Ljava/lang/System;->identityHashCode(Ljava/lang/Object;)I

    move-result v14

    not-int v15, v14

    const v18, 0x6f80022

    or-int v7, v15, v18

    mul-int/lit16 v7, v7, -0xc0

    const v18, -0xb1872b0

    add-int v18, v18, v7

    const v7, -0x31016f9e

    or-int/2addr v7, v15

    not-int v7, v7

    const v20, 0x1012191

    or-int v7, v20, v7

    mul-int/lit16 v7, v7, -0x180

    add-int v18, v18, v7

    const v7, -0x1012192

    or-int/2addr v7, v14

    not-int v7, v7

    const v20, -0x30004e0d

    or-int v15, v15, v20

    not-int v15, v15

    or-int/2addr v7, v15

    const v15, 0x37f96fbf

    or-int/2addr v14, v15

    not-int v14, v14

    or-int/2addr v7, v14

    mul-int/lit16 v7, v7, 0xc0

    add-int v7, v18, v7

    if-ne v5, v7, :cond_3f68

    .line 110
    invoke-super/range {p0 .. p0}, Landroid/app/Application;->onCreate()V

    .line 111
    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v5

    new-array v7, v4, [Ljava/lang/Class;

    invoke-virtual {v5, v6, v7}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v5

    invoke-virtual {v5, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/content/Context;

    invoke-virtual {v5}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v5

    invoke-virtual {v5}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/String;->length()I

    move-result v5

    add-int/lit8 v5, v5, -0x5

    const/16 v7, 0x12

    new-array v7, v7, [C

    fill-array-data v7, :array_3ff8

    const/4 v14, 0x1

    new-array v15, v14, [Ljava/lang/Object;

    invoke-static {v5, v7, v15}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    aget-object v5, v15, v4

    check-cast v5, Ljava/lang/String;

    invoke-virtual {v5}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v5

    invoke-static {}, Landroid/view/ViewConfiguration;->getTouchSlop()I

    move-result v7

    shr-int/lit8 v7, v7, 0x8

    rsub-int/lit8 v7, v7, 0xd

    const/16 v14, 0x10

    new-array v15, v14, [C

    fill-array-data v15, :array_400e

    const/4 v14, 0x1

    new-array v3, v14, [Ljava/lang/Object;

    invoke-static {v7, v15, v3}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    aget-object v3, v3, v4

    check-cast v3, Ljava/lang/String;

    invoke-virtual {v3}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v3

    new-array v7, v4, [Ljava/lang/Class;

    invoke-virtual {v5, v3, v7}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v3

    .line 112
    new-array v5, v4, [Ljava/lang/Object;

    const/4 v7, 0x0

    invoke-virtual {v3, v7, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    .line 120
    check-cast v3, Ljava/lang/Thread;

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v5

    new-array v14, v4, [Ljava/lang/Class;

    invoke-virtual {v5, v6, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v5

    invoke-virtual {v5, v7, v7}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/content/Context;

    invoke-virtual {v5}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v5

    invoke-virtual {v5}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v5

    const v7, 0x7f130129

    invoke-virtual {v5, v7}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v5

    const/16 v7, 0x9

    invoke-virtual {v5, v4, v7}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/String;->length()I

    move-result v5

    add-int/lit8 v5, v5, 0x7

    const/16 v7, 0x12

    new-array v7, v7, [C

    fill-array-data v7, :array_4022

    const/4 v14, 0x1

    new-array v15, v14, [Ljava/lang/Object;

    invoke-static {v5, v7, v15}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    aget-object v5, v15, v4

    check-cast v5, Ljava/lang/String;

    invoke-virtual {v5}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v5

    invoke-static {v5}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v5

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v7

    new-array v14, v4, [Ljava/lang/Class;

    invoke-virtual {v7, v6, v14}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v7

    const/4 v14, 0x0

    invoke-virtual {v7, v14, v14}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v7

    check-cast v7, Landroid/content/Context;

    invoke-virtual {v7}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v7

    invoke-virtual {v7}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v7}, Ljava/lang/String;->length()I

    move-result v7

    add-int/lit8 v7, v7, -0x8

    const/16 v14, 0x10

    new-array v15, v14, [C

    fill-array-data v15, :array_4038

    const/4 v14, 0x1

    new-array v0, v14, [Ljava/lang/Object;

    invoke-static {v7, v15, v0}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    aget-object v0, v0, v4

    check-cast v0, Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v0

    .line 127
    new-array v7, v4, [Ljava/lang/Class;

    .line 129
    invoke-virtual {v5, v0, v7}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 134
    new-array v5, v4, [Ljava/lang/Object;

    .line 144
    invoke-virtual {v0, v3, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/StackTraceElement;

    const v3, 0x34b86baa

    :try_start_366
    invoke-static {v3}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v3

    const/16 v5, 0x49

    const-wide/16 v14, 0x0

    if-nez v3, :cond_3b2

    invoke-static {}, Landroid/view/ViewConfiguration;->getGlobalActionKeyTimeout()J

    move-result-wide v20

    cmp-long v3, v20, v14

    add-int/lit8 v20, v3, 0x12

    invoke-static {v4, v4}, Landroid/view/View;->getDefaultSize(II)I

    move-result v3

    int-to-char v3, v3

    invoke-static {v4, v4}, Landroid/view/KeyEvent;->getDeadChar(II)I

    move-result v7

    rsub-int v7, v7, 0x430

    const v23, 0x5a7cfc5e

    const/16 v24, 0x0

    sget-object v21, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v17, 0x2

    aget-byte v14, v21, v17

    int-to-byte v14, v14

    aget-byte v15, v21, v5

    const/4 v5, 0x1

    sub-int/2addr v15, v5

    int-to-short v15, v15

    or-int/lit8 v4, v15, 0x2e

    int-to-byte v4, v4

    move-object/from16 v30, v8

    new-array v8, v5, [Ljava/lang/Object;

    invoke-static {v14, v15, v4, v8}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v8, v4

    move-object/from16 v25, v5

    check-cast v25, Ljava/lang/String;

    new-array v5, v4, [Ljava/lang/Class;

    move/from16 v21, v3

    move/from16 v22, v7

    move-object/from16 v26, v5

    invoke-static/range {v20 .. v26}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v3

    goto :goto_3b4

    :cond_3b2
    move-object/from16 v30, v8

    :goto_3b4
    check-cast v3, Ljava/lang/reflect/Method;

    const/4 v4, 0x0

    invoke-virtual {v3, v4, v4}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, [Ljava/lang/Object;
    :try_end_3bd
    .catchall {:try_start_366 .. :try_end_3bd} :catchall_3f5f

    .line 158
    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v5

    const/4 v7, 0x0

    new-array v8, v7, [Ljava/lang/Class;

    invoke-virtual {v5, v6, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v5

    invoke-virtual {v5, v4, v4}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/content/Context;

    invoke-virtual {v5}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v4

    invoke-virtual {v4}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v4

    const v5, 0x7f1300ae

    invoke-virtual {v4, v5}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v4

    const/4 v5, 0x4

    invoke-virtual {v4, v7, v5}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v4

    const/4 v5, 0x1

    invoke-virtual {v4, v5}, Ljava/lang/String;->codePointAt(I)I

    move-result v4

    add-int/lit8 v4, v4, -0x14

    invoke-static {}, Landroid/media/AudioTrack;->getMaxVolume()F

    move-result v5

    const/4 v8, 0x0

    cmpl-float v5, v5, v8

    add-int/lit8 v5, v5, 0x8

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v14

    new-array v15, v7, [Ljava/lang/Class;

    invoke-virtual {v14, v6, v15}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v14

    const/4 v15, 0x0

    invoke-virtual {v14, v15, v15}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v14

    check-cast v14, Landroid/content/Context;

    invoke-virtual {v14}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v14

    invoke-virtual {v14}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v14

    const v15, 0x7f130011

    invoke-virtual {v14, v15}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v14

    const/4 v15, 0x4

    invoke-virtual {v14, v7, v15}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v14

    const/4 v15, 0x2

    invoke-virtual {v14, v15}, Ljava/lang/String;->codePointAt(I)I

    move-result v14

    const v15, 0xa261

    add-int/2addr v14, v15

    int-to-char v14, v14

    const/4 v15, 0x1

    new-array v8, v15, [Ljava/lang/Object;

    invoke-static {v4, v5, v14, v8}, Lmx/com/cfe/cfecontigo/App;->a(IIC[Ljava/lang/Object;)V

    aget-object v4, v8, v7

    check-cast v4, Ljava/lang/String;

    invoke-virtual {v4}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v4

    invoke-static {v4}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v4

    invoke-static {v2}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v5

    new-array v8, v7, [Ljava/lang/Class;

    invoke-virtual {v5, v6, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v5

    const/4 v7, 0x0

    invoke-virtual {v5, v7, v7}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/content/Context;

    invoke-virtual {v5}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v5

    invoke-virtual {v5}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v5

    invoke-virtual {v5}, Ljava/lang/String;->length()I

    move-result v5

    add-int/lit8 v5, v5, -0x9

    const/16 v7, 0xe

    new-array v7, v7, [C

    fill-array-data v7, :array_404c

    const/4 v8, 0x1

    new-array v14, v8, [Ljava/lang/Object;

    invoke-static {v5, v7, v14}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v7, v14, v5

    check-cast v7, Ljava/lang/String;

    invoke-virtual {v7}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v7

    new-array v8, v5, [Ljava/lang/Class;

    invoke-virtual {v4, v7, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v4

    const v5, -0x4b90f2b9

    invoke-static {v5}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v5

    const/16 v7, 0x30

    const/16 v8, 0xc

    if-nez v5, :cond_4c1

    invoke-static {v7}, Landroid/text/AndroidCharacter;->getMirror(C)C

    move-result v5

    rsub-int/lit8 v31, v5, 0x48

    invoke-static {v7}, Landroid/text/AndroidCharacter;->getMirror(C)C

    move-result v5

    add-int/lit8 v5, v5, -0x30

    int-to-char v5, v5

    invoke-static {}, Landroid/media/AudioTrack;->getMaxVolume()F

    move-result v14

    const/4 v15, 0x0

    cmpl-float v14, v14, v15

    add-int/lit16 v14, v14, 0x1e8

    const v34, -0x2554654d

    const/16 v35, 0x0

    sget-object v15, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v21, 0xe

    aget-byte v21, v15, v21

    move-object/from16 v23, v6

    const/4 v7, 0x1

    add-int/lit8 v6, v21, -0x1

    int-to-byte v6, v6

    aget-byte v15, v15, v8

    int-to-short v15, v15

    const/16 v8, 0x25

    int-to-byte v8, v8

    move-object/from16 v24, v2

    new-array v2, v7, [Ljava/lang/Object;

    invoke-static {v6, v15, v8, v2}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v6, 0x0

    aget-object v2, v2, v6

    move-object/from16 v36, v2

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v5

    move/from16 v33, v14

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v5

    goto :goto_4c5

    :cond_4c1
    move-object/from16 v24, v2

    move-object/from16 v23, v6

    :goto_4c5
    check-cast v5, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    const/4 v6, 0x0

    invoke-virtual {v5, v6, v2}, Ljava/lang/reflect/Field;->setInt(Ljava/lang/Object;I)V

    .line 163
    array-length v5, v0

    move v6, v2

    :goto_4ce
    if-ge v6, v5, :cond_5aa

    .line 180
    aget-object v7, v0, v6

    array-length v8, v3

    move v14, v2

    :goto_4d4
    if-ge v14, v8, :cond_5a3

    .line 184
    aget-object v15, v3, v14

    move-object/from16 v25, v0

    .line 191
    new-array v0, v2, [Ljava/lang/Object;

    invoke-virtual {v4, v7, v0}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/String;

    .line 193
    invoke-virtual {v15, v0}, Ljava/lang/Object;->equals(Ljava/lang/Object;)Z

    move-result v0

    if-eqz v0, :cond_59c

    .line 3097
    sget v0, Lmx/com/cfe/cfecontigo/App;->RatingCompat:I

    add-int/lit8 v0, v0, 0x6f

    rem-int/lit16 v2, v0, 0x80

    sput v2, Lmx/com/cfe/cfecontigo/App;->MediaBrowserCompatSearchResultReceiver:I

    const/4 v2, 0x2

    rem-int/2addr v0, v2

    if-eqz v0, :cond_548

    const v0, -0x4b90f2b9

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_540

    const/16 v0, 0x30

    const/4 v2, 0x0

    invoke-static {v13, v0, v2}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CI)I

    move-result v3

    rsub-int/lit8 v31, v3, 0x17

    invoke-static {v2, v2}, Landroid/view/View$MeasureSpec;->makeMeasureSpec(II)I

    move-result v0

    int-to-char v0, v0

    invoke-static {v2}, Landroid/util/TypedValue;->complexToFloat(I)F

    move-result v3

    const/4 v2, 0x0

    cmpl-float v3, v3, v2

    add-int/lit16 v2, v3, 0x1e9

    const v34, -0x2554654d

    const/16 v35, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v4, 0xe

    aget-byte v4, v3, v4

    const/4 v5, 0x1

    sub-int/2addr v4, v5

    int-to-byte v4, v4

    const/16 v6, 0xc

    aget-byte v3, v3, v6

    int-to-short v3, v3

    const/16 v6, 0x25

    int-to-byte v6, v6

    new-array v7, v5, [Ljava/lang/Object;

    invoke-static {v4, v3, v6, v7}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v7, v3

    move-object/from16 v36, v4

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v0

    move/from16 v33, v2

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_540
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    const/4 v3, 0x0

    invoke-virtual {v0, v2, v3}, Ljava/lang/reflect/Field;->setInt(Ljava/lang/Object;I)V

    goto :goto_5aa

    :cond_548
    const/4 v3, 0x0

    const v0, -0x4b90f2b9

    .line 193
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_594

    invoke-static {v13, v3}, Landroid/text/TextUtils;->getOffsetAfter(Ljava/lang/CharSequence;I)I

    move-result v0

    rsub-int/lit8 v31, v0, 0x18

    invoke-static {}, Landroid/view/ViewConfiguration;->getMaximumFlingVelocity()I

    move-result v0

    const/16 v2, 0x10

    shr-int/2addr v0, v2

    int-to-char v0, v0

    invoke-static {}, Landroid/view/ViewConfiguration;->getMaximumDrawingCacheSize()I

    move-result v2

    shr-int/lit8 v2, v2, 0x18

    rsub-int v2, v2, 0x1e9

    const v34, -0x2554654d

    const/16 v35, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v4, 0xe

    aget-byte v4, v3, v4

    const/4 v5, 0x1

    sub-int/2addr v4, v5

    int-to-byte v4, v4

    const/16 v6, 0xc

    aget-byte v3, v3, v6

    int-to-short v3, v3

    const/16 v6, 0x25

    int-to-byte v6, v6

    new-array v7, v5, [Ljava/lang/Object;

    invoke-static {v4, v3, v6, v7}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v7, v3

    move-object/from16 v36, v4

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v0

    move/from16 v33, v2

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_594
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    const/4 v3, 0x1

    invoke-virtual {v0, v2, v3}, Ljava/lang/reflect/Field;->setInt(Ljava/lang/Object;I)V

    goto :goto_5aa

    :cond_59c
    add-int/lit8 v14, v14, 0x1

    move-object/from16 v0, v25

    const/4 v2, 0x0

    goto/16 :goto_4d4

    :cond_5a3
    move-object/from16 v25, v0

    add-int/lit8 v6, v6, 0x1

    const/4 v2, 0x0

    goto/16 :goto_4ce

    :cond_5aa
    :goto_5aa
    const-wide/16 v2, -0x1

    .line 207
    invoke-static {v2, v3}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v2, -0x5a1cad9d

    invoke-static {v2}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v2

    const/16 v3, 0x1a

    if-nez v2, :cond_5f9

    const/16 v2, 0x30

    const/4 v4, 0x0

    invoke-static {v13, v2, v4, v4}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CII)I

    move-result v5

    rsub-int/lit8 v31, v5, 0x12

    invoke-static {}, Landroid/media/AudioTrack;->getMinVolume()F

    move-result v4

    const/4 v5, 0x0

    cmpl-float v4, v4, v5

    int-to-char v4, v4

    invoke-static {v13, v2}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;C)I

    move-result v5

    add-int/lit16 v2, v5, 0x431

    const v34, -0x34d83a69    # -1.0995095E7f

    const/16 v35, 0x0

    const/16 v5, 0x28

    int-to-byte v5, v5

    or-int/lit8 v6, v5, 0x6

    int-to-short v6, v6

    sget-object v7, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    aget-byte v7, v7, v3

    int-to-byte v7, v7

    const/4 v8, 0x1

    new-array v14, v8, [Ljava/lang/Object;

    invoke-static {v5, v6, v7, v14}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v6, v14, v5

    move-object/from16 v36, v6

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v4

    move/from16 v33, v2

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v2

    :cond_5f9
    check-cast v2, Ljava/lang/reflect/Field;

    const/4 v4, 0x0

    invoke-virtual {v2, v4, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const v0, -0x1178ecac

    .line 211
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_641

    const/4 v0, 0x0

    invoke-static {v0, v0}, Landroid/graphics/drawable/Drawable;->resolveOpacity(II)I

    move-result v2

    rsub-int/lit8 v31, v2, 0x16

    invoke-static {v0}, Landroid/view/View$MeasureSpec;->getMode(I)I

    move-result v2

    int-to-char v2, v2

    invoke-static {v0, v0, v0}, Landroid/view/View;->resolveSizeAndState(III)I

    move-result v4

    rsub-int v0, v4, 0x4f8

    const v34, -0x7fbc7b60

    const/16 v35, 0x0

    const/16 v4, 0x28

    int-to-byte v4, v4

    or-int/lit8 v5, v4, 0x6

    int-to-short v5, v5

    sget-object v6, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    aget-byte v6, v6, v3

    int-to-byte v6, v6

    const/4 v7, 0x1

    new-array v8, v7, [Ljava/lang/Object;

    invoke-static {v4, v5, v6, v8}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v8, v4

    move-object/from16 v36, v5

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v2

    move/from16 v33, v0

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_641
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    invoke-virtual {v0, v2}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v4

    .line 213
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v6, 0x0

    .line 222
    new-array v7, v6, [Ljava/lang/Class;

    invoke-virtual {v0, v10, v7}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 229
    new-array v7, v6, [Ljava/lang/Object;

    invoke-virtual {v0, v2, v7}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    .line 231
    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v6

    const v0, 0x285dbb57

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    const/16 v2, 0xb2

    if-nez v0, :cond_6b4

    invoke-static {v13}, Landroid/view/KeyEvent;->keyCodeFromString(Ljava/lang/String;)I

    move-result v0

    rsub-int/lit8 v31, v0, 0x16

    const/4 v0, 0x0

    invoke-static {v0}, Landroid/graphics/ImageFormat;->getBitsPerPixel(I)I

    move-result v8

    rsub-int/lit8 v0, v8, -0x1

    int-to-char v0, v0

    invoke-static {}, Landroid/view/ViewConfiguration;->getKeyRepeatDelay()I

    move-result v8

    const/16 v14, 0x10

    shr-int/2addr v8, v14

    add-int/lit16 v8, v8, 0x4f8

    const v34, 0x46992ca3

    const/16 v35, 0x0

    sget-object v14, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v15, 0x49

    aget-byte v25, v14, v15

    const/4 v15, 0x1

    add-int/lit8 v3, v25, -0x1

    int-to-byte v3, v3

    const/16 v19, 0x46

    aget-byte v15, v14, v19

    neg-int v15, v15

    int-to-short v15, v15

    aget-byte v14, v14, v2

    const/4 v2, 0x1

    sub-int/2addr v14, v2

    int-to-byte v14, v14

    move-object/from16 v25, v1

    new-array v1, v2, [Ljava/lang/Object;

    invoke-static {v3, v15, v14, v1}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v1, v1, v2

    move-object/from16 v36, v1

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v0

    move/from16 v33, v8

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    goto :goto_6b6

    :cond_6b4
    move-object/from16 v25, v1

    :goto_6b6
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v2

    const/16 v0, 0x34

    shl-long v1, v2, v0

    ushr-long/2addr v1, v0

    sub-long/2addr v6, v1

    const/16 v1, 0xc

    shr-long v2, v6, v1

    cmp-long v1, v4, v2

    const/4 v2, 0x5

    if-nez v1, :cond_789

    const v1, -0x1e9d0d40

    .line 248
    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_715

    const/4 v1, 0x0

    invoke-static {v13, v1}, Landroid/text/TextUtils;->getOffsetBefore(Ljava/lang/CharSequence;I)I

    move-result v3

    add-int/lit8 v31, v3, 0x16

    invoke-static {v1, v1}, Landroid/graphics/drawable/Drawable;->resolveOpacity(II)I

    move-result v3

    int-to-char v1, v3

    invoke-static {}, Landroid/view/ViewConfiguration;->getKeyRepeatTimeout()I

    move-result v3

    const/16 v4, 0x10

    shr-int/2addr v3, v4

    rsub-int v3, v3, 0x4f8

    const v34, -0x70599acc

    const/16 v35, 0x0

    sget-object v4, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v5, 0x2

    aget-byte v6, v4, v5

    int-to-byte v5, v6

    const/16 v6, 0x49

    aget-byte v4, v4, v6

    const/4 v6, 0x1

    sub-int/2addr v4, v6

    int-to-short v4, v4

    or-int/lit8 v7, v4, 0x2e

    int-to-byte v7, v7

    new-array v8, v6, [Ljava/lang/Object;

    invoke-static {v5, v4, v7, v8}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v8, v4

    move-object/from16 v36, v5

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v1

    move/from16 v33, v3

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_715
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, [Ljava/lang/Object;

    .line 258
    new-array v3, v2, [Ljava/lang/Object;

    const/4 v4, 0x1

    new-array v5, v4, [I

    aput-object v5, v3, v4

    new-array v5, v4, [I

    const/4 v6, 0x2

    aput-object v5, v3, v6

    new-array v7, v4, [I

    const/4 v4, 0x3

    aput-object v7, v3, v4

    .line 264
    aget-object v8, v1, v6

    check-cast v8, [I

    const/4 v6, 0x0

    aget v8, v8, v6

    aget-object v14, v1, v4

    check-cast v14, [I

    aget v4, v14, v6

    aget-object v14, v1, v6

    check-cast v14, [Ljava/lang/String;

    const/4 v15, 0x4

    aget-object v1, v1, v15

    check-cast v1, [Ljava/lang/Object;

    check-cast v5, [I

    aput v8, v5, v6

    check-cast v7, [I

    aput v4, v7, v6

    aput-object v14, v3, v6

    aput-object v1, v3, v15

    invoke-static {}, Ljava/lang/Runtime;->getRuntime()Ljava/lang/Runtime;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/Runtime;->maxMemory()J

    move-result-wide v4

    long-to-int v1, v4

    not-int v1, v1

    const v4, 0x2390fef7

    or-int/2addr v4, v1

    mul-int/lit16 v4, v4, 0xb8

    const v5, 0x70cd49c3

    add-int/2addr v5, v4

    const v4, 0x2210ba63

    or-int/2addr v1, v4

    not-int v1, v1

    const v4, 0x3905c94

    or-int/2addr v1, v4

    mul-int/lit16 v1, v1, 0xb8

    add-int/2addr v5, v1

    const v1, 0x5b0f5b2b

    add-int/2addr v5, v1

    shl-int/lit8 v1, v5, 0xd

    xor-int/2addr v1, v5

    ushr-int/lit8 v4, v1, 0x11

    xor-int/2addr v1, v4

    shl-int/lit8 v4, v1, 0x5

    xor-int/2addr v1, v4

    const/4 v4, 0x1

    aget-object v5, v3, v4

    check-cast v5, [I

    const/4 v4, 0x0

    aput v1, v5, v4

    :goto_786
    const/4 v0, 0x3

    goto/16 :goto_915

    .line 265
    :cond_789
    invoke-static {v11}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    .line 273
    const-class v3, Ljava/lang/Object;

    filled-new-array {v3}, [Ljava/lang/Class;

    move-result-object v3

    .line 275
    invoke-virtual {v1, v12, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    .line 283
    filled-new-array/range {p0 .. p0}, [Ljava/lang/Object;

    move-result-object v3

    const/4 v4, 0x0

    invoke-virtual {v1, v4, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    .line 285
    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Number;->intValue()I

    move-result v1

    const/4 v3, 0x1

    .line 289
    :try_start_7a7
    new-array v4, v3, [Ljava/lang/Object;

    const v3, 0x703518d3

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v3

    const/4 v5, 0x0

    aput-object v3, v4, v5

    const v3, 0x40985059

    invoke-static {v3}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v3

    if-nez v3, :cond_7e9

    invoke-static {v5}, Landroid/view/KeyEvent;->normalizeMetaState(I)I

    move-result v3

    add-int/lit8 v31, v3, 0x19

    invoke-static {}, Landroid/view/ViewConfiguration;->getKeyRepeatTimeout()I

    move-result v3

    const/16 v5, 0x10

    shr-int/2addr v3, v5

    int-to-char v3, v3

    invoke-static {v13, v13}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)I

    move-result v5

    add-int/lit16 v5, v5, 0x4df

    const v34, 0x2e5cc7ad

    const/16 v35, 0x0

    const/16 v36, 0x0

    const/4 v6, 0x1

    new-array v7, v6, [Ljava/lang/Class;

    sget-object v6, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v8, 0x0

    aput-object v6, v7, v8

    move/from16 v32, v3

    move/from16 v33, v5

    move-object/from16 v37, v7

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v3

    :cond_7e9
    check-cast v3, Ljava/lang/reflect/Constructor;

    invoke-virtual {v3, v4}, Ljava/lang/reflect/Constructor;->newInstance([Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3
    :try_end_7ef
    .catchall {:try_start_7a7 .. :try_end_7ef} :catchall_3f5f

    const v4, 0x5b0f5b2b

    .line 306
    invoke-static {v1, v3, v4}, Lcom/huawei/agconnect/core/a/c$1;->read$270dde57(ILjava/lang/Object;I)[Ljava/lang/Object;

    move-result-object v3

    const v1, -0x1e9d0d40

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_843

    const/4 v4, 0x0

    invoke-static {v4}, Landroid/widget/ExpandableListView;->getPackedPositionForGroup(I)J

    move-result-wide v5

    const-wide/16 v7, 0x0

    cmp-long v1, v5, v7

    rsub-int/lit8 v31, v1, 0x16

    const/16 v1, 0x30

    invoke-static {v13, v1, v4}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;CI)I

    move-result v5

    const/4 v4, 0x1

    add-int/2addr v5, v4

    int-to-char v5, v5

    invoke-static {v13, v1}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;C)I

    move-result v6

    rsub-int v1, v6, 0x4f7

    const v34, -0x70599acc

    const/16 v35, 0x0

    sget-object v6, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v7, 0x2

    aget-byte v8, v6, v7

    int-to-byte v7, v8

    const/16 v8, 0x49

    aget-byte v6, v6, v8

    sub-int/2addr v6, v4

    int-to-short v6, v6

    or-int/lit8 v8, v6, 0x2e

    int-to-byte v8, v8

    new-array v14, v4, [Ljava/lang/Object;

    invoke-static {v7, v6, v8, v14}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v6, v14, v4

    move-object/from16 v36, v6

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v5

    move/from16 v33, v1

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_843
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v4, 0x0

    invoke-virtual {v1, v4, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    :try_start_849
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    const/4 v5, 0x0

    .line 310
    new-array v6, v5, [Ljava/lang/Class;

    invoke-virtual {v1, v10, v6}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    new-array v6, v5, [Ljava/lang/Object;

    invoke-virtual {v1, v4, v6}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Long;

    .line 317
    invoke-virtual {v1}, Ljava/lang/Number;->longValue()J

    move-result-wide v4
    :try_end_860
    .catch Ljava/lang/Exception; {:try_start_849 .. :try_end_860} :catch_3f59

    invoke-static {v4, v5}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v1

    const v6, 0x285dbb57

    invoke-static {v6}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v6

    if-nez v6, :cond_8b7

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v6

    const-wide/16 v14, 0x0

    cmp-long v6, v6, v14

    rsub-int/lit8 v31, v6, 0x17

    const/4 v6, 0x0

    invoke-static {v6}, Landroid/os/Process;->getThreadPriority(I)I

    move-result v7

    add-int/lit8 v7, v7, 0x14

    const/4 v8, 0x6

    shr-int/2addr v7, v8

    int-to-char v7, v7

    invoke-static {v6, v6}, Landroid/view/View;->resolveSize(II)I

    move-result v8

    add-int/lit16 v6, v8, 0x4f8

    const v34, 0x46992ca3

    const/16 v35, 0x0

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v14, 0x49

    aget-byte v15, v8, v14

    const/4 v14, 0x1

    sub-int/2addr v15, v14

    int-to-byte v15, v15

    const/16 v19, 0x46

    aget-byte v0, v8, v19

    neg-int v0, v0

    int-to-short v0, v0

    const/16 v19, 0xb2

    aget-byte v8, v8, v19

    sub-int/2addr v8, v14

    int-to-byte v8, v8

    new-array v2, v14, [Ljava/lang/Object;

    invoke-static {v15, v0, v8, v2}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v0, 0x0

    aget-object v2, v2, v0

    move-object/from16 v36, v2

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v7

    move/from16 v33, v6

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v6

    :cond_8b7
    check-cast v6, Ljava/lang/reflect/Field;

    const/4 v0, 0x0

    invoke-virtual {v6, v0, v1}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v0, 0xc

    shr-long v1, v4, v0

    .line 319
    invoke-static {v1, v2}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, -0x1178ecac

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_90d

    const-wide/16 v1, 0x0

    invoke-static {v1, v2}, Landroid/widget/ExpandableListView;->getPackedPositionGroup(J)I

    move-result v4

    add-int/lit8 v31, v4, 0x16

    invoke-static {v13, v13}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)I

    move-result v1

    int-to-char v1, v1

    invoke-static {}, Landroid/view/ViewConfiguration;->getMaximumFlingVelocity()I

    move-result v2

    const/16 v4, 0x10

    shr-int/2addr v2, v4

    add-int/lit16 v2, v2, 0x4f8

    const v34, -0x7fbc7b60

    const/16 v35, 0x0

    const/16 v4, 0x28

    int-to-byte v4, v4

    or-int/lit8 v5, v4, 0x6

    int-to-short v5, v5

    sget-object v6, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v7, 0x1a

    aget-byte v6, v6, v7

    int-to-byte v6, v6

    const/4 v7, 0x1

    new-array v8, v7, [Ljava/lang/Object;

    invoke-static {v4, v5, v6, v8}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v8, v4

    move-object/from16 v36, v5

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v1

    move/from16 v33, v2

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_90d
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    invoke-virtual {v1, v2, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    goto/16 :goto_786

    .line 334
    :goto_915
    aget-object v1, v3, v0

    check-cast v1, [I

    const/4 v0, 0x0

    aget v1, v1, v0

    const/4 v2, 0x2

    aget-object v4, v3, v2

    check-cast v4, [I

    aget v4, v4, v0

    if-ne v4, v1, :cond_3f3f

    .line 3097
    sget v0, Lmx/com/cfe/cfecontigo/App;->MediaBrowserCompatSearchResultReceiver:I

    add-int/lit8 v0, v0, 0x3f

    rem-int/lit16 v1, v0, 0x80

    sput v1, Lmx/com/cfe/cfecontigo/App;->RatingCompat:I

    rem-int/2addr v0, v2

    const/4 v0, 0x5

    .line 337
    new-array v1, v0, [Ljava/lang/Object;

    const/4 v0, 0x1

    new-array v4, v0, [I

    aput-object v4, v1, v0

    new-array v4, v0, [I

    aput-object v4, v1, v2

    new-array v5, v0, [I

    const/4 v6, 0x3

    aput-object v5, v1, v6

    .line 339
    aget-object v7, v3, v0

    check-cast v7, [I

    const/4 v0, 0x0

    aget v7, v7, v0

    .line 341
    aget-object v8, v3, v2

    check-cast v8, [I

    aget v2, v8, v0

    aget-object v8, v3, v6

    check-cast v8, [I

    aget v6, v8, v0

    aget-object v8, v3, v0

    check-cast v8, [Ljava/lang/String;

    const/4 v14, 0x4

    aget-object v3, v3, v14

    check-cast v3, [Ljava/lang/Object;

    check-cast v4, [I

    aput v2, v4, v0

    check-cast v5, [I

    aput v6, v5, v0

    aput-object v8, v1, v0

    aput-object v3, v1, v14

    invoke-static/range {p0 .. p0}, Ljava/lang/System;->identityHashCode(Ljava/lang/Object;)I

    move-result v0

    const v2, -0x350d20cb    # -7958426.5f

    or-int/2addr v0, v2

    not-int v0, v0

    const v2, -0x359cf437

    or-int/2addr v2, v0

    mul-int/lit16 v2, v2, -0xdc

    const v3, 0x2b7accfd

    add-int/2addr v3, v2

    const v2, 0x100c8

    or-int/2addr v0, v2

    mul-int/lit16 v0, v0, 0xdc

    add-int/2addr v3, v0

    const v0, -0x3ea984b6

    add-int/2addr v3, v0

    add-int/2addr v7, v3

    shl-int/lit8 v0, v7, 0xd

    xor-int/2addr v0, v7

    ushr-int/lit8 v2, v0, 0x11

    xor-int/2addr v0, v2

    shl-int/lit8 v2, v0, 0x5

    xor-int/2addr v0, v2

    const/4 v2, 0x1

    aget-object v3, v1, v2

    check-cast v3, [I

    const/4 v2, 0x0

    aput v0, v3, v2

    const v0, 0x316dc05d

    .line 420
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_9e3

    const/16 v0, 0x30

    invoke-static {v13, v0, v2}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CI)I

    move-result v3

    rsub-int/lit8 v31, v3, 0x14

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v3

    const-wide/16 v5, 0x0

    cmp-long v0, v3, v5

    const/4 v3, 0x1

    rsub-int/lit8 v7, v0, 0x1

    int-to-char v0, v7

    invoke-static {v13, v2}, Landroid/text/TextUtils;->getOffsetAfter(Ljava/lang/CharSequence;I)I

    move-result v3

    rsub-int v2, v3, 0x2f7

    const v34, 0x5fa957a9

    const/16 v35, 0x0

    const/16 v3, 0x28

    int-to-byte v3, v3

    or-int/lit8 v4, v3, 0x6

    int-to-short v4, v4

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v6, 0x1a

    aget-byte v5, v5, v6

    int-to-byte v5, v5

    const/4 v6, 0x1

    new-array v7, v6, [Ljava/lang/Object;

    invoke-static {v3, v4, v5, v7}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v7, v3

    move-object/from16 v36, v4

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v0

    move/from16 v33, v2

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_9e3
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    invoke-virtual {v0, v2}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v3

    .line 429
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v5, 0x0

    new-array v6, v5, [Ljava/lang/Class;

    .line 431
    invoke-virtual {v0, v10, v6}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 433
    new-array v6, v5, [Ljava/lang/Object;

    invoke-virtual {v0, v2, v6}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v6

    const v0, -0x25c7c814

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_a52

    invoke-static {v5, v5}, Landroid/widget/ExpandableListView;->getPackedPositionForChild(II)J

    move-result-wide v14

    const-wide/16 v27, 0x0

    cmp-long v0, v14, v27

    add-int/lit8 v31, v0, 0x16

    invoke-static {}, Landroid/view/ViewConfiguration;->getZoomControlsTimeout()J

    move-result-wide v14

    cmp-long v0, v14, v27

    add-int/lit8 v0, v0, -0x1

    int-to-char v0, v0

    invoke-static {v13}, Landroid/view/KeyEvent;->keyCodeFromString(Ljava/lang/String;)I

    move-result v2

    rsub-int v2, v2, 0x2f7

    const v34, -0x4b035fe8

    const/16 v35, 0x0

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v8, 0xe

    aget-byte v8, v5, v8

    const/4 v14, 0x1

    sub-int/2addr v8, v14

    int-to-byte v8, v8

    const/16 v15, 0xc

    aget-byte v5, v5, v15

    int-to-short v5, v5

    const/16 v15, 0x25

    int-to-byte v15, v15

    move-object/from16 v38, v1

    new-array v1, v14, [Ljava/lang/Object;

    invoke-static {v8, v5, v15, v1}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v1, v1, v5

    move-object/from16 v36, v1

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v0

    move/from16 v33, v2

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    goto :goto_a54

    :cond_a52
    move-object/from16 v38, v1

    :goto_a54
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v14

    const/16 v0, 0x34

    shl-long v1, v14, v0

    ushr-long/2addr v1, v0

    sub-long/2addr v6, v1

    const/16 v0, 0xc

    shr-long v1, v6, v0

    cmp-long v0, v3, v1

    const/16 v1, 0x11

    if-nez v0, :cond_b3c

    const v0, 0x3bf4eb60

    .line 447
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_ab9

    const/4 v0, 0x0

    invoke-static {v0, v0}, Landroid/view/View;->resolveSize(II)I

    move-result v2

    add-int/lit8 v31, v2, 0x15

    const/16 v2, 0x30

    invoke-static {v13, v2}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;C)I

    move-result v3

    const/4 v2, 0x1

    add-int/2addr v3, v2

    int-to-char v3, v3

    invoke-static {v0}, Landroid/graphics/Color;->blue(I)I

    move-result v4

    add-int/lit16 v0, v4, 0x2f7

    const v34, 0x55307c94

    const/16 v35, 0x0

    sget-object v4, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v5, 0x49

    aget-byte v6, v4, v5

    sub-int/2addr v6, v2

    int-to-byte v5, v6

    const/16 v6, 0x46

    aget-byte v6, v4, v6

    neg-int v6, v6

    int-to-short v6, v6

    const/16 v7, 0xb2

    aget-byte v4, v4, v7

    sub-int/2addr v4, v2

    int-to-byte v4, v4

    new-array v7, v2, [Ljava/lang/Object;

    invoke-static {v5, v6, v4, v7}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v4, v7, v2

    move-object/from16 v36, v4

    check-cast v36, Ljava/lang/String;

    const/16 v37, 0x0

    move/from16 v32, v3

    move/from16 v33, v0

    invoke-static/range {v31 .. v37}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_ab9
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    invoke-virtual {v0, v2}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/Object;

    const/4 v2, 0x5

    new-array v3, v2, [Ljava/lang/Object;

    const/4 v2, 0x1

    new-array v4, v2, [I

    const/4 v5, 0x0

    aput-object v4, v3, v5

    new-array v6, v2, [I

    const/4 v7, 0x2

    aput-object v6, v3, v7

    new-array v6, v2, [I

    const/4 v7, 0x4

    aput-object v6, v3, v7

    .line 449
    aget-object v8, v0, v5

    check-cast v8, [I

    aget v8, v8, v5

    aget-object v14, v0, v7

    check-cast v14, [I

    aget v7, v14, v5

    aget-object v14, v0, v2

    check-cast v14, [Ljava/lang/String;

    const/4 v15, 0x3

    aget-object v0, v0, v15

    check-cast v0, [Ljava/lang/Object;

    check-cast v4, [I

    aput v8, v4, v5

    check-cast v6, [I

    aput v7, v6, v5

    aput-object v14, v3, v2

    aput-object v0, v3, v15

    new-instance v0, Ljava/util/Random;

    invoke-direct {v0}, Ljava/util/Random;-><init>()V

    invoke-virtual {v0}, Ljava/util/Random;->nextInt()I

    move-result v0

    const v2, -0x64b0f08e

    or-int/2addr v2, v0

    mul-int/lit16 v2, v2, 0x178

    const v4, -0x61e40b

    add-int/2addr v4, v2

    not-int v2, v0

    const v5, 0x2d3c83c4

    or-int/2addr v2, v5

    not-int v2, v2

    const v5, -0x6dbcf3ce

    or-int/2addr v2, v5

    mul-int/lit16 v2, v2, -0x178

    add-int/2addr v4, v2

    const v2, -0x2d3c83c5

    or-int/2addr v0, v2

    not-int v0, v0

    const v2, 0x498c7349

    or-int/2addr v0, v2

    mul-int/lit16 v0, v0, 0x178

    add-int/2addr v4, v0

    const v0, -0x277d29bc

    add-int/2addr v4, v0

    shl-int/lit8 v0, v4, 0xd

    xor-int/2addr v0, v4

    ushr-int/lit8 v2, v0, 0x11

    xor-int/2addr v0, v2

    shl-int/lit8 v2, v0, 0x5

    xor-int/2addr v0, v2

    const/4 v2, 0x2

    aget-object v4, v3, v2

    check-cast v4, [I

    const/4 v2, 0x0

    aput v0, v4, v2

    move-object/from16 v2, v30

    :goto_b39
    const/4 v0, 0x4

    goto/16 :goto_d38

    :cond_b3c
    const/4 v2, 0x0

    .line 451
    invoke-virtual/range {p0 .. p0}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    move-result-object v0

    if-nez v0, :cond_b57

    .line 461
    invoke-static/range {v25 .. v25}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    .line 478
    new-array v3, v2, [Ljava/lang/Class;

    move-object/from16 v2, v30

    invoke-virtual {v0, v2, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v3, 0x0

    invoke-virtual {v0, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    goto :goto_b59

    :cond_b57
    move-object/from16 v2, v30

    :goto_b59
    if-eqz v0, :cond_b86

    .line 483
    instance-of v3, v0, Landroid/content/ContextWrapper;

    if-eqz v3, :cond_b7e

    .line 3097
    sget v3, Lmx/com/cfe/cfecontigo/App;->RatingCompat:I

    add-int/lit8 v3, v3, 0x39

    rem-int/lit16 v4, v3, 0x80

    sput v4, Lmx/com/cfe/cfecontigo/App;->MediaBrowserCompatSearchResultReceiver:I

    const/4 v4, 0x2

    rem-int/2addr v3, v4

    if-nez v3, :cond_b77

    .line 483
    move-object v3, v0

    check-cast v3, Landroid/content/ContextWrapper;

    invoke-virtual {v3}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    move-result-object v3

    if-eqz v3, :cond_b75

    goto :goto_b7e

    :cond_b75
    const/4 v0, 0x0

    goto :goto_b86

    .line 3097
    :cond_b77
    check-cast v0, Landroid/content/ContextWrapper;

    invoke-virtual {v0}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    const/4 v3, 0x0

    throw v3

    :cond_b7e
    :goto_b7e
    const/4 v3, 0x0

    .line 492
    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v18

    move-object/from16 v0, v18

    goto :goto_b87

    :cond_b86
    :goto_b86
    const/4 v3, 0x0

    .line 493
    :goto_b87
    invoke-static {v11}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v4

    .line 510
    const-class v5, Ljava/lang/Object;

    .line 516
    filled-new-array {v5}, [Ljava/lang/Class;

    move-result-object v5

    invoke-virtual {v4, v12, v5}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v4

    .line 518
    filled-new-array/range {p0 .. p0}, [Ljava/lang/Object;

    move-result-object v5

    invoke-virtual {v4, v3, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Ljava/lang/Integer;

    invoke-virtual {v4}, Ljava/lang/Number;->intValue()I

    move-result v3

    const/4 v4, 0x4

    .line 522
    :try_start_ba4
    new-array v5, v4, [Ljava/lang/Object;

    const v4, -0x277d29bc

    invoke-static {v4}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    const/4 v6, 0x3

    aput-object v4, v5, v6

    const-wide/16 v6, 0x0

    invoke-static {v6, v7}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v4

    const/4 v6, 0x2

    aput-object v4, v5, v6

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v3

    const/4 v4, 0x1

    aput-object v3, v5, v4

    const/4 v3, 0x0

    aput-object v0, v5, v3

    sget-object v0, Lmx/com/cfe/cfecontigo/App;->$$d:[B

    const/16 v3, 0x6a

    aget-byte v3, v0, v3

    add-int/lit8 v4, v3, -0x1

    int-to-byte v4, v4

    const/16 v6, 0x94

    int-to-short v6, v6

    const/4 v7, 0x1

    sub-int/2addr v3, v7

    int-to-byte v3, v3

    new-array v8, v7, [Ljava/lang/Object;

    invoke-static {v4, v6, v3, v8}, Lmx/com/cfe/cfecontigo/App;->e(BSS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v8, v3

    check-cast v4, Ljava/lang/String;

    invoke-static {v4}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v3

    aget-byte v4, v0, v1

    int-to-byte v4, v4

    const/16 v6, 0x8f

    int-to-short v6, v6

    const/16 v7, 0x4f

    aget-byte v0, v0, v7

    int-to-byte v0, v0

    const/4 v7, 0x1

    new-array v8, v7, [Ljava/lang/Object;

    invoke-static {v4, v6, v0, v8}, Lmx/com/cfe/cfecontigo/App;->e(BSS[Ljava/lang/Object;)V

    const/4 v0, 0x0

    aget-object v4, v8, v0

    check-cast v4, Ljava/lang/String;

    const/4 v6, 0x4

    new-array v7, v6, [Ljava/lang/Class;

    const-class v6, Landroid/content/Context;

    aput-object v6, v7, v0

    sget-object v0, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v6, 0x1

    aput-object v0, v7, v6

    sget-object v0, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    const/4 v6, 0x2

    aput-object v0, v7, v6

    sget-object v0, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v6, 0x3

    aput-object v0, v7, v6

    invoke-virtual {v3, v4, v7}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v3, 0x0

    invoke-virtual {v0, v3, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    move-object v3, v0

    check-cast v3, [Ljava/lang/Object;
    :try_end_c18
    .catchall {:try_start_ba4 .. :try_end_c18} :catchall_3f36

    const v0, 0x3bf4eb60

    .line 523
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_c69

    invoke-static {}, Landroid/os/Process;->myPid()I

    move-result v0

    shr-int/lit8 v0, v0, 0x16

    rsub-int/lit8 v30, v0, 0x15

    invoke-static {}, Landroid/view/ViewConfiguration;->getDoubleTapTimeout()I

    move-result v0

    const/16 v4, 0x10

    shr-int/2addr v0, v4

    int-to-char v0, v0

    const-wide/16 v4, 0x0

    invoke-static {v4, v5}, Landroid/widget/ExpandableListView;->getPackedPositionChild(J)I

    move-result v6

    add-int/lit16 v4, v6, 0x2f8

    const v33, 0x55307c94

    const/16 v34, 0x0

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v6, 0x49

    aget-byte v7, v5, v6

    const/4 v6, 0x1

    sub-int/2addr v7, v6

    int-to-byte v7, v7

    const/16 v8, 0x46

    aget-byte v8, v5, v8

    neg-int v8, v8

    int-to-short v8, v8

    const/16 v14, 0xb2

    aget-byte v5, v5, v14

    sub-int/2addr v5, v6

    int-to-byte v5, v5

    new-array v14, v6, [Ljava/lang/Object;

    invoke-static {v7, v8, v5, v14}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v6, v14, v5

    move-object/from16 v35, v6

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v4

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_c69
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v4, 0x0

    invoke-virtual {v0, v4, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    :try_start_c6f
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v5, 0x0

    .line 528
    new-array v6, v5, [Ljava/lang/Class;

    invoke-virtual {v0, v10, v6}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 531
    new-array v6, v5, [Ljava/lang/Object;

    invoke-virtual {v0, v4, v6}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v4
    :try_end_c86
    .catch Ljava/lang/Exception; {:try_start_c6f .. :try_end_c86} :catch_3f30

    .line 538
    invoke-static {v4, v5}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v6, -0x25c7c814

    invoke-static {v6}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v6

    if-nez v6, :cond_cda

    const/4 v6, 0x0

    invoke-static {v6}, Landroid/graphics/Color;->alpha(I)I

    move-result v7

    rsub-int/lit8 v30, v7, 0x15

    invoke-static {v13}, Landroid/os/Process;->getGidForName(Ljava/lang/String;)I

    move-result v7

    rsub-int/lit8 v7, v7, -0x1

    int-to-char v7, v7

    invoke-static {v6}, Landroid/os/Process;->getThreadPriority(I)I

    move-result v8

    add-int/lit8 v8, v8, 0x14

    const/4 v6, 0x6

    shr-int/2addr v8, v6

    add-int/lit16 v6, v8, 0x2f7

    const v33, -0x4b035fe8

    const/16 v34, 0x0

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v14, 0xe

    aget-byte v14, v8, v14

    const/4 v15, 0x1

    sub-int/2addr v14, v15

    int-to-byte v14, v14

    const/16 v19, 0xc

    aget-byte v8, v8, v19

    int-to-short v8, v8

    const/16 v1, 0x25

    int-to-byte v1, v1

    move-object/from16 v39, v3

    new-array v3, v15, [Ljava/lang/Object;

    invoke-static {v14, v8, v1, v3}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v1, 0x0

    aget-object v3, v3, v1

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v7

    move/from16 v32, v6

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v6

    goto :goto_cdc

    :cond_cda
    move-object/from16 v39, v3

    :goto_cdc
    check-cast v6, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v6, v1, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v0, 0xc

    shr-long v3, v4, v0

    .line 548
    invoke-static {v3, v4}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, 0x316dc05d

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_d2e

    const/4 v1, 0x0

    invoke-static {v13, v13, v1}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;I)I

    move-result v3

    add-int/lit8 v30, v3, 0x15

    invoke-static {v1, v1}, Landroid/view/KeyEvent;->getDeadChar(II)I

    move-result v3

    int-to-char v3, v3

    invoke-static {v13, v13, v1}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;I)I

    move-result v4

    rsub-int v1, v4, 0x2f7

    const v33, 0x5fa957a9

    const/16 v34, 0x0

    const/16 v4, 0x28

    int-to-byte v4, v4

    or-int/lit8 v5, v4, 0x6

    int-to-short v5, v5

    sget-object v6, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v7, 0x1a

    aget-byte v6, v6, v7

    int-to-byte v6, v6

    const/4 v7, 0x1

    new-array v8, v7, [Ljava/lang/Object;

    invoke-static {v4, v5, v6, v8}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v8, v4

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v3

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_d2e
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    move-object/from16 v3, v39

    goto/16 :goto_b39

    .line 565
    :goto_d38
    aget-object v1, v3, v0

    check-cast v1, [I

    const/4 v0, 0x0

    aget v1, v1, v0

    aget-object v4, v3, v0

    check-cast v4, [I

    aget v4, v4, v0

    if-ne v4, v1, :cond_3f16

    const/4 v1, 0x5

    .line 570
    new-array v4, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v5, v1, [I

    aput-object v5, v4, v0

    new-array v6, v1, [I

    const/4 v7, 0x2

    aput-object v6, v4, v7

    new-array v6, v1, [I

    const/4 v8, 0x4

    aput-object v6, v4, v8

    aget-object v14, v3, v7

    check-cast v14, [I

    aget v7, v14, v0

    aget-object v14, v3, v0

    check-cast v14, [I

    aget v14, v14, v0

    aget-object v15, v3, v8

    check-cast v15, [I

    aget v8, v15, v0

    aget-object v15, v3, v1

    check-cast v15, [Ljava/lang/String;

    const/16 v16, 0x3

    aget-object v3, v3, v16

    check-cast v3, [Ljava/lang/Object;

    check-cast v5, [I

    aput v14, v5, v0

    check-cast v6, [I

    aput v8, v6, v0

    aput-object v15, v4, v1

    aput-object v3, v4, v16

    const-string v1, "android.app.ActivityThread"

    invoke-static {v1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    const-string v3, "currentApplication"

    new-array v5, v0, [Ljava/lang/Class;

    invoke-virtual {v1, v3, v5}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/content/Context;

    invoke-virtual {v1}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v1

    invoke-virtual {v1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    const v3, 0x7f13000a

    invoke-virtual {v1, v3}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v1

    const/4 v3, 0x4

    invoke-virtual {v1, v0, v3}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v1

    const/4 v0, 0x2

    invoke-virtual {v1, v0}, Ljava/lang/String;->codePointAt(I)I

    move-result v1

    const v0, 0x16a1bd16

    add-int/2addr v1, v0

    not-int v0, v1

    const v3, 0xaf5fdf2

    or-int/2addr v3, v0

    not-int v3, v3

    const v5, 0x61020009

    or-int/2addr v5, v3

    mul-int/lit16 v5, v5, -0x2c8

    const v6, -0x229c7eeb

    add-int/2addr v6, v5

    const v5, -0x6102000a

    or-int/2addr v0, v5

    not-int v0, v0

    const v5, 0x6bf7fdfb

    or-int/2addr v1, v5

    not-int v1, v1

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, -0x2c8

    add-int/2addr v6, v0

    const v0, -0x6bd2f91c

    or-int/2addr v0, v3

    mul-int/lit16 v0, v0, 0x2c8

    add-int/2addr v6, v0

    add-int/2addr v7, v6

    shl-int/lit8 v0, v7, 0xd

    xor-int/2addr v0, v7

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v3, v4, v1

    check-cast v3, [I

    const/4 v1, 0x0

    aput v0, v3, v1

    const v0, 0x84c83a0

    .line 677
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_e36

    const v0, -0xffffe8

    invoke-static {v1, v1, v1}, Landroid/graphics/Color;->rgb(III)I

    move-result v3

    sub-int v30, v0, v3

    invoke-static {}, Landroid/view/KeyEvent;->getModifierMetaStateMask()I

    move-result v0

    int-to-byte v0, v0

    const/4 v3, 0x1

    add-int/2addr v0, v3

    int-to-char v0, v0

    const/16 v5, 0x30

    invoke-static {v13, v5, v1, v1}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CII)I

    move-result v6

    add-int/lit16 v1, v6, 0x2cb

    const v33, 0x66881454

    const/16 v34, 0x0

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v6, 0x2

    aget-byte v7, v5, v6

    int-to-byte v6, v7

    const/16 v7, 0x49

    aget-byte v5, v5, v7

    sub-int/2addr v5, v3

    int-to-short v5, v5

    or-int/lit8 v7, v5, 0x2e

    int-to-byte v7, v7

    new-array v8, v3, [Ljava/lang/Object;

    invoke-static {v6, v5, v7, v8}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v5, v8, v3

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_e36
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v5

    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v3, 0x0

    .line 681
    new-array v7, v3, [Ljava/lang/Class;

    invoke-virtual {v0, v10, v7}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 685
    new-array v7, v3, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v7}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0

    const v3, 0x39daac37

    invoke-static {v3}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v3

    if-nez v3, :cond_ea8

    const/16 v3, 0x30

    invoke-static {v3}, Landroid/text/AndroidCharacter;->getMirror(C)C

    move-result v7

    rsub-int/lit8 v30, v7, 0x48

    invoke-static {v13, v13}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)I

    move-result v3

    int-to-char v3, v3

    const-wide/16 v7, 0x0

    invoke-static {v7, v8}, Landroid/widget/ExpandableListView;->getPackedPositionChild(J)I

    move-result v14

    rsub-int v7, v14, 0x2c9

    const v33, 0x571e3bc3

    const/16 v34, 0x0

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v14, 0x49

    aget-byte v15, v8, v14

    const/4 v14, 0x1

    sub-int/2addr v15, v14

    int-to-byte v15, v15

    const/16 v19, 0x46

    aget-byte v14, v8, v19

    neg-int v14, v14

    int-to-short v14, v14

    const/16 v19, 0xb2

    aget-byte v8, v8, v19

    move-object/from16 v39, v4

    const/4 v4, 0x1

    sub-int/2addr v8, v4

    int-to-byte v8, v8

    move-object/from16 v40, v13

    new-array v13, v4, [Ljava/lang/Object;

    invoke-static {v15, v14, v8, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v8, v13, v4

    move-object/from16 v35, v8

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v3

    move/from16 v32, v7

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v3

    goto :goto_eac

    :cond_ea8
    move-object/from16 v39, v4

    move-object/from16 v40, v13

    :goto_eac
    check-cast v3, Ljava/lang/reflect/Field;

    const/4 v4, 0x0

    invoke-virtual {v3, v4}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v7

    const/16 v3, 0x34

    shl-long/2addr v7, v3

    ushr-long/2addr v7, v3

    sub-long/2addr v0, v7

    const/16 v3, 0xc

    shr-long/2addr v0, v3

    cmp-long v0, v5, v0

    if-nez v0, :cond_fa7

    .line 3097
    sget v0, Lmx/com/cfe/cfecontigo/App;->RatingCompat:I

    add-int/lit8 v0, v0, 0x2f

    rem-int/lit16 v1, v0, 0x80

    sput v1, Lmx/com/cfe/cfecontigo/App;->MediaBrowserCompatSearchResultReceiver:I

    const/4 v1, 0x2

    rem-int/2addr v0, v1

    const v0, -0x782f793d

    .line 696
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_f18

    invoke-static {}, Landroid/view/ViewConfiguration;->getJumpTapTimeout()I

    move-result v0

    const/16 v1, 0x10

    shr-int/2addr v0, v1

    rsub-int/lit8 v30, v0, 0x18

    invoke-static {}, Landroid/os/Process;->myPid()I

    move-result v0

    shr-int/lit8 v0, v0, 0x16

    int-to-char v0, v0

    invoke-static {}, Landroid/os/SystemClock;->currentThreadTimeMillis()J

    move-result-wide v3

    const-wide/16 v5, -0x1

    cmp-long v1, v3, v5

    add-int/lit16 v1, v1, 0x2c9

    const v33, -0x16ebeec9

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v4, 0xe

    aget-byte v4, v3, v4

    const/4 v5, 0x1

    sub-int/2addr v4, v5

    int-to-byte v4, v4

    const/16 v6, 0xc

    aget-byte v3, v3, v6

    int-to-short v3, v3

    const/16 v6, 0x25

    int-to-byte v6, v6

    new-array v7, v5, [Ljava/lang/Object;

    invoke-static {v4, v3, v6, v7}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v7, v3

    move-object/from16 v35, v4

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_f18
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/Object;

    const/4 v1, 0x4

    new-array v3, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v4, v1, [I

    aput-object v4, v3, v1

    new-array v5, v1, [I

    const/4 v6, 0x2

    aput-object v5, v3, v6

    new-array v5, v1, [I

    const/4 v6, 0x3

    aput-object v5, v3, v6

    .line 703
    aget-object v7, v0, v6

    check-cast v7, [I

    const/4 v6, 0x0

    aget v7, v7, v6

    aget-object v8, v0, v1

    check-cast v8, [I

    aget v1, v8, v6

    aget-object v0, v0, v6

    check-cast v0, [Ljava/lang/String;

    check-cast v5, [I

    aput v7, v5, v6

    check-cast v4, [I

    aput v1, v4, v6

    aput-object v0, v3, v6

    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-string v1, "currentApplication"

    new-array v4, v6, [Ljava/lang/Class;

    invoke-virtual {v0, v1, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/Context;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;

    move-result-object v0

    iget v0, v0, Landroid/content/pm/ApplicationInfo;->targetSdkVersion:I

    const v1, -0x1a96fd10

    add-int/2addr v0, v1

    not-int v1, v0

    const v4, -0x200050db

    or-int v5, v4, v1

    not-int v5, v5

    const v6, 0x478cca21

    or-int v7, v0, v6

    not-int v7, v7

    or-int/2addr v5, v7

    mul-int/lit16 v5, v5, 0x14d

    const v7, -0x26b9225d

    add-int/2addr v7, v5

    or-int/2addr v0, v4

    not-int v0, v0

    or-int/2addr v1, v6

    not-int v1, v1

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, 0x14d

    add-int/2addr v7, v0

    const v0, 0x71147cd2

    add-int/2addr v7, v0

    shl-int/lit8 v0, v7, 0xd

    xor-int/2addr v0, v7

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v4, v3, v1

    check-cast v4, [I

    const/4 v1, 0x0

    aput v0, v4, v1

    move-object/from16 v7, v40

    :goto_fa4
    const/4 v0, 0x1

    goto/16 :goto_11ae

    .line 713
    :cond_fa7
    invoke-virtual/range {p0 .. p0}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    move-result-object v0

    if-nez v0, :cond_fc9

    .line 3097
    sget v0, Lmx/com/cfe/cfecontigo/App;->MediaBrowserCompatSearchResultReceiver:I

    add-int/lit8 v0, v0, 0x31

    rem-int/lit16 v1, v0, 0x80

    sput v1, Lmx/com/cfe/cfecontigo/App;->RatingCompat:I

    const/4 v1, 0x2

    rem-int/2addr v0, v1

    .line 722
    invoke-static/range {v25 .. v25}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v1, 0x0

    new-array v3, v1, [Ljava/lang/Class;

    invoke-virtual {v0, v2, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    .line 729
    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    :cond_fc9
    if-eqz v0, :cond_fdf

    .line 733
    instance-of v1, v0, Landroid/content/ContextWrapper;

    if-eqz v1, :cond_fdb

    .line 738
    move-object v1, v0

    check-cast v1, Landroid/content/ContextWrapper;

    invoke-virtual {v1}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    move-result-object v1

    if-eqz v1, :cond_fd9

    goto :goto_fdb

    :cond_fd9
    const/4 v0, 0x0

    goto :goto_fdf

    .line 744
    :cond_fdb
    :goto_fdb
    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    .line 753
    :cond_fdf
    :goto_fdf
    invoke-static {v11}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    .line 768
    const-class v3, Ljava/lang/Object;

    .line 769
    filled-new-array {v3}, [Ljava/lang/Class;

    move-result-object v3

    invoke-virtual {v1, v12, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    .line 779
    filled-new-array/range {p0 .. p0}, [Ljava/lang/Object;

    move-result-object v3

    const/4 v4, 0x0

    invoke-virtual {v1, v4, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Number;->intValue()I

    move-result v1

    const/4 v3, 0x4

    .line 791
    :try_start_ffd
    new-array v4, v3, [Ljava/lang/Object;

    const v3, 0x71147cd2

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v3

    const/4 v5, 0x3

    aput-object v3, v4, v5

    const-wide/16 v5, 0x0

    invoke-static {v5, v6}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v3

    const/4 v5, 0x2

    aput-object v3, v4, v5

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    const/4 v3, 0x1

    aput-object v1, v4, v3

    const/4 v1, 0x0

    aput-object v0, v4, v1

    sget-object v1, Lmx/com/cfe/cfecontigo/App;->$$d:[B

    const/16 v3, 0x20

    aget-byte v3, v1, v3

    int-to-byte v3, v3

    const/16 v5, 0x74

    int-to-short v5, v5

    const/16 v6, 0x6a

    aget-byte v6, v1, v6

    const/4 v7, 0x1

    sub-int/2addr v6, v7

    int-to-byte v6, v6

    new-array v8, v7, [Ljava/lang/Object;

    invoke-static {v3, v5, v6, v8}, Lmx/com/cfe/cfecontigo/App;->e(BSS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v5, v8, v3

    check-cast v5, Ljava/lang/String;

    invoke-static {v5}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v3

    const/16 v5, 0xc

    aget-byte v6, v1, v5

    int-to-byte v5, v6

    or-int/lit8 v6, v5, 0x51

    int-to-short v6, v6

    const/16 v7, 0x44

    aget-byte v1, v1, v7

    int-to-byte v1, v1

    const/4 v7, 0x1

    new-array v8, v7, [Ljava/lang/Object;

    invoke-static {v5, v6, v1, v8}, Lmx/com/cfe/cfecontigo/App;->e(BSS[Ljava/lang/Object;)V

    const/4 v1, 0x0

    aget-object v5, v8, v1

    check-cast v5, Ljava/lang/String;

    const/4 v6, 0x4

    new-array v7, v6, [Ljava/lang/Class;

    const-class v6, Landroid/content/Context;

    aput-object v6, v7, v1

    sget-object v1, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v6, 0x1

    aput-object v1, v7, v6

    sget-object v1, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    const/4 v6, 0x2

    aput-object v1, v7, v6

    sget-object v1, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v6, 0x3

    aput-object v1, v7, v6

    invoke-virtual {v3, v5, v7}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v4}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    move-object v3, v1

    check-cast v3, [Ljava/lang/Object;
    :try_end_1075
    .catchall {:try_start_ffd .. :try_end_1075} :catchall_3f36

    if-eqz v0, :cond_11a6

    const v0, -0x782f793d

    .line 800
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_10c2

    invoke-static {}, Landroid/view/ViewConfiguration;->getJumpTapTimeout()I

    move-result v0

    const/16 v1, 0x10

    shr-int/2addr v0, v1

    add-int/lit8 v30, v0, 0x18

    const/4 v0, 0x0

    invoke-static {v0}, Landroid/view/View$MeasureSpec;->getMode(I)I

    move-result v1

    int-to-char v0, v1

    invoke-static {}, Landroid/view/KeyEvent;->getModifierMetaStateMask()I

    move-result v1

    int-to-byte v1, v1

    rsub-int v1, v1, 0x2c9

    const v33, -0x16ebeec9

    const/16 v34, 0x0

    sget-object v4, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v5, 0xe

    aget-byte v5, v4, v5

    const/4 v6, 0x1

    sub-int/2addr v5, v6

    int-to-byte v5, v5

    const/16 v7, 0xc

    aget-byte v4, v4, v7

    int-to-short v4, v4

    const/16 v7, 0x25

    int-to-byte v7, v7

    new-array v8, v6, [Ljava/lang/Object;

    invoke-static {v5, v4, v7, v8}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v8, v4

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_10c2
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    .line 805
    :try_start_10c8
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v4, 0x0

    new-array v5, v4, [Ljava/lang/Class;

    invoke-virtual {v0, v10, v5}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 812
    new-array v5, v4, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0
    :try_end_10df
    .catch Ljava/lang/Exception; {:try_start_10c8 .. :try_end_10df} :catch_11a0

    .line 821
    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v4

    const v5, 0x39daac37

    invoke-static {v5}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v5

    if-nez v5, :cond_113b

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v5

    const-wide/16 v7, 0x0

    cmp-long v5, v5, v7

    add-int/lit8 v30, v5, 0x17

    invoke-static/range {v40 .. v40}, Landroid/os/Process;->getGidForName(Ljava/lang/String;)I

    move-result v5

    rsub-int/lit8 v5, v5, -0x1

    int-to-char v5, v5

    move-object/from16 v7, v40

    const/16 v6, 0x30

    const/4 v8, 0x0

    invoke-static {v7, v6, v8, v8}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CII)I

    move-result v13

    add-int/lit16 v6, v13, 0x2cb

    const v33, 0x571e3bc3

    const/16 v34, 0x0

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v13, 0x49

    aget-byte v14, v8, v13

    const/4 v13, 0x1

    sub-int/2addr v14, v13

    int-to-byte v14, v14

    const/16 v15, 0x46

    aget-byte v15, v8, v15

    neg-int v15, v15

    int-to-short v15, v15

    const/16 v19, 0xb2

    aget-byte v8, v8, v19

    sub-int/2addr v8, v13

    int-to-byte v8, v8

    move-object/from16 v40, v3

    new-array v3, v13, [Ljava/lang/Object;

    invoke-static {v14, v15, v8, v3}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v8, 0x0

    aget-object v3, v3, v8

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v5

    move/from16 v32, v6

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v5

    goto :goto_113f

    :cond_113b
    move-object/from16 v7, v40

    move-object/from16 v40, v3

    :goto_113f
    check-cast v5, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v5, v3, v4}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v3, 0xc

    shr-long/2addr v0, v3

    .line 828
    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, 0x84c83a0

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_1199

    invoke-static {}, Landroid/view/ViewConfiguration;->getWindowTouchSlop()I

    move-result v1

    shr-int/lit8 v1, v1, 0x8

    rsub-int/lit8 v30, v1, 0x18

    const/high16 v1, -0x1000000

    const/4 v3, 0x0

    invoke-static {v3, v3, v3}, Landroid/graphics/Color;->rgb(III)I

    move-result v4

    sub-int/2addr v1, v4

    int-to-char v1, v1

    const/16 v4, 0x30

    invoke-static {v7, v4, v3, v3}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;CII)I

    move-result v5

    rsub-int v3, v5, 0x2c9

    const v33, 0x66881454

    const/16 v34, 0x0

    sget-object v4, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v5, 0x2

    aget-byte v6, v4, v5

    int-to-byte v5, v6

    const/16 v6, 0x49

    aget-byte v4, v4, v6

    const/4 v6, 0x1

    sub-int/2addr v4, v6

    int-to-short v4, v4

    or-int/lit8 v8, v4, 0x2e

    int-to-byte v8, v8

    new-array v13, v6, [Ljava/lang/Object;

    invoke-static {v5, v4, v8, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v13, v4

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v1

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_1199
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    goto :goto_11aa

    .line 832
    :catch_11a0
    new-instance v0, Ljava/lang/RuntimeException;

    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    throw v0

    :cond_11a6
    move-object/from16 v7, v40

    move-object/from16 v40, v3

    :goto_11aa
    move-object/from16 v3, v40

    goto/16 :goto_fa4

    .line 841
    :goto_11ae
    aget-object v1, v3, v0

    check-cast v1, [I

    const/4 v4, 0x0

    aget v1, v1, v4

    const/4 v5, 0x3

    .line 843
    aget-object v6, v3, v5

    check-cast v6, [I

    aget v6, v6, v4

    if-ne v6, v1, :cond_123a

    const/4 v1, 0x4

    new-array v6, v1, [Ljava/lang/Object;

    new-array v1, v0, [I

    aput-object v1, v6, v0

    new-array v8, v0, [I

    const/4 v13, 0x2

    aput-object v8, v6, v13

    new-array v8, v0, [I

    aput-object v8, v6, v5

    .line 849
    aget-object v14, v3, v13

    check-cast v14, [I

    aget v13, v14, v4

    aget-object v14, v3, v5

    check-cast v14, [I

    aget v5, v14, v4

    aget-object v14, v3, v0

    check-cast v14, [I

    aget v0, v14, v4

    aget-object v3, v3, v4

    check-cast v3, [Ljava/lang/String;

    check-cast v8, [I

    aput v5, v8, v4

    check-cast v1, [I

    aput v0, v1, v4

    aput-object v3, v6, v4

    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-string v1, "currentApplication"

    new-array v3, v4, [Ljava/lang/Class;

    invoke-virtual {v0, v1, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/res/Resources;->getDisplayMetrics()Landroid/util/DisplayMetrics;

    move-result-object v0

    iget v0, v0, Landroid/util/DisplayMetrics;->heightPixels:I

    or-int/lit16 v1, v0, -0x20a1

    mul-int/lit16 v1, v1, -0x17d

    const v3, -0x386631b6

    add-int/2addr v3, v1

    not-int v0, v0

    const v1, 0x36ecde5b

    or-int/2addr v0, v1

    not-int v0, v0

    const v1, -0x64ce2fc

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, 0x17d

    add-int/2addr v3, v0

    const v0, 0x308e20

    add-int/2addr v3, v0

    add-int/2addr v13, v3

    shl-int/lit8 v0, v13, 0xd

    xor-int/2addr v0, v13

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v3, v6, v1

    check-cast v3, [I

    const/4 v4, 0x0

    aput v0, v3, v4

    goto/16 :goto_12cb

    :cond_123a
    const/4 v1, 0x2

    add-int/lit8 v0, v6, -0x1

    mul-int/2addr v0, v6

    .line 867
    rem-int/2addr v0, v1

    .line 870
    div-int/2addr v6, v0

    const/4 v0, 0x0

    invoke-static {v0, v6, v4}, Landroid/widget/Toast;->makeText(Landroid/content/Context;II)Landroid/widget/Toast;

    move-result-object v5

    .line 876
    invoke-virtual {v5}, Landroid/widget/Toast;->show()V

    const/4 v0, 0x4

    .line 863
    new-array v6, v0, [Ljava/lang/Object;

    const/4 v0, 0x1

    new-array v5, v0, [I

    aput-object v5, v6, v0

    new-array v8, v0, [I

    aput-object v8, v6, v1

    new-array v8, v0, [I

    const/4 v13, 0x3

    aput-object v8, v6, v13

    aget-object v14, v3, v1

    check-cast v14, [I

    aget v1, v14, v4

    .line 878
    aget-object v14, v3, v13

    check-cast v14, [I

    aget v13, v14, v4

    aget-object v14, v3, v0

    check-cast v14, [I

    aget v0, v14, v4

    aget-object v3, v3, v4

    check-cast v3, [Ljava/lang/String;

    check-cast v8, [I

    aput v13, v8, v4

    check-cast v5, [I

    aput v0, v5, v4

    aput-object v3, v6, v4

    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-string v3, "currentApplication"

    new-array v5, v4, [Ljava/lang/Class;

    invoke-virtual {v0, v3, v5}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v3, 0x0

    invoke-virtual {v0, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/res/Resources;->getConfiguration()Landroid/content/res/Configuration;

    move-result-object v0

    iget v0, v0, Landroid/content/res/Configuration;->keyboard:I

    not-int v0, v0

    const v3, 0x7802c56

    or-int/2addr v3, v0

    not-int v3, v3

    const v4, 0x600ceea5

    or-int v5, v3, v4

    mul-int/lit16 v5, v5, 0x2fc

    const v8, 0x7ce102cb

    add-int/2addr v8, v5

    or-int/2addr v0, v4

    not-int v0, v0

    const v4, 0x7800052

    or-int/2addr v0, v4

    mul-int/lit16 v0, v0, -0x5f8

    add-int/2addr v8, v0

    const v0, 0x678cc2f3

    or-int/2addr v0, v3

    mul-int/lit16 v0, v0, 0x2fc

    add-int/2addr v8, v0

    add-int/2addr v1, v8

    shl-int/lit8 v0, v1, 0xd

    xor-int/2addr v0, v1

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v3, v6, v1

    check-cast v3, [I

    const/4 v1, 0x0

    aput v0, v3, v1

    :goto_12cb
    const v0, 0x22118af

    .line 890
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_131a

    const/4 v1, 0x0

    invoke-static {v1, v1}, Landroid/graphics/PointF;->length(FF)F

    move-result v0

    cmpl-float v0, v0, v1

    add-int/lit8 v30, v0, 0x18

    const-wide/16 v0, 0x0

    invoke-static {v0, v1}, Landroid/widget/ExpandableListView;->getPackedPositionChild(J)I

    move-result v3

    const/4 v0, 0x1

    add-int/2addr v3, v0

    int-to-char v0, v3

    const/4 v1, 0x0

    invoke-static {v1, v1, v1}, Landroid/view/View;->resolveSizeAndState(III)I

    move-result v3

    add-int/lit16 v1, v3, 0x2ca

    const v33, 0x6ce58f5b

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v4, 0xac

    aget-byte v4, v3, v4

    neg-int v4, v4

    int-to-byte v4, v4

    const/16 v5, 0x5c

    int-to-short v5, v5

    const/16 v8, 0x49

    aget-byte v3, v3, v8

    const/4 v8, 0x1

    sub-int/2addr v3, v8

    int-to-byte v3, v3

    new-array v13, v8, [Ljava/lang/Object;

    invoke-static {v4, v5, v3, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v13, v3

    move-object/from16 v35, v4

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_131a
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v3

    .line 897
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v5, 0x0

    new-array v8, v5, [Ljava/lang/Class;

    invoke-virtual {v0, v10, v8}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 905
    new-array v8, v5, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v8}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    .line 913
    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0

    const v8, 0x196e6434

    invoke-static {v8}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v8

    if-nez v8, :cond_1389

    invoke-static {v5, v5, v5, v5}, Landroid/graphics/Color;->argb(IIII)I

    move-result v8

    rsub-int/lit8 v30, v8, 0x18

    invoke-static {}, Landroid/view/ViewConfiguration;->getGlobalActionKeyTimeout()J

    move-result-wide v13

    const-wide/16 v27, 0x0

    cmp-long v5, v13, v27

    const/4 v8, 0x1

    rsub-int/lit8 v5, v5, 0x1

    int-to-char v5, v5

    invoke-static {}, Landroid/os/Process;->getElapsedCpuTime()J

    move-result-wide v13

    cmp-long v8, v13, v27

    rsub-int v8, v8, 0x2cb

    const v33, 0x77aaf3c0

    const/16 v34, 0x0

    const/16 v13, 0x28

    int-to-byte v13, v13

    or-int/lit8 v14, v13, 0x6

    int-to-short v14, v14

    sget-object v15, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v26, 0x1a

    aget-byte v15, v15, v26

    int-to-byte v15, v15

    move-object/from16 v40, v6

    move-object/from16 v41, v10

    const/4 v6, 0x1

    new-array v10, v6, [Ljava/lang/Object;

    invoke-static {v13, v14, v15, v10}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v6, 0x0

    aget-object v10, v10, v6

    move-object/from16 v35, v10

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v5

    move/from16 v32, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v8

    goto :goto_138d

    :cond_1389
    move-object/from16 v40, v6

    move-object/from16 v41, v10

    :goto_138d
    check-cast v8, Ljava/lang/reflect/Field;

    const/4 v5, 0x0

    invoke-virtual {v8, v5}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v13

    const/16 v5, 0x34

    shl-long/2addr v13, v5

    ushr-long/2addr v13, v5

    sub-long/2addr v0, v13

    const/16 v5, 0xc

    shr-long/2addr v0, v5

    cmp-long v0, v3, v0

    if-nez v0, :cond_1475

    const v0, 0x21e3c858

    .line 920
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_13f2

    const/16 v0, 0x30

    const/4 v1, 0x0

    invoke-static {v7, v0, v1, v1}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CII)I

    move-result v3

    add-int/lit8 v30, v3, 0x19

    invoke-static {}, Landroid/view/ViewConfiguration;->getTapTimeout()I

    move-result v0

    const/16 v3, 0x10

    shr-int/2addr v0, v3

    int-to-char v0, v0

    invoke-static {v1}, Landroid/os/Process;->getThreadPriority(I)I

    move-result v3

    add-int/lit8 v3, v3, 0x14

    const/4 v1, 0x6

    shr-int/2addr v3, v1

    rsub-int v1, v3, 0x2ca

    const v33, 0x4f275fac

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v4, 0x20

    aget-byte v4, v3, v4

    int-to-byte v4, v4

    sget v5, Lmx/com/cfe/cfecontigo/App;->$$b:I

    and-int/lit16 v5, v5, 0x178

    int-to-short v5, v5

    const/16 v6, 0xb2

    aget-byte v3, v3, v6

    const/4 v6, 0x1

    sub-int/2addr v3, v6

    int-to-byte v3, v3

    new-array v8, v6, [Ljava/lang/Object;

    invoke-static {v4, v5, v3, v8}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v8, v3

    move-object/from16 v35, v4

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_13f2
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/Object;

    const/4 v1, 0x4

    new-array v3, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v4, v1, [I

    aput-object v4, v3, v1

    new-array v5, v1, [I

    const/4 v6, 0x2

    aput-object v5, v3, v6

    new-array v5, v1, [I

    const/4 v6, 0x3

    aput-object v5, v3, v6

    .line 928
    aget-object v8, v0, v6

    check-cast v8, [I

    const/4 v6, 0x0

    aget v8, v8, v6

    aget-object v10, v0, v1

    check-cast v10, [I

    aget v1, v10, v6

    aget-object v0, v0, v6

    check-cast v0, [Ljava/lang/String;

    check-cast v5, [I

    aput v8, v5, v6

    check-cast v4, [I

    aput v1, v4, v6

    aput-object v0, v3, v6

    invoke-static {}, Ljava/lang/Runtime;->getRuntime()Ljava/lang/Runtime;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Runtime;->totalMemory()J

    move-result-wide v0

    long-to-int v0, v0

    not-int v1, v0

    const v4, 0x2d361148

    or-int/2addr v4, v1

    not-int v4, v4

    const v5, 0x124108b3

    or-int/2addr v4, v5

    mul-int/lit16 v4, v4, -0x4a4

    const v6, -0x96695cf

    add-int/2addr v6, v4

    const v4, -0x2d361149

    or-int/2addr v0, v4

    not-int v0, v0

    or-int/2addr v0, v5

    const v5, 0x3a5709b3

    or-int/2addr v5, v1

    not-int v5, v5

    or-int/2addr v0, v5

    mul-int/lit16 v0, v0, 0x252

    add-int/2addr v6, v0

    or-int v0, v4, v1

    not-int v0, v0

    const v1, 0x5201048

    or-int/2addr v0, v1

    or-int/2addr v0, v5

    mul-int/lit16 v0, v0, 0x252

    add-int/2addr v6, v0

    const v0, -0x7a2ddf78

    add-int/2addr v6, v0

    shl-int/lit8 v0, v6, 0xd

    xor-int/2addr v0, v6

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v4, v3, v1

    check-cast v4, [I

    const/4 v1, 0x0

    aput v0, v4, v1

    move-object/from16 v6, v41

    :goto_1472
    const/4 v0, 0x1

    goto/16 :goto_165d

    :cond_1475
    const/4 v1, 0x0

    .line 934
    invoke-virtual/range {p0 .. p0}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    move-result-object v0

    if-nez v0, :cond_148d

    .line 944
    invoke-static/range {v25 .. v25}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    .line 951
    new-array v3, v1, [Ljava/lang/Class;

    .line 958
    invoke-virtual {v0, v2, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    .line 968
    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    :cond_148d
    if-eqz v0, :cond_14a3

    .line 975
    instance-of v1, v0, Landroid/content/ContextWrapper;

    if-eqz v1, :cond_149f

    move-object v1, v0

    check-cast v1, Landroid/content/ContextWrapper;

    invoke-virtual {v1}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    move-result-object v1

    if-eqz v1, :cond_149d

    goto :goto_149f

    :cond_149d
    const/4 v0, 0x0

    goto :goto_14a3

    .line 981
    :cond_149f
    :goto_149f
    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    .line 992
    :cond_14a3
    :goto_14a3
    invoke-static {v11}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    .line 993
    const-class v3, Ljava/lang/Object;

    filled-new-array {v3}, [Ljava/lang/Class;

    move-result-object v3

    .line 1001
    invoke-virtual {v1, v12, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    .line 1014
    filled-new-array/range {p0 .. p0}, [Ljava/lang/Object;

    move-result-object v3

    const/4 v4, 0x0

    .line 1024
    invoke-virtual {v1, v4, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Number;->intValue()I

    move-result v1

    const/4 v3, 0x3

    :try_start_14c1
    new-array v4, v3, [Ljava/lang/Object;

    const v3, -0x7a2ddf78

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v3

    const/4 v5, 0x2

    aput-object v3, v4, v5

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    const/4 v3, 0x1

    aput-object v1, v4, v3

    const/4 v1, 0x0

    aput-object v0, v4, v1

    sget v1, Lmx/com/cfe/cfecontigo/App;->$$e:I

    and-int/lit8 v3, v1, 0x38

    int-to-byte v3, v3

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$d:[B

    const/16 v6, 0x6a

    aget-byte v6, v5, v6

    const/4 v8, 0x1

    sub-int/2addr v6, v8

    int-to-short v6, v6

    int-to-byte v10, v6

    new-array v13, v8, [Ljava/lang/Object;

    invoke-static {v3, v6, v10, v13}, Lmx/com/cfe/cfecontigo/App;->e(BSS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v6, v13, v3

    check-cast v6, Ljava/lang/String;

    invoke-static {v6}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v3

    and-int/lit8 v1, v1, 0x38

    int-to-byte v1, v1

    const/16 v6, 0x50

    aget-byte v6, v5, v6

    int-to-short v6, v6

    const/16 v8, 0xc

    aget-byte v5, v5, v8

    int-to-byte v5, v5

    const/4 v8, 0x1

    new-array v10, v8, [Ljava/lang/Object;

    invoke-static {v1, v6, v5, v10}, Lmx/com/cfe/cfecontigo/App;->e(BSS[Ljava/lang/Object;)V

    const/4 v1, 0x0

    aget-object v5, v10, v1

    check-cast v5, Ljava/lang/String;

    const/4 v6, 0x3

    new-array v8, v6, [Ljava/lang/Class;

    const-class v6, Landroid/content/Context;

    aput-object v6, v8, v1

    sget-object v1, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v6, 0x1

    aput-object v1, v8, v6

    sget-object v1, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v6, 0x2

    aput-object v1, v8, v6

    invoke-virtual {v3, v5, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v4}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    move-object v3, v1

    check-cast v3, [Ljava/lang/Object;
    :try_end_1529
    .catchall {:try_start_14c1 .. :try_end_1529} :catchall_3f36

    if-eqz v0, :cond_1655

    const v0, 0x21e3c858

    .line 1030
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_1579

    const/4 v1, 0x0

    const/4 v4, 0x0

    invoke-static {v4, v1, v1}, Landroid/util/TypedValue;->complexToFraction(IFF)F

    move-result v0

    cmpl-float v0, v0, v1

    rsub-int/lit8 v30, v0, 0x18

    invoke-static {v4, v1, v1}, Landroid/util/TypedValue;->complexToFraction(IFF)F

    move-result v0

    cmpl-float v0, v0, v1

    int-to-char v0, v0

    invoke-static {v4, v4}, Landroid/view/View;->resolveSize(II)I

    move-result v1

    add-int/lit16 v1, v1, 0x2ca

    const v33, 0x4f275fac

    const/16 v34, 0x0

    sget-object v4, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v5, 0x20

    aget-byte v5, v4, v5

    int-to-byte v5, v5

    sget v6, Lmx/com/cfe/cfecontigo/App;->$$b:I

    and-int/lit16 v6, v6, 0x178

    int-to-short v6, v6

    const/16 v8, 0xb2

    aget-byte v4, v4, v8

    const/4 v8, 0x1

    sub-int/2addr v4, v8

    int-to-byte v4, v4

    new-array v10, v8, [Ljava/lang/Object;

    invoke-static {v5, v6, v4, v10}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v10, v4

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_1579
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    :try_start_157f
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v4, 0x0

    .line 1039
    new-array v5, v4, [Ljava/lang/Class;

    move-object/from16 v6, v41

    .line 1041
    invoke-virtual {v0, v6, v5}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    new-array v5, v4, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    .line 1045
    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0
    :try_end_1598
    .catch Ljava/lang/Exception; {:try_start_157f .. :try_end_1598} :catch_164f

    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v4

    const v5, 0x196e6434

    invoke-static {v5}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v5

    if-nez v5, :cond_15e8

    invoke-static {}, Landroid/view/ViewConfiguration;->getScrollBarFadeDuration()I

    move-result v5

    const/16 v8, 0x10

    shr-int/2addr v5, v8

    add-int/lit8 v30, v5, 0x18

    invoke-static {}, Landroid/os/Process;->myPid()I

    move-result v5

    shr-int/lit8 v5, v5, 0x16

    int-to-char v5, v5

    invoke-static {}, Landroid/view/ViewConfiguration;->getScrollDefaultDelay()I

    move-result v10

    shr-int/2addr v10, v8

    add-int/lit16 v8, v10, 0x2ca

    const v33, 0x77aaf3c0

    const/16 v34, 0x0

    const/16 v10, 0x28

    int-to-byte v10, v10

    or-int/lit8 v13, v10, 0x6

    int-to-short v13, v13

    sget-object v14, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v15, 0x1a

    aget-byte v14, v14, v15

    int-to-byte v14, v14

    move-object/from16 v41, v3

    const/4 v15, 0x1

    new-array v3, v15, [Ljava/lang/Object;

    invoke-static {v10, v13, v14, v3}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v10, 0x0

    aget-object v3, v3, v10

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v5

    move/from16 v32, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v5

    goto :goto_15ea

    :cond_15e8
    move-object/from16 v41, v3

    :goto_15ea
    check-cast v5, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v5, v3, v4}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v3, 0xc

    shr-long/2addr v0, v3

    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, 0x22118af

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_1648

    const/4 v3, 0x0

    invoke-static {v7, v3, v3}, Landroid/text/TextUtils;->getCapsMode(Ljava/lang/CharSequence;II)I

    move-result v1

    add-int/lit8 v30, v1, 0x18

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v3

    const-wide/16 v13, 0x0

    cmp-long v1, v3, v13

    add-int/lit8 v1, v1, -0x1

    int-to-char v1, v1

    invoke-static {}, Landroid/media/AudioTrack;->getMaxVolume()F

    move-result v3

    const/4 v4, 0x0

    cmpl-float v3, v3, v4

    add-int/lit16 v3, v3, 0x2c9

    const v33, 0x6ce58f5b

    const/16 v34, 0x0

    sget-object v4, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v5, 0xac

    aget-byte v5, v4, v5

    neg-int v5, v5

    int-to-byte v5, v5

    const/16 v8, 0x5c

    int-to-short v8, v8

    const/16 v10, 0x49

    aget-byte v4, v4, v10

    const/4 v10, 0x1

    sub-int/2addr v4, v10

    int-to-byte v4, v4

    new-array v13, v10, [Ljava/lang/Object;

    invoke-static {v5, v8, v4, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v13, v4

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v1

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_1648
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    goto :goto_1659

    :catch_164f
    new-instance v0, Ljava/lang/RuntimeException;

    .line 1047
    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    throw v0

    :cond_1655
    move-object/from16 v6, v41

    move-object/from16 v41, v3

    :goto_1659
    move-object/from16 v3, v41

    goto/16 :goto_1472

    .line 1050
    :goto_165d
    aget-object v1, v3, v0

    check-cast v1, [I

    const/4 v4, 0x0

    aget v1, v1, v4

    const/4 v5, 0x3

    aget-object v8, v3, v5

    check-cast v8, [I

    aget v8, v8, v4

    if-ne v8, v1, :cond_16d8

    const/4 v1, 0x4

    .line 1059
    new-array v8, v1, [Ljava/lang/Object;

    new-array v1, v0, [I

    aput-object v1, v8, v0

    new-array v10, v0, [I

    const/4 v13, 0x2

    aput-object v10, v8, v13

    new-array v10, v0, [I

    aput-object v10, v8, v5

    aget-object v14, v3, v13

    check-cast v14, [I

    aget v13, v14, v4

    aget-object v14, v3, v5

    check-cast v14, [I

    aget v5, v14, v4

    aget-object v14, v3, v0

    check-cast v14, [I

    aget v0, v14, v4

    aget-object v3, v3, v4

    check-cast v3, [Ljava/lang/String;

    check-cast v10, [I

    aput v5, v10, v4

    check-cast v1, [I

    aput v0, v1, v4

    aput-object v3, v8, v4

    invoke-static {}, Landroid/os/Process;->getElapsedCpuTime()J

    move-result-wide v0

    long-to-int v0, v0

    not-int v1, v0

    const v3, -0x1942698d

    or-int/2addr v3, v1

    not-int v3, v3

    const v4, 0x11004880

    or-int/2addr v3, v4

    const v4, -0x46089064

    or-int/2addr v0, v4

    not-int v0, v0

    or-int/2addr v3, v0

    mul-int/lit16 v3, v3, -0x2c9

    const v4, -0x4c831c40

    add-int/2addr v4, v3

    mul-int/lit16 v0, v0, 0x592

    add-int/2addr v4, v0

    const v0, -0x4e4ab170

    or-int/2addr v0, v1

    not-int v0, v0

    mul-int/lit16 v0, v0, 0x2c9

    add-int/2addr v4, v0

    add-int/2addr v13, v4

    shl-int/lit8 v0, v13, 0xd

    xor-int/2addr v0, v13

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v3, v8, v1

    check-cast v3, [I

    const/4 v4, 0x0

    aput v0, v3, v4

    move v1, v4

    goto/16 :goto_176a

    :cond_16d8
    const/4 v1, 0x2

    add-int/lit8 v0, v8, -0x1

    mul-int/2addr v0, v8

    .line 1076
    rem-int/2addr v0, v1

    .line 1082
    div-int/2addr v8, v0

    const/4 v0, 0x0

    invoke-static {v0, v8, v4}, Landroid/widget/Toast;->makeText(Landroid/content/Context;II)Landroid/widget/Toast;

    move-result-object v5

    .line 1090
    invoke-virtual {v5}, Landroid/widget/Toast;->show()V

    const/4 v0, 0x4

    new-array v8, v0, [Ljava/lang/Object;

    const/4 v0, 0x1

    new-array v5, v0, [I

    aput-object v5, v8, v0

    new-array v10, v0, [I

    aput-object v10, v8, v1

    new-array v10, v0, [I

    const/4 v13, 0x3

    aput-object v10, v8, v13

    aget-object v14, v3, v1

    check-cast v14, [I

    aget v1, v14, v4

    aget-object v14, v3, v13

    check-cast v14, [I

    aget v13, v14, v4

    aget-object v14, v3, v0

    check-cast v14, [I

    aget v0, v14, v4

    aget-object v3, v3, v4

    check-cast v3, [Ljava/lang/String;

    check-cast v10, [I

    aput v13, v10, v4

    check-cast v5, [I

    aput v0, v5, v4

    aput-object v3, v8, v4

    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-string v3, "currentApplication"

    new-array v5, v4, [Ljava/lang/Class;

    invoke-virtual {v0, v3, v5}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v3, 0x0

    invoke-virtual {v0, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/res/Resources;->getConfiguration()Landroid/content/res/Configuration;

    move-result-object v0

    iget v0, v0, Landroid/content/res/Configuration;->screenHeightDp:I

    const v3, -0x151a5f15

    or-int v4, v3, v0

    not-int v4, v4

    const v5, 0x10121b04

    or-int/2addr v4, v5

    mul-int/lit16 v4, v4, 0x159

    const v5, -0x259c4148

    add-int/2addr v5, v4

    not-int v4, v0

    or-int/2addr v3, v4

    not-int v3, v3

    const v4, 0x4260a0e3

    or-int/2addr v3, v4

    mul-int/lit16 v3, v3, 0x159

    add-int/2addr v5, v3

    const v3, -0x10121b05

    or-int/2addr v0, v3

    not-int v0, v0

    mul-int/lit16 v0, v0, 0x159

    add-int/2addr v5, v0

    add-int/2addr v1, v5

    shl-int/lit8 v0, v1, 0xd

    xor-int/2addr v0, v1

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v3, v8, v1

    check-cast v3, [I

    const/4 v1, 0x0

    aput v0, v3, v1

    :goto_176a
    const v0, 0x6afd23e8

    .line 1136
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_17b2

    invoke-static {v1, v1}, Landroid/view/Gravity;->getAbsoluteGravity(II)I

    move-result v0

    rsub-int/lit8 v30, v0, 0x15

    invoke-static {v1, v1}, Landroid/view/View;->resolveSize(II)I

    move-result v0

    rsub-int v0, v0, 0x685

    int-to-char v0, v0

    invoke-static {v1, v1}, Landroid/view/View;->resolveSize(II)I

    move-result v3

    add-int/lit16 v1, v3, 0x241

    const v33, 0x439b41c

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v4, 0xe

    aget-byte v4, v3, v4

    const/4 v5, 0x1

    sub-int/2addr v4, v5

    int-to-byte v4, v4

    const/16 v10, 0xc

    aget-byte v3, v3, v10

    int-to-short v3, v3

    const/16 v10, 0x25

    int-to-byte v10, v10

    new-array v13, v5, [Ljava/lang/Object;

    invoke-static {v4, v3, v10, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v13, v3

    move-object/from16 v35, v4

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_17b2
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v3

    .line 1141
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v5, 0x0

    .line 1146
    new-array v10, v5, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v10}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1153
    new-array v10, v5, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v10}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0

    const v5, -0x4314ded6

    .line 1160
    invoke-static {v5}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v5

    if-nez v5, :cond_1821

    invoke-static {}, Landroid/view/ViewConfiguration;->getMaximumDrawingCacheSize()I

    move-result v5

    shr-int/lit8 v5, v5, 0x18

    add-int/lit8 v30, v5, 0x15

    const/4 v5, 0x0

    invoke-static {v7, v7, v5}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;I)I

    move-result v10

    add-int/lit16 v10, v10, 0x685

    int-to-char v10, v10

    invoke-static {v5}, Landroid/view/View$MeasureSpec;->getSize(I)I

    move-result v13

    add-int/lit16 v5, v13, 0x241

    const v33, -0x2dd04922

    const/16 v34, 0x0

    sget-object v13, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v14, 0xac

    aget-byte v14, v13, v14

    neg-int v14, v14

    int-to-byte v14, v14

    const/16 v15, 0x5c

    int-to-short v15, v15

    const/16 v29, 0x49

    aget-byte v13, v13, v29

    move-object/from16 v41, v8

    const/4 v8, 0x1

    sub-int/2addr v13, v8

    int-to-byte v13, v13

    move-object/from16 v42, v12

    new-array v12, v8, [Ljava/lang/Object;

    invoke-static {v14, v15, v13, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v8, 0x0

    aget-object v12, v12, v8

    move-object/from16 v35, v12

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v10

    move/from16 v32, v5

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v5

    goto :goto_1825

    :cond_1821
    move-object/from16 v41, v8

    move-object/from16 v42, v12

    :goto_1825
    check-cast v5, Ljava/lang/reflect/Field;

    const/4 v8, 0x0

    invoke-virtual {v5, v8}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v12

    const/16 v5, 0x34

    shl-long/2addr v12, v5

    ushr-long/2addr v12, v5

    sub-long/2addr v0, v12

    const/16 v5, 0xc

    shr-long/2addr v0, v5

    cmp-long v0, v3, v0

    if-nez v0, :cond_1910

    const v0, 0x1aba5f5c

    .line 1177
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_1888

    const/4 v0, 0x0

    invoke-static {v0}, Landroid/graphics/Color;->green(I)I

    move-result v1

    rsub-int/lit8 v30, v1, 0x15

    invoke-static {v7}, Landroid/os/Process;->getGidForName(Ljava/lang/String;)I

    move-result v0

    add-int/lit16 v0, v0, 0x686

    int-to-char v0, v0

    const/4 v1, 0x0

    invoke-static {v1, v1}, Landroid/graphics/PointF;->length(FF)F

    move-result v3

    cmpl-float v3, v3, v1

    rsub-int v1, v3, 0x241

    const v33, 0x747ec8a8

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v4, 0x49

    aget-byte v5, v3, v4

    const/4 v4, 0x1

    sub-int/2addr v5, v4

    int-to-byte v5, v5

    const/16 v8, 0x46

    aget-byte v8, v3, v8

    neg-int v8, v8

    int-to-short v8, v8

    const/16 v10, 0xb2

    aget-byte v3, v3, v10

    sub-int/2addr v3, v4

    int-to-byte v3, v3

    new-array v10, v4, [Ljava/lang/Object;

    invoke-static {v5, v8, v3, v10}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v10, v3

    move-object/from16 v35, v4

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_1888
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/Object;

    const/4 v1, 0x4

    new-array v3, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v4, v1, [I

    const/4 v5, 0x0

    aput-object v4, v3, v5

    new-array v8, v1, [I

    aput-object v8, v3, v1

    new-array v10, v1, [I

    const/4 v12, 0x3

    aput-object v10, v3, v12

    .line 1182
    aget-object v10, v0, v1

    check-cast v10, [I

    aget v1, v10, v5

    aget-object v0, v0, v5

    check-cast v0, [I

    aget v0, v0, v5

    new-array v10, v5, [Ljava/lang/String;

    check-cast v8, [I

    aput v1, v8, v5

    check-cast v4, [I

    aput v0, v4, v5

    const/4 v0, 0x2

    aput-object v10, v3, v0

    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-string v1, "currentApplication"

    new-array v4, v5, [Ljava/lang/Class;

    invoke-virtual {v0, v1, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/String;->length()I

    move-result v0

    const v1, 0x1228e273

    add-int/2addr v0, v1

    const v1, -0x40282

    not-int v4, v0

    or-int/2addr v1, v4

    not-int v1, v1

    const v4, -0x59fcbfbc

    or-int/2addr v1, v4

    mul-int/lit16 v1, v1, -0x24f

    const v4, -0x5b3c16aa

    add-int/2addr v4, v1

    const v1, -0x40282

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, 0x24f

    add-int/2addr v4, v0

    const v0, 0x25423a02

    add-int/2addr v4, v0

    shl-int/lit8 v0, v4, 0xd

    xor-int/2addr v0, v4

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x3

    aget-object v4, v3, v1

    check-cast v4, [I

    const/4 v1, 0x0

    aput v0, v4, v1

    move v0, v1

    goto/16 :goto_1b2a

    :cond_1910
    const/4 v1, 0x0

    const v0, -0x24460e0c

    .line 1186
    :try_start_1914
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_1945

    invoke-static {}, Landroid/view/ViewConfiguration;->getGlobalActionKeyTimeout()J

    move-result-wide v3

    const-wide/16 v12, 0x0

    cmp-long v0, v3, v12

    add-int/lit8 v30, v0, 0xe

    invoke-static {v1}, Landroid/view/KeyEvent;->normalizeMetaState(I)I

    move-result v0

    int-to-char v0, v0

    invoke-static {}, Landroid/view/ViewConfiguration;->getScrollBarSize()I

    move-result v1

    shr-int/lit8 v1, v1, 0x8

    add-int/lit16 v1, v1, 0x4b0

    const v33, -0x4a829a00

    const/16 v34, 0x0

    const/16 v35, 0x0

    const/4 v3, 0x0

    new-array v4, v3, [Ljava/lang/Class;

    move/from16 v31, v0

    move/from16 v32, v1

    move-object/from16 v36, v4

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_1945
    check-cast v0, Ljava/lang/reflect/Constructor;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Constructor;->newInstance([Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    const/4 v1, 0x4

    .line 1194
    new-array v3, v1, [Ljava/lang/Object;

    const-wide/16 v4, 0x0

    invoke-static {v4, v5}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v1

    const/4 v4, 0x3

    aput-object v1, v3, v4

    const v1, 0x25423a02

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    const/4 v4, 0x2

    aput-object v1, v3, v4

    const/4 v1, 0x1

    aput-object v0, v3, v1

    const/4 v0, 0x0

    const/4 v1, 0x0

    aput-object v0, v3, v1

    const v0, -0xdefcc9b

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_19ff

    invoke-static {}, Landroid/view/ViewConfiguration;->getMaximumDrawingCacheSize()I

    move-result v0

    shr-int/lit8 v0, v0, 0x18

    rsub-int/lit8 v30, v0, 0x15

    const/16 v0, 0x30

    const/4 v1, 0x0

    invoke-static {v7, v0, v1}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;CI)I

    move-result v4

    rsub-int v1, v4, 0x684

    int-to-char v1, v1

    invoke-static {v7, v0}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;C)I

    move-result v4

    add-int/lit16 v0, v4, 0x242

    const v33, -0x632b5b6f

    const/16 v34, 0x0

    const/16 v4, 0x28

    int-to-byte v4, v4

    or-int/lit8 v5, v4, 0x6

    int-to-short v5, v5

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v10, 0x1a

    aget-byte v8, v8, v10

    int-to-byte v8, v8

    const/4 v10, 0x1

    new-array v12, v10, [Ljava/lang/Object;

    invoke-static {v4, v5, v8, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v12, v4

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/4 v5, 0x4

    new-array v8, v5, [Ljava/lang/Class;

    invoke-static {}, Landroid/os/SystemClock;->uptimeMillis()J

    move-result-wide v12

    const-wide/16 v14, 0x0

    cmp-long v5, v12, v14

    add-int/lit8 v5, v5, 0x31

    invoke-static {v4, v4}, Landroid/view/View$MeasureSpec;->makeMeasureSpec(II)I

    move-result v10

    int-to-char v4, v10

    invoke-static {}, Landroid/media/AudioTrack;->getMaxVolume()F

    move-result v10

    const/4 v12, 0x0

    cmpl-float v10, v10, v12

    rsub-int v10, v10, 0x257

    invoke-static {v5, v4, v10}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(ICI)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Ljava/lang/Class;

    const/4 v5, 0x0

    aput-object v4, v8, v5

    invoke-static {v5}, Landroid/view/View$MeasureSpec;->getSize(I)I

    move-result v4

    rsub-int/lit8 v4, v4, 0x42

    const/16 v10, 0x30

    invoke-static {v7, v10, v5}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CI)I

    move-result v12

    rsub-int v5, v12, 0x4957

    int-to-char v5, v5

    invoke-static {v7, v10}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;C)I

    move-result v12

    add-int/lit16 v12, v12, 0x289

    invoke-static {v4, v5, v12}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(ICI)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Ljava/lang/Class;

    const/4 v5, 0x1

    aput-object v4, v8, v5

    sget-object v4, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x2

    aput-object v4, v8, v5

    sget-object v4, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x3

    aput-object v4, v8, v5

    move/from16 v31, v1

    move/from16 v32, v0

    move-object/from16 v36, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_19ff
    check-cast v0, Ljava/lang/reflect/Method;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    move-object v3, v0

    check-cast v3, [Ljava/lang/Object;
    :try_end_1a09
    .catchall {:try_start_1914 .. :try_end_1a09} :catchall_3f5f

    const v0, 0x1aba5f5c

    .line 1204
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_1a56

    const/4 v0, 0x0

    invoke-static {v7, v7, v0}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;I)I

    move-result v1

    rsub-int/lit8 v30, v1, 0x15

    invoke-static {v7}, Landroid/os/Process;->getGidForName(Ljava/lang/String;)I

    move-result v0

    rsub-int v0, v0, 0x684

    int-to-char v0, v0

    invoke-static {v7}, Landroid/view/MotionEvent;->axisFromString(Ljava/lang/String;)I

    move-result v1

    add-int/lit16 v1, v1, 0x242

    const v33, 0x747ec8a8

    const/16 v34, 0x0

    sget-object v4, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v5, 0x49

    aget-byte v8, v4, v5

    const/4 v5, 0x1

    sub-int/2addr v8, v5

    int-to-byte v8, v8

    const/16 v10, 0x46

    aget-byte v10, v4, v10

    neg-int v10, v10

    int-to-short v10, v10

    const/16 v12, 0xb2

    aget-byte v4, v4, v12

    sub-int/2addr v4, v5

    int-to-byte v4, v4

    new-array v12, v5, [Ljava/lang/Object;

    invoke-static {v8, v10, v4, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v12, v4

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_1a56
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    :try_start_1a5c
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v4, 0x0

    new-array v5, v4, [Ljava/lang/Class;

    .line 1210
    invoke-virtual {v0, v6, v5}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    new-array v5, v4, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0
    :try_end_1a73
    .catch Ljava/lang/Exception; {:try_start_1a5c .. :try_end_1a73} :catch_3f10

    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v4

    const v5, -0x4314ded6

    invoke-static {v5}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v5

    if-nez v5, :cond_1ac8

    invoke-static {v7, v7}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)I

    move-result v5

    rsub-int/lit8 v30, v5, 0x15

    invoke-static {}, Landroid/view/ViewConfiguration;->getScrollDefaultDelay()I

    move-result v5

    const/16 v8, 0x10

    shr-int/2addr v5, v8

    rsub-int v5, v5, 0x685

    int-to-char v5, v5

    const/4 v8, 0x0

    invoke-static {v8}, Landroid/telephony/cdma/CdmaCellLocation;->convertQuartSecToDecDegrees(I)D

    move-result-wide v12

    const-wide/16 v14, 0x0

    cmpl-double v8, v12, v14

    add-int/lit16 v8, v8, 0x241

    const v33, -0x2dd04922

    const/16 v34, 0x0

    sget-object v10, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v12, 0xac

    aget-byte v12, v10, v12

    neg-int v12, v12

    int-to-byte v12, v12

    const/16 v13, 0x5c

    int-to-short v13, v13

    const/16 v14, 0x49

    aget-byte v10, v10, v14

    const/4 v14, 0x1

    sub-int/2addr v10, v14

    int-to-byte v10, v10

    new-array v15, v14, [Ljava/lang/Object;

    invoke-static {v12, v13, v10, v15}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v10, 0x0

    aget-object v12, v15, v10

    move-object/from16 v35, v12

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v5

    move/from16 v32, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v5

    :cond_1ac8
    check-cast v5, Ljava/lang/reflect/Field;

    const/4 v8, 0x0

    invoke-virtual {v5, v8, v4}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v4, 0xc

    shr-long/2addr v0, v4

    .line 1216
    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, 0x6afd23e8

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_1b23

    invoke-static {}, Landroid/os/Process;->myTid()I

    move-result v1

    shr-int/lit8 v1, v1, 0x16

    rsub-int/lit8 v30, v1, 0x15

    const-wide/16 v4, 0x0

    invoke-static {v4, v5}, Landroid/widget/ExpandableListView;->getPackedPositionGroup(J)I

    move-result v1

    rsub-int v1, v1, 0x685

    int-to-char v1, v1

    const/16 v4, 0x30

    invoke-static {v7, v4}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;C)I

    move-result v5

    rsub-int v4, v5, 0x240

    const v33, 0x439b41c

    const/16 v34, 0x0

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v8, 0xe

    aget-byte v8, v5, v8

    const/4 v10, 0x1

    sub-int/2addr v8, v10

    int-to-byte v8, v8

    const/16 v12, 0xc

    aget-byte v5, v5, v12

    int-to-short v5, v5

    const/16 v12, 0x25

    int-to-byte v12, v12

    new-array v13, v10, [Ljava/lang/Object;

    invoke-static {v8, v5, v12, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v8, v13, v5

    move-object/from16 v35, v8

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v1

    move/from16 v32, v4

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_1b23
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v4, 0x0

    invoke-virtual {v1, v4, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/4 v0, 0x0

    .line 1228
    :goto_1b2a
    aget-object v1, v3, v0

    check-cast v1, [I

    aget v1, v1, v0

    const/4 v4, 0x1

    aget-object v5, v3, v4

    check-cast v5, [I

    aget v4, v5, v0

    if-ne v4, v1, :cond_3eee

    .line 3097
    sget v0, Lmx/com/cfe/cfecontigo/App;->MediaBrowserCompatSearchResultReceiver:I

    add-int/lit8 v0, v0, 0x33

    rem-int/lit16 v1, v0, 0x80

    sput v1, Lmx/com/cfe/cfecontigo/App;->RatingCompat:I

    const/4 v1, 0x2

    rem-int/2addr v0, v1

    const/4 v0, 0x4

    .line 1234
    new-array v1, v0, [Ljava/lang/Object;

    const/4 v0, 0x1

    new-array v4, v0, [I

    const/4 v5, 0x0

    aput-object v4, v1, v5

    new-array v8, v0, [I

    aput-object v8, v1, v0

    new-array v10, v0, [I

    const/4 v12, 0x3

    aput-object v10, v1, v12

    aget-object v10, v3, v12

    check-cast v10, [I

    aget v10, v10, v5

    .line 1238
    aget-object v12, v3, v0

    check-cast v12, [I

    aget v0, v12, v5

    aget-object v3, v3, v5

    check-cast v3, [I

    aget v3, v3, v5

    new-array v12, v5, [Ljava/lang/String;

    check-cast v8, [I

    aput v0, v8, v5

    check-cast v4, [I

    aput v3, v4, v5

    const/4 v0, 0x2

    aput-object v12, v1, v0

    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-string v3, "currentApplication"

    new-array v4, v5, [Ljava/lang/Class;

    invoke-virtual {v0, v3, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v3, 0x0

    invoke-virtual {v0, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/res/Resources;->getConfiguration()Landroid/content/res/Configuration;

    move-result-object v0

    iget v0, v0, Landroid/content/res/Configuration;->mnc:I

    const v3, 0x26842a28

    or-int/2addr v0, v3

    not-int v0, v0

    const v3, 0x4802228

    or-int/2addr v3, v0

    mul-int/lit16 v3, v3, -0xc4

    const v4, -0x3efb3724

    add-int/2addr v3, v4

    const v4, 0x22040800

    or-int/2addr v0, v4

    mul-int/lit16 v0, v0, 0xc4

    add-int/2addr v3, v0

    add-int/2addr v10, v3

    shl-int/lit8 v0, v10, 0xd

    xor-int/2addr v0, v10

    ushr-int/lit8 v3, v0, 0x11

    xor-int/2addr v0, v3

    shl-int/lit8 v3, v0, 0x5

    xor-int/2addr v0, v3

    const/4 v3, 0x3

    aget-object v4, v1, v3

    check-cast v4, [I

    const/4 v3, 0x0

    aput v0, v4, v3

    const v0, -0x11c210ac

    .line 1296
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_1c02

    invoke-static {v3, v3}, Landroid/view/View;->combineMeasuredStates(II)I

    move-result v0

    add-int/lit8 v30, v0, 0x12

    const v0, 0xf06e

    invoke-static {v3}, Landroid/view/View$MeasureSpec;->getMode(I)I

    move-result v4

    sub-int/2addr v0, v4

    int-to-char v0, v0

    invoke-static {v3, v3}, Landroid/view/View$MeasureSpec;->makeMeasureSpec(II)I

    move-result v4

    rsub-int v3, v4, 0x678

    const v33, -0x7f068760

    const/16 v34, 0x0

    const/16 v4, 0x28

    int-to-byte v4, v4

    or-int/lit8 v5, v4, 0x6

    int-to-short v5, v5

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v10, 0x1a

    aget-byte v8, v8, v10

    int-to-byte v8, v8

    const/4 v10, 0x1

    new-array v12, v10, [Ljava/lang/Object;

    invoke-static {v4, v5, v8, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v12, v4

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_1c02
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v0, v3}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v4

    .line 1304
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v8, 0x0

    .line 1312
    new-array v10, v8, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v10}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    new-array v10, v8, [Ljava/lang/Object;

    invoke-virtual {v0, v3, v10}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    .line 1322
    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v12

    const v0, 0x5f78b1e3

    .line 1326
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_1c71

    const/4 v3, 0x0

    invoke-static {v3, v3}, Landroid/graphics/PointF;->length(FF)F

    move-result v0

    cmpl-float v0, v0, v3

    rsub-int/lit8 v30, v0, 0x12

    const v0, 0xf06e

    const/4 v3, 0x0

    invoke-static {v3, v3, v3}, Landroid/view/View;->resolveSizeAndState(III)I

    move-result v8

    add-int/2addr v8, v0

    int-to-char v0, v8

    invoke-static {v3, v3, v3, v3}, Landroid/graphics/Color;->argb(IIII)I

    move-result v8

    rsub-int v3, v8, 0x678

    const v33, 0x31bc2617

    const/16 v34, 0x0

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v10, 0xe

    aget-byte v10, v8, v10

    const/4 v14, 0x1

    sub-int/2addr v10, v14

    int-to-byte v10, v10

    const/16 v15, 0xc

    aget-byte v8, v8, v15

    int-to-short v8, v8

    const/16 v15, 0x25

    int-to-byte v15, v15

    move-object/from16 v43, v1

    new-array v1, v14, [Ljava/lang/Object;

    invoke-static {v10, v8, v15, v1}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v8, 0x0

    aget-object v1, v1, v8

    move-object/from16 v35, v1

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    goto :goto_1c73

    :cond_1c71
    move-object/from16 v43, v1

    :goto_1c73
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v14

    const/16 v0, 0x34

    shl-long/2addr v14, v0

    ushr-long/2addr v14, v0

    sub-long/2addr v12, v14

    const/16 v0, 0xc

    shr-long/2addr v12, v0

    cmp-long v0, v4, v12

    if-nez v0, :cond_1d69

    const v0, -0x501e3140

    .line 1338
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_1cd4

    invoke-static {v7}, Landroid/view/KeyEvent;->keyCodeFromString(Ljava/lang/String;)I

    move-result v0

    add-int/lit8 v30, v0, 0x12

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v0

    const-wide/16 v3, 0x0

    cmp-long v0, v0, v3

    const v1, 0xf06f

    sub-int/2addr v1, v0

    int-to-char v0, v1

    const/4 v1, 0x0

    invoke-static {v1}, Landroid/graphics/ImageFormat;->getBitsPerPixel(I)I

    move-result v3

    add-int/lit16 v1, v3, 0x679

    const v33, -0x3edaa6cc

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v4, 0x2

    aget-byte v5, v3, v4

    int-to-byte v4, v5

    const/16 v5, 0x49

    aget-byte v3, v3, v5

    const/4 v5, 0x1

    sub-int/2addr v3, v5

    int-to-short v3, v3

    or-int/lit8 v8, v3, 0x2e

    int-to-byte v8, v8

    new-array v10, v5, [Ljava/lang/Object;

    invoke-static {v4, v3, v8, v10}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v10, v3

    move-object/from16 v35, v4

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_1cd4
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/Object;

    const/4 v1, 0x4

    new-array v3, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v4, v1, [I

    const/4 v5, 0x0

    aput-object v4, v3, v5

    new-array v4, v1, [I

    aput-object v4, v3, v1

    new-array v8, v1, [I

    const/4 v10, 0x2

    aput-object v8, v3, v10

    .line 1347
    aget-object v12, v0, v1

    check-cast v12, [I

    aget v1, v12, v5

    aget-object v12, v0, v10

    check-cast v12, [I

    aget v10, v12, v5

    const/4 v12, 0x3

    aget-object v0, v0, v12

    check-cast v0, [Ljava/lang/String;

    check-cast v4, [I

    aput v1, v4, v5

    check-cast v8, [I

    aput v10, v8, v5

    aput-object v0, v3, v12

    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-string v1, "currentApplication"

    new-array v4, v5, [Ljava/lang/Class;

    invoke-virtual {v0, v1, v4}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/res/Resources;->getConfiguration()Landroid/content/res/Configuration;

    move-result-object v0

    iget v0, v0, Landroid/content/res/Configuration;->navigation:I

    const v1, 0x1c90284

    or-int v4, v0, v1

    mul-int/lit16 v4, v4, 0x3dc

    const v5, 0x7665c9e

    add-int/2addr v5, v4

    not-int v4, v0

    const v8, 0x19cde2f7

    or-int/2addr v8, v4

    not-int v8, v8

    const v10, -0x1bdfeaf8

    or-int/2addr v8, v10

    mul-int/lit16 v8, v8, -0x7b8

    add-int/2addr v5, v8

    const v8, 0x3db0a84

    or-int/2addr v0, v8

    not-int v0, v0

    or-int/2addr v0, v1

    const v1, -0x3db0a85

    or-int/2addr v1, v4

    not-int v1, v1

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, 0x3dc

    add-int/2addr v5, v0

    const v0, 0x12b0cf30

    add-int/2addr v5, v0

    shl-int/lit8 v0, v5, 0xd

    xor-int/2addr v0, v5

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x0

    aget-object v4, v3, v1

    check-cast v4, [I

    aput v0, v4, v1

    move-object/from16 v4, v42

    :goto_1d66
    const/4 v0, 0x2

    goto/16 :goto_1f73

    :cond_1d69
    const/4 v1, 0x0

    .line 1365
    invoke-virtual/range {p0 .. p0}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    move-result-object v0

    if-nez v0, :cond_1d81

    .line 1367
    invoke-static/range {v25 .. v25}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    .line 1372
    new-array v3, v1, [Ljava/lang/Class;

    invoke-virtual {v0, v2, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    :cond_1d81
    if-eqz v0, :cond_1da1

    .line 3097
    sget v1, Lmx/com/cfe/cfecontigo/App;->RatingCompat:I

    add-int/lit8 v1, v1, 0x6f

    rem-int/lit16 v3, v1, 0x80

    sput v3, Lmx/com/cfe/cfecontigo/App;->MediaBrowserCompatSearchResultReceiver:I

    const/4 v3, 0x2

    rem-int/2addr v1, v3

    .line 1377
    instance-of v1, v0, Landroid/content/ContextWrapper;

    if-eqz v1, :cond_1d9d

    .line 1381
    move-object v1, v0

    check-cast v1, Landroid/content/ContextWrapper;

    invoke-virtual {v1}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    move-result-object v1

    if-eqz v1, :cond_1d9b

    goto :goto_1d9d

    :cond_1d9b
    const/4 v0, 0x0

    goto :goto_1da1

    :cond_1d9d
    :goto_1d9d
    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    .line 1397
    :cond_1da1
    :goto_1da1
    invoke-static {v11}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    const-class v3, Ljava/lang/Object;

    .line 1401
    filled-new-array {v3}, [Ljava/lang/Class;

    move-result-object v3

    move-object/from16 v4, v42

    invoke-virtual {v1, v4, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    .line 1410
    filled-new-array/range {p0 .. p0}, [Ljava/lang/Object;

    move-result-object v3

    const/4 v5, 0x0

    invoke-virtual {v1, v5, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Number;->intValue()I

    move-result v1

    const/4 v3, 0x4

    .line 1424
    :try_start_1dc1
    new-array v5, v3, [Ljava/lang/Object;

    const v3, 0x12b0cf30

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v3

    const/4 v8, 0x3

    aput-object v3, v5, v8

    const-wide/16 v12, 0x0

    invoke-static {v12, v13}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v3

    const/4 v8, 0x2

    aput-object v3, v5, v8

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    const/4 v3, 0x1

    aput-object v1, v5, v3

    const/4 v1, 0x0

    aput-object v0, v5, v1

    invoke-static {}, Landroid/os/Process;->myTid()I

    move-result v1

    shr-int/lit8 v1, v1, 0x16

    const/16 v3, 0x49

    add-int/2addr v1, v3

    const/16 v3, 0x4c

    new-array v3, v3, [C

    fill-array-data v3, :array_405e

    const/4 v8, 0x1

    new-array v10, v8, [Ljava/lang/Object;

    invoke-static {v1, v3, v10}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    const/4 v1, 0x0

    aget-object v3, v10, v1

    check-cast v3, Ljava/lang/String;

    invoke-virtual {v3}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$d:[B

    const/16 v8, 0x6a

    aget-byte v8, v3, v8

    int-to-byte v8, v8

    const/16 v10, 0xc

    aget-byte v12, v3, v10

    int-to-short v10, v12

    const/16 v12, 0x2d

    aget-byte v3, v3, v12

    int-to-byte v3, v3

    const/4 v12, 0x1

    new-array v13, v12, [Ljava/lang/Object;

    invoke-static {v8, v10, v3, v13}, Lmx/com/cfe/cfecontigo/App;->e(BSS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v8, v13, v3

    check-cast v8, Ljava/lang/String;

    const/4 v10, 0x4

    new-array v12, v10, [Ljava/lang/Class;

    const-class v10, Landroid/content/Context;

    aput-object v10, v12, v3

    sget-object v3, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v10, 0x1

    aput-object v3, v12, v10

    sget-object v3, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    const/4 v10, 0x2

    aput-object v3, v12, v10

    sget-object v3, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v10, 0x3

    aput-object v3, v12, v10

    invoke-virtual {v1, v8, v12}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    move-object v3, v1

    check-cast v3, [Ljava/lang/Object;
    :try_end_1e41
    .catchall {:try_start_1dc1 .. :try_end_1e41} :catchall_3f36

    if-eqz v0, :cond_1f6d

    const v0, -0x501e3140

    .line 1434
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_1e91

    invoke-static {}, Landroid/view/ViewConfiguration;->getEdgeSlop()I

    move-result v0

    const/16 v1, 0x10

    shr-int/2addr v0, v1

    rsub-int/lit8 v30, v0, 0x12

    invoke-static {}, Landroid/view/ViewConfiguration;->getTapTimeout()I

    move-result v0

    shr-int/2addr v0, v1

    const v1, 0xf06e

    sub-int/2addr v1, v0

    int-to-char v0, v1

    const/4 v1, 0x0

    invoke-static {v7, v7, v1}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;I)I

    move-result v5

    rsub-int v1, v5, 0x678

    const v33, -0x3edaa6cc

    const/16 v34, 0x0

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v8, 0x2

    aget-byte v10, v5, v8

    int-to-byte v8, v10

    const/16 v10, 0x49

    aget-byte v5, v5, v10

    const/4 v10, 0x1

    sub-int/2addr v5, v10

    int-to-short v5, v5

    or-int/lit8 v12, v5, 0x2e

    int-to-byte v12, v12

    new-array v13, v10, [Ljava/lang/Object;

    invoke-static {v8, v5, v12, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v8, v13, v5

    move-object/from16 v35, v8

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_1e91
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    :try_start_1e97
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v5, 0x0

    new-array v8, v5, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v8}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1439
    new-array v8, v5, [Ljava/lang/Object;

    .line 1442
    invoke-virtual {v0, v1, v8}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0
    :try_end_1eae
    .catch Ljava/lang/Exception; {:try_start_1e97 .. :try_end_1eae} :catch_1f67

    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v5

    const v8, 0x5f78b1e3

    invoke-static {v8}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v8

    if-nez v8, :cond_1f02

    const/4 v8, 0x0

    invoke-static {v8, v8}, Landroid/view/View$MeasureSpec;->makeMeasureSpec(II)I

    move-result v10

    rsub-int/lit8 v30, v10, 0x12

    const v10, 0xf06f

    const/16 v12, 0x30

    invoke-static {v7, v12}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;C)I

    move-result v13

    add-int/2addr v13, v10

    int-to-char v10, v13

    invoke-static {v8, v8, v8}, Landroid/view/View;->resolveSizeAndState(III)I

    move-result v12

    add-int/lit16 v8, v12, 0x678

    const v33, 0x31bc2617

    const/16 v34, 0x0

    sget-object v12, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v13, 0xe

    aget-byte v13, v12, v13

    const/4 v14, 0x1

    sub-int/2addr v13, v14

    int-to-byte v13, v13

    const/16 v15, 0xc

    aget-byte v12, v12, v15

    int-to-short v12, v12

    const/16 v15, 0x25

    int-to-byte v15, v15

    move-object/from16 v42, v3

    new-array v3, v14, [Ljava/lang/Object;

    invoke-static {v13, v12, v15, v3}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v12, 0x0

    aget-object v3, v3, v12

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v10

    move/from16 v32, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v8

    goto :goto_1f04

    :cond_1f02
    move-object/from16 v42, v3

    :goto_1f04
    check-cast v8, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v8, v3, v5}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v3, 0xc

    shr-long/2addr v0, v3

    .line 1456
    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, -0x11c210ac

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_1f60

    invoke-static {}, Landroid/view/ViewConfiguration;->getTouchSlop()I

    move-result v1

    shr-int/lit8 v1, v1, 0x8

    rsub-int/lit8 v30, v1, 0x12

    invoke-static {}, Landroid/view/ViewConfiguration;->getScrollBarFadeDuration()I

    move-result v1

    const/16 v3, 0x10

    shr-int/2addr v1, v3

    const v3, 0xf06e

    add-int/2addr v1, v3

    int-to-char v1, v1

    const/16 v3, 0x30

    const/4 v5, 0x0

    invoke-static {v7, v3, v5, v5}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;CII)I

    move-result v8

    rsub-int v3, v8, 0x677

    const v33, -0x7f068760

    const/16 v34, 0x0

    const/16 v5, 0x28

    int-to-byte v5, v5

    or-int/lit8 v8, v5, 0x6

    int-to-short v8, v8

    sget-object v10, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v12, 0x1a

    aget-byte v10, v10, v12

    int-to-byte v10, v10

    const/4 v12, 0x1

    new-array v13, v12, [Ljava/lang/Object;

    invoke-static {v5, v8, v10, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v8, v13, v5

    move-object/from16 v35, v8

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v1

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_1f60
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    goto :goto_1f6f

    :catch_1f67
    new-instance v0, Ljava/lang/RuntimeException;

    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    throw v0

    :cond_1f6d
    move-object/from16 v42, v3

    :goto_1f6f
    move-object/from16 v3, v42

    goto/16 :goto_1d66

    .line 1458
    :goto_1f73
    aget-object v1, v3, v0

    check-cast v1, [I

    const/4 v0, 0x0

    aget v1, v1, v0

    const/4 v5, 0x1

    aget-object v8, v3, v5

    check-cast v8, [I

    aget v5, v8, v0

    if-ne v5, v1, :cond_3ecc

    .line 3097
    sget v0, Lmx/com/cfe/cfecontigo/App;->MediaBrowserCompatSearchResultReceiver:I

    add-int/lit8 v0, v0, 0x3f

    rem-int/lit16 v1, v0, 0x80

    sput v1, Lmx/com/cfe/cfecontigo/App;->RatingCompat:I

    const/4 v1, 0x2

    rem-int/2addr v0, v1

    const/4 v0, 0x4

    .line 1472
    new-array v5, v0, [Ljava/lang/Object;

    const/4 v0, 0x1

    new-array v8, v0, [I

    const/4 v10, 0x0

    aput-object v8, v5, v10

    new-array v8, v0, [I

    aput-object v8, v5, v0

    new-array v12, v0, [I

    aput-object v12, v5, v1

    .line 1474
    aget-object v13, v3, v10

    check-cast v13, [I

    aget v13, v13, v10

    .line 1478
    aget-object v14, v3, v0

    check-cast v14, [I

    aget v0, v14, v10

    aget-object v14, v3, v1

    check-cast v14, [I

    aget v1, v14, v10

    const/4 v14, 0x3

    aget-object v3, v3, v14

    check-cast v3, [Ljava/lang/String;

    check-cast v8, [I

    aput v0, v8, v10

    check-cast v12, [I

    aput v1, v12, v10

    aput-object v3, v5, v14

    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-string v1, "currentApplication"

    new-array v3, v10, [Ljava/lang/Class;

    invoke-virtual {v0, v1, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/res/Resources;->getConfiguration()Landroid/content/res/Configuration;

    move-result-object v0

    iget v0, v0, Landroid/content/res/Configuration;->mcc:I

    const v1, 0x16fd6875

    or-int/2addr v1, v0

    mul-int/lit16 v1, v1, 0x178

    const v3, -0x26bb0efe

    add-int/2addr v3, v1

    not-int v1, v0

    const v8, 0x3678a3f6

    or-int/2addr v1, v8

    not-int v1, v1

    const v8, 0x854801

    or-int/2addr v1, v8

    mul-int/lit16 v1, v1, -0x178

    add-int/2addr v3, v1

    const v1, -0x3678a3f7

    or-int/2addr v0, v1

    not-int v0, v0

    const v1, -0x2085cb84

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, 0x178

    add-int/2addr v3, v0

    add-int/2addr v13, v3

    shl-int/lit8 v0, v13, 0xd

    xor-int/2addr v0, v13

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x0

    aget-object v3, v5, v1

    check-cast v3, [I

    aput v0, v3, v1

    .line 133
    sget-object v0, Lat/bluesource/choicesdk/core/ChoiceSdk;->Companion:Lat/bluesource/choicesdk/core/ChoiceSdk$Companion;

    move-object/from16 v3, p0

    move-object v8, v3

    check-cast v8, Landroid/app/Application;

    invoke-virtual {v0, v8}, Lat/bluesource/choicesdk/core/ChoiceSdk$Companion;->init(Landroid/app/Application;)V

    const v0, -0x5a1cad9d

    .line 1319
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2062

    invoke-static {v1}, Landroid/graphics/Color;->green(I)I

    move-result v0

    rsub-int/lit8 v30, v0, 0x13

    invoke-static {}, Landroid/view/ViewConfiguration;->getTapTimeout()I

    move-result v0

    const/16 v8, 0x10

    shr-int/2addr v0, v8

    int-to-char v0, v0

    invoke-static {v1}, Landroid/graphics/ImageFormat;->getBitsPerPixel(I)I

    move-result v8

    add-int/lit16 v1, v8, 0x431

    const v33, -0x34d83a69    # -1.0995095E7f

    const/16 v34, 0x0

    const/16 v8, 0x28

    int-to-byte v8, v8

    or-int/lit8 v10, v8, 0x6

    int-to-short v10, v10

    sget-object v12, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v13, 0x1a

    aget-byte v12, v12, v13

    int-to-byte v12, v12

    const/4 v13, 0x1

    new-array v14, v13, [Ljava/lang/Object;

    invoke-static {v8, v10, v12, v14}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v8, 0x0

    aget-object v10, v14, v8

    move-object/from16 v35, v10

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2062
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v12

    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v8, 0x0

    .line 1324
    new-array v10, v8, [Ljava/lang/Class;

    .line 1327
    invoke-virtual {v0, v6, v10}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1343
    new-array v10, v8, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v10}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0

    const v10, 0x24b997f2

    invoke-static {v10}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v10

    if-nez v10, :cond_20cb

    invoke-static {v8}, Landroid/graphics/ImageFormat;->getBitsPerPixel(I)I

    move-result v10

    rsub-int/lit8 v30, v10, 0x12

    invoke-static {v8, v8}, Landroid/view/View;->resolveSize(II)I

    move-result v10

    int-to-char v10, v10

    invoke-static {v8}, Landroid/graphics/Color;->blue(I)I

    move-result v14

    add-int/lit16 v8, v14, 0x430

    const v33, 0x4a7d0006    # 4145153.5f

    const/16 v34, 0x0

    sget-object v14, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v15, 0xe

    aget-byte v15, v14, v15

    move-object/from16 v42, v5

    const/4 v5, 0x1

    sub-int/2addr v15, v5

    int-to-byte v15, v15

    const/16 v19, 0xc

    aget-byte v14, v14, v19

    int-to-short v14, v14

    move-object/from16 v44, v2

    const/16 v2, 0x25

    int-to-byte v2, v2

    new-array v3, v5, [Ljava/lang/Object;

    invoke-static {v15, v14, v2, v3}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v3, v3, v2

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v10

    move/from16 v32, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v10

    goto :goto_20cf

    :cond_20cb
    move-object/from16 v44, v2

    move-object/from16 v42, v5

    :goto_20cf
    check-cast v10, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    invoke-virtual {v10, v2}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v14

    const/16 v2, 0x34

    shl-long/2addr v14, v2

    ushr-long/2addr v14, v2

    sub-long/2addr v0, v14

    const/16 v2, 0xc

    shr-long/2addr v0, v2

    cmp-long v0, v12, v0

    if-nez v0, :cond_21d5

    const v0, 0x449f060

    .line 1358
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2132

    invoke-static {}, Landroid/view/ViewConfiguration;->getKeyRepeatTimeout()I

    move-result v0

    const/16 v1, 0x10

    shr-int/2addr v0, v1

    add-int/lit8 v30, v0, 0x13

    const/16 v0, 0x30

    const/4 v2, 0x0

    invoke-static {v7, v0, v2}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CI)I

    move-result v3

    const/4 v0, 0x1

    add-int/2addr v3, v0

    int-to-char v0, v3

    invoke-static {}, Landroid/view/ViewConfiguration;->getTapTimeout()I

    move-result v2

    shr-int/2addr v2, v1

    add-int/lit16 v1, v2, 0x430

    const v33, 0x6a8d6794

    const/16 v34, 0x0

    sget-object v2, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v3, 0xac

    aget-byte v3, v2, v3

    neg-int v3, v3

    int-to-byte v3, v3

    const/16 v5, 0x5c

    int-to-short v5, v5

    const/16 v8, 0x49

    aget-byte v2, v2, v8

    const/4 v8, 0x1

    sub-int/2addr v2, v8

    int-to-byte v2, v2

    new-array v10, v8, [Ljava/lang/Object;

    invoke-static {v3, v5, v2, v10}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v3, v10, v2

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2132
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/Object;

    const/4 v1, 0x4

    new-array v2, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v3, v1, [I

    const/4 v5, 0x0

    aput-object v3, v2, v5

    new-array v8, v1, [I

    const/4 v10, 0x2

    aput-object v8, v2, v10

    new-array v12, v1, [I

    const/4 v13, 0x3

    aput-object v12, v2, v13

    .line 1361
    aget-object v12, v0, v5

    check-cast v12, [I

    aget v12, v12, v5

    aget-object v13, v0, v10

    check-cast v13, [I

    aget v10, v13, v5

    aget-object v0, v0, v1

    check-cast v0, [Ljava/lang/String;

    check-cast v3, [I

    aput v12, v3, v5

    check-cast v8, [I

    aput v10, v8, v5

    const-string v1, "android.app.ActivityThread"

    invoke-static {v1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    const-string v3, "currentApplication"

    new-array v8, v5, [Ljava/lang/Class;

    invoke-virtual {v1, v3, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/content/Context;

    invoke-virtual {v1}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v1

    invoke-virtual {v1}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v1

    const v3, 0x7f1300aa

    invoke-virtual {v1, v3}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v1

    const/4 v3, 0x4

    invoke-virtual {v1, v5, v3}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v1}, Ljava/lang/String;->length()I

    move-result v1

    const v3, 0x535f786

    add-int/2addr v1, v3

    not-int v3, v1

    const v5, -0x3865b963

    or-int/2addr v5, v3

    not-int v5, v5

    const v8, 0x38019820

    or-int/2addr v5, v8

    const v10, 0x4e465de

    or-int/2addr v3, v10

    not-int v3, v3

    or-int/2addr v3, v5

    mul-int/lit16 v3, v3, 0x1d0

    const v5, -0x1d1d4000

    add-int/2addr v5, v3

    const v3, -0x642143

    or-int/2addr v3, v1

    mul-int/lit16 v3, v3, -0x1d0

    add-int/2addr v5, v3

    or-int/2addr v1, v10

    not-int v1, v1

    or-int/2addr v1, v8

    mul-int/lit16 v1, v1, 0x1d0

    add-int/2addr v5, v1

    const v1, -0x4604594b

    add-int/2addr v5, v1

    shl-int/lit8 v1, v5, 0xd

    xor-int/2addr v1, v5

    ushr-int/lit8 v3, v1, 0x11

    xor-int/2addr v1, v3

    shl-int/lit8 v3, v1, 0x5

    xor-int/2addr v1, v3

    const/4 v3, 0x3

    aget-object v5, v2, v3

    check-cast v5, [I

    const/4 v3, 0x0

    aput v1, v5, v3

    const/4 v1, 0x1

    aput-object v0, v2, v1

    :goto_21d2
    const/4 v0, 0x2

    goto/16 :goto_2390

    .line 1370
    :cond_21d5
    invoke-static {v11}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-class v1, Ljava/lang/Object;

    .line 1373
    filled-new-array {v1}, [Ljava/lang/Class;

    move-result-object v1

    invoke-virtual {v0, v4, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1380
    filled-new-array/range {p0 .. p0}, [Ljava/lang/Object;

    move-result-object v1

    const/4 v2, 0x0

    invoke-virtual {v0, v2, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    invoke-virtual {v0}, Ljava/lang/Number;->intValue()I

    move-result v0

    const/4 v1, 0x4

    .line 1397
    :try_start_21f3
    new-array v2, v1, [Ljava/lang/Object;

    const v1, -0x4604594b

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    const/4 v3, 0x3

    aput-object v1, v2, v3

    const-wide/16 v12, 0x0

    invoke-static {v12, v13}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v1

    const/4 v3, 0x2

    aput-object v1, v2, v3

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v0

    const/4 v1, 0x1

    aput-object v0, v2, v1

    const/4 v0, 0x0

    const/4 v1, 0x0

    aput-object v0, v2, v1

    invoke-static {v7, v1}, Landroid/text/TextUtils;->getOffsetBefore(Ljava/lang/CharSequence;I)I

    move-result v0

    const/16 v1, 0x30

    add-int/2addr v0, v1

    const/16 v1, 0x32

    new-array v1, v1, [C

    fill-array-data v1, :array_40ae

    const/4 v3, 0x1

    new-array v5, v3, [Ljava/lang/Object;

    invoke-static {v0, v1, v5}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    const/4 v0, 0x0

    aget-object v1, v5, v0

    check-cast v1, Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    sget v1, Lmx/com/cfe/cfecontigo/App;->$$e:I

    and-int/lit8 v1, v1, 0x38

    int-to-byte v1, v1

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$d:[B

    const/16 v5, 0x50

    aget-byte v5, v3, v5

    int-to-short v5, v5

    const/16 v8, 0xc

    aget-byte v3, v3, v8

    int-to-byte v3, v3

    const/4 v8, 0x1

    new-array v10, v8, [Ljava/lang/Object;

    invoke-static {v1, v5, v3, v10}, Lmx/com/cfe/cfecontigo/App;->e(BSS[Ljava/lang/Object;)V

    const/4 v1, 0x0

    aget-object v3, v10, v1

    check-cast v3, Ljava/lang/String;

    const/4 v5, 0x4

    new-array v8, v5, [Ljava/lang/Class;

    const-class v5, Ljava/lang/Class;

    aput-object v5, v8, v1

    sget-object v1, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x1

    aput-object v1, v8, v5

    sget-object v1, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x2

    aput-object v1, v8, v5

    sget-object v1, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x3

    aput-object v1, v8, v5

    invoke-virtual {v0, v3, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v2}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    move-object v2, v0

    check-cast v2, [Ljava/lang/Object;
    :try_end_2272
    .catchall {:try_start_21f3 .. :try_end_2272} :catchall_3f36

    const v0, 0x449f060

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_22c2

    const/4 v0, 0x0

    invoke-static {v0, v0}, Landroid/view/View;->combineMeasuredStates(II)I

    move-result v1

    add-int/lit8 v30, v1, 0x13

    invoke-static {}, Landroid/view/ViewConfiguration;->getZoomControlsTimeout()J

    move-result-wide v0

    const-wide/16 v12, 0x0

    cmp-long v0, v0, v12

    add-int/lit8 v0, v0, -0x1

    int-to-char v0, v0

    const/16 v1, 0x30

    invoke-static {v7, v1}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;C)I

    move-result v3

    add-int/lit16 v1, v3, 0x431

    const v33, 0x6a8d6794

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v5, 0xac

    aget-byte v5, v3, v5

    neg-int v5, v5

    int-to-byte v5, v5

    const/16 v8, 0x5c

    int-to-short v8, v8

    const/16 v10, 0x49

    aget-byte v3, v3, v10

    const/4 v10, 0x1

    sub-int/2addr v3, v10

    int-to-byte v3, v3

    new-array v12, v10, [Ljava/lang/Object;

    invoke-static {v5, v8, v3, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v5, v12, v3

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_22c2
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v2}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    .line 1400
    :try_start_22c8
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v3, 0x0

    .line 1402
    new-array v5, v3, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v5}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1406
    new-array v5, v3, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0
    :try_end_22df
    .catch Ljava/lang/Exception; {:try_start_22c8 .. :try_end_22df} :catch_3ec6

    .line 1415
    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v3

    const v5, 0x24b997f2

    invoke-static {v5}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v5

    if-nez v5, :cond_2332

    invoke-static {}, Landroid/view/ViewConfiguration;->getPressedStateDuration()I

    move-result v5

    const/16 v8, 0x10

    shr-int/2addr v5, v8

    add-int/lit8 v30, v5, 0x13

    invoke-static {}, Landroid/view/ViewConfiguration;->getScrollBarSize()I

    move-result v5

    shr-int/lit8 v5, v5, 0x8

    int-to-char v5, v5

    invoke-static {}, Landroid/view/ViewConfiguration;->getZoomControlsTimeout()J

    move-result-wide v12

    const-wide/16 v14, 0x0

    cmp-long v8, v12, v14

    add-int/lit16 v8, v8, 0x42f

    const v33, 0x4a7d0006    # 4145153.5f

    const/16 v34, 0x0

    sget-object v10, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v12, 0xe

    aget-byte v12, v10, v12

    const/4 v13, 0x1

    sub-int/2addr v12, v13

    int-to-byte v12, v12

    const/16 v14, 0xc

    aget-byte v10, v10, v14

    int-to-short v10, v10

    const/16 v14, 0x25

    int-to-byte v14, v14

    new-array v15, v13, [Ljava/lang/Object;

    invoke-static {v12, v10, v14, v15}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v10, 0x0

    aget-object v12, v15, v10

    move-object/from16 v35, v12

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v5

    move/from16 v32, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v5

    :cond_2332
    check-cast v5, Ljava/lang/reflect/Field;

    const/4 v8, 0x0

    invoke-virtual {v5, v8, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v3, 0xc

    shr-long/2addr v0, v3

    .line 1420
    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, -0x5a1cad9d

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_2388

    invoke-static {}, Landroid/view/ViewConfiguration;->getTapTimeout()I

    move-result v1

    const/16 v3, 0x10

    shr-int/2addr v1, v3

    rsub-int/lit8 v30, v1, 0x13

    invoke-static {}, Landroid/view/ViewConfiguration;->getTouchSlop()I

    move-result v1

    shr-int/lit8 v1, v1, 0x8

    int-to-char v1, v1

    const/4 v3, 0x0

    invoke-static {v3, v3}, Landroid/view/View;->resolveSize(II)I

    move-result v5

    rsub-int v3, v5, 0x430

    const v33, -0x34d83a69    # -1.0995095E7f

    const/16 v34, 0x0

    const/16 v5, 0x28

    int-to-byte v5, v5

    or-int/lit8 v8, v5, 0x6

    int-to-short v8, v8

    sget-object v10, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v12, 0x1a

    aget-byte v10, v10, v12

    int-to-byte v10, v10

    const/4 v12, 0x1

    new-array v13, v12, [Ljava/lang/Object;

    invoke-static {v5, v8, v10, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v8, v13, v5

    move-object/from16 v35, v8

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v1

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_2388
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    goto/16 :goto_21d2

    .line 1425
    :goto_2390
    aget-object v1, v2, v0

    check-cast v1, [I

    const/4 v3, 0x0

    aget v1, v1, v3

    aget-object v5, v2, v3

    check-cast v5, [I

    aget v5, v5, v3

    if-ne v5, v1, :cond_2434

    const/4 v1, 0x4

    .line 1434
    new-array v5, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v8, v1, [I

    aput-object v8, v5, v3

    new-array v10, v1, [I

    aput-object v10, v5, v0

    new-array v12, v1, [I

    const/4 v13, 0x3

    aput-object v12, v5, v13

    .line 1443
    aget-object v12, v2, v13

    check-cast v12, [I

    aget v12, v12, v3

    aget-object v13, v2, v3

    check-cast v13, [I

    aget v13, v13, v3

    aget-object v14, v2, v0

    check-cast v14, [I

    aget v0, v14, v3

    aget-object v2, v2, v1

    check-cast v2, [Ljava/lang/String;

    check-cast v8, [I

    aput v13, v8, v3

    check-cast v10, [I

    aput v0, v10, v3

    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-string v1, "currentApplication"

    new-array v8, v3, [Ljava/lang/Class;

    invoke-virtual {v0, v1, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/res/Resources;->getDisplayMetrics()Landroid/util/DisplayMetrics;

    move-result-object v0

    iget v0, v0, Landroid/util/DisplayMetrics;->widthPixels:I

    const v1, -0x1597ecdc

    or-int/2addr v1, v0

    not-int v1, v1

    not-int v3, v0

    const v8, 0x27b23265

    or-int/2addr v8, v3

    not-int v8, v8

    or-int/2addr v1, v8

    const v8, 0x1597ecdb

    or-int/2addr v8, v3

    not-int v8, v8

    or-int/2addr v1, v8

    mul-int/lit16 v1, v1, -0x204

    const v10, -0x3faa0ae8

    add-int/2addr v10, v1

    const v1, -0x5922042

    or-int/2addr v0, v1

    not-int v0, v0

    const v1, -0x22201225

    or-int/2addr v1, v3

    not-int v1, v1

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, 0x204

    add-int/2addr v10, v0

    const v0, 0x22201224

    or-int/2addr v0, v8

    mul-int/lit16 v0, v0, 0x204

    add-int/2addr v10, v0

    add-int/2addr v12, v10

    shl-int/lit8 v0, v12, 0xd

    xor-int/2addr v0, v12

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x3

    aget-object v3, v5, v1

    check-cast v3, [I

    const/4 v1, 0x0

    aput v0, v3, v1

    const/4 v0, 0x1

    aput-object v2, v5, v0

    move-object/from16 v0, p0

    const/4 v1, 0x0

    goto/16 :goto_24be

    :cond_2434
    const/4 v0, 0x1

    .line 1447
    new-instance v1, Ljava/util/ArrayList;

    .line 1453
    invoke-direct {v1}, Ljava/util/ArrayList;-><init>()V

    aget-object v3, v2, v0

    check-cast v3, [Ljava/lang/String;

    if-eqz v3, :cond_244c

    const/4 v0, 0x0

    .line 1457
    :goto_2441
    array-length v8, v3

    if-ge v0, v8, :cond_244c

    aget-object v8, v3, v0

    invoke-interface {v1, v8}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v0, v0, 0x1

    goto :goto_2441

    :cond_244c
    add-int/lit8 v0, v5, -0x1

    mul-int/2addr v0, v5

    const/4 v1, 0x2

    .line 1468
    rem-int/2addr v0, v1

    div-int/2addr v5, v0

    const/4 v0, 0x0

    const/4 v3, 0x0

    .line 1473
    invoke-static {v0, v5, v3}, Landroid/widget/Toast;->makeText(Landroid/content/Context;II)Landroid/widget/Toast;

    move-result-object v5

    .line 1479
    invoke-virtual {v5}, Landroid/widget/Toast;->show()V

    const/4 v0, 0x4

    .line 1470
    new-array v5, v0, [Ljava/lang/Object;

    const/4 v0, 0x1

    new-array v8, v0, [I

    aput-object v8, v5, v3

    new-array v10, v0, [I

    aput-object v10, v5, v1

    new-array v12, v0, [I

    const/4 v13, 0x3

    aput-object v12, v5, v13

    .line 1489
    aget-object v12, v2, v13

    check-cast v12, [I

    aget v12, v12, v3

    aget-object v13, v2, v3

    check-cast v13, [I

    aget v13, v13, v3

    aget-object v14, v2, v1

    check-cast v14, [I

    aget v1, v14, v3

    aget-object v2, v2, v0

    check-cast v2, [Ljava/lang/String;

    check-cast v8, [I

    aput v13, v8, v3

    check-cast v10, [I

    aput v1, v10, v3

    invoke-static/range {p0 .. p0}, Ljava/lang/System;->identityHashCode(Ljava/lang/Object;)I

    move-result v0

    not-int v0, v0

    const v1, -0x32e452ca

    or-int/2addr v1, v0

    not-int v1, v1

    const v3, -0xa65cc78

    or-int/2addr v1, v3

    mul-int/lit16 v1, v1, -0x3d7

    const v8, -0x6d8b64e2

    add-int/2addr v8, v1

    or-int/2addr v0, v3

    not-int v0, v0

    const v1, 0x8018c36

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, 0x3d7

    add-int/2addr v8, v0

    add-int/2addr v12, v8

    shl-int/lit8 v0, v12, 0xd

    xor-int/2addr v0, v12

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x3

    aget-object v3, v5, v1

    check-cast v3, [I

    const/4 v1, 0x0

    aput v0, v3, v1

    const/4 v0, 0x1

    aput-object v2, v5, v0

    move-object/from16 v0, p0

    .line 1492
    :goto_24be
    move-object v2, v0

    check-cast v2, Landroid/content/Context;

    :try_start_24c1
    filled-new-array {v2}, [Ljava/lang/Object;

    move-result-object v3

    const v8, 0x5376ed95

    invoke-static {v8}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v8

    if-nez v8, :cond_2500

    invoke-static {v1}, Landroid/graphics/Color;->green(I)I

    move-result v8

    const/4 v1, 0x6

    add-int/lit8 v30, v8, 0x6

    invoke-static {}, Landroid/view/KeyEvent;->getModifierMetaStateMask()I

    move-result v1

    int-to-byte v1, v1

    rsub-int v1, v1, 0x18ae

    int-to-char v1, v1

    invoke-static {}, Landroid/os/Process;->getElapsedCpuTime()J

    move-result-wide v12

    const-wide/16 v14, 0x0

    cmp-long v8, v12, v14

    rsub-int v8, v8, 0xdb7

    const v33, 0x3db27a61

    const/16 v34, 0x0

    const-string v35, "read"

    const/4 v10, 0x1

    new-array v12, v10, [Ljava/lang/Class;

    const-class v10, Landroid/content/Context;

    const/4 v13, 0x0

    aput-object v10, v12, v13

    move/from16 v31, v1

    move/from16 v32, v8

    move-object/from16 v36, v12

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v8

    :cond_2500
    check-cast v8, Ljava/lang/reflect/Method;

    const/4 v1, 0x0

    invoke-virtual {v8, v1, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    :try_end_2506
    .catchall {:try_start_24c1 .. :try_end_2506} :catchall_3f5f

    const v1, -0x502ad334

    .line 1494
    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_2550

    const/4 v3, 0x0

    invoke-static {v3, v3}, Landroid/widget/ExpandableListView;->getPackedPositionForChild(II)J

    move-result-wide v12

    const-wide/16 v14, 0x0

    cmp-long v1, v12, v14

    const/16 v8, 0x11

    rsub-int/lit8 v30, v1, 0x11

    invoke-static {v3}, Landroid/view/KeyEvent;->normalizeMetaState(I)I

    move-result v1

    int-to-char v1, v1

    invoke-static {v7}, Landroid/view/KeyEvent;->keyCodeFromString(Ljava/lang/String;)I

    move-result v3

    rsub-int v3, v3, 0x3f7

    const v33, -0x3eee44c8

    const/16 v34, 0x0

    const/16 v8, 0x28

    int-to-byte v8, v8

    or-int/lit8 v10, v8, 0x6

    int-to-short v10, v10

    sget-object v12, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v13, 0x1a

    aget-byte v12, v12, v13

    int-to-byte v12, v12

    const/4 v13, 0x1

    new-array v14, v13, [Ljava/lang/Object;

    invoke-static {v8, v10, v12, v14}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v8, 0x0

    aget-object v10, v14, v8

    move-object/from16 v35, v10

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v1

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_2550
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v12

    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    const/4 v8, 0x0

    .line 1495
    new-array v10, v8, [Ljava/lang/Class;

    .line 1497
    invoke-virtual {v1, v6, v10}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    new-array v10, v8, [Ljava/lang/Object;

    invoke-virtual {v1, v3, v10}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Long;

    .line 1500
    invoke-virtual {v1}, Ljava/lang/Number;->longValue()J

    move-result-wide v14

    const v1, -0x5300e3c8

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_25be

    const/16 v3, 0x30

    invoke-static {v7, v3}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;C)I

    move-result v1

    const/16 v3, 0x11

    rsub-int/lit8 v30, v1, 0x11

    invoke-static {v7}, Landroid/text/TextUtils;->getTrimmedLength(Ljava/lang/CharSequence;)I

    move-result v1

    int-to-char v1, v1

    invoke-static {}, Landroid/os/Process;->myPid()I

    move-result v3

    shr-int/lit8 v3, v3, 0x16

    add-int/lit16 v3, v3, 0x3f7

    const v33, -0x3dc47434

    const/16 v34, 0x0

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v10, 0x2

    aget-byte v0, v8, v10

    int-to-byte v0, v0

    const/16 v10, 0x49

    aget-byte v8, v8, v10

    const/4 v10, 0x1

    sub-int/2addr v8, v10

    int-to-short v8, v8

    move-object/from16 v45, v2

    or-int/lit8 v2, v8, 0x2e

    int-to-byte v2, v2

    move-object/from16 v46, v5

    new-array v5, v10, [Ljava/lang/Object;

    invoke-static {v0, v8, v2, v5}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v0, 0x0

    aget-object v2, v5, v0

    move-object/from16 v35, v2

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v1

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    goto :goto_25c2

    :cond_25be
    move-object/from16 v45, v2

    move-object/from16 v46, v5

    :goto_25c2
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v0, 0x0

    invoke-virtual {v1, v0}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v1

    const/16 v0, 0x34

    shl-long/2addr v1, v0

    ushr-long/2addr v1, v0

    sub-long/2addr v14, v1

    const/16 v0, 0xc

    shr-long v1, v14, v0

    cmp-long v0, v12, v1

    if-nez v0, :cond_268e

    const v0, -0x6d0d0331

    .line 1517
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2629

    const/4 v1, 0x0

    invoke-static {v1}, Landroid/os/Process;->getThreadPriority(I)I

    move-result v0

    add-int/lit8 v0, v0, 0x14

    const/4 v2, 0x6

    shr-int/2addr v0, v2

    rsub-int/lit8 v30, v0, 0x12

    invoke-static {v1, v1}, Landroid/graphics/drawable/Drawable;->resolveOpacity(II)I

    move-result v0

    int-to-char v0, v0

    invoke-static {v1}, Landroid/telephony/cdma/CdmaCellLocation;->convertQuartSecToDecDegrees(I)D

    move-result-wide v2

    const-wide/16 v12, 0x0

    cmpl-double v1, v2, v12

    add-int/lit16 v1, v1, 0x3f7

    const v33, -0x3c994c5

    const/16 v34, 0x0

    sget-object v2, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v3, 0x49

    aget-byte v5, v2, v3

    const/4 v3, 0x1

    sub-int/2addr v5, v3

    int-to-byte v5, v5

    const/16 v8, 0x46

    aget-byte v8, v2, v8

    neg-int v8, v8

    int-to-short v8, v8

    const/16 v10, 0xb2

    aget-byte v2, v2, v10

    sub-int/2addr v2, v3

    int-to-byte v2, v2

    new-array v10, v3, [Ljava/lang/Object;

    invoke-static {v5, v8, v2, v10}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v3, v10, v2

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2629
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v2, v1, [I

    new-array v3, v1, [I

    new-array v5, v1, [I

    filled-new-array {v2, v3, v5}, [Ljava/lang/Object;

    move-result-object v2

    .line 1524
    aget-object v3, v0, v1

    check-cast v3, [I

    const/4 v5, 0x0

    aget v3, v3, v5

    aget-object v0, v0, v5

    check-cast v0, [I

    aget v0, v0, v5

    aget-object v8, v2, v1

    check-cast v8, [I

    aput v3, v8, v5

    aget-object v1, v2, v5

    check-cast v1, [I

    aput v0, v1, v5

    invoke-static/range {p0 .. p0}, Ljava/lang/System;->identityHashCode(Ljava/lang/Object;)I

    move-result v0

    const v1, -0x23bbcdc0

    or-int/2addr v1, v0

    not-int v1, v1

    const v3, 0x2332448f

    or-int/2addr v1, v3

    mul-int/lit16 v1, v1, 0x240

    const v3, -0x4e040af2

    add-int/2addr v3, v1

    not-int v0, v0

    const v1, -0x898931

    or-int/2addr v0, v1

    not-int v0, v0

    const v1, 0xc042240

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, 0x240

    add-int/2addr v3, v0

    const v0, -0x2510284e

    add-int/2addr v3, v0

    shl-int/lit8 v0, v3, 0xd

    xor-int/2addr v0, v3

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v3, v2, v1

    check-cast v3, [I

    const/4 v1, 0x0

    aput v0, v3, v1

    move v0, v1

    goto/16 :goto_2840

    .line 1529
    :cond_268e
    invoke-static {v11}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    .line 1538
    const-class v1, Ljava/lang/Object;

    filled-new-array {v1}, [Ljava/lang/Class;

    move-result-object v1

    .line 1541
    invoke-virtual {v0, v4, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1551
    filled-new-array/range {p0 .. p0}, [Ljava/lang/Object;

    move-result-object v1

    const/4 v2, 0x0

    invoke-virtual {v0, v2, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    invoke-virtual {v0}, Ljava/lang/Number;->intValue()I

    move-result v0

    const/4 v1, 0x2

    .line 1560
    :try_start_26ac
    new-array v2, v1, [Ljava/lang/Object;

    const v1, -0x562a6a0e

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    const/4 v3, 0x1

    aput-object v1, v2, v3

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v0

    const/4 v1, 0x0

    aput-object v0, v2, v1

    const v0, 0x3e50af86

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2718

    invoke-static {v1}, Landroid/os/Process;->getThreadPriority(I)I

    move-result v0

    add-int/lit8 v0, v0, 0x14

    const/4 v3, 0x6

    shr-int/2addr v0, v3

    rsub-int/lit8 v30, v0, 0x12

    invoke-static {v1}, Landroid/graphics/ImageFormat;->getBitsPerPixel(I)I

    move-result v0

    const/4 v3, 0x1

    add-int/2addr v0, v3

    int-to-char v0, v0

    invoke-static {v1, v1}, Landroid/graphics/drawable/Drawable;->resolveOpacity(II)I

    move-result v3

    add-int/lit16 v1, v3, 0x3f7

    const v33, 0x50943872

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v5, 0xac

    aget-byte v5, v3, v5

    neg-int v5, v5

    int-to-byte v5, v5

    const/16 v8, 0x5c

    int-to-short v8, v8

    const/16 v10, 0x49

    aget-byte v3, v3, v10

    const/4 v10, 0x1

    sub-int/2addr v3, v10

    int-to-byte v3, v3

    new-array v12, v10, [Ljava/lang/Object;

    invoke-static {v5, v8, v3, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v5, v12, v3

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/4 v5, 0x2

    new-array v8, v5, [Ljava/lang/Class;

    sget-object v5, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    aput-object v5, v8, v3

    sget-object v3, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x1

    aput-object v3, v8, v5

    move/from16 v31, v0

    move/from16 v32, v1

    move-object/from16 v36, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2718
    check-cast v0, Ljava/lang/reflect/Method;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v2}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    move-object v2, v0

    check-cast v2, [Ljava/lang/Object;
    :try_end_2722
    .catchall {:try_start_26ac .. :try_end_2722} :catchall_3f5f

    const v0, -0x6d0d0331

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2774

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtimeNanos()J

    move-result-wide v0

    const-wide/16 v12, 0x0

    cmp-long v0, v0, v12

    rsub-int/lit8 v30, v0, 0x13

    invoke-static {v7}, Landroid/os/Process;->getGidForName(Ljava/lang/String;)I

    move-result v0

    const/4 v1, 0x1

    add-int/2addr v0, v1

    int-to-char v0, v0

    invoke-static {}, Landroid/view/ViewConfiguration;->getJumpTapTimeout()I

    move-result v3

    const/16 v5, 0x10

    shr-int/2addr v3, v5

    rsub-int v3, v3, 0x3f7

    const v33, -0x3c994c5

    const/16 v34, 0x0

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v8, 0x49

    aget-byte v10, v5, v8

    sub-int/2addr v10, v1

    int-to-byte v8, v10

    const/16 v10, 0x46

    aget-byte v10, v5, v10

    neg-int v10, v10

    int-to-short v10, v10

    const/16 v12, 0xb2

    aget-byte v5, v5, v12

    sub-int/2addr v5, v1

    int-to-byte v5, v5

    new-array v12, v1, [Ljava/lang/Object;

    invoke-static {v8, v10, v5, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v1, 0x0

    aget-object v5, v12, v1

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2774
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v2}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    :try_start_277a
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v3, 0x0

    .line 1565
    new-array v5, v3, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v5}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    new-array v5, v3, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0
    :try_end_2791
    .catch Ljava/lang/Exception; {:try_start_277a .. :try_end_2791} :catch_3ec0

    .line 1579
    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v3

    const v5, -0x5300e3c8

    invoke-static {v5}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v5

    if-nez v5, :cond_27dd

    const/16 v5, 0x30

    const/4 v8, 0x0

    invoke-static {v7, v5, v8}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CI)I

    move-result v10

    add-int/lit8 v30, v10, 0x13

    invoke-static {v8, v8}, Landroid/view/View$MeasureSpec;->makeMeasureSpec(II)I

    move-result v5

    int-to-char v5, v5

    invoke-static {v8}, Landroid/view/View$MeasureSpec;->getMode(I)I

    move-result v10

    rsub-int v8, v10, 0x3f7

    const v33, -0x3dc47434

    const/16 v34, 0x0

    sget-object v10, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v12, 0x2

    aget-byte v13, v10, v12

    int-to-byte v12, v13

    const/16 v13, 0x49

    aget-byte v10, v10, v13

    const/4 v13, 0x1

    sub-int/2addr v10, v13

    int-to-short v10, v10

    or-int/lit8 v14, v10, 0x2e

    int-to-byte v14, v14

    new-array v15, v13, [Ljava/lang/Object;

    invoke-static {v12, v10, v14, v15}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v10, 0x0

    aget-object v12, v15, v10

    move-object/from16 v35, v12

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v5

    move/from16 v32, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v5

    :cond_27dd
    check-cast v5, Ljava/lang/reflect/Field;

    const/4 v8, 0x0

    invoke-virtual {v5, v8, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v3, 0xc

    shr-long/2addr v0, v3

    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, -0x502ad334

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_2839

    const/16 v3, 0x30

    const/4 v5, 0x0

    invoke-static {v7, v3, v5}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CI)I

    move-result v1

    const/16 v3, 0x11

    rsub-int/lit8 v30, v1, 0x11

    invoke-static {v5, v5}, Landroid/widget/ExpandableListView;->getPackedPositionForChild(II)J

    move-result-wide v12

    const-wide/16 v14, 0x0

    cmp-long v1, v12, v14

    rsub-int/lit8 v1, v1, -0x1

    int-to-char v1, v1

    const v3, -0xfffc09

    invoke-static {v5, v5, v5}, Landroid/graphics/Color;->rgb(III)I

    move-result v8

    sub-int v32, v3, v8

    const v33, -0x3eee44c8

    const/16 v34, 0x0

    const/16 v3, 0x28

    int-to-byte v3, v3

    or-int/lit8 v5, v3, 0x6

    int-to-short v5, v5

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v10, 0x1a

    aget-byte v8, v8, v10

    int-to-byte v8, v8

    const/4 v10, 0x1

    new-array v12, v10, [Ljava/lang/Object;

    invoke-static {v3, v5, v8, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v5, v12, v3

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_2839
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/4 v0, 0x0

    .line 1588
    :goto_2840
    aget-object v1, v2, v0

    move-object v3, v1

    check-cast v3, [I

    aget v3, v3, v0

    const/4 v5, 0x1

    aget-object v8, v2, v5

    move-object v10, v8

    check-cast v10, [I

    aget v10, v10, v0

    if-ne v10, v3, :cond_28c1

    new-array v3, v5, [I

    new-array v10, v5, [I

    new-array v12, v5, [I

    filled-new-array {v3, v10, v12}, [Ljava/lang/Object;

    move-result-object v3

    const/4 v10, 0x2

    aget-object v2, v2, v10

    check-cast v2, [I

    aget v2, v2, v0

    check-cast v8, [I

    aget v8, v8, v0

    check-cast v1, [I

    aget v1, v1, v0

    aget-object v10, v3, v5

    check-cast v10, [I

    aput v8, v10, v0

    aget-object v5, v3, v0

    check-cast v5, [I

    aput v1, v5, v0

    const-string v1, "android.app.ActivityThread"

    invoke-static {v1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    const-string v5, "currentApplication"

    new-array v8, v0, [Ljava/lang/Class;

    invoke-virtual {v1, v5, v8}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/res/Resources;->getConfiguration()Landroid/content/res/Configuration;

    move-result-object v0

    iget v0, v0, Landroid/content/res/Configuration;->mcc:I

    not-int v1, v0

    const v5, -0x48a10061

    or-int/2addr v1, v5

    not-int v1, v1

    mul-int/lit16 v1, v1, 0x82

    const v5, 0x4c8fbcb2    # 7.535963E7f

    add-int/2addr v1, v5

    const v5, -0x48a10061

    or-int/2addr v0, v5

    not-int v0, v0

    const v5, 0x48200e

    or-int/2addr v0, v5

    mul-int/lit16 v0, v0, 0x82

    add-int/2addr v1, v0

    add-int/2addr v2, v1

    shl-int/lit8 v0, v2, 0xd

    xor-int/2addr v0, v2

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v2, v3, v1

    check-cast v2, [I

    const/4 v5, 0x0

    aput v0, v2, v5

    move v1, v5

    goto :goto_2929

    :cond_28c1
    move v5, v0

    const/4 v1, 0x2

    add-int/lit8 v0, v10, -0x1

    mul-int/2addr v0, v10

    .line 1602
    rem-int/2addr v0, v1

    div-int/2addr v10, v0

    const/4 v0, 0x0

    invoke-static {v0, v10, v5}, Landroid/widget/Toast;->makeText(Landroid/content/Context;II)Landroid/widget/Toast;

    move-result-object v3

    .line 1604
    invoke-virtual {v3}, Landroid/widget/Toast;->show()V

    const/4 v0, 0x1

    .line 1620
    new-array v3, v0, [I

    new-array v8, v0, [I

    new-array v10, v0, [I

    filled-new-array {v3, v8, v10}, [Ljava/lang/Object;

    move-result-object v3

    .line 1629
    aget-object v8, v2, v1

    check-cast v8, [I

    aget v1, v8, v5

    aget-object v8, v2, v0

    check-cast v8, [I

    aget v8, v8, v5

    aget-object v2, v2, v5

    check-cast v2, [I

    aget v2, v2, v5

    aget-object v10, v3, v0

    check-cast v10, [I

    aput v8, v10, v5

    aget-object v0, v3, v5

    check-cast v0, [I

    aput v2, v0, v5

    invoke-static {}, Ljava/lang/Runtime;->getRuntime()Ljava/lang/Runtime;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Runtime;->maxMemory()J

    move-result-wide v12

    long-to-int v0, v12

    const v2, 0x2de04601

    or-int/2addr v0, v2

    not-int v0, v0

    const/high16 v2, 0x8e00000

    or-int/2addr v2, v0

    mul-int/lit16 v2, v2, -0xc4

    const v5, -0x35c36436    # -3090162.5f

    add-int/2addr v2, v5

    const v5, 0x25004601

    or-int/2addr v0, v5

    mul-int/lit16 v0, v0, 0xc4

    add-int/2addr v2, v0

    add-int/2addr v1, v2

    shl-int/lit8 v0, v1, 0xd

    xor-int/2addr v0, v1

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v2, v3, v1

    check-cast v2, [I

    const/4 v1, 0x0

    aput v0, v2, v1

    :goto_2929
    const v0, 0x5d8c1285

    .line 1641
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2977

    invoke-static {v1}, Landroid/view/KeyEvent;->normalizeMetaState(I)I

    move-result v0

    add-int/lit8 v30, v0, 0x18

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtimeNanos()J

    move-result-wide v0

    const-wide/16 v12, 0x0

    cmp-long v0, v0, v12

    add-int/lit8 v0, v0, -0x1

    int-to-char v0, v0

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtimeNanos()J

    move-result-wide v1

    cmp-long v1, v1, v12

    add-int/lit16 v1, v1, 0x2c9

    const v33, 0x33488571

    const/16 v34, 0x0

    sget-object v2, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v5, 0x4

    aget-byte v8, v2, v5

    const/4 v5, 0x1

    sub-int/2addr v8, v5

    int-to-byte v8, v8

    const/16 v10, 0x85

    int-to-short v10, v10

    const/16 v12, 0xb2

    aget-byte v2, v2, v12

    sub-int/2addr v2, v5

    int-to-byte v2, v2

    new-array v12, v5, [Ljava/lang/Object;

    invoke-static {v8, v10, v2, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v5, v12, v2

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2977
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v12

    .line 1647
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v2, 0x0

    .line 1649
    new-array v5, v2, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v5}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    new-array v5, v2, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    .line 1656
    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0

    const v5, 0x632af61

    invoke-static {v5}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v5

    if-nez v5, :cond_29e7

    invoke-static {v2}, Landroid/widget/ExpandableListView;->getPackedPositionForGroup(I)J

    move-result-wide v14

    const-wide/16 v27, 0x0

    cmp-long v5, v14, v27

    add-int/lit8 v30, v5, 0x18

    invoke-static {}, Landroid/view/ViewConfiguration;->getGlobalActionKeyTimeout()J

    move-result-wide v14

    cmp-long v5, v14, v27

    const/4 v8, 0x1

    rsub-int/lit8 v5, v5, 0x1

    int-to-char v5, v5

    invoke-static {v2, v2}, Landroid/graphics/drawable/Drawable;->resolveOpacity(II)I

    move-result v8

    add-int/lit16 v2, v8, 0x2ca

    const v33, 0x68f63895

    const/16 v34, 0x0

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v10, 0xe

    aget-byte v10, v8, v10

    int-to-byte v10, v10

    or-int/lit16 v14, v10, 0x86

    int-to-short v14, v14

    const/16 v15, 0xb2

    aget-byte v8, v8, v15

    const/4 v15, 0x1

    sub-int/2addr v8, v15

    int-to-byte v8, v8

    move-object/from16 v47, v3

    new-array v3, v15, [Ljava/lang/Object;

    invoke-static {v10, v14, v8, v3}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v8, 0x0

    aget-object v3, v3, v8

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v5

    move/from16 v32, v2

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v5

    goto :goto_29e9

    :cond_29e7
    move-object/from16 v47, v3

    :goto_29e9
    check-cast v5, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    invoke-virtual {v5, v2}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v14

    const/16 v2, 0x34

    shl-long/2addr v14, v2

    ushr-long/2addr v14, v2

    sub-long/2addr v0, v14

    const/16 v2, 0xc

    shr-long/2addr v0, v2

    cmp-long v0, v12, v0

    if-nez v0, :cond_2adb

    const v0, 0x2735df86

    .line 1674
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2a4c

    invoke-static {}, Landroid/view/ViewConfiguration;->getScrollDefaultDelay()I

    move-result v0

    const/16 v1, 0x10

    shr-int/2addr v0, v1

    rsub-int/lit8 v30, v0, 0x18

    invoke-static {}, Landroid/media/AudioTrack;->getMaxVolume()F

    move-result v0

    const/4 v2, 0x0

    cmpl-float v0, v0, v2

    const/4 v2, 0x1

    rsub-int/lit8 v0, v0, 0x1

    int-to-char v0, v0

    invoke-static {}, Landroid/view/ViewConfiguration;->getScrollDefaultDelay()I

    move-result v2

    shr-int/2addr v2, v1

    rsub-int v1, v2, 0x2ca

    const v33, 0x49f14872

    const/16 v34, 0x0

    sget-object v2, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v3, 0x54

    aget-byte v3, v2, v3

    int-to-byte v3, v3

    const/16 v5, 0xb1

    int-to-short v5, v5

    const/16 v8, 0xb2

    aget-byte v2, v2, v8

    const/4 v8, 0x1

    sub-int/2addr v2, v8

    int-to-byte v2, v2

    new-array v10, v8, [Ljava/lang/Object;

    invoke-static {v3, v5, v2, v10}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v3, v10, v2

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2a4c
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/Object;

    const/4 v1, 0x4

    new-array v2, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v3, v1, [I

    aput-object v3, v2, v1

    new-array v5, v1, [I

    const/4 v8, 0x2

    aput-object v5, v2, v8

    new-array v5, v1, [I

    const/4 v8, 0x3

    aput-object v5, v2, v8

    .line 1677
    aget-object v10, v0, v8

    check-cast v10, [I

    const/4 v8, 0x0

    aget v10, v10, v8

    aget-object v12, v0, v1

    check-cast v12, [I

    aget v1, v12, v8

    aget-object v0, v0, v8

    check-cast v0, [Ljava/lang/String;

    check-cast v5, [I

    aput v10, v5, v8

    check-cast v3, [I

    aput v1, v3, v8

    aput-object v0, v2, v8

    const-string v0, "android.app.ActivityThread"

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const-string v1, "currentApplication"

    new-array v3, v8, [Ljava/lang/Class;

    invoke-virtual {v0, v1, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/res/Resources;->getConfiguration()Landroid/content/res/Configuration;

    move-result-object v0

    iget v0, v0, Landroid/content/res/Configuration;->touchscreen:I

    not-int v1, v0

    const v3, -0x5c7936fc

    or-int/2addr v3, v1

    not-int v3, v3

    const v5, -0xb13e401

    or-int/2addr v5, v0

    not-int v5, v5

    or-int/2addr v3, v5

    mul-int/lit16 v3, v3, 0xd2

    const v5, 0x13dc8115

    add-int/2addr v5, v3

    const v3, -0x302c001

    or-int/2addr v1, v3

    not-int v1, v1

    const v3, -0x546812fc

    or-int/2addr v0, v3

    not-int v0, v0

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, 0xd2

    add-int/2addr v5, v0

    const v0, 0x70444f7

    add-int/2addr v5, v0

    shl-int/lit8 v0, v5, 0xd

    xor-int/2addr v0, v5

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v3, v2, v1

    check-cast v3, [I

    const/4 v1, 0x0

    aput v0, v3, v1

    move-object/from16 v1, v23

    :goto_2ad8
    const/4 v0, 0x1

    goto/16 :goto_2ccf

    .line 1684
    :cond_2adb
    invoke-static {v11}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    .line 1687
    const-class v1, Ljava/lang/Object;

    filled-new-array {v1}, [Ljava/lang/Class;

    move-result-object v1

    .line 1693
    invoke-virtual {v0, v4, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1701
    filled-new-array/range {p0 .. p0}, [Ljava/lang/Object;

    move-result-object v1

    const/4 v2, 0x0

    invoke-virtual {v0, v2, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Integer;

    invoke-virtual {v0}, Ljava/lang/Number;->intValue()I

    move-result v0

    const/4 v1, 0x2

    :try_start_2af9
    new-array v2, v1, [Ljava/lang/Object;

    const v1, 0x70444f7

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    const/4 v3, 0x1

    aput-object v1, v2, v3

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v0

    const/4 v1, 0x0

    aput-object v0, v2, v1

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    new-array v3, v1, [Ljava/lang/Class;

    move-object/from16 v1, v23

    invoke-virtual {v0, v1, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v3, 0x0

    invoke-virtual {v0, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v3, 0x7f130102

    invoke-virtual {v0, v3}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v0

    const/4 v3, 0x4

    const/4 v5, 0x0

    invoke-virtual {v0, v5, v3}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v0

    const/4 v3, 0x1

    invoke-virtual {v0, v3}, Ljava/lang/String;->codePointAt(I)I

    move-result v0

    add-int/lit8 v0, v0, -0x5

    invoke-static {v5}, Landroid/view/KeyEvent;->normalizeMetaState(I)I

    move-result v3

    add-int/lit8 v3, v3, 0x64

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v8

    new-array v10, v5, [Ljava/lang/Class;

    invoke-virtual {v8, v1, v10}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v5

    const/4 v8, 0x0

    invoke-virtual {v5, v8, v8}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/content/Context;

    invoke-virtual {v5}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v5

    invoke-virtual {v5}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v5

    const/4 v8, 0x6

    invoke-virtual {v5, v8}, Ljava/lang/String;->codePointAt(I)I

    move-result v5

    add-int/lit16 v5, v5, 0x3cfd

    int-to-char v5, v5

    const/4 v8, 0x1

    new-array v10, v8, [Ljava/lang/Object;

    invoke-static {v0, v3, v5, v10}, Lmx/com/cfe/cfecontigo/App;->a(IIC[Ljava/lang/Object;)V

    const/4 v0, 0x0

    aget-object v3, v10, v0

    check-cast v3, Ljava/lang/String;

    invoke-virtual {v3}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    sget v3, Lmx/com/cfe/cfecontigo/App;->$$e:I

    and-int/lit8 v3, v3, 0x38

    int-to-byte v3, v3

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$d:[B

    const/16 v8, 0x50

    aget-byte v8, v5, v8

    int-to-short v8, v8

    const/16 v10, 0xc

    aget-byte v5, v5, v10

    int-to-byte v5, v5

    const/4 v10, 0x1

    new-array v12, v10, [Ljava/lang/Object;

    invoke-static {v3, v8, v5, v12}, Lmx/com/cfe/cfecontigo/App;->e(BSS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v5, v12, v3

    check-cast v5, Ljava/lang/String;

    const/4 v8, 0x2

    new-array v10, v8, [Ljava/lang/Class;

    sget-object v8, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    aput-object v8, v10, v3

    sget-object v3, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v8, 0x1

    aput-object v3, v10, v8

    invoke-virtual {v0, v5, v10}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v3, 0x0

    invoke-virtual {v0, v3, v2}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    move-object v2, v0

    check-cast v2, [Ljava/lang/Object;
    :try_end_2ba9
    .catchall {:try_start_2af9 .. :try_end_2ba9} :catchall_3f36

    const v0, 0x2735df86

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2bf5

    const/4 v3, 0x0

    const/4 v5, 0x0

    invoke-static {v5, v3, v3}, Landroid/util/TypedValue;->complexToFraction(IFF)F

    move-result v0

    cmpl-float v0, v0, v3

    add-int/lit8 v30, v0, 0x18

    invoke-static {v5, v5}, Landroid/view/Gravity;->getAbsoluteGravity(II)I

    move-result v0

    int-to-char v0, v0

    invoke-static {}, Landroid/media/AudioTrack;->getMinVolume()F

    move-result v5

    cmpl-float v5, v5, v3

    rsub-int v3, v5, 0x2ca

    const v33, 0x49f14872

    const/16 v34, 0x0

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v8, 0x54

    aget-byte v8, v5, v8

    int-to-byte v8, v8

    const/16 v10, 0xb1

    int-to-short v10, v10

    const/16 v12, 0xb2

    aget-byte v5, v5, v12

    const/4 v12, 0x1

    sub-int/2addr v5, v12

    int-to-byte v5, v5

    new-array v13, v12, [Ljava/lang/Object;

    invoke-static {v8, v10, v5, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v8, v13, v5

    move-object/from16 v35, v8

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2bf5
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v0, v3, v2}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    .line 1706
    :try_start_2bfb
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v5, 0x0

    .line 1716
    new-array v8, v5, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v8}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1722
    new-array v8, v5, [Ljava/lang/Object;

    invoke-virtual {v0, v3, v8}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    .line 1726
    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v12
    :try_end_2c12
    .catch Ljava/lang/Exception; {:try_start_2bfb .. :try_end_2c12} :catch_3eba

    invoke-static {v12, v13}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v3, 0x632af61

    invoke-static {v3}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v3

    if-nez v3, :cond_2c68

    invoke-static {}, Landroid/view/ViewConfiguration;->getKeyRepeatTimeout()I

    move-result v3

    const/16 v5, 0x10

    shr-int/2addr v3, v5

    add-int/lit8 v30, v3, 0x18

    const/4 v3, 0x0

    invoke-static {v3}, Landroid/widget/ExpandableListView;->getPackedPositionForGroup(I)J

    move-result-wide v14

    const-wide/16 v27, 0x0

    cmp-long v3, v14, v27

    int-to-char v3, v3

    invoke-static {}, Landroid/view/ViewConfiguration;->getLongPressTimeout()I

    move-result v8

    shr-int/2addr v8, v5

    add-int/lit16 v5, v8, 0x2ca

    const v33, 0x68f63895

    const/16 v34, 0x0

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v10, 0xe

    aget-byte v10, v8, v10

    int-to-byte v10, v10

    or-int/lit16 v14, v10, 0x86

    int-to-short v14, v14

    const/16 v15, 0xb2

    aget-byte v8, v8, v15

    const/4 v15, 0x1

    sub-int/2addr v8, v15

    int-to-byte v8, v8

    move-object/from16 v23, v2

    new-array v2, v15, [Ljava/lang/Object;

    invoke-static {v10, v14, v8, v2}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v8, 0x0

    aget-object v2, v2, v8

    move-object/from16 v35, v2

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v3

    move/from16 v32, v5

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v3

    goto :goto_2c6a

    :cond_2c68
    move-object/from16 v23, v2

    :goto_2c6a
    check-cast v3, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    invoke-virtual {v3, v2, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v0, 0xc

    shr-long v2, v12, v0

    .line 1729
    invoke-static {v2, v3}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v2, 0x5d8c1285

    invoke-static {v2}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v2

    if-nez v2, :cond_2cc5

    const/16 v2, 0x30

    const/4 v3, 0x0

    invoke-static {v7, v2, v3}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;CI)I

    move-result v5

    rsub-int/lit8 v30, v5, 0x17

    const-wide/16 v12, 0x0

    invoke-static {v12, v13}, Landroid/widget/ExpandableListView;->getPackedPositionChild(J)I

    move-result v2

    rsub-int/lit8 v2, v2, -0x1

    int-to-char v2, v2

    invoke-static {v7, v7, v3}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;I)I

    move-result v5

    add-int/lit16 v3, v5, 0x2ca

    const v33, 0x33488571

    const/16 v34, 0x0

    sget-object v5, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v8, 0x4

    aget-byte v10, v5, v8

    const/4 v8, 0x1

    sub-int/2addr v10, v8

    int-to-byte v10, v10

    const/16 v12, 0x85

    int-to-short v12, v12

    const/16 v13, 0xb2

    aget-byte v5, v5, v13

    sub-int/2addr v5, v8

    int-to-byte v5, v5

    new-array v13, v8, [Ljava/lang/Object;

    invoke-static {v10, v12, v5, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v8, v13, v5

    move-object/from16 v35, v8

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v2

    move/from16 v32, v3

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v2

    :cond_2cc5
    check-cast v2, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v2, v3, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    move-object/from16 v2, v23

    goto/16 :goto_2ad8

    .line 1732
    :goto_2ccf
    aget-object v3, v2, v0

    check-cast v3, [I

    const/4 v5, 0x0

    aget v3, v3, v5

    const/4 v8, 0x3

    .line 1733
    aget-object v10, v2, v8

    check-cast v10, [I

    aget v10, v10, v5

    if-ne v10, v3, :cond_3ea0

    const/4 v3, 0x4

    .line 1743
    new-array v10, v3, [Ljava/lang/Object;

    new-array v3, v0, [I

    aput-object v3, v10, v0

    new-array v12, v0, [I

    const/4 v13, 0x2

    aput-object v12, v10, v13

    new-array v12, v0, [I

    aput-object v12, v10, v8

    .line 1753
    aget-object v14, v2, v13

    check-cast v14, [I

    aget v13, v14, v5

    .line 1756
    aget-object v14, v2, v8

    check-cast v14, [I

    aget v8, v14, v5

    aget-object v14, v2, v0

    check-cast v14, [I

    aget v0, v14, v5

    aget-object v2, v2, v5

    check-cast v2, [Ljava/lang/String;

    check-cast v12, [I

    aput v8, v12, v5

    check-cast v3, [I

    aput v0, v3, v5

    aput-object v2, v10, v5

    invoke-static {}, Landroid/os/SystemClock;->elapsedRealtime()J

    move-result-wide v2

    long-to-int v0, v2

    not-int v2, v0

    const v3, -0x38af00e7

    or-int/2addr v3, v2

    not-int v3, v3

    const v5, -0x2ede1a16

    or-int/2addr v3, v5

    const v8, 0x38af00e6

    or-int/2addr v8, v0

    not-int v8, v8

    or-int/2addr v3, v8

    mul-int/lit16 v3, v3, -0x234

    const v8, 0x7c99f6f

    add-int/2addr v8, v3

    const v3, -0x6501a12

    or-int/2addr v0, v3

    not-int v0, v0

    mul-int/lit16 v0, v0, 0x468

    add-int/2addr v8, v0

    or-int v0, v5, v2

    not-int v0, v0

    const v2, -0x3eff1af8

    or-int/2addr v0, v2

    mul-int/lit16 v0, v0, 0x234

    add-int/2addr v8, v0

    add-int/2addr v13, v8

    shl-int/lit8 v0, v13, 0xd

    xor-int/2addr v0, v13

    ushr-int/lit8 v2, v0, 0x11

    xor-int/2addr v0, v2

    shl-int/lit8 v2, v0, 0x5

    xor-int/2addr v0, v2

    const/4 v2, 0x2

    aget-object v3, v10, v2

    check-cast v3, [I

    const/4 v2, 0x0

    aput v0, v3, v2

    const v0, 0x7855cfd9

    .line 1836
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2d96

    invoke-static {}, Landroid/view/ViewConfiguration;->getZoomControlsTimeout()J

    move-result-wide v12

    const-wide/16 v14, 0x0

    cmp-long v0, v12, v14

    add-int/lit8 v30, v0, 0x14

    invoke-static {v2, v2, v2}, Landroid/view/View;->resolveSizeAndState(III)I

    move-result v0

    int-to-char v0, v0

    invoke-static {}, Landroid/view/KeyEvent;->getModifierMetaStateMask()I

    move-result v2

    int-to-byte v2, v2

    rsub-int v2, v2, 0x332

    const v33, 0x1691582d

    const/16 v34, 0x0

    const/16 v3, 0x28

    int-to-byte v3, v3

    or-int/lit8 v5, v3, 0x6

    int-to-short v5, v5

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v12, 0x1a

    aget-byte v8, v8, v12

    int-to-byte v8, v8

    const/4 v12, 0x1

    new-array v13, v12, [Ljava/lang/Object;

    invoke-static {v3, v5, v8, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v5, v13, v3

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v2

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2d96
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v2, 0x0

    invoke-virtual {v0, v2}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v12

    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v3, 0x0

    .line 1840
    new-array v5, v3, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v5}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1842
    new-array v5, v3, [Ljava/lang/Object;

    invoke-virtual {v0, v2, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v2

    const v0, -0x4985fabb

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2e05

    invoke-static {}, Landroid/os/Process;->getElapsedCpuTime()J

    move-result-wide v14

    const-wide/16 v27, 0x0

    cmp-long v0, v14, v27

    rsub-int/lit8 v30, v0, 0x16

    invoke-static {v7}, Landroid/os/Process;->getGidForName(Ljava/lang/String;)I

    move-result v0

    const/4 v5, 0x1

    add-int/2addr v0, v5

    int-to-char v0, v0

    const/4 v8, 0x0

    invoke-static {v8}, Landroid/graphics/Color;->alpha(I)I

    move-result v14

    rsub-int v8, v14, 0x333

    const v33, -0x27416d4f

    const/16 v34, 0x0

    sget-object v14, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v15, 0x2

    aget-byte v5, v14, v15

    int-to-byte v5, v5

    const/16 v15, 0x49

    aget-byte v14, v14, v15

    const/4 v15, 0x1

    sub-int/2addr v14, v15

    int-to-short v14, v14

    move-object/from16 v23, v10

    or-int/lit8 v10, v14, 0x2e

    int-to-byte v10, v10

    move-object/from16 v48, v1

    new-array v1, v15, [Ljava/lang/Object;

    invoke-static {v5, v14, v10, v1}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v1, v1, v5

    move-object/from16 v35, v1

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    goto :goto_2e09

    :cond_2e05
    move-object/from16 v48, v1

    move-object/from16 v23, v10

    :goto_2e09
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v14

    const/16 v0, 0x34

    shl-long/2addr v14, v0

    ushr-long/2addr v14, v0

    sub-long/2addr v2, v14

    const/16 v0, 0xc

    shr-long v1, v2, v0

    cmp-long v0, v12, v1

    if-nez v0, :cond_2edd

    const v0, -0x5093124

    .line 1860
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2e6b

    const v0, 0x1000015

    const/4 v1, 0x0

    invoke-static {v1, v1, v1}, Landroid/graphics/Color;->rgb(III)I

    move-result v2

    add-int v30, v2, v0

    invoke-static {v1}, Landroid/view/View$MeasureSpec;->getMode(I)I

    move-result v0

    int-to-char v0, v0

    invoke-static {v1}, Landroid/graphics/Color;->alpha(I)I

    move-result v2

    rsub-int v1, v2, 0x333

    const v33, -0x6bcda6d8

    const/16 v34, 0x0

    sget-object v2, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v3, 0x49

    aget-byte v5, v2, v3

    const/4 v3, 0x1

    sub-int/2addr v5, v3

    int-to-byte v5, v5

    const/16 v8, 0x46

    aget-byte v8, v2, v8

    neg-int v8, v8

    int-to-short v8, v8

    const/16 v10, 0xb2

    aget-byte v2, v2, v10

    sub-int/2addr v2, v3

    int-to-byte v2, v2

    new-array v10, v3, [Ljava/lang/Object;

    invoke-static {v5, v8, v2, v10}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v3, v10, v2

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2e6b
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/Object;

    const/4 v1, 0x4

    .line 1865
    new-array v2, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v3, v1, [I

    const/4 v5, 0x0

    aput-object v3, v2, v5

    new-array v8, v1, [I

    aput-object v8, v2, v1

    new-array v10, v1, [I

    const/4 v12, 0x2

    aput-object v10, v2, v12

    .line 1866
    aget-object v10, v0, v1

    check-cast v10, [I

    aget v1, v10, v5

    aget-object v10, v0, v5

    check-cast v10, [I

    aget v10, v10, v5

    const/4 v12, 0x3

    aget-object v0, v0, v12

    check-cast v0, [Ljava/lang/String;

    check-cast v8, [I

    aput v1, v8, v5

    check-cast v3, [I

    aput v10, v3, v5

    aput-object v0, v2, v12

    invoke-static/range {p0 .. p0}, Ljava/lang/System;->identityHashCode(Ljava/lang/Object;)I

    move-result v0

    const v1, -0x3f020bd

    or-int/2addr v1, v0

    not-int v1, v1

    const v3, 0xa00088

    or-int/2addr v1, v3

    mul-int/lit16 v3, v1, 0x3e0

    const v5, -0x69fb1149

    add-int/2addr v5, v3

    not-int v3, v0

    const v8, 0x37f5f7bf

    or-int/2addr v3, v8

    not-int v3, v3

    or-int/2addr v1, v3

    mul-int/lit16 v1, v1, -0x1f0

    add-int/2addr v5, v1

    const v1, 0x34a5d78b

    or-int/2addr v0, v1

    mul-int/lit16 v0, v0, 0x1f0

    add-int/2addr v5, v0

    const v0, -0x2b8df416

    add-int/2addr v5, v0

    shl-int/lit8 v0, v5, 0xd

    xor-int/2addr v0, v5

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v3, v2, v1

    check-cast v3, [I

    const/4 v1, 0x0

    aput v0, v3, v1

    move v0, v1

    goto/16 :goto_310a

    .line 1873
    :cond_2edd
    invoke-static {v11}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    .line 1879
    const-class v1, Ljava/lang/Object;

    filled-new-array {v1}, [Ljava/lang/Class;

    move-result-object v1

    invoke-virtual {v0, v4, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1883
    filled-new-array/range {p0 .. p0}, [Ljava/lang/Object;

    move-result-object v1

    const/4 v2, 0x0

    invoke-virtual {v0, v2, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    .line 1888
    check-cast v0, Ljava/lang/Integer;

    invoke-virtual {v0}, Ljava/lang/Number;->intValue()I

    move-result v0

    const/4 v1, 0x1

    .line 1890
    :try_start_2efb
    new-array v2, v1, [Ljava/lang/Object;

    const v1, -0x745ac03e

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    const/4 v3, 0x0

    aput-object v1, v2, v3

    const v1, 0x6cc71b55

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_2f3d

    invoke-static {}, Landroid/view/ViewConfiguration;->getKeyRepeatDelay()I

    move-result v1

    const/16 v3, 0x10

    shr-int/2addr v1, v3

    rsub-int/lit8 v30, v1, 0x15

    invoke-static {v7, v7}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)I

    move-result v1

    int-to-char v1, v1

    const/4 v3, 0x0

    invoke-static {v3, v3, v3}, Landroid/view/View;->resolveSizeAndState(III)I

    move-result v5

    rsub-int v5, v5, 0x3a3

    const v33, 0x2038ca1

    const/16 v34, 0x0

    const/16 v35, 0x0

    const/4 v8, 0x1

    new-array v10, v8, [Ljava/lang/Class;

    sget-object v8, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    aput-object v8, v10, v3

    move/from16 v31, v1

    move/from16 v32, v5

    move-object/from16 v36, v10

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_2f3d
    check-cast v1, Ljava/lang/reflect/Constructor;

    invoke-virtual {v1, v2}, Ljava/lang/reflect/Constructor;->newInstance([Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    const/4 v2, 0x5

    .line 1897
    new-array v3, v2, [Ljava/lang/Object;

    const/4 v2, 0x0

    invoke-static {v2}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;

    move-result-object v5

    const/4 v2, 0x4

    aput-object v5, v3, v2

    const/4 v2, 0x3

    aput-object v1, v3, v2

    const v1, -0x2b8df416

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    const/4 v2, 0x2

    aput-object v1, v3, v2

    const-wide/16 v1, 0x0

    invoke-static {v1, v2}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v5

    const/4 v1, 0x1

    aput-object v5, v3, v1

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v0

    const/4 v1, 0x0

    aput-object v0, v3, v1

    const v0, -0x39f0dd40    # -9160.6875f

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_2fe5

    invoke-static {v7}, Landroid/view/MotionEvent;->axisFromString(Ljava/lang/String;)I

    move-result v0

    rsub-int/lit8 v30, v0, 0x14

    invoke-static {v1, v1}, Landroid/view/View;->combineMeasuredStates(II)I

    move-result v0

    int-to-char v0, v0

    invoke-static {v1}, Landroid/graphics/Color;->alpha(I)I

    move-result v2

    rsub-int v1, v2, 0x333

    const v33, -0x57344acc

    const/16 v34, 0x0

    const/16 v2, 0x28

    int-to-byte v2, v2

    or-int/lit8 v5, v2, 0x6

    int-to-short v5, v5

    sget-object v8, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v10, 0x1a

    aget-byte v8, v8, v10

    int-to-byte v8, v8

    const/4 v10, 0x1

    new-array v12, v10, [Ljava/lang/Object;

    invoke-static {v2, v5, v8, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v5, v12, v2

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/4 v5, 0x5

    new-array v8, v5, [Ljava/lang/Class;

    sget-object v5, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    aput-object v5, v8, v2

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x1

    aput-object v2, v8, v5

    sget-object v2, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x2

    aput-object v2, v8, v5

    invoke-static {v7, v7}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)I

    move-result v2

    const/16 v5, 0x1a

    rsub-int/lit8 v2, v2, 0x1a

    const/4 v5, 0x0

    invoke-static {v5}, Landroid/graphics/Color;->red(I)I

    move-result v10

    rsub-int v5, v10, 0x5128

    int-to-char v5, v5

    invoke-static {}, Landroid/os/Process;->myPid()I

    move-result v10

    shr-int/lit8 v10, v10, 0x16

    add-int/lit16 v10, v10, 0x389

    invoke-static {v2, v5, v10}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(ICI)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Ljava/lang/Class;

    const/4 v5, 0x3

    aput-object v2, v8, v5

    sget-object v2, Ljava/lang/Boolean;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x4

    aput-object v2, v8, v5

    move/from16 v31, v0

    move/from16 v32, v1

    move-object/from16 v36, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_2fe5
    check-cast v0, Ljava/lang/reflect/Method;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    move-object v2, v0

    check-cast v2, [Ljava/lang/Object;
    :try_end_2fef
    .catchall {:try_start_2efb .. :try_end_2fef} :catchall_3f5f

    const v0, -0x5093124

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_303d

    invoke-static {}, Landroid/view/ViewConfiguration;->getFadingEdgeLength()I

    move-result v0

    const/16 v1, 0x10

    shr-int/2addr v0, v1

    add-int/lit8 v30, v0, 0x15

    const/4 v0, 0x0

    invoke-static {v0}, Landroid/graphics/Color;->red(I)I

    move-result v1

    int-to-char v1, v1

    invoke-static {v0, v0}, Landroid/graphics/drawable/Drawable;->resolveOpacity(II)I

    move-result v3

    rsub-int v0, v3, 0x333

    const v33, -0x6bcda6d8

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v5, 0x49

    aget-byte v8, v3, v5

    const/4 v5, 0x1

    sub-int/2addr v8, v5

    int-to-byte v8, v8

    const/16 v10, 0x46

    aget-byte v10, v3, v10

    neg-int v10, v10

    int-to-short v10, v10

    const/16 v12, 0xb2

    aget-byte v3, v3, v12

    sub-int/2addr v3, v5

    int-to-byte v3, v3

    new-array v12, v5, [Ljava/lang/Object;

    invoke-static {v8, v10, v3, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v5, v12, v3

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v1

    move/from16 v32, v0

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_303d
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v2}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    .line 1910
    :try_start_3043
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v3, 0x0

    new-array v5, v3, [Ljava/lang/Class;

    .line 1918
    invoke-virtual {v0, v6, v5}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 1923
    new-array v5, v3, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    .line 1932
    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0
    :try_end_305a
    .catch Ljava/lang/Exception; {:try_start_3043 .. :try_end_305a} :catch_3e9a

    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v3

    const v5, -0x4985fabb

    invoke-static {v5}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v5

    if-nez v5, :cond_30ac

    invoke-static {}, Landroid/view/ViewConfiguration;->getFadingEdgeLength()I

    move-result v5

    const/16 v8, 0x10

    shr-int/2addr v5, v8

    rsub-int/lit8 v30, v5, 0x15

    const-wide/16 v12, 0x0

    invoke-static {v12, v13}, Landroid/widget/ExpandableListView;->getPackedPositionChild(J)I

    move-result v5

    rsub-int/lit8 v5, v5, -0x1

    int-to-char v5, v5

    invoke-static {}, Landroid/view/ViewConfiguration;->getZoomControlsTimeout()J

    move-result-wide v14

    cmp-long v8, v14, v12

    add-int/lit16 v8, v8, 0x332

    const v33, -0x27416d4f

    const/16 v34, 0x0

    sget-object v10, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v12, 0x2

    aget-byte v13, v10, v12

    int-to-byte v12, v13

    const/16 v13, 0x49

    aget-byte v10, v10, v13

    const/4 v13, 0x1

    sub-int/2addr v10, v13

    int-to-short v10, v10

    or-int/lit8 v14, v10, 0x2e

    int-to-byte v14, v14

    new-array v15, v13, [Ljava/lang/Object;

    invoke-static {v12, v10, v14, v15}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v10, 0x0

    aget-object v12, v15, v10

    move-object/from16 v35, v12

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v5

    move/from16 v32, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v5

    :cond_30ac
    check-cast v5, Ljava/lang/reflect/Field;

    const/4 v8, 0x0

    invoke-virtual {v5, v8, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v3, 0xc

    shr-long/2addr v0, v3

    .line 1941
    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, 0x7855cfd9

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_3103

    const/16 v1, 0x30

    invoke-static {v1}, Landroid/text/AndroidCharacter;->getMirror(C)C

    move-result v3

    add-int/lit8 v30, v3, -0x1b

    const/4 v3, 0x0

    invoke-static {v3}, Landroid/os/Process;->getThreadPriority(I)I

    move-result v5

    add-int/lit8 v5, v5, 0x14

    const/4 v3, 0x6

    shr-int/2addr v5, v3

    int-to-char v3, v5

    invoke-static {v7, v1}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;C)I

    move-result v5

    add-int/lit16 v1, v5, 0x334

    const v33, 0x1691582d

    const/16 v34, 0x0

    const/16 v5, 0x28

    int-to-byte v5, v5

    or-int/lit8 v8, v5, 0x6

    int-to-short v8, v8

    sget-object v10, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v12, 0x1a

    aget-byte v10, v10, v12

    int-to-byte v10, v10

    const/4 v12, 0x1

    new-array v13, v12, [Ljava/lang/Object;

    invoke-static {v5, v8, v10, v13}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v5, 0x0

    aget-object v8, v13, v5

    move-object/from16 v35, v8

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v3

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_3103
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/4 v0, 0x0

    .line 1953
    :goto_310a
    aget-object v1, v2, v0

    check-cast v1, [I

    aget v1, v1, v0

    const/4 v3, 0x1

    aget-object v5, v2, v3

    check-cast v5, [I

    aget v5, v5, v0

    if-ne v5, v1, :cond_3179

    const/4 v1, 0x4

    .line 1956
    new-array v5, v1, [Ljava/lang/Object;

    new-array v1, v3, [I

    aput-object v1, v5, v0

    new-array v8, v3, [I

    aput-object v8, v5, v3

    new-array v10, v3, [I

    const/4 v12, 0x2

    aput-object v10, v5, v12

    .line 1962
    aget-object v10, v2, v12

    check-cast v10, [I

    aget v10, v10, v0

    aget-object v12, v2, v3

    check-cast v12, [I

    aget v3, v12, v0

    aget-object v12, v2, v0

    check-cast v12, [I

    aget v12, v12, v0

    const/4 v13, 0x3

    aget-object v2, v2, v13

    check-cast v2, [Ljava/lang/String;

    check-cast v8, [I

    aput v3, v8, v0

    check-cast v1, [I

    aput v12, v1, v0

    aput-object v2, v5, v13

    invoke-static/range {p0 .. p0}, Ljava/lang/System;->identityHashCode(Ljava/lang/Object;)I

    move-result v0

    const v1, 0x1d706976

    or-int v2, v0, v1

    not-int v2, v2

    const v3, 0x1b258ed1

    or-int/2addr v2, v3

    mul-int/lit8 v2, v2, 0x38

    const v8, -0x466bd901

    add-int/2addr v2, v8

    not-int v0, v0

    or-int/2addr v0, v3

    not-int v0, v0

    or-int/2addr v0, v1

    mul-int/lit8 v0, v0, 0x38

    add-int/2addr v2, v0

    add-int/2addr v10, v2

    shl-int/lit8 v0, v10, 0xd

    xor-int/2addr v0, v10

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v2, v5, v1

    check-cast v2, [I

    const/4 v1, 0x0

    aput v0, v2, v1

    goto/16 :goto_320d

    :cond_3179
    new-instance v0, Ljava/util/ArrayList;

    .line 1968
    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    const/4 v1, 0x3

    aget-object v3, v2, v1

    check-cast v3, [Ljava/lang/String;

    if-eqz v3, :cond_3191

    const/4 v1, 0x0

    .line 1993
    :goto_3186
    array-length v8, v3

    if-ge v1, v8, :cond_3191

    .line 1997
    aget-object v8, v3, v1

    invoke-interface {v0, v8}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v1, v1, 0x1

    goto :goto_3186

    :cond_3191
    add-int/lit8 v0, v5, -0x1

    mul-int/2addr v0, v5

    const/4 v1, 0x2

    .line 2011
    rem-int/2addr v0, v1

    div-int/2addr v5, v0

    const/4 v0, 0x0

    const/4 v3, 0x0

    .line 2013
    invoke-static {v0, v5, v3}, Landroid/widget/Toast;->makeText(Landroid/content/Context;II)Landroid/widget/Toast;

    move-result-object v5

    .line 2017
    invoke-virtual {v5}, Landroid/widget/Toast;->show()V

    const/4 v0, 0x4

    .line 2019
    new-array v5, v0, [Ljava/lang/Object;

    const/4 v0, 0x1

    new-array v8, v0, [I

    aput-object v8, v5, v3

    new-array v10, v0, [I

    aput-object v10, v5, v0

    new-array v12, v0, [I

    aput-object v12, v5, v1

    .line 2053
    aget-object v12, v2, v1

    check-cast v12, [I

    aget v1, v12, v3

    .line 2058
    aget-object v12, v2, v0

    check-cast v12, [I

    aget v0, v12, v3

    aget-object v12, v2, v3

    check-cast v12, [I

    aget v12, v12, v3

    const/4 v13, 0x3

    aget-object v2, v2, v13

    check-cast v2, [Ljava/lang/String;

    check-cast v10, [I

    aput v0, v10, v3

    check-cast v8, [I

    aput v12, v8, v3

    aput-object v2, v5, v13

    invoke-static {}, Ljava/lang/Runtime;->getRuntime()Ljava/lang/Runtime;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Runtime;->freeMemory()J

    move-result-wide v2

    long-to-int v0, v2

    not-int v0, v0

    const v2, 0x98b2857

    or-int/2addr v2, v0

    not-int v2, v2

    const v3, 0x2f0acff0

    or-int v8, v2, v3

    mul-int/lit16 v8, v8, 0x2fc

    const v10, -0x14a6c495

    add-int/2addr v10, v8

    or-int/2addr v0, v3

    not-int v0, v0

    const v3, 0x812007

    or-int/2addr v0, v3

    mul-int/lit16 v0, v0, -0x5f8

    add-int/2addr v10, v0

    const v0, 0x2681e7a7

    or-int/2addr v0, v2

    mul-int/lit16 v0, v0, 0x2fc

    add-int/2addr v10, v0

    add-int/2addr v1, v10

    shl-int/lit8 v0, v1, 0xd

    xor-int/2addr v0, v1

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x2

    aget-object v2, v5, v1

    check-cast v2, [I

    const/4 v1, 0x0

    aput v0, v2, v1

    :goto_320d
    const v0, -0x723704d

    .line 2075
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_325a

    invoke-static {v1}, Landroid/view/View$MeasureSpec;->getSize(I)I

    move-result v0

    add-int/lit8 v30, v0, 0x18

    invoke-static {v1, v1, v1, v1}, Landroid/graphics/Color;->argb(IIII)I

    move-result v0

    int-to-char v0, v0

    invoke-static {}, Landroid/media/AudioTrack;->getMaxVolume()F

    move-result v1

    const/4 v2, 0x0

    cmpl-float v1, v1, v2

    rsub-int v1, v1, 0x22a

    const v33, -0x69e7e7b9

    const/16 v34, 0x0

    sget-object v2, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v3, 0x49

    aget-byte v8, v2, v3

    const/4 v3, 0x1

    sub-int/2addr v8, v3

    int-to-byte v8, v8

    const/16 v10, 0x46

    aget-byte v10, v2, v10

    neg-int v10, v10

    int-to-short v10, v10

    const/16 v12, 0xb2

    aget-byte v2, v2, v12

    sub-int/2addr v2, v3

    int-to-byte v2, v2

    new-array v12, v3, [Ljava/lang/Object;

    invoke-static {v8, v10, v2, v12}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v3, v12, v2

    move-object/from16 v35, v3

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_325a
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v2

    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v8, 0x0

    .line 2083
    new-array v10, v8, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v10}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 2085
    new-array v10, v8, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v10}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0

    const v10, -0x7a81e4e4

    .line 2091
    invoke-static {v10}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v10

    if-nez v10, :cond_32c4

    invoke-static {v8}, Landroid/view/View$MeasureSpec;->getSize(I)I

    move-result v10

    rsub-int/lit8 v30, v10, 0x18

    invoke-static {v7, v8, v8}, Landroid/text/TextUtils;->getCapsMode(Ljava/lang/CharSequence;II)I

    move-result v10

    int-to-char v10, v10

    invoke-static {v8}, Landroid/widget/ExpandableListView;->getPackedPositionForGroup(I)J

    move-result-wide v12

    const-wide/16 v14, 0x0

    cmp-long v8, v12, v14

    rsub-int v8, v8, 0x229

    const v33, -0x14457318

    const/16 v34, 0x0

    sget-object v12, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v13, 0x2

    aget-byte v14, v12, v13

    int-to-byte v13, v14

    const/16 v14, 0x49

    aget-byte v12, v12, v14

    const/4 v14, 0x1

    sub-int/2addr v12, v14

    int-to-short v12, v12

    or-int/lit8 v15, v12, 0x2e

    int-to-byte v15, v15

    move-object/from16 v26, v5

    new-array v5, v14, [Ljava/lang/Object;

    invoke-static {v13, v12, v15, v5}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v12, 0x0

    aget-object v5, v5, v12

    move-object/from16 v35, v5

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v10

    move/from16 v32, v8

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v10

    goto :goto_32c6

    :cond_32c4
    move-object/from16 v26, v5

    :goto_32c6
    check-cast v10, Ljava/lang/reflect/Field;

    const/4 v5, 0x0

    invoke-virtual {v10, v5}, Ljava/lang/reflect/Field;->getLong(Ljava/lang/Object;)J

    move-result-wide v12

    const/16 v5, 0x34

    shl-long/2addr v12, v5

    ushr-long/2addr v12, v5

    sub-long/2addr v0, v12

    const/16 v5, 0xc

    shr-long/2addr v0, v5

    cmp-long v0, v2, v0

    if-nez v0, :cond_338f

    const v0, -0x67c5074d

    .line 2098
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_331e

    const/16 v0, 0x30

    invoke-static {v0}, Landroid/text/AndroidCharacter;->getMirror(C)C

    move-result v1

    add-int/lit8 v8, v1, -0x18

    const/4 v1, 0x0

    invoke-static {v7, v0, v1}, Landroid/text/TextUtils;->lastIndexOf(Ljava/lang/CharSequence;CI)I

    move-result v0

    const/4 v2, 0x1

    add-int/2addr v0, v2

    int-to-char v9, v0

    invoke-static {v1}, Landroid/graphics/Color;->blue(I)I

    move-result v0

    add-int/lit16 v10, v0, 0x229

    const v11, -0x90190b9

    const/4 v12, 0x0

    sget-object v0, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v1, 0xac

    aget-byte v1, v0, v1

    neg-int v1, v1

    int-to-byte v1, v1

    const/16 v2, 0x5c

    int-to-short v2, v2

    const/16 v3, 0x49

    aget-byte v0, v0, v3

    const/4 v3, 0x1

    sub-int/2addr v0, v3

    int-to-byte v0, v0

    new-array v4, v3, [Ljava/lang/Object;

    invoke-static {v1, v2, v0, v4}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v0, 0x0

    aget-object v1, v4, v0

    move-object v13, v1

    check-cast v13, Ljava/lang/String;

    const/4 v14, 0x0

    invoke-static/range {v8 .. v14}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_331e
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Ljava/lang/reflect/Field;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Ljava/lang/Object;

    const/4 v1, 0x4

    .line 2111
    new-array v2, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v3, v1, [I

    const/4 v4, 0x0

    aput-object v3, v2, v4

    new-array v5, v1, [I

    aput-object v5, v2, v1

    new-array v5, v1, [I

    const/4 v1, 0x2

    aput-object v5, v2, v1

    .line 2118
    aget-object v6, v0, v4

    check-cast v6, [I

    aget v6, v6, v4

    aget-object v7, v0, v1

    check-cast v7, [I

    aget v1, v7, v4

    const/4 v7, 0x3

    aget-object v0, v0, v7

    check-cast v0, [Ljava/lang/String;

    check-cast v3, [I

    aput v6, v3, v4

    check-cast v5, [I

    aput v1, v5, v4

    invoke-static {}, Landroid/os/Process;->getStartElapsedRealtime()J

    move-result-wide v3

    long-to-int v1, v3

    not-int v3, v1

    const v4, -0x38e37009

    or-int/2addr v3, v4

    not-int v3, v3

    const/high16 v5, 0x28010000

    or-int/2addr v3, v5

    mul-int/lit16 v3, v3, -0xf5

    const v5, -0x29911ce8

    add-int/2addr v5, v3

    or-int/2addr v1, v4

    not-int v1, v1

    mul-int/lit16 v3, v1, -0xf5

    add-int/2addr v5, v3

    const v3, 0x16e6f21e

    or-int/2addr v1, v3

    mul-int/lit16 v1, v1, 0xf5

    add-int/2addr v5, v1

    const v1, -0x6f251b1d

    add-int/2addr v5, v1

    shl-int/lit8 v1, v5, 0xd

    xor-int/2addr v1, v5

    ushr-int/lit8 v3, v1, 0x11

    xor-int/2addr v1, v3

    shl-int/lit8 v3, v1, 0x5

    xor-int/2addr v1, v3

    const/4 v3, 0x1

    aget-object v4, v2, v3

    check-cast v4, [I

    const/4 v3, 0x0

    aput v1, v4, v3

    const/4 v1, 0x3

    aput-object v0, v2, v1

    move-object/from16 v8, v48

    :cond_338c
    :goto_338c
    const/4 v0, 0x2

    goto/16 :goto_37aa

    :cond_338f
    const/4 v3, 0x0

    .line 2130
    invoke-virtual/range {p0 .. p0}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    move-result-object v0

    if-nez v0, :cond_33a9

    .line 2138
    invoke-static/range {v25 .. v25}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    .line 2139
    new-array v1, v3, [Ljava/lang/Class;

    move-object/from16 v2, v44

    .line 2140
    invoke-virtual {v0, v2, v1}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v1, 0x0

    .line 2149
    invoke-virtual {v0, v1, v1}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    :cond_33a9
    if-eqz v0, :cond_33c0

    .line 2151
    instance-of v1, v0, Landroid/content/ContextWrapper;

    const/4 v2, 0x1

    if-eq v1, v2, :cond_33b1

    goto :goto_33ba

    .line 2153
    :cond_33b1
    move-object v1, v0

    check-cast v1, Landroid/content/ContextWrapper;

    invoke-virtual {v1}, Landroid/content/ContextWrapper;->getBaseContext()Landroid/content/Context;

    move-result-object v1

    if-eqz v1, :cond_33bf

    .line 2154
    :goto_33ba
    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    goto :goto_33c0

    :cond_33bf
    const/4 v0, 0x0

    .line 2169
    :cond_33c0
    :goto_33c0
    invoke-static {v11}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    .line 2179
    const-class v2, Ljava/lang/Object;

    filled-new-array {v2}, [Ljava/lang/Class;

    move-result-object v2

    invoke-virtual {v1, v4, v2}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    .line 2187
    filled-new-array/range {p0 .. p0}, [Ljava/lang/Object;

    move-result-object v2

    const/4 v3, 0x0

    .line 2192
    invoke-virtual {v1, v3, v2}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Ljava/lang/Integer;

    invoke-virtual {v1}, Ljava/lang/Number;->intValue()I

    move-result v1

    .line 2201
    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v2

    const/4 v4, 0x0

    new-array v5, v4, [Ljava/lang/Class;

    move-object/from16 v8, v48

    invoke-virtual {v2, v8, v5}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v2

    invoke-virtual {v2, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/content/Context;

    invoke-virtual {v2}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v2

    invoke-virtual {v2}, Landroid/content/Context;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;

    move-result-object v2

    iget v2, v2, Landroid/content/pm/ApplicationInfo;->targetSdkVersion:I

    add-int/lit8 v2, v2, 0x1c

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v5

    new-array v10, v4, [Ljava/lang/Class;

    invoke-virtual {v5, v8, v10}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v5

    invoke-virtual {v5, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Landroid/content/Context;

    invoke-virtual {v5}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v3

    invoke-virtual {v3}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    const v5, 0x7f130127

    invoke-virtual {v3, v5}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v3

    const/4 v5, 0x4

    invoke-virtual {v3, v4, v5}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/String;->length()I

    move-result v3

    add-int/lit16 v3, v3, 0x8a

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v5

    new-array v10, v4, [Ljava/lang/Class;

    invoke-virtual {v5, v8, v10}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v4

    const/4 v5, 0x0

    invoke-virtual {v4, v5, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/content/Context;

    invoke-virtual {v4}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v4

    invoke-virtual {v4}, Landroid/content/Context;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;

    move-result-object v4

    iget v4, v4, Landroid/content/pm/ApplicationInfo;->targetSdkVersion:I

    add-int/lit8 v4, v4, -0x24

    int-to-char v4, v4

    const/4 v5, 0x1

    new-array v10, v5, [Ljava/lang/Object;

    invoke-static {v2, v3, v4, v10}, Lmx/com/cfe/cfecontigo/App;->a(IIC[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v3, v10, v2

    check-cast v3, Ljava/lang/String;

    invoke-virtual {v3}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v2

    invoke-static {}, Landroid/view/ViewConfiguration;->getFadingEdgeLength()I

    move-result v3

    const/16 v4, 0x10

    shr-int/2addr v3, v4

    add-int/lit8 v3, v3, 0x40

    const/16 v4, 0x42

    new-array v4, v4, [C

    fill-array-data v4, :array_40e4

    const/4 v5, 0x1

    new-array v10, v5, [Ljava/lang/Object;

    invoke-static {v3, v4, v10}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v10, v3

    check-cast v4, Ljava/lang/String;

    invoke-virtual {v4}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v3

    filled-new-array {v2, v3}, [Ljava/lang/String;

    move-result-object v2

    const/4 v3, 0x5

    .line 2202
    :try_start_3477
    new-array v4, v3, [Ljava/lang/Object;

    const v3, -0x6f251b1d

    invoke-static {v3}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v3

    const/4 v5, 0x4

    aput-object v3, v4, v5

    const-wide/16 v10, 0x11

    invoke-static {v10, v11}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v3

    const/4 v5, 0x3

    aput-object v3, v4, v5

    invoke-static {v1}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    const/4 v3, 0x2

    aput-object v1, v4, v3

    const/4 v1, 0x1

    aput-object v2, v4, v1

    const/4 v1, 0x0

    aput-object v0, v4, v1

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v2

    new-array v3, v1, [Ljava/lang/Class;

    invoke-virtual {v2, v8, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    const/4 v2, 0x0

    invoke-virtual {v1, v2, v2}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    check-cast v1, Landroid/content/Context;

    invoke-virtual {v1}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    const-string v1, "&/%$UgUQBA=="

    const/4 v2, 0x1

    invoke-virtual {v1, v2}, Ljava/lang/String;->codePointAt(I)I

    move-result v1

    add-int/lit8 v1, v1, 0x7

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v2

    const/4 v3, 0x0

    new-array v5, v3, [Ljava/lang/Class;

    invoke-virtual {v2, v8, v5}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v2

    const/4 v3, 0x0

    invoke-virtual {v2, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/content/Context;

    invoke-virtual {v2}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v2

    invoke-virtual {v2}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v2

    const v3, 0x7f1300a7

    invoke-virtual {v2, v3}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v2

    const/4 v3, 0x4

    const/4 v5, 0x0

    invoke-virtual {v2, v5, v3}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v2

    const/4 v3, 0x3

    invoke-virtual {v2, v3}, Ljava/lang/String;->codePointAt(I)I

    move-result v2

    add-int/lit16 v2, v2, 0xaa

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v3

    new-array v10, v5, [Ljava/lang/Class;

    invoke-virtual {v3, v8, v10}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v3

    const/4 v5, 0x0

    invoke-virtual {v3, v5, v5}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/content/Context;

    invoke-virtual {v3}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v3

    invoke-virtual {v3}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    const v5, 0x7f130011

    invoke-virtual {v3, v5}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v3

    const/4 v5, 0x4

    const/4 v10, 0x0

    invoke-virtual {v3, v10, v5}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/String;->length()I

    move-result v3

    add-int/lit8 v3, v3, -0x4

    int-to-char v3, v3

    const/4 v5, 0x1

    new-array v11, v5, [Ljava/lang/Object;

    invoke-static {v1, v2, v3, v11}, Lmx/com/cfe/cfecontigo/App;->a(IIC[Ljava/lang/Object;)V

    aget-object v1, v11, v10

    check-cast v1, Ljava/lang/String;

    invoke-virtual {v1}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v1

    invoke-static {v1}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v1

    sget-object v2, Lmx/com/cfe/cfecontigo/App;->$$d:[B

    const/16 v3, 0x11

    aget-byte v5, v2, v3

    int-to-byte v3, v5

    const/16 v5, 0x8f

    int-to-short v5, v5

    const/16 v10, 0x4f

    aget-byte v2, v2, v10

    int-to-byte v2, v2

    const/4 v10, 0x1

    new-array v11, v10, [Ljava/lang/Object;

    invoke-static {v3, v5, v2, v11}, Lmx/com/cfe/cfecontigo/App;->e(BSS[Ljava/lang/Object;)V

    const/4 v2, 0x0

    aget-object v3, v11, v2

    check-cast v3, Ljava/lang/String;

    const/4 v5, 0x5

    new-array v10, v5, [Ljava/lang/Class;

    const-class v5, Landroid/content/Context;

    aput-object v5, v10, v2

    const-class v2, [Ljava/lang/String;

    const/4 v5, 0x1

    aput-object v2, v10, v5

    sget-object v2, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x2

    aput-object v2, v10, v5

    sget-object v2, Ljava/lang/Long;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x3

    aput-object v2, v10, v5

    sget-object v2, Ljava/lang/Integer;->TYPE:Ljava/lang/Class;

    const/4 v5, 0x4

    aput-object v2, v10, v5

    invoke-virtual {v1, v3, v10}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v1

    const/4 v2, 0x0

    invoke-virtual {v1, v2, v4}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    move-object v2, v1

    check-cast v2, [Ljava/lang/Object;
    :try_end_3564
    .catchall {:try_start_3477 .. :try_end_3564} :catchall_3f36

    const/4 v1, 0x2

    .line 2203
    aget-object v3, v2, v1

    check-cast v3, [I

    const/4 v1, 0x0

    aget v3, v3, v1

    aget-object v3, v2, v1

    check-cast v3, [I

    aget v3, v3, v1

    if-eqz v0, :cond_338c

    .line 3097
    sget v0, Lmx/com/cfe/cfecontigo/App;->MediaBrowserCompatSearchResultReceiver:I

    add-int/lit8 v0, v0, 0x4b

    rem-int/lit16 v1, v0, 0x80

    sput v1, Lmx/com/cfe/cfecontigo/App;->RatingCompat:I

    const/4 v1, 0x2

    rem-int/2addr v0, v1

    if-nez v0, :cond_3690

    const v0, -0x67c5074d

    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_35d2

    invoke-static {}, Landroid/os/Process;->getElapsedCpuTime()J

    move-result-wide v0

    const-wide/16 v3, 0x0

    cmp-long v0, v0, v3

    rsub-int/lit8 v30, v0, 0x19

    invoke-static {}, Landroid/view/ViewConfiguration;->getScrollBarFadeDuration()I

    move-result v0

    const/16 v1, 0x10

    shr-int/2addr v0, v1

    int-to-char v0, v0

    const/4 v1, 0x0

    const/4 v3, 0x0

    invoke-static {v3, v1, v1}, Landroid/util/TypedValue;->complexToFraction(IFF)F

    move-result v4

    cmpl-float v3, v4, v1

    rsub-int v1, v3, 0x229

    const v33, -0x90190b9

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v4, 0xac

    aget-byte v4, v3, v4

    neg-int v4, v4

    int-to-byte v4, v4

    const/16 v5, 0x5c

    int-to-short v5, v5

    const/16 v10, 0x49

    aget-byte v3, v3, v10

    const/4 v10, 0x1

    sub-int/2addr v3, v10

    int-to-byte v3, v3

    new-array v11, v10, [Ljava/lang/Object;

    invoke-static {v4, v5, v3, v11}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v11, v3

    move-object/from16 v35, v4

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_35d2
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v2}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    :try_start_35d8
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v3, 0x0

    new-array v4, v3, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v4}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v3, 0x1

    new-array v4, v3, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v4}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0
    :try_end_35f0
    .catch Ljava/lang/Exception; {:try_start_35d8 .. :try_end_35f0} :catch_37a4

    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v3

    const v4, -0x7a81e4e4

    invoke-static {v4}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v4

    if-nez v4, :cond_3633

    const/4 v5, 0x0

    invoke-static {v7, v7, v5, v5}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;Ljava/lang/CharSequence;II)I

    move-result v4

    rsub-int/lit8 v9, v4, 0x18

    invoke-static {v5, v5}, Landroid/view/View;->resolveSize(II)I

    move-result v4

    int-to-char v10, v4

    invoke-static {v5}, Landroid/graphics/Color;->red(I)I

    move-result v4

    add-int/lit16 v11, v4, 0x229

    const v12, -0x14457318

    const/4 v13, 0x0

    sget-object v4, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v5, 0x2

    aget-byte v6, v4, v5

    int-to-byte v5, v6

    const/16 v6, 0x49

    aget-byte v4, v4, v6

    const/4 v6, 0x1

    sub-int/2addr v4, v6

    int-to-short v4, v4

    or-int/lit8 v14, v4, 0x2e

    int-to-byte v14, v14

    new-array v15, v6, [Ljava/lang/Object;

    invoke-static {v5, v4, v14, v15}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v15, v4

    move-object v14, v5

    check-cast v14, Ljava/lang/String;

    const/4 v15, 0x0

    invoke-static/range {v9 .. v15}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v4

    :cond_3633
    check-cast v4, Ljava/lang/reflect/Field;

    const/4 v5, 0x0

    invoke-virtual {v4, v5, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v3, 0x75

    shr-long/2addr v0, v3

    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, -0x723704d

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_3688

    const/16 v1, 0x30

    const/4 v3, 0x0

    invoke-static {v7, v1, v3, v3}, Landroid/text/TextUtils;->indexOf(Ljava/lang/CharSequence;CII)I

    move-result v1

    rsub-int/lit8 v9, v1, 0x17

    const-wide/16 v4, 0x0

    invoke-static {v4, v5}, Landroid/widget/ExpandableListView;->getPackedPositionType(J)I

    move-result v1

    int-to-char v10, v1

    invoke-static {v3}, Landroid/graphics/Color;->blue(I)I

    move-result v1

    rsub-int v11, v1, 0x229

    const v12, -0x69e7e7b9

    const/4 v13, 0x0

    sget-object v1, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v3, 0x49

    aget-byte v3, v1, v3

    const/4 v4, 0x1

    sub-int/2addr v3, v4

    int-to-byte v3, v3

    const/16 v5, 0x46

    aget-byte v5, v1, v5

    neg-int v5, v5

    int-to-short v5, v5

    const/16 v6, 0xb2

    aget-byte v1, v1, v6

    sub-int/2addr v1, v4

    int-to-byte v1, v1

    new-array v6, v4, [Ljava/lang/Object;

    invoke-static {v3, v5, v1, v6}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v1, 0x0

    aget-object v3, v6, v1

    move-object v14, v3

    check-cast v14, Ljava/lang/String;

    const/4 v15, 0x0

    invoke-static/range {v9 .. v15}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_3688
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    goto/16 :goto_338c

    :cond_3690
    const v0, -0x67c5074d

    .line 2204
    invoke-static {v0}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v0

    if-nez v0, :cond_36e1

    const/4 v1, 0x0

    invoke-static {v1}, Landroid/telephony/cdma/CdmaCellLocation;->convertQuartSecToDecDegrees(I)D

    move-result-wide v3

    const-wide/16 v0, 0x0

    cmpl-double v0, v3, v0

    add-int/lit8 v30, v0, 0x18

    invoke-static {}, Landroid/view/ViewConfiguration;->getKeyRepeatDelay()I

    move-result v0

    const/16 v1, 0x10

    shr-int/2addr v0, v1

    int-to-char v0, v0

    const-wide/16 v3, 0x0

    invoke-static {v3, v4}, Landroid/widget/ExpandableListView;->getPackedPositionType(J)I

    move-result v1

    add-int/lit16 v1, v1, 0x229

    const v33, -0x90190b9

    const/16 v34, 0x0

    sget-object v3, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v4, 0xac

    aget-byte v4, v3, v4

    neg-int v4, v4

    int-to-byte v4, v4

    const/16 v5, 0x5c

    int-to-short v5, v5

    const/16 v10, 0x49

    aget-byte v3, v3, v10

    const/4 v10, 0x1

    sub-int/2addr v3, v10

    int-to-byte v3, v3

    new-array v11, v10, [Ljava/lang/Object;

    invoke-static {v4, v5, v3, v11}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v3, 0x0

    aget-object v4, v11, v3

    move-object/from16 v35, v4

    check-cast v35, Ljava/lang/String;

    const/16 v36, 0x0

    move/from16 v31, v0

    move/from16 v32, v1

    invoke-static/range {v30 .. v36}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v0

    :cond_36e1
    check-cast v0, Ljava/lang/reflect/Field;

    const/4 v1, 0x0

    invoke-virtual {v0, v1, v2}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    .line 2212
    :try_start_36e7
    invoke-static {v9}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v3, 0x0

    new-array v4, v3, [Ljava/lang/Class;

    invoke-virtual {v0, v6, v4}, Ljava/lang/Class;->getDeclaredMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    .line 2222
    new-array v4, v3, [Ljava/lang/Object;

    invoke-virtual {v0, v1, v4}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Number;->longValue()J

    move-result-wide v0
    :try_end_36fe
    .catch Ljava/lang/Exception; {:try_start_36e7 .. :try_end_36fe} :catch_37a4

    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v3

    const v4, -0x7a81e4e4

    invoke-static {v4}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v4

    if-nez v4, :cond_3745

    const/4 v5, 0x0

    invoke-static {v5}, Landroid/view/View$MeasureSpec;->getMode(I)I

    move-result v4

    rsub-int/lit8 v9, v4, 0x18

    invoke-static {v7, v5}, Landroid/text/TextUtils;->getOffsetAfter(Ljava/lang/CharSequence;I)I

    move-result v4

    int-to-char v10, v4

    invoke-static {}, Landroid/view/ViewConfiguration;->getZoomControlsTimeout()J

    move-result-wide v4

    const-wide/16 v6, 0x0

    cmp-long v4, v4, v6

    rsub-int v11, v4, 0x22a

    const v12, -0x14457318

    const/4 v13, 0x0

    sget-object v4, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/4 v5, 0x2

    aget-byte v6, v4, v5

    int-to-byte v5, v6

    const/16 v6, 0x49

    aget-byte v4, v4, v6

    const/4 v6, 0x1

    sub-int/2addr v4, v6

    int-to-short v4, v4

    or-int/lit8 v7, v4, 0x2e

    int-to-byte v7, v7

    new-array v14, v6, [Ljava/lang/Object;

    invoke-static {v5, v4, v7, v14}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v4, 0x0

    aget-object v5, v14, v4

    move-object v14, v5

    check-cast v14, Ljava/lang/String;

    const/4 v15, 0x0

    invoke-static/range {v9 .. v15}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v4

    :cond_3745
    check-cast v4, Ljava/lang/reflect/Field;

    const/4 v5, 0x0

    invoke-virtual {v4, v5, v3}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    const/16 v3, 0xc

    shr-long/2addr v0, v3

    .line 2225
    invoke-static {v0, v1}, Ljava/lang/Long;->valueOf(J)Ljava/lang/Long;

    move-result-object v0

    const v1, -0x723704d

    invoke-static {v1}, Lo/vvvv0076v0076v;->MediaBrowserCompatCustomActionResultReceiver(I)Ljava/lang/Object;

    move-result-object v1

    if-nez v1, :cond_379c

    const/4 v3, 0x0

    invoke-static {v3, v3}, Landroid/graphics/drawable/Drawable;->resolveOpacity(II)I

    move-result v1

    add-int/lit8 v9, v1, 0x18

    invoke-static {v3}, Landroid/util/TypedValue;->complexToFloat(I)F

    move-result v1

    const/4 v3, 0x0

    cmpl-float v1, v1, v3

    int-to-char v10, v1

    invoke-static {}, Landroid/view/ViewConfiguration;->getFadingEdgeLength()I

    move-result v1

    const/16 v3, 0x10

    shr-int/2addr v1, v3

    add-int/lit16 v11, v1, 0x229

    const v12, -0x69e7e7b9

    const/4 v13, 0x0

    sget-object v1, Lmx/com/cfe/cfecontigo/App;->$$a:[B

    const/16 v3, 0x49

    aget-byte v3, v1, v3

    const/4 v4, 0x1

    sub-int/2addr v3, v4

    int-to-byte v3, v3

    const/16 v5, 0x46

    aget-byte v5, v1, v5

    neg-int v5, v5

    int-to-short v5, v5

    const/16 v6, 0xb2

    aget-byte v1, v1, v6

    sub-int/2addr v1, v4

    int-to-byte v1, v1

    new-array v6, v4, [Ljava/lang/Object;

    invoke-static {v3, v5, v1, v6}, Lmx/com/cfe/cfecontigo/App;->d(BIS[Ljava/lang/Object;)V

    const/4 v1, 0x0

    aget-object v3, v6, v1

    move-object v14, v3

    check-cast v14, Ljava/lang/String;

    const/4 v15, 0x0

    invoke-static/range {v9 .. v15}, Lo/vvvv0076v0076v;->write(ICIIZLjava/lang/String;[Ljava/lang/Class;)Ljava/lang/Object;

    move-result-object v1

    :cond_379c
    check-cast v1, Ljava/lang/reflect/Field;

    const/4 v3, 0x0

    invoke-virtual {v1, v3, v0}, Ljava/lang/reflect/Field;->set(Ljava/lang/Object;Ljava/lang/Object;)V

    goto/16 :goto_338c

    :catch_37a4
    new-instance v0, Ljava/lang/RuntimeException;

    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    throw v0

    .line 2227
    :goto_37aa
    aget-object v1, v2, v0

    check-cast v1, [I

    const/4 v0, 0x0

    aget v1, v1, v0

    .line 2229
    aget-object v3, v2, v0

    check-cast v3, [I

    aget v3, v3, v0

    if-ne v3, v1, :cond_3e75

    const/4 v1, 0x4

    new-array v3, v1, [Ljava/lang/Object;

    const/4 v1, 0x1

    new-array v4, v1, [I

    aput-object v4, v3, v0

    new-array v5, v1, [I

    aput-object v5, v3, v1

    new-array v5, v1, [I

    const/4 v6, 0x2

    aput-object v5, v3, v6

    .line 2239
    aget-object v7, v2, v1

    check-cast v7, [I

    aget v1, v7, v0

    aget-object v7, v2, v0

    check-cast v7, [I

    aget v7, v7, v0

    aget-object v9, v2, v6

    check-cast v9, [I

    aget v6, v9, v0

    const/4 v9, 0x3

    aget-object v2, v2, v9

    check-cast v2, [Ljava/lang/String;

    check-cast v4, [I

    aput v7, v4, v0

    check-cast v5, [I

    aput v6, v5, v0

    invoke-static/range {p0 .. p0}, Ljava/lang/System;->identityHashCode(Ljava/lang/Object;)I

    move-result v0

    const v4, -0x23ca8002

    or-int/2addr v4, v0

    not-int v4, v4

    const v5, 0x2bffe225

    or-int/2addr v5, v0

    not-int v5, v5

    or-int/2addr v4, v5

    mul-int/lit8 v4, v4, 0x45

    const v5, 0x7b64c1da

    add-int/2addr v5, v4

    const v4, -0x2bebe026

    or-int/2addr v4, v0

    not-int v4, v4

    const v6, 0x8216024

    or-int/2addr v4, v6

    const v6, 0x23de8201

    or-int/2addr v0, v6

    not-int v0, v0

    or-int/2addr v0, v4

    mul-int/lit8 v0, v0, -0x45

    add-int/2addr v5, v0

    const v0, 0x5648a00

    add-int/2addr v5, v0

    add-int/2addr v1, v5

    shl-int/lit8 v0, v1, 0xd

    xor-int/2addr v0, v1

    ushr-int/lit8 v1, v0, 0x11

    xor-int/2addr v0, v1

    shl-int/lit8 v1, v0, 0x5

    xor-int/2addr v0, v1

    const/4 v1, 0x1

    aget-object v4, v3, v1

    check-cast v4, [I

    const/4 v1, 0x0

    aput v0, v4, v1

    const/4 v0, 0x3

    aput-object v2, v3, v0

    .line 2265
    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    new-array v2, v1, [Ljava/lang/Class;

    invoke-virtual {v0, v8, v2}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v2, 0x0

    invoke-virtual {v0, v2, v2}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v2, 0x7f1300af

    invoke-virtual {v0, v2}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v0

    const/4 v2, 0x4

    invoke-virtual {v0, v1, v2}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v1}, Ljava/lang/String;->codePointAt(I)I

    move-result v0

    add-int/lit8 v0, v0, -0x17

    const/16 v2, 0x12

    new-array v2, v2, [C

    fill-array-data v2, :array_412a

    const/4 v4, 0x1

    new-array v5, v4, [Ljava/lang/Object;

    invoke-static {v0, v2, v5}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    aget-object v0, v5, v1

    check-cast v0, Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v0

    const/4 v2, 0x0

    invoke-static {v1, v2, v2}, Landroid/util/TypedValue;->complexToFraction(IFF)F

    move-result v4

    cmpl-float v2, v4, v2

    add-int/lit8 v2, v2, 0x27

    const/16 v4, 0x2a

    new-array v4, v4, [C

    fill-array-data v4, :array_4140

    const/4 v5, 0x1

    new-array v6, v5, [Ljava/lang/Object;

    invoke-static {v2, v4, v6}, Lmx/com/cfe/cfecontigo/App;->c(I[C[Ljava/lang/Object;)V

    aget-object v2, v6, v1

    check-cast v2, Ljava/lang/String;

    invoke-virtual {v2}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Ljava/lang/String;->concat(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v0

    .line 2848
    aget-object v2, v38, v5

    check-cast v2, [I

    aget v2, v2, v1

    mul-int v1, v2, v2

    const v4, 0x5d7ab79a

    mul-int/2addr v4, v2

    neg-int v4, v4

    or-int v6, v1, v4

    shl-int/2addr v6, v5

    xor-int/2addr v1, v4

    sub-int/2addr v6, v1

    const v1, -0x7f9dd97c

    mul-int/2addr v2, v1

    neg-int v1, v2

    not-int v1, v1

    sub-int/2addr v6, v1

    sub-int/2addr v6, v5

    const v1, -0x6bedfd1f

    or-int v2, v6, v1

    shl-int/2addr v2, v5

    xor-int/2addr v1, v6

    sub-int/2addr v2, v1

    shr-int/lit8 v1, v2, 0x1b

    xor-int/lit8 v4, v1, -0x3f

    and-int/lit8 v1, v1, -0x3f

    shl-int/2addr v1, v5

    add-int/2addr v4, v1

    .line 2901
    div-int/lit8 v4, v4, 0x20

    and-int/lit8 v1, v4, 0x1

    or-int/2addr v4, v5

    add-int/2addr v1, v4

    not-int v1, v1

    sub-int v1, v2, v1

    sub-int/2addr v1, v5

    const/16 v4, 0x11

    shr-int/2addr v2, v4

    const v4, 0xffff

    sub-int/2addr v2, v4

    const v4, 0x8000

    .line 2963
    div-int/2addr v2, v4

    or-int/lit8 v4, v2, 0x1

    shl-int/2addr v4, v5

    xor-int/2addr v2, v5

    sub-int/2addr v4, v2

    xor-int/2addr v1, v4

    neg-int v1, v1

    and-int/lit8 v2, v1, 0x9

    or-int/lit8 v1, v1, 0x9

    add-int/2addr v2, v1

    shr-int/lit8 v1, v2, 0x1d

    xor-int/lit8 v4, v1, -0xf

    and-int/lit8 v1, v1, -0xf

    const/4 v5, 0x1

    shl-int/2addr v1, v5

    add-int/2addr v4, v1

    .line 2995
    div-int/lit8 v4, v4, 0x8

    or-int/lit8 v1, v4, 0x1

    shl-int/2addr v1, v5

    xor-int/2addr v4, v5

    sub-int/2addr v1, v4

    add-int/lit8 v1, v1, 0x1

    neg-int v1, v1

    and-int/2addr v1, v2

    mul-int/lit16 v1, v1, 0x22a

    const v2, 0x5b4be

    .line 3051
    div-int/2addr v2, v1

    const/4 v1, 0x2

    .line 3058
    aget-object v4, v39, v1

    check-cast v4, [I

    const/4 v1, 0x0

    aget v4, v4, v1

    mul-int v1, v4, v4

    const v5, 0x439a857f

    mul-int/2addr v5, v4

    neg-int v5, v5

    and-int v6, v1, v5

    or-int/2addr v1, v5

    add-int/2addr v6, v1

    const v1, -0x5835216b

    mul-int/2addr v4, v1

    neg-int v1, v4

    not-int v1, v1

    sub-int/2addr v6, v1

    const v1, -0x35b9e863

    sub-int/2addr v6, v1

    shr-int/lit8 v1, v6, 0x18

    add-int/lit16 v1, v1, -0x1ff

    .line 3129
    div-int/lit16 v1, v1, 0x100

    xor-int/lit8 v4, v1, 0x1

    const/4 v5, 0x1

    and-int/2addr v1, v5

    shl-int/2addr v1, v5

    add-int/2addr v4, v1

    or-int v1, v6, v4

    shl-int/2addr v1, v5

    xor-int/2addr v4, v6

    sub-int/2addr v1, v4

    shr-int/lit8 v4, v6, 0xf

    const v6, 0x3ffff

    sub-int/2addr v4, v6

    const/high16 v6, 0x20000

    .line 3169
    div-int/2addr v4, v6

    and-int/lit8 v6, v4, 0x1

    or-int/2addr v4, v5

    add-int/2addr v6, v4

    xor-int/2addr v1, v6

    neg-int v1, v1

    xor-int/lit8 v4, v1, 0x8

    and-int/lit8 v1, v1, 0x8

    shl-int/2addr v1, v5

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x14

    or-int/lit16 v6, v1, -0x1fff

    shl-int/2addr v6, v5

    xor-int/lit16 v1, v1, -0x1fff

    sub-int/2addr v6, v1

    .line 3223
    div-int/lit16 v6, v6, 0x1000

    and-int/lit8 v1, v6, 0x1

    or-int/2addr v6, v5

    add-int/2addr v1, v6

    or-int/lit8 v6, v1, 0x1

    shl-int/2addr v6, v5

    xor-int/2addr v1, v5

    sub-int/2addr v6, v1

    neg-int v1, v6

    and-int/2addr v1, v4

    mul-int/lit16 v1, v1, 0x41b

    const v4, 0x77d410

    .line 3251
    div-int/2addr v4, v1

    add-int/2addr v2, v4

    const/4 v1, 0x2

    .line 3058
    aget-object v4, v40, v1

    check-cast v4, [I

    const/4 v1, 0x0

    aget v4, v4, v1

    mul-int v1, v4, v4

    const v5, 0x400a5722

    mul-int/2addr v5, v4

    neg-int v5, v5

    and-int v6, v1, v5

    or-int/2addr v1, v5

    add-int/2addr v6, v1

    const v1, 0x6563f7b0

    mul-int/2addr v4, v1

    neg-int v1, v4

    or-int v4, v6, v1

    const/4 v5, 0x1

    shl-int/2addr v4, v5

    xor-int/2addr v1, v6

    sub-int/2addr v4, v1

    const v1, -0x5dd0d6ef

    and-int v5, v4, v1

    or-int/2addr v1, v4

    add-int/2addr v5, v1

    shr-int/lit8 v1, v5, 0x18

    and-int/lit16 v4, v1, -0x1ff

    or-int/lit16 v1, v1, -0x1ff

    add-int/2addr v4, v1

    .line 3145
    div-int/lit16 v4, v4, 0x100

    add-int/lit8 v4, v4, 0x1

    xor-int v1, v5, v4

    and-int/2addr v4, v5

    const/4 v6, 0x1

    shl-int/2addr v4, v6

    add-int/2addr v1, v4

    shr-int/lit8 v4, v5, 0x16

    xor-int/lit16 v5, v4, -0x7ff

    and-int/lit16 v4, v4, -0x7ff

    shl-int/2addr v4, v6

    add-int/2addr v5, v4

    .line 3202
    div-int/lit16 v5, v5, 0x400

    and-int/lit8 v4, v5, 0x1

    or-int/2addr v5, v6

    add-int/2addr v4, v5

    xor-int/2addr v1, v4

    neg-int v1, v1

    and-int/lit8 v4, v1, 0x5

    const/4 v5, 0x5

    or-int/2addr v1, v5

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x1c

    and-int/lit8 v5, v1, -0x1f

    or-int/lit8 v1, v1, -0x1f

    add-int/2addr v5, v1

    const/16 v1, 0x10

    .line 3229
    div-int/2addr v5, v1

    xor-int/lit8 v1, v5, 0x1

    const/4 v6, 0x1

    and-int/2addr v5, v6

    shl-int/2addr v5, v6

    add-int/2addr v1, v5

    and-int/lit8 v5, v1, 0x1

    or-int/2addr v1, v6

    add-int/2addr v5, v1

    neg-int v1, v5

    and-int/2addr v1, v4

    mul-int/lit8 v1, v1, 0x4d

    const v4, 0xb5f9

    .line 3254
    div-int/2addr v4, v1

    add-int/2addr v2, v4

    const/4 v1, 0x2

    .line 3064
    aget-object v4, v41, v1

    check-cast v4, [I

    const/4 v1, 0x0

    aget v4, v4, v1

    mul-int v1, v4, v4

    const v5, 0x4dbbf6fc    # 3.9419072E8f

    mul-int/2addr v5, v4

    neg-int v5, v5

    not-int v5, v5

    sub-int/2addr v1, v5

    const/4 v5, 0x1

    sub-int/2addr v1, v5

    const v6, -0x625a3e8e

    mul-int/2addr v4, v6

    neg-int v4, v4

    or-int v6, v1, v4

    shl-int/2addr v6, v5

    xor-int/2addr v1, v4

    sub-int/2addr v6, v1

    const v1, -0x30f16c2f

    and-int v4, v6, v1

    or-int/2addr v1, v6

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x1c

    xor-int/lit8 v5, v1, -0x1f

    and-int/lit8 v1, v1, -0x1f

    const/4 v6, 0x1

    shl-int/2addr v1, v6

    add-int/2addr v5, v1

    const/16 v1, 0x10

    .line 3132
    div-int/2addr v5, v1

    and-int/lit8 v1, v5, 0x1

    or-int/2addr v5, v6

    add-int/2addr v1, v5

    not-int v1, v1

    sub-int v1, v4, v1

    sub-int/2addr v1, v6

    shr-int/lit8 v4, v4, 0x17

    and-int/lit16 v5, v4, -0x3ff

    or-int/lit16 v4, v4, -0x3ff

    add-int/2addr v5, v4

    .line 3158
    div-int/lit16 v5, v5, 0x200

    add-int/lit8 v5, v5, 0x1

    xor-int/2addr v1, v5

    neg-int v1, v1

    and-int/lit8 v4, v1, 0x1

    const/4 v5, 0x1

    or-int/2addr v1, v5

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x10

    const v6, 0x1ffff

    sub-int/2addr v1, v6

    const/high16 v6, 0x10000

    .line 3216
    div-int/2addr v1, v6

    or-int/lit8 v6, v1, 0x1

    shl-int/2addr v6, v5

    xor-int/2addr v1, v5

    sub-int/2addr v6, v1

    add-int/lit8 v6, v6, 0x1

    neg-int v1, v6

    and-int/2addr v1, v4

    mul-int/lit16 v1, v1, 0x61d

    const v4, 0xe4ddb

    .line 3257
    div-int/2addr v4, v1

    add-int/2addr v2, v4

    const/4 v1, 0x3

    .line 3062
    aget-object v4, v43, v1

    check-cast v4, [I

    const/4 v1, 0x0

    aget v4, v4, v1

    mul-int v1, v4, v4

    const v5, 0x3d9d829e

    mul-int/2addr v5, v4

    neg-int v5, v5

    xor-int v6, v1, v5

    and-int/2addr v1, v5

    const/4 v5, 0x1

    shl-int/2addr v1, v5

    add-int/2addr v6, v1

    const v1, 0x792b86b4

    mul-int/2addr v4, v1

    neg-int v1, v4

    not-int v1, v1

    sub-int/2addr v6, v1

    sub-int/2addr v6, v5

    const v1, 0xec6b791

    or-int v4, v6, v1

    shl-int/2addr v4, v5

    xor-int/2addr v1, v6

    sub-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x18

    and-int/lit16 v5, v1, -0x1ff

    or-int/lit16 v1, v1, -0x1ff

    add-int/2addr v5, v1

    .line 3135
    div-int/lit16 v5, v5, 0x100

    add-int/lit8 v5, v5, 0x1

    xor-int v1, v4, v5

    and-int/2addr v5, v4

    const/4 v6, 0x1

    shl-int/2addr v5, v6

    add-int/2addr v1, v5

    shr-int/lit8 v4, v4, 0xf

    const v5, -0x3ffff

    and-int/2addr v5, v4

    const v6, -0x3ffff

    or-int/2addr v4, v6

    add-int/2addr v5, v4

    const/high16 v4, 0x20000

    .line 3173
    div-int/2addr v5, v4

    or-int/lit8 v4, v5, 0x1

    const/4 v6, 0x1

    shl-int/2addr v4, v6

    xor-int/2addr v5, v6

    sub-int/2addr v4, v5

    xor-int/2addr v1, v4

    neg-int v1, v1

    or-int/lit8 v4, v1, 0x3

    shl-int/2addr v4, v6

    const/4 v5, 0x3

    xor-int/2addr v1, v5

    sub-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x19

    add-int/lit16 v1, v1, -0xff

    .line 3213
    div-int/lit16 v1, v1, 0x80

    or-int/lit8 v5, v1, 0x1

    shl-int/2addr v5, v6

    xor-int/2addr v1, v6

    sub-int/2addr v5, v1

    xor-int/lit8 v1, v5, 0x1

    and-int/2addr v5, v6

    shl-int/2addr v5, v6

    add-int/2addr v1, v5

    neg-int v1, v1

    and-int/2addr v1, v4

    mul-int/lit16 v1, v1, 0x6c6

    const v4, 0x3ce1ae

    .line 3259
    div-int/2addr v4, v1

    add-int/2addr v2, v4

    const/4 v1, 0x0

    .line 3063
    aget-object v4, v42, v1

    check-cast v4, [I

    aget v4, v4, v1

    mul-int v1, v4, v4

    const v5, 0x34d7fdc9

    mul-int/2addr v5, v4

    neg-int v5, v5

    not-int v5, v5

    sub-int/2addr v1, v5

    const/4 v5, 0x1

    sub-int/2addr v1, v5

    const v6, -0x2724db95

    mul-int/2addr v4, v6

    neg-int v4, v4

    and-int v6, v1, v4

    or-int/2addr v1, v4

    add-int/2addr v6, v1

    const v1, -0x77ad895c

    or-int v4, v6, v1

    shl-int/2addr v4, v5

    xor-int/2addr v1, v6

    sub-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0xf

    const v6, -0x3ffff

    xor-int/2addr v6, v1

    const v7, -0x3ffff

    and-int/2addr v1, v7

    shl-int/2addr v1, v5

    add-int/2addr v6, v1

    const/high16 v1, 0x20000

    .line 3114
    div-int/2addr v6, v1

    or-int/lit8 v1, v6, 0x1

    shl-int/2addr v1, v5

    xor-int/2addr v6, v5

    sub-int/2addr v1, v6

    and-int v5, v4, v1

    or-int/2addr v1, v4

    add-int/2addr v5, v1

    shr-int/lit8 v1, v4, 0x17

    add-int/lit16 v1, v1, -0x3ff

    .line 3150
    div-int/lit16 v1, v1, 0x200

    and-int/lit8 v4, v1, 0x1

    const/4 v6, 0x1

    or-int/2addr v1, v6

    add-int/2addr v4, v1

    xor-int v1, v5, v4

    neg-int v1, v1

    and-int/lit8 v4, v1, 0x1

    or-int/2addr v1, v6

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x11

    const v5, -0xffff

    xor-int/2addr v5, v1

    const v7, -0xffff

    and-int/2addr v1, v7

    shl-int/2addr v1, v6

    add-int/2addr v5, v1

    const v1, 0x8000

    .line 3206
    div-int/2addr v5, v1

    and-int/lit8 v1, v5, 0x1

    or-int/2addr v5, v6

    add-int/2addr v1, v5

    and-int/lit8 v5, v1, 0x1

    or-int/2addr v1, v6

    add-int/2addr v5, v1

    neg-int v1, v5

    and-int/2addr v1, v4

    mul-int/lit16 v1, v1, 0x7c4

    const v4, 0x1e1f44

    .line 3226
    div-int/2addr v4, v1

    add-int/2addr v2, v4

    const/4 v1, 0x3

    .line 3066
    aget-object v1, v46, v1

    check-cast v1, [I

    const/4 v4, 0x0

    aget v1, v1, v4

    mul-int v4, v1, v1

    const v5, 0x66b2b387

    mul-int/2addr v5, v1

    neg-int v5, v5

    not-int v5, v5

    sub-int/2addr v4, v5

    const/4 v5, 0x1

    sub-int/2addr v4, v5

    const v6, -0x70890603

    mul-int/2addr v1, v6

    neg-int v1, v1

    xor-int v6, v4, v1

    and-int/2addr v1, v4

    shl-int/2addr v1, v5

    add-int/2addr v6, v1

    const v1, -0x518714fc

    and-int v4, v6, v1

    or-int/2addr v1, v6

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x1a

    add-int/lit8 v1, v1, -0x7f

    .line 3119
    div-int/lit8 v1, v1, 0x40

    xor-int/lit8 v5, v1, 0x1

    const/4 v6, 0x1

    and-int/2addr v1, v6

    shl-int/2addr v1, v6

    add-int/2addr v5, v1

    and-int v1, v4, v5

    or-int/2addr v5, v4

    add-int/2addr v1, v5

    const/16 v5, 0x10

    shr-int/2addr v4, v5

    const v5, 0x1ffff

    sub-int/2addr v4, v5

    const/high16 v5, 0x10000

    .line 3140
    div-int/2addr v4, v5

    and-int/lit8 v5, v4, 0x1

    const/4 v6, 0x1

    or-int/2addr v4, v6

    add-int/2addr v5, v4

    xor-int/2addr v1, v5

    neg-int v1, v1

    xor-int/lit8 v4, v1, 0x5

    const/4 v5, 0x5

    and-int/2addr v1, v5

    shl-int/2addr v1, v6

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x17

    add-int/lit16 v1, v1, -0x3ff

    .line 3185
    div-int/lit16 v1, v1, 0x200

    add-int/lit8 v1, v1, 0x2

    neg-int v1, v1

    and-int/2addr v1, v4

    mul-int/lit16 v1, v1, 0x473

    const v4, 0x483681

    .line 3223
    div-int/2addr v4, v1

    add-int/2addr v2, v4

    const/4 v1, 0x2

    .line 3058
    aget-object v4, v47, v1

    check-cast v4, [I

    const/4 v1, 0x0

    aget v4, v4, v1

    mul-int v1, v4, v4

    const v5, 0x5b6548f5

    mul-int/2addr v5, v4

    neg-int v5, v5

    and-int v6, v1, v5

    or-int/2addr v1, v5

    add-int/2addr v6, v1

    const v1, -0x3f0cfdd1

    mul-int/2addr v4, v1

    neg-int v1, v4

    or-int v4, v6, v1

    const/4 v5, 0x1

    shl-int/2addr v4, v5

    xor-int/2addr v1, v6

    sub-int/2addr v4, v1

    const v1, -0x184c78bc

    and-int v5, v4, v1

    or-int/2addr v1, v4

    add-int/2addr v5, v1

    shr-int/lit8 v1, v5, 0x11

    const v4, -0xffff

    xor-int/2addr v4, v1

    const v6, -0xffff

    and-int/2addr v1, v6

    const/4 v6, 0x1

    shl-int/2addr v1, v6

    add-int/2addr v4, v1

    const v1, 0x8000

    .line 3112
    div-int/2addr v4, v1

    xor-int/lit8 v1, v4, 0x1

    and-int/2addr v4, v6

    shl-int/2addr v4, v6

    add-int/2addr v1, v4

    xor-int v4, v5, v1

    and-int/2addr v1, v5

    shl-int/2addr v1, v6

    add-int/2addr v4, v1

    shr-int/lit8 v1, v5, 0xf

    const v5, 0x3ffff

    sub-int/2addr v1, v5

    const/high16 v5, 0x20000

    .line 3134
    div-int/2addr v1, v5

    add-int/lit8 v1, v1, 0x1

    xor-int/2addr v1, v4

    neg-int v1, v1

    xor-int/lit8 v4, v1, 0x6

    const/4 v5, 0x6

    and-int/2addr v1, v5

    const/4 v5, 0x1

    shl-int/2addr v1, v5

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x13

    add-int/lit16 v1, v1, -0x3fff

    .line 3176
    div-int/lit16 v1, v1, 0x2000

    or-int/lit8 v6, v1, 0x1

    shl-int/2addr v6, v5

    xor-int/2addr v1, v5

    sub-int/2addr v6, v1

    xor-int/lit8 v1, v6, 0x1

    and-int/2addr v6, v5

    shl-int/2addr v6, v5

    add-int/2addr v1, v6

    neg-int v1, v1

    and-int/2addr v1, v4

    mul-int/lit16 v1, v1, 0x383

    const v4, 0x21fe0a

    .line 3222
    div-int/2addr v4, v1

    add-int/2addr v2, v4

    const/4 v1, 0x2

    .line 3058
    aget-object v4, v23, v1

    check-cast v4, [I

    const/4 v1, 0x0

    aget v4, v4, v1

    mul-int v1, v4, v4

    const v5, 0xf889821

    mul-int/2addr v5, v4

    neg-int v5, v5

    or-int v6, v1, v5

    const/4 v7, 0x1

    shl-int/2addr v6, v7

    xor-int/2addr v1, v5

    sub-int/2addr v6, v1

    const v1, 0x3edae6a7

    mul-int/2addr v4, v1

    neg-int v1, v4

    not-int v1, v1

    sub-int/2addr v6, v1

    sub-int/2addr v6, v7

    const v1, 0x4b5e5f10    # 1.4573328E7f

    or-int v4, v6, v1

    shl-int/2addr v4, v7

    xor-int/2addr v1, v6

    sub-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x10

    const v5, -0x1ffff

    xor-int/2addr v5, v1

    const v6, -0x1ffff

    and-int/2addr v1, v6

    shl-int/2addr v1, v7

    add-int/2addr v5, v1

    const/high16 v1, 0x10000

    .line 3124
    div-int/2addr v5, v1

    add-int/lit8 v5, v5, 0x1

    or-int v1, v4, v5

    shl-int/2addr v1, v7

    xor-int/2addr v5, v4

    sub-int/2addr v1, v5

    shr-int/lit8 v4, v4, 0x1d

    xor-int/lit8 v5, v4, -0xf

    and-int/lit8 v4, v4, -0xf

    shl-int/2addr v4, v7

    add-int/2addr v5, v4

    .line 3171
    div-int/lit8 v5, v5, 0x8

    xor-int/lit8 v4, v5, 0x1

    and-int/2addr v5, v7

    shl-int/2addr v5, v7

    add-int/2addr v4, v5

    xor-int/2addr v1, v4

    neg-int v1, v1

    xor-int/lit8 v4, v1, 0x5

    const/4 v5, 0x5

    and-int/2addr v1, v5

    shl-int/2addr v1, v7

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0xf

    const v5, -0x3ffff

    and-int/2addr v5, v1

    const v6, -0x3ffff

    or-int/2addr v1, v6

    add-int/2addr v5, v1

    const/high16 v1, 0x20000

    .line 3193
    div-int/2addr v5, v1

    and-int/lit8 v1, v5, 0x1

    const/4 v6, 0x1

    or-int/2addr v5, v6

    add-int/2addr v1, v5

    add-int/lit8 v1, v1, 0x1

    neg-int v1, v1

    and-int/2addr v1, v4

    mul-int/lit16 v1, v1, 0x1ca

    const v4, 0x153ec

    .line 3228
    div-int/2addr v4, v1

    add-int/2addr v2, v4

    const/4 v1, 0x2

    .line 3063
    aget-object v1, v26, v1

    check-cast v1, [I

    const/4 v4, 0x0

    aget v1, v1, v4

    mul-int v4, v1, v1

    const v5, 0xe364bff

    mul-int/2addr v5, v1

    neg-int v5, v5

    and-int v6, v4, v5

    or-int/2addr v4, v5

    add-int/2addr v6, v4

    const v4, 0x60feb415

    mul-int/2addr v1, v4

    neg-int v1, v1

    not-int v1, v1

    sub-int/2addr v6, v1

    const/4 v1, 0x1

    sub-int/2addr v6, v1

    const v4, -0x67edff9c

    or-int v5, v6, v4

    shl-int/2addr v5, v1

    xor-int/2addr v4, v6

    sub-int/2addr v5, v4

    shr-int/lit8 v4, v5, 0x1c

    or-int/lit8 v6, v4, -0x1f

    shl-int/2addr v6, v1

    xor-int/lit8 v4, v4, -0x1f

    sub-int/2addr v6, v4

    const/16 v4, 0x10

    .line 3117
    div-int/2addr v6, v4

    or-int/lit8 v4, v6, 0x1

    shl-int/2addr v4, v1

    xor-int/2addr v6, v1

    sub-int/2addr v4, v6

    or-int v6, v5, v4

    shl-int/2addr v6, v1

    xor-int v1, v5, v4

    sub-int/2addr v6, v1

    const/16 v1, 0x11

    shr-int/lit8 v1, v5, 0x11

    const v4, -0xffff

    and-int/2addr v4, v1

    const v5, -0xffff

    or-int/2addr v1, v5

    add-int/2addr v4, v1

    const v1, 0x8000

    .line 3163
    div-int/2addr v4, v1

    or-int/lit8 v1, v4, 0x1

    const/4 v5, 0x1

    shl-int/2addr v1, v5

    xor-int/2addr v4, v5

    sub-int/2addr v1, v4

    xor-int/2addr v1, v6

    neg-int v1, v1

    and-int/lit8 v4, v1, 0x9

    or-int/lit8 v1, v1, 0x9

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x17

    and-int/lit16 v5, v1, -0x3ff

    or-int/lit16 v1, v1, -0x3ff

    add-int/2addr v5, v1

    .line 3200
    div-int/lit16 v5, v5, 0x200

    and-int/lit8 v1, v5, 0x1

    const/4 v6, 0x1

    or-int/2addr v5, v6

    add-int/2addr v1, v5

    add-int/lit8 v1, v1, 0x1

    neg-int v1, v1

    and-int/2addr v1, v4

    mul-int/lit16 v1, v1, 0x17d

    const v4, 0x2e0b3

    .line 3231
    div-int/2addr v4, v1

    add-int/2addr v2, v4

    .line 3058
    aget-object v1, v3, v6

    check-cast v1, [I

    const/4 v3, 0x0

    aget v1, v1, v3

    mul-int v3, v1, v1

    const v4, 0x4f0d2878

    mul-int/2addr v4, v1

    neg-int v4, v4

    or-int v5, v3, v4

    shl-int/2addr v5, v6

    xor-int/2addr v3, v4

    sub-int/2addr v5, v3

    const v3, -0x69f9ea3e

    mul-int/2addr v1, v3

    neg-int v1, v1

    or-int v3, v5, v1

    shl-int/2addr v3, v6

    xor-int/2addr v1, v5

    sub-int/2addr v3, v1

    const v1, -0x7c10f6b7

    and-int v4, v3, v1

    or-int/2addr v1, v3

    add-int/2addr v4, v1

    shr-int/lit8 v1, v4, 0x1d

    xor-int/lit8 v3, v1, -0xf

    and-int/lit8 v1, v1, -0xf

    const/4 v5, 0x1

    shl-int/2addr v1, v5

    add-int/2addr v3, v1

    .line 3129
    div-int/lit8 v3, v3, 0x8

    add-int/lit8 v3, v3, 0x1

    and-int v1, v4, v3

    or-int/2addr v3, v4

    add-int/2addr v1, v3

    const/16 v3, 0x10

    shr-int/lit8 v3, v4, 0x10

    const v4, -0x1ffff

    xor-int/2addr v4, v3

    const v5, -0x1ffff

    and-int/2addr v3, v5

    const/4 v5, 0x1

    shl-int/2addr v3, v5

    add-int/2addr v4, v3

    const/high16 v3, 0x10000

    .line 3154
    div-int/2addr v4, v3

    add-int/lit8 v4, v4, 0x1

    xor-int/2addr v1, v4

    neg-int v1, v1

    and-int/lit8 v3, v1, 0x9

    or-int/lit8 v1, v1, 0x9

    add-int/2addr v3, v1

    shr-int/lit8 v1, v3, 0x15

    or-int/lit16 v4, v1, -0xfff

    const/4 v5, 0x1

    shl-int/2addr v4, v5

    xor-int/lit16 v1, v1, -0xfff

    sub-int/2addr v4, v1

    .line 3212
    div-int/lit16 v4, v4, 0x800

    and-int/lit8 v1, v4, 0x1

    or-int/2addr v4, v5

    add-int/2addr v1, v4

    and-int/lit8 v4, v1, 0x1

    or-int/2addr v1, v5

    add-int/2addr v4, v1

    neg-int v1, v4

    and-int/2addr v1, v3

    mul-int/lit16 v1, v1, 0x2b8

    const v3, -0x1cbd728

    .line 3250
    div-int/2addr v3, v1

    add-int/2addr v2, v3

    .line 2265
    invoke-virtual {v0, v2}, Ljava/lang/String;->substring(I)Ljava/lang/String;

    move-result-object v0

    move-object/from16 v1, v45

    invoke-static {v1, v0}, Lo/safeSizeOf;->checkSelfPermission(Landroid/content/Context;Ljava/lang/String;)I

    move-result v0

    if-eqz v0, :cond_3d94

    .line 3058
    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v2, 0x0

    new-array v3, v2, [Ljava/lang/Class;

    invoke-virtual {v0, v8, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v3, 0x0

    invoke-virtual {v0, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/Context;->getApplicationInfo()Landroid/content/pm/ApplicationInfo;

    move-result-object v0

    iget v0, v0, Landroid/content/pm/ApplicationInfo;->targetSdkVersion:I

    const/4 v4, 0x5

    add-int/2addr v0, v4

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v4

    new-array v5, v2, [Ljava/lang/Class;

    invoke-virtual {v4, v8, v5}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v4

    invoke-virtual {v4, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v4

    check-cast v4, Landroid/content/Context;

    invoke-virtual {v4}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v3

    invoke-virtual {v3}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    const v4, 0x7f1300b7

    invoke-virtual {v3, v4}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v3

    const/4 v4, 0x4

    invoke-virtual {v3, v2, v4}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/String;->length()I

    move-result v3

    add-int/lit16 v3, v3, 0x100

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v4

    new-array v5, v2, [Ljava/lang/Class;

    invoke-virtual {v4, v8, v5}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v2

    const/4 v4, 0x0

    invoke-virtual {v2, v4, v4}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/content/Context;

    invoke-virtual {v2}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v2

    invoke-virtual {v2}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v2

    const/4 v4, 0x5

    invoke-virtual {v2, v4}, Ljava/lang/String;->codePointAt(I)I

    move-result v2

    add-int/lit8 v2, v2, -0x6d

    int-to-char v2, v2

    const/4 v4, 0x1

    new-array v5, v4, [Ljava/lang/Object;

    invoke-static {v0, v3, v2, v5}, Lmx/com/cfe/cfecontigo/App;->a(IIC[Ljava/lang/Object;)V

    const/4 v0, 0x0

    aget-object v2, v5, v0

    check-cast v2, Ljava/lang/String;

    invoke-virtual {v2}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v0

    invoke-static {v1, v0}, Lo/safeSizeOf;->checkSelfPermission(Landroid/content/Context;Ljava/lang/String;)I

    move-result v0

    if-nez v0, :cond_3e74

    .line 3072
    :cond_3d94
    sget-object v0, Lat/bluesource/choicesdk/location/factory/FusedLocationProviderFactory;->Companion:Lat/bluesource/choicesdk/location/factory/FusedLocationProviderFactory$Companion;

    invoke-virtual {v0, v1}, Lat/bluesource/choicesdk/location/factory/FusedLocationProviderFactory$Companion;->getFusedLocationProviderClient(Landroid/content/Context;)Lat/bluesource/choicesdk/location/common/FusedLocationProviderClient;

    move-result-object v0

    invoke-interface {v0}, Lat/bluesource/choicesdk/location/common/FusedLocationProviderClient;->getLastLocation()Lat/bluesource/choicesdk/core/task/Task;

    move-result-object v0

    .line 3073
    new-instance v2, Lo/zzew$write;

    new-instance v3, Lo/zzev;

    move-object/from16 v4, p0

    invoke-direct {v3, v4}, Lo/zzev;-><init>(Lmx/com/cfe/cfecontigo/App;)V

    invoke-direct {v2, v3}, Lo/zzew$write;-><init>(Lo/Strings;)V

    check-cast v2, Lat/bluesource/choicesdk/core/task/listener/OnSuccessListener;

    invoke-interface {v0, v2}, Lat/bluesource/choicesdk/core/task/Task;->addOnSuccessListener(Lat/bluesource/choicesdk/core/task/listener/OnSuccessListener;)Lat/bluesource/choicesdk/core/task/Task;

    .line 3077
    invoke-static {v1}, Lo/zzmu;->RemoteActionCompatParcelizer(Landroid/content/Context;)Z

    move-result v0

    if-eqz v0, :cond_3e66

    .line 3078
    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    const/4 v1, 0x0

    new-array v2, v1, [Ljava/lang/Class;

    invoke-virtual {v0, v8, v2}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v0

    const/4 v2, 0x0

    invoke-virtual {v0, v2, v2}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Landroid/content/Context;

    invoke-virtual {v0}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v0

    invoke-virtual {v0}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v0

    const v2, 0x7f13009e

    invoke-virtual {v0, v2}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v0

    const/4 v2, 0x4

    invoke-virtual {v0, v1, v2}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v0, v1}, Ljava/lang/String;->codePointAt(I)I

    move-result v0

    add-int/lit8 v0, v0, -0x1f

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v2

    new-array v3, v1, [Ljava/lang/Class;

    invoke-virtual {v2, v8, v3}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v2

    const/4 v3, 0x0

    invoke-virtual {v2, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v2

    check-cast v2, Landroid/content/Context;

    invoke-virtual {v2}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v2

    invoke-virtual {v2}, Landroid/content/Context;->getPackageName()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/String;->length()I

    move-result v2

    add-int/lit16 v2, v2, 0x118

    invoke-static/range {v24 .. v24}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v5

    new-array v6, v1, [Ljava/lang/Class;

    invoke-virtual {v5, v8, v6}, Ljava/lang/Class;->getMethod(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;

    move-result-object v5

    invoke-virtual {v5, v3, v3}, Ljava/lang/reflect/Method;->invoke(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v3

    check-cast v3, Landroid/content/Context;

    invoke-virtual {v3}, Landroid/content/Context;->getApplicationContext()Landroid/content/Context;

    move-result-object v3

    invoke-virtual {v3}, Landroid/content/Context;->getResources()Landroid/content/res/Resources;

    move-result-object v3

    const v5, 0x7f1300bb

    invoke-virtual {v3, v5}, Landroid/content/res/Resources;->getString(I)Ljava/lang/String;

    move-result-object v3

    const/4 v5, 0x4

    invoke-virtual {v3, v1, v5}, Ljava/lang/String;->substring(II)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v3}, Ljava/lang/String;->length()I

    move-result v3

    add-int/lit8 v3, v3, -0x4

    int-to-char v3, v3

    const/4 v5, 0x1

    new-array v5, v5, [Ljava/lang/Object;

    invoke-static {v0, v2, v3, v5}, Lmx/com/cfe/cfecontigo/App;->a(IIC[Ljava/lang/Object;)V

    aget-object v0, v5, v1

    check-cast v0, Ljava/lang/String;

    invoke-virtual {v0}, Ljava/lang/String;->intern()Ljava/lang/String;

    move-result-object v0

    iput-object v0, v4, Lmx/com/cfe/cfecontigo/App;->read:Ljava/lang/String;

    .line 3079
    new-array v10, v1, [Ljava/lang/Object;

    invoke-static {}, Lcom/google/android/gms/internal/firebase-auth-api/zzrw;->RemoteActionCompatParcelizer()I

    move-result v6

    invoke-static {}, Lcom/google/android/gms/internal/firebase-auth-api/zzrw;->RemoteActionCompatParcelizer()I

    move-result v7

    invoke-static {}, Lcom/google/android/gms/internal/firebase-auth-api/zzrw;->RemoteActionCompatParcelizer()I

    move-result v9

    invoke-static {}, Lcom/google/android/gms/internal/firebase-auth-api/zzrw;->RemoteActionCompatParcelizer()I

    move-result v11

    const v8, 0x13143022

    const v5, -0x13143021

    invoke-static/range {v5 .. v11}, Lcom/google/firebase/messaging/FirebaseMessaging;->RemoteActionCompatParcelizer(IIIII[Ljava/lang/Object;I)Ljava/lang/Object;

    move-result-object v0

    check-cast v0, Lcom/google/firebase/messaging/FirebaseMessaging;

    invoke-virtual {v0}, Lcom/google/firebase/messaging/FirebaseMessaging;->MediaBrowserCompatCustomActionResultReceiver()Lcom/google/android/gms/tasks/Task;

    move-result-object v0

    new-instance v1, Lo/zzex;

    invoke-direct {v1, v4}, Lo/zzex;-><init>(Lmx/com/cfe/cfecontigo/App;)V

    invoke-virtual {v0, v1}, Lcom/google/android/gms/tasks/Task;->addOnCompleteListener(Lcom/google/android/gms/tasks/OnCompleteListener;)Lcom/google/android/gms/tasks/Task;

    return-void

    .line 3086
    :cond_3e66
    invoke-static {v1}, Lo/zzmu;->IconCompatParcelizer(Landroid/content/Context;)Z

    move-result v0

    if-eqz v0, :cond_3e74

    .line 3087
    new-instance v0, Lmx/com/cfe/cfecontigo/App$RemoteActionCompatParcelizer;

    invoke-direct {v0, v4}, Lmx/com/cfe/cfecontigo/App$RemoteActionCompatParcelizer;-><init>(Lmx/com/cfe/cfecontigo/App;)V

    .line 3097
    invoke-virtual {v0}, Ljava/lang/Thread;->start()V

    :cond_3e74
    return-void

    .line 2241
    :cond_3e75
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    const/4 v1, 0x3

    aget-object v1, v2, v1

    check-cast v1, [Ljava/lang/String;

    if-eqz v1, :cond_3e98

    .line 3097
    sget v2, Lmx/com/cfe/cfecontigo/App;->RatingCompat:I

    const/16 v3, 0x11

    add-int/2addr v2, v3

    rem-int/lit16 v3, v2, 0x80

    sput v3, Lmx/com/cfe/cfecontigo/App;->MediaBrowserCompatSearchResultReceiver:I

    const/4 v3, 0x2

    rem-int/2addr v2, v3

    const/4 v4, 0x0

    .line 2243
    :goto_3e8d
    array-length v2, v1

    if-ge v4, v2, :cond_3e98

    .line 2245
    aget-object v2, v1, v4

    invoke-interface {v0, v2}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v4, v4, 0x1

    goto :goto_3e8d

    :cond_3e98
    const/4 v0, 0x0

    .line 1492
    throw v0

    .line 1950
    :catch_3e9a
    new-instance v0, Ljava/lang/RuntimeException;

    .line 1952
    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    throw v0

    .line 1756
    :cond_3ea0
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    const/4 v1, 0x0

    .line 1766
    aget-object v2, v2, v1

    check-cast v2, [Ljava/lang/String;

    if-eqz v2, :cond_3eb8

    const/4 v4, 0x0

    .line 1773
    :goto_3ead
    array-length v1, v2

    if-ge v4, v1, :cond_3eb8

    .line 1778
    aget-object v1, v2, v4

    invoke-interface {v0, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v4, v4, 0x1

    goto :goto_3ead

    :cond_3eb8
    const/4 v0, 0x0

    .line 1785
    throw v0

    .line 1729
    :catch_3eba
    new-instance v0, Ljava/lang/RuntimeException;

    .line 1732
    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    throw v0

    .line 1579
    :catch_3ec0
    new-instance v0, Ljava/lang/RuntimeException;

    .line 1587
    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    throw v0

    .line 1420
    :catch_3ec6
    new-instance v0, Ljava/lang/RuntimeException;

    .line 1424
    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    throw v0

    .line 1478
    :cond_3ecc
    new-instance v0, Ljava/util/ArrayList;

    .line 1480
    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    const/4 v1, 0x3

    aget-object v1, v3, v1

    check-cast v1, [Ljava/lang/String;

    if-eqz v1, :cond_3ee4

    const/4 v4, 0x0

    .line 1486
    :goto_3ed9
    array-length v2, v1

    if-ge v4, v2, :cond_3ee4

    .line 1490
    aget-object v2, v1, v4

    invoke-interface {v0, v2}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v4, v4, 0x1

    goto :goto_3ed9

    .line 110
    :cond_3ee4
    new-instance v0, Ljava/lang/RuntimeException;

    .line 120
    invoke-static {v5}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    invoke-direct {v0, v1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    .line 128
    throw v0

    .line 1239
    :cond_3eee
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    const/4 v1, 0x2

    .line 1240
    aget-object v1, v3, v1

    check-cast v1, [Ljava/lang/String;

    if-eqz v1, :cond_3f06

    const/4 v2, 0x0

    .line 1248
    :goto_3efb
    array-length v3, v1

    if-ge v2, v3, :cond_3f06

    aget-object v3, v1, v2

    invoke-interface {v0, v3}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v2, v2, 0x1

    goto :goto_3efb

    .line 1252
    :cond_3f06
    new-instance v0, Ljava/lang/RuntimeException;

    .line 1260
    invoke-static {v4}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v1

    .line 1265
    invoke-direct {v0, v1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    .line 1275
    throw v0

    .line 1216
    :catch_3f10
    new-instance v0, Ljava/lang/RuntimeException;

    .line 1224
    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    throw v0

    .line 574
    :cond_3f16
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    const/4 v1, 0x1

    .line 582
    aget-object v1, v3, v1

    check-cast v1, [Ljava/lang/String;

    if-eqz v1, :cond_3f2e

    const/4 v4, 0x0

    .line 593
    :goto_3f23
    array-length v2, v1

    if-ge v4, v2, :cond_3f2e

    .line 600
    aget-object v2, v1, v4

    .line 603
    invoke-interface {v0, v2}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v4, v4, 0x1

    goto :goto_3f23

    :cond_3f2e
    const/4 v0, 0x0

    .line 607
    throw v0

    .line 558
    :catch_3f30
    new-instance v0, Ljava/lang/RuntimeException;

    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    .line 565
    throw v0

    :catchall_3f36
    move-exception v0

    .line 522
    invoke-virtual {v0}, Ljava/lang/Throwable;->getCause()Ljava/lang/Throwable;

    move-result-object v1

    if-eqz v1, :cond_3f3e

    throw v1

    :cond_3f3e
    throw v0

    .line 341
    :cond_3f3f
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    const/4 v1, 0x0

    .line 357
    aget-object v2, v3, v1

    check-cast v2, [Ljava/lang/String;

    if-eqz v2, :cond_3f57

    move v4, v1

    .line 364
    :goto_3f4c
    array-length v1, v2

    if-ge v4, v1, :cond_3f57

    .line 371
    aget-object v1, v2, v4

    invoke-interface {v0, v1}, Ljava/util/List;->add(Ljava/lang/Object;)Z

    add-int/lit8 v4, v4, 0x1

    goto :goto_3f4c

    :cond_3f57
    const/4 v0, 0x0

    .line 376
    throw v0

    .line 320
    :catch_3f59
    new-instance v0, Ljava/lang/RuntimeException;

    invoke-direct {v0}, Ljava/lang/RuntimeException;-><init>()V

    .line 325
    throw v0

    :catchall_3f5f
    move-exception v0

    .line 144
    invoke-virtual {v0}, Ljava/lang/Throwable;->getCause()Ljava/lang/Throwable;

    move-result-object v1

    if-eqz v1, :cond_3f67

    throw v1

    :cond_3f67
    throw v0

    .line 90
    :cond_3f68
    new-instance v0, Ljava/lang/RuntimeException;

    const-string v1, "838433062"

    invoke-direct {v0, v1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    .line 94
    throw v0

    .line 64
    :cond_3f70
    new-instance v0, Ljava/lang/RuntimeException;

    const-string v1, "1290157083"

    invoke-direct {v0, v1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V

    .line 73
    throw v0

    :array_3f78
    .array-data 2
        0x3842s
        0x3823s
        0x3d35s
        0x40c3s
        -0x68cfs
        -0x347s
        -0x3c9ds
        -0x2d1bs
        0x3fa5s
        -0xbcas
        -0x3415s
        -0x25cfs
        0x3733s
        -0x1249s
        -0x2d99s
        -0x3c57s
        0x2e9bs
        -0x1ad4s
        -0x2515s
        -0x349as
        0x2614s
        -0x22a2s
        -0x1eads
        -0xb72s
        0x1dbes
        -0x2529s
        -0x1623s
        -0x3e6s
        0x1513s
        -0x2dbds
    .end array-data

    :array_3f9a
    .array-data 2
        0x296as
        0x2909s
        -0x2a94s
        0x35d2s
        0x373s
        0x14fbs
        -0x499cs
        0x46a7s
        0x2e87s
        0x1c68s
        -0x4116s
        0x4e1cs
        0x260as
        0x5ees
        -0x5896s
        0x57acs
        0x3f91s
        0xd77s
        -0x5006s
        0x5f24s
        0x3725s
        0x3500s
    .end array-data

    :array_3fb4
    .array-data 2
        -0x6060s
        -0x2f68s
        0x7271s
        -0x1ee5s
        -0x6443s
        -0x28e4s
        -0x45bs
        -0x2342s
        -0x725cs
        0x3215s
        -0x1bf9s
        -0x1e32s
        -0x4c45s
        0x7984s
        0x727es
        -0x74bs
        -0x63d4s
        0x493s
    .end array-data

    :array_3fca
    .array-data 2
        0x1877s
        -0x7054s
        -0xb65s
        -0x24f5s
        0x561fs
        -0x7a3es
        0x250s
        -0x7f4bs
        -0xa56s
        0x7d6cs
        0x7649s
        0x14c8s
        -0x839s
        0x5142s
        0x722fs
        -0x36b0s
        -0x3f84s
        -0x436fs
    .end array-data

    :array_3fe0
    .array-data 2
        -0x5d23s
        -0x2f03s
        -0x20d5s
        -0x7466s
        0x47cds
        0x3789s
        0x2fc8s
        -0x3643s
        0x55c8s
        -0x6281s
        -0x7505s
        0x6e75s
        -0x4629s
        0x20c3s
        0x280as
        -0x279cs
        0x21c1s
        -0x9c5s
        0x649as
        0x63bcs
    .end array-data

    :array_3ff8
    .array-data 2
        -0x2065s
        0x734s
        -0x3192s
        0x2764s
        -0x16d5s
        0x5184s
        0x427es
        -0x79ecs
        -0x7e8es
        -0x69b1s
        -0x26f4s
        -0x7fc1s
        0x613cs
        -0x5e68s
        0x1fbfs
        0x18c8s
        0x41f7s
        -0x6f3ds
    .end array-data

    :array_400e
    .array-data 2
        0x38fas
        -0x37a7s
        -0xb64s
        -0x4e60s
        -0x22ees
        0x5bdds
        -0x6b80s
        -0x11fs
        0x236bs
        -0x5ab6s
        -0x20c6s
        -0x76f9s
        -0x6b3es
        -0x129fs
        0x79s
        -0x53b8s
    .end array-data

    :array_4022
    .array-data 2
        -0x2065s
        0x734s
        -0x3192s
        0x2764s
        -0x16d5s
        0x5184s
        0x427es
        -0x79ecs
        -0x7e8es
        -0x69b1s
        -0x26f4s
        -0x7fc1s
        0x613cs
        -0x5e68s
        0x1fbfs
        0x18c8s
        0x41f7s
        -0x6f3ds
    .end array-data

    :array_4038
    .array-data 2
        -0x23a4s
        -0x5c42s
        -0xb5fs
        -0x3757s
        0x6917s
        -0x2b31s
        0x2075s
        0x227ds
        0x1717s
        -0x49d8s
        -0x4a9es
        0x43c5s
        -0x79ecs
        -0x3334s
        0x6748s
        0x11e0s
    .end array-data

    :array_404c
    .array-data 2
        0x1c2es
        0x81bs
        -0x1249s
        -0x27c8s
        0x6045s
        -0x1d39s
        -0x3dfds
        -0x6a9cs
        -0xfb3s
        0x5ccbs
        0x7711s
        0x4fees
        -0x747s
        -0x4d1ds
    .end array-data

    :array_405e
    .array-data 2
        0x1703s
        -0x46fds
        -0x5435s
        0x4149s
        -0x491as
        0x5803s
        0x256s
        0x609s
        0x58d8s
        -0x31d5s
        -0x791cs
        -0x2621s
        -0x40cfs
        0x55fas
        0x412ds
        0x1a4ds
        -0x7defs
        -0x1230s
        0x655fs
        0x5cdbs
        0x34d0s
        0x1a3fs
        0x4285s
        -0x6757s
        -0x1a5bs
        0x6e9fs
        -0x4ce3s
        0x524cs
        0x108as
        -0x1045s
        0x590fs
        -0x565bs
        0x64f1s
        -0x2f29s
        -0x393cs
        0x2ed6s
        -0xcd2s
        -0xb2es
        0x13d9s
        0xbfes
        0x36c6s
        0x55ccs
        -0xc59s
        -0x7e9s
        0x6b0as
        -0x7a0s
        -0x3b7cs
        -0x569ds
        -0x29a1s
        -0x23abs
        0x5013s
        0x7ee2s
        -0x1ca2s
        0x6df0s
        -0x8cbs
        -0x6da5s
        0x267cs
        -0x764fs
        -0x6021s
        -0x184fs
        -0x27c2s
        0x3dbds
        0x2b95s
        -0x654s
        0x3aa8s
        -0x134s
        0x15f4s
        -0x79f5s
        -0x21b1s
        0x7357s
        -0x2a89s
        -0x7d4as
        -0x53c1s
        0x3a28s
        -0x48es
        -0x309ds
    .end array-data

    :array_40ae
    .array-data 2
        -0x10d8s
        -0x4b58s
        0x32c9s
        -0xa2es
        -0x17d5s
        0x1cas
        0x69e8s
        -0x556bs
        -0x2978s
        -0x39a5s
        0x4663s
        0x71bds
        0x1efas
        -0x1576s
        -0x5c6es
        0x7e6as
        0x41a4s
        -0x2a20s
        0x1fdds
        0x4dd9s
        0x2004s
        0x44d0s
        0x206cs
        0x4dd6s
        -0x607bs
        0x4be1s
        0x2d85s
        -0x2e05s
        0x5f46s
        -0x364ds
        0x6d03s
        -0x1e59s
        0x3ddfs
        0x26bbs
        -0x5cdfs
        -0x40acs
        -0x904s
        -0x4c22s
        -0x3fd4s
        -0x4c0bs
        -0x1a8fs
        0x6313s
        0x4ddes
        -0x24bas
        -0x76fas
        0x54cas
        0x514es
        0x7fffs
        -0x6ee1s
        -0x70d9s
    .end array-data

    :array_40e4
    .array-data 2
        0x64ebs
        -0x56e3s
        -0x1af5s
        -0x36cbs
        -0x4ad5s
        0x1969s
        -0x10ds
        0x51des
        -0x5e94s
        -0x7cd9s
        0x16b6s
        0x37cfs
        0x2af9s
        -0x1d26s
        0x6d89s
        -0x6225s
        0x7beas
        0x3c1cs
        0x2c0as
        -0x42ces
        -0x3112s
        -0x5974s
        0x79f7s
        0x17d5s
        -0x308as
        -0x7f07s
        -0x42des
        0x2a55s
        -0x2e21s
        -0x21a1s
        -0x5df4s
        0x10e6s
        -0x711bs
        0x4f2as
        -0x6b3es
        0x2711s
        -0x2f6s
        -0x1e56s
        0x5f6fs
        -0x1e79s
        0x7a2bs
        0x53cds
        -0x2998s
        -0x6b8s
        0x69bbs
        -0x16e4s
        -0xbe7s
        -0x1030s
        -0x6d32s
        -0x721fs
        0x1126s
        -0x610es
        0x666fs
        -0x6b92s
        -0x54cas
        -0x2abs
        -0x44d0s
        0x3ceds
        -0x2407s
        0xa1bs
        0x4cbfs
        0x3c30s
        0x6ab1s
        0x394fs
        -0x71d8s
        -0x6674s
    .end array-data

    :array_412a
    .array-data 2
        -0x3d92s
        0x5cf3s
        -0x7eb5s
        0x77f1s
        -0x385cs
        -0x62acs
        0x4fa5s
        -0x384s
        -0x7b40s
        -0x7702s
        0x50ads
        -0x2017s
        0x5c5es
        0x6d22s
        0x6ed6s
        -0x1e71s
        0x297ds
        -0x2964s
    .end array-data

    :array_4140
    .array-data 2
        0x7a00s
        -0x303bs
        -0x277ds
        -0x56c4s
        -0x3c5cs
        0x3e09s
        -0x2cc6s
        -0x274s
        0x476bs
        -0x7856s
        0x397bs
        -0x58d5s
        0x69as
        -0x2c61s
        0x4031s
        -0x24a9s
        -0x3f05s
        -0x7906s
        -0x5520s
        -0x4b7fs
        -0x26bs
        -0x2689s
        -0x7826s
        -0x521cs
        -0x606cs
        -0xc75s
        0x161cs
        -0xeebs
        -0x1e25s
        -0x4222s
        -0x1f39s
        -0x1a2cs
        -0x728fs
        -0x141ds
        -0x3b9bs
        0x38ces
        0x314fs
        0x9fs
        -0x704as
        0x8a1s
        0x4d4bs
        -0x6cbfs
    .end array-data
.end method
