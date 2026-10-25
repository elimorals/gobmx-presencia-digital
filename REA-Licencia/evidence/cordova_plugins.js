
  cordova.define('cordova/plugin_list', function(require, exports, module) {
    module.exports = [
      {
          "id": "cordova-plugin-network-information.Connection",
          "file": "plugins/cordova-plugin-network-information/www/Connection.js",
          "pluginId": "cordova-plugin-network-information",
        "clobbers": [
          "Connection"
        ]
        },
      {
          "id": "cordova-plugin-app-version.AppVersionPlugin",
          "file": "plugins/cordova-plugin-app-version/www/AppVersionPlugin.js",
          "pluginId": "cordova-plugin-app-version",
        "clobbers": [
          "cordova.getAppVersion"
        ]
        },
      {
          "id": "cordova-plugin-inappbrowser.inappbrowser",
          "file": "plugins/cordova-plugin-inappbrowser/www/inappbrowser.js",
          "pluginId": "cordova-plugin-inappbrowser",
        "clobbers": [
          "cordova.InAppBrowser.open"
        ]
        },
      {
          "id": "cordova-plugin-advanced-http.http",
          "file": "plugins/cordova-plugin-advanced-http/www/advanced-http.js",
          "pluginId": "cordova-plugin-advanced-http",
        "clobbers": [
          "cordova.plugin.http"
        ]
        },
      {
          "id": "phonegap-plugin-barcodescanner.BarcodeScanner",
          "file": "plugins/phonegap-plugin-barcodescanner/www/barcodescanner.js",
          "pluginId": "phonegap-plugin-barcodescanner",
        "clobbers": [
          "cordova.plugins.barcodeScanner"
        ]
        },
      {
          "id": "cordova-plugin-screen-orientation.screenorientation",
          "file": "plugins/cordova-plugin-screen-orientation/www/screenorientation.js",
          "pluginId": "cordova-plugin-screen-orientation",
        "clobbers": [
          "cordova.plugins.screenorientation"
        ]
        },
      {
          "id": "uk.co.workingedge.cordova.plugin.sqliteporter.sqlitePorter",
          "file": "plugins/uk.co.workingedge.cordova.plugin.sqliteporter/www/sqlitePorter.js",
          "pluginId": "uk.co.workingedge.cordova.plugin.sqliteporter",
        "clobbers": [
          "cordova.plugins.sqlitePorter"
        ]
        },
      {
          "id": "cordova-plugin-network-information.network",
          "file": "plugins/cordova-plugin-network-information/www/network.js",
          "pluginId": "cordova-plugin-network-information",
        "clobbers": [
          "navigator.connection",
          "navigator.network.connection"
        ]
        },
      {
          "id": "cordova-sqlcipher-adapter.SQLitePlugin",
          "file": "plugins/cordova-sqlcipher-adapter/www/SQLitePlugin.js",
          "pluginId": "cordova-sqlcipher-adapter",
        "clobbers": [
          "SQLitePlugin"
        ]
        },
      {
          "id": "cordova-plugin-file.DirectoryEntry",
          "file": "plugins/cordova-plugin-file/www/DirectoryEntry.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.DirectoryEntry"
        ]
        },
      {
          "id": "cordova-plugin-file.DirectoryReader",
          "file": "plugins/cordova-plugin-file/www/DirectoryReader.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.DirectoryReader"
        ]
        },
      {
          "id": "cordova-plugin-file.Entry",
          "file": "plugins/cordova-plugin-file/www/Entry.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.Entry"
        ]
        },
      {
          "id": "cordova-plugin-file.File",
          "file": "plugins/cordova-plugin-file/www/File.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.File"
        ]
        },
      {
          "id": "cordova-plugin-file.FileEntry",
          "file": "plugins/cordova-plugin-file/www/FileEntry.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.FileEntry"
        ]
        },
      {
          "id": "cordova-plugin-file.FileError",
          "file": "plugins/cordova-plugin-file/www/FileError.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.FileError"
        ]
        },
      {
          "id": "cordova-plugin-file.FileReader",
          "file": "plugins/cordova-plugin-file/www/FileReader.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.FileReader"
        ]
        },
      {
          "id": "cordova-plugin-file.FileSystem",
          "file": "plugins/cordova-plugin-file/www/FileSystem.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.FileSystem"
        ]
        },
      {
          "id": "cordova-plugin-file.FileUploadOptions",
          "file": "plugins/cordova-plugin-file/www/FileUploadOptions.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.FileUploadOptions"
        ]
        },
      {
          "id": "cordova-plugin-file.FileUploadResult",
          "file": "plugins/cordova-plugin-file/www/FileUploadResult.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.FileUploadResult"
        ]
        },
      {
          "id": "cordova-plugin-file.FileWriter",
          "file": "plugins/cordova-plugin-file/www/FileWriter.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.FileWriter"
        ]
        },
      {
          "id": "cordova-plugin-file.Flags",
          "file": "plugins/cordova-plugin-file/www/Flags.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.Flags"
        ]
        },
      {
          "id": "cordova-plugin-file.LocalFileSystem",
          "file": "plugins/cordova-plugin-file/www/LocalFileSystem.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.LocalFileSystem"
        ],
        "merges": [
          "window"
        ]
        },
      {
          "id": "cordova-plugin-file.Metadata",
          "file": "plugins/cordova-plugin-file/www/Metadata.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.Metadata"
        ]
        },
      {
          "id": "cordova-plugin-file.ProgressEvent",
          "file": "plugins/cordova-plugin-file/www/ProgressEvent.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.ProgressEvent"
        ]
        },
      {
          "id": "cordova-plugin-file.requestFileSystem",
          "file": "plugins/cordova-plugin-file/www/requestFileSystem.js",
          "pluginId": "cordova-plugin-file",
        "clobbers": [
          "window.requestFileSystem"
        ]
        },
      {
          "id": "cordova-plugin-statusbar.statusbar",
          "file": "plugins/cordova-plugin-statusbar/www/statusbar.js",
          "pluginId": "cordova-plugin-statusbar",
        "clobbers": [
          "window.StatusBar"
        ]
        },
      {
          "id": "cordova-plugin-advanced-http.cookie-handler",
          "file": "plugins/cordova-plugin-advanced-http/www/cookie-handler.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.dependency-validator",
          "file": "plugins/cordova-plugin-advanced-http/www/dependency-validator.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.error-codes",
          "file": "plugins/cordova-plugin-advanced-http/www/error-codes.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.global-configs",
          "file": "plugins/cordova-plugin-advanced-http/www/global-configs.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.helpers",
          "file": "plugins/cordova-plugin-advanced-http/www/helpers.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.js-util",
          "file": "plugins/cordova-plugin-advanced-http/www/js-util.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.local-storage-store",
          "file": "plugins/cordova-plugin-advanced-http/www/local-storage-store.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.lodash",
          "file": "plugins/cordova-plugin-advanced-http/www/lodash.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.messages",
          "file": "plugins/cordova-plugin-advanced-http/www/messages.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.ponyfills",
          "file": "plugins/cordova-plugin-advanced-http/www/ponyfills.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.public-interface",
          "file": "plugins/cordova-plugin-advanced-http/www/public-interface.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.tough-cookie",
          "file": "plugins/cordova-plugin-advanced-http/www/umd-tough-cookie.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-advanced-http.url-util",
          "file": "plugins/cordova-plugin-advanced-http/www/url-util.js",
          "pluginId": "cordova-plugin-advanced-http"
        },
      {
          "id": "cordova-plugin-file.fileSystems",
          "file": "plugins/cordova-plugin-file/www/fileSystems.js",
          "pluginId": "cordova-plugin-file"
        },
      {
          "id": "cordova-plugin-file.isChrome",
          "file": "plugins/cordova-plugin-file/www/browser/isChrome.js",
          "pluginId": "cordova-plugin-file",
        "runs": true
        },
      {
          "id": "cordova-plugin-file.fileSystems-roots",
          "file": "plugins/cordova-plugin-file/www/fileSystems-roots.js",
          "pluginId": "cordova-plugin-file",
        "runs": true
        },
      {
          "id": "es6-promise-plugin.Promise",
          "file": "plugins/es6-promise-plugin/www/promise.js",
          "pluginId": "es6-promise-plugin",
        "runs": true
        },
      {
          "id": "cordova-plugin-file.fileSystemPaths",
          "file": "plugins/cordova-plugin-file/www/fileSystemPaths.js",
          "pluginId": "cordova-plugin-file",
        "merges": [
          "cordova"
        ],
        "runs": true
        },
      {
          "id": "cordova-plugin-file.androidFileSystem",
          "file": "plugins/cordova-plugin-file/www/android/FileSystem.js",
          "pluginId": "cordova-plugin-file",
        "merges": [
          "FileSystem"
        ]
        },
      {
          "id": "cordova-plugin-file.resolveLocalFileSystemURI",
          "file": "plugins/cordova-plugin-file/www/resolveLocalFileSystemURI.js",
          "pluginId": "cordova-plugin-file",
        "merges": [
          "window"
        ]
        }
    ];
    module.exports.metadata =
    // TOP OF METADATA
    {
      "cordova-plugin-advanced-http": "3.0.1",
      "cordova-plugin-app-version": "0.1.12",
      "cordova-plugin-file": "6.0.2",
      "cordova-plugin-inappbrowser": "4.0.0",
      "cordova-plugin-network-information": "2.0.2",
      "cordova-plugin-screen-orientation": "3.0.2",
      "cordova-plugin-statusbar": "2.4.3",
      "cordova-sqlcipher-adapter": "0.5.1",
      "es6-promise-plugin": "4.2.2",
      "phonegap-plugin-barcodescanner": "8.1.0",
      "uk.co.workingedge.cordova.plugin.sqliteporter": "1.1.1"
    };
    // BOTTOM OF METADATA
    });
    